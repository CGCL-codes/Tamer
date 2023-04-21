package net.sourceforge.mezzo.editor.mezzo;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Switch Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sourceforge.mezzo.editor.mezzo.SwitchCase#getTestExpression <em>Test Expression</em>}</li>
 *   <li>{@link net.sourceforge.mezzo.editor.mezzo.SwitchCase#getCaseBlock <em>Case Block</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sourceforge.mezzo.editor.mezzo.MezzoPackage#getSwitchCase()
 * @model
 * @generated
 */
public interface SwitchCase extends EObject {

    /**
   * Returns the value of the '<em><b>Test Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Test Expression</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Test Expression</em>' containment reference.
   * @see #setTestExpression(Expression)
   * @see net.sourceforge.mezzo.editor.mezzo.MezzoPackage#getSwitchCase_TestExpression()
   * @model containment="true"
   * @generated
   */
    Expression getTestExpression();

    /**
   * Sets the value of the '{@link net.sourceforge.mezzo.editor.mezzo.SwitchCase#getTestExpression <em>Test Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Test Expression</em>' containment reference.
   * @see #getTestExpression()
   * @generated
   */
    void setTestExpression(Expression value);

    /**
   * Returns the value of the '<em><b>Case Block</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Case Block</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Case Block</em>' containment reference.
   * @see #setCaseBlock(CompoundStatement)
   * @see net.sourceforge.mezzo.editor.mezzo.MezzoPackage#getSwitchCase_CaseBlock()
   * @model containment="true"
   * @generated
   */
    CompoundStatement getCaseBlock();

    /**
   * Sets the value of the '{@link net.sourceforge.mezzo.editor.mezzo.SwitchCase#getCaseBlock <em>Case Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Case Block</em>' containment reference.
   * @see #getCaseBlock()
   * @generated
   */
    void setCaseBlock(CompoundStatement value);
}
