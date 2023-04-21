package org.kalypso.nofdpidss.core.base.gml.model.floodrisk.implementation;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.nofdpidss.core.base.gml.model.floodrisk.IFloodRiskEmptyTemplate;
import org.kalypso.nofdpidss.core.base.gml.model.floodrisk.IFloodRiskEmptyTemplates;
import org.kalypso.nofdpidss.core.base.gml.model.floodrisk.IFloodRiskSetupModel;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;

/**
 * @author Dirk Kuch
 */
public class FloodRiskEmptyTemplatesHandler extends AbstractFloodRiskSetupModelListWrapper implements IFloodRiskEmptyTemplates {

    public FloodRiskEmptyTemplatesHandler(final IFloodRiskSetupModel model, final FeatureList list) {
        super(model, list);
    }

    /**
   * @see org.kalypso.nofdpidss.core.base.gml.flood.risk.IFloodRiskEmptyTemplates#getTemplates()
   */
    public IFloodRiskEmptyTemplate[] getTemplates() {
        final List<IFloodRiskEmptyTemplate> myList = new ArrayList<IFloodRiskEmptyTemplate>();
        for (final Object obj : this) {
            if (!(obj instanceof Feature)) continue;
            final Feature f = (Feature) obj;
            myList.add(new FloodRiskEmptyTemplateHandler(getModel(), f));
        }
        return myList.toArray(new IFloodRiskEmptyTemplate[] {});
    }
}
