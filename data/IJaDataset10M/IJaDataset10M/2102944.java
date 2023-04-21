package org.jdiagnose.remote.filter;

import org.jdiagnose.RemoteResult;
import org.jdiagnose.remote.RemoteResultMatcher;
import org.jdiagnose.runtime.ResultState;

/**
 * @author jmccrindle
 */
public class DefaultRemoteResultMatcher implements RemoteResultMatcher {

    private String host;

    private String agent;

    private String name;

    private ResultState resultState;

    public boolean accept(RemoteResult result) {
        if (host != null) {
            if (!host.equals(result.getHost())) return false;
        }
        if (agent != null) {
            if (!agent.equals(result.getAgent())) return false;
        }
        if (name != null) {
            if (!name.equals(result.getResultInfo().getName())) return false;
        }
        if (resultState != null) {
            if (resultState != result.getResultInfo().getState()) return false;
        }
        return true;
    }

    /**
     * @param agent The agent to set.
     */
    public void setAgent(String agent) {
        this.agent = agent;
    }

    /**
     * @param host The host to set.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param resultState The resultState to set.
     */
    public void setResultState(String resultState) {
        this.resultState = ResultState.getEnum(resultState);
    }
}
