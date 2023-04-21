package fr.msimeon.mads.mads;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Script Plan Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.ScriptPlanItem#getItemRef <em>Item Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.msimeon.mads.mads.MadsPackage#getScriptPlanItem()
 * @model
 * @generated
 */
public interface ScriptPlanItem extends PlanItem {

    /**
   * Returns the value of the '<em><b>Item Ref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Item Ref</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Item Ref</em>' reference.
   * @see #setItemRef(Script)
   * @see fr.msimeon.mads.mads.MadsPackage#getScriptPlanItem_ItemRef()
   * @model
   * @generated
   */
    Script getItemRef();

    /**
   * Sets the value of the '{@link fr.msimeon.mads.mads.ScriptPlanItem#getItemRef <em>Item Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Item Ref</em>' reference.
   * @see #getItemRef()
   * @generated
   */
    void setItemRef(Script value);
}
