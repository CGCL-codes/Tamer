package org.datanucleus.enhancer.samples;

/**
 * @version $Revision: 1.2 $
 */
public class ClassWithPersistentFinalField {

    private final String name;

    private final double cost;

    private final double sell;

    public ClassWithPersistentFinalField(String NAME) {
        name = NAME;
        cost = 55.5;
        sell = 65.7;
    }

    /**
     * @return Returns the cost.
     */
    public double getCost() {
        return cost;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the sell.
     */
    public double getSell() {
        return sell;
    }
}
