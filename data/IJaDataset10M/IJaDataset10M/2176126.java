package com.wrupple.muba.catalogs.client.module.services.logic.impl;

import java.util.ArrayList;
import com.google.gwt.event.shared.EventBus;
import com.wrupple.muba.catalogs.client.events.catalog.EntriesRetrivedEvent;
import com.wrupple.muba.catalogs.domain.CatalogEntry;
import com.wrupple.muba.common.client.application.DataCallback;

public class EntryRetrivingServiceHook extends DataCallback<CatalogEntry> {

    String catalogid;

    private EventBus eventBus;

    public static class List extends DataCallback<java.util.List<CatalogEntry>> {

        String catalogid;

        private EventBus eventBus;

        public List(String catalogid, EventBus eventBus) {
            super();
            assert catalogid != null;
            assert eventBus != null;
            this.catalogid = catalogid;
            this.eventBus = eventBus;
        }

        @Override
        public void execute() {
            if (result != null) {
                EntriesRetrivedEvent event = new EntriesRetrivedEvent(catalogid, result);
                eventBus.fireEvent(event);
            }
        }
    }

    public EntryRetrivingServiceHook(String catalogid, EventBus eventBus) {
        super();
        assert catalogid != null;
        assert eventBus != null;
        this.catalogid = catalogid;
        this.eventBus = eventBus;
    }

    @Override
    public void execute() {
        if (result != null) {
            ArrayList<CatalogEntry> list = new ArrayList<CatalogEntry>(1);
            list.add(result);
            fireeventAndStuff(catalogid, list);
        }
    }

    private void fireeventAndStuff(String catalog, java.util.List<CatalogEntry> entries) {
        EntriesRetrivedEvent event = new EntriesRetrivedEvent(catalog, entries);
        eventBus.fireEvent(event);
    }
}
