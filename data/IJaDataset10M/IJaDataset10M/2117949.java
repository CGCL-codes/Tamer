package net.sourceforge.mazix.persistence.dto.levels.items.ball;

import net.sourceforge.mazix.persistence.dto.levels.items.AbstractItemDTO;

/**
 * The data transfer object used as a ball wall item data carrier. Be careful, while this transfer
 * object is serializable, there isn't any business rules nor fields controls.
 * <p>
 * <i>Note : DTOs follows the Sun's JavaBean convention</i>
 * </p>
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.8
 * @version 0.8
 */
public class BallItemDTO extends AbstractItemDTO {

    /** Serial version UID. */
    private static final long serialVersionUID = 9215457957935399398L;

    /**
     * Empty constructor. Sets all fields to their default value.
     * 
     * @since 0.8
     */
    public BallItemDTO() {
        super();
    }

    /**
     * Full constructor.
     * 
     * @param lvlId
     *            the level id.
     * @param seriesFileName
     *            the level series file name.
     * @param xCoordinate
     *            the square x coordinate.
     * @param yCoordinate
     *            the square y coordinate.
     * @since 0.8
     */
    public BallItemDTO(final int lvlId, final String seriesFileName, final int xCoordinate, final int yCoordinate) {
        super(lvlId, seriesFileName, xCoordinate, yCoordinate);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public final String toString() {
        return getClass().getSimpleName() + " [" + super.toString() + "]";
    }
}
