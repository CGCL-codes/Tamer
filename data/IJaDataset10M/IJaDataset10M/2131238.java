package net.sourceforge.pseudoq.solver;

import java.util.Map;
import net.sourceforge.pseudoq.model.Coordinate;
import net.sourceforge.pseudoq.model.Puzzle;
import net.sourceforge.pseudoq.model.Region;

/**
 * Solver for MINI type puzzles.
 * @author <a href="http://sourceforge.net/users/stevensa">Andrew Stevens</a>
 */
public class MiniSolver extends AbstractSolver {

    /** Log4J logger */
    private static final org.apache.log4j.Logger log = org.apache.log4j.LogManager.getLogger(MiniSolver.class);

    /**
     * Creates a new instance of MiniSolver 
     */
    public MiniSolver(Puzzle puzzle) {
        super(puzzle);
    }

    protected void setupCounters(Puzzle puzzle) {
        Map<String, Region> regions = puzzle.getRegions();
        int maxint = puzzle.getMaxInt();
        log.debug("Setting up counters");
        for (int i = 0; i < maxint; i++) {
            counters.put("rowValueCount-" + i, new RegionValueCounter(regions.get("row-" + i)));
            counters.put("columnValueCount-" + i, new RegionValueCounter(regions.get("column-" + i)));
            counters.put("boxValueCount-" + i, new RegionValueCounter(regions.get("box-" + i)));
            for (int j = 1; j <= maxint; j++) {
                counters.put("rowIndicatorCount-" + i + "-" + j, new RegionValueIndicatorCounter(regions.get("row-" + i), j));
                counters.put("columnIndicatorCount-" + i + "-" + j, new RegionValueIndicatorCounter(regions.get("column-" + i), j));
                counters.put("boxIndicatorCount-" + i + "-" + j, new RegionValueIndicatorCounter(regions.get("box-" + i), j));
            }
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= maxint; j++) {
                Region superRow = regions.get("superRow-" + i);
                counters.put("superRowIndicatorCount-" + i + "-" + j, new RegionValueIndicatorCounter(superRow, j));
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= maxint; j++) {
                Region superColumn = regions.get("superColumn-" + i);
                counters.put("superColumnIndicatorCount-" + i + "-" + j, new RegionValueIndicatorCounter(superColumn, j));
            }
        }
        for (Coordinate coord : grid.keySet()) {
            Region cellRegions = new Region();
            for (Region region : regions.values()) {
                if (region.contains(coord) && region.getName() != null && (region.getName().startsWith("Row ") || region.getName().startsWith("Column ") || region.getName().startsWith("Box "))) {
                    cellRegions.addAll(region);
                }
            }
            counters.put("cellPossibilitiesCount-" + coord.getRow() + "-" + coord.getColumn(), new CellPossibilitiesCounter(coord, maxint, cellRegions));
        }
    }

    public void setupStrategies(Puzzle puzzle) {
        Map<String, Region> regions = puzzle.getRegions();
        int maxint = puzzle.getMaxInt();
        log.debug("Setting up strategies");
        for (int i = 0; i < maxint; i++) {
            strategies.add(new GapFillRegionStrategy(grid, counters.get("rowValueCount-" + i), regions.get("row-" + i), maxint));
            strategies.add(new GapFillRegionStrategy(grid, counters.get("columnValueCount-" + i), regions.get("column-" + i), maxint));
            strategies.add(new GapFillRegionStrategy(grid, counters.get("boxValueCount-" + i), regions.get("box-" + i), maxint));
        }
        for (int i = 0; i < maxint; i++) {
            strategies.add(new ScanRowsStrategy(0, i + 1, 0, 1, grid, counters, 0, 1, 3));
            strategies.add(new ScanRowsStrategy(1, i + 1, 2, 3, grid, counters, 2, 3, 3));
            strategies.add(new ScanRowsStrategy(2, i + 1, 4, 5, grid, counters, 4, 5, 3));
            strategies.add(new ScanColumnsStrategy(0, i + 1, 0, 2, grid, counters, 0, 4, 2, 2));
            strategies.add(new ScanColumnsStrategy(1, i + 1, 3, 5, grid, counters, 1, 5, 2, 2));
        }
        for (Coordinate coord : grid.keySet()) {
            if (Integer.valueOf(0).equals(grid.get(coord))) {
                strategies.add(new SingleCellStrategy(coord, grid, counters.get("cellPossibilitiesCount-" + coord.getRow() + "-" + coord.getColumn())));
            }
        }
        for (int i = 0; i < maxint; i++) {
            for (int j = 1; j <= maxint; j++) {
                strategies.add(new ValuePossibilitiesStrategy(regions.get("row-" + i), j, (RegionValueIndicatorCounter) counters.get("rowIndicatorCount-" + i + "-" + j), counters));
                strategies.add(new ValuePossibilitiesStrategy(regions.get("column-" + i), j, (RegionValueIndicatorCounter) counters.get("columnIndicatorCount-" + i + "-" + j), counters));
                strategies.add(new ValuePossibilitiesStrategy(regions.get("box-" + i), j, (RegionValueIndicatorCounter) counters.get("boxIndicatorCount-" + i + "-" + j), counters));
            }
        }
    }
}
