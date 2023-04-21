package org.geotools.gpx2.gpxentities;

import java.util.*;

/**
 * Represents a GPX track. A track is made up of a TrackSegments. Each
 * TrackSegment in a Track object contains SimpleWaypoints that represent the
 * "track points" in the Track.
 *
 * @see TrackSegment
 * @see SimpleWaypoint
 * @see BasicTrack
 * @see BasicTrackSegment
 */
public interface Track {

    /**
	 * Returns the name of this TrackSegment.
	 */
    public abstract String getName();

    /**
	 * Sets the name of the TrackSegment.
	 */
    public abstract void setName(String argName);

    /**
	 * Adds a TrackSegment to this Track.
	 */
    public abstract void addTrackSegment(TrackSegment argTrackSegment);

    /**
	 * Clears all of the TrackSegments within this Track.
	 */
    public abstract void clearTrackSegments();

    /**
	 * Returns the TrackSegments contained in this Track.
	 */
    public abstract List<TrackSegment> getTrackSegments();

    /**
	 * Returns the number of TrackSegments contained in this Track.
	 */
    public abstract int getNumberOfSegments();
}
