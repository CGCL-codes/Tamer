package com.ibm.realtime.flexotask.validation;

import com.ibm.realtime.analysis.MethodWrapper;

/**
 * This class represents a type rule violation relating
 * to a statement of a method body in a class.
 */
public class StatementViolation extends MethodViolation {

    /** The line number in the source code of
   * the violating statement */
    private int lineNumber;

    /**
   * Constructor
   * @param severity the severity.
   * @param mw the wrapper of the method in which the violation occurred.
   * @param lineNumber the line number in which the violation occurred.
   * @param msg the message describing the violation.
   */
    public StatementViolation(Severity severity, MethodWrapper mw, int lineNumber, String msg) {
        super(severity, mw, msg);
        this.lineNumber = lineNumber;
    }

    /**
   * Returns the line number in the source code of the
   * statement violating some type rule.
   * @return the line number in which the violation occurred.
   */
    public int getLineNumber() {
        return lineNumber;
    }

    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof StatementViolation)) {
            return false;
        }
        return lineNumber == ((StatementViolation) obj).lineNumber;
    }
}
