package org.xsocket.connection;

/**
 * Adapter interface, which is implemented by xSocket extension such as http or multiplexed.
 * By implementing this interface the adapted handler will be exported by JMX instead of the adapter. 
 * Implementing this interface has only effects on exposing JMX artifacts. It does not have any effect
 * on the business functionality.
 * 
 * @author grro
 */
public interface IHandlerAdapter {

    /**
	 * returns the adapted handler
	 *  
	 * @return the adapted handler 
	 */
    public Object getAdaptee();
}
