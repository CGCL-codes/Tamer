package org.kalypso.nofdpidss.ui.view.interactiveplanning.navigation.variantmanager;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.IModelEventDelegate;
import org.kalypso.nofdpidss.core.base.MyJob;
import org.kalypso.nofdpidss.core.base.MyModelEventListener;
import org.kalypso.nofdpidss.core.base.WorkspaceSync;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMap;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IMaps;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IProjectModel;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolGeoData;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolProject;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.common.modules.NofdpModules.MODULE;
import org.kalypso.nofdpidss.core.view.INofdpView;
import org.kalypso.nofdpidss.core.view.WindowManager.WINDOW_TYPE;
import org.kalypso.nofdpidss.core.view.map.CommandSetMapName;
import org.kalypso.nofdpidss.core.view.map.IMapActions;
import org.kalypso.nofdpidss.core.view.map.MapCommand;
import org.kalypso.nofdpidss.core.view.map.IMapActions.ACTION;
import org.kalypso.nofdpidss.core.view.parts.IVMMenuPart;
import org.kalypso.nofdpidss.geodata.base.UIMapTool;
import org.kalypso.nofdpidss.ui.application.AbstractMapMenuPart;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.nofdpidss.ui.view.common.parts.ViewPartNavigation;
import org.kalypso.ogc.gml.command.FeatureChange;
import org.kalypso.ogc.gml.command.FeatureChangeModellEvent;
import org.kalypso.ui.views.map.MapView;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.event.FeatureStructureChangeModellEvent;
import org.kalypsodeegree.model.feature.event.ModellEvent;

/**
 * @author Dirk Kuch
 */
public class VMMenuPart extends AbstractMapMenuPart implements IVMMenuPart {

    public VMMenuPart(final FormToolkit toolkit, final Composite parent) {
        super(MODULE.eVariantManager, toolkit, parent);
    }

    public void draw() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        try {
            updateViews();
            final VMNavBuilder builder = new VMNavBuilder(pool);
            builder.build(getToolkit(), getBody());
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
    }

    /**
   * @see org.kalypso.nofdpidss.core.view.AbstractMenuPart#registerWorkspaceListeners()
   */
    @Override
    protected void registerWorkspaceListeners() {
        final PoolProject pool = (PoolProject) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eProject);
        final ViewPartNavigation viewPart = (ViewPartNavigation) NofdpCorePlugin.getWindowManager().getAbstractViewPart(WINDOW_TYPE.eNavigation);
        final IModelEventDelegate delegate = new IModelEventDelegate() {

            public MyJob handleModelEvent(ModellEvent modellEvent) {
                if (modellEvent instanceof FeatureStructureChangeModellEvent) {
                    final FeatureStructureChangeModellEvent event = (FeatureStructureChangeModellEvent) modellEvent;
                    Feature[] parentFeatures = event.getParentFeatures();
                    Feature[] changedFeatures = event.getChangedFeatures();
                    if (parentFeatures.length != 1 && changedFeatures.length != 1) return null;
                    for (final Feature element : parentFeatures) {
                        QName qParent = element.getFeatureType().getQName();
                        final Feature changedFeature = changedFeatures[0];
                        if (changedFeature == null) System.out.println("Strange! Fixme");
                        QName qFeature = changedFeature.getFeatureType().getQName();
                        if (IProjectModel.QN_TYPE.equals(qParent) && IVariant.QN_TYPE.equals(qFeature)) return new MyJob(Messages.VMMenuPart_4) {

                            @Override
                            public IStatus runInUIThread(IProgressMonitor arg0) {
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
                } else if (modellEvent instanceof FeatureChangeModellEvent) {
                    FeatureChangeModellEvent event = (FeatureChangeModellEvent) modellEvent;
                    FeatureChange[] changes = event.getChanges();
                    for (FeatureChange change : changes) {
                        QName property = change.getProperty().getQName();
                        if (GmlConstants.NS_VARIANTS.equals(property.getNamespaceURI())) return new MyJob(Messages.VMMenuPart_4) {

                            @Override
                            public IStatus runInUIThread(IProgressMonitor arg0) {
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
        addMyWorkspaceListener(viewPart, pool.getWorkspace(), new MyModelEventListener(delegate));
    }

    protected void updateViews() throws CoreException {
        final Map<String, IViewReference> views = NofdpCorePlugin.getWindowManager().showView(new INofdpView() {

            public String[] getForceUpdateIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_VARIANT_MANAGER };
            }

            public String[] getOptionalViewIds() {
                return new String[] {};
            }

            public String getPerspective() {
                return NofdpIDSSConstants.NOFDP_PERSPECTIVE_VARIANT_MANAGER;
            }

            public String[] getViewIds() {
                return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_NAVIGATION, NofdpIDSSConstants.NOFDP_VIEWPART_MAP_OUTLINE, NofdpIDSSConstants.NOFDP_VIEWPART_VARIANT_MANAGER_MAP, NofdpIDSSConstants.NOFDP_VIEWPART_VARIANT_MANAGER };
            }
        });
        final IViewReference ref = views.get(NofdpIDSSConstants.NOFDP_VIEWPART_VARIANT_MANAGER_MAP);
        if (ref != null) {
            final MapView mapView = (MapView) ref.getView(false);
            final PoolGeoData poolGeodata = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
            final IMap map = poolGeodata.getModel().getMaps().getMap(IMaps.ID_MAP_VARIANTS_TEMPLATE);
            WorkspaceSync.sync(map.getIFile(), IResource.DEPTH_INFINITE);
            final Map<MapCommand, IMapActions.ACTION[]> commands = new HashMap<MapCommand, ACTION[]>();
            commands.put(new CommandSetMapName(Messages.VMMenuPart_5), new IMapActions.ACTION[] { IMapActions.ACTION.eThemeStatusChanged });
            UIMapTool.loadMap(this, mapView, map.getIFile(), commands);
            setMapView(mapView);
        }
    }
}
