package guineu.modules.msms.open;

import guineu.desktop.Desktop;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;
import guineu.taskcontrol.TaskEvent;
import guineu.taskcontrol.TaskStatus;
import guineu.util.dialogs.ExitCode;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

public class OpenFile implements GuineuProcessingModule {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Desktop desktop;

    private OpenMSMSFileParameters parameters;

    public void taskStarted(Task task) {
        logger.info("Running Open File");
    }

    public void taskFinished(Task task) {
        if (task.getStatus() == TaskStatus.FINISHED) {
            logger.info("Finished open file on " + ((OpenFileTask) task).getTaskDescription());
        }
        if (task.getStatus() == TaskStatus.ERROR) {
            String msg = "Error while open file on .. " + ((OpenFileTask) task).getErrorMessage();
            logger.severe(msg);
            desktop.displayErrorMessage(msg);
        }
    }

    public ParameterSet getParameterSet() {
        return parameters;
    }

    public String toString() {
        return "Open File";
    }

    public Task[] runModule() {
        Task tasks[] = new OpenFileTask[1];
        tasks[0] = new OpenFileTask(desktop, parameters);
        GuineuCore.getTaskController().addTasks(tasks);
        return tasks;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            ExitCode exitCode = parameters.showSetupDialog();
            if (exitCode != ExitCode.OK) {
                return;
            }
            runModule();
        } catch (Exception exception) {
        }
    }

    public void statusChanged(TaskEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Task[] runModule(ParameterSet parameters) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GuineuModuleCategory getModuleCategory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setSeparator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
