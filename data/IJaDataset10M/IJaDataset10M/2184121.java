package com.safi.core.actionstep.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import com.safi.core.actionstep.ActionStepException;
import com.safi.core.actionstep.ActionStepFactory;
import com.safi.core.actionstep.ActionStepPackage;
import com.safi.core.actionstep.DynamicValue;
import com.safi.core.actionstep.IfThen;
import com.safi.core.actionstep.Output;
import com.safi.core.actionstep.OutputType;
import com.safi.db.util.VariableTranslator;
import com.safi.core.saflet.SafletContext;
import com.safi.db.VariableType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Then</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.safi.core.actionstep.impl.IfThenImpl#getBooleanExpression <em>Boolean Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IfThenImpl extends ActionStepImpl implements IfThen {

    /**
	 * The cached value of the '{@link #getBooleanExpression() <em>Boolean Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @see #getBooleanExpression()
	 * @generated
	 * @ordered
	 */
    protected DynamicValue booleanExpression;

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    protected IfThenImpl() {
        super();
    }

    @Override
    public void beginProcessing(SafletContext context) throws ActionStepException {
        super.beginProcessing(context);
        int idx = 0;
        try {
            Object result = resolveDynamicValue(booleanExpression, context);
            Boolean bool = (Boolean) VariableTranslator.translateValue(VariableType.BOOLEAN, result);
            if (bool) {
                idx = 1;
            }
        } catch (Exception e) {
            handleException(context, e);
            return;
        }
        handleSuccess(context, idx);
    }

    @Override
    public void createDefaultOutputs() {
        Output o = ActionStepFactory.eINSTANCE.createOutput();
        o.setOutputType(OutputType.ERROR);
        o.setName("false");
        setErrorOutput(o);
        getOutputs().add(o);
        o = ActionStepFactory.eINSTANCE.createOutput();
        o.setOutputType(OutputType.DEFAULT);
        o.setName("true");
        setDefaultOutput(o);
        getOutputs().add(o);
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ActionStepPackage.eINSTANCE.getIfThen();
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public DynamicValue getBooleanExpression() {
        return booleanExpression;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetBooleanExpression(DynamicValue newBooleanExpression, NotificationChain msgs) {
        DynamicValue oldBooleanExpression = booleanExpression;
        booleanExpression = newBooleanExpression;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION, oldBooleanExpression, newBooleanExpression);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBooleanExpression(DynamicValue newBooleanExpression) {
        if (newBooleanExpression != booleanExpression) {
            NotificationChain msgs = null;
            if (booleanExpression != null) msgs = ((InternalEObject) booleanExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION, null, msgs);
            if (newBooleanExpression != null) msgs = ((InternalEObject) newBooleanExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION, null, msgs);
            msgs = basicSetBooleanExpression(newBooleanExpression, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION, newBooleanExpression, newBooleanExpression));
    }

    /**
	 * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION:
                return basicSetBooleanExpression(null, msgs);
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
            case ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION:
                return getBooleanExpression();
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
            case ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION:
                setBooleanExpression((DynamicValue) newValue);
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
            case ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION:
                setBooleanExpression((DynamicValue) null);
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
            case ActionStepPackage.IF_THEN__BOOLEAN_EXPRESSION:
                return booleanExpression != null;
        }
        return super.eIsSet(featureID);
    }
}
