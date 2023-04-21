package com.vividsolutions.jump.workbench.ui.plugin.analysis;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jump.feature.*;
import com.vividsolutions.jump.task.TaskMonitor;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.StandardCategoryNames;
import com.vividsolutions.jump.workbench.plugin.*;
import com.vividsolutions.jump.workbench.plugin.util.*;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.MultiInputDialog;
import com.vividsolutions.jump.workbench.ui.SelectionManager;

/**
* Queries a layer by a spatial predicate.
*/
public class AttributeQueryPlugIn extends AbstractPlugIn implements ThreadedPlugIn {

    private static final String ATTR_GEOMETRY_AREA = "Geometry.Area";

    private static final String ATTR_GEOMETRY_LENGTH = "Geometry.Length";

    private static final String ATTR_GEOMETRY_NUMPOINTS = "Geometry.NumPoints";

    private static final String ATTR_GEOMETRY_NUMCOMPONENTS = "Geometry.NumComponents";

    private static final String ATTR_GEOMETRY_ISCLOSED = "Geometry.IsClosed";

    private static final String ATTR_GEOMETRY_ISSIMPLE = "Geometry.IsSimple";

    private static final String ATTR_GEOMETRY_ISVALID = "Geometry.IsValid";

    private static final String ATTR_GEOMETRY_TYPE = "Geometry.Type";

    private Collection functionNames;

    private MultiInputDialog dialog;

    private Layer srcLayer;

    private String attrName;

    private String funcNameToRun;

    private String value = "";

    private boolean complementResult = false;

    private boolean exceptionThrown = false;

    private JRadioButton updateSourceRB;

    private JRadioButton createNewLayerRB;

    private boolean createLayer = true;

    public AttributeQueryPlugIn() {
        functionNames = AttributePredicate.getNames();
    }

    private String categoryName = StandardCategoryNames.RESULT;

    public void setCategoryName(String value) {
        categoryName = value;
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
        if (srcLayer == null) return false;
        if (StringUtil.isEmpty(value)) return false;
        if (StringUtil.isEmpty(attrName)) return false;
        if (StringUtil.isEmpty(funcNameToRun)) return false;
        return true;
    }

    public void run(TaskMonitor monitor, PlugInContext context) throws Exception {
        monitor.allowCancellationRequests();
        monitor.report("Executing query...");
        FeatureCollection sourceFC = srcLayer.getFeatureCollectionWrapper();
        if (monitor.isCancelRequested()) return;
        FeatureCollection resultFC = executeQuery(sourceFC, attrName, value);
        if (createLayer) {
            String outputLayerName = LayerNameGenerator.generateOperationOnLayerName(funcNameToRun, srcLayer.getName());
            context.getLayerManager().addCategory(categoryName);
            context.addLayer(categoryName, outputLayerName, resultFC);
        } else {
            SelectionManager selectionManager = context.getLayerViewPanel().getSelectionManager();
            selectionManager.clear();
            selectionManager.getFeatureSelection().selectItems(srcLayer, resultFC.getFeatures());
        }
        if (exceptionThrown) {
            context.getWorkbenchFrame().warnUser("Errors found while executing query");
        }
    }

    private FeatureCollection executeQuery(FeatureCollection sourceFC, String attrName, String value) {
        AttributePredicate pred = AttributePredicate.getPredicate(funcNameToRun);
        FeatureCollection resultFC = new FeatureDataset(sourceFC.getFeatureSchema());
        for (Iterator i = sourceFC.iterator(); i.hasNext(); ) {
            Feature f = (Feature) i.next();
            Object fVal = getValue(f, attrName);
            boolean predResult = pred.isTrue(fVal, value);
            if (complementResult) predResult = !predResult;
            if (predResult) {
                if (createLayer) {
                    resultFC.add(f.clone(true));
                } else {
                    resultFC.add(f);
                }
            }
        }
        return resultFC;
    }

