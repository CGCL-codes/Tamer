package rafa.math.gen;

import java.beans.PropertyChangeListener;
import java.util.List;
import rafa.Listened;
import rafa.Named;
import rafa.dep.Dependent;

/**
 *
 * @author rafa
 */
public interface INumberGenerator extends Dependent, Named, Listened, PropertyChangeListener {

    /**
     * Properties offered by this object to change listeners.
     * @author rafa
     */
    public static enum Property {

        VALUE_GENERATED, STRATEGY, RESET
    }

    /**
     * Performs an iteration of value generation.
     * @throws Exception
     */
    void generate() throws Exception;

    /**
     * @param index The value index
     * @return The requested value
     * @throws java.lang.Exception 
     */
    Number getValue(int index) throws Exception;

    /**
     * @return Returns the values.
     */
    List<Number> getValues();

    /**
     * Resets the generator to initial values.
     * @throws java.lang.Exception 
     */
    void reset() throws Exception;
}
