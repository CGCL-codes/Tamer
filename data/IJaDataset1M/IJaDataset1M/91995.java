package org.km.xplane.airports.gui.internal.data;

import java.util.ArrayList;
import org.km.xplane.airports.math.Tools;
import org.km.xplane.airports.model.ATC;
import org.km.xplane.airports.model.Constants;

public class ATCAdapter extends AirportPartAdapter implements IEnumerationPropertyProvider {

    private static final String NAME = "Name";

    private static final String FREQUENCY = "Frequency";

    private static final String TYPE = "Type";

    private final ATC _atc;

    ATCAdapter(ATC atc) {
        super(atc);
        _atc = atc;
    }

    @Override
    public String getName() {
        return _atc.getName();
    }

    @Override
    public Object[] getProperties() {
        ArrayList<Property> properties = new ArrayList<Property>();
        properties.add(new Property(this, NAME));
        properties.add(new Property(this, FREQUENCY));
        properties.add(new EnumProperty(this, TYPE));
        return properties.toArray();
    }

    @Override
    public String getPropertyValue(String name) {
        if (name.equals(NAME)) return _atc.getName();
        if (name.equals(FREQUENCY)) return Tools.doubleToString(_atc.getFrequency() / 100.0, 3, 3);
        if (name.equals(TYPE)) return Constants.atcToString(_atc.getType());
        return super.getPropertyValue(name);
    }

    @Override
    public void setValue(String name, Object value) {
        if (name.equals(NAME)) _atc.setName((String) value);
        if (name.equals(FREQUENCY)) _atc.setFrequency((int) Double.parseDouble((String) value) * 100);
        if (name.equals(TYPE)) _atc.setType((Integer) value);
        notifyChanged();
    }

    String[] getValueItems(String name) {
        return Constants.getATCValueItems();
    }

    public String[] getValueItems(Object id) {
        return Constants.getATCValueItems();
    }

    public int stringToValue(Object id, String stringValue) {
        return Constants.stringToAtc(stringValue);
    }
}
