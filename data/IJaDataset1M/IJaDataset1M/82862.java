package org.edits.optimizer;

import java.util.HashMap;
import java.util.Map;
import net.sourceforge.jswarm_pso.Swarm;
import org.edits.definition.ModuleDefinition;
import org.edits.engines.EntailmentEngine;
import org.edits.etaf.EntailmentPair;
import org.edits.processor.EDITSIterator;

/**
 * 
 * @author Milen Kouylekov
 * 
 */
public class PSOOptimizer extends EntailmentEngineOptimizer {

    private double inertia = 0.95;

    private double maxMinVelocity = 1;

    @Override
    public void configure(ModuleDefinition conf) throws Exception {
        super.configure(conf);
        inertia = Double.parseDouble(conf.option("inertia").getValue());
        maxMinVelocity = Double.parseDouble(conf.option("min_max_velocity").getValue());
    }

    @Override
    public void optimize(EntailmentEngine engine, EDITSIterator<EntailmentPair> iter) throws Exception {
        if (getNumberOfIterations() < 1) return;
        Map<String, Double> params = engine.parameters();
        MyParticle.setParticleDimension(params.size());
        MyFitnessFunction function = new MyFitnessFunction(true, engine, params, iter);
        Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES, new MyParticle(), function);
        swarm.setInertia(inertia);
        swarm.setMaxPosition(getMaxPosition());
        swarm.setMinPosition(getMinPosition());
        swarm.setMaxMinVelocity(maxMinVelocity);
        for (int i = 0; i < getNumberOfIterations(); i++) swarm.evolve();
        double[] position = swarm.getBestPosition();
        Map<String, Double> nparams = new HashMap<String, Double>();
        int i = 0;
        for (String key : params.keySet()) {
            nparams.put(key, position[i]);
            i++;
        }
        engine.updateParameters(nparams);
    }
}
