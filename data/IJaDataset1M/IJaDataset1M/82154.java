package com.safi.core.actionstep.impl;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.safi.core.actionstep.ActionStepPackage;
import com.safi.core.actionstep.DBQueryParamId;
import com.safi.core.actionstep.DynamicValue;
import com.safi.core.actionstep.QueryParamMapping;
import com.safi.db.SQLDataType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Query Param Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.core.actionstep.impl.QueryParamMappingImpl#getQueryParam <em>Query Param</em>}</li>
 *   <li>{@link com.safi.core.actionstep.impl.QueryParamMappingImpl#getSetAsDatatype <em>Set As Datatype</em>}</li>
 *   <li>{@link com.safi.core.actionstep.impl.QueryParamMappingImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QueryParamMappingImpl extends ItemImpl implements QueryParamMapping {

    /**
	 * The cached value of the '{@link #getQueryParam() <em>Query Param</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getQueryParam()
	 * @generated
	 * @ordered
	 */
    protected DBQueryParamId queryParam;

    /**
	 * The default value of the '{@link #getSetAsDatatype() <em>Set As Datatype</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSetAsDatatype()
	 * @generated
	 * @ordered
	 */
    protected static final SQLDataType SET_AS_DATATYPE_EDEFAULT = SQLDataType.TEXT;

    /**
	 * The cached value of the '{@link #getSetAsDatatype() <em>Set As Datatype</em>}' attribute.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getSetAsDatatype()
	 * @generated
	 * @ordered
	 */
    protected SQLDataType setAsDatatype = SET_AS_DATATYPE_EDEFAULT;

    /**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
    protected DynamicValue value;

    protected MappingAdapter adapter = new MappingAdapter();

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected QueryParamMappingImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ActionStepPackage.eINSTANCE.getQueryParamMapping();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public DBQueryParamId getQueryParam() {
        return queryParam;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetQueryParam(DBQueryParamId newQueryParam, NotificationChain msgs) {
        DBQueryParamId oldQueryParam = queryParam;
        queryParam = newQueryParam;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM, oldQueryParam, newQueryParam);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
    public void setQueryParam(DBQueryParamId newQueryParam) {
        if (newQueryParam != queryParam) {
            NotificationChain msgs = null;
            if (queryParam != null) {
                this.eAdapters().remove(newQueryParam);
                msgs = ((InternalEObject) queryParam).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM, null, msgs);
                queryParam.eAdapters().remove(adapter);
            }
            if (newQueryParam != null) {
                msgs = ((InternalEObject) newQueryParam).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM, null, msgs);
                newQueryParam.eAdapters().add(adapter);
                updateLabelText(newQueryParam);
            } else setLabelText("");
            msgs = basicSetQueryParam(newQueryParam, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM, newQueryParam, newQueryParam));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public SQLDataType getSetAsDatatype() {
        return setAsDatatype;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setSetAsDatatype(SQLDataType newSetAsDatatype) {
        SQLDataType oldSetAsDatatype = setAsDatatype;
        setAsDatatype = newSetAsDatatype == null ? SET_AS_DATATYPE_EDEFAULT : newSetAsDatatype;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionStepPackage.QUERY_PARAM_MAPPING__SET_AS_DATATYPE, oldSetAsDatatype, setAsDatatype));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public DynamicValue getValue() {
        return value;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetValue(DynamicValue newValue, NotificationChain msgs) {
        DynamicValue oldValue = value;
        value = newValue;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionStepPackage.QUERY_PARAM_MAPPING__VALUE, oldValue, newValue);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(DynamicValue newValue) {
        if (newValue != value) {
            NotificationChain msgs = null;
            if (value != null) msgs = ((InternalEObject) value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.QUERY_PARAM_MAPPING__VALUE, null, msgs);
            if (newValue != null) msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.QUERY_PARAM_MAPPING__VALUE, null, msgs);
            msgs = basicSetValue(newValue, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionStepPackage.QUERY_PARAM_MAPPING__VALUE, newValue, newValue));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM:
                return basicSetQueryParam(null, msgs);
            case ActionStepPackage.QUERY_PARAM_MAPPING__VALUE:
                return basicSetValue(null, msgs);
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
            case ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM:
                return getQueryParam();
            case ActionStepPackage.QUERY_PARAM_MAPPING__SET_AS_DATATYPE:
                return getSetAsDatatype();
            case ActionStepPackage.QUERY_PARAM_MAPPING__VALUE:
                return getValue();
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
            case ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM:
                setQueryParam((DBQueryParamId) newValue);
                return;
            case ActionStepPackage.QUERY_PARAM_MAPPING__SET_AS_DATATYPE:
                setSetAsDatatype((SQLDataType) newValue);
                return;
            case ActionStepPackage.QUERY_PARAM_MAPPING__VALUE:
                setValue((DynamicValue) newValue);
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
            case ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM:
                setQueryParam((DBQueryParamId) null);
                return;
            case ActionStepPackage.QUERY_PARAM_MAPPING__SET_AS_DATATYPE:
                setSetAsDatatype(SET_AS_DATATYPE_EDEFAULT);
                return;
            case ActionStepPackage.QUERY_PARAM_MAPPING__VALUE:
                setValue((DynamicValue) null);
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
            case ActionStepPackage.QUERY_PARAM_MAPPING__QUERY_PARAM:
                return queryParam != null;
            case ActionStepPackage.QUERY_PARAM_MAPPING__SET_AS_DATATYPE:
                return setAsDatatype != SET_AS_DATATYPE_EDEFAULT;
            case ActionStepPackage.QUERY_PARAM_MAPPING__VALUE:
                return value != null;
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
        result.append(" (setAsDatatype: ");
        result.append(setAsDatatype);
        result.append(')');
        return result.toString();
    }

    private void updateLabelText(DBQueryParamId queryParam) {
        String text = null;
        if (queryParam == null) text = "-none-"; else {
            String id = queryParam.getId();
            if (StringUtils.isBlank(id)) text = "-none-"; else text = id.substring(id.lastIndexOf('.') + 1, id.length());
            if (text.length() > 15) {
                text = text.substring(0, 12) + "...";
            }
        }
        setLabelText(text);
    }

    private class MappingAdapter implements Adapter {

        private Notifier myTarger;

        public Notifier getTarget() {
            return myTarger;
        }

        public boolean isAdapterForType(Object type) {
            return false;
        }

        public void notifyChanged(Notification notification) {
            updateLabelText((DBQueryParamId) notification.getNotifier());
        }

        public void setTarget(Notifier newTarget) {
            myTarger = newTarget;
        }
    }
}
