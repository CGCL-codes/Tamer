package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songkeywords
 */
public class asky extends DMAPStringParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "daap.songkeywords";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public asky() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public asky(String value) {
        super(PARAM_NAME, value);
    }
}
