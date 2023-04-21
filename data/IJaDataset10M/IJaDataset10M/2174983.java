package jstress.run;

import java.util.Properties;
import jstress.task.TaskRunner;

/**
 * RunStrategyFactory is responsible for passing back an appropriate RunStrategy
 * based on a String that is passed in.  It is also responsible for handling
 * parameters passed in to individual RunStrategies.
 *
 * The properties for configuring RunStrategy instances are stored by default in
 * a file named "jstress.properties" found in the current direcctory. This value
 * can be overridden by setting the key "jstress.configuration" on the
 * System.properties.
 *
 * Current available RunStrategies are:
 *   "BurstRun"              == jstress.run.BurstRun
 *   "LinearRun"             == jstress.run.LinearRun
 *   "LinearStepRun"         == jstress.run.LinearStepRun
 *   "SerializedRun"         == jstress.run.SerializedRun
 *   "InfiniteSerializedRun" == jstress.run.InfiniteSerializedRun
 *
 * @see RunStrategy
 */
public class RunStrategyFactory {

    public static final String STRATEGY_PACKAGE = "jstress.run.";

    public RunStrategyFactory() {
    }

    public Properties getStrategyProperties() {
        return TaskRunner.getProperties();
    }

    /**
   * Loads the given RunStrategy by name. Strategies are loaded by
   * pre-pending the "jstress.run" package name and loading them
   * via the current class loader. Custom strategies can be loaded
   * if the name is a fully qualified class name.
   */
    public RunStrategy getRunStrategy(String strategyName) {
        RunStrategy runStrategy = loadStrategy(strategyName);
        if (runStrategy != null) runStrategy.applyProperties(getStrategyProperties());
        return runStrategy;
    }

    private RunStrategy loadStrategy(String strategyName) {
        if (strategyName.indexOf(".") == -1) strategyName = STRATEGY_PACKAGE + strategyName;
        try {
            return (RunStrategy) Class.forName(strategyName).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("could not instantiate given strategy: " + strategyName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("could not access given strategy: " + strategyName, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("could not find given strategy: " + strategyName, e);
        }
    }
}
