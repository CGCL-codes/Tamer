package org.libreplan.web.planner.allocation;

import java.util.Arrays;
import java.util.List;
import org.libreplan.business.orders.entities.AggregatedHoursGroup;
import org.libreplan.web.common.Util;
import org.libreplan.web.planner.allocation.ResourceAllocationController.HoursRendererColumn;
import org.zkoss.ganttz.timetracker.ICellForDetailItemRenderer;
import org.zkoss.ganttz.timetracker.OnColumnsRowRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Footer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.SimpleListModel;

/**
 *
 * @author Diego Pino Garcia<dpino@igalia.com>
 *
 */
public class TaskInformation extends HtmlMacroComponent {

    private Grid gridTaskRows;

    private Button btnRecommendedAllocation;

    private Footer totalEstimatedHours;

    private ITotalHoursCalculationListener totalHoursCalculation;

    @Override
    public void afterCompose() {
        super.afterCompose();
        this.setVariable("taskInformationController", this, true);
        btnRecommendedAllocation = (Button) getFellowIfAny("btnRecommendedAllocation");
        gridTaskRows = (Grid) getFellowIfAny("gridTaskRows");
        totalEstimatedHours = (Footer) getFellowIfAny("totalEstimatedHours");
    }

    public Integer getTotalHours() {
        if (totalHoursCalculation != null) {
            return totalHoursCalculation.getTotalHours();
        }
        return Integer.valueOf(0);
    }

    public void initializeGridTaskRows(List<AggregatedHoursGroup> rows) {
        gridTaskRows.setModel(new SimpleListModel(rows));
        gridTaskRows.setRowRenderer(newTaskRowsRenderer());
    }

    public Button getBtnRecommendedAllocation() {
        return btnRecommendedAllocation;
    }

    public void showRecomendedAllocationButton() {
        btnRecommendedAllocation.setVisible(true);
    }

    public void hideRecomendedAllocationButton() {
        btnRecommendedAllocation.setVisible(false);
    }

    private RowRenderer newTaskRowsRenderer() {
        return OnColumnsRowRenderer.create(hoursCellRenderer, Arrays.asList(HoursRendererColumn.values()));
    }

    private static final ICellForDetailItemRenderer<HoursRendererColumn, AggregatedHoursGroup> hoursCellRenderer = new ICellForDetailItemRenderer<HoursRendererColumn, AggregatedHoursGroup>() {

        @Override
        public Component cellFor(HoursRendererColumn column, AggregatedHoursGroup data) {
            return column.cell(column, data);
        }
    };

    /**
     *
     * Listener for calculating total number of hours
     *
     * @author Diego Pino Garcia <dpino@igalia.com>
     *
     */
    public interface ITotalHoursCalculationListener {

        public Integer getTotalHours();
    }

    public void onCalculateTotalHours(ITotalHoursCalculationListener totalHoursCalculation) {
        this.totalHoursCalculation = totalHoursCalculation;
    }

    public void refreshTotalHours() {
        Util.reloadBindings(totalEstimatedHours);
    }
}
