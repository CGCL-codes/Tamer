package org.kalypso.nofdpidss.ui.view.analysistools.navigation.vegetationsuitability;

import java.util.Map;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.contribs.eclipse.ui.progress.ProgressUtilities;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.analysis.navigation.vs.VSMenuBuilder;
import org.kalypso.nofdpidss.analysis.suitabilities.ISuitabilityBuilder;
import org.kalypso.nofdpidss.analysis.suitabilities.ISuitabilityMenuPart;
import org.kalypso.nofdpidss.analysis.suitabilities.SuitabilityHelper;
import org.kalypso.nofdpidss.analysis.suitabilities.vs.VSGenerator;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.IModelEventDelegate;
import org.kalypso.nofdpidss.core.base.MyJob;
import org.kalypso.nofdpidss.core.base.MyModelEventListener;
import org.kalypso.nofdpidss.core.base.WorkspaceSync;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataModel;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMap;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolGeoData;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.common.modules.NofdpModules.MODULE;
import org.kalypso.nofdpidss.core.common.utils.gml.VSGmlUtils;
import org.kalypso.nofdpidss.core.view.INofdpView;
import org.kalypso.nofdpidss.core.view.WindowManager.WINDOW_TYPE;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.nofdpidss.ui.view.analysistools.navigation.SuitabilityMenuPart;
import org.kalypso.nofdpidss.ui.view.common.parts.ViewPartNavigation;
import org.kalypso.ogc.gml.FeatureUtils;
import org.kalypso.ui.views.map.MapView;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.event.ModellEvent;

/**
 * @author Dirk Kuch
 */
public class VSMenuPart extends SuitabilityMenuPart implements ISuitabilityMenuPart {

    private static final String SELECTED_MENU = "vsSelectedMenu";

    public VSMenuPart(final FormToolkit toolkit, final Composite parent) {
        super(MODULE.eVegetationSuitability, toolkit, parent);
    }

    public void draw() throws CoreException {
        final Map<String, IViewReference> references = NofdpCorePlugin.getWindowManager().showView(new INofdpView() {

            public String[] getForceUpdateIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_BROWSER };
            }

            public String[] getOptionalViewIds() {
                return new String[] {};
            }

            public String getPerspective() {
                return NofdpIDSSConstants.NOFDP_PERSPECTIVE_DEFAULT;
            }

            public String[] getViewIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_NAVIGATION, NofdpIDSSConstants.NOFDP_VIEWPART_MAP_OUTLINE, NofdpIDSSConstants.NOFDP_VIEWPART_VEGETATION_SUITABILITY_MAP };
            }
        });
        final IViewReference reference = references.get(NofdpIDSSConstants.NOFDP_VIEWPART_VEGETATION_SUITABILITY_MAP);
        final MapView mapView = (MapView) reference.getView(false);
        setMapView(mapView);
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        final IGeodataModel model = pool.getModel();
        final IMap map = model.getMaps().getMap("templateVegetationSuitability");
        showMap(map.getIFile(), Messages.VSMenuPart_4);
        final ILabelProvider labelProvider = new LabelProvider() {

            @Override
            public String getText(final Object element) {
                if (element instanceof Feature) {
                    final String featureName = FeatureUtils.getFeatureName(GmlConstants.NS_GEODATA, (Feature) element);
                    return featureName;
                }
                return super.getText(element);
            }
        };
        final ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {

            public void selectionChanged(final SelectionChangedEvent event) {
                final ISelection selection = event.getSelection();
                if (selection instanceof StructuredSelection) {
                    final StructuredSelection cSel = (StructuredSelection) selection;
                    final Object element = cSel.getFirstElement();
                    if (element instanceof Feature) {
                        final Feature f = (Feature) element;
                        setSelectedMenu(f);
                        try {
                            redraw(null);
                        } catch (CoreException e) {
                            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                        }
                        getBody().layout();
                    }
                }
            }
        };
        final Object input = VSGmlUtils.getSections(pool);
        getMenu(Messages.VSMenuPart_1, labelProvider, selectionChangedListener, input);
        final Feature selected = getSelectedMenu();
        if (selected != null) prepareDrawing(selected);
    }

    @Override
    protected Feature getSelectedMenu() {
        final IDialogSettings settings = super.getSettings();
        final String selection = settings.get(VSMenuPart.SELECTED_MENU);
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        return VSGmlUtils.getSelectedMenu(pool, selection);
    }

    protected void prepareDrawing(final Feature feature) {
        final ISuitabilityBuilder builder = new VSMenuBuilder(this, feature);
        draw(builder);
        final SelectionAdapter selectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                process();
            }
        };
        getComputeButton().addSelectionListener(selectionAdapter);
    }

    protected void process() {
        SuitabilityHelper.removeResults(getMapView());
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        WorkspaceSync.sync(pool.getMappedProject(), IResource.DEPTH_INFINITE);
        final VSGenerator runnable = new VSGenerator(this, pool.getMappedProject(), pool, getSelectedMenu(), getSelectedDatasets());
        final IStatus status = ProgressUtilities.busyCursorWhile(runnable);
        ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.VSMenuPart_5, Messages.VSMenuPart_6, status);
        if (!status.isOK()) return;
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.AbstractMenuPart#registerWorkspaceListeners()
   */
    @Override
    protected void registerWorkspaceListeners() {
        final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
        final ViewPartNavigation viewPart = (ViewPartNavigation) NofdpCorePlugin.getWindowManager().getAbstractViewPart(WINDOW_TYPE.eNavigation);
        final IModelEventDelegate delegate = new IModelEventDelegate() {

            public MyJob handleModelEvent(final ModellEvent modellEvent) {
                return new MyJob(Messages.VSMenuPart_7) {

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
        };
        addMyWorkspaceListener(viewPart, pool.getWorkspace(), new MyModelEventListener(delegate));
    }

    protected void setSelectedMenu(final Feature selection) {
        final IDialogSettings settings = super.getSettings();
        settings.put(VSMenuPart.SELECTED_MENU, selection.getId());
    }
}
