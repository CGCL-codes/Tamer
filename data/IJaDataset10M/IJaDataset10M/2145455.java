package net.kodeninja.DMAP.parameters.daap;

import net.kodeninja.DMAP.DataTypes.DMAPDateParameter;
import java.util.Date;

/**
 * Generated class for parameter: daap.songdatemodified
 */
public class asdm extends DMAPDateParameter {

    /**
	 * Parameter Type
	 */
    private static final String PARAM_NAME = "daap.songdatemodified";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public asdm() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor used to create the tag.
	 * @param value The value of the parameter.
	 */
    public asdm(Date value) {
        super(PARAM_NAME, value);
    }
}
