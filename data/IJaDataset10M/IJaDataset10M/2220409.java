package com.ixora.rms.agents.db2;

import com.ixora.rms.agents.AgentId;
import com.ixora.rms.agents.impl.AbstractAgent;
import com.ixora.rms.exception.InvalidConfiguration;

/**
 * DB2SystemAgent
 */
public class DB2Agent extends AbstractAgent {

    /**
	 * Default constructor
	 * @throws DB2AgentNotSupportedException
	 */
    public DB2Agent(AgentId agentId, Listener listener) throws DB2AgentNotSupportedException {
        super(agentId, listener);
        fRootEntity = new DB2RootEntity(this.fContext);
    }

    /**
	 * Called when configuration has changed
	 * @see com.ixora.rms.agents.impl.AbstractAgent#configCustomChanged()
	 */
    protected void configCustomChanged() throws InvalidConfiguration, Throwable {
        ((DB2RootEntity) fRootEntity).beginSession();
    }
}
