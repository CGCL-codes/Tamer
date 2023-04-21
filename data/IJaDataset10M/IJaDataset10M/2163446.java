package ch.lambdaj.function.argument;

/**
 * A factpry for creating arguments placeholder for final classes
 * @author Mario Fusco
 */
public interface FinalClassArgumentCreator<T> {

    /**
     * Create a placeholder for an argument of the final class T using the given seed.
     * @param seed  The seed to generate the unique placeholder
     * @return A placeholder for an argument of class T
     */
    T createArgumentPlaceHolder(int seed);
}
