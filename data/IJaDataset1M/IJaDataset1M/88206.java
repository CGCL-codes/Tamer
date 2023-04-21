package org.kalypso.nofdpidss.core.base.gml.model.project.base.implementation;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IEcoHydraulicConveyanceCapacityContainer;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;

/**
 * @author Dirk Kuch
 */
public class EcoHydraulicConveyanceCapacityContainerHandler extends AbstractProjectModelListWrapper implements IEcoHydraulicConveyanceCapacityContainer {

    public EcoHydraulicConveyanceCapacityContainerHandler(final IProjectModel model, final FeatureList list) {
        super(model, list);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.project.IMeasureContainer#getMeasures()
   */
    public IMeasure[] getMeasures() {
        final List<IMeasure> measures = new ArrayList<IMeasure>();
        for (final Object obj : this) {
            if (!(obj instanceof Feature)) continue;
            final Feature feature = (Feature) obj;
            final IMeasure measure = MeasureFabrication.getMeasure(getModel(), feature);
            measures.add(measure);
        }
        return measures.toArray(new IMeasure[] {});
    }
}
