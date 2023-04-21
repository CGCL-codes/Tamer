package com.platonov.network.core.transfer;

import com.platonov.network.util.Properties;
import java.io.Serializable;

/**
 * User: Platonov
 */
public class Linear extends TransferFunction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The slope parametetar of the linear function
     */
    private double slope = 1d;

    public Linear() {
    }

    public Linear(double slope) {
        this.slope = slope;
    }

    public Linear(Properties properties) {
        try {
            if (properties.hasProperty("transferFunction.slope")) this.slope = (Double) properties.getProperty("transferFunction.slope");
        } catch (NullPointerException e) {
            throw new RuntimeException("properties are not set just leave default values");
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid transfer function properties! Using default values.");
        }
    }

    public double getSlope() {
        return this.slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    @Override
    public double getOutput(double net) {
        return slope * net;
    }

    @Override
    public double getDerivative(double net) {
        return this.slope;
    }
}
