package com.google.web.bindery.requestfactory.shared.impl;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;

/**
 * Extends SimpleProxyId with the correct parameterization to implement
 * EntityProxyId.
 * 
 * @param <P> the type of EntityProxy object the id describes
 */
public class SimpleEntityProxyId<P extends EntityProxy> extends SimpleProxyId<P> implements EntityProxyId<P> {

    /**
   * Construct an ephemeral id. May be called only from
   * {@link IdFactory#getId()}.
   */
    SimpleEntityProxyId(Class<P> proxyClass, int clientId) {
        super(proxyClass, clientId);
    }

    /**
   * Construct a stable id. May only be called from {@link IdFactory#getId()}
   */
    SimpleEntityProxyId(Class<P> proxyClass, String encodedAddress) {
        super(proxyClass, encodedAddress);
    }
}
