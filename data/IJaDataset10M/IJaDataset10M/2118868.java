package com.sin.server;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.sin.shared.Me;

public class MeProxyLocator extends Locator<Me, Long> {

    @Override
    public Me create(Class<? extends Me> clazz) {
        return new Me();
    }

    @Override
    public Me find(Class<? extends Me> clazz, Long id) {
        return null;
    }

    @Override
    public Class<Me> getDomainType() {
        return Me.class;
    }

    @Override
    public Long getId(Me domainObject) {
        return domainObject.getId();
    }

    @Override
    public Class<Long> getIdType() {
        return null;
    }

    @Override
    public Object getVersion(Me domainObject) {
        return domainObject.getVersion();
    }
}
