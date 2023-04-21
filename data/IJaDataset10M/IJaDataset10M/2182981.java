package com.aurorasoftworks.signal.examples.core;

public interface IAccount {

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    boolean getCanPlaceOrders();

    void setCanPlaceOrders(boolean canPlaceOrders);

    boolean getCanViewOrders();

    void setCanViewOrders(boolean canViewOrders);

    boolean getCanModifyOrders();

    void setCanModifyOrders(boolean canModifyOrders);

    void restore(IAccount account);
}
