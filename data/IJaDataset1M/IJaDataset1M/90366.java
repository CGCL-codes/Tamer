package edu.uchicago.agentcell.molecules;

import edu.uchicago.agentcell.math.Vect;

/**
 * @author emonet
 *
 * 
 * 
 */
public class ConcentrationFieldSteps extends ConcentrationField {

    private Vect start1;

    private double level1;

    private Vect start2;

    private double level2;

    private Vect start3;

    private double level3;

    private Vect start4;

    private double level4;

    private Vect start5;

    private double level5;

    /**
     * @param newMolecularType
     * @param start1
     * @param level1
     * @param start2
     * @param level2
     * @param start3
     * @param level3
     * @param start4
     * @param level4
     * @param start5
     * @param level5
     */
    public ConcentrationFieldSteps(String newMolecularType, Vect start1, double level1, Vect start2, double level2, Vect start3, double level3, Vect start4, double level4, Vect start5, double level5) {
        this.setMolecularType(newMolecularType);
        this.start1 = start1;
        this.level1 = level1;
        this.start2 = start2;
        this.level2 = level2;
        this.start3 = start3;
        this.level3 = level3;
        this.start4 = start4;
        this.level4 = level4;
        this.start5 = start5;
        this.level5 = level5;
    }

    public double getConcentrationLevelAt(Vect position) {
        if (position.getElement(2) < start1.getElement(2)) {
            return 0.0;
        } else if ((position.getElement(2) >= start1.getElement(2)) && (position.getElement(2) < start2.getElement(2))) {
            return level1;
        } else if ((position.getElement(2) >= start2.getElement(2)) && (position.getElement(2) < start3.getElement(2))) {
            return level2;
        } else if ((position.getElement(2) >= start3.getElement(2)) && (position.getElement(2) < start4.getElement(2))) {
            return level3;
        } else if ((position.getElement(2) >= start4.getElement(2)) && (position.getElement(2) < start5.getElement(2))) {
            return level4;
        } else {
            return level5;
        }
    }
}
