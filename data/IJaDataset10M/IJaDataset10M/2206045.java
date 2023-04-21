// TODO: Document GeneticAlgorithm
package org.ezvolve.algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.ezvolve.FitnessComparator;
import java.util.Random;
import org.ezvolve.*;
import org.ezvolve.algorithms.operators.CrossoverOperator;
import org.ezvolve.algorithms.operators.MutationOperator;
import org.ezvolve.algorithms.operators.SelectionOperator;

public final class GeneticAlgorithm<C>
        extends Algorithm<C> {

    private final CandidateFactory<C> candidateFactory;
    private final FitnessFunction<C> fitnessFunction;
    private final int populationSize;
    private final SelectionOperator selectionOperator;
    private final CrossoverOperator<C> crossoverOperator;
    private final MutationOperator<C> mutationOperator;
    private final FitnessComparator fitnessComparator;

    private GeneticAlgorithm(CandidateFactory<C> candidateFactory,
                             FitnessFunction<C> fitnessFunction,
                             int populationSize,
                             SelectionOperator selectionOperator,
                             CrossoverOperator<C> crossoverOperator,
                             MutationOperator<C> mutationOperator,
                             FitnessComparator fitnessComparator) {
        if (candidateFactory == null) {
            throw new NullPointerException("candidateFactory");
        }
        if (fitnessFunction == null) {
            throw new NullPointerException("fitnessFunction");
        }
        if (populationSize < 1) {
            throw new IllegalArgumentException(
                    "populationSize = " + populationSize + ", expected: >= 1");
        }
        if (selectionOperator == null) {
            throw new NullPointerException("selectionOperator");
        }
        if (crossoverOperator == null) {
            throw new NullPointerException("crossoverOperator");
        }
        if (mutationOperator == null) {
            throw new NullPointerException("mutationOperator");
        }
        if (fitnessComparator == null) {
            throw new NullPointerException("fitnessComparator");
        }
        this.candidateFactory = candidateFactory;
        this.fitnessFunction = fitnessFunction;
        this.populationSize = populationSize;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.fitnessComparator = fitnessComparator;
    }

    @Override
    public State<C> initialState(Random random)
            throws InterruptedException {

        // Checks that the RNG is not null
        if (random == null) {
            throw new NullPointerException("random");
        }

        List<EvaluatedCandidate<C>> pool = new ArrayList<>(populationSize);

        // The main loop for producing an initial population
        for (int i = 0; i < populationSize; i++) {

            // Checks for interrupt
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            // Samples the candidate factory
            C candidate = candidateFactory.sample(random);

            // Checks that the candidate is not null
            if (candidate == null) {
                throw new NullPointerException(
                        "Candidate factory returned a null candidate.");
            }

            // Evaluates the candidate
            Fitness fitness = fitnessFunction.fitnessOf(candidate);

            // Checks that the fitness is not null
            if (fitness == null) {
                throw new NullPointerException(
                        "Fitness function returned a null fitness.");
            }

            // Adds the new candidate
            pool.add(EvaluatedCandidate.of(candidate, fitness));

        }

        return new PopulationState<>(fitnessComparator.bestOf(pool),
                                     CandidatePool.fromReference(pool));

    }

    @Override
    public State<C> nextState(State<C> state,
                              int iterationNumber,
                              Random random)
            throws InterruptedException {
        if (state == null) {
            throw new NullPointerException("state");
        }
        if (random == null) {
            throw new NullPointerException("random");
        }
        if (!(state instanceof PopulationState)) {
            throw new ClassCastException(
                    "state is not an instance of PopulationState");
        }
        if (iterationNumber < 1) {
            throw new IllegalArgumentException("iterationNumber = "
                    + iterationNumber + ", iterationNumber >= 1");
        }
        PopulationState<C> s = (PopulationState<C>) state;
        int inputSize = crossoverOperator.inputSize();
        int outputSize = crossoverOperator.outputSize();
        int crossoversRequired = (populationSize / outputSize)
                    + ((populationSize % outputSize == 0) ? 0 : 1);
        int parentsRequired = crossoversRequired * inputSize;
        List<EvaluatedCandidate<C>> newPopulation
                = new ArrayList<>(populationSize);

        // Checks for interrupt
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        // Prepairs the selection operator
        Iterator<EvaluatedCandidate<C>> iterator
                = selectionOperator.select(parentsRequired,
                                           fitnessComparator, 
                                           s.population(), 
                                           iterationNumber, 
                                           random);

        // The main loop for producing a sucessor population
        for (int i = 0; i < crossoversRequired; i++) {
            
            // Checks for interrupt
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            List<C> temp = new ArrayList<>(inputSize);

            // Gets the required number of candidates from the selection
            // iterator to be used for crossover
            for (int j = 0; j < inputSize; j++) {
            
                // Checks for interrupt
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

                // Gets the next candidate from the selection iterator
                EvaluatedCandidate<C> candidate = iterator.next();

                // Checks the selection output
                if (temp == null) {
                    throw new NullPointerException(
                            "Selection operator returned a null candidate.");
                }

                // Adds the candidate to the list for processing
                temp.add(candidate.candidate());

            }
            
            // Checks for interrupt
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            // Applies the crossover operator on the selected candidates
            temp = crossoverOperator.crossover(temp, iterationNumber);

            // Checks the crossover output
            if (temp == null) {
                throw new NullPointerException(
                        "Crossover operator returned null.");
            }
            if (temp.size() != outputSize) {
                throw new RuntimeException("Crossover operator returned "
                        + temp.size() + " instance(s), expected "
                        + outputSize + ".");
            }

            // Applies mutation and fitness evaluation on the resulting
            // individuals output from crossover
            for (int j = 0; j < temp.size(); j++) {

                // Checks for interrupt
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                // Gets the next candidate
                C candidate = temp.get(j);
                
                // Checks that the candidate is not null
                if (candidate == null) {
                    throw new NullPointerException(
                            "Crossover operator returned a null candidate.");
                }
                
                // Mutates the candidate
                candidate = mutationOperator.mutate(candidate,
                                                    iterationNumber);
                
                // Checks that the candidate is not null
                if (candidate == null) {
                    throw new NullPointerException(
                            "Mutation operator returned a null candidate.");
                }
            
                // Checks for interrupt
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                
                // Evaluates the candidate
                Fitness fitness = fitnessFunction.fitnessOf(candidate);
                
                // Checks that the fitness is not null
                if (fitness == null) {
                    throw new NullPointerException(
                            "Fitness function returned a null fitness.");
                }
                
                // Adds the new candidate
                newPopulation.add(EvaluatedCandidate.of(candidate,fitness));
                
            }

        }

        // Checks that the population is large enough
        if (newPopulation.size() < populationSize) {
            throw new AssertionError(
                    "GA created under-sized population, cause unknown.");
        }

        // Reduces the population at random if too large
        while (newPopulation.size() > populationSize) {
            newPopulation.remove(random.nextInt(newPopulation.size()));
        }

        // Gets the best individual so far
        EvaluatedCandidate<C> bestPrevious
                = s.best();
        EvaluatedCandidate<C> bestCurrent
                = fitnessComparator.bestOf(newPopulation);
        EvaluatedCandidate<C> best
                = fitnessComparator.bestOf(bestPrevious, bestCurrent);
        
        // Returns the new state
        return new PopulationState<>(
                best, CandidatePool.fromReference(newPopulation));

    }

}
