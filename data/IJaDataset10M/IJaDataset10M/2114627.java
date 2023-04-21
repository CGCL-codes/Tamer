package game.models;

import java.io.Serializable;

/**
 * This interface can be implemented by both Input data features and Connectable Models to form hierarchical structures within {@link ModelConnectable}
 */
public interface OutputProducer extends Serializable {

    /**
     * The output can be produced i ACTIVE and PASSIVE modes
     * In Active mode, request is further propagated to inputs (if any)
     * In Passive mode, the cached value is provided
     * @param mode Passive or Active
     */
    public void setMode(Mode mode);

    public Mode getMode();

    /**
     * Provides the output
     * @return output
     */
    public double getOutput();

    /**
     * Provides name of the producer
     * @return name
     */
    public String getName();

    /**
     * Provides name, that is subsequently used in math expressions generated fom models
     * @return name (in case of simple inputs) or expression (in case of models)
     */
    public String toEquation();
}
