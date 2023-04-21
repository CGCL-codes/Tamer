package org.edits.optimizer;

import java.util.HashMap;
import java.util.Map;
import org.edits.engines.EntailmentEngine;
import org.edits.etaf.EntailmentPair;
import org.edits.processor.EDITSIterator;
import org.jgap.Chromosome;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;

/**
 * 
 * @author Milen Kouylekov
 * 
 */
public class GeneticOptimizer extends EntailmentEngineOptimizer {

    @Override
    public void optimize(EntailmentEngine engine, EDITSIterator<EntailmentPair> iter) throws Exception {
        if (getNumberOfIterations() < 1) return;
        Map<String, Double> params = engine.parameters();
        MaxFunction f = new MaxFunction(engine, params, iter);
        org.jgap.Configuration.reset();
        org.jgap.Configuration gaConf = new DefaultConfiguration();
        gaConf.setPreservFittestIndividual(true);
        gaConf.setKeepPopulationSizeConstant(true);
        int chromeSize = params.size();
        Genotype genotype = null;
        try {
            IChromosome sampleChromosome = new Chromosome(gaConf, new DoubleGene(gaConf, getMinPosition(), getMaxPosition()), chromeSize);
            gaConf.setSampleChromosome(sampleChromosome);
            gaConf.setPopulationSize(4);
            gaConf.setFitnessFunction(f);
            genotype = Genotype.randomInitialGenotype(gaConf);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        for (int i = 0; i < getNumberOfIterations(); i++) genotype.evolve();
        IChromosome bestFitness = genotype.getFittestChromosome();
        double[] position = new double[params.size()];
        for (int i = 0; i < bestFitness.size(); i++) {
            DoubleGene value = (DoubleGene) bestFitness.getGene(bestFitness.size() - (i + 1));
            position[i] = value.doubleValue();
        }
        Map<String, Double> nparams = new HashMap<String, Double>();
        int i = 0;
        for (String key : params.keySet()) {
            nparams.put(key, position[i]);
            i++;
        }
        engine.updateParameters(nparams);
    }
}
