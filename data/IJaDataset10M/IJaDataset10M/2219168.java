package org.client;

import net.customware.gwt.dispatch.shared.Result;

public class GetContactResult implements Result {

    private Contact contact;

    public GetContactResult() {
    }

    public GetContactResult(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}
