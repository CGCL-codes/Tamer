package guineu.modules.dataanalysis.qvalue;

import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.taskcontrol.Task;
import guineu.data.Dataset;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;

/**
 *
 * @author scsandra
 */
public class QvalueModule implements GuineuProcessingModule {

    public static final String MODULE_NAME = "Calculate Q-value (R)";

    public ParameterSet getParameterSet() {
        return null;
    }

    public String toString() {
        return MODULE_NAME;
    }

    public Task[] runModule(ParameterSet parameters) {
        Dataset[] DataFiles = GuineuCore.getDesktop().getSelectedDataFiles();
        Task tasks[] = new QvalueTask[1];
        tasks[0] = new QvalueTask(DataFiles[0]);
        GuineuCore.getTaskController().addTasks(tasks);
        return tasks;
    }

    public GuineuModuleCategory getModuleCategory() {
        return GuineuModuleCategory.DATAANALYSIS;
    }

    public String getIcon() {
        return "icons/FDR.png";
    }

    public boolean setSeparator() {
        return false;
    }
}
