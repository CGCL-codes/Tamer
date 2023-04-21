package de.iritgo.aktera.journal.dashboard;

import de.iritgo.aktera.dashboard.DashboardGroup;
import de.iritgo.aktera.dashboard.DashboardItem;
import de.iritgo.aktera.dashboard.GroupVisitor;
import de.iritgo.aktera.dashboard.groups.AbstractGroupImpl;

public class JournalGroupImpl extends AbstractGroupImpl implements JournalGroup {

    private int numberOfColumns;

    private String primaryType;

    private Integer ownerId;

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public String getDescription() {
        return "desctiption";
    }

    public void update() {
    }

    public void generate(GroupVisitor visitor) {
        customVisitor.setParentVisitor(visitor);
        customVisitor.generate((DashboardGroup) this);
        for (DashboardItem dashboardItem : dashboardItems) {
            if (dashboardItem instanceof JournalItem) {
                ((JournalItem) dashboardItem).init(primaryType, ownerId);
            }
            dashboardItem.generate(customVisitor);
        }
    }
}
