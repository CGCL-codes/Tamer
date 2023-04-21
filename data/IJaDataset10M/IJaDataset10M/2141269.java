package net.sf.javailp;

import java.util.HashMap;
import java.util.Map;

/**
 * The class {@code ResultImpl} is a {@code Map} based implementation of the
 * {@link Result}.
 * 
 * @author lukasiewycz
 * 
 */
public class ResultImpl implements Result {

    protected Map<Object, Number> primalValues;

    protected Map<Object, Number> dualValues;

    protected Number objectiveValue = null;

    protected Linear objectiveFunction = null;

    /**
	 * Constructs a {@code ResultImpl} for a {@code Problem} without objective
	 * function.
	 */
    public ResultImpl() {
        super();
        this.primalValues = new HashMap<Object, Number>();
        this.dualValues = new HashMap<Object, Number>();
    }

    /**
	 * Constructs a {@code ResultImpl} for a {@code Problem} with objective
	 * function and the optimal value.
	 */
    public ResultImpl(Number objectiveValue) {
        super();
        this.primalValues = new HashMap<Object, Number>();
        this.dualValues = new HashMap<Object, Number>();
        this.objectiveValue = objectiveValue;
    }

    /**
	 * Constructs a {@code ResultImpl} for a {@code Problem} with an objective
	 * function.
	 */
    public ResultImpl(Linear objectiveFunction) {
        super();
        this.primalValues = new HashMap<Object, Number>();
        this.dualValues = new HashMap<Object, Number>();
        this.objectiveFunction = objectiveFunction;
    }

    public Number getObjective() {
        if (objectiveValue != null) {
            return objectiveValue;
        } else if (objectiveFunction != null) {
            objectiveValue = objectiveFunction.evaluate(this.primalValues);
            return objectiveValue;
        } else {
            return null;
        }
    }

    public boolean getBoolean(Object key) {
        Number number = primalValues.get(key);
        double v = number.doubleValue();
        if (v == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Number get(Object key) {
        return primalValues.get(key);
    }

    public void put(Object key, Number value) {
        primalValues.put(key, value);
    }

    public Number getPrimalValue(Object key) {
        return primalValues.get(key);
    }

    public void putPrimalValue(Object key, Number value) {
        primalValues.put(key, value);
    }

    public Number getDualValue(Object key) {
        return dualValues.get(key);
    }

    public void putDualValue(Object key, Number value) {
        dualValues.put(key, value);
    }

    public Boolean containsVar(Object var) {
        return primalValues.containsKey(var);
    }

    @Override
    public String toString() {
        return "Objective: " + getObjective() + " " + primalValues.toString();
    }

    private static final long serialVersionUID = 1L;
}
