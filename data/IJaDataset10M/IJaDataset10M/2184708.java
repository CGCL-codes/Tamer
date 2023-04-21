package com.wozgonon.math;

import com.wozgonon.docustrate.Feature;
import com.wozgonon.docustrate.WorkInProgress;

/**
 * To provide a consistent framework for "run time testing" by allowing classes to implement integrity checks that can be invoked at test time or at run time. 
 * 
 * <li>Runs a test rather than specifies an algebraic expression so not quite the same (as in Hoare style invariants).
 * <li>Can be called directly or indirectly from JUnit tests.
 * <ul>
 * <li>Philosophically JUnit tends to assume testing results is done outside the main body of code in the JUnit tests:
 * <li>This means the expression of invariants is decoupled from the class, whereas to my mind a class invariant is intrinsic to the class.
 * <li>One can run tests at run time to check the integrity of a real system on the real production environment.
 * </ul>
 * 
 * @see http://en.wikipedia.org/wiki/Class_invariant
 * @see http://en.wikipedia.org/wiki/Design_by_contract
 * @see http://en.wikipedia.org/wiki/Invariant_(computer_science)
 * 
 * TODO Investigate http://en.wikipedia.org/wiki/JML
 */
@Feature(value = "Invariants", description = "Consistent framework for classes to implement 'invariants' to check intergity at run time.")
@WorkInProgress("Consider how this can better fit in with JUnit framework")
public interface IHasInvariant {

    /**
	 * Use assert statements to ensure the integrity of the class.
	 */
    public void assertClassInvariant();
}
