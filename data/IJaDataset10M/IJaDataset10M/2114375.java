package org.kalypso.nofdpidss.ui.view.evaluation.navigation.valuebenefit;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.IModelEventDelegate;
import org.kalypso.nofdpidss.core.base.MyColors;
import org.kalypso.nofdpidss.core.base.MyJob;
import org.kalypso.nofdpidss.core.base.MyModelEventListener;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.common.modules.NofdpModules.MODULE;
import org.kalypso.nofdpidss.core.common.utils.gml.ATGmlUtils;
import org.kalypso.nofdpidss.core.view.INofdpView;
import org.kalypso.nofdpidss.core.view.WindowManager;
import org.kalypso.nofdpidss.core.view.parts.IVBAnalysisResultsBuilder;
import org.kalypso.nofdpidss.core.view.parts.IValueBenefitMenuPart;
import org.kalypso.nofdpidss.evaluation.common.ui.EvalutationTemplateChooser;
import org.kalypso.nofdpidss.ui.application.AbstractMenuPart;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.nofdpidss.ui.view.common.parts.ViewPartNavigation;
import org.kalypso.ogc.gml.command.FeatureChangeModellEvent;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.event.FeatureStructureChangeModellEvent;
import org.kalypsodeegree.model.feature.event.ModellEvent;

/**
 * @author Dirk Kuch
 */
public class VBMenuPart extends AbstractMenuPart implements IValueBenefitMenuPart {

    private final Map<String, Feature> m_hChartFeatureMapping = new HashMap<String, Feature>();

    private IVBAnalysisResultsBuilder m_analysisResultBuilder;

    public VBMenuPart(final FormToolkit toolkit, final Composite parent) {
        super(MODULE.eValueBenefitAnalysis, toolkit, parent);
    }

    public void draw() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(MyBasePool.POOL_TYPE.eProject);
        final Feature lastAT = ATGmlUtils.getLastUsedAssessmentTemplate(pool);
        if (lastAT != null) NofdpCorePlugin.getWindowManager().showView(new INofdpView() {

            public String[] getForceUpdateIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_VALUE_BENEFIT_ANALYSIS_RESULT, NofdpIDSSConstants.NOFDP_VIEWPART_VALUE_BENEFIT_ANALYSIS_SETUP };
            }

            public String[] getOptionalViewIds() {
                return new String[] {};
            }

            public String getPerspective() {
                return NofdpIDSSConstants.NOFDP_PERSPECTIVE_VALUE_BENEFIT_ANALYSIS;
            }

            public String[] getViewIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_NAVIGATION, NofdpIDSSConstants.NOFDP_VIEWPART_VALUE_BENEFIT_ANALYSIS_SETUP, NofdpIDSSConstants.NOFDP_VIEWPART_VALUE_BENEFIT_ANALYSIS_RESULT };
            }
        }); else showMainWindow();
        new EvalutationTemplateChooser(this, getToolkit(), getBody());
        getToolkit().createLabel(getBody(), "");
        renderListOfVariants(getToolkit(), getBody());
    }

    public Feature getChartFeature(final String key) {
        return m_hChartFeatureMapping.get(key);
    }

    public void refreshMainViewBottom() {
        if (m_analysisResultBuilder != null) m_analysisResultBuilder.refresh();
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.AbstractMenuPart#registerWorkspaceListeners()
   */
    @Override
    protected void registerWorkspaceListeners() {
        final ViewPartNavigation viewPart = (ViewPartNavigation) NofdpCorePlugin.getWindowManager().getAbstractViewPart(WindowManager.WINDOW_TYPE.eNavigation);
        final MyBasePool gmlPool = NofdpCorePlugin.getProjectManager().getPool(MyBasePool.POOL_TYPE.eProject);
        final IModelEventDelegate delegate = new IModelEventDelegate() {

            public MyJob handleModelEvent(ModellEvent modellEvent) {
                if (modellEvent instanceof FeatureStructureChangeModellEvent) {
                    Feature[] arrChanges = ((FeatureStructureChangeModellEvent) modellEvent).getChangedFeatures();
                    for (Feature feature : arrChanges) {
                        QName name = feature.getFeatureType().getQName();
                        if (GmlConstants.QN_AT_TYPE.equals(name)) return new MyJob(Messages.VBMenuPart_1) {

                            @Override
                            public IStatus runInUIThread(IProgressMonitor monitor) {
                                refreshMainViewBottom();
                                return Status.OK_STATUS;
                            }
                        };
                    }
                } else if (modellEvent instanceof FeatureChangeModellEvent) {
                    FeatureChangeModellEvent event = (FeatureChangeModellEvent) modellEvent;
                    Feature[] features = event.getFeatures();
                    for (Feature feature : features) {
                        QName qname = feature.getFeatureType().getQName();
                        if (GmlConstants.QN_TYPE_AT_RATING_MEMBER_CRITERION_RANKING_WEIGHT.equals(qname)) return new MyJob(Messages.VBMenuPart_1) {

                            @Override
                            public IStatus runInUIThread(IProgressMonitor monitor) {
                                try {
                                    redraw(null);
                                } catch (CoreException e) {
                                    NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                                    return Status.CANCEL_STATUS;
                                }
                                return Status.OK_STATUS;
                            }
                        };
                    }
                }
                return null;
            }
        };
        addMyWorkspaceListener(viewPart, gmlPool.getWorkspace(), new MyModelEventListener(delegate));
    }

    private void renderListOfVariants(final FormToolkit toolkit, final Composite body) {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IVariant[] variants = pool.getModel().getVariantMembers().getVariants();
        final Group group = new Group(body, SWT.NONE);
        group.setLayout(new GridLayout(2, false));
        group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        toolkit.adapt(group);
        group.setText(Messages.VBMenuPart_2);
        group.setFont(MyColors.fTextBold);
        final FormText description = toolkit.createFormText(group, false);
        description.setText("<p>" + Messages.VBMenuPart_3 + "</p>", true, false);
        description.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 0));
        toolkit.createLabel(group, "");
        toolkit.createLabel(group, "");
        for (int i = 0; i < variants.length; i++) {
            final IVariant variant = variants[i];
            final Label lShortCut = toolkit.createLabel(group, String.format("VAR%02d", i + 1));
            lShortCut.setFont(MyColors.fTextBold);
            final FormText fName = toolkit.createFormText(group, false);
            fName.setText("<p>" + variant.getName() + "</p>", true, false);
            fName.setWhitespaceNormalized(true);
        }
    }

    public void setChartFeature(final String key, final Feature feature) {
        m_hChartFeatureMapping.put(key, feature);
    }

    public void setMainViewBottom(final IVBAnalysisResultsBuilder builder) {
        m_analysisResultBuilder = builder;
    }

    public void showMainWindow() {
        NofdpCorePlugin.getWindowManager().showView(new INofdpView() {

            public String[] getForceUpdateIds() {
                return new String[] {};
            }

            public String[] getOptionalViewIds() {
                return new String[] {};
            }

            public String getPerspective() {
                return NofdpIDSSConstants.NOFDP_PERSPECTIVE_DEFAULT;
            }

            public String[] getViewIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_NAVIGATION, NofdpIDSSConstants.NOFDP_VIEWPART_BROWSER };
            }
        });
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.parts.IValueBenefitMenuPart#getVariants()
   */
    public IVariant[] getVariants() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final IProjectModel model = pool.getModel();
        final IVariant[] variants = model.getVariantMembers().getVariants();
        return variants;
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.parts.IValueBenefitMenuPart#setVariants(org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant[])
   */
    public void setVariants(final IVariant[] variants) {
    }
}
