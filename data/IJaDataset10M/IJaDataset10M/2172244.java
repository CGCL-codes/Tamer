package net.sourceforge.mazix.core.business.impl.memory.levels.squares.wall;

import static net.sourceforge.mazix.core.business.levels.squares.SquareType.WALL_SQUARE_TYPE;
import net.sourceforge.mazix.components.exception.LogicException;
import net.sourceforge.mazix.core.business.levels.Level;
import net.sourceforge.mazix.core.business.levels.coordinates.Coordinates;
import net.sourceforge.mazix.core.business.levels.items.Item;
import net.sourceforge.mazix.core.business.levels.squares.Square;
import net.sourceforge.mazix.core.business.levels.squares.SquareType;
import net.sourceforge.mazix.core.business.levels.squares.wall.WallSquare;
import net.sourceforge.mazix.persistence.direction.Direction;

/**
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class WallSquareMemoryImpl implements WallSquare {

    /** Serial version UID. */
    private static final long serialVersionUID = 7491341168294640115L;

    /**
     * @throws LogicException
     * @since 0.8
     */
    public WallSquareMemoryImpl() throws LogicException {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public boolean canGoThrough(final Item itm, final Direction dir, final Coordinates coordsTo, final Level level) throws LogicException {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Square deepClone() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void moveItem(final Item itm, final Direction dir, final Coordinates coordsTo, final Level level) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void save() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public SquareType getSquareType() {
        return WALL_SQUARE_TYPE;
    }
}
