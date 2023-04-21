package gui;

import java.util.Iterator;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.visual.VisualItem;
import util.Map;
import util.Waypoint;

public class MapLayout extends PlainLayout {

    @Override
    public void run(double frac) {
        Iterator iter = getVisualization().items(getGroup());
        while (iter.hasNext()) {
            VisualItem item = (VisualItem) iter.next();
            if (item instanceof Node) {
                Node node = (Node) item;
                Waypoint wp = (Waypoint) node.get(Map.NODE);
                setX(item, null, positionToPoint(wp.getPositionX()));
                setY(item, null, positionToPoint(wp.getPositionY()));
            } else if (item instanceof Edge) {
                Edge edge = (Edge) item;
                item.setSize(3);
            } else {
                System.err.println("Got non-node (" + item.getClass() + ") item.");
            }
        }
    }

    public MapLayout(String group) {
        super(group);
    }
}
