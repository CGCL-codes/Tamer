package org.red5.server.api;

/**
 * Mark an object that can be bandwidth controlled.
 * <p>
 * A bw-controlled object has the bandwidth config property and a link to the
 * parent controllable object.
 * </p>
 * <p>
 * The parent controllable object acts as the bandwidth provider
 * for this object, thus generates a tree structure, in which
 * the <tt>null</tt> parent means the host. The next depth level
 * is the <tt>IClient</tt>. The following is
 * <tt>IStreamCapableConnection</tt>. The deepest level is
 * <tt>IClientStream</tt>. That is, bandwidth can be separately configured for
 * client stream or connection, or client or the whole application.
 * </p>
 * <p>
 * The summary of children's bandwidth can't exceed the parent's bandwidth
 * even though the children's bandwidth could be configured larger than the
 * parent's bandwidth.
 * </p>
 * 
 * @author The Red5 Project (red5@osflash.org)
 * @author Steven Gong (steven.gong@gmail.com)
 */
public interface IBWControllable {

    /**
	 * Return parent IFlowControllable object
	 * 
	 * @return	parent     Parent flow controllable
	 */
    IBWControllable getParentBWControllable();

    /**
	 * Return bandwidth configuration object. Bandwidth configuration
	 * allows you to set bandwidth size for audio, video and total amount.
	 * 
	 * @return	Bandwidth configuration object
	 */
    IBandwidthConfigure getBandwidthConfigure();

    /**
     * Setter for bandwidth configuration
     *
     * @param config Value to set for bandwidth configuration
     */
    void setBandwidthConfigure(IBandwidthConfigure config);
}
