package guineu.modules.file.openMassLynxFiles;

import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;

/**
 *
 * @author scsandra
 */
public class OpenMassLynxFileModule implements GuineuProcessingModule {

    public static final String MODULE_NAME = "Mass Lynx Files";

    private massLynxParameters parameters = new massLynxParameters();

    public ParameterSet getParameterSet() {
        return this.parameters;
    }

    public String toString() {
        return MODULE_NAME;
    }

    public Task[] runModule(ParameterSet parameters) {
        String FilePath = parameters.getParameter(massLynxParameters.fileName).getName();
        if (FilePath != null) {
            Task tasks[] = new OpenFileTask[1];
            tasks[0] = new OpenFileTask(FilePath);
            GuineuCore.getTaskController().addTasks(tasks);
            return tasks;
        } else {
            return null;
        }
    }

    public GuineuModuleCategory getModuleCategory() {
        return GuineuModuleCategory.FILE;
    }

    public String getIcon() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setSeparator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
