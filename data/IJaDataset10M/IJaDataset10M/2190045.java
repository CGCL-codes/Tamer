package Bpxl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Information Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Bpxl.InformationType#getPath <em>Path</em>}</li>
 *   <li>{@link Bpxl.InformationType#getAncillaryInfo <em>Ancillary Info</em>}</li>
 *   <li>{@link Bpxl.InformationType#getFaultName <em>Fault Name</em>}</li>
 *   <li>{@link Bpxl.InformationType#getEventId <em>Event Id</em>}</li>
 *   <li>{@link Bpxl.InformationType#getProcessId <em>Process Id</em>}</li>
 *   <li>{@link Bpxl.InformationType#getTimestamp <em>Timestamp</em>}</li>
 * </ul>
 * </p>
 *
 * @see Bpxl.BpxlPackage#getInformationType()
 * @model extendedMetaData="name='information_._type' kind='elementOnly'"
 * @generated
 */
public interface InformationType extends EObject {

    /**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see Bpxl.BpxlPackage#getInformationType_Path()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='path' namespace='##targetNamespace'"
	 * @generated
	 */
    String getPath();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
    void setPath(String value);

    /**
	 * Returns the value of the '<em><b>Ancillary Info</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ancillary Info</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ancillary Info</em>' attribute.
	 * @see #setAncillaryInfo(String)
	 * @see Bpxl.BpxlPackage#getInformationType_AncillaryInfo()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='ancillaryInfo' namespace='##targetNamespace'"
	 * @generated
	 */
    String getAncillaryInfo();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getAncillaryInfo <em>Ancillary Info</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ancillary Info</em>' attribute.
	 * @see #getAncillaryInfo()
	 * @generated
	 */
    void setAncillaryInfo(String value);

    /**
	 * Returns the value of the '<em><b>Fault Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fault Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fault Name</em>' containment reference.
	 * @see #setFaultName(EObject)
	 * @see Bpxl.BpxlPackage#getInformationType_FaultName()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='faultName' namespace='##targetNamespace'"
	 * @generated
	 */
    EObject getFaultName();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getFaultName <em>Fault Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fault Name</em>' containment reference.
	 * @see #getFaultName()
	 * @generated
	 */
    void setFaultName(EObject value);

    /**
	 * Returns the value of the '<em><b>Event Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Id</em>' attribute.
	 * @see #setEventId(String)
	 * @see Bpxl.BpxlPackage#getInformationType_EventId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='eventId' namespace='##targetNamespace'"
	 * @generated
	 */
    String getEventId();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getEventId <em>Event Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Id</em>' attribute.
	 * @see #getEventId()
	 * @generated
	 */
    void setEventId(String value);

    /**
	 * Returns the value of the '<em><b>Process Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Process Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Process Id</em>' attribute.
	 * @see #setProcessId(String)
	 * @see Bpxl.BpxlPackage#getInformationType_ProcessId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='processId' namespace='##targetNamespace'"
	 * @generated
	 */
    String getProcessId();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getProcessId <em>Process Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process Id</em>' attribute.
	 * @see #getProcessId()
	 * @generated
	 */
    void setProcessId(String value);

    /**
	 * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timestamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timestamp</em>' attribute.
	 * @see #setTimestamp(String)
	 * @see Bpxl.BpxlPackage#getInformationType_Timestamp()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='timestamp' namespace='##targetNamespace'"
	 * @generated
	 */
    String getTimestamp();

    /**
	 * Sets the value of the '{@link Bpxl.InformationType#getTimestamp <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timestamp</em>' attribute.
	 * @see #getTimestamp()
	 * @generated
	 */
    void setTimestamp(String value);
}
