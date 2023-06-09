package lejos.robotics.proposal;

import java.util.Collection;
import lejos.geom.Point;
import lejos.robotics.Pose;

/**
 * 
 * This class creates a set of waypoints connected by straight lines that lead from one location to another without
 * colliding with mapped geometry. 
 *
 */
public interface PathFinder {

    Collection<WayPoint> findRoute(Pose start, Point destination) throws DestinationUnreachableException;

    Collection<WayPoint> findRoute(Pose start, Pose destination) throws DestinationUnreachableException;
}
