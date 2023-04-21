package net.sourceforge.pseudoq.generation;

import java.util.Map;
import net.sourceforge.pseudoq.model.Grid;
import net.sourceforge.pseudoq.model.Region;

/**
 * Specifies a service that can fill a grid with values.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public interface GridFiller {

    /**
     * Fill a grid with values, subject to the usual constraints.  In some
     * respects similar to a {@link net.sourceforge.pseudoq.solver.Solver},
     * but is not concerned with finding a unique solution.  Usually, it expects
     * an empty grid to be supplied, but if not will attempt to leave the
     * supplied values intact and will only fill in the empty cells.
     * @param grid The empty (or partly empty) grid.
     * @param regions Map of all the regions in the grid, for example, as
     * returned by {@link net.sourceforge.pseudoq.model.Puzzle#getRegions()}.
     * @param digits Maximum possible value.
     * @throws GenerationException if unable to fill the grid.
     */
    public void fill(Grid grid, Map<String, Region> regions, int digits) throws GenerationException;
}
