package de.fzi.mappso.communication.observer;

import de.fzi.mappso.align.MapPSOAlignment;
import de.fzi.mappso.particlecluster.ParticleCluster;

/**
 * Another mock implementation of a {@link ClusterCommObserver}.
 * 
 * @author Juergen Bock (bock@fzi.de)
 *
 */
public class MockObserver2 implements ClusterCommObserver {

    @Override
    public void reportNewState(MapPSOAlignment alignState, ParticleCluster source) {
        throw new UnsupportedOperationException();
    }
}
