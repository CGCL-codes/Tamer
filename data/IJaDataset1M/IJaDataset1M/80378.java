package guineu.modules.file.saveOtherFile;

import guineu.data.Dataset;
import guineu.database.intro.InDataBase;
import guineu.database.intro.impl.InOracle;
import guineu.parameters.ParameterSet;
import guineu.parameters.SimpleParameterSet;
import guineu.taskcontrol.AbstractTask;
import guineu.taskcontrol.TaskStatus;

/**
 *
 * @author scsandra
 */
public class SaveOtherFileTask extends AbstractTask {

    private Dataset dataset;

    private String path;

    private InDataBase db;

    private ParameterSet parameters;

    public SaveOtherFileTask(Dataset dataset, ParameterSet parameters, String path) {
        this.dataset = dataset;
        this.path = path;
        this.parameters = parameters;
        db = new InOracle();
    }

    public String getTaskDescription() {
        return "Saving Dataset... ";
    }

    public double getFinishedPercentage() {
        return db.getProgress();
    }

    public void cancel() {
        setStatus(TaskStatus.CANCELED);
    }

    public void run() {
        try {
            setStatus(TaskStatus.PROCESSING);
            if (parameters.getParameter(SaveOtherParameters.type).getValue().matches(".*Excel.*")) {
                db.WriteExcelFile(dataset, path, (SimpleParameterSet) parameters);
            } else {
                db.WriteCommaSeparatedFile(dataset, path, (SimpleParameterSet) parameters);
            }
            setStatus(TaskStatus.FINISHED);
        } catch (Exception e) {
            setStatus(TaskStatus.ERROR);
        }
    }
}
