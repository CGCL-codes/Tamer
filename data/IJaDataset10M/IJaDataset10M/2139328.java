package com.sun.j2ee.blueprints.lodgingsupplier;

/**
 * JNDI names of various EJBs
 */
public class JNDINames {

    public static final String LODGE_ORDER_EJB = "java:comp/env/ejb/local/lodgingsupplier/LodgingOrder";

    public static final String LODGE_QUEUECONNECTIONFACTORY = "java:comp/env/jms/lodging/QueueConnectionFactory";

    public static final String LODGE_QUEUE = "java:comp/env/jms/lodging/LodgingQueue";

    public static final String BROKER_SERVICE_NAME = "java:comp/env/service/WebServiceBroker";
}
