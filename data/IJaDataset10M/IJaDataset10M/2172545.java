package org.yournamehere.client.person;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 * @author Mufasa
 */
public interface PersonServiceAsync {

    public abstract void loadPerson(Person p, AsyncCallback<Person> asyncCallback);
}
