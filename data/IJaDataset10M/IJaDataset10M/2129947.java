package fi.jab.esb.config.xml;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Actions Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link fi.jab.esb.config.xml.ActionsType#getAction <em>Action</em>}</li>
 *   <li>{@link fi.jab.esb.config.xml.ActionsType#getMep <em>Mep</em>}</li>
 * </ul>
 * </p>
 *
 * @see fi.jab.esb.config.xml.XmlPackage#getActionsType()
 * @model extendedMetaData="name='actions_._type' kind='elementOnly'"
 * @generated
 */
public interface ActionsType extends EObject {

    /**
   * Returns the value of the '<em><b>Action</b></em>' containment reference list.
   * The list contents are of type {@link fi.jab.esb.config.xml.ActionType}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * 
   * 				An action which will act on the message when it is received by the service. Actions can be chained. Hence you can specify a
   * 				list of actions. Action are 'pluggable' and can be custom code, however JBossESB comes with a list of predefined actions.
   * 			
   * <!-- end-model-doc -->
   * @return the value of the '<em>Action</em>' containment reference list.
   * @see fi.jab.esb.config.xml.XmlPackage#getActionsType_Action()
   * @model containment="true" required="true"
   *        extendedMetaData="kind='element' name='action' namespace='##targetNamespace'"
   * @generated
   */
    EList<ActionType> getAction();

    /**
   * Returns the value of the '<em><b>Mep</b></em>' attribute.
   * The default value is <code>"RequestResponse"</code>.
   * The literals are from the enumeration {@link fi.jab.esb.config.xml.MepType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mep</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mep</em>' attribute.
   * @see fi.jab.esb.config.xml.MepType
   * @see #isSetMep()
   * @see #unsetMep()
   * @see #setMep(MepType)
   * @see fi.jab.esb.config.xml.XmlPackage#getActionsType_Mep()
   * @model default="RequestResponse" unsettable="true"
   *        extendedMetaData="kind='attribute' name='mep'"
   * @generated
   */
    MepType getMep();

    /**
   * Sets the value of the '{@link fi.jab.esb.config.xml.ActionsType#getMep <em>Mep</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mep</em>' attribute.
   * @see fi.jab.esb.config.xml.MepType
   * @see #isSetMep()
   * @see #unsetMep()
   * @see #getMep()
   * @generated
   */
    void setMep(MepType value);

    /**
   * Unsets the value of the '{@link fi.jab.esb.config.xml.ActionsType#getMep <em>Mep</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSetMep()
   * @see #getMep()
   * @see #setMep(MepType)
   * @generated
   */
    void unsetMep();

    /**
   * Returns whether the value of the '{@link fi.jab.esb.config.xml.ActionsType#getMep <em>Mep</em>}' attribute is set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return whether the value of the '<em>Mep</em>' attribute is set.
   * @see #unsetMep()
   * @see #getMep()
   * @see #setMep(MepType)
   * @generated
   */
    boolean isSetMep();
}
