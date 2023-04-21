package org.uimafit.component;

import org.apache.uima.UimaContext;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.component.initialize.ConfigurationParameterInitializer;
import org.uimafit.component.initialize.ExternalResourceInitializer;
import org.uimafit.descriptor.OperationalProperties;
import org.uimafit.util.ExtendedLogger;

/**
 * Base class for a JCas multiplier which initializes itself based on annotations.
 *
 * @author Richard Eckart de Castilho
 */
@OperationalProperties(outputsNewCases = true)
public abstract class JCasMultiplier_ImplBase extends org.apache.uima.analysis_component.JCasMultiplier_ImplBase {

    private ExtendedLogger logger;

    public ExtendedLogger getLogger() {
        if (logger == null) {
            logger = new ExtendedLogger(getContext());
        }
        return logger;
    }

    @Override
    public void initialize(final UimaContext context) throws ResourceInitializationException {
        super.initialize(context);
        ConfigurationParameterInitializer.initialize(this, context);
        ExternalResourceInitializer.initialize(context, this);
    }
}
