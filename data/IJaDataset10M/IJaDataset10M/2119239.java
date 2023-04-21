package com.p6spy.engine.test.jboss;

/**
 * Local home interface for test/HelloWorld.
 */
public interface HelloWorldLocalHome extends javax.ejb.EJBLocalHome {

    public static final String COMP_NAME = "java:comp/env/ejb/test/HelloWorldLocal";

    public static final String JNDI_NAME = "test/HelloWorldLocal";

    public com.p6spy.engine.test.jboss.HelloWorld create() throws javax.ejb.CreateException;
}
