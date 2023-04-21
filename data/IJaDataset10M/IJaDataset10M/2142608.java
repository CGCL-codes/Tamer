package genetic;

/**
 * Factory for FitnessCalculator.
 * Given a FitnessCalculatorSettings object returns a FitnessCalculator of the 
 * type specified in its settings.
 * 
 * @author Fabrizio Pastore [ fabrizio.pastore@gmail.com ]
 *
 */
public class FitnessFactory {

    /**
	 * Returns a FitnessCalculator from given settngs.
	 * 
	 * @param set
	 * @param g
	 * @return
	 */
    public static FitnessCalculator get(FitnessSettings set, Geneticator g) {
        FitnessCalculator fc = null;
        if (set instanceof FitnessTrapSettings) fc = new FitnessTrap((FitnessTrapSettings) set);
        if (set instanceof OptimusInRunSettings) fc = new FitnessOptimusInRun((OptimusInRunSettings) set);
        return fc;
    }
}
