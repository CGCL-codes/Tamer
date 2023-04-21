package etch.util.cmd;

/**
 * A parameter whose value is a double.
 */
public class DoubleParameter extends Parameter {

    /**
	 * @param cp
	 * @param name
	 * @param method
	 * @param description
	 * @param isRequired
	 * @param constraint
	 * @throws Exception
	 */
    public DoubleParameter(CommandParser cp, String name, String method, String description, boolean isRequired, Constraint constraint) throws Exception {
        super(cp, name, method, PARAMS, description, isRequired, constraint);
    }

    private static final Class<?>[] PARAMS = { CommandParser.class, Parameter.class, Double.class };

    @Override
    public Object convertValue(String value) {
        return new Double(value);
    }
}
