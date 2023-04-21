package org.vexi.framework;

public interface IUserStore<U extends IUser> {

    U getUser(String username);

    /**
     * @param  user - user to add
     * @return true if user didn't already exist, and user has been added
     */
    boolean addUser(U user);

    /**
     * Inform the IUserStore that it should close, so it can ensure
     * all information is persisted. 
     */
    void close();
}
