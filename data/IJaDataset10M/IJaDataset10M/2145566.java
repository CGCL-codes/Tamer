package com.ivis.xprocess.ui.editors.dynamic.elements.specific.dates;

import java.util.Map;
import com.ivis.xprocess.core.Xtask;
import com.ivis.xprocess.ui.editors.dynamic.elements.specific.dates.graphical.DateLabelDescriptor;
import com.ivis.xprocess.ui.editors.dynamic.elements.specific.dates.graphical.DatesGraphicalEditor;
import com.ivis.xprocess.ui.editors.dynamic.elements.specific.dates.graphical.DatesProvider;
import com.ivis.xprocess.ui.util.dates.DateType;
import com.ivis.xprocess.util.Day;

/**
 * The xprocess widget for the date graphical editor on Projects.
 *
 */
public class ProjectDatesGraphicalEditor extends DatesGraphicalEditor {

    @Override
    public String getContributionToTitle() {
        myDatesProvider.setEditorContext(getEditorContext());
        String forecastDateLabel = "Forecast End";
        String targetEndLabel = myDatesProvider.getLabelText(DateType.TargetEnd);
        String forecastEnd = DateType.toString(myDatesProvider.getValue(DateType.ForecastEnd));
        String targetEnd = DateType.toString(myDatesProvider.getValue(DateType.TargetEnd));
        return forecastDateLabel + ": " + forecastEnd + "; " + targetEndLabel + ": " + targetEnd;
    }

    @Override
    protected DatesProvider createDatesProvider() {
        return new ProjectDatesProvider(ChildTaskDatesGraphicalEditor.LABEL_DESCRIPTORS);
    }

    private final class ProjectDatesProvider extends DatesProvider {

        private ProjectDatesProvider(Map<DateType, DateLabelDescriptor> descriptors) {
            super(descriptors);
        }

        @Override
        public int getBookedTime(Day day) {
            return getTask().getBookedTime(day);
        }

        @Override
        public boolean showForecastBarsTails() {
            return !getTask().isClosed();
        }

        @Override
        public Day getLatestImportantDate() {
            return getTask().getLatestAssignmentDate();
        }

        private Xtask getTask() {
            return (Xtask) getEditorContext().getElementWrapper().getElement();
        }
    }
}
