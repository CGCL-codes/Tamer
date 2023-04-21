package mapEditor.utils;

import javax.swing.event.ChangeListener;
import java.util.Vector;
import java.util.Enumeration;

public class PointSet extends ValueType {

    private Vector<MapLocation> pointSet = new Vector<MapLocation>();

    public void addPointListener(ChangeListener changeListener) {
        for (Enumeration<MapLocation> e = pointSet.elements(); e.hasMoreElements(); ) {
            e.nextElement().add(changeListener);
        }
    }

    public void removePointListener(ChangeListener changeListener) {
        for (Enumeration<MapLocation> e = pointSet.elements(); e.hasMoreElements(); ) {
            e.nextElement().remove(changeListener);
        }
    }

    public void clearPointSet() {
        pointSet.clear();
    }

    public int numberOfPoints() {
        return (pointSet.size());
    }

    public void add(MapLocation location) {
        pointSet.add(location);
        FIRE_CHANGE_EVENT("ADD");
    }

    public MapLocation getMapLocation(int i) {
        return (pointSet.elementAt(i));
    }

    public void setMapLocation(MapLocation location, int i) {
        pointSet.setElementAt(location, i);
        FIRE_CHANGE_EVENT("MODIFY:" + i);
    }
}
