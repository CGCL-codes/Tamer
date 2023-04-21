package edu.rice.cs.drjava.model.repl.newjvm;

/**
 * Type to represent a void result from a call to interpret.
 * 
 * @version $Id: VoidResult.java 1725 2003-09-22 03:52:52Z centgraf $
 */
public class VoidResult implements InterpretResult {

    public <T> T apply(InterpretResultVisitor<T> v) {
        return v.forVoidResult(this);
    }

    public String toString() {
        return "(void)";
    }
}
