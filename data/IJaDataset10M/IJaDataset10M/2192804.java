package de.hauschild.dbc4j.engine;

import de.hauschild.dbc4j.MalformedContractException;
import de.hauschild.dbc4j.annotation.Invariant;
import de.hauschild.dbc4j.annotation.PostCondition;
import de.hauschild.dbc4j.annotation.PreCondition;

/**
 * A {@link ContractEngine} verifies if the contracts defined by {@link PreCondition PreConditions},
 * {@link PostCondition PostConditions} and {@link Invariant Invariants} are kept.
 * 
 * @since 1.0.0
 * @author Klaus Hauschild
 */
public interface ContractEngine {

    /**
   * Evaluate the contract expression of the {@link PostCondition}.
   * 
   * @param postCondition
   *          the post condition
   * @param target
   *          the target object
   * @param arguments
   *          the arguments
   * @param result
   *          the result of the method invocation
   * @param thrown
   *          the thrown exception from the method invocation
   * @return true, if successful
   * @throws MalformedContractException
   *           is thrown if the expression has not the type of {@link Boolean} or malformed in the context of the
   *           {@link ContractEngine}
   */
    boolean evaluate(PostCondition postCondition, Object target, Object[] arguments, final Object result, final Throwable thrown) throws MalformedContractException;

    /**
   * Evaluate the contract expression of the {@link PreCondition}.
   * 
   * @param preCondition
   *          the post condition
   * @param target
   *          the target object
   * @param arguments
   *          the arguments
   * @return <code>true</code> if the specified expression evaluates to <code>true</code>,<br />
   *         <code>false</code> otherwise
   * @throws MalformedContractException
   *           is thrown if the expression has not the type of {@link Boolean} or malformed in the context of the
   *           {@link ContractEngine}
   */
    boolean evaluate(PreCondition preCondition, Object target, Object[] arguments) throws MalformedContractException;
}
