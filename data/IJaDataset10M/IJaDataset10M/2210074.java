package edu.itba.ia.tp1.problem.binary2bcd.circuittree.aptitude;

import edu.itba.ia.tp1.engine.population.AbstractIndividual;
import edu.itba.ia.tp1.problem.binary2bcd.AbstractAptitude;
import edu.itba.ia.tp1.problem.binary2bcd.circuittree.CircuitTree;

/**
 * This is the aptitude function for a Binary to BCD Converter (CircuitTree
 * implementation).
 * 
 * @author Mart�n A. Heras
 */
public class MainCircuitTreeAptitudeImpl extends AbstractAptitude {

    /**
	 * Creates a new instance of <code>AptitudeImpl</code>.
	 */
    public MainCircuitTreeAptitudeImpl() {
        super();
    }

    /**
	 * Naif approach to get individual aptitude.
	 * 
	 * @deprecated
	 * @param individual
	 *            The individual.
	 * @return The individual aptitude.
	 */
    public Double evaluate2(AbstractIndividual individual) {
        int size = this.inputOutputMap.size();
        Double aptitude = new Double(0);
        Double matches = 0.0;
        for (Integer input : this.inputOutputMap.keySet()) {
            if (individual.operate(input).equals(this.inputOutputMap.get(input))) {
                matches++;
            }
        }
        aptitude = new Double(matches / size);
        return aptitude;
    }

    public Double evaluate(AbstractIndividual individual) {
        Double aptitude = new Double(0.0);
        int size = this.inputOutputMap.size();
        int nOutputs = ((CircuitTree) individual).getOutputBits();
        double totalMatchesAmount = 0.0;
        double bitMatchAmount = (1.0 / (nOutputs * size)) / 2;
        int realOutput;
        int output;
        for (Integer input : this.inputOutputMap.keySet()) {
            output = (Integer) individual.operate(input);
            realOutput = this.inputOutputMap.get(input).intValue();
            if (output == realOutput) {
                totalMatchesAmount++;
            } else {
                for (int i = 0; i < nOutputs; i++) {
                    if ((output % 2) == 1 && (realOutput % 2) == 1) {
                        totalMatchesAmount += bitMatchAmount;
                    } else if ((output % 2) == 0 && (realOutput % 2) == 0) {
                        totalMatchesAmount += bitMatchAmount;
                    }
                    output /= 2;
                    realOutput /= 2;
                }
            }
        }
        aptitude = new Double(totalMatchesAmount / size);
        if (aptitude.compareTo(1.0) != 0) {
            Long nGates = ((CircuitTree) individual).getGatesLength();
            aptitude -= penalize(aptitude, nGates);
        }
        return aptitude;
    }

    /**
	 * Penalize aptitude, depending on the number of gates.
	 * 
	 * @param aptitude
	 *            Aptitude to be penalized.
	 * @param nGates
	 *            Number of gates.
	 * @return Penalization.
	 */
    private Double penalize(Double aptitude, Long nGates) {
        Double penalization = 0.0;
        if (nGates > 1000) {
            penalization = aptitude * 0.5;
        } else if (nGates > 750) {
            penalization = aptitude * 0.4;
        } else if (nGates > 500) {
            penalization = aptitude * 0.3;
        } else if (nGates > 400) {
            penalization = aptitude * 0.25;
        } else if (nGates > 300) {
            penalization = aptitude * 0.2;
        } else if (nGates > 250) {
            penalization = aptitude * 0.15;
        } else if (nGates > 200) {
            penalization = aptitude * 0.1;
        } else if (nGates > 150) {
            penalization = aptitude * 0.05;
        } else if (nGates > 100) {
            penalization = aptitude * 0.025;
        } else if (nGates > 80) {
            penalization = aptitude * 0.02;
        } else if (nGates > 50) {
            penalization = aptitude * 0.015;
        } else if (nGates > 25) {
            penalization = aptitude * 0.007;
        }
        return penalization;
    }
}
