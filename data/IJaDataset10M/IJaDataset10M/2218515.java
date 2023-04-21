package com.ixora.rms.agents.oracle;

import com.ixora.rms.agents.AgentId;
import com.ixora.rms.agents.impl.AbstractAgent;

/**
 * OracleAgent
 * Collects Oracle database data using providers with SQL statements.
 */
public class OracleAgent extends AbstractAgent {

    /**
	 * Constructor
	 */
    public OracleAgent(AgentId agentId, Listener listener) {
        super(agentId, listener);
    }
}
