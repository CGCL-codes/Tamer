package org.xaware.server.engine.controller;

import org.xaware.server.engine.IScriptNode;
import org.xaware.server.engine.IStackFrame;
import org.xaware.server.engine.ITransactionContext;
import org.xaware.server.engine.enums.SubstitutionFailureLevel;
import org.xaware.server.streaming.XmlOutputStreamer;

/**
 * This class is a value object responsible for maintaining
 * the execution state of one level of the CallStack, which
 * represents the execution of one Element in a BizView script.
 * All fields are final (and thus the object is immutable).
 * @author Tim Uttormark
 */
public class StackFrame implements IStackFrame {

    private final IScriptNode node;

    private final ITransactionContext transactionContext;

    private final boolean startsNewTransactionScope;

    private final SubstitutionFailureLevel substitutionFailureLevel;

    private final boolean isInsidePruneElement;

    private final XmlOutputStreamer outputStreamer;

    private final boolean startsNewOutputStream;

    /**
     * Constructor for StackFrame, a value object which maintains
     * the execution state of one Element in a BizView script.
     * All parameters are required since all fields are final.
     * @param node the ScriptNode containing the Element to execute.
     * @param transactionContext the TransactionContext under which
     * the Element is executing.
     * @param startedNewXact a boolean indicating whether a new
     * TransactionContext was started at this ScriptNode (and thus
     * must finish when the StackFrame is popped off the CallStack).
     * @param substFailureLevel the SubstitutionFailureLevel in
     * effect during the execution of this Element.
     * @param isInsidePruneElement a boolean indicating whether
     * this Element is executing within the scope of an
     * xa:visible="prune" declaration (which disables outstreaming).
     * @param outputStreamer the XmlOutputStreamer through which
     * execution results should be streamed out.  A null value
     * indicates that outputStreaming is not in effect.
     * @param startedNewOutputStream a boolean indicating whether
     * a new XmlOutputStreamer was started at this ScriptNode (and
     * thus must be closed when the StackFrame is popped off the
     * CallStack).
     */
    public StackFrame(IScriptNode node, ITransactionContext transactionContext, boolean startedNewXact, SubstitutionFailureLevel substFailureLevel, boolean isInsidePruneElement, XmlOutputStreamer outputStreamer, boolean startedNewOutputStream) {
        this.node = node;
        this.transactionContext = transactionContext;
        this.startsNewTransactionScope = startedNewXact;
        this.substitutionFailureLevel = substFailureLevel;
        this.isInsidePruneElement = isInsidePruneElement;
        this.outputStreamer = outputStreamer;
        this.startsNewOutputStream = startedNewOutputStream;
    }

    /**
     * Returns the IScriptNode associated with this StackFrame.
     * @return the IScriptNode associated with this StackFrame.
     */
    public IScriptNode getNode() {
        return this.node;
    }

    /**
     * Returns the ITransactionContext associated with this StackFrame.
     * @return the ITransactionContext associated with this StackFrame.
     */
    public ITransactionContext getTransactionContext() {
        return this.transactionContext;
    }

    /**
     * Returns a boolean indicating whether the ScriptNode associated
     * with this StackFrame demarcated the beginning of a transaction.
     * @return a boolean indicating whether the ScriptNode associated
     * with this StackFrame demarcated the beginning of a transaction.
     */
    public boolean startsNewTransactionScope() {
        return this.startsNewTransactionScope;
    }

    /**
     * Returns the SubstitutionFailureLevel associated
     * with this StackFrame.
     * @return the SubstitutionFailureLevel associated
     * with this StackFrame.
     */
    public SubstitutionFailureLevel getSubstitutionFailureLevel() {
        return this.substitutionFailureLevel;
    }

    /**
     * Returns a boolean indicating whether the node associated
     * with this StackFrame is tagged with xa:visible="prune"
     * or is a descendant of one that is.
     * @return Returns the boolean value isInsidePruneElement.
     */
    public boolean isInsidePruneElement() {
        return this.isInsidePruneElement;
    }

    /**
     * Returns the XmlOutputStreamer in effect for during
     * this part of BizView execution, or null if none.
     * @return Returns the XmlOutputStreamer in effect.
     */
    public XmlOutputStreamer getXmlOutputStreamer() {
        return this.outputStreamer;
    }

    /**
     * Returns a boolean indicating whether the ScriptNode associated
     * with this StackFrame opened a new OutputStream.
     * @return a boolean indicating whether the ScriptNode associated
     * with this StackFrame opened a new OutputStream.
     */
    public boolean startsNewOutputStream() {
        return this.startsNewOutputStream;
    }

    /**
     * Returns the name of the BizView file associated with the
     * context of the node associated with this StackFrame.
     * @return a String containing the name of the associated
     * BizView file.
     */
    public String getBizViewName() {
        return this.node.getContext().getBizViewName();
    }

    @Override
    public String toString() {
        String fullContextClass = node.getContext().getClass().getName();
        String contextClass = fullContextClass.substring(fullContextClass.lastIndexOf('.') + 1);
        String fullScriptNodeClass = node.getClass().getName();
        String scriptNodeClass = fullScriptNodeClass.substring(fullScriptNodeClass.lastIndexOf('.') + 1);
        StringBuffer buf = new StringBuffer(256);
        buf.append("ScriptNode on context:     ").append(scriptNodeClass).append(" on ").append(contextClass).append("\n").append(node).append("\n");
        buf.append("TransactionContext:        ").append(transactionContext).append("\n");
        buf.append("startsNewTransactionScope: ").append(startsNewTransactionScope).append("\n");
        buf.append("substitutionFailureLevel:  ").append(substitutionFailureLevel).append("\n");
        buf.append("isInsidePruneElement:      ").append(isInsidePruneElement).append("\n");
        buf.append("XmlOutputStreamer:         ").append(outputStreamer).append("\n");
        buf.append("startsNewOutputStream:     ").append(startsNewOutputStream);
        return buf.toString();
    }
}
