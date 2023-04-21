package org.jquantlib.pricingengines.arguments;

import org.jquantlib.processes.StochasticProcess;

/**
 * Arguments for single-asset option calculation
 * 
 * @note This inner class must be kept <b>private</b> as its fields and
 *       ancertor's fields are exposed. This programming style is not
 *       recommended and we should use getters/setters instead. At the moment,
 *       we keep the original implementation.
 * 
 * @author Richard Gomes
 */
public class OneAssetOptionArguments extends OptionArguments {

    public StochasticProcess stochasticProcess;

    public OneAssetOptionArguments() {
        super();
    }

    @Override
    public void validate() {
        super.validate();
        if (stochasticProcess.initialValues()[0] <= 0.0) throw new IllegalArgumentException("Negative or zero underlying given");
    }
}
