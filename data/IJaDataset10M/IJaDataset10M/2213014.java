package org.chartsy.htdcp;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import org.chartsy.main.chart.AbstractPropertiesNode;
import org.chartsy.main.utils.SerialVersion;
import org.chartsy.main.utils.StrokeGenerator;
import org.chartsy.main.utils.StrokePropertyEditor;
import org.openide.nodes.Sheet;

/**
 *
 * @author joshua.taylor
 */
public class IndicatorNode extends AbstractPropertiesNode {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public IndicatorNode() {
        super("Hilbert Transform DCP Properties");
    }

    public IndicatorNode(IndicatorProperties indicatorProperties) {
        super("Hilbert Transform DCP Properties", indicatorProperties);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Sheet.Set set = getPropertiesSet();
        sheet.put(set);
        try {
            set.put(getProperty("Label", "Sets the label", IndicatorProperties.class, String.class, PropertyEditorSupport.class, "getLabel", "setLabel", IndicatorProperties.LABEL));
            set.put(getProperty("Marker Visibility", "Sets the marker visibility", IndicatorProperties.class, boolean.class, null, "getMarker", "setMarker", IndicatorProperties.MARKER));
            set.put(getProperty("Line Color", "Sets the line color", IndicatorProperties.class, Color.class, null, "getColor", "setColor", IndicatorProperties.COLOR));
            set.put(getProperty("Line Style", "Sets the line style", IndicatorProperties.class, Stroke.class, StrokePropertyEditor.class, "getStroke", "setStroke", StrokeGenerator.getStroke(IndicatorProperties.STROKE_INDEX)));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "[Hilbert Transform DC Period Node] : Method does not exist.", ex);
        }
        return sheet;
    }
}
