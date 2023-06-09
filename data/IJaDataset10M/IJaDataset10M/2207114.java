package net.sf.mzmine.modules.masslistmethods.shoulderpeaksfilter;

import java.util.logging.Logger;
import net.sf.mzmine.data.DataPoint;
import net.sf.mzmine.data.MassList;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.data.Scan;
import net.sf.mzmine.data.impl.SimpleMassList;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.AbstractTask;
import net.sf.mzmine.taskcontrol.TaskStatus;

/**
 *
 */
public class ShoulderPeaksFilterTask extends AbstractTask {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private RawDataFile dataFile;

    private int processedScans = 0, totalScans;

    private int[] scanNumbers;

    private String massListName, suffix;

    private boolean autoRemove;

    private ParameterSet parameters;

    /**
	 * @param dataFile
	 * @param parameters
	 */
    public ShoulderPeaksFilterTask(RawDataFile dataFile, ParameterSet parameters) {
        this.dataFile = dataFile;
        this.parameters = parameters;
        this.massListName = parameters.getParameter(ShoulderPeaksFilterParameters.massList).getValue();
        this.suffix = parameters.getParameter(ShoulderPeaksFilterParameters.suffix).getValue();
        this.autoRemove = parameters.getParameter(ShoulderPeaksFilterParameters.autoRemove).getValue();
    }

    /**
	 * @see net.sf.mzmine.taskcontrol.Task#getTaskDescription()
	 */
    public String getTaskDescription() {
        return "Filtering shoulder peaks in " + dataFile;
    }

    /**
	 * @see net.sf.mzmine.taskcontrol.Task#getFinishedPercentage()
	 */
    public double getFinishedPercentage() {
        if (totalScans == 0) return 0; else return (double) processedScans / totalScans;
    }

    public RawDataFile getDataFile() {
        return dataFile;
    }

    /**
	 * @see Runnable#run()
	 */
    public void run() {
        setStatus(TaskStatus.PROCESSING);
        logger.info("Started mass filter on " + dataFile);
        scanNumbers = dataFile.getScanNumbers();
        totalScans = scanNumbers.length;
        boolean haveMassList = false;
        for (int i = 0; i < totalScans; i++) {
            Scan scan = dataFile.getScan(scanNumbers[i]);
            MassList massList = scan.getMassList(massListName);
            if (massList != null) {
                haveMassList = true;
                break;
            }
        }
        if (!haveMassList) {
            setStatus(TaskStatus.ERROR);
            this.errorMessage = dataFile.getName() + " has no mass list called '" + massListName + "'";
            return;
        }
        for (int i = 0; i < totalScans; i++) {
            if (isCanceled()) return;
            Scan scan = dataFile.getScan(scanNumbers[i]);
            MassList massList = scan.getMassList(massListName);
            if (massList == null) {
                processedScans++;
                continue;
            }
            DataPoint mzPeaks[] = massList.getDataPoints();
            DataPoint newMzPeaks[] = ShoulderPeaksFilter.filterMassValues(mzPeaks, parameters);
            SimpleMassList newMassList = new SimpleMassList(massListName + " " + suffix, scan, newMzPeaks);
            scan.addMassList(newMassList);
            if (autoRemove) scan.removeMassList(massList);
            processedScans++;
        }
        setStatus(TaskStatus.FINISHED);
        logger.info("Finished shoulder peaks filter on " + dataFile);
    }

    public Object[] getCreatedObjects() {
        return null;
    }
}
