package org.kalypso.nofdpidss.measure.construction.base.details;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureDikeConstruction;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class MCDetailBuilderDykeConstruction extends MCAbstractDetailBuilder {

    public MCDetailBuilderDykeConstruction(final Feature selection) {
        super(selection);
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.common.parts.MCAbstractDetailBuilder#getFields()
   */
    @Override
    protected QName[] getFields() {
        final List<QName> myNames = new ArrayList<QName>();
        myNames.add(IMeasureDikeConstruction.QN_CALC_DIKE_CREST_LINE_LENGTH);
        myNames.add(IMeasureDikeConstruction.QN_CALC_PROTECTED_AREA_SIZE);
        return myNames.toArray(new QName[] {});
    }
}
