package org.jboss.tutorial.service.bean;

import javax.ejb.Local;
import javax.ejb.Remote;
import org.jboss.ejb3.annotation.Management;
import org.jboss.ejb3.annotation.Service;

/**
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 61136 $
 */
@Service(objectName = ServiceOne.OBJECT_NAME)
@Local(ServiceOneLocal.class)
@Remote(ServiceOneRemote.class)
@Management(ServiceOneManagement.class)
public class ServiceOne implements ServiceOneLocal, ServiceOneRemote, ServiceOneManagement {

    /**
    * The ObjectName for {@link ServiceOne} 
    */
    public static final String OBJECT_NAME = "tutorial:service=ServiceOne";

    int attribute;

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getAttribute() {
        return this.attribute;
    }

    public String sayHello() {
        return "Hello from service One";
    }

    public void create() throws Exception {
        System.out.println("ServiceOne - Created");
    }

    public void start() throws Exception {
        System.out.println("ServiceOne - Started");
    }

    public void stop() {
        System.out.println("ServiceOne - Stopped");
    }

    public void destroy() {
        System.out.println("ServiceOne - Destroyed");
    }
}
