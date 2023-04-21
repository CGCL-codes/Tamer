package gui.mcast;

import gui.mcast.logic.AgentHandlingFacade;
import javax.swing.table.AbstractTableModel;

public class AgentEditorAvailAgentsTableModel extends AbstractTableModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1473179908801398515L;

    AgentHandlingFacade agentHandler = null;

    public AgentEditorAvailAgentsTableModel() {
        this.agentHandler = AgentHandlingFacade.getInstance();
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return agentHandler.getAgentNames().size();
    }

    public Object getValueAt(int arg0, int arg1) {
        return agentHandler.getAgentNames().toArray()[arg0];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return "agent name";
    }
}
