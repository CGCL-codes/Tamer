package computational.geometry.hvsegmentswindowing;

import computational.geometry.*;
import java.util.List;

/**
 * Generic implementation for the problem of windowing a set 
 * of horizontal and vertical segments.
 */
public abstract class HVSegmentsWindowing {

    private boolean isUpdated = false;

    public HVSegmentsWindowing() {
    }

    public boolean isUpdated() {
        return (isUpdated);
    }

    public abstract void update();

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public abstract List getSegments();

    public abstract void setSegments(List segmentL) throws PreconditionViolatedException;

    public abstract void addSegment(Segment s) throws PreconditionViolatedException;

    public abstract void clear();

    public abstract List query(Rectangle r);
}
