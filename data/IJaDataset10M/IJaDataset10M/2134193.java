package Traces;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Object Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Traces.ObjectChange#getFeatureChanges <em>Feature Changes</em>}</li>
 *   <li>{@link Traces.ObjectChange#getMetaClassName <em>Meta Class Name</em>}</li>
 *   <li>{@link Traces.ObjectChange#getObjectID <em>Object ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see Traces.TracesPackage#getObjectChange()
 * @model
 * @generated
 */
public interface ObjectChange extends AbstractChange {

    /**
	 * Returns the value of the '<em><b>Feature Changes</b></em>' containment reference list.
	 * The list contents are of type {@link Traces.FeatureChange}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Changes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Changes</em>' containment reference list.
	 * @see Traces.TracesPackage#getObjectChange_FeatureChanges()
	 * @model containment="true"
	 * @generated
	 */
    EList<FeatureChange> getFeatureChanges();

    /**
	 * Returns the value of the '<em><b>Meta Class Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Meta Class Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Meta Class Name</em>' attribute.
	 * @see #setMetaClassName(String)
	 * @see Traces.TracesPackage#getObjectChange_MetaClassName()
	 * @model required="true"
	 * @generated
	 */
    String getMetaClassName();

    /**
	 * Sets the value of the '{@link Traces.ObjectChange#getMetaClassName <em>Meta Class Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Meta Class Name</em>' attribute.
	 * @see #getMetaClassName()
	 * @generated
	 */
    void setMetaClassName(String value);

    /**
	 * Returns the value of the '<em><b>Object ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Object ID</em>' attribute isn't clear, there really should be
	 * more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Object ID</em>' attribute.
	 * @see #setObjectID(String)
	 * @see Traces.TracesPackage#getObjectChange_ObjectID()
	 * @model required="true"
	 * @generated
	 */
    String getObjectID();

    /**
	 * Sets the value of the '{@link Traces.ObjectChange#getObjectID <em>Object ID</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Object ID</em>' attribute.
	 * @see #getObjectID()
	 * @generated
	 */
    void setObjectID(String value);
}
