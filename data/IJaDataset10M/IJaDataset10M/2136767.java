package de.uniba.wiai.kinf.pw.projects.lillytab.terms.swrl;

import de.uniba.wiai.kinf.pw.projects.lillytab.terms.IAtom;

/**
 *
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public interface ISWRLAtomicTerm<Name extends Comparable<? super Name>, Klass extends Comparable<? super Klass>, Role extends Comparable<? super Role>> extends ISWRLTerm<Name, Klass, Role>, IAtom {
}
