package scene.factory.property;

import java.util.Arrays;
import java.util.List;
import scene.factory.InvalidValueException;

/**
 * A property accepting boolean values.
 * 
 * The set of accepted values is {true, false, yes, no, 1, 0} (modulo
 * capitalization).
 * 
 * @author tom
 */
public class BooleanProperty extends Property {

    /**
	 * The set of strings yielding a positive value.
	 */
    static final String[] yes = new String[] { "yes", "true", "1" };

    /**
	 * The set of strings yielding a negative value.
	 */
    static final String[] no = new String[] { "no", "false", "0" };

    /**
	 * The list of strings to used when toggling in the level editor.
	 */
    static final List<String> toggleValues = Arrays.asList("Yes", "No");

    /**
	 * @param name
	 *            The name of the property.
	 */
    public BooleanProperty(String name) {
        super(name);
    }

    /**
	 * @param name
	 *            The name of the property.
	 * @param friendlyName
	 *            A more user-friendly version of the property name.
	 */
    public BooleanProperty(String name, String friendlyName) {
        super(name, friendlyName);
    }

    /**
	 * @param name
	 *            The name of the property.
	 * @param friendlyName
	 *            A more user-friendly version of the property name.
	 * @param description
	 *            A short description of the property.
	 */
    public BooleanProperty(String name, String friendlyName, String description) {
        super(name, friendlyName, description);
    }

    @Override
    public boolean validate(String value) {
        for (String s : yes) if (s.equalsIgnoreCase(value)) return true;
        for (String s : no) if (s.equalsIgnoreCase(value)) return true;
        return false;
    }

    /**
	 * Parse a boolean value.
	 * 
	 * @param value
	 *            The value to parse.
	 * @return The parsed value.
	 * @throws InvalidValueException
	 *             if the value could not be parsed.
	 */
    public static boolean parse(String value) throws InvalidValueException {
        for (String s : yes) if (s.equalsIgnoreCase(value)) return true;
        for (String s : no) if (s.equalsIgnoreCase(value)) return false;
        throw new InvalidValueException("Boolean");
    }

    @Override
    public InputMethod getInputMethod() {
        return InputMethod.CYCLE;
    }

    @Override
    public List<String> getValues() {
        return toggleValues;
    }

    /**
	 * Convert a boolean value to a string value, using string constants as used
	 * in the toggle feature.
	 * 
	 * @param value
	 *            The boolean version to convert to a string.
	 * @return The value, converted to a string.
	 */
    public static String toString(boolean value) {
        return value ? toggleValues.get(0) : toggleValues.get(1);
    }
}
