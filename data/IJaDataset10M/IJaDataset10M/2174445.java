package com.ohioedge.j2ee.api.org.prod;

import com.ohioedge.j2ee.api.prod.ShipmentMethodBean;
import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.model.ManagedTransientObject;
import java.sql.Timestamp;
import java.util.Collection;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ProductTypeBean.java	1.3.1 10/15/2002
 * @author Sandeep Dixit
 * @version 1.3.1
 */
public class ProductTypeBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(ShipmentMethodBean.class);

    private Integer productTypeID;

    private String name;

    private String description;

    private Integer organizationID;

    public Integer getProductTypeID() {
        return this.productTypeID;
    }

    public void setProductTypeID(Integer id) {
        this.productTypeID = id;
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

    public ProductTypeBean() {
    }

    public Integer getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Integer organizationID) {
        this.organizationID = organizationID;
    }
}
