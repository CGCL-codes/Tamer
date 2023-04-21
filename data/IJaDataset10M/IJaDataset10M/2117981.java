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
 * @(#)JunctionTypeBean.java	1.3.1 10/15/2002
 * JunctionTypeBean is a java bean with the main function of facilitating
 * communication between JSPs and JunctionType EJB
 * @version 1.3.1
 */
public class JunctionTypeBean extends org.j2eebuilder.model.ManagedTransientObjectImpl {

    private static transient LogManager log = new LogManager(JunctionTypeBean.class);

    public static final String AND = "AND";

    public static final String XOR = "XOR";

    public static final String OR = "OR";

    public static final String UNDEFINED = "UNDEFINED";

    private Integer junctionTypeID;

    private String name;

    private String description;

    public Integer getJunctionTypeID() {
        return this.junctionTypeID;
    }

    public void setJunctionTypeID(Integer id) {
        this.junctionTypeID = id;
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

    public JunctionTypeBean() {
    }
}
