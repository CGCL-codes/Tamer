package gridrm.agents.logger;

import gridrm.core.GridAgent;
import gridrm.core.GridAgentConstants;
import gridrm.core.GridException;
import gridrm.util.ResultLogger;
import jade.domain.FIPAException;
import jade.gui.GuiEvent;
import java.io.IOException;

public class LoggerAgent extends GridAgent {

    public ResultLogger resultLog;

    @Override
    protected void agentSetup() {
        log.info("Setting up agent:");
        try {
            registerAgent(GridAgentConstants.LOGGER_AGENT_TYPE);
            log.info("Worker agent registered in the DF.");
        } catch (FIPAException e) {
            log.severe(e.toString());
            e.printStackTrace();
        }
        try {
            resultLog = new ResultLogger(ResultLogger.getUniqueFileName("Grid-Log.csv"));
        } catch (IOException e) {
            log.severe(e.toString());
            e.printStackTrace();
        }
        addBehaviour(new LoggingBehaviour(this, resultLog));
        sendToLogger(GridAgentConstants.LogTypes.Info, "------Ready to log------");
    }

    @Override
    protected void agentTakeDown() throws GridException {
    }

    @Override
    protected String getPropertiesFileName() {
        return null;
    }

    @Override
    protected void onGuiEvent(GuiEvent ev) {
    }
}
