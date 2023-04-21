package com.aelitis.azureus.util;

import java.net.MalformedURLException;
import java.net.URL;
import com.aelitis.azureus.core.cnetwork.ContentNetwork;
import com.aelitis.azureus.core.cnetwork.ContentNetworkManagerFactory;

/**
 * @author TuxPaper
 * @created Dec 10, 2008
 *
 */
public class ContentNetworkUtils {

    /**
	 * Get content network url based on service id.
	 * @param cn
	 * @param serviceID
	 * @return null if service is not supported
	 *
	 * @since 4.0.0.5
	 */
    public static String getUrl(ContentNetwork cn, int serviceID) {
        try {
            if (!cn.isServiceSupported(serviceID)) {
                return null;
            }
            return cn.getServiceURL(serviceID);
        } catch (Throwable t) {
            return null;
        }
    }

    public static String getUrl(ContentNetwork cn, int serviceID, Object[] params) {
        try {
            if (!cn.isServiceSupported(serviceID)) {
                return null;
            }
            return cn.getServiceURL(serviceID, params);
        } catch (Throwable t) {
            return null;
        }
    }

    public static ContentNetwork getContentNetworkFromTarget(String target) {
        ContentNetwork cn = null;
        if (target != null && target.startsWith("ContentNetwork.")) {
            long networkID = Long.parseLong(target.substring(15));
            cn = ContentNetworkManagerFactory.getSingleton().getContentNetwork(networkID);
        }
        if (cn == null) {
            cn = ConstantsVuze.getDefaultContentNetwork();
        }
        return cn;
    }

    public static String getTarget(ContentNetwork cn) {
        return "ContentNetwork." + (cn == null ? ConstantsVuze.getDefaultContentNetwork().getID() : cn.getID());
    }

    public static void setSourceRef(String target, String sourceRef, boolean override) {
        setSourceRef(getContentNetworkFromTarget(target), sourceRef, override);
    }

    public static void setSourceRef(ContentNetwork cn, String sourceRef, boolean override) {
        if (cn == ConstantsVuze.getDefaultContentNetwork()) {
            return;
        }
        if (cn.isServiceSupported(ContentNetwork.SERVICE_AUTHORIZE)) {
            boolean authShown = false;
            Object oAuthShown = cn.getPersistentProperty(ContentNetwork.PP_AUTH_PAGE_SHOWN);
            if (oAuthShown instanceof Boolean) {
                authShown = ((Boolean) oAuthShown).booleanValue();
            }
            if (!authShown) {
                override = true;
            }
        }
        String old = (String) cn.getPersistentProperty(ContentNetwork.PP_SOURCE_REF);
        if (old == null || override) {
            if (sourceRef != null && sourceRef.startsWith("http")) {
                try {
                    URL url = new URL(sourceRef);
                    sourceRef = url.getHost() + url.getPath();
                } catch (MalformedURLException e) {
                }
            }
            cn.setPersistentProperty(ContentNetwork.PP_SOURCE_REF, sourceRef);
        }
    }
}
