package tudresden.ocl20.pivot.essentialocl.standardlibrary;

import tudresden.ocl20.pivot.modelinstancetype.types.IModelInstanceBoolean;

/**
 * 
 * 
 * @author Matthias Braeuer
 * @version 1.0 30.03.2007
 */
public interface OclBoolean extends OclLibraryObject {

    /**
	 * Returns the adated {@link IModelInstanceBoolean}.
	 * 
	 * @return the adated {@link IModelInstanceBoolean}
	 */
    IModelInstanceBoolean getModelInstanceBoolean();

    /**
	 * 
	 * @param b
	 * @return <code>true</code> OR anything is <code>true</code>,
	 *         <code>false</code> otherwise<br>
	 *         <strong>This rule does not apply for invalid. Invalid results in
	 *         invalid</strong>.
	 */
    OclBoolean or(OclBoolean b);

    /**
	 * 
	 * @param b
	 * @return true if either <code>this</code> or <code>b</code> is true, but not
	 *         both.
	 */
    OclBoolean xor(OclBoolean b);

    /**
	 * 
	 * @param b
	 * @return <code>false</code> AND anythings returns <code>false</code><br>
	 *         <strong>This rule does not apply for invalid. Invalid results in
	 *         invalid</strong>.
	 */
    OclBoolean and(OclBoolean b);

    /**
	 * 
	 * @param b
	 * @return true if <code>this</code> is false.
	 */
    OclBoolean not();

    /**
	 * 
	 * @param b
	 * @return <code>false</code> IMPLIES anything returns <code>true</code><br>
	 *         anything IMPLIES <code>true</code> returns <code>true</code><br>
	 *         <strong>These rules do not apply for invalid. Invalid results in
	 *         invalid</strong>.
	 */
    OclBoolean implies(OclBoolean b);

    /**
	 * Get boolean representation of <code>this</code>.<br>
	 * <strong>When calling this method, make sure to test that this
	 * {@link OclBoolean} is neither invalid nor undefined!</strong>
	 * 
	 * @return <code>true</code>, if <code>this</code> is true
	 */
    boolean isTrue();

    /**
	 * If-then-else-statemant.
	 * 
	 * @param thenStatement
	 * @param elseStatement
	 * 
	 * @return thanStatement if <code>this</code> ist true, otherwise
	 *         elseStatement
	 */
    OclAny ifThenElse(OclAny thenStatement, OclAny elseStatement);

    /**
	 * 
	 * @return a {@link OclString} representation of this {@link OclBoolean}
	 */
    OclString convertToString();
}
