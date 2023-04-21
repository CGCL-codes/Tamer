package org.spantus.math;

import java.util.List;

/**
 * 
 * @author Mindaugas Greibus
 * 
 * @since 0.0.1
 * Created Jun 1, 2009
 *
 */
public class LPCResult {

    private List<Double> result;

    private Double error;

    private List<Double> reflection;

    public List<Double> getResult() {
        return result;
    }

    public void setResult(List<Double> result) {
        this.result = result;
    }

    public Double getError() {
        return error;
    }

    public void setError(Double error) {
        this.error = error;
    }

    public List<Double> getReflection() {
        return reflection;
    }

    public void setReflection(List<Double> reflection) {
        this.reflection = reflection;
    }
}
