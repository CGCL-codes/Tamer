package org.wiw.client;

import java.util.List;
import org.wiw.client.model.Person;
import org.wiw.client.model.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MapServiceAsync {

    void greetServer(String input, AsyncCallback<String> callback);

    void getCurrentUser(AsyncCallback<Person> callback);

    void updatePerson(long id, String color, AsyncCallback<Void> callback);

    void addPlace(double latitude, double longitude, Person person, String when, String description, AsyncCallback<Place> callback);

    void getAllPlaces(AsyncCallback<List<Place>> callback);

    void movePlace(String id, double latitude, double longitude, AsyncCallback<Void> callback);

    void editPlace(String id, String when, String description, AsyncCallback<Void> callback);

    void removePlace(String id, AsyncCallback<Void> callback);
}
