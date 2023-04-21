package com.rapidminer.gui.properties;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Properties;
import com.rapidminer.operator.Operator;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.tools.ParameterService;

/**
 * This class is somehow missnamed, because it's original purpose is to
 * display defined Parameters of the {@link ParameterService}.
 * It also holds a method for applying changes in the value back to the {@link ParameterService}.
 * @author Sebastian Land, Simon Fischer
 */
public class SettingsPropertyPanel extends PropertyPanel {

    private static final long serialVersionUID = 313811558626370370L;

    private final Collection<ParameterType> shownParameterTypes;

    private final Properties parameterValues;

    public SettingsPropertyPanel(Collection<ParameterType> shownParameterTypes) {
        this.shownParameterTypes = shownParameterTypes;
        this.parameterValues = new Properties();
        for (ParameterType type : shownParameterTypes) {
            String key = type.getKey();
            parameterValues.put(key, ParameterService.getParameterValue(key));
        }
        setupComponents();
    }

    @Override
    protected Collection<ParameterType> getProperties() {
        return shownParameterTypes;
    }

    @Override
    protected String getValue(ParameterType type) {
        String value = parameterValues.getProperty(type.getKey());
        if (value == null) {
            return null;
        } else {
            return type.transformNewValue(value);
        }
    }

    @Override
    protected void setValue(Operator operator, ParameterType type, String value) {
        parameterValues.put(type.getKey(), value);
    }

    /** Applies the properties without saving them. */
    public void applyProperties() {
        for (ParameterType type : shownParameterTypes) {
            String value = parameterValues.getProperty(type.getKey());
            ParameterService.setParameterValue(type, value);
        }
    }

    @Override
    protected Operator getOperator() {
        return null;
    }

    /**
     * This method remains for compatibility. But the settings edited in this pane
     * will now be directly saved by in the ParameterService.
     */
    @Deprecated
    public void applyProperties(Properties properties) {
        applyProperties();
    }

    /**
     * This method is deprecated and won't affect any parameters in the {@link ParameterService}.
     * Applies and write the properties in the system dependent config file in
     * the user directory.
     * 
     */
    @Deprecated
    public void writeProperties(PrintWriter out) throws IOException {
        for (ParameterType type : shownParameterTypes) {
            String value = getValue(type);
            if (value != null) {
                System.setProperty(type.getKey(), value);
                out.println(type.getKey() + " = " + value);
            }
        }
    }
}
