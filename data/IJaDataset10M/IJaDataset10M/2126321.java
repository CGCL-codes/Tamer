package org.vizzini.ai.geneticalgorithm;

/**
 * Defines methods required by game genetic algorithms.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public interface IGameGeneticAlgorithm extends IGeneticAlgorithm {

    /**
     * @return  the fitness loss value.
     *
     * @since   v0.1
     */
    int getFitnessLoss();

    /**
     * @return  the fitness tie value.
     *
     * @since   v0.1
     */
    int getFitnessTie();

    /**
     * @return  the fitness win value.
     *
     * @since   v0.1
     */
    int getFitnessWin();

    /**
     * Set the fitness loss value. Typically this value is negative.
     *
     * @param  fitness  Fitness.
     *
     * @since  v0.1
     */
    void setFitnessLoss(int fitness);

    /**
     * Set the fitness tie value.
     *
     * @param  fitness  Fitness.
     *
     * @since  v0.1
     */
    void setFitnessTie(int fitness);

    /**
     * Set the fitness win value. Typically this value is positive.
     *
     * @param  fitness  Fitness.
     *
     * @since  v0.1
     */
    void setFitnessWin(int fitness);

    /**
     * @param  isVerbose  flag which indicates whether to provide text output.
     *
     * @since  v0.1
     */
    void setVerbose(boolean isVerbose);
}
