package net.kodeninja.DMAP.parameters.dpap;

import net.kodeninja.DMAP.DataTypes.DMAPIntParameter;

/**
 * DPAP.ImagePixelHeight
 * The height of the image in pixels.
 * @author Charles Ikeson
 */
public class phgt extends DMAPIntParameter {

    private static final String PARAM_NAME = "DPAP.ImagePixelHeight";

    /**
	 * Default constructor used when reading tags from a stream.
	 */
    public phgt() {
        super(PARAM_NAME);
    }

    /**
	 * Constructor for the height parameter using the passed value. 
	 * @param Height The height of the image.
	 */
    public phgt(int Height) {
        super(PARAM_NAME, Height);
    }
}
