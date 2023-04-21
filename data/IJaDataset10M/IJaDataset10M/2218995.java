package picoevo.core.representation;

import picoevo.core.evolution.*;
import picoevo.toolbox.*;

public abstract class Population extends PopulationObject implements EvolvableInterface, InitialisationInterface, EvaluationInterface, PopulationInterface {

    protected World _evolutionSpace;

    protected int _generation = 0;

    public Population(String __populationName, World __evolutionSpace) {
        super(__populationName);
        _evolutionSpace = __evolutionSpace;
    }

    /** get age of the population (i.e. number of times it has been "renewed") */
    public int getGenerationNumber() {
        return _generation;
    }

    /** reset age of the population to zero */
    public void resetGenerationNumber() {
        _generation = 0;
    }

    /** add the given individual to this population */
    public void registerIndividual(Individual __ind) {
        this._individualList.add(__ind);
    }

    /** initialise the population */
    public void performInitialisation() {
        ((InitialisationOperator_Population) this.getTemplate().getPopulationInitialisationOperator()).initialise(this);
    }

    /** Perform evaluation for the population (i.e. call Individual.performEvaluation method for each individual) */
    public void performEvaluation() {
        if (this.getTemplate().existsEvaluationOperator_Population() == true) ((EvaluationOperator_Population) this.getTemplate().getEvaluationOperator_Population()).evaluate(this);
        if (this.getTemplate().existsEvaluationOperator_Individual() == true) for (int i = 0; i != this._individualList.size(); i++) this.getIndividual(i).performEvaluation();
    }

    /**
	 * Renew the population (i.e. individuals list) by replacing it with the content of the target PopulationContainer.
	 * 
	 * warning (1): after this call, the PopulationContainer passed as parameter does not contain any individuals to ensure
  	 * single-referenced individuals
	 * warning (2): at the end of this method, we assume that there will be no pointer to any of the objects previously
	 * contained in _individualList so as to let all of these to be garbage collected - In practical, this means that
	 * the programmer should pay special attention to suppress any PopulationContainer and/or PopulationViewer that
	 * is not needed (set embedded IndividualList explicitly to "null" ) - e.g. The PopulationContainer parameter
	 * should be set to null after calling this method.
	 * 
	 * dev note : final class for the moment.
	 * 
	 * 	 
	 * @param newpop
	 */
    public final void renewPopulation(PopulationContainer __newPopulationContainer) {
        if (__newPopulationContainer == null || __newPopulationContainer.getPopulationSize() == 0) Display.critical("[\"" + this.getName() + "\"] " + this.getClass().getName() + "::renewPopulation - new population is empty!");
        this._individualList = __newPopulationContainer.getIndividualList();
        for (int i = 0; i != this.getPopulationSize(); i++) {
            this.getIndividual(i).setEvaluationFlag(false);
            this.getIndividual(i).setName("ind#" + i);
        }
        __newPopulationContainer.reset();
        this._generation++;
    }

    /**
	 * Display population statistics (average and best individuals)
	 *
	 */
    public void displayStatistics() {
        this.getTemplate().getPopulationStatisticsOperator().displayStatistics(this);
    }

    /**
	 * Log population statistics (average and best individuals) into a file (as given in the parameter set)
	 *
	 */
    public void logStatistics() {
        if (this.getTemplate().getLogFilename() != null) this.getTemplate().getPopulationStatisticsOperator().logStatistics(this, this.getTemplate().getLogFilename());
    }

    public final ParameterSet getTemplate() {
        return ((ParameterSet) this.getOwner().getTemplate());
    }

    public final World getOwner() {
        return this._evolutionSpace;
    }

    /**
	 * Trace all individuals and (maybe) embedded elements for this population - no return value.
	 * By default, this method performs nothing -- however, descendant classes may overwrite this and related methods to perform user-specified code.
	 * e.g.: specific operator/terminal code for post-evaluation monitoring purpose. 
	 *
	 */
    public void trace() {
        for (int i = 0; i != this.getIndividualList().size(); i++) this.getIndividual(i).trace();
    }
}
