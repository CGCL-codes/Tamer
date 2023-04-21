package com.wrupple.muba.catalogs.client.events.catalog;

import java.util.List;
import com.google.gwt.event.shared.GwtEvent;
import com.wrupple.muba.catalogs.domain.CatalogEntry;

public class EntriesRetrivedEvent extends GwtEvent<EntriesRetrivedEventHandler> {

    private static Type<EntriesRetrivedEventHandler> type;

    private String catalog;

    private List<CatalogEntry> entries;

    public EntriesRetrivedEvent(String catalog2, List<CatalogEntry> entries2) {
        this.catalog = catalog2;
        this.entries = entries2;
    }

    public String getCatalog() {
        return catalog;
    }

    public List<CatalogEntry> getEntries() {
        return entries;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<EntriesRetrivedEventHandler> getAssociatedType() {
        return getType();
    }

    public static com.google.gwt.event.shared.GwtEvent.Type<EntriesRetrivedEventHandler> getType() {
        if (type == null) {
            type = new Type<EntriesRetrivedEventHandler>();
        }
        return type;
    }

    @Override
    protected void dispatch(EntriesRetrivedEventHandler handler) {
        handler.onEntriesRetrived(this);
    }
}
