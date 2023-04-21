package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPShortParameter;

/**
 * Generated class for parameter: daap.songdisccount
 */
public class asdc extends DMAPShortParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "daap.songdisccount";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public asdc() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public asdc(short value) {
        super(PARAM_NAME, value);
    }
}
