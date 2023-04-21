package com.wonebiz.crm.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.wonebiz.crm.client.entity.User;

@RemoteServiceRelativePath("customerServiceRPC")
public interface CustomerServiceRPC extends RemoteService {

    String validateCustomerName(String name);

    boolean ifReachMaxCustQty(String id);

    String validateContactPhone(String phone);

    String validateContactCell(String cell);

    User getOriginalOwnerByCustomerId(String customerId);
}
