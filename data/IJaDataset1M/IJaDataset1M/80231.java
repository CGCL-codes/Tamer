package org.rakiura.micro;

/**
 * Represents an abstract view over the internal structure of the
 * Agent.  This particular view of the agents is to be used internally
 * by the micro-kernel implementation only. 
 * For any publicly available contract see {@link Agent Agent}.
 * 
 *<br><br>
 * AbstractAgent.java<br>
 * Created: Fri Mar 30 12:11:40 2001<br>
 *
 * @author Mariusz Nowostawski   (mariusz@rakiura.org)
 * @version $Revision: 1.1 $ $Date: 2006/09/20 09:34:09 $
 */
abstract class AbstractAgent extends Agent {

    public abstract boolean allowed(Role r);

    public abstract void prohibit(Role r);

    public abstract void permit(Role r);

    public abstract void suspend();

    public abstract void resume();

    public abstract void die();

    public abstract AgentState packState();

    public abstract void unpackState(AgentState state);
}
