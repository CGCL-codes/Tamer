package org.chartsy.willr;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import org.chartsy.main.chart.AbstractPropertiesNode;
import org.chartsy.main.utils.AlphaPropertyEditor;
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
        super("Williams %R Properties");
    }

    public IndicatorNode(IndicatorProperties indicatorProperties) {
        super("Williams %R Properties", indicatorProperties);
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
            set.put(getProperty("Period", "Sets the period value", IndicatorProperties.class, int.class, null, "getPeriod", "setPeriod", IndicatorProperties.PERIOD));
            set.put(getProperty("Line Color", "Sets the line color", IndicatorProperties.class, Color.class, null, "getColor", "setColor", IndicatorProperties.COLOR));
            set.put(getProperty("Line Style", "Sets the line style", IndicatorProperties.class, Stroke.class, StrokePropertyEditor.class, "getStroke", "setStroke", StrokeGenerator.getStroke(IndicatorProperties.STROKE_INDEX)));
            set.put(getProperty("Inside Color", "Sets the inside color", IndicatorProperties.class, Color.class, null, "getInsideColor", "setInsideColor", IndicatorProperties.INSIDE_COLOR));
            set.put(getProperty("Inside Alpha", "Sets the inside alpha value", IndicatorProperties.class, int.class, AlphaPropertyEditor.class, "getInsideAlpha", "setInsideAlpha", IndicatorProperties.INSIDE_ALPHA));
            set.put(getProperty("Inside Visibility", "Sets the inside visibility flag", IndicatorProperties.class, boolean.class, null, "getInsideVisibility", "setInsideVisibility", IndicatorProperties.INSIDE_VISIBILITY));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "[Williams %R Node] : Method does not exist.", ex);
        }
        return sheet;
    }
}
