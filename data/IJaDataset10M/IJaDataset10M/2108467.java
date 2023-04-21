package org.km.xplane.airports.gui.internal.data;

import java.util.ArrayList;
import org.eclipse.ui.IWorkbenchPage;
import org.km.xplane.airports.math.Tools;
import org.km.xplane.airports.model.Constants;
import org.km.xplane.airports.model.Runway;

public class RunwayAdapter extends AirportPartAdapter implements IChangeListener, IEnumerationPropertyProvider {

    private static final String WIDTH = "Width";

    private static final String CENTER_LIGHTS = "Center Lights";

    private static final String DISTANCE_SIGNS = "Distance Lights";

    private static final String EDGE_LIGHTS = "Edge Lights";

    private static final String SHOULDER = "Shoulder";

    private static final String SMOOTH = "Smooth";

    private static final String SURFACE = "Surface";

    private static final String LENGTH = "Length";

    private final Runway _runway;

    private final RunwaySideDummyAdapter[] _sides;

    RunwayAdapter(Runway runway) {
        super(runway);
        _runway = runway;
        _sides = new RunwaySideDummyAdapter[] { new RunwaySideDummyAdapter(runway, 0), new RunwaySideDummyAdapter(runway, 1) };
        _sides[0].addListener(this);
        _sides[1].addListener(this);
    }

    @Override
    public String getName() {
        return _runway.getName(0) + " - " + _runway.getName(1);
    }

    @Override
    public AirportPartAdapter[] getChildren() {
        return _sides;
    }

    @Override
    public Object[] getProperties() {
        ArrayList<Property> properties = new ArrayList<Property>();
        properties.add(new Property(this, WIDTH));
        properties.add(new BooleanEnumProperty(this, CENTER_LIGHTS));
        properties.add(new BooleanEnumProperty(this, DISTANCE_SIGNS));
        properties.add(new EnumProperty(this, EDGE_LIGHTS));
        properties.add(new EnumProperty(this, SHOULDER));
        properties.add(new Property(this, SMOOTH));
        properties.add(new EnumProperty(this, SURFACE));
        properties.add(new ReadOnlyProperty(this, LENGTH));
        return properties.toArray();
    }

    @Override
    public String getPropertyValue(String name) {
        if (name.equals(WIDTH)) return _runway.getWidth();
        if (name.equals(CENTER_LIGHTS)) return _runway.getCenterLights() ? "true" : "false";
        if (name.equals(DISTANCE_SIGNS)) return _runway.getDistanceSigns() ? "true" : "false";
        if (name.equals(EDGE_LIGHTS)) return Constants.edgeLightsToString(_runway.getEdgeLights());
        if (name.equals(SHOULDER)) return Constants.shoulderToString(_runway.getShoulder());
        if (name.equals(SMOOTH)) return _runway.getSmooth();
        if (name.equals(SURFACE)) return Constants.surfaceToString(_runway.getSurface());
        if (name.equals(LENGTH)) return Tools.doubleToString(_runway.getLength(), 2);
        return "<novalue>";
    }

    @Override
    public void setValue(String name, Object value) {
        if (name.equals(WIDTH)) _runway.setWidth((String) value);
        if (name.equals(CENTER_LIGHTS)) _runway.setCenterLights((Boolean) value);
        if (name.equals(DISTANCE_SIGNS)) _runway.setDistanceSigns((Boolean) value);
        if (name.equals(EDGE_LIGHTS)) _runway.setEdgeLights((Integer) value);
        if (name.equals(SHOULDER)) _runway.setShoulder((Integer) value);
        if (name.equals(SMOOTH)) _runway.setSmooth((String) value);
        if (name.equals(SURFACE)) _runway.setSurface(1 + (Integer) value);
        notifyChanged();
    }

    public void changed(Object element) {
        notifyChanged();
    }

    @Override
    public void registerOnPage(IWorkbenchPage page) {
        _sides[0].registerOnPage(page);
        _sides[1].registerOnPage(page);
        super.registerOnPage(page);
    }

    public String[] getValueItems(Object id) {
        if (id.equals(EDGE_LIGHTS)) return Constants.getEdgeLightsStringValues(); else if (id.equals(SHOULDER)) return Constants.getShoulderStringValues(); else if (id.equals(SURFACE)) return Constants.getSurfaceStringValues();
        return null;
    }

    public int stringToValue(Object id, String stringValue) {
        if (id.equals(EDGE_LIGHTS)) return Constants.edgeLightsToValue(stringValue); else if (id.equals(SHOULDER)) return Constants.shoulderToValue(stringValue); else if (id.equals(SURFACE)) return Constants.surfaceToValue(stringValue);
        return 0;
    }
}
