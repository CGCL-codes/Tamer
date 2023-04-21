package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: com.apple.itunes.itms-songid
 */
public class aeSI extends DMAPIntParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "com.apple.itunes.itms-songid";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public aeSI() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public aeSI(int value) {
        super(PARAM_NAME, value);
    }
}
