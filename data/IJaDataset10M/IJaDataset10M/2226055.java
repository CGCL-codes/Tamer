package org.remus.infomngmnt;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Binary Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.BinaryReference#getId <em>Id</em>}</li>
 *   <li>{@link org.remus.infomngmnt.BinaryReference#getProjectRelativePath <em>Project Relative Path</em>}</li>
 *   <li>{@link org.remus.infomngmnt.BinaryReference#isDirty <em>Dirty</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.remus.infomngmnt.InfomngmntPackage#getBinaryReference()
 * @model
 * @generated
 */
public interface BinaryReference extends EObject {

    /**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getBinaryReference_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.BinaryReference#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Project Relative Path</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Project Relative Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Project Relative Path</em>' attribute.
	 * @see #setProjectRelativePath(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getBinaryReference_ProjectRelativePath()
	 * @model default="" required="true"
	 * @generated
	 */
    String getProjectRelativePath();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.BinaryReference#getProjectRelativePath <em>Project Relative Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Project Relative Path</em>' attribute.
	 * @see #getProjectRelativePath()
	 * @generated
	 */
    void setProjectRelativePath(String value);

    /**
	 * Returns the value of the '<em><b>Dirty</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dirty</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dirty</em>' attribute.
	 * @see #setDirty(boolean)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getBinaryReference_Dirty()
	 * @model
	 * @generated
	 */
    boolean isDirty();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.BinaryReference#isDirty <em>Dirty</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dirty</em>' attribute.
	 * @see #isDirty()
	 * @generated
	 */
    void setDirty(boolean value);
}
