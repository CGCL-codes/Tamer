package teach.multiagent07.net;

import java.util.Iterator;
import org.matsim.basic.v01.BasicNetImpl;

public class CANetwork extends BasicNetImpl {

    @Override
    public CANode newNode(String label) {
        CANode node = new CANode(label);
        return node;
    }

    @Override
    public CALink newLink(String label) {
        CALink link = new CALink(label);
        return link;
    }

    @Override
    public void connect() {
        Iterator i = links.values().iterator();
        while (i.hasNext()) {
            CALink link = (CALink) i.next();
            link.getToNode().addInLink(link);
            link.getFromNode().addOutLink(link);
        }
    }

    public void randomfill(double d) {
        for (Object l : links.values()) ((CALink) l).randomFill(d);
    }

    public void build() {
        for (Object l : links.values()) ((CALink) l).build();
    }

    public void move(int time) {
        for (Object l : links.values()) ((CALink) l).move(time);
        for (Object n : nodes.values()) ((CANode) n).move(time);
    }
}
