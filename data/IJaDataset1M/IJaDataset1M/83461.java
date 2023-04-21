package ec.simple;

import ec.*;
import ec.util.Checkpoint;

/**
 * A SimpleEvolutionState is an EvolutionState which implements a simple form
 * of generational evolution.
 *
 * <p>First, all the individuals in the population are created.
 * <b>(A)</b>Then all individuals in the population are evaluated.
 * Then the population is replaced in its entirety with a new population
 * of individuals bred from the old population.  Goto <b>(A)</b>.
 *
 * <p>Evolution stops when an ideal individual is found (if quitOnRunComplete
 * is set to true), or when the number of generations (loops of <b>(A)</b>)
 * exceeds the parameter value numGenerations.  Each generation the system
 * will perform garbage collection and checkpointing, if the appropriate
 * parameters were set.
 *
 * <p>This approach can be readily used for
 * most applications of Genetic Algorithms and Genetic Programming.
 *
 * @author Sean Luke
 * @version 1.0 
 */
public class SimpleEvolutionState extends EvolutionState {

    /**
     * 
     */
    public void startFresh() {
        output.message("Setting up");
        setup(this, null);
        output.message("Initializing Generation 0");
        statistics.preInitializationStatistics(this);
        population = initializer.initialPopulation(this, 0);
        statistics.postInitializationStatistics(this);
        exchanger.initializeContacts(this);
        evaluator.initializeContacts(this);
    }

    /**
     * @return
     * @throws InternalError
     */
    public int evolve() {
        if (generation > 0) output.message("Generation " + generation);
        statistics.preEvaluationStatistics(this);
        evaluator.evaluatePopulation(this);
        statistics.postEvaluationStatistics(this);
        if (evaluator.runComplete(this) && quitOnRunComplete) {
            output.message("Found Ideal Individual");
            return R_SUCCESS;
        }
        if (generation == numGenerations - 1) {
            return R_FAILURE;
        }
        statistics.prePreBreedingExchangeStatistics(this);
        population = exchanger.preBreedingExchangePopulation(this);
        statistics.postPreBreedingExchangeStatistics(this);
        String exchangerWantsToShutdown = exchanger.runComplete(this);
        if (exchangerWantsToShutdown != null) {
            output.message(exchangerWantsToShutdown);
            return R_SUCCESS;
        }
        statistics.preBreedingStatistics(this);
        population = breeder.breedPopulation(this);
        statistics.postBreedingStatistics(this);
        statistics.prePostBreedingExchangeStatistics(this);
        population = exchanger.postBreedingExchangePopulation(this);
        statistics.postPostBreedingExchangeStatistics(this);
        generation++;
        if (checkpoint && generation % checkpointModulo == 0) {
            output.message("Checkpointing");
            statistics.preCheckpointStatistics(this);
            Checkpoint.setCheckpoint(this);
            statistics.postCheckpointStatistics(this);
        }
        return R_NOTDONE;
    }

    /**
     * @param result
     */
    public void finish(int result) {
        statistics.finalStatistics(this, result);
        finisher.finishPopulation(this, result);
        exchanger.closeContacts(this, result);
        evaluator.closeContacts(this, result);
    }
}
