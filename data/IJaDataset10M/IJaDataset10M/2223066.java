package org.nomadpim.module.timetracking.recurrent_activity;

import java.util.List;
import org.nomadpim.core.ui.component.Section;
import org.nomadpim.core.ui.component.SectionBuilder;
import org.nomadpim.core.ui.components.TimeIntervalListComponentAdapter;
import org.nomadpim.core.ui.editor.IEntityEditorConfiguration;
import org.nomadpim.module.timetracking.ITimetrackingModuleConstants;

public class RecurrentActivityEditorConfigurationSpentTimes implements IEntityEditorConfiguration {

    public List<Section> createPropertyFieldConfigurations() {
        SectionBuilder builder = new SectionBuilder(ITimetrackingModuleConstants.RESOURCES, RecurrentActivity.TYPE_NAME);
        builder.addList(RecurrentActivity.SPENT_TIMES, new TimeIntervalListComponentAdapter());
        return builder.getSections();
    }
}
