package org.kalypso.nofdpidss.measure.construction.edit.finish;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.measure.construction.edit.finish.common.AbstractFinishWorker;
import org.kalypso.nofdpidss.measure.construction.edit.pages.IMeasureEditBufferStrip;
import org.kalypso.ogc.gml.command.ChangeFeaturesCommand;
import org.kalypso.ogc.gml.command.FeatureChange;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class FinishWorkerBufferStripDetails extends AbstractFinishWorker {

    private final Feature m_feature;

    private final IMeasureEditBufferStrip m_bs;

    public FinishWorkerBufferStripDetails(final Feature feature, final IMeasureEditBufferStrip bs) {
        m_feature = feature;
        m_bs = bs;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.edit.finish.AbstractFinishWorker#perform()
   */
    @Override
    public ChangeFeaturesCommand[] perform() {
        final List<ChangeFeaturesCommand> myCommands = new ArrayList<ChangeFeaturesCommand>();
        final List<FeatureChange> changes = new ArrayList<FeatureChange>();
        final IFeatureType ft = m_feature.getFeatureType();
        changes.add(new FeatureChange(m_feature, ft.getProperty(GmlConstants.QN_MEASURE_MEASURE_LINE_BUFFER_WIDTH), m_bs.getLineStringBufferWidth()));
        myCommands.add(new ChangeFeaturesCommand(m_feature.getWorkspace(), changes.toArray(new FeatureChange[] {})));
        changes.clear();
        return myCommands.toArray(new ChangeFeaturesCommand[] {});
    }
}
