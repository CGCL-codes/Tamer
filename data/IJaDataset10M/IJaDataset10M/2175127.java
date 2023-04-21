package net.sf.laptracker.mobile;

import net.sf.laptracker.TrackPoint;

public interface TrackListener {

    public void newPoint(TrackPoint newPoint, boolean boundsChanged, boolean newSegment);

    public void currentPointChanged(final TrackPoint newPoint, int newIndex);

    public void stateChanged(int newState);
}
