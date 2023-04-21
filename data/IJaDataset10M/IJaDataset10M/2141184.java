package org.jaffa.modules.scheduler.services.configdomain;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jaffa.modules.scheduler.services.configdomain package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jaffa.modules.scheduler.services.configdomain
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JndiContext }
     * 
     */
    public JndiContext createJndiContext() {
        return new JndiContext();
    }

    /**
     * Create an instance of {@link SchedulerConfig }
     * 
     */
    public SchedulerConfig createSchedulerConfig() {
        return new SchedulerConfig();
    }

    /**
     * Create an instance of {@link Param }
     * 
     */
    public Param createParam() {
        return new Param();
    }

    /**
     * Create an instance of {@link Task }
     * 
     */
    public Task createTask() {
        return new Task();
    }

    /**
     * Create an instance of {@link Config }
     * 
     */
    public Config createConfig() {
        return new Config();
    }
}
