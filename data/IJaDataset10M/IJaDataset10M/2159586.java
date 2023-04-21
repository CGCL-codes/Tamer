package odm.impl;

import java.util.Collection;
import odm.Imports;
import odm.MainOntology;
import odm.OdmPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Main Ontology</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link odm.impl.MainOntologyImpl#getImports <em>Imports</em>}</li>
 *   <li>{@link odm.impl.MainOntologyImpl#getTag <em>Tag</em>}</li>
 *   <li>{@link odm.impl.MainOntologyImpl#getURI <em>URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MainOntologyImpl extends OntologyImpl implements MainOntology {

    /**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
    protected EList imports = null;

    /**
	 * The default value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected static final String TAG_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getTag() <em>Tag</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTag()
	 * @generated
	 * @ordered
	 */
    protected String tag = TAG_EDEFAULT;

    /**
	 * The default value of the '{@link #getURI() <em>URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getURI()
	 * @generated
	 * @ordered
	 */
    protected static final String URI_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getURI() <em>URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getURI()
	 * @generated
	 * @ordered
	 */
    protected String uRI = URI_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected MainOntologyImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return OdmPackage.Literals.MAIN_ONTOLOGY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getImports() {
        if (imports == null) {
            imports = new EObjectContainmentEList(Imports.class, this, OdmPackage.MAIN_ONTOLOGY__IMPORTS);
        }
        return imports;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setTag(String newTag) {
        String oldTag = tag;
        tag = newTag;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.MAIN_ONTOLOGY__TAG, oldTag, tag));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getURI() {
        return uRI;
    }

    public void setURI(String newURI) {
        String oldURI = uRI;
        uRI = newURI;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, OdmPackage.MAIN_ONTOLOGY__URI, oldURI, uRI));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case OdmPackage.MAIN_ONTOLOGY__IMPORTS:
                return ((InternalEList) getImports()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case OdmPackage.MAIN_ONTOLOGY__IMPORTS:
                return getImports();
            case OdmPackage.MAIN_ONTOLOGY__TAG:
                return getTag();
            case OdmPackage.MAIN_ONTOLOGY__URI:
                return getURI();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case OdmPackage.MAIN_ONTOLOGY__IMPORTS:
                getImports().clear();
                getImports().addAll((Collection) newValue);
                return;
            case OdmPackage.MAIN_ONTOLOGY__TAG:
                setTag((String) newValue);
                return;
            case OdmPackage.MAIN_ONTOLOGY__URI:
                setURI((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void eUnset(int featureID) {
        switch(featureID) {
            case OdmPackage.MAIN_ONTOLOGY__IMPORTS:
                getImports().clear();
                return;
            case OdmPackage.MAIN_ONTOLOGY__TAG:
                setTag(TAG_EDEFAULT);
                return;
            case OdmPackage.MAIN_ONTOLOGY__URI:
                setURI(URI_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case OdmPackage.MAIN_ONTOLOGY__IMPORTS:
                return imports != null && !imports.isEmpty();
            case OdmPackage.MAIN_ONTOLOGY__TAG:
                return TAG_EDEFAULT == null ? tag != null : !TAG_EDEFAULT.equals(tag);
            case OdmPackage.MAIN_ONTOLOGY__URI:
                return URI_EDEFAULT == null ? uRI != null : !URI_EDEFAULT.equals(uRI);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (tag: ");
        result.append(tag);
        result.append(", uRI: ");
        result.append(uRI);
        result.append(')');
        return result.toString();
    }
}
