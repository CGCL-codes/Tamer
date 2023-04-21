package org.scub.foundation.framework.gwt.module.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Point d'entré de l'application.
 * @author Goumard Stéphane (stephane.goumard@scub.net)
 */
public final class ScubFoundationModule implements EntryPoint {

    /**
     * Former une url pour les appels de service.
     * @param serviceName le nom du service.
     * @return GWT.getModuleBaseUrl.concat("/hander");
     */
    public static String getModuleBaseUrl(final String serviceName) {
        return GWT.getModuleBaseURL().concat("handler").concat(serviceName.indexOf("/") == 0 ? serviceName : "/" + serviceName);
    }

    /**
     * {@inheritDoc}
     */
    public void onModuleLoad() {
    }
}
