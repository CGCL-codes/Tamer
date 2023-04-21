package saapl;

import ail.others.MAS;
import ail.others.AILexception;
import ail.semantics.AILAgent;

/**
 * An SAAPL Agent.
 * 
 * @author louiseadennis
 *
 */
public class SAAPLAgent extends AILAgent {

    /**
	 * Construct a Gwendolen agent from an architecture and a name.
	 * 
	 * @param arch the Agent Architecture.
	 * @param name te name of the agent.
	 */
    public SAAPLAgent(MAS mas, String name) throws AILexception {
        super(mas, name);
        try {
            ((SAAPLEnv) fEnv).addPerceptListener(this);
        } catch (Exception e) {
            throw new AILexception("AIL: error creating the agent architecture! - " + e);
        }
        setReasoningCycle(new SAAPLRC());
    }

    /**
     * Getter method for the environment (assumed to be an SAAPL Environment).
     * @return
     */
    public SAAPLEnv getSAAPLEnv() {
        return (SAAPLEnv) fEnv;
    }
}
