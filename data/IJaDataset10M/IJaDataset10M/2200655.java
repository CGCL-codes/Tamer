package net.sf.orcc.ir;

import java.lang.String;
import org.eclipse.emf.ecore.EObject;

/**
 * This class defines a port. A port has a location, a type, a name.
 * 
 * @author Matthieu Wipliez
 * @model
 */
public interface Port extends EObject {

    /**
	 * Returns the name of this port.
	 * 
	 * @return the name of this port
	 * @model dataType="org.eclipse.emf.ecore.EString"
	 */
    String getName();

    /**
	 * Returns the number of tokens consumed by this port.
	 * 
	 * @return the number of tokens consumed by this port
	 * @model
	 */
    int getNumTokensConsumed();

    /**
	 * Returns the number of tokens produced by this port.
	 * 
	 * @return the number of tokens produced by this port
	 * @model
	 */
    int getNumTokensProduced();

    /**
	 * Returns the type of this port.
	 * 
	 * @return the type of this port
	 * @model containment="true"
	 */
    Type getType();

    /**
	 * Increases the number of tokens consumed by this port by the given
	 * integer.
	 * 
	 * @param n
	 *            a number of tokens
	 * @throws IllegalArgumentException
	 *             if n is less or equal to zero
	 */
    void increaseTokenConsumption(int n);

    /**
	 * Increases the number of tokens produced by this port by the given
	 * integer.
	 * 
	 * @param n
	 *            a number of tokens
	 * @throws IllegalArgumentException
	 *             if n is less or equal to zero
	 */
    void increaseTokenProduction(int n);

    /**
	 * Returns <code>true</code> if this port is native.
	 * 
	 * @return <code>true</code> if this port is native
	 * @model
	 */
    boolean isNative();

    /**
	 * Resets the number of tokens consumed by this port.
	 */
    void resetTokenConsumption();

    /**
	 * Resets the number of tokens produced by this port.
	 */
    void resetTokenProduction();

    /**
	 * Sets the name of this port.
	 * 
	 * @param name
	 *            the new name of this port
	 */
    void setName(String name);

    /**
	 * Sets this port as native if <code>newNative</code> is <code>true</code>.
	 * 
	 * @param newNative
	 *            <code>true</code> if this port is native
	 */
    void setNative(boolean newNative);

    /**
	 * Sets the value of the '{@link net.sf.orcc.ir.Port#getNumTokensConsumed <em>Num Tokens Consumed</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @param value the new value of the '<em>Num Tokens Consumed</em>' attribute.
	 * @see #getNumTokensConsumed()
	 * @generated
	 */
    void setNumTokensConsumed(int value);

    /**
	 * Sets the value of the '{@link net.sf.orcc.ir.Port#getNumTokensProduced <em>Num Tokens Produced</em>}' attribute.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @param value the new value of the '<em>Num Tokens Produced</em>' attribute.
	 * @see #getNumTokensProduced()
	 * @generated
	 */
    void setNumTokensProduced(int value);

    /**
	 * Sets the type of this port.
	 * 
	 * @param type
	 *            the new type of this port
	 */
    void setType(Type type);
}
