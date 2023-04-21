package org.book4j.views.contact;

import org.book4j.application.Application;
import org.book4j.events.IEvent;
import org.book4j.events.IEventListener;
import org.book4j.services.ContactChangedEvent;
import org.book4j.views.FieldChangedEvent;
import org.book4j.views.IFieldChangedListener;

public class ContactPresenter implements IEventListener, IFieldChangedListener {

    ContactView view;

    public ContactPresenter(ContactView view) {
        this.view = view;
    }

    public void initialize() {
        this.view.addFieldChangedListener(this);
        Application.getEventDispatcher().register("topic://contact/changed", this);
    }

    public void handleEvent(IEvent event) {
        if (event.getEvent() instanceof ContactChangedEvent) {
            ContactChangedEvent e = (ContactChangedEvent) event.getEvent();
            this.view.setContact(e.getContact());
        }
    }

    public void fieldChanged(FieldChangedEvent e) {
        Application.getEventDispatcher().fireEvent("topic://contact/field/changed", e);
    }
}
