package org.mandarax.kernel;

/**
 * A derivation event listener a couple of "hooks" to customize the derivation
 * process. These methods are called before the derivation, and whenever results are 
 * added to the final result set.
 * <br>
 * Implementations should be stateless (i.e. there should be no instance variables
 * and should have a public parameterless constructor. I.e., an instance can be build
 * from the class name.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 3.2
 */
public interface DerivationEventListener {

    /**
	 * This method is executed before a result set is added to the result set.
	 * @param session a session object
	 */
    public void onResultDo(Session session) throws InferenceException;

    /**
	 * This method is executed before the IE starts the derivation.
	 * @param session a session object
	 */
    public void beforeDerivationDo(Session session) throws InferenceException;
}
