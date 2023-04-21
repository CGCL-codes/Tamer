package org.remus.infomngmnt;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getPossibleInfoTypeId <em>Possible Info Type Id</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getId <em>Id</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getUrl <em>Url</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getName <em>Name</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getRepositoryTypeId <em>Repository Type Id</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getRepositoryTypeObjectId <em>Repository Type Object Id</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getWrappedObject <em>Wrapped Object</em>}</li>
 *   <li>{@link org.remus.infomngmnt.RemoteObject#getHash <em>Hash</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject()
 * @model
 * @generated
 */
public interface RemoteObject extends Adapter {

    /**
	 * Returns the value of the '<em><b>Possible Info Type Id</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Possible Info Type Id</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Possible Info Type Id</em>' attribute list.
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_PossibleInfoTypeId()
	 * @model
	 * @generated
	 */
    EList<String> getPossibleInfoTypeId();

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
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_Id()
	 * @model id="true"
	 * @generated
	 */
    String getId();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
    void setId(String value);

    /**
	 * Returns the value of the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Url</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_Url()
	 * @model required="true"
	 * @generated
	 */
    String getUrl();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getUrl <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
    void setUrl(String value);

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Repository Type Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repository Type Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repository Type Id</em>' attribute.
	 * @see #setRepositoryTypeId(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_RepositoryTypeId()
	 * @model required="true"
	 * @generated
	 */
    String getRepositoryTypeId();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getRepositoryTypeId <em>Repository Type Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repository Type Id</em>' attribute.
	 * @see #getRepositoryTypeId()
	 * @generated
	 */
    void setRepositoryTypeId(String value);

    /**
	 * Returns the value of the '<em><b>Repository Type Object Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repository Type Object Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repository Type Object Id</em>' attribute.
	 * @see #setRepositoryTypeObjectId(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_RepositoryTypeObjectId()
	 * @model required="true"
	 * @generated
	 */
    String getRepositoryTypeObjectId();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getRepositoryTypeObjectId <em>Repository Type Object Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Repository Type Object Id</em>' attribute.
	 * @see #getRepositoryTypeObjectId()
	 * @generated
	 */
    void setRepositoryTypeObjectId(String value);

    /**
	 * Returns the value of the '<em><b>Wrapped Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wrapped Object</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wrapped Object</em>' attribute.
	 * @see #setWrappedObject(Object)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_WrappedObject()
	 * @model dataType="org.remus.infomngmnt.Object"
	 * @generated
	 */
    Object getWrappedObject();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getWrappedObject <em>Wrapped Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wrapped Object</em>' attribute.
	 * @see #getWrappedObject()
	 * @generated
	 */
    void setWrappedObject(Object value);

    /**
	 * Returns the value of the '<em><b>Hash</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hash</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hash</em>' attribute.
	 * @see #setHash(String)
	 * @see org.remus.infomngmnt.InfomngmntPackage#getRemoteObject_Hash()
	 * @model
	 * @generated
	 */
    String getHash();

    /**
	 * Sets the value of the '{@link org.remus.infomngmnt.RemoteObject#getHash <em>Hash</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hash</em>' attribute.
	 * @see #getHash()
	 * @generated
	 */
    void setHash(String value);
}
