package org.decisiondeck.xmcda_oo.structure;

/**
 * <P>
 * The weight of a criterion. A non-negative value, not necessarily normalized (i.e. may be greater than one).
 * </P>
 * <P>
 * Objects of this type are immutable.
 * </P>
 * 
 * @author Olivier Cailloux
 * 
 */
public class Weight implements Comparable<Weight> {

    private final double m_weight;

    public Weight(final double weight) {
        if (weight < 0f) {
            throw new IllegalArgumentException("Invalid weight: " + weight + ".");
        }
        m_weight = weight;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Weight other = (Weight) obj;
        if (Double.doubleToLongBits(m_weight) != Double.doubleToLongBits(other.m_weight)) {
            return false;
        }
        return true;
    }

    /**
     * @return the weight as a non-negative double.
     */
    public double getValue() {
        return m_weight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final long temp = Double.doubleToLongBits(m_weight);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Weight [" + m_weight + "]";
    }

    @Override
    public int compareTo(Weight w2) {
        return Double.compare(m_weight, w2.m_weight);
    }
}
