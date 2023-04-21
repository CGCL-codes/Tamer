package org.pedia2.apps.demo.helloworld.client;

import java.util.List;
import org.pedia2.apps.demo.helloworld.client.model.City;
import org.pedia2.apps.demo.helloworld.client.model.Taxi;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {

    void greetServer(String input, AsyncCallback<String> callback);

    void createCity(String name, AsyncCallback<City> callback);

    void getCityList(AsyncCallback<List<City>> callback);

    void createTaxi(String taxiCompanyName, String taxiCompanyPhone, String cityId, AsyncCallback<Taxi> asyncCallback);

    void getAllTaxiList(AsyncCallback<List<Taxi>> asyncCallback);

    void getTaxiListForCity(String cityId, AsyncCallback<List<Taxi>> asyncCallback);

    void updateTaxi(String taxiId, String taxiCompanyName, String taxiCompanyPhone, AsyncCallback<Taxi> asyncCallback);

    void changeCityOfTaxi(String taxiId, String cityId, AsyncCallback<Taxi> asyncCallback);

    void getTaxi(String taxiId, AsyncCallback<Taxi> asyncCallback);

    void getCity(String cityId, AsyncCallback<City> asyncCallback);
}
