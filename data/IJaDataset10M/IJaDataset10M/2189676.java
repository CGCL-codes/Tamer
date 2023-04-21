package org.semtinel.core.analysis.icdiff;

import java.awt.Color;
import java.util.logging.Logger;
import org.semtinel.core.data.api.ConceptScheme;
import org.semtinel.core.experiments.api.AbstractFactory;
import org.semtinel.core.experiments.api.Dataprovider;
import org.semtinel.core.experiments.api.DataproviderFactory;
import org.semtinel.core.experiments.api.Experiment;
import org.semtinel.core.experiments.api.Flavor;
import org.semtinel.core.experiments.api.FlavorImpl;
import org.semtinel.core.register.RegisterDefinition;
import org.semtinel.core.register.RegisterSet;
import org.semtinel.core.visuals.treemap.TreemapView;
import org.semtinel.core.visuals.treemap.TreemapVisualisation;
import org.semtinel.core.visuals.treemap.TreemapVisualisationFactory;

/**
 *
 * @author kai
 */
public class IICDataproviderFactory extends AbstractFactory implements DataproviderFactory {

    private Logger log = Logger.getLogger(getClass().getName());

    public static final Flavor FLAVOR = new FlavorImpl(IICDataproviderFactory.class.getName());

    private static final IICDataproviderFactory factory = new IICDataproviderFactory();

    private IICDataproviderFactory() {
    }

    public static IICDataproviderFactory getFactory() {
        return factory;
    }

    public Flavor getAnalysisFlavor() {
        return IICAnalysisFactory.FLAVOR;
    }

    public String getDisplayName() {
        return "Intrinsic Information Content";
    }

    public Flavor getFlavor() {
        return FLAVOR;
    }

    public Dataprovider getInstance() {
        return new IICDataprovider(this);
    }

    public RegisterDefinition[] getRegisterDefinitions() {
        return new RegisterDefinition[] {};
    }

    public Flavor getVisualisationFlavor() {
        return TreemapVisualisationFactory.FLAVOR;
    }

    private class IICDataprovider implements Dataprovider {

        private DataproviderFactory factory;

        public IICDataprovider(DataproviderFactory factory) {
            this.factory = factory;
        }

        public boolean isSuitable(Experiment experiment) {
            return experiment.getVisualisation() instanceof TreemapVisualisation && experiment.getAnalysis() instanceof IICAnalysis;
        }

        public void update(Experiment experiment) {
            if (isSuitable(experiment)) {
                log.finer("Treemap Visualisation and IIC Analysis active, great!");
                TreemapVisualisation v = (TreemapVisualisation) experiment.getVisualisation();
                Object value = experiment.getRegisterSet().getRegister("concept_scheme").getValue();
                if (value == null) {
                    v.setRoot(null);
                } else if (!(value instanceof ConceptScheme)) {
                    throw new RuntimeException("Wrong type in register: " + value);
                }
                ConceptScheme cs = (ConceptScheme) value;
                v.setColorMetricProvider(new AnalysisColorMetricProvider((IICAnalysis) experiment.getAnalysis(), experiment));
                v.setColor(TreemapView.RED, Color.WHITE, TreemapView.BLUE, 0f, 1f);
                v.setRoot(cs);
                v.refresh();
            }
        }

        public DataproviderFactory getFactory() {
            return factory;
        }

        public void close() {
        }

        public boolean hasRequiredData(RegisterSet registerSet) {
            return true;
        }
    }
}
