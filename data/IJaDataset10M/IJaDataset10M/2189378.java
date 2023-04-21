package org.jjazz.rhythm.parameters;

import org.jjazz.rhythm.RhythmParameter;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.SwingPropertyChangeSupport;

/**
 * A RhythmParemeter whose value can be some specified strings.
 */
public class RP_StringEnum implements RhythmParameter<String>, Serializable {

    private String name;

    private String description;

    private String value;

    private String defaultValue;

    private String minValue;

    private String maxValue;

    private List<String> possibleValues;

    /** The listeners for changes in this model. */
    protected transient SwingPropertyChangeSupport pcs = new SwingPropertyChangeSupport(this);

    protected static final Logger LOGGER = Logger.getLogger(RP_StringEnum.class.getName());

    /**
     * @param name The name of the RhythmParameter.
     * @param defaultValue String The default value.
     * @param possibleValues String[] The possible values for this parameter.
     * By convention, min value is set to the 1st possible value, max value to the last one.
     */
    public RP_StringEnum(String name, String description, String defaultValue, String... possibleValues) {
        if (name == null || defaultValue == null || possibleValues == null || possibleValues.length == 0) {
            throw new IllegalArgumentException("name=" + name + " defaultVal=" + defaultValue + " possibleValues=" + possibleValues);
        }
        this.name = name;
        this.description = description;
        this.possibleValues = Arrays.asList(possibleValues);
        this.minValue = possibleValues[0];
        this.maxValue = possibleValues[possibleValues.length - 1];
        if (!this.possibleValues.contains(defaultValue)) {
            throw new IllegalArgumentException("n=" + name + " defaultVal=" + defaultValue + " possibleValues=" + possibleValues);
        }
        this.defaultValue = defaultValue;
    }

    @Override
    public RP_StringEnum clone() {
        return new RP_StringEnum(name, description, defaultValue, possibleValues.toArray(new String[0]));
    }

    @Override
    public void setValue(String v) {
        if (possibleValues.indexOf(v) == -1) {
            throw new IllegalArgumentException(v + " is not one of the possible values: " + possibleValues);
        }
        if (value != null && !value.equals(v)) {
            String oldValue = value;
            value = v;
            pcs.firePropertyChange(PROPERTY_VALUE_CHANGED, oldValue, value);
        }
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        pcs = new SwingPropertyChangeSupport(this);
    }

    /** Compatible if other is also a RP_StringEnum with same name. */
    @Override
    public boolean isCompatible(RhythmParameter rp) {
        return (rp instanceof RP_StringEnum) && rp.getName().equalsIgnoreCase(name);
    }

    @Override
    public String getTranscodedValue(RhythmParameter rp) {
        if (!isCompatible(rp)) {
            throw new IllegalArgumentException("this=" + this + " rp=" + rp);
        }
        RP_StringEnum rpd = (RP_StringEnum) rp;
        float rpdRatio = (float) rpd.possibleValues.indexOf(rpd.value) / (rpd.possibleValues.size() - 1);
        int newIndexValue = Math.round(rpdRatio * (possibleValues.size() - 1));
        return possibleValues.get(newIndexValue);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getMaxValue() {
        return maxValue;
    }

    @Override
    public String getMinValue() {
        return minValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNextValue() {
        int valueIndex = possibleValues.indexOf(value);
        return possibleValues.get((valueIndex >= possibleValues.size() - 1) ? 0 : valueIndex + 1);
    }

    @Override
    public String getPreviousValue() {
        int valueIndex = possibleValues.indexOf(value);
        return possibleValues.get((valueIndex == 0) ? possibleValues.size() - 1 : valueIndex - 1);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }
}
