package com.ohioedge.j2ee.api.org.prod;

import org.j2eebuilder.view.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import com.ohioedge.j2ee.api.org.prod.ejb.*;
import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)ProductBean.java	1.3.1 10/15/2002
 * ProductBean is a java bean with the main function of facilitating
 * communication between JSPs and Prod EJB
 * @version 1.3.1
 */
public class ProductBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(ProductBean.class);

    private Integer productID;

    private Integer organizationID;

    private String name;

    private String description;

    private Integer productTypeID;

    private ProductTypeBean productTypeVO;

    public Integer getProductID() {
        return this.productID;
    }

    public void setProductID(Integer id) {
        this.productID = id;
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

    public Integer getOrganizationID() {
        return this.organizationID;
    }

    public void setOrganizationID(Integer id) {
        this.organizationID = id;
    }

    public Integer getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(Integer productTypeID) {
        this.productTypeID = productTypeID;
    }

    public ProductTypeBean getProductTypeVO() {
        return productTypeVO;
    }

    public void setProductTypeVO(ProductTypeBean productTypeVO) {
        this.productTypeVO = productTypeVO;
    }

    public ProductBean() {
    }
}
