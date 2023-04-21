package etch.util.cmd;

/**
 * An option whose value is a double.
 */
public class DoubleOption extends Option {

    /**
	 * @param cp
	 * @param tokens
	 * @param name
	 * @param method
	 * @param description
	 * @param flags
	 * @param defaultValue
	 * @param constraint
	 * @throws Exception
	 */
    public DoubleOption(CommandParser cp, String tokens, String name, String method, String description, int flags, Double defaultValue, Constraint constraint) throws Exception {
        super(cp, tokens, name, method, PARAMS, description, flags, defaultValue, constraint);
    }

    private static final Class<?>[] PARAMS = { CommandParser.class, Option.class, String.class, Double.class };

    @Override
    protected Object convertValue(String value) {
        return new Double(value);
    }
}
