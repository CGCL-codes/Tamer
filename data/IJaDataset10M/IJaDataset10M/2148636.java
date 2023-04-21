package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.cygeek.tech.client.data.Country;
import java.util.Date;

public interface CountryServiceAsync {

    void getCountrys(AsyncCallback async);

    void addCountry(Country mCountry, boolean isNew, AsyncCallback async);

    void deleteCountry(String couCode, AsyncCallback async);

    void getCountry(String couCode, AsyncCallback async);
}
