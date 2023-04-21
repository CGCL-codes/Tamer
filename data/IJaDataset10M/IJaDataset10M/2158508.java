package com.wrupple.muba.catalogs.client.module.services.logic.impl;

import java.util.List;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wrupple.muba.catalogs.client.activity.CatalogActivity;
import com.wrupple.muba.catalogs.client.channels.CatalogEditingChannel;
import com.wrupple.muba.catalogs.client.module.services.logic.EntryUpdateService;
import com.wrupple.muba.catalogs.domain.CatalogEntry;
import com.wrupple.muba.common.client.application.DataCallback;
import com.wrupple.muba.common.client.application.SingletonListDataCallbackWrapper;
import com.wrupple.muba.common.shared.StateTransition;
import com.wrupple.vegetate.domain.CatalogResponseContract;

public class EntryUpdateServiceImpl extends AbstractCatalogService implements EntryUpdateService {

    private Provider<CatalogEditingChannel> serviceProvider;

    @Inject
    public EntryUpdateServiceImpl(Provider<CatalogEditingChannel> service) {
        super();
        this.serviceProvider = service;
    }

    @Override
    public void updateEntry(CatalogEntry data, String originalEntry, String catalogid, StateTransition<? extends JavaScriptObject> onDone, EventBus bus) {
        DataCallback<CatalogEntry> callback = new EntryCreationServiceHook(bus);
        StateTransition stupid = onDone;
        callback.hook(stupid);
        CatalogEditingChannel service = serviceProvider.get();
        StateTransition<List<CatalogEntry>> singletonSelector = new SingletonListDataCallbackWrapper<CatalogEntry>(callback);
        StateTransition<CatalogResponseContract> vegetateCallback = super.createChannelCallback(singletonSelector, catalogid, service);
        service.send(data, vegetateCallback, catalogid, CatalogActivity.UPDATE_ACTION, originalEntry);
    }
}
