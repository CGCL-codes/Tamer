package net.entropysoft.dashboard.plugin.dashboard.gef.policy;

import net.entropysoft.dashboard.plugin.dashboard.gef.command.DeleteLegendItemCommand;
import net.entropysoft.dashboard.plugin.dashboard.model.AbstractDashboardChart;
import net.entropysoft.dashboard.plugin.dashboard.model.DashboardChartLegendItem;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

public class ChartItemLegendComponentEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        Object parent = getHost().getParent().getModel();
        Object child = getHost().getModel();
        if (parent instanceof AbstractDashboardChart && child instanceof DashboardChartLegendItem) {
            return new DeleteLegendItemCommand((AbstractDashboardChart) parent, (DashboardChartLegendItem) child);
        }
        return super.createDeleteCommand(deleteRequest);
    }
}
