package org.semtinel.core.visuals.treemap;

import java.util.logging.Logger;
import org.semtinel.core.experiments.api.AbstractFactory;
import org.semtinel.core.experiments.api.Flavor;
import org.semtinel.core.experiments.api.VisualisationFactory;
import org.semtinel.core.experiments.api.FlavorImpl;

/**
 *
 * @author kai
 */
public class TreemapVisualisationFactoryDC extends AbstractFactory implements VisualisationFactory {

    private Logger log = Logger.getLogger(getClass().getName());

    public static final Flavor FLAVOR = new FlavorImpl(TreemapVisualisationFactory.class.getName());

    private static final TreemapVisualisationFactoryDC factory = new TreemapVisualisationFactoryDC();

    private TreemapVisualisationFactoryDC() {
    }

    ;

    public static TreemapVisualisationFactoryDC getFactory() {
        return factory;
    }

    public String getDisplayName() {
        return "Treemap - Direct Children";
    }

    public TreemapVisualisation getInstance() {
        return new TreemapVisualisation(this);
    }

    public Flavor getFlavor() {
        return FLAVOR;
    }
}
