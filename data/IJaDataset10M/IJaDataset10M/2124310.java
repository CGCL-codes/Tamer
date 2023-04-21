package org.rakiura.micro;

/**
 * Role representing a Group. This role is used to locate Roles and
 * Agents based on the appropriate types of Roles and Goals.
 * 
 *<br><br>
 * Group.java<br>
 * Created: Wed Mar 14 12:07:01 2001<br>
 *
 * @author <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version $Revision: 1.1 $ $Date: 2006/09/20 09:34:10 $
 */
public interface Group extends Role {

    /**
   * Lookup method for locating roles. This method will prepare a list
   * of all valid roles implementing a specific role type. Note, that
   * if an actual role implements a subrole of a specified role, it
   * will be returned as well via the default group implementation
   * mechanism. However, the developer can implement this behaviour
   * otherwise. 
   *@param aRoleType specification of the requested role
   *@return an array of all valid roles for a given specification */
    Role[] findRoles(final Class aRoleType);

    /**
   * Lookup method for locating agents. This method will prepare a list
   * of all valid agents which can achieve a specified goal.
   *@param aGoalType specification of the requested goal
   *@return an array of all valid agents for a given goal specification */
    Agent[] findAgents(final Class aGoalType);

    /**
   *Registers a given agent with this group.
   *@param anAgent agent to be registered
   */
    void register(final Agent anAgent);

    /**
   * Deregisters a given agent from this group.
   *@param anAgent agent to be deregistered
   */
    void deregister(final Agent anAgent);

    /** 
   * Returns all the agents registered within this group. 
   *@return an array of all registered agents.
   */
    Agent[] getAgents();

    /**
   * Returns an agent loader for this group.
   *@return an AgentLoader for this group.
   */
    AgentLoader getAgentLoader();
}
