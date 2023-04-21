package net.sf.mzmine.modules.projectmethods.projectsave;

import java.util.Collection;
import javax.annotation.Nonnull;
import net.sf.mzmine.modules.MZmineModuleCategory;
import net.sf.mzmine.modules.MZmineProcessingModule;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.util.ExitCode;

public class ProjectSaveAsModule implements MZmineProcessingModule {

    private static final String MODULE_NAME = "Save project as...";

    private static final String MODULE_DESCRIPTION = "This module saves the current MZmine project.";

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @Override
    public String getDescription() {
        return MODULE_DESCRIPTION;
    }

    @Override
    @Nonnull
    public ExitCode runModule(@Nonnull ParameterSet parameters, @Nonnull Collection<Task> tasks) {
        ProjectSavingTask newTask = new ProjectSavingTask(parameters);
        tasks.add(newTask);
        return ExitCode.OK;
    }

    @Override
    public MZmineModuleCategory getModuleCategory() {
        return MZmineModuleCategory.PROJECTIO;
    }

    @Override
    public Class<? extends ParameterSet> getParameterSetClass() {
        return ProjectSaveAsParameters.class;
    }
}
