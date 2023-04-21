package eu.medeia.ecore.apmm.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import eu.medeia.ecore.apmm.ApmmPackage;
import eu.medeia.ecore.apmm.AutomationComponent;
import eu.medeia.ecore.apmm.PlantComponent;
import eu.medeia.ecore.apmm.bm.impl.INamedElementImpl;
import eu.medeia.ecore.apmm.library.PlantComponentType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plant Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link eu.medeia.ecore.apmm.impl.PlantComponentImpl#getRelatedAC <em>Related AC</em>}</li>
 *   <li>{@link eu.medeia.ecore.apmm.impl.PlantComponentImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlantComponentImpl extends INamedElementImpl implements PlantComponent {

    /**
	 * The cached value of the '{@link #getRelatedAC() <em>Related AC</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelatedAC()
	 * @generated
	 * @ordered
	 */
    protected AutomationComponent relatedAC;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected PlantComponentType type;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PlantComponentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ApmmPackage.Literals.PLANT_COMPONENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationComponent getRelatedAC() {
        if (relatedAC != null && relatedAC.eIsProxy()) {
            InternalEObject oldRelatedAC = (InternalEObject) relatedAC;
            relatedAC = (AutomationComponent) eResolveProxy(oldRelatedAC);
            if (relatedAC != oldRelatedAC) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ApmmPackage.PLANT_COMPONENT__RELATED_AC, oldRelatedAC, relatedAC));
            }
        }
        return relatedAC;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AutomationComponent basicGetRelatedAC() {
        return relatedAC;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRelatedAC(AutomationComponent newRelatedAC) {
        AutomationComponent oldRelatedAC = relatedAC;
        relatedAC = newRelatedAC;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ApmmPackage.PLANT_COMPONENT__RELATED_AC, oldRelatedAC, relatedAC));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PlantComponentType getType() {
        if (type != null && type.eIsProxy()) {
            InternalEObject oldType = (InternalEObject) type;
            type = (PlantComponentType) eResolveProxy(oldType);
            if (type != oldType) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ApmmPackage.PLANT_COMPONENT__TYPE, oldType, type));
            }
        }
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PlantComponentType basicGetType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(PlantComponentType newType) {
        PlantComponentType oldType = type;
        type = newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ApmmPackage.PLANT_COMPONENT__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ApmmPackage.PLANT_COMPONENT__RELATED_AC:
                if (resolve) return getRelatedAC();
                return basicGetRelatedAC();
            case ApmmPackage.PLANT_COMPONENT__TYPE:
                if (resolve) return getType();
                return basicGetType();
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
            case ApmmPackage.PLANT_COMPONENT__RELATED_AC:
                setRelatedAC((AutomationComponent) newValue);
                return;
            case ApmmPackage.PLANT_COMPONENT__TYPE:
                setType((PlantComponentType) newValue);
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
            case ApmmPackage.PLANT_COMPONENT__RELATED_AC:
                setRelatedAC((AutomationComponent) null);
                return;
            case ApmmPackage.PLANT_COMPONENT__TYPE:
                setType((PlantComponentType) null);
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
            case ApmmPackage.PLANT_COMPONENT__RELATED_AC:
                return relatedAC != null;
            case ApmmPackage.PLANT_COMPONENT__TYPE:
                return type != null;
        }
        return super.eIsSet(featureID);
    }
}
