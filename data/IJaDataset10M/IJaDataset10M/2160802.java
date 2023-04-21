package binky.reportrunner.ui.actions.dashboard.edit;

import binky.reportrunner.data.RunnerDashboardChart;
import binky.reportrunner.ui.actions.dashboard.base.BaseEditDashboardAction;

public class SaveChart extends BaseEditDashboardAction {

    private static final long serialVersionUID = 1L;

    private RunnerDashboardChart item;

    @Override
    public String execute() throws Exception {
        if (this.item.getXaxisColumn().equals("-")) this.item.setXaxisColumn(null);
        if (this.item.getSeriesNameColumn().equals("-")) this.item.setSeriesNameColumn(null);
        if (this.item.getValueColumn().equals("-")) this.item.setValueColumn(null);
        return super.saveItem(this.item);
    }

    public RunnerDashboardChart getItem() {
        return item;
    }

    public void setItem(RunnerDashboardChart item) {
        this.item = item;
    }
}
