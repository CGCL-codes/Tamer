package org.chartsy.trima;

import java.awt.Color;
import java.awt.Stroke;
import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import org.chartsy.main.chart.AbstractPropertiesNode;
import org.chartsy.main.utils.PricePropertyEditor;
import org.chartsy.main.utils.SerialVersion;
import org.chartsy.main.utils.StrokeGenerator;
import org.chartsy.main.utils.StrokePropertyEditor;
import org.openide.nodes.Sheet;

/**
 *
 * @author joshua.taylor
 */
public class OverlayNode extends AbstractPropertiesNode {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public OverlayNode() {
        super("TRIMA Properties");
    }

    public OverlayNode(OverlayProperties overlayProperties) {
        super("TRIMA Properties", overlayProperties);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Sheet createSheet() {
        Sheet sheet = new Sheet();
        sheet.put(getSets()[0]);
        return sheet;
    }

    @Override
    public Sheet.Set[] getSets() {
        Sheet.Set[] sets = new Sheet.Set[1];
        Sheet.Set set = getPropertiesSet();
        sets[0] = set;
        try {
            set.put(getProperty("Period", "Sets the period value", OverlayProperties.class, int.class, null, "getPeriod", "setPeriod", OverlayProperties.PERIOD));
            set.put(getProperty("Price", "Sets the price type", OverlayProperties.class, String.class, PricePropertyEditor.class, "getPrice", "setPrice", OverlayProperties.PRICE));
            set.put(getProperty("Label", "Sets the label", OverlayProperties.class, String.class, PropertyEditorSupport.class, "getLabel", "setLabel", OverlayProperties.LABEL));
            set.put(getProperty("Marker Visibility", "Sets the marker visibility", OverlayProperties.class, boolean.class, null, "getMarker", "setMarker", OverlayProperties.MARKER));
            set.put(getProperty("Line Color", "Sets the line color", OverlayProperties.class, Color.class, null, "getColor", "setColor", OverlayProperties.COLOR));
            set.put(getProperty("Line Style", "Sets the line style", OverlayProperties.class, Stroke.class, StrokePropertyEditor.class, "getStroke", "setStroke", StrokeGenerator.getStroke(OverlayProperties.STROKE_INDEX)));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "[TRIMA Node] : Method does not exist.", ex);
        }
        return sets;
    }
}
