package net.sf.mzmine.modules.peaklistmethods.alignment.path;

import java.util.logging.Logger;
import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.impl.SimplePeakListAppliedMethod;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.peaklistmethods.alignment.path.functions.Aligner;
import net.sf.mzmine.modules.peaklistmethods.alignment.path.functions.ScoreAligner;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.project.MZmineProject;
import net.sf.mzmine.taskcontrol.AbstractTask;
import net.sf.mzmine.taskcontrol.TaskStatus;

/**
 *
 */
class PathAlignerTask extends AbstractTask {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private PeakList peakLists[], alignedPeakList;

    private String peakListName;

    private ParameterSet parameters;

    private Aligner aligner;

    PathAlignerTask(ParameterSet parameters) {
        this.parameters = parameters;
        peakLists = parameters.getParameter(PathAlignerParameters.peakLists).getValue();
        ;
        peakListName = parameters.getParameter(PathAlignerParameters.peakListName).getValue();
    }

    /**
     * @see net.sf.mzmine.taskcontrol.Task#getTaskDescription()
     */
    public String getTaskDescription() {
        return "Path aligner, " + peakListName + " (" + peakLists.length + " peak lists)";
    }

    /**
     * @see net.sf.mzmine.taskcontrol.Task#getFinishedPercentage()
     */
    public double getFinishedPercentage() {
        if (aligner == null) {
            return 0f;
        } else {
            return aligner.getProgress();
        }
    }

    /**
     * @see Runnable#run()
     */
    public void run() {
        setStatus(TaskStatus.PROCESSING);
        logger.info("Running Path aligner");
        aligner = (Aligner) new ScoreAligner(this.peakLists, parameters);
        alignedPeakList = aligner.align();
        MZmineProject currentProject = MZmineCore.getCurrentProject();
        currentProject.addPeakList(alignedPeakList);
        alignedPeakList.addDescriptionOfAppliedTask(new SimplePeakListAppliedMethod("Path aligner", parameters));
        logger.info("Finished Path aligner");
        setStatus(TaskStatus.FINISHED);
    }

    public Object[] getCreatedObjects() {
        return new Object[] { alignedPeakList };
    }
}
