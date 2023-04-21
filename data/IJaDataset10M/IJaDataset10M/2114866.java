package Datadescriptor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Datadescriptor.RecordType#getGroup <em>Group</em>}</li>
 *   <li>{@link Datadescriptor.RecordType#getData <em>Data</em>}</li>
 *   <li>{@link Datadescriptor.RecordType#getRecord <em>Record</em>}</li>
 *   <li>{@link Datadescriptor.RecordType#getBranch <em>Branch</em>}</li>
 *   <li>{@link Datadescriptor.RecordType#getCount <em>Count</em>}</li>
 *   <li>{@link Datadescriptor.RecordType#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see Datadescriptor.DatadescriptorPackage#getRecordType()
 * @model extendedMetaData="name='recordType' kind='elementOnly'"
 * @generated
 */
public interface RecordType extends EObject {

    /**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
    FeatureMap getGroup();

    /**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference list.
	 * The list contents are of type {@link Datadescriptor.DataType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference list.
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Data()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='data' namespace='##targetNamespace' group='group:0'"
	 * @generated
	 */
    EList<DataType> getData();

    /**
	 * Returns the value of the '<em><b>Record</b></em>' containment reference list.
	 * The list contents are of type {@link Datadescriptor.RecordType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Record</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Record</em>' containment reference list.
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Record()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='record' namespace='##targetNamespace' group='group:0'"
	 * @generated
	 */
    EList<RecordType> getRecord();

    /**
	 * Returns the value of the '<em><b>Branch</b></em>' containment reference list.
	 * The list contents are of type {@link Datadescriptor.BranchType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Branch</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Branch</em>' containment reference list.
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Branch()
	 * @model containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='branch' namespace='##targetNamespace' group='group:0'"
	 * @generated
	 */
    EList<BranchType> getBranch();

    /**
	 * Returns the value of the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Count</em>' attribute.
	 * @see #setCount(String)
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Count()
	 * @model dataType="Datadescriptor.CountType"
	 *        extendedMetaData="kind='attribute' name='count' namespace='##targetNamespace'"
	 * @generated
	 */
    String getCount();

    /**
	 * Sets the value of the '{@link Datadescriptor.RecordType#getCount <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count</em>' attribute.
	 * @see #getCount()
	 * @generated
	 */
    void setCount(String value);

    /**
	 * Returns the value of the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(String)
	 * @see Datadescriptor.DatadescriptorPackage#getRecordType_Target()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='target' namespace='##targetNamespace'"
	 * @generated
	 */
    String getTarget();

    /**
	 * Sets the value of the '{@link Datadescriptor.RecordType#getTarget <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
    void setTarget(String value);
}
