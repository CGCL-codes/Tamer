package org.kalypso.nofdpidss.measure.construction.edit.finish.common;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasure;
import org.kalypso.nofdpidss.measure.construction.edit.pages.PageMeasureGeneralInformation;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypso.ogc.gml.command.ChangeFeaturesCommand;
import org.kalypso.ogc.gml.command.FeatureChange;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class FinishWorkerCommonDetails extends AbstractFinishWorker {

    private final Feature m_feature;

    private final PageMeasureGeneralInformation m_pageMeasureCommonDetails;

    public FinishWorkerCommonDetails(final Feature feature, final PageMeasureGeneralInformation measureDetails) {
        m_feature = feature;
        m_pageMeasureCommonDetails = measureDetails;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measureconstruction.AbstractFinishWorker#perform()
   */
    @Override
    public ChangeFeaturesCommand[] perform() {
        final List<ChangeFeaturesCommand> myCommands = new ArrayList<ChangeFeaturesCommand>();
        final List<FeatureChange> changes = new ArrayList<FeatureChange>();
        final IFeatureType ft = m_feature.getFeatureType();
        final String name = m_pageMeasureCommonDetails.getMeasureName();
        changes.add(new FeatureChange(m_feature, ft.getProperty(GmlConstants.QN_MEASURE_MEASURE_NAME), name));
        final String description = m_pageMeasureCommonDetails.getMeasureDescription();
        changes.add(new FeatureChange(m_feature, ft.getProperty(GmlConstants.QN_MEASURE_MEASURE_DESCRIPTION), description));
        final Feature img = m_pageMeasureCommonDetails.getMeasureImage();
        if (img != null) changes.add(FeatureUtils.getLinkedFeatureChange(m_feature, IMeasure.QN_IMAGE, "projectInfo.gml#" + img.getId()));
        myCommands.add(new ChangeFeaturesCommand(m_feature.getWorkspace(), changes.toArray(new FeatureChange[] {})));
        changes.clear();
        return myCommands.toArray(new ChangeFeaturesCommand[] {});
    }
}
