package com.google.gwt.dev.asm.tree;

import com.google.gwt.dev.asm.MethodVisitor;

/**
 * A node that represents a try catch block.
 * 
 * @author Eric Bruneton
 */
public class TryCatchBlockNode {

    /**
     * Beginning of the exception handler's scope (inclusive).
     */
    public LabelNode start;

    /**
     * End of the exception handler's scope (exclusive).
     */
    public LabelNode end;

    /**
     * Beginning of the exception handler's code.
     */
    public LabelNode handler;

    /**
     * Internal name of the type of exceptions handled by the handler. May be
     * <tt>null</tt> to catch any exceptions (for "finally" blocks).
     */
    public String type;

    /**
     * Constructs a new {@link TryCatchBlockNode}.
     * 
     * @param start beginning of the exception handler's scope (inclusive).
     * @param end end of the exception handler's scope (exclusive).
     * @param handler beginning of the exception handler's code.
     * @param type internal name of the type of exceptions handled by the
     *        handler, or <tt>null</tt> to catch any exceptions (for "finally"
     *        blocks).
     */
    public TryCatchBlockNode(final LabelNode start, final LabelNode end, final LabelNode handler, final String type) {
        this.start = start;
        this.end = end;
        this.handler = handler;
        this.type = type;
    }

    /**
     * Makes the given visitor visit this try catch block.
     * 
     * @param mv a method visitor.
     */
    public void accept(final MethodVisitor mv) {
        mv.visitTryCatchBlock(start.getLabel(), end.getLabel(), handler == null ? null : handler.getLabel(), type);
    }
}
