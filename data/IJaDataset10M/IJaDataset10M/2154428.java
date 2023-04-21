package Slee11.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import Slee11.DescriptionType;
import Slee11.LibraryJarType;
import Slee11.LibraryType;
import Slee11.Slee11Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Library Jar Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Slee11.impl.LibraryJarTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link Slee11.impl.LibraryJarTypeImpl#getLibrary <em>Library</em>}</li>
 *   <li>{@link Slee11.impl.LibraryJarTypeImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LibraryJarTypeImpl extends EObjectImpl implements LibraryJarType {

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected DescriptionType description;

    /**
	 * The cached value of the '{@link #getLibrary() <em>Library</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLibrary()
	 * @generated
	 * @ordered
	 */
    protected EList<LibraryType> library;

    /**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected static final String ID_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
    protected String id = ID_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected LibraryJarTypeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return Slee11Package.Literals.LIBRARY_JAR_TYPE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public DescriptionType getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetDescription(DescriptionType newDescription, NotificationChain msgs) {
        DescriptionType oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION, oldDescription, newDescription);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(DescriptionType newDescription) {
        if (newDescription != description) {
            NotificationChain msgs = null;
            if (description != null) msgs = ((InternalEObject) description).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION, null, msgs);
            if (newDescription != null) msgs = ((InternalEObject) newDescription).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION, null, msgs);
            msgs = basicSetDescription(newDescription, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION, newDescription, newDescription));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<LibraryType> getLibrary() {
        if (library == null) {
            library = new EObjectContainmentEList<LibraryType>(LibraryType.class, this, Slee11Package.LIBRARY_JAR_TYPE__LIBRARY);
        }
        return library;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getId() {
        return id;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setId(String newId) {
        String oldId = id;
        id = newId;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, Slee11Package.LIBRARY_JAR_TYPE__ID, oldId, id));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION:
                return basicSetDescription(null, msgs);
            case Slee11Package.LIBRARY_JAR_TYPE__LIBRARY:
                return ((InternalEList<?>) getLibrary()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION:
                return getDescription();
            case Slee11Package.LIBRARY_JAR_TYPE__LIBRARY:
                return getLibrary();
            case Slee11Package.LIBRARY_JAR_TYPE__ID:
                return getId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION:
                setDescription((DescriptionType) newValue);
                return;
            case Slee11Package.LIBRARY_JAR_TYPE__LIBRARY:
                getLibrary().clear();
                getLibrary().addAll((Collection<? extends LibraryType>) newValue);
                return;
            case Slee11Package.LIBRARY_JAR_TYPE__ID:
                setId((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION:
                setDescription((DescriptionType) null);
                return;
            case Slee11Package.LIBRARY_JAR_TYPE__LIBRARY:
                getLibrary().clear();
                return;
            case Slee11Package.LIBRARY_JAR_TYPE__ID:
                setId(ID_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case Slee11Package.LIBRARY_JAR_TYPE__DESCRIPTION:
                return description != null;
            case Slee11Package.LIBRARY_JAR_TYPE__LIBRARY:
                return library != null && !library.isEmpty();
            case Slee11Package.LIBRARY_JAR_TYPE__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }
}
