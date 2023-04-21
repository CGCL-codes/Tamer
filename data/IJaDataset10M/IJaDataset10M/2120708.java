package org.gromurph.javascore.model.ratings;

/**
 * Portsmouth Yardstick support... see www.ussailing.org/portsmouth for
 * description.  This is a first cut at Portsmouth support.  It does not
 * yet support different wind factors (unless you change the rating for
 * a boat to the appropriate wind-based factor).
**/
public class RatingPortsmouth extends RatingYardstick {

    public static final String SYSTEM = "Portsmouth";

    public RatingPortsmouth() {
        super(SYSTEM, RatingYardstick.MAX_RATING);
    }

    public RatingPortsmouth(double inV) {
        super(SYSTEM, inV);
    }

    public int getDecs() {
        return 1;
    }

    /**
     * Creates a new instance of the maximum/fastest overall rating allowed by the rating system
     * @return
     */
    public Rating createMaxRating() {
        return new RatingPortsmouth(0.0);
    }

    /**
     * Creates a new instance of the minimum/slowest overall rating allowed by the rating system
     * @return
     */
    public Rating createMinRating() {
        return new RatingPortsmouth(9999.0);
    }
}
