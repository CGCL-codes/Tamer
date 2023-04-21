package pitt.search.semanticvectors.vectors;

import java.util.Random;
import pitt.search.semanticvectors.vectors.ComplexVector.Mode;

/**
 * Class for building vectors, designed to be used externally.
 * 
 * @author Dominic Widdows
 */
public class VectorFactory {

    private static final BinaryVector binaryInstance = new BinaryVector(0);

    private static final RealVector realInstance = new RealVector(0);

    private static final ComplexVector complexInstance = new ComplexVector(0, ComplexVector.Mode.POLAR_SPARSE);

    public static Vector createZeroVector(VectorType type, int dimension) {
        switch(type) {
            case BINARY:
                return new BinaryVector(dimension);
            case REAL:
                return new RealVector(dimension);
            case COMPLEX:
                return new ComplexVector(dimension, Mode.POLAR_SPARSE);
            default:
                throw new IllegalArgumentException("Unrecognized VectorType: " + type);
        }
    }

    /**
   * Can be called by external methods that don't explicitly use VectorType enums.
   * This design may be flawed, but it's easy to fix if need be.
   * 
   * @param type must be one of "binary", "real", "complex".
   * @return new vector of the appropriate type and dimension.
   */
    public static Vector createZeroVector(String type, int dimension) {
        return createZeroVector(VectorType.valueOf(type.toUpperCase()), dimension);
    }

    /**
   * Generates an appropriate random vector.
   * 
   * @param type one of the recognized vector types
   * @param dimension number of dimensions in the generated vector
   * @param numEntries total number of non-zero entries; must be no greater than half of dimension
   * @param random random number generator; passed in to enable deterministic testing
   * @return vector generated with appropriate type, dimension and number of nonzero entries
   */
    public static Vector generateRandomVector(VectorType type, int dimension, int numEntries, Random random) {
        if (2 * numEntries > dimension && !type.equals(VectorType.COMPLEX)) {
            throw new RuntimeException("Requested " + numEntries + " to be filled in sparse " + "vector of dimension " + dimension + ". This is not sparse and may cause problems.");
        }
        switch(type) {
            case BINARY:
                return binaryInstance.generateRandomVector(dimension, numEntries, random);
            case REAL:
                return realInstance.generateRandomVector(dimension, numEntries, random);
            case COMPLEX:
                return complexInstance.generateRandomVector(dimension, numEntries, random);
            default:
                throw new IllegalArgumentException("Unrecognized VectorType: " + type);
        }
    }

    public static int getLuceneByteSize(VectorType vectorType, int dimension) {
        switch(vectorType) {
            case BINARY:
                return 8 * ((dimension / 64));
            case REAL:
                return 4 * dimension;
            case COMPLEX:
                return 8 * dimension;
            default:
                throw new IllegalArgumentException("Unrecognized VectorType: " + vectorType);
        }
    }
}
