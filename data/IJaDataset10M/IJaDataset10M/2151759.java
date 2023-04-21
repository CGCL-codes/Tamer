package engine.individuals;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import engine.Population;
import engine.utils.WevoRandom;

/**
 * Vector of binary numbers.
 *
 * @author Marcin Brodziak (marcin@nierobcietegowdomu.pl)
 */
public class BinaryVector implements Serializable {

    /** Generated serial version UID. */
    private static final long serialVersionUID = 1991576218675992024L;

    /** Binary values in the vector. */
    private boolean[] bits;

    /**
   * Constructs BinaryVector.
   * @param size Size of the vector.
   */
    public BinaryVector(int size) {
        this.bits = new boolean[size];
    }

    /**
   * Constructs BinaryVector from a list of boolean values.
   * @param list Boolean values that form the basis for this individual.
   */
    public BinaryVector(boolean[] list) {
        bits = new boolean[list.length];
        for (int i = 0; i < list.length; i++) {
            bits[i] = list[i];
        }
    }

    /**
   * Sets the ith bit in the vector.
   * @param i Which bit to set.
   * @param value What it set to.
   */
    public void setBit(int i, boolean value) {
        bits[i] = value;
    }

    /**
   * Returns ith bit in the vector.
   * @param i Which bit to return.
   * @return Value of the bit.
   */
    public boolean getBit(int i) {
        return bits[i];
    }

    /**
   * Returns copy of the individual with ith bit set to value.
   * @param i Which bit to negate.
   */
    public void negateBit(int i) {
        this.setBit(i, !this.getBit(i));
    }

    /**
   * Returns size of the individual.
   * @return Size of the individual.
   */
    public int getSize() {
        return bits.length;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof BinaryVector)) {
            return false;
        }
        BinaryVector that = (BinaryVector) object;
        return Arrays.equals(this.bits, that.bits);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean b : bits) {
            sb.append(b ? 1 : 0);
        }
        return sb.toString();
    }

    /**
   * Generates a random individual of given length using given generator.
   * @param generator Random number generator.
   * @param individualLength Size of the individual.
   * @return Binary individual of given length.
   */
    public static BinaryVector generate(final WevoRandom generator, final int individualLength) {
        boolean[] chromosome = new boolean[individualLength];
        for (int j = 0; j < chromosome.length; j++) {
            chromosome[j] = generator.nextBoolean();
        }
        return new BinaryVector(chromosome);
    }

    /**
   * Generates initial population with random binary individuals.
   * @param generator Random number generator.
   * @param individualLength Size of the individual.
   * @param individuals Number of individuals to generate.
   * @return List of binary individuals that will form the basis for first
   * iteration of the algorithm.
   */
    public static Population<BinaryVector> generatePopulationOfRandomBinaryIndividuals(final WevoRandom generator, final int individualLength, final int individuals) {
        List<BinaryVector> result = new LinkedList<BinaryVector>();
        for (int i = 0; i < individuals; i++) {
            result.add(BinaryVector.generate(generator, individualLength));
        }
        return new Population<BinaryVector>(result);
    }
}
