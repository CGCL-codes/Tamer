package rice.pastry.standard;

import rice.pastry.*;
import rice.pastry.messaging.*;
import rice.pastry.routing.*;
import rice.pastry.leafset.*;
import rice.pastry.security.*;

/**
 * An implementation of the standard Pastry routing algorithm.
 *
 * @version $Id: StandardRouter.java,v 1.1.1.1 2003/06/17 21:10:43 egs Exp $
 *
 * @author Andrew Ladd
 * @author Rongmei Zhang/Y.Charlie Hu
 */
public class StandardRouter implements MessageReceiver {

    private NodeId localId;

    private NodeHandle localHandle;

    private RoutingTable routeTable;

    private LeafSet leafSet;

    private PastrySecurityManager security;

    private Address routeAddress;

    /**
     * Constructor.
     *
     * @param rt the routing table.
     * @param ls the leaf set.
     */
    public StandardRouter(NodeHandle handle, RoutingTable rt, LeafSet ls, PastrySecurityManager sm) {
        localHandle = handle;
        localId = handle.getNodeId();
        routeTable = rt;
        leafSet = ls;
        security = sm;
        routeAddress = new RouterAddress();
    }

    /**
     * Gets the address of this component.
     *
     * @return the address.
     */
    public Address getAddress() {
        return routeAddress;
    }

    /**
     * Receive a message from a remote node.
     *
     * @param msg the message.
     */
    public void receiveMessage(Message msg) {
        if (msg instanceof RouteMessage) {
            RouteMessage rm = (RouteMessage) msg;
            if (rm.routeMessage(localHandle.getNodeId()) == false) receiveRouteMessage(rm);
        } else {
            throw new Error("message " + msg + " bounced at StandardRouter");
        }
    }

    /**
     * Receive and process a route message.
     *
     * @param msg the message.
     */
    public void receiveRouteMessage(RouteMessage msg) {
        NodeId target = msg.getTarget();
        int cwSize = leafSet.cwSize();
        int ccwSize = leafSet.ccwSize();
        int lsPos = leafSet.mostSimilar(target);
        if (lsPos == 0) msg.nextHop = localHandle; else if ((lsPos > 0 && (lsPos < cwSize || !leafSet.get(lsPos).getNodeId().clockwise(target))) || (lsPos < 0 && (-lsPos < ccwSize || leafSet.get(lsPos).getNodeId().clockwise(target)))) {
            NodeHandle handle = leafSet.get(lsPos);
            if (handle.isAlive() == false) {
                leafSet.remove(handle.getNodeId());
                receiveRouteMessage(msg);
                return;
            } else {
                msg.nextHop = handle;
            }
        } else {
            RouteSet rs = routeTable.getBestEntry(target);
            NodeHandle handle = null;
            if (rs == null || (handle = rs.closestNode()) == null) {
                handle = routeTable.bestAlternateRoute(target);
                if (handle == null) {
                    handle = leafSet.get(lsPos);
                    if (handle.isAlive() == false) {
                        leafSet.remove(handle.getNodeId());
                        receiveRouteMessage(msg);
                        return;
                    }
                } else {
                    NodeId.Distance altDist = handle.getNodeId().distance(target);
                    NodeId.Distance lsDist = leafSet.get(lsPos).getNodeId().distance(target);
                    if (lsDist.compareTo(altDist) < 0) {
                        handle = leafSet.get(lsPos);
                        if (handle.isAlive() == false) {
                            leafSet.remove(handle.getNodeId());
                            receiveRouteMessage(msg);
                            return;
                        }
                    }
                }
            } else {
                checkForRouteTableHole(msg, handle);
            }
            msg.nextHop = handle;
        }
        msg.setPrevNode(localHandle);
        localHandle.receiveMessage(msg);
    }

    /**
     * checks to see if the previous node along the path was missing a RT entry
     * if so, we send the previous node the corresponding RT row to patch the hole
     *
     * @param msg the RouteMessage being routed
     * @param handle the next hop handle
     */
    private void checkForRouteTableHole(RouteMessage msg, NodeHandle handle) {
        if (msg.getPrevNode() == null) return;
        NodeId prevId = msg.getPrevNode().getNodeId();
        NodeId key = msg.getTarget();
        int diffDigit;
        if ((diffDigit = prevId.indexOfMSDD(key, RoutingTable.baseBitLength())) == localId.indexOfMSDD(key, RoutingTable.baseBitLength())) {
            RouteSet[] row = routeTable.getRow(diffDigit);
            BroadcastRouteRow brr = new BroadcastRouteRow(localHandle, row);
            NodeHandle prevNode = security.verifyNodeHandle(msg.getPrevNode());
            if (prevNode.isAlive()) prevNode.receiveMessage(brr);
        }
    }
}
