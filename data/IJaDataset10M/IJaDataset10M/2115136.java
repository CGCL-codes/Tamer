package org.rip.ssl;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.rip.keystore.KeyStoreUtils;
import org.rip.keystore.RipTrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrustManagerUtils {

    private static Logger LOG = LoggerFactory.getLogger(TrustManagerUtils.class);

    public static TrustManager getDefaultTrustManager() {
        TrustManagerFactory tmf = getDefaultTrustManagerFactory();
        if (tmf == null) {
            return null;
        }
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        RipTrustManager tm = new RipTrustManager(defaultTrustManager);
        tm.setChain(defaultTrustManager.getAcceptedIssuers());
        return tm;
    }

    public static TrustManagerFactory getDefaultTrustManagerFactory() {
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(KeyStoreUtils.openDefaultKeyStore());
            TrustManager[] mgrs = tmf.getTrustManagers();
            for (TrustManager t : mgrs) {
                LOG.info("TrustManager:" + t.toString());
            }
            return tmf;
        } catch (Exception e) {
            LOG.error("Exception initializing TrustManager", e);
        }
        return null;
    }
}
