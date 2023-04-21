package zet.tasks;

import ds.z.Project;
import gui.ZETMain;

/**
 * Performs the rasterization of a {@link ds.z.BuildingPlan}.
 * @author Jan-Philipp Kappmeier
 */
public class RasterizeTask implements Runnable {

    /** The project that should be rastered. */
    private Project project;

    /** Creates a new instance of the rasterization task.
	 * @param p
	 */
    public RasterizeTask(Project p) {
        if (p == null) {
            throw new java.lang.IllegalArgumentException("Project is null.");
        }
        project = p;
    }

    /** Performs rasterization. */
    public void run() {
        try {
            project.getBuildingPlan().rasterize();
        } catch (Exception ex) {
            ZETMain.sendError(ex.getLocalizedMessage());
        }
    }
}
