package net.sf.sail.emf.sailuserdata;

import org.doomdark.uuid.UUID;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EAnnotation Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.sf.sail.emf.sailuserdata.EAnnotationGroup#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link net.sf.sail.emf.sailuserdata.EAnnotationGroup#getAgentUUID <em>Agent UUID</em>}</li>
 *   <li>{@link net.sf.sail.emf.sailuserdata.EAnnotationGroup#getAnnotationSource <em>Annotation Source</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.sf.sail.emf.sailuserdata.SailuserdataPackage#getEAnnotationGroup()
 * @model
 * @generated
 */
public interface EAnnotationGroup extends EObject {

    /**
	 * Returns the value of the '<em><b>Annotations</b></em>' containment reference list.
	 * The list contents are of type {@link net.sf.sail.emf.sailuserdata.EAnnotation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotations</em>' containment reference list.
	 * @see net.sf.sail.emf.sailuserdata.SailuserdataPackage#getEAnnotationGroup_Annotations()
	 * @model type="net.sf.sail.emf.sailuserdata.EAnnotation" containment="true"
	 * @generated
	 */
    EList getAnnotations();

    /**
	 * Returns the value of the '<em><b>Agent UUID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Agent UUID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Agent UUID</em>' attribute.
	 * @see #setAgentUUID(UUID)
	 * @see net.sf.sail.emf.sailuserdata.SailuserdataPackage#getEAnnotationGroup_AgentUUID()
	 * @model dataType="net.sf.sail.emf.sailuserdata.EUUID"
	 * @generated
	 */
    UUID getAgentUUID();

    /**
	 * Sets the value of the '{@link net.sf.sail.emf.sailuserdata.EAnnotationGroup#getAgentUUID <em>Agent UUID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Agent UUID</em>' attribute.
	 * @see #getAgentUUID()
	 * @generated
	 */
    void setAgentUUID(UUID value);

    /**
	 * Returns the value of the '<em><b>Annotation Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annotation Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annotation Source</em>' attribute.
	 * @see #setAnnotationSource(String)
	 * @see net.sf.sail.emf.sailuserdata.SailuserdataPackage#getEAnnotationGroup_AnnotationSource()
	 * @model
	 * @generated
	 */
    String getAnnotationSource();

    /**
	 * Sets the value of the '{@link net.sf.sail.emf.sailuserdata.EAnnotationGroup#getAnnotationSource <em>Annotation Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotation Source</em>' attribute.
	 * @see #getAnnotationSource()
	 * @generated
	 */
    void setAnnotationSource(String value);
}
