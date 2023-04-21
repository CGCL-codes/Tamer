package org.rakiura.micro;

/**
 * Represents a system owner. This is a top-level agent which 
 * is used as an owner by the SystemAgentLoader, i.e. all agents
 * created by the SystemAgentLoader have this SystemOwner instance 
 * as the owner. 
 * 
 *<br><br>
 * SystemOwner.java<br>
 * Created: Thu Aug 16 15:42:34 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.1 $ $Date: 2006/09/20 09:34:10 $
 */
public class SystemOwner extends AnonymousAgent {

    private Group group = new DefaultGroup(new AgentLoader(this));

    private static final Agent instance = new SystemOwner();

    /**
   * Returns the instance of System Owner agent.
   */
    public static Agent getInstance() {
        return instance;
    }

    /**
   * Returns the group managed by this agent. 
   */
    public Group getGroup() {
        return this.group;
    }
}
