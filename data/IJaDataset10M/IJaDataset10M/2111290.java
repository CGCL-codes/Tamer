package org.xith3d.physics.simulation.joode;

/**
 * JOODE implementation of a Joint
 * 
 * @author Amos Wenger (aka BlueSky)
 */
public interface JoodeJoint {

    /**
     * @return the underlying JOODE joint.
     */
    public net.java.dev.joode.joint.Joint getJOODEJoint();
}
