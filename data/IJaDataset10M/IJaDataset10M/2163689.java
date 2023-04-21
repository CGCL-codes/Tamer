package de.fzi.harmonia.commons;

import java.util.Properties;
import de.fzi.harmonia.commons.basematchers.BaseMatcher;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.Evaluable;

/**
 * Mock implementation of a {@link BaseMatcher}
 * that is not valid, because the parameterised constructor is missing.
 * 
 * @author Juergen Bock (bock@fzi.de)
 *
 */
public class MockInvalidBaseMatcher implements BaseMatcher {

    @Override
    public double getEvaluation(Evaluable o) throws InfeasibleEvaluatorException {
        return 0;
    }

    @Override
    public Alignment getAlignmentContext() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Properties getParameters() {
        return null;
    }
}
