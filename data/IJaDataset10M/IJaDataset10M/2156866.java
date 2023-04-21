package net.sf.narad.core.node.serverStub;

import net.sf.narad.core.node.NodeInfo;

/**
 * This is a proxy for requesting services from server.
 * Nodes will interact with this to post request to the server. 
 * @author Narad_Dev
 *
 */
public interface ServerStub {

    public String registerNode(NodeInfo node);

    public boolean unregisterNode(NodeInfo node);

    public void requestSync(NodeInfo node);

    public void requestSyncCall();

    public void reportException();

    public void log();
}
