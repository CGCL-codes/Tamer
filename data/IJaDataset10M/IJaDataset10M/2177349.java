package org.ugr.bluerose;

/**
 * Interface for the reception of asynchronous replies
 *
 * @author Carlos Rodriguez Dominguez
 * @date 15-11-2009
 */
public interface AsyncMethodCallback {

    /**
 	* Callback method that is called when an specific asynchronous
 	* reply is received
 	*
 	* @param message Received reply message
 	*/
    public void callback(java.util.Vector<Byte> message);
}
