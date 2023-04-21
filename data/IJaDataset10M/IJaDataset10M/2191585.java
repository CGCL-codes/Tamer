package au.edu.archer.metadata.msf.mss;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link au.edu.archer.metadata.msf.mss.Operation#getOperator <em>Operator</em>}</li>
 *   <li>{@link au.edu.archer.metadata.msf.mss.Operation#getOperands <em>Operands</em>}</li>
 * </ul>
 * </p>
 *
 * @see au.edu.archer.metadata.msf.mss.MSSPackage#getOperation()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='CorrectNumberOfOperands'"
 * @generated
 */
public interface Operation extends Expression {

    /**
     * Returns the value of the '<em><b>Operator</b></em>' attribute.
     * The default value is <code>""</code>.
     * The literals are from the enumeration {@link au.edu.archer.metadata.msf.mss.Operator}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operator</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operator</em>' attribute.
     * @see au.edu.archer.metadata.msf.mss.Operator
     * @see #setOperator(Operator)
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getOperation_Operator()
     * @model default="" required="true"
     * @generated
     */
    Operator getOperator();

    /**
     * Sets the value of the '{@link au.edu.archer.metadata.msf.mss.Operation#getOperator <em>Operator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operator</em>' attribute.
     * @see au.edu.archer.metadata.msf.mss.Operator
     * @see #getOperator()
     * @generated
     */
    void setOperator(Operator value);

    /**
     * Returns the value of the '<em><b>Operands</b></em>' containment reference list.
     * The list contents are of type {@link au.edu.archer.metadata.msf.mss.Expression}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operands</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operands</em>' containment reference list.
     * @see au.edu.archer.metadata.msf.mss.MSSPackage#getOperation_Operands()
     * @model containment="true" required="true"
     * @generated
     */
    EList<Expression> getOperands();
}
