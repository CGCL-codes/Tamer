package fr.msimeon.mads.mads;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Herd Feed Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fr.msimeon.mads.mads.HerdFeedItem#getCommRef <em>Comm Ref</em>}</li>
 *   <li>{@link fr.msimeon.mads.mads.HerdFeedItem#getCommCalcType <em>Comm Calc Type</em>}</li>
 *   <li>{@link fr.msimeon.mads.mads.HerdFeedItem#getQuantity <em>Quantity</em>}</li>
 * </ul>
 * </p>
 *
 * @see fr.msimeon.mads.mads.MadsPackage#getHerdFeedItem()
 * @model
 * @generated
 */
public interface HerdFeedItem extends EObject {

    /**
   * Returns the value of the '<em><b>Comm Ref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comm Ref</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comm Ref</em>' reference.
   * @see #setCommRef(Commodity)
   * @see fr.msimeon.mads.mads.MadsPackage#getHerdFeedItem_CommRef()
   * @model
   * @generated
   */
    Commodity getCommRef();

    /**
   * Sets the value of the '{@link fr.msimeon.mads.mads.HerdFeedItem#getCommRef <em>Comm Ref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comm Ref</em>' reference.
   * @see #getCommRef()
   * @generated
   */
    void setCommRef(Commodity value);

    /**
   * Returns the value of the '<em><b>Comm Calc Type</b></em>' attribute.
   * The literals are from the enumeration {@link fr.msimeon.mads.mads.CommInPlanType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comm Calc Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comm Calc Type</em>' attribute.
   * @see fr.msimeon.mads.mads.CommInPlanType
   * @see #setCommCalcType(CommInPlanType)
   * @see fr.msimeon.mads.mads.MadsPackage#getHerdFeedItem_CommCalcType()
   * @model
   * @generated
   */
    CommInPlanType getCommCalcType();

    /**
   * Sets the value of the '{@link fr.msimeon.mads.mads.HerdFeedItem#getCommCalcType <em>Comm Calc Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comm Calc Type</em>' attribute.
   * @see fr.msimeon.mads.mads.CommInPlanType
   * @see #getCommCalcType()
   * @generated
   */
    void setCommCalcType(CommInPlanType value);

    /**
   * Returns the value of the '<em><b>Quantity</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Quantity</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Quantity</em>' containment reference.
   * @see #setQuantity(HerdDataByCat)
   * @see fr.msimeon.mads.mads.MadsPackage#getHerdFeedItem_Quantity()
   * @model containment="true"
   * @generated
   */
    HerdDataByCat getQuantity();

    /**
   * Sets the value of the '{@link fr.msimeon.mads.mads.HerdFeedItem#getQuantity <em>Quantity</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Quantity</em>' containment reference.
   * @see #getQuantity()
   * @generated
   */
    void setQuantity(HerdDataByCat value);
}
