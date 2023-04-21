package de.hpi.eworld.model.db.data.event;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;
import com.trolltech.qt.core.QPointF;
import de.hpi.eworld.model.db.data.GlobalPosition;

@Entity
@Table(name = "polygonlocations")
public class PolygonLocation extends Location {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6319440118425086537L;

    @CollectionOfElements
    @JoinTable(name = "polygonpoints", joinColumns = @JoinColumn(name = "polygonlocation"))
    @IndexColumn(name = "indexinlocation", base = 0)
    private List<GlobalPosition> points = new ArrayList<GlobalPosition>();

    public PolygonLocation() {
        points = new ArrayList<GlobalPosition>();
    }

    public void addPoint(GlobalPosition point) {
        this.points.add(point);
    }

    public void addPointAt(int index, GlobalPosition point) {
        this.points.add(index, point);
    }

    public void updatePointAt(int index, GlobalPosition newPoint) {
        this.points.set(index, newPoint);
    }

    public void clear() {
        points.clear();
    }

    public boolean pointExists(QPointF point) {
        if (this.points.indexOf(point) > -1) {
            return true;
        }
        return false;
    }

    public void removePoint(QPointF point) {
        if (this.pointExists(point)) {
            this.points.remove(point);
        }
    }

    public void removePointAt(int index) {
        if (this.points.size() >= index) {
            this.points.remove(index);
        }
    }

    protected void doDeepClone() {
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object.getClass() != this.getClass()) {
            return false;
        }
        PolygonLocation loc = (PolygonLocation) object;
        if (loc != null) {
            return false;
        }
        return false;
    }

    public List<GlobalPosition> getPoints() {
        return points;
    }

    public void setPoints(List<GlobalPosition> points) {
        this.points = points;
    }
}
