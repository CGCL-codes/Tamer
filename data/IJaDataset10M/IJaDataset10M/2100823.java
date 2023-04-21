package net.sf.mzmine.modules.peaklistmethods.io.xmlexport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.logging.Logger;
import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.modules.projectmethods.projectsave.PeakListSaveHandler;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.AbstractTask;
import net.sf.mzmine.taskcontrol.TaskStatus;
import de.schlichtherle.util.zip.ZipEntry;
import de.schlichtherle.util.zip.ZipOutputStream;

public class XMLExportTask extends AbstractTask {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private PeakList peakList;

    private PeakListSaveHandler peakListSaveHandler;

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private File fileName;

    private boolean compression;

    /**
	 * @param peakList
	 * @param parameters
	 */
    public XMLExportTask(ParameterSet parameters) {
        fileName = parameters.getParameter(XMLExportParameters.filename).getValue();
        compression = parameters.getParameter(XMLExportParameters.compression).getValue();
        this.peakList = parameters.getParameter(XMLExportParameters.peakList).getValue()[0];
    }

    /**
	 * @see net.sf.mzmine.taskcontrol.Task#getFinishedPercentage()
	 */
    public double getFinishedPercentage() {
        if (peakListSaveHandler == null) return 0;
        return peakListSaveHandler.getProgress();
    }

    public void cancel() {
        super.cancel();
        if (peakListSaveHandler != null) peakListSaveHandler.cancel();
    }

    /**
	 * @see net.sf.mzmine.taskcontrol.Task#getTaskDescription()
	 */
    public String getTaskDescription() {
        return "Saving peak list " + peakList + " to " + fileName;
    }

    /**
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        try {
            setStatus(TaskStatus.PROCESSING);
            logger.info("Started saving peak list " + peakList.getName());
            FileOutputStream fos = new FileOutputStream(fileName);
            OutputStream finalStream = fos;
            if (compression) {
                ZipOutputStream zos = new ZipOutputStream(fos);
                zos.setLevel(9);
                zos.putNextEntry(new ZipEntry(fileName.getName()));
                finalStream = zos;
            }
            Hashtable<RawDataFile, String> dataFilesIDMap = new Hashtable<RawDataFile, String>();
            for (RawDataFile file : peakList.getRawDataFiles()) {
                dataFilesIDMap.put(file, file.getName());
            }
            peakListSaveHandler = new PeakListSaveHandler(finalStream, dataFilesIDMap);
            peakListSaveHandler.savePeakList(peakList);
        } catch (Exception e) {
            if (getStatus() == TaskStatus.PROCESSING) {
                setStatus(TaskStatus.ERROR);
            }
            errorMessage = e.toString();
            e.printStackTrace();
            return;
        }
        logger.info("Finished saving " + peakList.getName());
        setStatus(TaskStatus.FINISHED);
    }

    public Object[] getCreatedObjects() {
        return null;
    }
}
