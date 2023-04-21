package jeco.kernel.algorithm.moge;

import Individuals.Individual;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the label and value (Double) of different objectives to be
 * associated to the same individual.
 *
 * @author J. M. Colmenar
 */
public class MultiFitness {

    /** Returns the MultiFitness list from an Individual list. This method
     * is used in sorting functions.
     * @param operands
     * @return
     */
    static List<MultiFitness> getMultiFitnessList(List<Individual> operands) {
        List<MultiFitness> mfList = new ArrayList<MultiFitness>();
        for (int i = 0; i < operands.size(); i++) {
            mfList.add(FitnessMO.evaluate(operands.get(i)));
        }
        return mfList;
    }

    private List<Double> values = null;

    private Individual indiv;

    protected Map<String, Number> properties;

    private static int EXECTIME = 0;

    private static int MEMUSAGE = 1;

    private static int ENERGY = 2;

    public MultiFitness(int numObjectives, Individual ind) {
        this.values = new ArrayList<Double>(numObjectives);
        this.indiv = ind;
        this.properties = new HashMap<String, Number>();
    }

    public Double getObjective(int index) {
        return values.get(index);
    }

    public void setObjective(int index, Double value) {
        values.add(index, value);
    }

    public Individual getIndividual() {
        return this.indiv;
    }

    public void setIndividual(Individual newIndiv) {
        this.indiv = newIndiv;
    }

    public Number getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Number value) {
        properties.put(name, value);
    }

    public int compareTo(MultiFitness solution, Comparator<MultiFitness> comparator) {
        return comparator.compare(this, solution);
    }

    boolean objectivesEquals(MultiFitness mf) {
        boolean equal = true;
        int i = 0;
        while (i < values.size() && equal) {
            equal = (this.getObjective(i) == mf.getObjective(i));
            i++;
        }
        return equal;
    }

    void setExecTime(Double value) {
        values.add(EXECTIME, value);
    }

    void setMemUsage(Double value) {
        values.add(MEMUSAGE, value);
    }

    void setEnergy(Double value) {
        values.add(ENERGY, value);
    }

    void getExecTime() {
        values.get(EXECTIME);
    }

    void getMemUsage() {
        values.get(MEMUSAGE);
    }

    void setEnergy() {
        values.get(ENERGY);
    }

    List<Double> getObjectives() {
        return this.values;
    }

    /**
     * This method returns the non-dominated solutions of a list of
     * individuals. 
     */
    public static List<Individual> getNonDominated(List<Individual> inputList) {
        List<Individual> nonDominated = new ArrayList<Individual>();
        boolean duplicated[] = new boolean[inputList.size()];
        for (int i = 0; i < inputList.size(); i++) duplicated[i] = false;
        MultiFitnessDominanceComparator comparator = new MultiFitnessDominanceComparator();
        int compare;
        MultiFitness solI;
        MultiFitness solJ;
        for (int i = 0; i < inputList.size(); i++) {
            solI = FitnessMO.evaluate(inputList.get(i));
            boolean dominated = false;
            int j = 0;
            while (!dominated && j < inputList.size()) {
                if ((j != i) && (!duplicated[j])) {
                    solJ = FitnessMO.evaluate(inputList.get(j));
                    compare = comparator.compare(solI, solJ);
                    dominated = (compare > 0);
                    if ((compare == 0) && solI.objectivesEquals(solJ)) duplicated[j] = true;
                }
                j++;
            }
            if (!dominated && inputList.get(i).isValid() && !duplicated[i]) nonDominated.add(inputList.get(i));
        }
        return nonDominated;
    }

    /**
     * Sort individuals by dominance and crowding distance: non-dominated
     * solutions first.
     */
    public static void sortByDominanceAndCrowding(List<Individual> inputList) {
        List<Individual> nonDominated = MultiFitness.getNonDominated(inputList);
        inputList.removeAll(nonDominated);
        CrowdingDistanceMultiFitness assigner = new CrowdingDistanceMultiFitness(FitnessMO.numObjectives);
        MultiFitnessCrowdingComparator comparator = new MultiFitnessCrowdingComparator();
        assigner.executeOnIndividualList(nonDominated);
        Collections.sort(MultiFitness.getMultiFitnessList(nonDominated), comparator);
        inputList.addAll(0, nonDominated);
    }
}
