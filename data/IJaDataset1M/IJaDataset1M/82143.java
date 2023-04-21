package org.kaiec.timeanalysis;

import org.semtinel.core.experiments.api.AbstractFactory;
import org.semtinel.core.experiments.api.AnalysisFactory;
import org.semtinel.core.experiments.api.Flavor;
import org.semtinel.core.experiments.api.FlavorImpl;
import org.semtinel.core.register.RegisterDefinition;

/**
 *
 * @author kai
 */
public class SetBasedTimeSeriesAnalysisFactory2 extends AbstractFactory implements AnalysisFactory {

    public static final Flavor FLAVOR = new FlavorImpl(SetBasedTimeSeriesAnalysisFactory.class.getName());

    private static final SetBasedTimeSeriesAnalysisFactory2 factory = new SetBasedTimeSeriesAnalysisFactory2();

    private SetBasedTimeSeriesAnalysisFactory2() {
    }

    ;

    public static SetBasedTimeSeriesAnalysisFactory2 getFactory() {
        return factory;
    }

    public Flavor getFlavor() {
        return FLAVOR;
    }

    public String getDisplayName() {
        return "Set Based Time Series 2";
    }

    public String getId() {
        return getClass().getName();
    }

    public SetBasedTimeSeriesAnalysis2 getInstance() {
        return new SetBasedTimeSeriesAnalysis2(this);
    }

    public RegisterDefinition[] getRegisterDefinitions() {
        return new RegisterDefinition[] { RegisterDefinition.CONCEPT_SCHEME, RegisterDefinition.ANNOTATION_SET_SERIES };
    }
}
