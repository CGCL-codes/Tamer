package com.teletalk.jserver.comm;

/**
 * Interface for endpoints used in communication. 
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta 2
 */
public interface EndPoint {

    /**
	 * Returns an EndPointIdentifier object identifying this end point.
	 *  
	 * @return an EndPointIdentifier object identifying this end point.
	 */
    public EndPointIdentifier getEndPointIdentifier();

    /**
    * Disconnects this EndPoint.
    * 
    * @since 2.0.1
    */
    public void disconnect();

    /**
    * Checks if this end point has estabished a link with the remote end.
    * 
    * @return <code>true</code> if a link has been established, otherwise <code>false</code>. 
    * 
    * @since 2.0.1
    */
    public boolean isLinkEstablished();
}
