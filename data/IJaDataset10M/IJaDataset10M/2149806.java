package org.jboss.jmx.adaptor.snmp.test;

/**
 * MBean interface.
 */
public interface NotificationProducerServiceMBean extends org.jboss.system.ServiceMBean {

    /**
    * Sends a test Notification of type "V1"
    */
    void sendV1() throws java.lang.Exception;

    /**
    * Sends a test Notification of type "V2"
    */
    void sendV2() throws java.lang.Exception;

    /**
   * Sends a test Notification of type "V3"
   */
    void sendV3() throws java.lang.Exception;

    /**
  * Sends a test SNMP GET
  */
    void get();

    /**
  * Sends a test SNMP GETBULK
  */
    void getBulk();

    /**
  * Sends a test SNMP GETNEXT
  */
    void getNext();

    /**
  * Sends a test SNMP SET
  */
    void set();
}
