package org.kalypso.nofdpidss.measure.construction.edit.finish;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureRetardingBasin;
import org.kalypso.nofdpidss.measure.construction.edit.finish.common.AbstractFinishWorker;
import org.kalypso.nofdpidss.measure.construction.edit.pages.IMeasureEditRetardingBasin;
import org.kalypso.ogc.gml.command.ChangeFeaturesCommand;
import org.kalypso.ogc.gml.command.FeatureChange;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class FinishWorkerRetardingBasinSettings extends AbstractFinishWorker {

    private final Feature m_feature;

    private final IMeasureEditRetardingBasin m_basin;

    public FinishWorkerRetardingBasinSettings(final Feature feature, final IMeasureEditRetardingBasin basin) {
        m_feature = feature;
        m_basin = basin;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.measure.construction.edit.finish.AbstractFinishWorker#perform()
   */
    @Override
    public ChangeFeaturesCommand[] perform() {
        final List<ChangeFeaturesCommand> myCommands = new ArrayList<ChangeFeaturesCommand>();
        final List<FeatureChange> changes = new ArrayList<FeatureChange>();
        final IFeatureType ft = m_feature.getFeatureType();
        changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_CREST_HEIGHT), m_basin.getWeirCrestHeight()));
        changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_CREST_WIDTH), m_basin.getWeirCrestWidth()));
        changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_BOTTOM_LEVEL), m_basin.getBottomLevel()));
        changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_STORAGE_AREA), m_basin.getStorageArea()));
        changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_IS_CONTROLLED), m_basin.isControlledBasin()));
        if (m_basin.getOutletLevel() != null) changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_OUTLET_BOTTOM_LEVEL), m_basin.getOutletLevel()));
        if (m_basin.getOutletWidth() != null) changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_OUTLET_WIDTH), m_basin.getOutletWidth()));
        if (m_basin.getOutletHeight() != null) changes.add(new FeatureChange(m_feature, ft.getProperty(IMeasureRetardingBasin.QN_OUTLET_HEIGHT), m_basin.getOutletHeight()));
        myCommands.add(new ChangeFeaturesCommand(m_feature.getWorkspace(), changes.toArray(new FeatureChange[] {})));
        changes.clear();
        final FeatureChange[] commands = m_basin.getCommands();
        if (commands.length > 0) {
            myCommands.add(new ChangeFeaturesCommand(m_feature.getWorkspace(), commands));
        }
        return myCommands.toArray(new ChangeFeaturesCommand[] {});
    }
}
