// Created by plusminus on 21:46:22 - 25.09.2008
package org.andnav.osm.views.util;

import org.andnav.osm.services.IOpenStreetMapTileProviderService;
import org.andnav.osm.tileprovider.IRegisterReceiver;
import org.andnav.osm.tileprovider.OpenStreetMapTileProviderBase;
import org.andnav.osm.tileprovider.OpenStreetMapTileProviderDirect;
import org.andnav.osm.tileprovider.renderer.OpenStreetMapRendererFactory;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nicolas Gramlich
 *
 */
public class OpenStreetMapTileProviderFactory implements OpenStreetMapViewConstants {

        private static final Logger logger = LoggerFactory.getLogger(OpenStreetMapTileProviderFactory.class);

	/**
	 * Get a tile provider.
	 * If a tile provider service exists then it will use the service,
	 * otherwise it'll use a direct tile provider that doesn't use a service.
	 * This can be used as the tile provider parameter in the {@link OpenStreetMapView} constructor.
	 * @param pContext
	 * @param pDownloadFinishedListener
	 * @return
	 */
	public static OpenStreetMapTileProviderBase getInstance(final Context aContext,
			final Handler aDownloadFinishedListener,
			final String aCloudmadeKey) {
		final Intent intent = new Intent(IOpenStreetMapTileProviderService.class.getName());
		final ResolveInfo ri = aContext.getPackageManager().resolveService(intent, 0);
		if (ri == null) {
			logger.info( "Service not found - using direct tile provider");
			final Context applicationContext = aContext.getApplicationContext();
			OpenStreetMapRendererFactory.setCloudmadeKey(aCloudmadeKey);
			return new OpenStreetMapTileProviderDirect(applicationContext);
		} else {
			logger.info( "Using tile provider service");
			return new OpenStreetMapTileProviderService(aContext, aDownloadFinishedListener);
			// XXX Perhaps we should pass the Intent or the class name (action) into
			//     this constructor since we do the same again in there.
			//     That will also give the option of specifying something else.
		}
	}

	/**
	 * This is a utility class with only static members.
	 */
	private OpenStreetMapTileProviderFactory() {
	}
}
