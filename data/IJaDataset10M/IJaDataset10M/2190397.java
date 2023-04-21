package cartago;

/**
 * Class storing info about an agent quit request
 * 
 * Used by WSP Rule Engine
 * 
 * @author aricci
 *
 */
public class AgentQuitRequestInfo {

    boolean failed;

    String failureMsg;

    AgentId agentId;

    AgentQuitRequestInfo(AgentId agentId) {
        failed = false;
        this.agentId = agentId;
    }

    /**
	 * Force the request to fail
	 * 
	 * @param msg message
	 * @param descr reason/description tuple
	 */
    public void setFailed(String msg) {
        failed = true;
        this.failureMsg = msg;
    }

    boolean isFailed() {
        return failed;
    }

    String getFailureMsg() {
        return failureMsg;
    }

    /**
	 * Get the agent that wants to quit
	 * @return
	 */
    public AgentId getAgentId() {
        return agentId;
    }
}
