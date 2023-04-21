package pcgen.cdom.helper;

import pcgen.cdom.base.Constants;

/**
 * A PointCost represents a characteristic and the cost of that characteristic
 * for a point-based spell system.
 */
public class PointCost {

    /**
	 * The characteristic for which this PointCost defines a cost
	 */
    private final String type;

    /**
	 * The point cost of the characterisic
	 */
    private final int cost;

    /**
	 * Constructs a new PointCost for the given characteristic and cost
	 * 
	 * @param key
	 *            The characteristic for which this PointCost defines a cost
	 * @param pointcost
	 *            The point cost of the characterisic
	 * @throws IllegalArgumentException
	 *             if the given characteristic is null
	 */
    public PointCost(String key, int pointcost) {
        if (key == null) {
            throw new IllegalArgumentException("Key for PointCost cannot be null");
        }
        type = key;
        cost = pointcost;
    }

    /**
	 * Returns the characteristic for which this PointCost defines a cost
	 * 
	 * @return The characteristic for which this PointCost defines a cost
	 */
    public String getType() {
        return type;
    }

    /**
	 * Returns the point cost of the characterisic
	 * 
	 * @return The point cost of the characterisic
	 */
    public int getCost() {
        return cost;
    }

    /**
	 * Returns the consistent-with-equals hashCode for this PointCost
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return type.hashCode() ^ cost;
    }

    /**
	 * Returns true if this PointCost is equal to the given Object. Equality is
	 * defined as being another PointCost object with an equal characteristic
	 * and cost
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object o) {
        if (o instanceof PointCost) {
            PointCost other = (PointCost) o;
            return type.equals(other.type) && cost == other.cost;
        }
        return false;
    }

    /**
	 * Returns a String representation of this PointCost, primarily for purposes
	 * of debugging. It is strongly advised that no dependency on this method be
	 * created, as the return value may be changed without warning.
	 * 
	 * @return A String representation of this PointCost
	 */
    @Override
    public String toString() {
        return getType() + Constants.EQUALS + getCost();
    }
}
