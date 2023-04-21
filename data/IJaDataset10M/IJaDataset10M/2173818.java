package org.jsens.businesslogic;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private final List<SimulatableVariable> usedVariables = new ArrayList<SimulatableVariable>();

    private final List<SimulatableEffect> effects = new ArrayList<SimulatableEffect>();

    private Map<Variable, Rectangle2D> variableDimensions = new HashMap<Variable, Rectangle2D>();

    private Map<Effect, List> effectPoints = new HashMap<Effect, List>();

    private int roundIterator;

    public List<SimulatableVariable> getUsedVariables() {
        return usedVariables;
    }

    public void addVariable(SimulatableVariable variable) {
        if (!usedVariables.contains(variable)) {
            usedVariables.add(variable);
        }
    }

    public void removeVariable(SimulatableVariable variable) {
        if (usedVariables.contains(variable)) {
            usedVariables.remove(variable);
        }
    }

    public List<SimulatableEffect> getEffects() {
        return effects;
    }

    public void addEffect(SimulatableEffect effect) {
        if (!effects.contains(effect)) {
            effects.add(effect);
        }
    }

    public void removeEffect(SimulatableEffect effect) {
        if (effects.contains(effect)) {
            effects.remove(effect);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public synchronized int getRoundIterator() {
        return roundIterator;
    }

    public synchronized void incRoundIterator() {
        this.roundIterator++;
    }

    public synchronized void resetRoundIterator() {
        this.roundIterator = 0;
    }

    /**
	 * Returns the variable dimensions. That means the position and the size
	 * inside a graph.
	 * 
	 * @return Map of positions and sizes
	 */
    public Map<Variable, Rectangle2D> getVariableDimensions() {
        return variableDimensions;
    }

    /**
	 * Sets the variable dimensions. Use this setter before you save the object.
	 * 
	 * @param variableDimensions
	 *            Map of positions and sizes
	 */
    public void setVariableDimensions(Map<Variable, Rectangle2D> variableDimensions) {
        this.variableDimensions = variableDimensions;
    }

    /**
	 * Returns the points of the effects. This points represent the coordinates
	 * in the graph where the arrow goes through.
	 * 
	 * @return A map containing the points.
	 */
    public Map<Effect, List> getEffectPoints() {
        return effectPoints;
    }

    /**
	 * Sets the effect points. Use this setter before you save the object.
	 * 
	 * @param effectPoints
	 *            Map of points which the effect line crosses.
	 */
    public void setEffectPoints(Map<Effect, List> effectPoints) {
        this.effectPoints = effectPoints;
    }

    public int compareTo(EffectStructure o) {
        return getName().compareTo(o.getName());
    }
}