    private Object getValue(Feature f, String attrName) {
        if (attrName == ATTR_GEOMETRY_AREA) {
            Geometry g = f.getGeometry();
            double area = (g == null) ? 0.0 : g.getArea();
            return new Double(area);
        }
        if (attrName == ATTR_GEOMETRY_LENGTH) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getLength();
            return new Double(len);
        }
        if (attrName == ATTR_GEOMETRY_NUMPOINTS) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getNumPoints();
            return new Double(len);
        }
        if (attrName == ATTR_GEOMETRY_NUMCOMPONENTS) {
            Geometry g = f.getGeometry();
            double len = (g == null) ? 0.0 : g.getNumGeometries();
            return new Double(len);
        }
        if (attrName == ATTR_GEOMETRY_ISCLOSED) {
            Geometry g = f.getGeometry();
            if (g instanceof LineString) return new Boolean(((LineString) g).isClosed());
            if (g instanceof MultiLineString) return new Boolean(((MultiLineString) g).isClosed());
            return new Boolean(false);
        }
        if (attrName == ATTR_GEOMETRY_ISSIMPLE) {
            Geometry g = f.getGeometry();
            boolean bool = g.isSimple();
            return new Boolean(bool);
        }
        if (attrName == ATTR_GEOMETRY_ISVALID) {
            Geometry g = f.getGeometry();
            boolean bool = g.isValid();
            return new Boolean(bool);
        }
        if (attrName == ATTR_GEOMETRY_TYPE) {
            Geometry g = f.getGeometry();
            return StringUtil.classNameWithoutQualifiers(g.getClass().getName());
        }
        return f.getAttribute(attrName);
    }

    private static final String LAYER = "Source Layer";

    private static final String ATTRIBUTE = "Attribute";

    private static final String PREDICATE = "Relation";

    private static final String VALUE = "Value";

    private static final String DIALOG_COMPLEMENT = "Complement Result";

    private static final String UPDATE_SRC = "Select features in the source layer.";

    private static final String CREATE_LYR = "Create a new layer for the results.";

    private JComboBox attrComboBox;

    private void setDialogValues(MultiInputDialog dialog, PlugInContext context) {
        dialog.setSideBarDescription("Finds the Source features which have attribute values satisfying a given condition");
        Layer initLayer = (srcLayer == null) ? context.getCandidateLayer(0) : srcLayer;
        JComboBox lyrCombo = dialog.addLayerComboBox(LAYER, initLayer, context.getLayerManager());
        lyrCombo.addItemListener(new LayerItemListener());
        attrComboBox = dialog.addComboBox(ATTRIBUTE, attrName, functionNames, null);
        dialog.addComboBox(PREDICATE, funcNameToRun, functionNames, null);
        dialog.addTextField(VALUE, value, 20, null, null);
        dialog.addCheckBox(DIALOG_COMPLEMENT, complementResult);
        final String OUTPUT_GROUP = "OUTPUT_GROUP";
        createNewLayerRB = dialog.addRadioButton(CREATE_LYR, OUTPUT_GROUP, createLayer, CREATE_LYR);
        updateSourceRB = dialog.addRadioButton(UPDATE_SRC, OUTPUT_GROUP, !createLayer, UPDATE_SRC);
        updateUI(initLayer);
    }

    private void getDialogValues(MultiInputDialog dialog) {
        srcLayer = dialog.getLayer(LAYER);
        attrName = dialog.getText(ATTRIBUTE);
        funcNameToRun = dialog.getText(PREDICATE);
        value = dialog.getText(VALUE);
        complementResult = dialog.getBoolean(DIALOG_COMPLEMENT);
        createLayer = dialog.getBoolean(CREATE_LYR);
    }

    private void updateUI(Layer lyr) {
        List attrNames = null;
        if (lyr != null) {
            FeatureCollection fc = lyr.getFeatureCollectionWrapper();
            FeatureSchema fs = fc.getFeatureSchema();
            attrNames = getAttributeNames(fs);
        } else {
            attrNames = new ArrayList();
        }
        attrComboBox.setModel(new DefaultComboBoxModel(new Vector(attrNames)));
        attrComboBox.setSelectedItem(attrName);
    }

    private static List getAttributeNames(FeatureSchema fs) {
        List names = new ArrayList();
        for (int i = 0; i < fs.getAttributeCount(); i++) {
            if (fs.getAttributeType(i) != AttributeType.GEOMETRY) names.add(fs.getAttributeName(i));
        }
        names.add(ATTR_GEOMETRY_AREA);
        names.add(ATTR_GEOMETRY_LENGTH);
        names.add(ATTR_GEOMETRY_NUMPOINTS);
        names.add(ATTR_GEOMETRY_NUMCOMPONENTS);
        names.add(ATTR_GEOMETRY_ISCLOSED);
        names.add(ATTR_GEOMETRY_ISSIMPLE);
        names.add(ATTR_GEOMETRY_ISVALID);
        names.add(ATTR_GEOMETRY_TYPE);
        return names;
    }

    private class LayerItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            updateUI((Layer) e.getItem());
        }
    }
}
