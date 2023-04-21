package SBVR.impl;

import SBVR.Community;
import SBVR.SBVRPackage;
import SBVR.URI;
import java.util.Collection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Community</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link SBVR.impl.CommunityImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link SBVR.impl.CommunityImpl#getSubCommunity <em>Sub Community</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CommunityImpl extends EObjectImpl implements Community {

    /**
	 * The cached value of the '{@link #getUri() <em>Uri</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
    protected EList<URI> uri;

    /**
	 * The cached value of the '{@link #getSubCommunity() <em>Sub Community</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubCommunity()
	 * @generated
	 * @ordered
	 */
    protected EList<Community> subCommunity;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CommunityImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return SBVRPackage.Literals.COMMUNITY;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<URI> getUri() {
        if (uri == null) {
            uri = new EObjectResolvingEList<URI>(URI.class, this, SBVRPackage.COMMUNITY__URI);
        }
        return uri;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Community> getSubCommunity() {
        if (subCommunity == null) {
            subCommunity = new EObjectResolvingEList<Community>(Community.class, this, SBVRPackage.COMMUNITY__SUB_COMMUNITY);
        }
        return subCommunity;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case SBVRPackage.COMMUNITY__URI:
                return getUri();
            case SBVRPackage.COMMUNITY__SUB_COMMUNITY:
                return getSubCommunity();
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
            case SBVRPackage.COMMUNITY__URI:
                getUri().clear();
                getUri().addAll((Collection<? extends URI>) newValue);
                return;
            case SBVRPackage.COMMUNITY__SUB_COMMUNITY:
                getSubCommunity().clear();
                getSubCommunity().addAll((Collection<? extends Community>) newValue);
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
            case SBVRPackage.COMMUNITY__URI:
                getUri().clear();
                return;
            case SBVRPackage.COMMUNITY__SUB_COMMUNITY:
                getSubCommunity().clear();
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
            case SBVRPackage.COMMUNITY__URI:
                return uri != null && !uri.isEmpty();
            case SBVRPackage.COMMUNITY__SUB_COMMUNITY:
                return subCommunity != null && !subCommunity.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
