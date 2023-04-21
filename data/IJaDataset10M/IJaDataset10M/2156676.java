package org.gruposp2p.aula.gwt.client.event;

import org.gruposp2p.aula.gwt.client.model.Itemcalificable;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ItemcalificableChangeEvent extends GwtEvent {

    private static final GwtEvent.Type TYPE = new GwtEvent.Type();

    private Itemcalificable itemcalificable;

    public static GwtEvent.Type getType() {
        return TYPE;
    }

    @Override
    public Type getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EventHandler handler) {
        ((ItemcalificableChangeHandler) handler).processItemcalificable(this);
    }

    public void setItemcalificable(Itemcalificable itemcalificable) {
        this.itemcalificable = itemcalificable;
    }

    public Itemcalificable getCompetence() {
        return itemcalificable;
    }
}
