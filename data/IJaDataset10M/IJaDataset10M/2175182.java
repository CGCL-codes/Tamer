package org.kalypso.nofdpidss.analysis.conflict;

import javax.xml.namespace.QName;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.analysis.conflict.tools.CDMatrix;
import org.kalypso.nofdpidss.analysis.i18n.Messages;
import org.kalypso.nofdpidss.analysis.navigation.conflict.ICDMenuPart;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.IModelEventDelegate;
import org.kalypso.nofdpidss.core.base.MyJob;
import org.kalypso.nofdpidss.core.base.MyModelEventListener;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IConflict;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategories;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMap;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMappingMember;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolGeoData;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.utils.gml.CDGmlUtils;
import org.kalypso.nofdpidss.core.view.WindowManager.WINDOW_TYPE;
import org.kalypso.nofdpidss.core.view.parts.IAbstractMenuPart;
import org.kalypso.nofdpidss.core.view.parts.IAbstractViewPart;
import org.kalypso.nofdpidss.core.view.widgets.MySection;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.event.FeatureStructureChangeModellEvent;
import org.kalypsodeegree.model.feature.event.FeaturesChangedModellEvent;
import org.kalypsodeegree.model.feature.event.ModellEvent;

/**
 * @author Dirk Kuch
 */
public class CDEditPage {

    private static final String SECTION_ID_MATRIX = "sectionConflictDetectionMatrix";

    private final Composite m_parent;

    private PoolGeoData m_pool;

    private IConflict m_conflict;

    private ICDMenuPart m_part;

    public CDEditPage(final IAbstractMenuPart menuPart, final Composite parent) {
        m_parent = parent;
        if (menuPart != null && menuPart instanceof ICDMenuPart) {
            m_part = (ICDMenuPart) menuPart;
            final String conflict = m_part.getSelectedConflict();
            final MyBasePool pool = NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
            if (pool instanceof PoolGeoData) {
                m_pool = (PoolGeoData) pool;
                m_conflict = CDGmlUtils.getConflict(m_pool, conflict);
            }
        }
        final IAbstractViewPart viewPart = NofdpCorePlugin.getWindowManager().getAbstractViewPart(WINDOW_TYPE.eMain);
        final IModelEventDelegate delegate = new IModelEventDelegate() {

            public MyJob handleModelEvent(final ModellEvent modellEvent) {
                if (modellEvent instanceof FeaturesChangedModellEvent) {
                    final FeaturesChangedModellEvent event = (FeaturesChangedModellEvent) modellEvent;
                    final Feature[] features = event.getFeatures();
                    if (GmlConstants.QN_GEODATA_CONFLICT_OBSERVATION_TABLE_TYPE.equals(features[0].getFeatureType().getQName())) return null; else if (IMappingMember.QN_TYPE.equals(features[0].getFeatureType().getQName())) return null;
                } else if (modellEvent instanceof FeatureStructureChangeModellEvent) {
                    final FeatureStructureChangeModellEvent event = (FeatureStructureChangeModellEvent) modellEvent;
                    final Feature[] parentFeatures = event.getParentFeatures();
                    if (parentFeatures.length > 0) {
                        final QName qParent = parentFeatures[0].getFeatureType().getQName();
                        if (IGeodataCategories.QN_CATEGORY_CONFLICT_AREA.equals(qParent)) return null;
                    }
                    final Feature[] features = event.getChangedFeatures();
                    if (features.length > 0) {
                        final QName qFeature = features[0].getFeatureType().getQName();
                        if (IMap.QN_TYPE.equals(qFeature)) return null;
                    }
                }
                return new MyJob(Messages.CDEditPage_1) {

                    @Override
                    public IStatus runInUIThread(final IProgressMonitor monitor) {
                        viewPart.redraw();
                        return Status.OK_STATUS;
                    }
                };
            }
        };
        viewPart.addWorkspaceListener(m_pool.getWorkspace(), new MyModelEventListener(delegate));
    }

    public void generateBody() throws Exception {
        final FormToolkit toolkit = new FormToolkit(m_parent.getDisplay());
        toolkit.adapt(m_parent);
        if (m_conflict != null) getCDBody(toolkit, m_parent); else toolkit.createLabel(m_parent, Messages.CDEditPage_2);
        if (!m_parent.isDisposed()) m_parent.layout();
    }

    private void getCDBody(final FormToolkit toolkit, final Composite parent) throws Exception {
        if (toolkit == null || parent == null || parent.isDisposed()) throw new IllegalStateException();
        final Composite body = toolkit.createComposite(parent);
        body.setLayout(new GridLayout());
        body.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final String name = m_conflict.getName();
        final MySection sConflictMatrix = new MySection(m_part, toolkit, body, ExpandableComposite.TITLE_BAR, true);
        final Composite cConflictMatrix = sConflictMatrix.setup(Messages.CDEditPage_3 + " " + name, new GridData(GridData.FILL, GridData.FILL, true, true), new GridData(GridData.FILL, GridData.FILL, true, true), CDEditPage.SECTION_ID_MATRIX);
        cConflictMatrix.setLayout(new GridLayout());
        final CDMatrix matrix = new CDMatrix(m_part, m_conflict, toolkit, cConflictMatrix);
        matrix.draw();
        body.layout();
        parent.layout();
    }
}
