package org.kalypso.nofdpidss.profiles.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.core.KalypsoCorePlugin;
import org.kalypso.gmlschema.feature.IFeatureType;
import org.kalypso.gmlschema.property.IPropertyType;
import org.kalypso.gmlschema.property.relation.IRelationType;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.commands.AtomarAddFeatureCommand;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IWaterBodies;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolGeoData;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolHydraulicModel;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.utils.modules.CrossSectionMapTool;
import org.kalypso.nofdpidss.profiles.i18n.Messages;
import org.kalypso.nofdpidss.profiles.wizard.waterbody.PageWaterBodyDetails;
import org.kalypso.ogc.gml.mapmodel.CommandableWorkspace;
import org.kalypso.ogc.gml.selection.IFeatureSelectionManager;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class WizardCreateWaterBody extends Wizard implements IWorkbenchWizard {

    private PageWaterBodyDetails m_waterBodyDetails;

    private final PoolHydraulicModel m_pool;

    public WizardCreateWaterBody(final PoolHydraulicModel pool) {
        m_pool = pool;
        setHelpAvailable(false);
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#addPages()
   */
    @Override
    public void addPages() {
        setWindowTitle(Messages.WizardCreateWaterBody_0);
        m_waterBodyDetails = new PageWaterBodyDetails();
        addPage(m_waterBodyDetails);
    }

    /**
   * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
   *      org.eclipse.jface.viewers.IStructuredSelection)
   */
    public void init(final IWorkbench workbench, final IStructuredSelection selection) {
    }

    /**
   * @see org.eclipse.jface.wizard.Wizard#performFinish()
   */
    @Override
    public boolean performFinish() {
        try {
            final String name = m_waterBodyDetails.getWaterBodyName();
            final String description = m_waterBodyDetails.getWaterBodyDescription();
            final CommandableWorkspace workspace = m_pool.getWorkspace();
            final Feature root = workspace.getRootFeature();
            final IRelationType prop = (IRelationType) root.getFeatureType().getProperty(IWaterBodies.QN_HYDRAULIC_WATER_BODY_MEMBER);
            final IFeatureType targetType = prop.getTargetFeatureType();
            final IFeatureSelectionManager selectionManager = KalypsoCorePlugin.getDefault().getSelectionManager();
            final List<String> names = new ArrayList<String>();
            names.add(name);
            final Map<IPropertyType, Object> map = new HashMap<IPropertyType, Object>();
            map.put(targetType.getProperty(Feature.QN_NAME), names);
            map.put(targetType.getProperty(Feature.QN_DESCRIPTION), description);
            final AtomarAddFeatureCommand command = new AtomarAddFeatureCommand(workspace, targetType, root, prop, -1, map, selectionManager);
            m_pool.postCommand(command, null);
            final PoolGeoData gdPool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
            CrossSectionMapTool.addWaterBody(gdPool, command.getNewFeature());
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return false;
        }
        return true;
    }
}
