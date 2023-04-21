package org.tzi.ugt.model;

import java.util.Set;
import org.tzi.ugt.main.Exception;
import org.tzi.ugt.model.uml.AssociationRole;
import org.tzi.ugt.model.uml.Object;
import org.tzi.ugt.model.uml.Operation;

/**
 * Represents an message in the UGT data model.
 * 
 * @author lschaps
 */
public class Message extends Activator {

    private Object m_Sender;

    private Object m_Receiver;

    private Activator m_Activator;

    private String m_SeqNo;

    private String m_ConditionClause;

    private String m_Assign;

    private Process m_Process;

    private Set m_Predecessors;

    private AssociationRole m_AssociationRole;

    /**
	 * Constructs the message for UGT data model.
	 * 
	 * @param in_Sender
	 *            The sender of the message.
	 * @param in_Receiver
	 *            The receiver of the message.
	 * @param in_SeqNo
	 *            sequence number of the message.
	 * @param in_CondClause
	 *            The condition clause of the message, if it has one.
	 * @param in_Assign
	 *            Flag, wheter the message assign a value.
	 * @param in_Process
	 *            The operation for the message.
	 */
    Message(Object in_Sender, Object in_Receiver, String in_SeqNo, String in_CondClause, String in_Assign, Process in_Process, AssociationRole in_AssociationRole) {
        super();
        m_Sender = in_Sender;
        m_Receiver = in_Receiver;
        m_ConditionClause = in_CondClause;
        m_Assign = in_Assign;
        m_SeqNo = in_SeqNo;
        m_Process = in_Process;
        m_AssociationRole = in_AssociationRole;
        m_Activator = null;
    }

    /**
	 * Returns the sequence number of the message.
	 * 
	 * @return The sequence number of the message.
	 */
    String getSeqNo() {
        return m_SeqNo;
    }

    /**
	 * Returns the last fragment of the sequence number of the message. The
	 * fragments are divided by dots.
	 * 
	 * @return The last fragment of the sequence number.
	 */
    public String getLastSeqNo() {
        int i = m_SeqNo.lastIndexOf('.');
        if (-1 == i) {
            return m_SeqNo;
        } else {
            return m_SeqNo.substring(i + 1);
        }
    }

    /**
	 * Sets the activator for the message
	 * 
	 * @param in_Activator
	 *            The activator for the message.
	 * 
	 * @throws Exception
	 *             Thrown when two different activator are given.
	 */
    void setActivator(Activator in_Activator) throws Exception {
        if (null == m_Activator) {
            m_Activator = in_Activator;
            in_Activator.addActivatorFor(this);
        } else if (m_Activator != in_Activator) {
            throw new Exception("Two different activators.");
        }
    }

    /**
	 * Adds a predecessor to the message.
	 * 
	 * @param in_Predecessors
	 *            The predecessor for the message.
	 */
    void setPredecessors(Set in_Predecessors) {
        m_Predecessors = in_Predecessors;
    }

    /**
	 * Returns the activator of the message.
	 * 
	 * @return The activator of the message.
	 */
    Activator getActivator() {
        return m_Activator;
    }

    String getAssign() {
        return m_Assign;
    }

    /**
	 * Returns the OCL conditions clause of the message.
	 * 
	 * @return The OCL condition clause of the message.
	 */
    String getConditionClause() {
        return m_ConditionClause;
    }

    /**
	 * Returns the expression of the message.
	 * 
	 * @return The expression of the message.
	 */
    Process getProcess() {
        return m_Process;
    }

    /**
	 * Returns the predecessors of the message.
	 * 
	 * @return A list of predecessors.
	 */
    Set getPredecessors() {
        return m_Predecessors;
    }

    /**
	 * Returns the receiver of the message.
	 * 
	 * @return The receiver of the message.
	 */
    Object getReceiver() {
        return m_Receiver;
    }

    /**
	 * Returns the sender of the message.
	 * 
	 * @return The sender object.
	 */
    Object getSender() {
        return m_Sender;
    }

    /**
	 * Returns the association role.
	 * 
	 * @return The association role.
	 */
    public AssociationRole getAssociationRole() {
        return m_AssociationRole;
    }

    public Operation getOperation() {
        return m_Process.getOperation();
    }

    public String name() {
        return getSeqNo();
    }
}
