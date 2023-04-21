package com.jogamp.gluegen;

import java.util.*;
import java.io.*;

public abstract class FunctionEmitter {

    public static final EmissionModifier STATIC = new EmissionModifier("static");

    private boolean isInterfaceVal;

    private ArrayList<EmissionModifier> modifiers = new ArrayList<EmissionModifier>();

    private CommentEmitter commentEmitter = null;

    private PrintWriter defaultOutput;

    /**
   * Constructs the FunctionEmitter with a CommentEmitter that emits nothing.
   */
    public FunctionEmitter(PrintWriter defaultOutput, boolean isInterface) {
        assert (defaultOutput != null);
        this.defaultOutput = defaultOutput;
        this.isInterfaceVal = isInterface;
    }

    /**
   * Makes this FunctionEmitter a copy of the passed one.
   */
    @SuppressWarnings("unchecked")
    public FunctionEmitter(FunctionEmitter arg) {
        modifiers = (ArrayList<EmissionModifier>) arg.modifiers.clone();
        commentEmitter = arg.commentEmitter;
        defaultOutput = arg.defaultOutput;
        isInterfaceVal = arg.isInterfaceVal;
    }

    public boolean isInterface() {
        return isInterfaceVal;
    }

    public PrintWriter getDefaultOutput() {
        return defaultOutput;
    }

    public void addModifiers(Iterator<EmissionModifier> mi) {
        while (mi.hasNext()) {
            modifiers.add(mi.next());
        }
    }

    public void addModifier(EmissionModifier m) {
        modifiers.add(m);
    }

    public boolean removeModifier(EmissionModifier m) {
        return modifiers.remove(m);
    }

    public void clearModifiers() {
        modifiers.clear();
    }

    public boolean hasModifier(EmissionModifier m) {
        return modifiers.contains(m);
    }

    public Iterator<EmissionModifier> getModifiers() {
        return modifiers.iterator();
    }

    public abstract String getName();

    /**
   * Emit the function to the specified output (instead of the default
   * output).
   */
    public void emit(PrintWriter output) {
        emitDocComment(output);
        emitSignature(output);
        emitBody(output);
    }

    /**
   * Emit the function to the default output (the output that was passed to
   * the constructor)
   */
    public final void emit() {
        emit(getDefaultOutput());
    }

    /** Returns, as a String, whatever {@link #emit} would output. */
    @Override
    public String toString() {
        StringWriter sw = new StringWriter(500);
        PrintWriter w = new PrintWriter(sw);
        emit(w);
        return sw.toString();
    }

    /**
   * Set the object that will emit the comment for this function. If the
   * parameter is null, no comment will be emitted.
   */
    public void setCommentEmitter(CommentEmitter cEmitter) {
        commentEmitter = cEmitter;
    }

    /**
   * Get the comment emitter for this FunctionEmitter. The return value may be
   * null, in which case no comment emitter has been set.
   */
    public CommentEmitter getCommentEmitter() {
        return commentEmitter;
    }

    protected void emitDocComment(PrintWriter writer) {
        if (commentEmitter != null) {
            writer.print(getBaseIndentString());
            writer.print(getCommentStartString());
            commentEmitter.emit(this, writer);
            writer.print(getBaseIndentString());
            writer.println(getCommentEndString());
        }
    }

    protected void emitSignature(PrintWriter writer) {
        writer.print(getBaseIndentString());
        int numEmitted = emitModifiers(writer);
        if (numEmitted > 0) {
            writer.print(" ");
        }
        emitReturnType(writer);
        writer.print(" ");
        emitName(writer);
        writer.print("(");
        emitArguments(writer);
        writer.print(")");
    }

    protected int emitModifiers(PrintWriter writer) {
        PrintWriter w = getDefaultOutput();
        int numEmitted = 0;
        for (Iterator<EmissionModifier> it = getModifiers(); it.hasNext(); ) {
            writer.print(it.next());
            ++numEmitted;
            if (it.hasNext()) {
                writer.print(" ");
            }
        }
        return numEmitted;
    }

    protected String getBaseIndentString() {
        return "";
    }

    protected String getCommentStartString() {
        return "/* ";
    }

    protected String getCommentEndString() {
        return " */";
    }

    protected abstract void emitReturnType(PrintWriter writer);

    protected abstract void emitName(PrintWriter writer);

    /** Returns the number of arguments emitted. */
    protected abstract int emitArguments(PrintWriter writer);

    protected abstract void emitBody(PrintWriter writer);

    public static class EmissionModifier {

        @Override
        public final String toString() {
            return emittedForm;
        }

        private String emittedForm;

        @Override
        public int hashCode() {
            return emittedForm.hashCode();
        }

        @Override
        public boolean equals(Object arg) {
            if (arg == null || (!(arg instanceof EmissionModifier))) {
                return false;
            }
            return emittedForm.equals(((EmissionModifier) arg).emittedForm);
        }

        protected EmissionModifier(String emittedForm) {
            this.emittedForm = emittedForm;
        }
    }
}
