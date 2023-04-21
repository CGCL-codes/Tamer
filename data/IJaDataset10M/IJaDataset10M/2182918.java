package com.centraview.account.inventory;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface InventoryLocalHome extends EJBLocalHome {

    public InventoryLocal create() throws CreateException;
}
