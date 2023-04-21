package gumbo.arg;

public class IntegerOption extends ValueOption {

    /**
	 * Creates an instance, with any number of found children.
	 * @param keys String of comma-delimited option keys that are aliases for
	 * this option. Never empty. Each key must contain its prefix (e.g. "-xxx",
	 * "--xxx").
	 * @param desc Description used in the usage report. Never empty.
	 * @param isRequired If true, and this option is findable, it must be found,
	 * regardless of any parent constraints.
	 */
    public IntegerOption(String keys, String desc, boolean isRequired) {
        this(keys, desc, isRequired, 0, Integer.MAX_VALUE);
    }

    /**
	 * Creates an instance.
	 * @param keys String of comma-delimited option keys that are aliases for
	 * this option. Never empty. Each key must contain its prefix (e.g. "-xxx",
	 * "--xxx").
	 * @param desc Description used in the usage report. Never empty.
	 * @param isRequired If true, and this option is findable, it must be found,
	 * regardless of any parent constraints.
	 * @param foundChildrenMin Minimum number of children that must be found
	 * (>=0, <=min).
	 * @param foundChildrenMax Maximum number of children that must be found
	 * (>=min).
	 */
    public IntegerOption(String keys, String desc, boolean isRequired, int foundChildrenMin, int foundChildrenMax) {
        super(keys, desc, isRequired, foundChildrenMin, foundChildrenMax, Integer.class);
    }

    @Override
    public final Integer getValue() {
        return (Integer) super.getValue();
    }

    /**
	 * Creates an option that is optional, with no constraints on the number of
	 * children found.
	 */
    public static IntegerOption newOptional(String keys, String desc) {
        return new IntegerOption(keys, desc, false, 0, Integer.MAX_VALUE);
    }

    /**
	 * Creates an option that is required, with no constraints on the number of
	 * children found.
	 */
    public static IntegerOption newRequired(String keys, String desc) {
        return new IntegerOption(keys, desc, true, 0, Integer.MAX_VALUE);
    }
}
