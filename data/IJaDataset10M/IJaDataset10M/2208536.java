package de.uniba.wiai.kinf.pw.projects.lillytab.terms.swrl;

/**
 *
 * @param <Name>
 * @param <Klass>
 * @param <Role> 
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public interface ISWRLClassAtom<Name extends Comparable<? super Name>, Klass extends Comparable<? super Klass>, Role extends Comparable<? super Role>> extends ISWRLAtomicTerm<Name, Klass, Role> {

    public Klass getKlass();

    public ISWRLIndividual<Name, Klass, Role> getIndividual();
}
