package Cells;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * Utility class that calculates total surface coverage as number of cells / 
 * number of available sites.
 *  
 * @author Eric Tatara
 *
 */
public class CoverageCounter {

    public double coveragePercent;

    @ScheduledMethod(start = 1, interval = 1, priority = ScheduleParameters.LAST_PRIORITY)
    public void step() {
        updateCount();
    }

    public void updateCount() {
        Context context = ContextUtils.getContext(this);
        Grid grid = (Grid) context.getProjection("Grid");
        double numPoints = grid.getDimensions().getHeight() * grid.getDimensions().getWidth();
        double numCells = context.size() - 1;
        coveragePercent = (numCells / numPoints) * 100;
    }

    public double getCoveragePercent() {
        return coveragePercent;
    }
}
