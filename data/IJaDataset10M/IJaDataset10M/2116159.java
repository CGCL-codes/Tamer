package guineu.modules.filter.Alignment.centering.mean;

import guineu.data.Dataset;
import guineu.main.GuineuCore;
import guineu.modules.GuineuModuleCategory;
import guineu.modules.GuineuProcessingModule;
import guineu.parameters.ParameterSet;
import guineu.taskcontrol.Task;

public class MeanCenteringModule implements GuineuProcessingModule {

    public static final String MODULE_NAME = "Mean centering";

    public String toString() {
        return MODULE_NAME;
    }

    public ParameterSet getParameterSet() {
        return null;
    }

    public Task[] runModule(ParameterSet parameters) {
        Dataset[] peakLists = GuineuCore.getDesktop().getSelectedDataFiles();
        if ((peakLists == null) || (peakLists.length == 0)) {
            GuineuCore.getDesktop().displayErrorMessage("Please select peak lists for Mean centering");
            return null;
        }
        Task task = new MeanCenteringTask(peakLists);
        GuineuCore.getTaskController().addTask(task);
        return new Task[] { task };
    }

    public GuineuModuleCategory getModuleCategory() {
        return GuineuModuleCategory.ALIGNMENT;
    }

    public String getIcon() {
        return null;
    }

    public boolean setSeparator() {
        return false;
    }
}
