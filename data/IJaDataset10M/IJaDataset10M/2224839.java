package org.jboss.tutorial.partial_deployment_descriptor.bean;

/**
 * PartialXMLDD
 *
 * @author Jaikiran Pai
 * @version $Revision: $
 */
public interface PartialXMLDD {

    /**
    * Echo message
    * @param message
    * @return
    */
    String echoMessage(String message);

    /**
    * Change the message
    * @param message
    * @return
    */
    String changeMessage(String message);

    /**
    * Remove the bean
    */
    public void remove();
}
