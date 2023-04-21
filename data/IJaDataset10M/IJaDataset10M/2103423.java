package org.kalypso.nofdpidss.ui.application.hydraulic.computation.map;

import java.awt.Cursor;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.progress.UIJob;
import org.kalypso.commons.command.ICommandTarget;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.flow.network.IStructureNode;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.ICalculationCase;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.IVariantResultMember;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolHelper;
import org.kalypso.nofdpidss.core.common.NofdpIDSSConstants;
import org.kalypso.nofdpidss.core.view.INofdpView;
import org.kalypso.nofdpidss.hydraulic.computation.utils.SelectResultNodeHelper;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.OutlineCSNDiagram;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.ViewpartCSNDiagram;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart.ConnectionNodeDiagramWrapper;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart.CsnDiagramWrapper;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart.ICsnDiagramWrapper;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart.RetardingBasinDiagramWrapper;
import org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart.WeirDiagramWrapper;
import org.kalypso.nofdpidss.ui.i18n.Messages;
import org.kalypso.nofdpidss.ui.view.interactiveplanning.navigation.hydrauliccomputation.HCMenuPart;
import org.kalypso.ogc.gml.CascadingLayerKalypsoTheme;
import org.kalypso.ogc.gml.GisTemplateFeatureTheme;
import org.kalypso.ogc.gml.IKalypsoTheme;
import org.kalypso.ogc.gml.map.IMapPanel;
import org.kalypso.ogc.gml.map.utilities.ThemeHelper;
import org.kalypso.ogc.gml.map.widgets.SelectionWidget;
import org.kalypso.ogc.gml.map.widgets.mapfunctions.IRectangleMapFunction;
import org.kalypso.ogc.gml.map.widgets.mapfunctions.MapfunctionHelper;
import org.kalypso.ogc.gml.mapmodel.CommandableWorkspace;
import org.kalypso.ogc.gml.selection.EasyFeatureWrapper;
import org.kalypso.ogc.gml.selection.IFeatureSelectionManager;
import org.kalypsodeegree.model.feature.Feature;
import org.kalypsodeegree.model.feature.FeatureList;
import org.kalypsodeegree.model.geometry.GM_Point;

/**
 * @author Dirk Kuch
 */
public class SelectResultNode extends SelectionWidget {

    private static final String WEIR_NODE = "WeirNode";

    private static final String RETARDING_BASIN_NODE = "RetardingBasinNode";

    private static final String POLDER_NODE = "PolderNode";

    private static final String LINKAGE_NODE = "LinkageNode";

    private static final String CONNECTION_NODE = "ConnectionNode";

    private static final String BOUNDARY_NODE = "BoundaryNode";

    private static final String CROSS_SECTION_NODES = "CrossSectionNodes";

    protected static final String THEME_ID = "themeId";

    protected static final String[] VALID_THEMES = new String[] { CROSS_SECTION_NODES, BOUNDARY_NODE, CONNECTION_NODE, LINKAGE_NODE, POLDER_NODE, RETARDING_BASIN_NODE, WEIR_NODE };

    public SelectResultNode() {
        super(Messages.SelectFNElementWidget_1, Messages.SelectFNElementWidget_2, SelectResultNode.clickFunction);
    }

