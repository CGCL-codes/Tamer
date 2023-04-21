package org.jboss.tutorial.service_deployment_descriptor.bean;

/**
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 61136 $
 */
public interface ServiceTwoManagement {

    String sayHello();

    void start() throws Exception;

    void stop() throws Exception;
}
