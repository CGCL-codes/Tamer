package net.sf.hibernate4gwt.sample.client.user;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserRemoteAsync {

    /**
	 * Returns the user list
	 */
    public void getUserList(AsyncCallback callback);
}
