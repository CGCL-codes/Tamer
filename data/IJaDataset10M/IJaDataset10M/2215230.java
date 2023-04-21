package bitWave.examples.util;

import bitWave.linAlg.Quaternion;
import bitWave.linAlg.Vec4;
import bitWave.physics.impl.RigidBodyImpl;
import bitWave.visualization.Feature;

/**
 * A RigidBody that is also a Feature.
 */
public class FeaturedRigidBody extends RigidBodyImpl implements Feature {

    public FeaturedRigidBody(String identifier, Vec4 position, Vec4 velocity, Quaternion attitude, Vec4 angularVelocity) {
        super(identifier, position, velocity, attitude, angularVelocity);
    }
}
