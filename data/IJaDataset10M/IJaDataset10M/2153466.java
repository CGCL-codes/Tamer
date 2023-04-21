package uk.ac.imperial.ma.metric.exerciseEngine.classic;

import java.util.Map;
import java.util.TreeMap;
import uk.ac.imperial.ma.metric.exerciseEngine.Configuration;
import uk.ac.imperial.ma.metric.exerciseEngine.MathObject;

/**
 * An implementation of <code>Configuration</code> which is suitable for the
 * "metric-classic" exercises.
 *
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 15 Oct 2008
 */
public class MetricClassicConfiguration implements Configuration {

    private Map<String, MathObject> parameters;

    private Map<String, MathObject> variables;

    private double tolerance;

    public MetricClassicConfiguration() {
        this.parameters = new TreeMap<String, MathObject>();
        this.variables = new TreeMap<String, MathObject>();
        this.tolerance = 0.00001;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.exerciseEngine.Configuration#addParameter(String, uk.ac.imperial.ma.metric.exerciseEngine.MathObject)
	 */
    @Override
    public void addParameter(String identifier, MathObject parameter) {
        parameters.put(identifier, parameter);
    }

    /**
	 * @see uk.ac.imperial.ma.metric.exerciseEngine.Configuration#addVariable(String, uk.ac.imperial.ma.metric.exerciseEngine.MathObject)
	 */
    @Override
    public void addVariable(String identifier, MathObject variable) {
        variables.put(identifier, variable);
    }

    /**
	 * @see uk.ac.imperial.ma.metric.exerciseEngine.Configuration#initialise()
	 */
    @Override
    public void initialise() {
        for (MathObject parameter : parameters.values()) {
            parameter.initialise();
        }
        for (MathObject variable : variables.values()) {
            variable.initialise();
        }
    }

    /**
	 * @see uk.ac.imperial.ma.metric.exerciseEngine.Configuration#setTolerance(double)
	 */
    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
	 * @see uk.ac.imperial.ma.metric.exerciseEngine.Configuration#getTolerance()
	 */
    @Override
    public double getTolerance() {
        return tolerance;
    }
}
