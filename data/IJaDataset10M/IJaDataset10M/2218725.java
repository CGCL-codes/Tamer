package binky.reportrunner.ui.actions.dashboard;

import binky.reportrunner.exceptions.SecurityException;
import binky.reportrunner.ui.actions.dashboard.base.BaseDashboardAction;

public class InvokeItem extends BaseDashboardAction {

    private static final long serialVersionUID = 1L;

    private Integer itemId;

    @Override
    public String execute() throws Exception {
        String groupName = super.getDashboardService().getItem(itemId).getGroup().getGroupName();
        if (super.getSessionUser().getGroups().contains(groupName) || super.getSessionUser().getIsAdmin()) {
            super.getDashboardService().invokeDashboardItem(itemId);
            return SUCCESS;
        } else {
            SecurityException se = new SecurityException("Group " + groupName + " not valid for user " + super.getSessionUser().getUserName());
            throw se;
        }
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
