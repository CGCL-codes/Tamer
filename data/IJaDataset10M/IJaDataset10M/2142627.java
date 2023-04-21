package net.tourbook.ui.tourChart.action;

import net.tourbook.Messages;
import net.tourbook.ui.tourChart.TourChart;
import org.eclipse.jface.action.Action;

class ActionCanScrollZoomedChart extends Action {

    private TourChart fTourChart;

    public ActionCanScrollZoomedChart(final TourChart tourChart) {
        super(Messages.Tour_Action_scroll_zoomed_chart, AS_CHECK_BOX);
        fTourChart = tourChart;
    }

    @Override
    public void run() {
        fTourChart.actionCanScrollChart(isChecked());
    }
}
