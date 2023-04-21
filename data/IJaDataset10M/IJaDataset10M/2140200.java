package se.mdh.mrtc.save.taEditor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TA</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getParameter <em>Parameter</em>}</li>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getDeclarations <em>Declarations</em>}</li>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getContainsExit <em>Contains Exit</em>}</li>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getContainsEntry <em>Contains Entry</em>}</li>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getContainsLocation <em>Contains Location</em>}</li>
 *   <li>{@link se.mdh.mrtc.save.taEditor.TA#getTAName <em>TA Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA()
 * @model
 * @generated
 */
public interface TA extends EObject {

    /**
	 * Returns the value of the '<em><b>Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' attribute.
	 * @see #setParameter(String)
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_Parameter()
	 * @model
	 * @generated
	 */
    String getParameter();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.save.taEditor.TA#getParameter <em>Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter</em>' attribute.
	 * @see #getParameter()
	 * @generated
	 */
    void setParameter(String value);

    /**
	 * Returns the value of the '<em><b>Declarations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Declarations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Declarations</em>' attribute.
	 * @see #setDeclarations(String)
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_Declarations()
	 * @model
	 * @generated
	 */
    String getDeclarations();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.save.taEditor.TA#getDeclarations <em>Declarations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Declarations</em>' attribute.
	 * @see #getDeclarations()
	 * @generated
	 */
    void setDeclarations(String value);

    /**
	 * Returns the value of the '<em><b>Contains Exit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contains Exit</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contains Exit</em>' containment reference.
	 * @see #setContainsExit(Exit)
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_ContainsExit()
	 * @model containment="true"
	 * @generated
	 */
    Exit getContainsExit();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.save.taEditor.TA#getContainsExit <em>Contains Exit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contains Exit</em>' containment reference.
	 * @see #getContainsExit()
	 * @generated
	 */
    void setContainsExit(Exit value);

    /**
	 * Returns the value of the '<em><b>Contains Entry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contains Entry</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contains Entry</em>' containment reference.
	 * @see #setContainsEntry(AbstractEntry)
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_ContainsEntry()
	 * @model containment="true"
	 * @generated
	 */
    AbstractEntry getContainsEntry();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.save.taEditor.TA#getContainsEntry <em>Contains Entry</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contains Entry</em>' containment reference.
	 * @see #getContainsEntry()
	 * @generated
	 */
    void setContainsEntry(AbstractEntry value);

    /**
	 * Returns the value of the '<em><b>Contains Location</b></em>' containment reference list.
	 * The list contents are of type {@link se.mdh.mrtc.save.taEditor.AbstractLocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contains Location</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contains Location</em>' containment reference list.
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_ContainsLocation()
	 * @model containment="true"
	 * @generated
	 */
    EList<AbstractLocation> getContainsLocation();

    /**
	 * Returns the value of the '<em><b>TA Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>TA Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>TA Name</em>' attribute.
	 * @see #setTAName(String)
	 * @see se.mdh.mrtc.save.taEditor.TaEditorPackage#getTA_TAName()
	 * @model
	 * @generated
	 */
    String getTAName();

    /**
	 * Sets the value of the '{@link se.mdh.mrtc.save.taEditor.TA#getTAName <em>TA Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>TA Name</em>' attribute.
	 * @see #getTAName()
	 * @generated
	 */
    void setTAName(String value);
}
