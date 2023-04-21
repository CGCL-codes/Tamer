package com.producteev4j.services;

import com.producteev4j.services.version0.ProducteevServiceV0;
import com.producteev4j.transport.ProducteevTransport;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrey
 * Date: 14/05/11
 * TimeImpl: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class ProducteevServiceFactory {

    public static final String VERSION_0 = ProducteevServiceV0.class.getName();

    private ProducteevServiceFactory() {
    }

    public static ProducteevService getService(String version, ProducteevTransport transport) {
        if (VERSION_0.equals(version)) {
            return getVersio0(transport);
        } else {
            return getVersio0(transport);
        }
    }

    private static ProducteevService getVersio0(ProducteevTransport transport) {
        ProducteevServiceV0 serviceV0 = new ProducteevServiceV0();
        serviceV0.setTransport(transport);
        return serviceV0;
    }
}
