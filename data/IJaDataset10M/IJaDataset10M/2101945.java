package com.isa.jump.plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.vividsolutions.jump.geom.CoordUtil;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.workbench.WorkbenchContext;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.Task;
import com.vividsolutions.jump.workbench.plugin.MultiEnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.plugin.EnableCheckFactory;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;

public class ConvertProjectUnitsPlugIn extends AbstractPlugIn {

    private static final String UNITS_FROM = "From:";

    private static final String UNITS_TO = "To:";

    private static final String CONVERSION_FACTOR = "Conversion Units:";

    private static final double feetToMeters = 0.3048;

    private static final String[] unitsArray = new String[] { "Feet", "Meters" };

    private String unitsFrom = "Feet";

    private String unitsTo = "Meters";

    private double conversionFactor = feetToMeters;

    private static WorkbenchContext workbenchContext;

    public ConvertProjectUnitsPlugIn() {
    }

    public void initialize(PlugInContext context) throws Exception {
        workbenchContext = context.getWorkbenchContext();
        FeatureInstaller featureInstaller = new FeatureInstaller(context.getWorkbenchContext());
        featureInstaller.addMainMenuItem(this, "File", "Convert Task Units To..." + "{pos:6}", null, createEnableCheck(context.getWorkbenchContext()));
    }

    public boolean execute(PlugInContext context) throws Exception {
        reportNothingToUndoYet(context);
        WorkbenchContext wc = context.getWorkbenchContext();
        unitsFrom = getTaskUnits(context);
        final MultiInputDialog dialog = new MultiInputDialog(context.getWorkbenchFrame(), "Convert Task Units", true);
        Collection unitsList = new ArrayList();
        for (int i = 0; i < unitsArray.length; i++) unitsList.add(unitsArray[i]);
        dialog.addLabel("Convert task units ...");
        final JComboBox unitsFromComboBox = dialog.addComboBox(UNITS_FROM, "", unitsList, "From: ");
        unitsList.add("Custom");
        final JComboBox unitsToComboBox = dialog.addComboBox(UNITS_TO, "", unitsList, "To: ");
        unitsFromComboBox.setSelectedItem(unitsFrom);
        unitsToComboBox.setSelectedItem(unitsTo);
        final JTextField conversionFactorField = dialog.addDoubleField(CONVERSION_FACTOR, conversionFactor, 8);
        if (unitsTo.equalsIgnoreCase("Custom")) conversionFactorField.enable(true); else conversionFactorField.enable(false);
        conversionFactorField.setToolTipText("This is the correct conversion factor.");
        dialog.getComboBox(UNITS_TO).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setUnits(unitsFromComboBox, unitsToComboBox, conversionFactorField);
            }
        });
        dialog.getComboBox(UNITS_FROM).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setUnits(unitsFromComboBox, unitsToComboBox, conversionFactorField);
            }
        });
        dialog.setVisible(true);
        if (dialog.wasOKPressed()) {
            conversionFactor = dialog.getDouble(CONVERSION_FACTOR);
            if (conversionFactor == 0.0 || conversionFactor == 1.0) return true;
            if (conversionFactor < 0.0) {
                int response = JOptionPane.showConfirmDialog(wc.getLayerViewPanel(), "A negative conversion factor is going to do something weird.\nContinue?", "JUMP", JOptionPane.OK_CANCEL_OPTION);
                if (response == JOptionPane.CANCEL_OPTION) return true;
            }
            Collection layerCollection = (Collection) wc.getLayerNamePanel().getLayerManager().getLayers();
            for (Iterator l = layerCollection.iterator(); l.hasNext(); ) {
                Layer layer = (Layer) l.next();
                for (Iterator f = layer.getFeatureCollectionWrapper().getFeatures().iterator(); f.hasNext(); ) {
                    Feature feature = (Feature) f.next();
                    Geometry geo = feature.getGeometry();
                    convert(geo, conversionFactor);
                    geo.geometryChanged();
                }
                layer.setFeatureCollectionModified(true);
                Layer.tryToInvalidateEnvelope(layer);
            }
            wc.getLayerViewPanel().getViewport().zoomToFullExtent();
            GeoUtils.setTaskUnits(context, unitsTo);
        }
        return true;
    }

    private void convert(Geometry geometry, final double conversionFactor) {
        geometry.apply(new CoordinateFilter() {

            public void filter(Coordinate coordinate) {
                coordinate.setCoordinate(CoordUtil.multiply(conversionFactor, coordinate));
            }
        });
    }

    private void setUnits(JComboBox unitsFromComboBox, JComboBox unitsToComboBox, JTextField conversionFactorField) {
        unitsFrom = (String) unitsFromComboBox.getSelectedItem();
        unitsTo = (String) unitsToComboBox.getSelectedItem();
        conversionFactorField.enable(false);
        conversionFactorField.setToolTipText("This is the correct conversion factor.");
        if ((unitsFrom.equalsIgnoreCase("Feet")) && (unitsTo.equalsIgnoreCase("Meters"))) conversionFactor = feetToMeters; else if ((unitsFrom.equalsIgnoreCase("Meters")) && (unitsTo.equalsIgnoreCase("Feet"))) conversionFactor = 1.0 / feetToMeters; else if (unitsTo.equalsIgnoreCase("Custom")) {
            conversionFactorField.enable(true);
            conversionFactorField.setToolTipText("Enter custom conversion factor.");
        }
        DecimalFormat df = new DecimalFormat("0.0#####");
        String cf = df.format(conversionFactor);
        conversionFactorField.setText(cf);
    }

    private String getTaskUnits(PlugInContext context) {
        try {
            String unitsFrom = GeoUtils.getTaskUnits(context.getWorkbenchContext());
            if (unitsFrom == null) return "Feet"; else if (unitsFrom.equalsIgnoreCase("Undefined")) return "Feet"; else return unitsFrom;
        } catch (Exception ex) {
            return "Feet";
        }
    }

    private static EnableCheck noLoadDeferredLayersCanExistCheck() {
        return new EnableCheck() {

            public String check(JComponent component) {
                boolean deferredLoadsExist = false;
                Collection layerCollection = (Collection) workbenchContext.getLayerNamePanel().getLayerManager().getLayerables(Layer.class);
                for (Iterator i = layerCollection.iterator(); i.hasNext(); ) {
                    Layer layer = (Layer) i.next();
                    if (layer.isLoadDeferred()) deferredLoadsExist = true;
                }
                return (((deferredLoadsExist))) ? "Can not convert units for a task with load-deferred layers. Turn all layers visible." : null;
            }
        };
    }

    public static MultiEnableCheck createEnableCheck(WorkbenchContext workbenchContext) {
        EnableCheckFactory checkFactory = new EnableCheckFactory(workbenchContext);
        return new MultiEnableCheck().add(checkFactory.createAtLeastNLayersMustExistCheck(1)).add(noLoadDeferredLayersCanExistCheck());
    }
}
