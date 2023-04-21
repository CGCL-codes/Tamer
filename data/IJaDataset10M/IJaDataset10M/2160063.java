package net.sourceforge.modelintegra.core.metamodel.mimodel.impl;

import java.util.Collection;
import net.sourceforge.modelintegra.core.metamodel.mimodel.MimodelPackage;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Model;
import net.sourceforge.modelintegra.core.metamodel.mimodel.Requirement;
import net.sourceforge.modelintegra.core.metamodel.mimodel.UC;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.impl.RequirementImpl#getUc <em>Uc</em>}</li>
 *   <li>{@link net.sourceforge.modelintegra.core.metamodel.mimodel.impl.RequirementImpl#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RequirementImpl extends TraceabilityElementImpl implements Requirement {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public static final String copyright = "Test";

    /**
	 * The cached value of the '{@link #getUc() <em>Uc</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUc()
	 * @generated
	 * @ordered
	 */
    protected EList uc;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected RequirementImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EClass eStaticClass() {
        return MimodelPackage.Literals.REQUIREMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList getUc() {
        if (uc == null) {
            uc = new EObjectWithInverseResolvingEList.ManyInverse(UC.class, this, MimodelPackage.REQUIREMENT__UC, MimodelPackage.UC__REQUIREMENT);
        }
        return uc;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Model getModel() {
        if (eContainerFeatureID != MimodelPackage.REQUIREMENT__MODEL) return null;
        return (Model) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MimodelPackage.REQUIREMENT__UC:
                return ((InternalEList) getUc()).basicAdd(otherEnd, msgs);
            case MimodelPackage.REQUIREMENT__MODEL:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return eBasicSetContainer(otherEnd, MimodelPackage.REQUIREMENT__MODEL, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case MimodelPackage.REQUIREMENT__UC:
                return ((InternalEList) getUc()).basicRemove(otherEnd, msgs);
            case MimodelPackage.REQUIREMENT__MODEL:
                return eBasicSetContainer(null, MimodelPackage.REQUIREMENT__MODEL, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID) {
            case MimodelPackage.REQUIREMENT__MODEL:
                return eInternalContainer().eInverseRemove(this, MimodelPackage.MODEL__REQUIREMENT, Model.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MimodelPackage.REQUIREMENT__UC:
                return getUc();
            case MimodelPackage.REQUIREMENT__MODEL:
                return getModel();
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
            case MimodelPackage.REQUIREMENT__UC:
                getUc().clear();
                getUc().addAll((Collection) newValue);
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
            case MimodelPackage.REQUIREMENT__UC:
                getUc().clear();
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
            case MimodelPackage.REQUIREMENT__UC:
                return uc != null && !uc.isEmpty();
            case MimodelPackage.REQUIREMENT__MODEL:
                return getModel() != null;
        }
        return super.eIsSet(featureID);
    }
}
