package alfmetamodel.impl;

import alfmetamodel.AAnnotation;
import alfmetamodel.AOperation;
import alfmetamodel.AParameter;
import alfmetamodel.AlfmetamodelPackage;
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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>AOperation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#getAOperation_has_body <em>AOperation has body</em>}</li>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#getName <em>Name</em>}</li>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#getAOperation_has_param <em>AOperation has param</em>}</li>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#isIsStatic <em>Is Static</em>}</li>
 *   <li>{@link alfmetamodel.impl.AOperationImpl#getVisibility <em>Visibility</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AOperationImpl extends EObjectImpl implements AOperation {

    /**
	 * The cached value of the '{@link #getAOperation_has_body() <em>AOperation has body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAOperation_has_body()
	 * @generated
	 * @ordered
	 */
    protected AAnnotation aOperation_has_body;

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
    protected static final String RETURN_TYPE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
    protected String returnType = RETURN_TYPE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getAOperation_has_param() <em>AOperation has param</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAOperation_has_param()
	 * @generated
	 * @ordered
	 */
    protected EList<AParameter> aOperation_has_param;

    /**
	 * The default value of the '{@link #isIsStatic() <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStatic()
	 * @generated
	 * @ordered
	 */
    protected static final boolean IS_STATIC_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isIsStatic() <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStatic()
	 * @generated
	 * @ordered
	 */
    protected boolean isStatic = IS_STATIC_EDEFAULT;

    /**
	 * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
    protected static final String VISIBILITY_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
    protected String visibility = VISIBILITY_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected AOperationImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return AlfmetamodelPackage.Literals.AOPERATION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public AAnnotation getAOperation_has_body() {
        return aOperation_has_body;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetAOperation_has_body(AAnnotation newAOperation_has_body, NotificationChain msgs) {
        AAnnotation oldAOperation_has_body = aOperation_has_body;
        aOperation_has_body = newAOperation_has_body;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY, oldAOperation_has_body, newAOperation_has_body);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAOperation_has_body(AAnnotation newAOperation_has_body) {
        if (newAOperation_has_body != aOperation_has_body) {
            NotificationChain msgs = null;
            if (aOperation_has_body != null) msgs = ((InternalEObject) aOperation_has_body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY, null, msgs);
            if (newAOperation_has_body != null) msgs = ((InternalEObject) newAOperation_has_body).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY, null, msgs);
            msgs = basicSetAOperation_has_body(newAOperation_has_body, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY, newAOperation_has_body, newAOperation_has_body));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getReturnType() {
        return returnType;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setReturnType(String newReturnType) {
        String oldReturnType = returnType;
        returnType = newReturnType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__RETURN_TYPE, oldReturnType, returnType));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<AParameter> getAOperation_has_param() {
        if (aOperation_has_param == null) {
            aOperation_has_param = new EObjectContainmentEList<AParameter>(AParameter.class, this, AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM);
        }
        return aOperation_has_param;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isIsStatic() {
        return isStatic;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIsStatic(boolean newIsStatic) {
        boolean oldIsStatic = isStatic;
        isStatic = newIsStatic;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__IS_STATIC, oldIsStatic, isStatic));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getVisibility() {
        return visibility;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVisibility(String newVisibility) {
        String oldVisibility = visibility;
        visibility = newVisibility;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlfmetamodelPackage.AOPERATION__VISIBILITY, oldVisibility, visibility));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String toString() {
        if (eIsProxy()) return super.toString();
        String result = this.returnType + "  " + this.name + " () { \n\n}";
        return result;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY:
                return basicSetAOperation_has_body(null, msgs);
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM:
                return ((InternalEList<?>) getAOperation_has_param()).basicRemove(otherEnd, msgs);
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
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY:
                return getAOperation_has_body();
            case AlfmetamodelPackage.AOPERATION__NAME:
                return getName();
            case AlfmetamodelPackage.AOPERATION__RETURN_TYPE:
                return getReturnType();
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM:
                return getAOperation_has_param();
            case AlfmetamodelPackage.AOPERATION__IS_STATIC:
                return isIsStatic();
            case AlfmetamodelPackage.AOPERATION__VISIBILITY:
                return getVisibility();
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
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY:
                setAOperation_has_body((AAnnotation) newValue);
                return;
            case AlfmetamodelPackage.AOPERATION__NAME:
                setName((String) newValue);
                return;
            case AlfmetamodelPackage.AOPERATION__RETURN_TYPE:
                setReturnType((String) newValue);
                return;
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM:
                getAOperation_has_param().clear();
                getAOperation_has_param().addAll((Collection<? extends AParameter>) newValue);
                return;
            case AlfmetamodelPackage.AOPERATION__IS_STATIC:
                setIsStatic((Boolean) newValue);
                return;
            case AlfmetamodelPackage.AOPERATION__VISIBILITY:
                setVisibility((String) newValue);
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
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY:
                setAOperation_has_body((AAnnotation) null);
                return;
            case AlfmetamodelPackage.AOPERATION__NAME:
                setName(NAME_EDEFAULT);
                return;
            case AlfmetamodelPackage.AOPERATION__RETURN_TYPE:
                setReturnType(RETURN_TYPE_EDEFAULT);
                return;
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM:
                getAOperation_has_param().clear();
                return;
            case AlfmetamodelPackage.AOPERATION__IS_STATIC:
                setIsStatic(IS_STATIC_EDEFAULT);
                return;
            case AlfmetamodelPackage.AOPERATION__VISIBILITY:
                setVisibility(VISIBILITY_EDEFAULT);
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
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_BODY:
                return aOperation_has_body != null;
            case AlfmetamodelPackage.AOPERATION__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case AlfmetamodelPackage.AOPERATION__RETURN_TYPE:
                return RETURN_TYPE_EDEFAULT == null ? returnType != null : !RETURN_TYPE_EDEFAULT.equals(returnType);
            case AlfmetamodelPackage.AOPERATION__AOPERATION_HAS_PARAM:
                return aOperation_has_param != null && !aOperation_has_param.isEmpty();
            case AlfmetamodelPackage.AOPERATION__IS_STATIC:
                return isStatic != IS_STATIC_EDEFAULT;
            case AlfmetamodelPackage.AOPERATION__VISIBILITY:
                return VISIBILITY_EDEFAULT == null ? visibility != null : !VISIBILITY_EDEFAULT.equals(visibility);
        }
        return super.eIsSet(featureID);
    }
}
