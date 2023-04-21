package org.mandarax.compiler.rt;

/**
 * Interface for derivation listener.
 * @author <a href="http://www-ist.massey.ac.nz/JBDietrich/">Jens Dietrich</a>
 * @version 0.1
 * @param <T> the type of the iterated elements
 */
public interface DerivationListener {

    /**
	 * Notify the listener.  
	 * @param ruleRef
	 * @param derivationDepth
	 * @param derivationCount
	 */
    public void step(String ruleRef, int derivationDepth, int derivationCount);
}
