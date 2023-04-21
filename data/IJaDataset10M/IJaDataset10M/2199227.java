package org.xj4.parameterized.set;

import org.apache.commons.lang.NullArgumentException;

/**
 * Abstract implementation of a parameter set based simply on arrays of objects.
 *
 * @author Jared Bunting
 */
public abstract class AbstractSimpleParameterSet<T> extends AbstractParameterSet {

    private T[] iterationArray;

    public AbstractSimpleParameterSet(String name, String[] definedLabels, T[] iterationArray) {
        super(name, definedLabels);
        this.iterationArray = iterationArray;
    }

    /**
   * The default label is simply the iteration index.
   * @param iteration the iteration to specify the label for
   * @return the index
   */
    protected String defaultLabel(int iteration) {
        return Integer.toString(iteration);
    }

    /**
   * The default name is <code>null</code>.
   * @return <code>null</code> as the default name
   */
    protected String defaultName() {
        return null;
    }

    public int getNumIterations() {
        return iterationArray.length;
    }

    protected T[] getIterationArray() {
        return iterationArray;
    }

    public void validate() {
        for (T iteration : iterationArray) {
            if (iteration == null) {
                throw new NullArgumentException("Iteration cannot be null!");
            }
        }
    }
}
