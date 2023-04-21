package Operator.Operations;

import Individuals.Individual;

/**
 * Interface for creation of individuals
 * @author Conor
 */
public interface CreationOperation extends Operation {

    /**
     * Creates an individual
     * @return created individual
     */
    public Individual createIndividual();
}
