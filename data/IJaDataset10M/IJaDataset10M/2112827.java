package net.sourceforge.jenesis4java;

import java.util.List;

/**
 * <code>Statement</code> subinterface for the <code>switch</code> construct.
 */
public interface Switch extends ConditionalStatement {

    /**
     * Gets the set of cases as an list of <code>Case</code>.
     */
    List<Case> getCases();

    /**
     * Gets the default case.
     */
    Default getDefault();

    /**
     * Adds a new <code>Case</code> to the set of cases.
     */
    Case newCase(Expression constant);
}
