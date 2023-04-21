package net.sf.vgap4.assistant.models.helpers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;
import net.sf.vgap4.assistant.models.Waypoint;

public class WaypointList extends Vector<Waypoint> {

    private static final long serialVersionUID = -630098705153486674L;

    public String dump() {
        this.orderAndFilter();
        StringBuffer buffer = new StringBuffer("[WaypointList");
        Iterator<Waypoint> wit = this.iterator();
        while (wit.hasNext()) {
            Waypoint point = wit.next();
            buffer.append("turn:").append(point.getTurn());
            buffer.append("-order:").append(point.getOrder());
            buffer.append("-type:").append(point.getType()).append('\n');
        }
        buffer.append("]");
        return buffer.toString();
    }

    public void orderAndFilter() {
        Collections.sort(this, new WaypointComparator());
        if (size() > 1) {
            boolean currentFound = false;
            for (int pointer = this.size() - 1; pointer > 0; pointer--) {
                Waypoint last = this.get(pointer);
                Waypoint previous = this.getPrevious(pointer);
                if (null == last) remove(pointer);
                if (null == previous) continue;
                if ((0 == last.getLocation().x) && (0 == last.getLocation().y)) {
                    remove(last);
                    continue;
                }
                if (previous.checkLocation(last.getLocation())) {
                    if (Waypoint.WAYPOINT_PASTWAYPOINT.equals(last.getType())) {
                        previous.setType(Waypoint.WAYPOINT_PASTWAYPOINT);
                        this.remove(pointer);
                    }
                    if (Waypoint.WAYPOINT_CURRENTWAYPOINT.equals(last.getType())) {
                        previous.setType(Waypoint.WAYPOINT_CURRENTWAYPOINT);
                        this.remove(pointer);
                        currentFound = true;
                    }
                    if (Waypoint.WAYPOINT_FUTUREWAYPOINT.equals(last.getType())) {
                        previous.setType(Waypoint.WAYPOINT_FUTUREWAYPOINT);
                        this.remove(pointer);
                    }
                    continue;
                }
                if (currentFound) {
                    previous.setType(Waypoint.WAYPOINT_PASTWAYPOINT);
                    continue;
                }
                if (Waypoint.WAYPOINT_CURRENTWAYPOINT.equals(last.getType())) {
                    currentFound = true;
                    previous.setType(Waypoint.WAYPOINT_PASTWAYPOINT);
                    continue;
                }
                if (Waypoint.WAYPOINT_CURRENTWAYPOINT.equals(previous.getType())) {
                    last.setType(Waypoint.WAYPOINT_FUTUREWAYPOINT);
                    currentFound = true;
                }
            }
        }
    }

    private Waypoint getPrevious(int index) {
        if (index == 0) return null;
        Waypoint element = this.get(index - 1);
        if (null == element) return getPrevious(index); else return element;
    }
}

class WaypointComparator implements Comparator<Waypoint> {

    public int compare(Waypoint o1, Waypoint o2) {
        if ((o1).getTurn() == (o2).getTurn()) {
            if ((o1).getOrder() == (o2).getOrder()) {
                if (Waypoint.WAYPOINT_PASTWAYPOINT.equals((o1).getType())) {
                    if (Waypoint.WAYPOINT_PASTWAYPOINT.equals((o2).getType())) return 0; else return -1;
                }
                if (Waypoint.WAYPOINT_CURRENTWAYPOINT.equals((o1).getType())) {
                    if (Waypoint.WAYPOINT_PASTWAYPOINT.equals((o2).getType())) return 1;
                    if (Waypoint.WAYPOINT_FUTUREWAYPOINT.equals((o2).getType())) return -1;
                    return 0;
                }
                if (Waypoint.WAYPOINT_FUTUREWAYPOINT.equals((o1).getType())) {
                    if (Waypoint.WAYPOINT_FUTUREWAYPOINT.equals((o2).getType())) return 0; else return 1;
                }
            } else return (o1).getOrder() - (o2).getOrder();
        } else return (o1).getTurn() - (o2).getTurn();
        return 0;
    }
}
