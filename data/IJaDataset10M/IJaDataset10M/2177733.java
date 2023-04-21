package com.griddynamics.convergence.fabric.rmi;

/**
 * This listener can be used to monitor a ConnectionHandler.
 * (ie. to know when it finishes)
 * 
 * @date   07/10/2006 
 * @author lipe

 * @see	   com.griddynamics.convergence.demo.utils.rmi.ConnectionHandler
 */
public interface IConnectionHandlerListener {

    void connectionClosed();
}
