package server.network.executions;

import server.common.DataBase;
import server.common.LoggingManager;
import server.common.Network;
import server.execution.AbstractExecution;

public class ImportExecution extends AbstractExecution {

    private String m_importedNet = null;

    private String m_filename = null;

    private int m_netID = -1;

    public ImportExecution(Network network, String filename, String input) {
        this.m_network = network;
        this.m_filename = filename;
        this.m_importedNet = input;
    }

    public void run() {
        boolean success = m_network.importNetwork(this, m_filename, m_importedNet);
        m_netID = DataBase.putNetwork(m_network);
        if (!success) {
            LoggingManager.getInstance().writeSystem(m_methodName + " hasn't completed successfully, check logs.", "ImportExecution", m_methodName, null);
            reportSuccess(AbstractExecution.PHASE_FAILURE, AbstractExecution.PHASE_COMPLETE);
        } else reportSuccess(AbstractExecution.PHASE_SUCCESS, AbstractExecution.PHASE_COMPLETE);
    }

    public Object getResult() {
        return m_netID;
    }
}
