package de.fhg.igd.semoa.server;

import de.fhg.igd.util.*;

/**
 * Supports setting and getting of <code>Ticket</code>
 * instances which are used in migrating agents.
 *
 * @author Volker Roth
 * @version "$Id: MobilityContextImp.java 1563 2005-03-24 17:47:32Z jpeters $"
 */
public class MobilityContextImp extends Object implements MobilityContext {

    /**
     * The <code>AgentContext</code> that backs this instance.
     */
    private AgentContext context_;

    /**
     * The <code>Ticket</code> pointing to the next hop
     * of the agent whose context this is.
     */
    private Ticket ticket_;

    /**
     * Creates an instance that is backed by the given
     * <code>AgentContext</code>.
     *
     * @param context The <code>AgentContext</code> to which
     *   this instance refers.
     */
    public MobilityContextImp(AgentContext context) {
        if (context == null) {
            throw new NullPointerException("AgentContext");
        }
        context_ = context;
    }

    /**
     * Returns the <code>Ticket</code> that points to the most
     * recent sender of the agent to which this
     * <code>MobilityContext</code> belongs.
     *
     * @return The <code>Ticket</code> pointing back to the most
     *   recent sender of the agent, or <code>null</code> if the
     *   agent was launched locally.
     */
    public Ticket getReverseTicket() {
        try {
            return (Ticket) context_.get(FieldType.REVERSE_TICKET);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * This method returns the <code>Ticket</code> that was set
     * by the agent as a reference to its next destination, or
     * <code>null</code> if the agent did not set any <code>
     * Ticket</code>. This implementation always returns <code>
     * null</code>.
     *
     * @return <code>null</code>.
     */
    public Ticket getTicket() {
        return ticket_;
    }

    /**
     * Sets the ticket to the agent's next hop, or <code>null
     * </code> (default) if the agent wants to terminate on
     * this machine.
     *
     * @param ticket The ticket pointing to the agent's next
     *   hop, or <code>null</code> if the agent shall be
     *   ditched after terminating.
     */
    public void setTicket(Ticket ticket) {
        ticket_ = ticket;
    }

    /**
     * Returns the {@link AgentCard AgentCard} of the agent.
     * The <code>AgentCard</code> is the unique representation
     * of the agent, in particular of the agent's globally
     * unique name. Agents can be contacted and identified
     * using their card.
     *
     * @return The <code>AgentCard</code>.
     */
    public AgentCard getCard() {
        try {
            return (AgentCard) context_.get(FieldType.CARD);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Returns the {@link Resource Resource} in which all the
     * agent's data is stored. All data contained in this
     * resource is guaranteed to be transported with the agent.
     * Numerous security filters work in conjunction to assure
     * the security of this data. Among other things, the agent's
     * snapshot is stored in the resource.<p>
     *
     * The agent resource can be any type of storage, including
     * persistent storage on the local file system. It can also
     * be a view on some in memory storage such as a hashtable,
     * it can represent a database table that stores the data
     * of the agent, or it can be a mixture of these things.
     *
     * @return The <code>Resource</code> that is used to keep
     *   the data of an agent.
     */
    public Resource getResource() {
        try {
            return (Resource) context_.get(FieldType.RESOURCE);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
}
