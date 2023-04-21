package com.redtwitch.craps.agents;

/**
 * Factory that creates StreakAgents
 * @author  <a href="mailto:glenn@redtwitch.com">Glenn Wilson</a>
 *
 * @deprecated This class is no longer necessary
 * $Id: StreakAgentFactory.java,v 1.3 2005/08/26 17:46:58 wilsong123 Exp $
 */
public class StreakAgentFactory implements AgentFactory {

    /** Creates a new instance of ComeConservativeAgentFactory */
    public StreakAgentFactory() {
    }

    public Agent getAgentInstance(int balance, int units) {
        return new StreakAgent(balance, units);
    }

    public Agent getAgentInstance() {
        return new StreakAgent();
    }
}
