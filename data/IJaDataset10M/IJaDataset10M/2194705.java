package org.kalypso.nofdpidss.ui.view.interactiveplanning.navigation.measureconstruction;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.jface.wizard.IUpdateable;
import org.kalypso.core.KalypsoCorePlugin;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolHelper;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.measures.MCUtils.MEASURE;
import org.kalypso.nofdpidss.measure.construction.create.MeasureBuilder;
import org.kalypso.nofdpidss.measure.construction.create.interfaces.IMeasureConstructionCreationHandler;
import org.kalypso.nofdpidss.measure.construction.edit.WizardEditMeasure;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.mapmodel.CommandableWorkspace;
import org.kalypso.ogc.gml.selection.EasyFeatureWrapper;
import org.kalypso.ogc.gml.selection.IFeatureSelectionManager;
import org.kalypsodeegree.model.feature.Feature;

/**
 * @author Dirk Kuch
 */
public class MeasureConstructionCreationHandler implements IMeasureConstructionCreationHandler {

    protected final PoolProject m_projectPool;

    private final IFeatureSelectionManager m_mgrSelection;

    private IMapPanel m_mapPanel;

    public MeasureConstructionCreationHandler(final PoolProject poolProject) {
        m_projectPool = poolProject;
        m_mgrSelection = KalypsoCorePlugin.getDefault().getSelectionManager();
    }

    public void callPostMeasureCreationWizard(final Feature feature) {
        final UIJob job = new UIJob(Messages.MeasureConstructionCreationHandler_0) {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                final WizardEditMeasure wizard = new WizardEditMeasure(feature, false);
                wizard.init(PlatformUI.getWorkbench(), null);
                final WizardDialog dialog = new WizardDialog(null, wizard);
                dialog.addPageChangedListener(new IPageChangedListener() {

                    public void pageChanged(final PageChangedEvent event) {
                        Object page = event.getSelectedPage();
                        if (page instanceof IUpdateable) {
                            IUpdateable update = (IUpdateable) page;
                            update.update();
                        }
                    }
                });
                dialog.open();
                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    public void createNewMeasure(final MEASURE measure) {
        try {
            final MeasureBuilder builder = MeasureBuilder.getBuilder(m_projectPool, this, measure);
            if (builder == null) return;
            builder.init();
            final Feature feature = builder.getMeasure();
            final CommandableWorkspace workspace = PoolHelper.getCommandableWorkspace(feature.getWorkspace());
            m_mgrSelection.setSelection(new EasyFeatureWrapper[] { new EasyFeatureWrapper(workspace, feature, feature.getParent(), feature.getParentRelation()) });
            builder.draw();
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        } catch (final InterruptedException e) {
            return;
        }
    }

    public void dispose() {
    }

    public IMapPanel getMapPanel() {
        return m_mapPanel;
    }

    public void setMapPanel(final IMapPanel mapPanel) {
        m_mapPanel = mapPanel;
    }
}
