package ch.unizh.ini.jaer.projects.spatiatemporaltracking.tracker.cluster;

import ch.unizh.ini.jaer.projects.spatiatemporaltracking.tracker.cluster.assignment.AssignableCluster;
import ch.unizh.ini.jaer.projects.spatiatemporaltracking.tracker.event.EventAssignable;
import ch.unizh.ini.jaer.projects.spatiatemporaltracking.parameter.ParameterListener;
import javax.media.opengl.GLAutoDrawable;

/**
 *
 * @author matthias
 * 
 * The interface provides the basic methods to maintain and draw a cluster.
 */
public interface FeatureCluster extends EventAssignable, AssignableCluster, ParameterListener {

    /**
     * Initialzes the object.
     */
    public void init();

    /**
     * Resets the object.
     */
    public void reset();

    /**
     * Prepares the cluster to be deleted.
     */
    public void clear();

    /**
     * Gets true if the cluster is a candidate.
     * 
     * @return True, if the cluster is a candidate.
     */
    public boolean isCandidate();

    /**
     * Draws the cluster.
     * 
     * @param drawable The object used to draw.
     */
    public void draw(GLAutoDrawable drawable);
}
