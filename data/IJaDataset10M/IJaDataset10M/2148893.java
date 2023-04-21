package com.ohioedge.j2ee.api.prod;

import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.model.ManagedTransientObject;
import java.sql.Timestamp;
import java.util.Collection;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ShipmentMethodBean.java	1.3.1 10/15/2002
 * @author Sandeep Dixit
 * @version 1.3.1
 */
public class ShipmentMethodBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(ShipmentMethodBean.class);

    private Integer shipmentMethodID;

    private String name;

    private String description;

    public Integer getShipmentMethodID() {
        return this.shipmentMethodID;
    }

    public void setShipmentMethodID(Integer id) {
        this.shipmentMethodID = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShipmentMethodBean() {
    }
}
