package com.sin.client.jsonprequestbuilders;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

public class JsonpRequestBuilderModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(WestJsonpRequestBulder.class).to(WestJsonpRequestBulderImpl.class).in(Singleton.class);
        bind(CenterJsonpRequestBulder.class).to(CenterJsonpRequestBulderImpl.class).in(Singleton.class);
    }
}
