package org.apache.naming;

import java.util.Enumeration;
import javax.naming.Context;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * Represents a reference handler for a web service.
 *
 * @author Fabien Carrion
 */
public class HandlerRef extends Reference {

    /**
     * Default factory for this reference.
     */
    public static final String DEFAULT_FACTORY = org.apache.naming.factory.Constants.DEFAULT_HANDLER_FACTORY;

    /**
     * HandlerName address type.
     */
    public static final String HANDLER_NAME = "handlername";

    /**
     * Handler Classname address type.
     */
    public static final String HANDLER_CLASS = "handlerclass";

    /**
     * Handler Classname address type.
     */
    public static final String HANDLER_LOCALPART = "handlerlocalpart";

    /**
     * Handler Classname address type.
     */
    public static final String HANDLER_NAMESPACE = "handlernamespace";

    /**
     * Handler Classname address type.
     */
    public static final String HANDLER_PARAMNAME = "handlerparamname";

    /**
     * Handler Classname address type.
     */
    public static final String HANDLER_PARAMVALUE = "handlerparamvalue";

    /**
     * Handler SoapRole address type.
     */
    public static final String HANDLER_SOAPROLE = "handlersoaprole";

    /**
     * Handler PortName address type.
     */
    public static final String HANDLER_PORTNAME = "handlerportname";

    /**
     * Service Reference.
     * 
     * @param serviceClass Service class
     */
    public HandlerRef(String refname, String handlerClass) {
        this(refname, handlerClass, null, null);
    }

    /**
     * Service Reference.
     * 
     * @param serviceClass Service class
     */
    public HandlerRef(String refname, String handlerClass, String factory, String factoryLocation) {
        super(refname, factory, factoryLocation);
        StringRefAddr refAddr = null;
        if (refname != null) {
            refAddr = new StringRefAddr(HANDLER_NAME, refname);
            add(refAddr);
        }
        if (handlerClass != null) {
            refAddr = new StringRefAddr(HANDLER_CLASS, handlerClass);
            add(refAddr);
        }
    }

    /**
     * Retrieves the class name of the factory of the object to which this 
     * reference refers.
     */
    public String getFactoryClassName() {
        String factory = super.getFactoryClassName();
        if (factory != null) {
            return factory;
        } else {
            factory = System.getProperty(Context.OBJECT_FACTORIES);
            if (factory != null) {
                return null;
            } else {
                return DEFAULT_FACTORY;
            }
        }
    }

    /**
     * Return a String rendering of this object.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("HandlerRef[");
        sb.append("className=");
        sb.append(getClassName());
        sb.append(",factoryClassLocation=");
        sb.append(getFactoryClassLocation());
        sb.append(",factoryClassName=");
        sb.append(getFactoryClassName());
        Enumeration refAddrs = getAll();
        while (refAddrs.hasMoreElements()) {
            RefAddr refAddr = (RefAddr) refAddrs.nextElement();
            sb.append(",{type=");
            sb.append(refAddr.getType());
            sb.append(",content=");
            sb.append(refAddr.getContent());
            sb.append("}");
        }
        sb.append("]");
        return (sb.toString());
    }
}
