package net.kodeninja.DMAP.parameters.dpap;

import java.util.Date;
import net.kodeninja.DMAP.DataTypes.DMAPDateParameter;

/**
 * DPAP.CreationDate
 * The creation date of the image.
 * @author Charles Ikeson
 *
 */
public class picd extends DMAPDateParameter {

    private static final String PARAM_NAME = "DPAP.CreationDate";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public picd() {
        super(PARAM_NAME);
    }

    /**
	 * Creates the parameter using the passed value.
	 * @param CreationDate The creation date of the image.
	 */
    public picd(Date CreationDate) {
        super(PARAM_NAME, CreationDate);
    }
}
