package de.uniba.wiai.kinf.pw.projects.lillytab.terms.swrl;

import de.uniba.wiai.kinf.pw.projects.lillytab.terms.ITerm;
import java.util.Collection;

/**
 *
 * @param <Name> 
 * @param <Klass>
 * @param <Role>
 * @author Peter Wullinger <peter.wullinger@uni-bamberg.de>
 */
public interface ISWRLRule<Name extends Comparable<? super Name>, Klass extends Comparable<? super Klass>, Role extends Comparable<? super Role>> extends ITerm {

    ISWRLTerm<Name, Klass, Role> getHead();

    ISWRLTerm<Name, Klass, Role> getBody();

    Collection<? extends ISWRLVariable<Name, Klass, Role>> getVariables();
}
