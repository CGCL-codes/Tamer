package com.factoria2.absolute.widgets.basic.event;

import com.google.gwt.event.shared.GwtEvent;

public class ItemSelectionEvent<T> extends GwtEvent<ItemSelectionHandler<T>> {

    public static enum Id {

        ITEM_SELECTED, ITEM_DESELECTED
    }

    public static <T> ItemSelectionEvent<T> fireItemDeselected(final HasItemSelectionHandlers<T> source, final T item) {
        ItemSelectionEvent<T> event = new ItemSelectionEvent<T>(Id.ITEM_DESELECTED, item);
        source.fireEvent(event);
        return event;
    }

    public static <T> ItemSelectionEvent<T> fireItemSelected(final HasItemSelectionHandlers<T> source, final T item) {
        ItemSelectionEvent<T> event = new ItemSelectionEvent<T>(Id.ITEM_SELECTED, item);
        source.fireEvent(event);
        return event;
    }

    private Id id;

    private T item;

    private ItemSelectionEvent(final Id id, final T item) {
        this.id = id;
        this.item = item;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Type<ItemSelectionHandler<T>> getAssociatedType() {
        return (Type) ItemSelectionHandler.TYPE;
    }

    public Id getId() {
        return id;
    }

    public T getItem() {
        return item;
    }

    @Override
    protected void dispatch(final ItemSelectionHandler<T> handler) {
        switch(id) {
            case ITEM_SELECTED:
                {
                    handler.onItemSelected(this);
                    break;
                }
            case ITEM_DESELECTED:
                {
                    handler.onItemDeselected(this);
                    break;
                }
        }
    }
}
