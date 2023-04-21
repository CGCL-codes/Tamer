package com.ohioedge.j2ee.api.org.proc;

import org.j2eebuilder.view.*;
import org.j2eebuilder.util.*;
import org.j2eebuilder.model.ManagedTransientObject;
import com.ohioedge.j2ee.api.org.proc.ejb.*;
import java.util.Collection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)InputTypeBean.java	1.3.1 10/15/2002
 * InputTypeBean is a java bean with the main function of facilitating
 * communication between JSPs and InputType EJB
 * @version 1.3.1
 */
public class InputTypeBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(InputTypeBean.class);

    private Integer inputTypeID;

    private String name;

    private String description;

    public Integer getInputTypeID() {
        return this.inputTypeID;
    }

    public void setInputTypeID(Integer id) {
        this.inputTypeID = id;
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

    public InputTypeBean() {
    }
}
