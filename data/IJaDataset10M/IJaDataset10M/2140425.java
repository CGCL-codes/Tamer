package net.sourceforge.pseudoq.gui;

import net.sourceforge.pseudoq.model.PuzzleTypeEnum;

/**
 * Factory class for GridPainter instances.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public class GridPainterFactory {

    /** Log4J logger */
    private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager.getLogger(GridPainterFactory.class);

    /** Prevent instantiation as all methods are static. */
    private GridPainterFactory() {
    }

    /**
     * Create a new GridPainter instance for the specified puzzle type.  The
     * actual implementation class returned will depend on the type.
     * @param type The puzzle type.
     * @return An initialised painter, or null if the puzzle type is unknown
     * to the factory.
     */
    public static GridPainter newInstance(PuzzleTypeEnum type) {
        GridPainter painter = null;
        switch(type) {
            case TINY:
                painter = new TinyGridPainter();
                break;
            case MINI:
                painter = new MiniGridPainter();
                break;
            case MINI_X:
                painter = new MiniXGridPainter();
                break;
            case SMALL_X:
                painter = new SmallXGridPainter();
                break;
            case STANDARD:
                painter = new StandardGridPainter();
                break;
            case SUPER:
                painter = new SuperGridPainter();
                break;
            case LARGE:
                painter = new LargeGridPainter();
                break;
            case STANDARD_SAMURAI:
                painter = new SamuraiGridPainter();
                break;
            default:
                log.error("Unknown puzzle type");
                break;
        }
        return painter;
    }
}
