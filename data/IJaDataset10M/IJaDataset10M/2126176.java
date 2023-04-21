package com.isa.jump.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComponent;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureCollection;
import com.vividsolutions.jump.feature.FeatureDataset;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.model.UndoableCommand;
import com.vividsolutions.jump.workbench.plugin.*;
import com.vividsolutions.jump.workbench.plugin.util.*;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.LayerViewPanel;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.SelectionManagerProxy;
import com.vividsolutions.jump.workbench.ui.plugin.analysis.GeometryFunction;

public class CutPolygonPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static final String UPDATE_SRC = "Update the polygon with result";

    private static final String ADD_TO_SRC = "Add result to the polygon layer";

    private static final String CREATE_LYR = "Create new layer for result";

    private MultiInputDialog dialog;

    private Layer srcLayer;

    private GeometryFunction differenceFunction = GeometryFunction.getFunction("Difference (Source-Mask)");

    private GeometryFunction intersectionFunction = GeometryFunction.getFunction("Intersection");

    private boolean createLayer = true;

    private boolean updateSource = false;

    private boolean addToSource = false;

    public CutPolygonPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        WorkbenchContext workbenchContext = context.getWorkbenchContext();
        context.getFeatureInstaller().addMainMenuItem(this, new String[] { "Tools", "Edit" }, getName() + "...", false, null, this.createEnableCheck(workbenchContext));
    }

    public boolean execute(PlugInContext context) throws Exception {
        dialog = new MultiInputDialog(context.getWorkbenchFrame(), getName(), true);
        setDialogValues(dialog, context);
        GUIUtil.centreOnWindow(dialog);
        dialog.setVisible(true);
        if (!dialog.wasOKPressed()) {
            return false;
        }
        getDialogValues(dialog);
        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        monitor.allowCancellationRequests();
        Collection selectedFeatures = context.getLayerViewPanel().getSelectionManager().getFeaturesWithSelectedItems();
        Iterator i = selectedFeatures.iterator();
        Feature featureOne = (Feature) i.next();
        Feature featureTwo = (Feature) i.next();
        Feature polyFeature = featureOne;
        Geometry linestring = featureTwo.getGeometry();
        if (linestring instanceof Polygon) {
            polyFeature = featureTwo;
            linestring = featureOne.getGeometry();
        }
        double bufferWidth = 0.01;
        Geometry buffer = linestring.buffer(bufferWidth);
        for (Iterator lyr = context.getWorkbenchContext().getLayerViewPanel().getSelectionManager().getLayersWithSelectedItems().iterator(); lyr.hasNext(); ) {
            Layer layer = (Layer) lyr.next();
            for (Iterator ftr = layer.getFeatureCollectionWrapper().getFeatures().iterator(); ftr.hasNext(); ) {
                if (polyFeature == ftr.next()) {
                    srcLayer = layer;
                    break;
                }
            }
            if (srcLayer == layer) break;
        }
        monitor.report("Executing function " + differenceFunction.getName() + "...");
        Collection resultFeatures = new ArrayList();
        Geometry result = null;
        Geometry intersection = null;
        try {
            Geometry geoms[] = new Geometry[2];
            geoms[0] = polyFeature.getGeometry();
            geoms[1] = buffer;
            result = differenceFunction.execute(geoms, new double[2]);
            geoms[1] = linestring;
            intersection = intersectionFunction.execute(geoms, new double[2]);
        } catch (RuntimeException ex) {
            context.getWorkbenchFrame().warnUser("Errors found while executing Cut Polygon");
        }
        Coordinate[] intersectionPts = intersection.getCoordinates();
        if (result == null || result.isEmpty()) return;
        int lsNumPts = linestring.getNumPoints();
        Coordinate[] lsCoords = linestring.getCoordinates();
        for (int j = 0; j < result.getNumGeometries(); j++) {
            Feature fNew = polyFeature.clone(true);
            Geometry geo = (Geometry) result.getGeometryN(j).clone();
            int numPts = geo.getNumPoints();
            Coordinate[] coords = geo.getCoordinates();
            for (int m = 0; m < numPts; m++) {
                for (int n = 0; n < lsNumPts - 1; n++) {
                    Coordinate p0 = lsCoords[n];
                    Coordinate p1 = lsCoords[n + 1];
                    double distToLine = GeoUtils.getDistance(coords[m], p0, p1);
                    if (Math.abs((distToLine) - (bufferWidth)) < 0.001) {
                        Coordinate snapPt = getNearestSnapPoint(coords[m], intersectionPts);
                        coords[m].x = snapPt.x;
                        coords[m].y = snapPt.y;
                    }
                }
            }
            CoordinateList coordList = new CoordinateList(coords, false);
            Polygon poly = new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(coordList.toCoordinateArray()), null);
            fNew.setGeometry(poly);
            resultFeatures.add(fNew);
        }
        if (createLayer) {
            String outputLayerName = LayerNameGenerator.generateOperationOnLayerName("Cut Polygon", srcLayer.getName());
            FeatureCollection resultFC = new FeatureDataset(srcLayer.getFeatureCollectionWrapper().getFeatureSchema());
            resultFC.addAll(resultFeatures);
            String categoryName = StandardCategoryNames.RESULT;
            context.getLayerManager().addCategory(categoryName);
            Layer newLayer = context.addLayer(categoryName, outputLayerName, resultFC);
            newLayer.setFeatureCollectionModified(true);
        } else if (updateSource) {
            final Collection undoableNewFeatures = resultFeatures;
            final Feature undoablePolyFeatures = polyFeature;
            UndoableCommand cmd = new UndoableCommand(getName()) {

                public void execute() {
                    srcLayer.getFeatureCollectionWrapper().remove(undoablePolyFeatures);
                    srcLayer.getFeatureCollectionWrapper().addAll(undoableNewFeatures);
                }

                public void unexecute() {
                    srcLayer.getFeatureCollectionWrapper().removeAll(undoableNewFeatures);
                    srcLayer.getFeatureCollectionWrapper().add(undoablePolyFeatures);
                }
            };
            execute(cmd, context);
        } else if (addToSource) {
            final Collection undoableFeatures = resultFeatures;
            UndoableCommand cmd = new UndoableCommand(getName()) {

                public void execute() {
                    srcLayer.getFeatureCollectionWrapper().addAll(undoableFeatures);
                }

                public void unexecute() {
                    srcLayer.getFeatureCollectionWrapper().removeAll(undoableFeatures);
                }
            };
            execute(cmd, context);
        }
    }

    private Coordinate getNearestSnapPoint(Coordinate coord, Coordinate[] intersectionPts) {
        Coordinate closestPt = (Coordinate) ((Coordinate) intersectionPts[0]).clone();
        double shortestDist = coord.distance(intersectionPts[0]);
        for (int i = 1; i < intersectionPts.length; i++) {
            if (coord.distance(intersectionPts[i]) < shortestDist) {
                closestPt = (Coordinate) ((Coordinate) intersectionPts[i]).clone();
                shortestDist = coord.distance(intersectionPts[i]);
            }
        }
        return closestPt;
    }

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarDescription("Uses the selected linestring to cut " + "the selected polygon into separate sections.");
        final String OUTPUT_GROUP = "Match Type";
        dialog.addRadioButton(CREATE_LYR, OUTPUT_GROUP, createLayer, "Create a new layer for the resulting sections.");
        dialog.addRadioButton(UPDATE_SRC, OUTPUT_GROUP, updateSource, "Replace the polygon with the resulting sections.");
        dialog.addRadioButton(ADD_TO_SRC, OUTPUT_GROUP, addToSource, "Add the resulting sections to the polygon layer.");
    }

    private void getDialogValues(MultiInputDialog dialog) {
        createLayer = dialog.getBoolean(CREATE_LYR);
        updateSource = dialog.getBoolean(UPDATE_SRC);
        addToSource = dialog.getBoolean(ADD_TO_SRC);
    }

    public EnableCheck onlyPolyAndLinestringMayBeSelected(final WorkbenchContext workbenchContext) {
        return new EnableCheck() {

            public String check(JComponent component) {
                Collection selectedItems = ((SelectionManagerProxy) workbenchContext.getWorkbench().getFrame().getActiveInternalFrame()).getSelectionManager().getSelectedItems();
                int polyCount = 0;
                int lsCount = 0;
                for (Iterator i = selectedItems.iterator(); i.hasNext(); ) {
                    Geometry geo = (Geometry) i.next();
                    if (geo instanceof Polygon) polyCount++;
                    if (geo instanceof LineString) lsCount++;
                }
                if (polyCount == 1 && lsCount == 1) return null;
                return "Must select one polygon and one linestring";
            }
        };
    }

    public MultiEnableCheck createEnableCheck(final WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createWindowWithLayerViewPanelMustBeActiveCheck()).add(checkFactory.createExactlyNFeaturesMustBeSelectedCheck(2)).add(onlyPolyAndLinestringMayBeSelected(workbenchContext));
    }
}
