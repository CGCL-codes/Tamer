package org.edits.engines.optimizer;

import java.util.HashMap;
import java.util.Map;
import org.edits.engines.EntailmentEngine;
import org.edits.engines.EvaluationStatistics;
import org.edits.etaf.EntailmentPair;
import org.edits.processor.EDITSIterator;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class MyFitnessFunction extends FitnessFunction {

    private EntailmentEngine engine;

    private EDITSIterator<EntailmentPair> iter;

    private Map<String, Double> params;

    /** Default constructor */
    public MyFitnessFunction(boolean maximize, EntailmentEngine engine_, Map<String, Double> params_, EDITSIterator<EntailmentPair> iter_) {
        super(maximize);
        engine = engine_;
        params = params_;
        iter = iter_;
    }

    /**
	 * Evaluates a particles at a given position
	 * 
	 * @param position
	 *            : Particle's position
	 * @return Fitness function for a particle
	 */
    @Override
    public double evaluate(double position[]) {
        Map<String, Double> nparams = new HashMap<String, Double>();
        int i = 0;
        for (String key : params.keySet()) {
            nparams.put(key, position[i]);
            i++;
        }
        try {
            engine.updateParameters(nparams);
            EvaluationStatistics stats = engine.train(iter);
            iter.reset();
            return stats.value(engine.trainingOptions());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return 0.0;
    }
}
