package de.uniba.wiai.kinf.pw.projects.lillytab.terms;

/**
 *
 *
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 * @param <Term>
 */
public interface IOperatorTerm<Term extends ITerm> extends ITermList<Term> {

    String getOperatorName();
}
