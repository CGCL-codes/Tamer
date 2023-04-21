package overlaysim.protocol.overlay.codhtsimple;

import overlaysim.message.Message;
import overlaysim.message.NetworkMessage;
import overlaysim.protocol.Protocol;
import overlaysim.protocol.overlay.GuidTools;
import overlaysim.protocol.overlay.dhtlayer.RoutePath;

public class DeliverMessage extends NetworkMessage {

    public NetworkMessage msg;

    public RoutePath rp;

    public int to_dest;

    public DeliverMessage(int my_node_id, int src, int dest, int to_d, NetworkMessage m) {
        super(Message.CHORD_NETWORK_MESSAGE, my_node_id, Protocol.PROTOCOL_OVERLAY_CHORD, src, dest);
        to_dest = to_d;
        msg = m;
    }

    public DeliverMessage(int my_node_id, int src, int dest, RoutePath r, NetworkMessage m) {
        super(Message.CHORD_NETWORK_MESSAGE, my_node_id, Protocol.PROTOCOL_OVERLAY_CHORD, src, dest);
        rp = r;
        msg = m;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("DeliverMessage (");
        result.append(super.toString());
        result.append("to_dest=");
        result.append(to_dest);
        result.append("\nmsg=");
        result.append(msg);
        result.append(")");
        return result.toString();
    }
}
