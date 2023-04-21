package net.kodeninja.DMAP.parameters.com.apple.itunes;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: com.apple.itunes.series-name
 */
public class aeSN extends DMAPStringParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "com.apple.itunes.series-name";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public aeSN() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public aeSN(String value) {
        super(PARAM_NAME, value);
    }
}
