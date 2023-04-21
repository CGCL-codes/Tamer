package org.slasoi.models.scm.impl;

import java.util.Collection;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.slasoi.models.scm.ServiceConstructionModelPackage;
import org.slasoi.models.scm.SoftwareElement;
import org.slasoi.models.scm.WebServiceElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Web Service Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.slasoi.models.scm.impl.WebServiceElementImpl#getRequiresList <em>Requires</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WebServiceElementImpl extends ServiceLandscapeElementImpl implements WebServiceElement {

    /**
	 * The cached value of the '{@link #getRequiresList() <em>Requires</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiresList()
	 * @generated
	 * @ordered
	 */
    protected EList<SoftwareElement> requires;

    /**
	 * The empty value for the '{@link #getRequires() <em>Requires</em>}' array accessor.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequires()
	 * @generated
	 * @ordered
	 */
    protected static final SoftwareElement[] REQUIRES_EEMPTY_ARRAY = new SoftwareElement[0];

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected WebServiceElementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ServiceConstructionModelPackage.Literals.WEB_SERVICE_ELEMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SoftwareElement[] getRequires() {
        if (requires == null || requires.isEmpty()) return REQUIRES_EEMPTY_ARRAY;
        BasicEList<SoftwareElement> list = (BasicEList<SoftwareElement>) requires;
        list.shrink();
        return (SoftwareElement[]) list.data();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public SoftwareElement getRequires(int index) {
        return getRequiresList().get(index);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getRequiresLength() {
        return requires == null ? 0 : requires.size();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRequires(SoftwareElement[] newRequires) {
        ((BasicEList<SoftwareElement>) getRequiresList()).setData(newRequires.length, newRequires);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRequires(int index, SoftwareElement element) {
        getRequiresList().set(index, element);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<SoftwareElement> getRequiresList() {
        if (requires == null) {
            requires = new EObjectResolvingEList<SoftwareElement>(SoftwareElement.class, this, ServiceConstructionModelPackage.WEB_SERVICE_ELEMENT__REQUIRES);
        }
        return requires;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ServiceConstructionModelPackage.WEB_SERVICE_ELEMENT__REQUIRES:
                return getRequiresList();
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
            case ServiceConstructionModelPackage.WEB_SERVICE_ELEMENT__REQUIRES:
                getRequiresList().clear();
                getRequiresList().addAll((Collection<? extends SoftwareElement>) newValue);
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
            case ServiceConstructionModelPackage.WEB_SERVICE_ELEMENT__REQUIRES:
                getRequiresList().clear();
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
            case ServiceConstructionModelPackage.WEB_SERVICE_ELEMENT__REQUIRES:
                return requires != null && !requires.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
