package br.org.articulus.client.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Interface to execute actions of User
 * @author anderson
 * @version 1.0
 */
public interface UserServiceAsync {

    @SuppressWarnings("unchecked")
    void returnList(AsyncCallback callback);

    @SuppressWarnings("unchecked")
    void removeUser(int id, AsyncCallback callback);
}
