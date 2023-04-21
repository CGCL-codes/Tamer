package playground.gregor.sim2d_v2.simulation;

import java.util.Random;

/**
 * @author laemmel
 * 
 */
public class DefaultSim2DEngineFactory {

    public Sim2DEngine createSim2DEngine(final Sim2D sim, final Random random) {
        return new Sim2DEngine(sim, random);
    }
}
