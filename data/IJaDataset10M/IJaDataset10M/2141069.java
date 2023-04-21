package overlaysim.protocol.overlay.chord;

import java.math.BigInteger;
import java.util.LinkedList;
import overlaysim.protocol.overlay.GuidTools;
import overlaysim.protocol.overlay.OverlayPath;
import overlaysim.protocol.overlay.dhtlayer.RoutePath;

public class LookUpReq extends ChordMessage {

    public BigInteger guid;

    public String content;

    public int lookup_node_id;

    public int msg_type;

    public OverlayPath overlay_path = new OverlayPath();

    ;

    public LinkedList<RoutePath> routepaths = new LinkedList<RoutePath>();

    ;

    public LookUpReq(int my_node_id, int src, int dest, BigInteger id, int node_id) {
        super(my_node_id, src, dest);
        lookup_node_id = node_id;
        guid = id;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("LookUpReq (");
        result.append(super.toString());
        result.append("overlay_path (" + overlay_path.path);
        result.append(")");
        result.append("guid=");
        result.append(GuidTools.guid_to_string_40(guid));
        result.append(" content=");
        result.append(content);
        result.append(")");
        return result.toString();
    }
}
