package tdmodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import tdmodel.ActionModelProvider;
import tdmodel.Model;
import tdmodel.TdmodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Action Model Provider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link tdmodel.impl.ActionModelProviderImpl#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ActionModelProviderImpl extends EObjectImpl implements ActionModelProvider {

    /**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
    protected Model model;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ActionModelProviderImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return TdmodelPackage.Literals.ACTION_MODEL_PROVIDER;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Model getModel() {
        return model;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetModel(Model newModel, NotificationChain msgs) {
        Model oldModel = model;
        model = newModel;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL, oldModel, newModel);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setModel(Model newModel) {
        if (newModel != model) {
            NotificationChain msgs = null;
            if (model != null) msgs = ((InternalEObject) model).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL, null, msgs);
            if (newModel != null) msgs = ((InternalEObject) newModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL, null, msgs);
            msgs = basicSetModel(newModel, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL, newModel, newModel));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL:
                return basicSetModel(null, msgs);
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
            case TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL:
                return getModel();
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
            case TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL:
                setModel((Model) newValue);
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
            case TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL:
                setModel((Model) null);
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
            case TdmodelPackage.ACTION_MODEL_PROVIDER__MODEL:
                return model != null;
        }
        return super.eIsSet(featureID);
    }
}
