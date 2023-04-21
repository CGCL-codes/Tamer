package tefkat.engine.runtime;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pattern Use</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link tefkat.engine.runtime.PatternUse#getDefn <em>Defn</em>}</li>
 *   <li>{@link tefkat.engine.runtime.PatternUse#getArg <em>Arg</em>}</li>
 * </ul>
 * </p>
 *
 * @see tefkat.engine.runtime.RuntimePackage#getPatternUse()
 * @model
 * @generated
 */
public interface PatternUse extends SimpleTerm {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String copyright = "Copyright michael lawley 2004";

    /**
     * Returns the value of the '<em><b>Defn</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Defn</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Defn</em>' reference.
     * @see #setDefn(PatternDefn)
     * @see tefkat.engine.runtime.RuntimePackage#getPatternUse_Defn()
     * @model required="true"
     * @generated
     */
    PatternDefn getDefn();

    /**
     * Sets the value of the '{@link tefkat.engine.runtime.PatternUse#getDefn <em>Defn</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Defn</em>' reference.
     * @see #getDefn()
     * @generated
     */
    void setDefn(PatternDefn value);

    /**
     * Returns the value of the '<em><b>Arg</b></em>' containment reference list.
     * The list contents are of type {@link tefkat.engine.runtime.Expression}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arg</em>' containment reference list.
     * @see tefkat.engine.runtime.RuntimePackage#getPatternUse_Arg()
     * @model type="tefkat.engine.runtime.Expression" containment="true"
     * @generated
     */
    EList getArg();
}
