package uk.org.sith.jecmaunit.runner.engine.rhino;

import org.junit.runners.model.InitializationError;
import uk.org.sith.jecmaunit.engine.Engine;
import uk.org.sith.jecmaunit.engine.rhino.RhinoEngine;
import uk.org.sith.jecmaunit.runner.engine.EngineRunner;

/**
 * Rhino-specific runner
 * 
 * @author Antony Lees
 */
public class RhinoRunner extends EngineRunner {

    private Engine engine;

    /**
	 * Creates a new runner for the given test class
	 * @param klass the test class containing the tests
	 */
    public RhinoRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.engine = new RhinoEngine();
    }
}
