package model.impl;

import model.Association;
import model.AssociationClass;
import model.ModelPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Association Class</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.impl.AssociationClassImpl#getAssociation <em>Association</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AssociationClassImpl extends ClassImpl implements AssociationClass {

    /**
	 * The cached value of the '{@link #getAssociation() <em>Association</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssociation()
	 * @generated
	 * @ordered
	 */
    protected Association association;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AssociationClassImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.ASSOCIATION_CLASS;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Association getAssociation() {
        if (association != null && association.eIsProxy()) {
            InternalEObject oldAssociation = (InternalEObject) association;
            association = (Association) eResolveProxy(oldAssociation);
            if (association != oldAssociation) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.ASSOCIATION_CLASS__ASSOCIATION, oldAssociation, association));
            }
        }
        return association;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Association basicGetAssociation() {
        return association;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAssociation(Association newAssociation, NotificationChain msgs) {
        Association oldAssociation = association;
        association = newAssociation;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.ASSOCIATION_CLASS__ASSOCIATION, oldAssociation, newAssociation);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAssociation(Association newAssociation) {
        if (newAssociation != association) {
            NotificationChain msgs = null;
            if (association != null) msgs = ((InternalEObject) association).eInverseRemove(this, ModelPackage.ASSOCIATION__ASSOCIATION_CLASS, Association.class, msgs);
            if (newAssociation != null) msgs = ((InternalEObject) newAssociation).eInverseAdd(this, ModelPackage.ASSOCIATION__ASSOCIATION_CLASS, Association.class, msgs);
            msgs = basicSetAssociation(newAssociation, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ASSOCIATION_CLASS__ASSOCIATION, newAssociation, newAssociation));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                if (association != null) msgs = ((InternalEObject) association).eInverseRemove(this, ModelPackage.ASSOCIATION__ASSOCIATION_CLASS, Association.class, msgs);
                return basicSetAssociation((Association) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                return basicSetAssociation(null, msgs);
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
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                if (resolve) return getAssociation();
                return basicGetAssociation();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                setAssociation((Association) newValue);
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
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                setAssociation((Association) null);
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
            case ModelPackage.ASSOCIATION_CLASS__ASSOCIATION:
                return association != null;
        }
        return super.eIsSet(featureID);
    }
}
