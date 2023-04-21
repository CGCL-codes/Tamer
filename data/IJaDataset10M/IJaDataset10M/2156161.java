package org.mobicents.tools.sip.balancer;

import java.rmi.Remote;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * </p>
 * 
 * @author M. Ranganathan
 * @author baranowb 
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A> 
 *
 */
public interface NodeRegister extends Remote {

    public SIPNode getNextNode() throws IndexOutOfBoundsException;

    public SIPNode stickSessionToNode(String callID, SIPNode node);

    public SIPNode getGluedNode(String callID);

    public SIPNode[] getAllNodes();

    public void unStickSessionFromNode(String callID);

    public void handlePingInRegister(ArrayList<SIPNode> ping);

    public void forceRemovalInRegister(ArrayList<SIPNode> ping);

    public boolean isSIPNodePresent(String host, int port, String transportParam, String version);

    public SIPNode getNode(String host, int port, String transportParam, String version);

    public void jvmRouteSwitchover(String fromJvmRoute, String toJvmRoute);

    public String getLatestVersion();
}
