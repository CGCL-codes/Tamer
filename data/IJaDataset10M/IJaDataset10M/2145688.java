package com.unical.ae.gullivertravels.client;

import java.util.List;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.unical.ae.gullivertravels.dto.Account;
import com.unical.ae.gullivertravels.dto.Booking;
import com.unical.ae.gullivertravels.dto.Cruise;

public interface ManageBookingServiceAsync {

    void delete(Long id, AsyncCallback<Void> callback);

    void findAll(AsyncCallback<List<Booking>> callback);

    void findByAccount(Account account, AsyncCallback<List<Booking>> callback);

    void findByPriceEqual(Double price, AsyncCallback<List<Booking>> callback);

    void findByPriceGreater(Double price, AsyncCallback<List<Booking>> callback);

    void findByPriceLess(Double price, AsyncCallback<List<Booking>> callback);

    void getBooking(Long id, AsyncCallback<Booking> callback);

    void insert(Account account, Cruise cruise, Integer numberOfAdults, Integer numberOfChildren, Double price, AsyncCallback<Boolean> callback);

    void update(Account account, Cruise cruise, Long id, Integer numberOfAdults, Integer numberOfChildren, Double price, AsyncCallback<Boolean> callback);
}
