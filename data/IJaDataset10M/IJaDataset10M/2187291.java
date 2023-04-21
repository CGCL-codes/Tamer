package org.jheuristics.ga;

import org.jheuristics.util.Condition;

public abstract class AbstractEvolver implements Evolver {

    private GAConfig config;

    private GAStatus status;

    /**
	 * TODO
	 *
	 * @param config
	 */
    public AbstractEvolver(GAConfig config) {
        setGAConfiguration(config);
    }

    /**
	 * TODO
	 *
	 * @param age
	 * @param current
	 * @param avaliable
	 * @param selected
	 * @return
	 */
    protected GAStatus createGAStatus(int age, Population current, Population avaliable, Population selected) {
        if (0 > age) {
            throw new IllegalArgumentException();
        }
        current.sort();
        return new DefaultGAStatus(age, new UnmodifiablePopulation(current), avaliable, selected);
    }

    /**
	 * TODO
	 *
	 * @return
	 */
    protected Population createEmptyPopulation() {
        return new DefaultPopulation();
    }

    /**
	 * TODO 
	 */
    protected abstract void initialize();

    public void evolve() {
        if (null == status) {
            initialize();
        }
        config.getOperator().operate(status, config);
        config.getBulkFitnessEvaluator().evaluateFitness(status.getSelectedIndividualsPopulation(), status, config);
        status = createGAStatus(status.getEvolutionAge() + 1, status.getSelectedIndividualsPopulation(), createEmptyPopulation(), createEmptyPopulation());
    }

    public void evolve(int n) {
        for (int i = 0; i < n; i++) evolve();
    }

    public void evolve(Condition contition) {
        if (null == status) {
            initialize();
        }
        while (contition.check(new Object[] { status, config })) {
            evolve();
        }
    }

    public GAStatus getEvolutionStatus() {
        return status;
    }

    /**
	 * TODO
	 *
	 * @param status
	 */
    protected void setEvolutionStatus(GAStatus status) {
        if (null == status) {
            throw new NullPointerException();
        }
        this.status = status;
    }

    public GAConfig getGAConfiguration() {
        return config;
    }

    public void setGAConfiguration(GAConfig config) {
        if (null == config) {
            throw new NullPointerException();
        }
        this.config = config;
    }
}
