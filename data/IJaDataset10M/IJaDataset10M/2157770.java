package org.chartsy.htsine;

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
        super("Hilbert Transform Sine Properties");
    }

    public IndicatorNode(IndicatorProperties indicatorProperties) {
        super("Hilbert Transform Sine Properties", indicatorProperties);
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
            set.put(getProperty("In Phase Line Color", "Sets the in phase line color", IndicatorProperties.class, Color.class, null, "getSineLineColor", "setSineLineColor", IndicatorProperties.SINE_LINE_COLOR));
            set.put(getProperty("Sine Line Style", "Sets the sine line style", IndicatorProperties.class, Stroke.class, StrokePropertyEditor.class, "getSineLineStroke", "setSineLineStroke", StrokeGenerator.getStroke(IndicatorProperties.SINE_LINE_STROKE_INDEX)));
            set.put(getProperty("Lead Sine Line Color", "Sets the lead sine line color", IndicatorProperties.class, Color.class, null, "getLeadSineLineColor", "setLeadSineLineColor", IndicatorProperties.LEAD_SINE_LINE_COLOR));
            set.put(getProperty("Lead Sine Line Style", "Sets the lead sine line style", IndicatorProperties.class, Stroke.class, StrokePropertyEditor.class, "getLeadSineLineStroke", "setLeadSineLineStroke", StrokeGenerator.getStroke(IndicatorProperties.LEAD_SINE_LINE_STROKE_INDEX)));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "[Hilbert Transform Sine Node] : Method does not exist.", ex);
        }
        return sheet;
    }
}
