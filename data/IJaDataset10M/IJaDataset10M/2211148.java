package net.kodeninja.DMAP.parameters.dmap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * Generated class for parameter: dmap.authenticationschemes
 */
public class msas extends DMAPIntParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "dmap.authenticationschemes";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public msas() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public msas(int value) {
        super(PARAM_NAME, value);
    }
}
