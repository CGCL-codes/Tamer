package org.jgap.impl;

import java.util.*;
import org.jgap.*;

/**
 * Extension of IntegerGene. The only difference is that this gene allele has to
 * be a multiple of a specific significance, ie
 *
 *    Gene gene = new MutipleIntegerGene(configuration, -100,100,3);
 *
 * The genes allele must be between -100 and 100 and be a multiple of 3.
 *
 * @author Chris Jones
 * @since 3.5
 */
public class MutipleIntegerGene extends NumberGene implements IPersistentRepresentation {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.2 $";

    /**
   * The upper bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Integer.MAX_VALUE.
   */
    private int m_upperBounds;

    /**
   * The lower bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Integer.MIN_VALUE
   */
    private int m_lowerBounds;

    /**
   * The lower bounds of values represented by this Gene. If not explicitly
   * provided by the user, this should be set to Integer.MIN_VALUE
   */
    private int m_significance;

    /**
   * Represents the constant range of values supported by integers.
   */
    protected static final long INTEGER_RANGE = (long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE;

    public MutipleIntegerGene() throws InvalidConfigurationException {
        this(Genotype.getStaticConfiguration());
    }

    /**
   * Constructs a new IntegerGene with default settings. No bounds will
   * be put into effect for values (alleles) of this Gene instance, other
   * than the standard range of Integer values.
   *
   * @param a_config the configuration to use
   * @throws InvalidConfigurationException
   *
   * @author Klaus Meffert
   * @since 3.5
   */
    public MutipleIntegerGene(final Configuration a_config) throws InvalidConfigurationException {
        this(a_config, -(Integer.MAX_VALUE / 2), Integer.MAX_VALUE / 2, 1);
    }

    /**
   * Constructs a new IntegerGene with the specified lower and upper
   * bounds for values (alleles) of this Gene instance.
   *
   * @param a_config the configuration to use
   * @param a_lowerBound the lowest value that this Gene may possess,
   * inclusively
   * @param a_upperBound the highest value that this Gene may possess,
   * inclusively
   * @throws InvalidConfigurationException
   *
   * @author Chris Johnes
   * @since 3.5
   */
    public MutipleIntegerGene(final Configuration a_config, final int a_lowerBound, final int a_upperBound, final int a_significance) throws InvalidConfigurationException {
        super(a_config);
        m_lowerBounds = a_lowerBound;
        m_upperBounds = a_upperBound;
        m_significance = a_significance;
        if (m_upperBounds - m_lowerBounds < m_significance) {
            int test = round((m_upperBounds + m_lowerBounds) / 2, m_significance);
            if (test < m_lowerBounds || test > m_upperBounds) {
                throw new IllegalArgumentException("Lower and upper bound do not match significance!");
            }
        }
    }

    /**
   * Provides implementation-independent means for creating new Gene
   * instances.
   *
   * @return a new Gene instance of the same type and with the same setup as
   * this concrete Gene
   *
   * @author Chris Jones
   * @since 3.5
   */
    protected Gene newGeneInternal() {
        try {
            MutipleIntegerGene result = new MutipleIntegerGene(getConfiguration(), m_lowerBounds, m_upperBounds, m_significance);
            return result;
        } catch (InvalidConfigurationException iex) {
            throw new IllegalStateException(iex.getMessage());
        }
    }

    /**
   * Retrieves a string representation of this Gene that includes any
   * information required to reconstruct it at a later time, such as its
   * value and internal state. This string will be used to represent this
   * Gene in XML persistence. This is an optional method but, if not
   * implemented, XML persistence and possibly other features will not be
   * available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @return string representation of this Gene's current state
   *
   * @author Chris Jones
   * @since 3.5
   */
    public String getPersistentRepresentation() {
        String s;
        if (getInternalValue() == null) {
            s = "null";
        } else {
            s = getInternalValue().toString();
        }
        return s + PERSISTENT_FIELD_DELIMITER + m_lowerBounds + PERSISTENT_FIELD_DELIMITER + m_upperBounds + PERSISTENT_FIELD_DELIMITER + m_significance;
    }

    /**
   * Sets the value and internal state of this Gene from the string
   * representation returned by a previous invocation of the
   * getPersistentRepresentation() method. This is an optional method but,
   * if not implemented, XML persistence and possibly other features will not
   * be available. An UnsupportedOperationException should be thrown if no
   * implementation is provided.
   *
   * @param a_representation the string representation retrieved from a
   * prior call to the getPersistentRepresentation() method
   *
   * @throws UnsupportedOperationException to indicate that no implementation
   * is provided for this method
   * @throws UnsupportedRepresentationException if this Gene implementation
   * does not support the given string representation
   *
   * @author Chris Jones
   * @since 3.5
   */
    public void setValueFromPersistentRepresentation(final String a_representation) throws UnsupportedRepresentationException {
        if (a_representation != null) {
            StringTokenizer tokenizer = new StringTokenizer(a_representation, PERSISTENT_FIELD_DELIMITER);
            if (tokenizer.countTokens() != 4) {
                throw new UnsupportedRepresentationException("The format of the given persistent representation " + " is not recognized: it does not contain three tokens: " + a_representation);
            }
            String valueRepresentation = tokenizer.nextToken();
            String lowerBoundRepresentation = tokenizer.nextToken();
            String upperBoundRepresentation = tokenizer.nextToken();
            String significanceRepresentation = tokenizer.nextToken();
            if (valueRepresentation.equals("null")) {
                setAllele(null);
            } else {
                try {
                    setAllele(new Integer(Integer.parseInt(valueRepresentation)));
                } catch (NumberFormatException e) {
                    throw new UnsupportedRepresentationException("The format of the given persistent representation " + "is not recognized: field 1 does not appear to be " + "an integer value.");
                }
            }
            try {
                m_lowerBounds = Integer.parseInt(lowerBoundRepresentation);
            } catch (NumberFormatException e) {
                throw new UnsupportedRepresentationException("The format of the given persistent representation " + "is not recognized: field 2 does not appear to be " + "an integer value.");
            }
            try {
                m_upperBounds = Integer.parseInt(upperBoundRepresentation);
            } catch (NumberFormatException e) {
                throw new UnsupportedRepresentationException("The format of the given persistent representation " + "is not recognized: field 3 does not appear to be " + "an integer value.");
            }
            try {
                m_significance = Integer.parseInt(significanceRepresentation);
            } catch (NumberFormatException e) {
                throw new UnsupportedRepresentationException("The format of the given persistent representation " + "is not recognized: field 3 does not appear to be " + "an integer value.");
            }
        }
    }

    /**
   * Sets the value (allele) of this Gene to a random Integer value between
   * the lower and upper bounds (if any) of this Gene.
   *
   * @param a_numberGenerator the random number generator that should be
   * used to create any random values. It's important to use this generator to
   * maintain the user's flexibility to configure the genetic engine to use the
   * random number generator of their choice
   *
   * @author Chris Jones
   * @since 3.5
   */
    public void setToRandomValue(final RandomGenerator a_numberGenerator) {
        setAllele(new Integer(getRandomValue(a_numberGenerator)));
    }

    private Integer getRandomValue(RandomGenerator a_numberGenerator) {
        double randomValue = ((long) m_upperBounds - (long) m_lowerBounds) * a_numberGenerator.nextDouble() + m_lowerBounds;
        return round(randomValue, m_significance);
    }

    private int round(double value, Integer factor) {
        if (value % factor == 0) {
            return (int) value;
        }
        int floor = (int) ((value / factor)) * factor;
        int ceiling = floor + factor;
        if (ceiling - value <= value - floor && ceiling <= m_upperBounds) {
            return ceiling;
        } else if (floor >= m_lowerBounds) {
            return floor;
        } else {
            return ceiling;
        }
    }

    /**
   * Compares to objects by first casting them into their expected type
   * (e.g. Integer for IntegerGene) and then calling the compareTo-method
   * of the casted type.
   * @param a_o1 first object to be compared, always is not null
   * @param a_o2 second object to be compared, always is not null
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the object provided for comparison
   *
   * @author Neil Rostan
   * @since 3.5
   */
    protected int compareToNative(final Object a_o1, final Object a_o2) {
        return ((Integer) a_o1).compareTo((Integer) a_o2);
    }

    /**
   * Maps the value of this IntegerGene to within the bounds specified by
   * the m_upperBounds and m_lowerBounds instance variables. The value's
   * relative position within the integer range will be preserved within the
   * bounds range (in other words, if the value is about halfway between the
   * integer max and min, then the resulting value will be about halfway
   * between the upper bounds and lower bounds). If the value is null or
   * is already within the bounds, it will be left unchanged.
   *
   * @author Neil Rostan
   * @author Klaus Meffert
   * @since 1.0
   */
    protected void mapValueToWithinBounds() {
        if (getAllele() != null) {
            Integer i_value = ((Integer) getAllele());
            if (i_value.intValue() > m_upperBounds || i_value.intValue() < m_lowerBounds) {
                RandomGenerator rn;
                if (getConfiguration() != null) {
                    rn = getConfiguration().getRandomGenerator();
                } else {
                    rn = new StockRandomGenerator();
                }
                if (m_upperBounds == m_lowerBounds) {
                    setAllele(new Integer(m_lowerBounds));
                } else {
                    setToRandomValue(rn);
                }
            }
        }
    }

    /**
   * See interface Gene for description.
   * @param a_index ignored (because there is only 1 atomic element)
   * @param a_percentage percentage of mutation (greater than -1 and smaller
   * than 1)
   *
   * @author Chris Jones
   * @since 3.5
   */
    public void applyMutation(final int a_index, final double a_percentage) {
        double range = ((long) m_upperBounds - (long) m_lowerBounds) * a_percentage;
        if (getAllele() == null) {
            setAllele(new Integer((int) range + m_lowerBounds));
        } else {
            Integer i_value = ((Integer) getAllele());
            int newValue = (int) Math.round(i_value.intValue() + range);
            newValue = round(newValue, m_significance);
            setAllele(new Integer(newValue));
        }
    }

    /**
   * Modified hashCode() function to return different hashcodes for differently
   * ordered genes in a chromosome.
   * @return -1 if no allele set, otherwise value return by BaseGene.hashCode()
   *
   * @author Klaus Meffert
   * @since 3.5
   */
    public int hashCode() {
        if (getInternalValue() == null) {
            return -1;
        } else {
            return super.hashCode();
        }
    }

    /**
   * @return string representation of this Gene's value that may be useful for
   * display purposes
   *
   * @author Chris Jones
   * @since 3.5
   */
    public String toString() {
        String s = "IntegerGene(" + m_lowerBounds + "," + m_upperBounds + "," + m_significance + ")" + "=";
        if (getInternalValue() == null) {
            s += "null";
        } else {
            s += getInternalValue().toString();
        }
        return s;
    }

    /**
   * @return the lower bounds of the integer gene
   *
   * @author Klaus Meffert
   * @since 3.5
   */
    public int getLowerBounds() {
        return m_lowerBounds;
    }

    /**
   * @return the upper bounds of the integer gene
   *
   * @author Klaus Meffert
   * @since 3.5
   */
    public int getUpperBounds() {
        return m_upperBounds;
    }

    /**
   * @return the upper bounds of the integer gene
   *
   * @author Klaus Meffert
   * @since 3.5
   */
    public int getSignificance() {
        return m_significance;
    }
}
