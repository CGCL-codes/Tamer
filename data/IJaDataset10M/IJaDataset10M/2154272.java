package net.sf.sit.candidate;

import java.lang.reflect.Method;

/**
 * Matches a candidate method to a facade method.
 */
public interface Matcher {

    /**
	 * Match a candidate method to a facade method.
	 * 
	 * @param facadeMethod
	 *            Facade method
	 * @param candidateMethod
	 *            Candidate method
	 * @return True iff the candidate method matches
	 */
    boolean matches(Method facadeMethod, Method candidateMethod);
}
