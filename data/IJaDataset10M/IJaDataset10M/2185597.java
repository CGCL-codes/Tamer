package peersim.core;

/**
 * A protocol that does nothing but knows everything.
 * It provides an interface which models a protocol that knows all nodes
 * in the network, i.e. the neighborhood set of this protocol is always the
 * whole node set. this protocol is also extremely cheap, in fact it
 * has no data fields.
 *
 * 不做任何事情，但知道一切信息的协议。
 * 它提供了一个接口，一个在网络中知道所有节点的协议的建模。比如这个协议的邻居集合总是整个
 * 节点集合，这个协议很简单，它没有数据域
 */
public final class OracleIdleProtocol implements Protocol, Linkable {

    /** Does nothing */
    public OracleIdleProtocol(String prefix) {
    }

    /** Returns <tt>this</tt> to maximize memory saving. It contains no fields.*/
    public Object clone() {
        return this;
    }

    /** This is an expensive operation, should not be used at all.
     * It returns false only if the given node is not in the current network.
     */
    public boolean contains(Node n) {
        final int len = Network.size();
        for (int i = 0; i < len; i++) {
            if (Network.node[i] == n) {
                return true;
            }
        }
        return false;
    }

    /** Unsupported operation */
    public boolean addNeighbor(Node n) {
        throw new UnsupportedOperationException();
    }

    /**
     * The neighborhood contains the node itself, ie it contains the loop
     * edge.
     */
    public Node getNeighbor(int i) {
        return Network.node[i];
    }

    public int degree() {
        return Network.size();
    }

    public void pack() {
    }

    public void onKill() {
    }

    public String toString() {
        return degree() + " [all the nodes of the overlay network]";
    }
}
