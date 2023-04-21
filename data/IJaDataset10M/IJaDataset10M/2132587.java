package Operator;

import Individuals.Individual;
import Individuals.Populations.Population;
import Operator.Operations.Operation;
import Util.Random.RandomNumberGenerator;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class for joining two populations. 
 * The incomingPopulation is added to the population.
 * Eg in ReplacementOperator
 */
public abstract class JoinOperator extends OperatorModule {

    protected Population incomingPopulation;

    /**
     * Constructor
     * @param rng random number generator
     * @param incPop incomming population
     */
    public JoinOperator(RandomNumberGenerator rng, Population incPop) {
        super(rng);
        this.incomingPopulation = incPop;
    }

    /** Create new instance*/
    public JoinOperator() {
    }

    public abstract void perform();

    public abstract void setOperation(Operation op);

    /**
     * Get the population coming in
     * @return incomming population
     */
    public Population getIncomingPopulation() {
        return incomingPopulation;
    }

    /**
     * Set the incomming population
     * @param incomingPopulation incomming population
     */
    public void setIncomingPopulation(Population incomingPopulation) {
        this.incomingPopulation = incomingPopulation;
    }

    /**
     * Increase the age of the operands by 1
     * @param operands operands for the operation
     **/
    protected void increaseAge(List<Individual> operands) {
        Iterator<Individual> iO = operands.iterator();
        Individual ind;
        while (iO.hasNext()) {
            ind = iO.next();
            int age = ind.getAge() + 1;
            ind.setAge(age);
        }
    }
}
