package org.opt4j.optimizer.ea.moead;

import org.opt4j.config.annotations.Ignore;
import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;
import org.opt4j.optimizer.ea.ConstantCrossoverRate;
import org.opt4j.optimizer.ea.CrossoverRate;
import org.opt4j.optimizer.ea.EvolutionaryAlgorithmModule.CrossoverRateType;
import org.opt4j.start.Constant;

/**
 * The MOEA/D optimizer.
 * 
 * @see "Q. Zhang: MOEA/D: A Multiobjective Evolutionary Algorithm Based on
 *      Decomposition. IEEE Transactions on Evolutionary Computation, vol. 11,
 *      no. 6, pp. 712-731, Dec. 2007."
 * 
 * 
 * @author reimann
 * 
 */
public class MOEADModule extends OptimizerModule {

    @MaxIterations
    protected int generations = 1000;

    @Constant(value = "rate", namespace = ConstantCrossoverRate.class)
    protected double crossoverRate = 0.95;

    @Ignore
    protected CrossoverRateType crossoverRateType = CrossoverRateType.CONSTANT;

    @Constant(value = "n", namespace = MOEAD.class)
    protected int subproblemsN = 30;

    @Constant(value = "t", namespace = MOEAD.class)
    protected int neighborsT = 3;

    @Override
    protected void config() {
        bindOptimizer(MOEAD.class);
        bind(CrossoverRate.class).to(ConstantCrossoverRate.class).in(SINGLETON);
    }

    public int getSubproblemsN() {
        return subproblemsN;
    }

    public void setSubproblemsN(int subproblemsN) {
        this.subproblemsN = subproblemsN;
    }

    public int getNeighborsT() {
        return neighborsT;
    }

    public void setNeighborsT(int neighborsT) {
        this.neighborsT = neighborsT;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public CrossoverRateType getCrossoverRateType() {
        return crossoverRateType;
    }

    public void setCrossoverRateType(CrossoverRateType crossoverRateType) {
        this.crossoverRateType = crossoverRateType;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
}
