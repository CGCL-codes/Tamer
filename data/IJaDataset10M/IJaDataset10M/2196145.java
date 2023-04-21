package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPStringParameter;

/**
 * Generated class for parameter: daap.songeqpreset
 */
public class aseq extends DMAPStringParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "daap.songeqpreset";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public aseq() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public aseq(String value) {
        super(PARAM_NAME, value);
    }
}
