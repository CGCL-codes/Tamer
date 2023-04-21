package com.plato.creasus.client;

import java.util.HashMap;
import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.plato.creasus.client.model.Entry;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    LoginInfo login(String requestUri);

    String addNewEntry(Entry entry);

    List<Entry> getEntryList();

    HashMap<String, Integer> addUser(String email);
}
