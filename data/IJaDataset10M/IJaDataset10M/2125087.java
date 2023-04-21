package eu.actorsproject.xlim.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for TranslationOptions and their values 
 */
public abstract class BagOfTranslationOptions {

    protected Map<String, Object> mValues = new HashMap<String, Object>();

    public Object getValue(String optionName) {
        Object value = mValues.get(optionName);
        if (value == null) value = getOverriddenValue(optionName);
        return value;
    }

    public boolean getBooleanValue(String optionName) {
        Object value = getValue(optionName);
        if (value != null && value instanceof Boolean) return (Boolean) value; else throw new IllegalArgumentException("No such boolean-valued option: " + optionName);
    }

    public void setValue(String optionName, String value) {
        TranslationOption option = getOption(optionName);
        if (option != null) {
            Object o = option.checkValue(value);
            if (o != null) mValues.put(optionName, o); else throw new IllegalArgumentException("Illegal value for option \"" + optionName + "\": \"" + value + "\"");
        } else throw new IllegalArgumentException("No such option: " + optionName);
    }

    public boolean hasOption(String optionName) {
        return getOption(optionName) != null;
    }

    public void registerOption(TranslationOption option) {
        throw new UnsupportedOperationException();
    }

    public abstract TranslationOption getOption(String optionName);

    protected abstract Object getOverriddenValue(String optionName);
}