    private static final IRectangleMapFunction clickFunction = new IRectangleMapFunction() {

        public void execute(final IMapPanel mapPanel, final Rectangle rectangle) {
            HCMenuPart menuPart = (HCMenuPart) NofdpCorePlugin.getWindowManager().getMenuPart();
            Map<IVariantResultMember, ICalculationCase[]> selection = menuPart.getSelection();
            if (selection.size() < 1) return;
            IKalypsoTheme[] themes = mapPanel.getMapModell().getAllThemes();
            Set<IKalypsoTheme> myFlowNetworkThemes = new LinkedHashSet<IKalypsoTheme>();
            for (IKalypsoTheme theme : themes) {
                if (theme instanceof CascadingLayerKalypsoTheme) {
                    CascadingLayerKalypsoTheme cascading = (CascadingLayerKalypsoTheme) theme;
                    IKalypsoTheme[] childThemes = cascading.getChildThemes();
                    IKalypsoTheme[] csnThemes = ThemeHelper.getThemeByProperties(childThemes, THEME_ID, VALID_THEMES);
                    for (IKalypsoTheme child : csnThemes) {
                        myFlowNetworkThemes.add(child);
                    }
                }
            }
            for (IKalypsoTheme theme : myFlowNetworkThemes) {
                try {
                    EasyFeatureWrapper[] myFeatures = findFeatures(theme);
                    if (myFeatures.length == 0) {
                        continue;
                    }
                    EasyFeatureWrapper[] selected = MapfunctionHelper.findFeatureToSelect(mapPanel, rectangle, myFeatures, 10);
                    if (selected.length <= 0) {
                        continue;
                    }
                    IFeatureSelectionManager manager = mapPanel.getSelectionManager();
                    manager.setSelection(selected);
                    final String property = theme.getProperty(THEME_ID, "");
                    if (WEIR_NODE.equals(property)) {
                        Feature feature = selected[0].getFeature();
                        QName type = feature.getFeatureType().getQName();
                        GM_Point location = (GM_Point) feature.getProperty(IStructureNode.QN_LOCATION);
                        WeirDiagramWrapper wrapper = new WeirDiagramWrapper(Messages.SelectResultNode_0);
                        SelectResultNodeHelper.fillWeirNodeWrapper(wrapper, type, location, selection);
                        ViewpartCSNDiagram.WRAPPER = wrapper;
                    } else if (RETARDING_BASIN_NODE.equals(property)) {
                        Feature feature = selected[0].getFeature();
                        QName type = feature.getFeatureType().getQName();
                        GM_Point location = (GM_Point) feature.getProperty(IStructureNode.QN_LOCATION);
                        RetardingBasinDiagramWrapper wrapper = new RetardingBasinDiagramWrapper(Messages.SelectResultNode_1);
                        SelectResultNodeHelper.fillRetardingBasinNodeWrapper(wrapper, type, location, selection);
                        ViewpartCSNDiagram.WRAPPER = wrapper;
                    } else if (POLDER_NODE.equals(property)) {
                        Feature feature = selected[0].getFeature();
                        QName type = feature.getFeatureType().getQName();
                        GM_Point location = (GM_Point) feature.getProperty(IStructureNode.QN_LOCATION);
                        RetardingBasinDiagramWrapper wrapper = new RetardingBasinDiagramWrapper(Messages.SelectResultNode_2);
                        SelectResultNodeHelper.fillPolderNodeWrapper(wrapper, type, location, selection);
                        ViewpartCSNDiagram.WRAPPER = wrapper;
                    } else if (LINKAGE_NODE.equals(property) || CONNECTION_NODE.equals(property) || BOUNDARY_NODE.equals(property)) {
                        Feature feature = selected[0].getFeature();
                        QName type = feature.getFeatureType().getQName();
                        GM_Point location = (GM_Point) feature.getProperty(IStructureNode.QN_LOCATION);
                        ConnectionNodeDiagramWrapper wrapper = new ConnectionNodeDiagramWrapper(Messages.SelectResultNode_3);
                        SelectResultNodeHelper.fillConnectionNodeWrapper(wrapper, type, location, selection);
                        ViewpartCSNDiagram.WRAPPER = wrapper;
                    } else if (CROSS_SECTION_NODES.equals(property)) {
                        Feature feature = selected[0].getFeature();
                        QName type = feature.getFeatureType().getQName();
                        GM_Point location = (GM_Point) feature.getProperty(IStructureNode.QN_LOCATION);
                        ICsnDiagramWrapper wrapper = new CsnDiagramWrapper(Messages.SelectResultNode_4);
                        SelectResultNodeHelper.fillCrossSectionNodeWrapper(wrapper, type, location, selection);
                        ViewpartCSNDiagram.WRAPPER = wrapper;
                    }
                    new UIJob("") {

                        @Override
                        public IStatus runInUIThread(final IProgressMonitor monitor) {
                            Map<String, IViewReference> references = NofdpCorePlugin.getWindowManager().showView(new INofdpView() {

                                public String[] getForceUpdateIds() {
                                    return new String[] {};
                                }

                                public String[] getOptionalViewIds() {
                                    return new String[] {};
                                }

                                public String getPerspective() {
                                    return NofdpIDSSConstants.NOFDP_PERSPECTIVE_HYDRAULIC_COMPUTATION;
                                }

                                public String[] getViewIds() {
                                    return new String[] { NofdpIDSSConstants.NOFDP_VIEWPART_NAVIGATION, OutlineCSNDiagram.ID, NofdpIDSSConstants.NOFDP_VIEWPART_MAP_OUTLINE, NofdpIDSSConstants.NOFDP_VIEWPART_HYDRAULIC_COMPUTATION_MAP, ViewpartCSNDiagram.ID };
                                }
                            });
                            IViewReference reference = references.get(ViewpartCSNDiagram.ID);
                            ViewpartCSNDiagram diagram = (ViewpartCSNDiagram) reference.getView(false);
                            diagram.updateControl();
                            return Status.OK_STATUS;
                        }
                    }.schedule();
                    return;
                } catch (Exception e) {
                    NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
                }
            }
        }

        private EasyFeatureWrapper[] findFeatures(final IKalypsoTheme theme) {
            if (!(theme instanceof GisTemplateFeatureTheme)) return new EasyFeatureWrapper[] {};
            GisTemplateFeatureTheme gis = (GisTemplateFeatureTheme) theme;
            FeatureList featureList = gis.getFeatureList();
            List<EasyFeatureWrapper> myFeatures = new ArrayList<EasyFeatureWrapper>();
            for (Object object : featureList) {
                if (!(object instanceof Feature)) {
                    continue;
                }
                Feature f = (Feature) object;
                CommandableWorkspace workspace = PoolHelper.getCommandableWorkspace(f.getWorkspace());
                myFeatures.add(new EasyFeatureWrapper(workspace, f, f.getOwner(), f.getParentRelation()));
            }
            return myFeatures.toArray(new EasyFeatureWrapper[] {});
        }
    };

    /**
   * @see org.kalypso.ogc.gml.map.widgets.SelectionWidget#activate(org.kalypso.commons.command.ICommandTarget,
   *      org.kalypso.ogc.gml.map.MapPanel)
   */
    @Override
    public void activate(final ICommandTarget commandPoster, final IMapPanel mapPanel) {
        super.activate(commandPoster, mapPanel);
        final Cursor cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        getMapPanel().setCursor(cursor);
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.SelectionWidget#leftPressed(java.awt.Point)
   */
    @Override
    public void leftPressed(final Point p) {
    }

    /**
   * @see org.kalypso.ogc.gml.map.widgets.SelectionWidget#leftReleased(java.awt.Point)
   */
    @Override
    public void leftReleased(final Point p) {
        getClickFunction().execute(getMapPanel(), new Rectangle(p.x, p.y, 1, 1));
    }
}
