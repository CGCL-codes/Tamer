package net.sf.mzmine.modules.rawdatamethods.filtering.datasetfilters;

import java.util.Collection;
import javax.annotation.Nonnull;
import net.sf.mzmine.modules.MZmineModuleCategory;
import net.sf.mzmine.modules.MZmineProcessingModule;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.util.ExitCode;

public class DataSetFiltersModule implements MZmineProcessingModule {

    private static final String MODULE_NAME = "Data set filtering";

    private static final String MODULE_DESCRIPTION = "This module performs filtering algorithms on the whole raw data files.";

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
        Task newTask = new DataSetFilteringTask(parameters);
        tasks.add(newTask);
        return ExitCode.OK;
    }

    public MZmineModuleCategory getModuleCategory() {
        return MZmineModuleCategory.RAWDATAFILTERING;
    }

    @Override
    public Class<? extends ParameterSet> getParameterSetClass() {
        return DataSetFiltersParameters.class;
    }
}
