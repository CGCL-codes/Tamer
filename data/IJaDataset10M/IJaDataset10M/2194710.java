package net.sourceforge.sqlexplorer.mysql.tabs;

import net.sourceforge.sqlexplorer.Messages;
import net.sourceforge.sqlexplorer.dbdetail.tab.AbstractSQLTab;

public class ProcessTab extends AbstractSQLTab {

    public ProcessTab() {
    }

    /**
     * Tab label.
     */
    public String getLabelText() {
        return Messages.getString("mysql.DatabaseDetailView.Tab.Processes");
    }

    /**
     * SQL statement to populate tab with.
     */
    public String getSQL() {
        return "show full processlist";
    }

    /**
     * Status message for the process list tab.
     */
    public String getStatusMessage() {
        return Messages.getString("mysql.DatabaseDetailView.Tab.Processes.status") + " " + getNode().getSession().toString();
    }
}
