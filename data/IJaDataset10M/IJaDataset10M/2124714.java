package org.jiaho.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Manuel Martins
 */
public interface jiahoServerServiceAsync {

    public void connect(String username, String password, String host, int port, boolean newAccount, AsyncCallback callback);

    public void isLogin(String conID, AsyncCallback callback);

    public void isClose(String conID, AsyncCallback callback);

    public void disconnect(String conID, AsyncCallback callback);

    public void getRoster(String conID, AsyncCallback callback);

    public void getPresence(String JID, String conID, AsyncCallback callback);

    public void getContactsCount(String conID, AsyncCallback callback);

    public void getNewContacts(String conID, AsyncCallback callback);

    public void sendPresence(String conID, String presenceMode, String presenceMessage, AsyncCallback callback);

    public void getChatMessages(String conID, AsyncCallback callback);

    public void sendChatMessage(String conID, String messageBody, String messageSubject, String messageTo, AsyncCallback callback);

    public void addContact(String conID, String JID, AsyncCallback callback);

    public void remContact(String conID, String JID, AsyncCallback callback);
}
