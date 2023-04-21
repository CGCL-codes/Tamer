package com.phamola.examples.client.messenger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.phamola.examples.client.contact.Contact;
import com.phamola.examples.client.contact.Message;

/**
 * The Asynchronous interface.
 */
public interface MessengerServiceAsync {

    void signIn(String name, AsyncCallback callback);

    void signOut(AsyncCallback callback);

    void sendMessage(Contact to, Message message, AsyncCallback callback);

    void getEvents(AsyncCallback callback);
}
