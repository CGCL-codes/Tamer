package org.chartsy.vwap;

import java.awt.Color;
import java.beans.PropertyEditorSupport;
import java.util.logging.Level;
import org.chartsy.main.chart.AbstractPropertiesNode;
import org.chartsy.main.utils.PricePropertyEditor;
import org.chartsy.main.utils.SerialVersion;
import org.openide.nodes.Sheet;

/**
 *
 * @author Viorel
 */
public class OverlayNode extends AbstractPropertiesNode {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public OverlayNode() {
        super("Volume Weighted Average Price");
    }

    public OverlayNode(OverlayProperties overlayProperties) {
        super("Volume Weighted Average Price", overlayProperties);
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
            set.put(getProperty("Price", "Sets the price type", OverlayProperties.class, String.class, PricePropertyEditor.class, "getPrice", "setPrice", OverlayProperties.PRICE));
            set.put(getProperty("Label", "Sets the label", OverlayProperties.class, String.class, PropertyEditorSupport.class, "getLabel", "setLabel", OverlayProperties.LABEL));
            set.put(getProperty("Marker Visibility", "Sets the marker visibility", OverlayProperties.class, boolean.class, null, "getMarker", "setMarker", OverlayProperties.MARKER));
            set.put(getProperty("Line Color", "Sets the line color", OverlayProperties.class, Color.class, null, "getColor", "setColor", OverlayProperties.COLOR));
            set.put(getProperty("Band 1 Visibility", "Sets the band 1 visibility", OverlayProperties.class, boolean.class, null, "getBand1Visibility", "setBand1Visibility", OverlayProperties.BAND1_VISIBILITY));
            set.put(getProperty("Band 1 Color", "Sets the band 1 color", OverlayProperties.class, Color.class, null, "getBand1Color", "setBand1Color", OverlayProperties.BAND1_COLOR));
            set.put(getProperty("Band 1 Dev", "Sets the band 1 dev value", OverlayProperties.class, int.class, null, "getBand1Dev", "setBand1Dev", OverlayProperties.BAND1_DEV));
            set.put(getProperty("Band 2 Visibility", "Sets the band 2 visibility", OverlayProperties.class, boolean.class, null, "getBand2Visibility", "setBand2Visibility", OverlayProperties.BAND2_VISIBILITY));
            set.put(getProperty("Band 2 Color", "Sets the band 2 color", OverlayProperties.class, Color.class, null, "getBand2Color", "setBand2Color", OverlayProperties.BAND2_COLOR));
            set.put(getProperty("Band 2 Dev", "Sets the band 2 dev value", OverlayProperties.class, int.class, null, "getBand2Dev", "setBand2Dev", OverlayProperties.BAND2_DEV));
            set.put(getProperty("Band 3 Visibility", "Sets the band 3 visibility", OverlayProperties.class, boolean.class, null, "getBand3Visibility", "setBand3Visibility", OverlayProperties.BAND3_VISIBILITY));
            set.put(getProperty("Band 3 Color", "Sets the band 3 color", OverlayProperties.class, Color.class, null, "getBand3Color", "setBand3Color", OverlayProperties.BAND3_COLOR));
            set.put(getProperty("Band 3 Dev", "Sets the band 3 dev value", OverlayProperties.class, int.class, null, "getBand3Dev", "setBand3Dev", OverlayProperties.BAND3_DEV));
        } catch (NoSuchMethodException ex) {
            LOG.log(Level.SEVERE, "[VWAPNode] : Method does not exist.", ex);
        }
        return sets;
    }
}
