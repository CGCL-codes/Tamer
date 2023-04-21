package br.usp.ime.dojo.client;

import java.util.Set;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ChatServiceAsync {

    public void checkNewMessages(String nome, String room, AsyncCallback<String> callback);

    public void sendMessage(String nome, String room, String message, AsyncCallback<String> callback);

    public void registerUser(String user, String room, AsyncCallback<Void> callback);

    public void getUsers(String room, AsyncCallback<Set<String>> callback);

    public void unregisterUser(String user, String room, AsyncCallback<Void> callback);

    public void createChatRoom(String room, AsyncCallback<Void> callback);
}
