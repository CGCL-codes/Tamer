package ch.romix.jsens.bo;

import java.util.ArrayList;
import java.util.HashMap;
import ch.romix.jsens.gui.Program;

public class SingleInfluenceMatrix implements InfluenceMatrix {

    private String name;

    private HashMap<Variable, HashMap<Variable, Integer>> influences = new HashMap<Variable, HashMap<Variable, Integer>>();

    transient ArrayList<InfluenceMatrixChangeListener> influenceMatrixChangeListeners;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Sets the influence from one variable to another.
	 * 
	 * @param from
	 *            first variable
	 * @param to
	 *            another variable as the first one
	 * @param influence
	 *            int value of the influence
	 */
    public void setInfluence(Variable from, Variable to, int influence) {
        if (from == to) return;
        if (!influences.containsKey(from)) {
            influences.put(from, new HashMap<Variable, Integer>());
        }
        HashMap<Variable, Integer> mapTo = influences.get(from);
        mapTo.put(to, influence);
    }

    public int getInfluence(Variable from, Variable to) {
        if (from == to) return 0;
        if (!influences.containsKey(from)) return 0;
        HashMap<Variable, Integer> mapTo = influences.get(from);
        if (!mapTo.containsKey(to)) return 0;
        return mapTo.get(to);
    }

    public int getActiveSum(Variable var) {
        int retVal = 0;
        for (Variable varTo : Program.sensitivityModel.getAllVariables()) {
            retVal += getInfluence(var, varTo);
        }
        return retVal;
    }

    public int getPassiveSum(Variable var) {
        int retVal = 0;
        for (Variable varFrom : Program.sensitivityModel.getAllVariables()) {
            retVal += getInfluence(varFrom, var);
        }
        return retVal;
    }

    public int getProduct(Variable var) {
        return getActiveSum(var) * getPassiveSum(var);
    }

    public int getQuotient(Variable var) {
        int passiveSum = getPassiveSum(var);
        if (passiveSum == 0) return 0;
        return (int) ((double) getActiveSum(var) / passiveSum * 100);
    }

    public void addInfluenceMatrixChangeListener(InfluenceMatrixChangeListener l) {
        if (influenceMatrixChangeListeners == null) {
            influenceMatrixChangeListeners = new ArrayList<InfluenceMatrixChangeListener>();
        }
        influenceMatrixChangeListeners.add(l);
    }

    public void removeInfluenceMatrixChangeListener(InfluenceMatrixChangeListener l) {
        if (influenceMatrixChangeListeners != null) {
            influenceMatrixChangeListeners.remove(l);
        }
    }

    /**
	 * This event is fired if the matrix changes.
	 */
    public void fireInfluenceMatrixChanged() {
        for (InfluenceMatrixChangeListener l : influenceMatrixChangeListeners) {
            l.InfluenceMatrixChanged();
        }
    }

    public int compareTo(Object o) {
        return toString().compareTo(o.toString());
    }
}
