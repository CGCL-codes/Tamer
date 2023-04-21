package ie.ucd.clops.runtime.options;

import ie.ucd.clops.runtime.options.EnumOption.EnumPart;
import ie.ucd.clops.runtime.options.exception.InvalidOptionPropertyValueException;
import ie.ucd.clops.runtime.options.exception.InvalidOptionValueException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class EnumListOption extends StringListOption implements IEnumListOption {

    private final EnumPart enumPart;

    public EnumListOption(String identifier, String prefix) {
        super(identifier, prefix);
        enumPart = new EnumPart();
    }

    @Override
    public String convertFromStringToListValue(String valueString) throws InvalidOptionValueException {
        if (!enumPart.isValidValue(valueString)) {
            throw new InvalidOptionValueException(valueString + " is not an allowed choice.");
        }
        return valueString;
    }

    @Override
    public void set(List<String> value) throws InvalidOptionValueException {
        List<String> invalidValues = new LinkedList<String>();
        for (String v : value) {
            if (!enumPart.isValidValue(v)) {
                invalidValues.add(v);
            }
        }
        if (invalidValues.size() == 0) {
            super.set(value);
        } else {
            if (invalidValues.size() == 1) {
                throw new InvalidOptionValueException(invalidValues.get(0) + " is not an valid choice.");
            } else {
                throw new InvalidOptionValueException(invalidValues + " are not valid choices.");
            }
        }
    }

    @Override
    protected String getTypeString() {
        return "EnumList";
    }

    private static Collection<String> acceptedPropertyNames;

    protected static Collection<String> getStaticAcceptedPropertyNames() {
        if (acceptedPropertyNames == null) {
            acceptedPropertyNames = new LinkedList<String>();
            acceptedPropertyNames.addAll(StringListOption.getStaticAcceptedPropertyNames());
            acceptedPropertyNames.addAll(EnumPart.getStaticAcceptedPropertyNames());
        }
        return acceptedPropertyNames;
    }

    @Override
    public void setProperty(String propertyName, String propertyValue) throws InvalidOptionPropertyValueException {
        if (!enumPart.setProperty(propertyName, propertyValue)) {
            super.setProperty(propertyName, propertyValue);
        }
    }

    @Override
    public Collection<String> getAcceptedPropertyNames() {
        return getStaticAcceptedPropertyNames();
    }
}
