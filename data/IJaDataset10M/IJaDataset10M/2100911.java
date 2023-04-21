package gnu.trove.function;

/**
 * Interface for functions that accept and return one float primitive.
 */
public interface TFloatFunction {

    /**
     * Execute this function with <tt>value</tt>
     *
     * @param value a <code>float</code> input
     * @return a <code>float</code> result
     */
    public float execute(float value);
}
