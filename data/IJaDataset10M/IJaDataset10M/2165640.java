package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: daap.songstarttime
 */
public class asst extends DMAPIntParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "daap.songstarttime";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public asst() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public asst(int value) {
        super(PARAM_NAME, value);
    }
}
