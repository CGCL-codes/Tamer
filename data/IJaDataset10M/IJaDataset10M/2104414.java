package org.modelversioning.core.conditions.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.modelversioning.core.conditions.Condition;
import org.modelversioning.core.conditions.ConditionsPackage;
import org.modelversioning.core.conditions.EvaluationResult;
import org.modelversioning.core.conditions.EvaluationStatus;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Evaluation Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getException <em>Exception</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getEvaluator <em>Evaluator</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getSubResults <em>Sub Results</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getParentResult <em>Parent Result</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getFailedCondition <em>Failed Condition</em>}</li>
 *   <li>{@link org.modelversioning.core.conditions.impl.EvaluationResultImpl#getFailedCandidate <em>Failed Candidate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EvaluationResultImpl extends EObjectImpl implements EvaluationResult {

    /**
	 * The default value of the '{@link #getMessage() <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessage()
	 * @generated
	 * @ordered
	 */
    protected static final String MESSAGE_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getMessage() <em>Message</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessage()
	 * @generated
	 * @ordered
	 */
    protected String message = MESSAGE_EDEFAULT;

    /**
	 * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
    protected static final EvaluationStatus STATUS_EDEFAULT = EvaluationStatus.SATISFIED;

    /**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
    protected EvaluationStatus status = STATUS_EDEFAULT;

    /**
	 * The default value of the '{@link #getException() <em>Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getException()
	 * @generated NOT
	 * @ordered
	 */
    protected static final Throwable EXCEPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getException() <em>Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getException()
	 * @generated NOT
	 * @ordered
	 */
    protected Throwable exception = EXCEPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getEvaluator() <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvaluator()
	 * @generated
	 * @ordered
	 */
    protected static final String EVALUATOR_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getEvaluator() <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvaluator()
	 * @generated
	 * @ordered
	 */
    protected String evaluator = EVALUATOR_EDEFAULT;

    /**
	 * The cached value of the '{@link #getSubResults() <em>Sub Results</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubResults()
	 * @generated
	 * @ordered
	 */
    protected EList<EvaluationResult> subResults;

    /**
	 * The cached value of the '{@link #getFailedCondition() <em>Failed Condition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCondition()
	 * @generated
	 * @ordered
	 */
    protected Condition failedCondition;

    /**
	 * The cached value of the '{@link #getFailedCandidate() <em>Failed Candidate</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCandidate()
	 * @generated
	 * @ordered
	 */
    protected EObject failedCandidate;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected EvaluationResultImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return ConditionsPackage.Literals.EVALUATION_RESULT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMessage(String newMessage) {
        String oldMessage = message;
        message = newMessage;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__MESSAGE, oldMessage, message));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public EvaluationStatus getStatus() {
        if (!this.isMultiResult()) {
            return status;
        } else {
            boolean isSatisfied = true;
            boolean isError = false;
            for (EvaluationResult subResult : this.getSubResults()) {
                switch(subResult.getStatus()) {
                    case UNSATISFIED:
                        isSatisfied = false;
                        break;
                    case ERROR:
                        isSatisfied = false;
                        isError = true;
                        break;
                    default:
                        break;
                }
            }
            if (isError) {
                return EvaluationStatus.ERROR;
            }
            if (!isSatisfied) {
                return EvaluationStatus.UNSATISFIED;
            } else {
                return EvaluationStatus.SATISFIED;
            }
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStatus(EvaluationStatus newStatus) {
        EvaluationStatus oldStatus = status;
        status = newStatus == null ? STATUS_EDEFAULT : newStatus;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__STATUS, oldStatus, status));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public Throwable getException() {
        return exception;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public void setException(Throwable newException) {
        Object oldException = exception;
        exception = newException;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__EXCEPTION, oldException, exception));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getEvaluator() {
        return evaluator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setEvaluator(String newEvaluator) {
        String oldEvaluator = evaluator;
        evaluator = newEvaluator;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__EVALUATOR, oldEvaluator, evaluator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<EvaluationResult> getSubResults() {
        if (subResults == null) {
            subResults = new EObjectContainmentWithInverseEList<EvaluationResult>(EvaluationResult.class, this, ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS, ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT);
        }
        return subResults;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EvaluationResult getParentResult() {
        if (eContainerFeatureID() != ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT) return null;
        return (EvaluationResult) eContainer();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public NotificationChain basicSetParentResult(EvaluationResult newParentResult, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newParentResult, ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT, msgs);
        return msgs;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setParentResult(EvaluationResult newParentResult) {
        if (newParentResult != eInternalContainer() || (eContainerFeatureID() != ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT && newParentResult != null)) {
            if (EcoreUtil.isAncestor(this, newParentResult)) throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
            if (newParentResult != null) msgs = ((InternalEObject) newParentResult).eInverseAdd(this, ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS, EvaluationResult.class, msgs);
            msgs = basicSetParentResult(newParentResult, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT, newParentResult, newParentResult));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Condition getFailedCondition() {
        if (failedCondition != null && failedCondition.eIsProxy()) {
            InternalEObject oldFailedCondition = (InternalEObject) failedCondition;
            failedCondition = (Condition) eResolveProxy(oldFailedCondition);
            if (failedCondition != oldFailedCondition) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConditionsPackage.EVALUATION_RESULT__FAILED_CONDITION, oldFailedCondition, failedCondition));
            }
        }
        return failedCondition;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Condition basicGetFailedCondition() {
        return failedCondition;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFailedCondition(Condition newFailedCondition) {
        Condition oldFailedCondition = failedCondition;
        failedCondition = newFailedCondition;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__FAILED_CONDITION, oldFailedCondition, failedCondition));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getFailedCandidate() {
        if (failedCandidate != null && failedCandidate.eIsProxy()) {
            InternalEObject oldFailedCandidate = (InternalEObject) failedCandidate;
            failedCandidate = eResolveProxy(oldFailedCandidate);
            if (failedCandidate != oldFailedCandidate) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, ConditionsPackage.EVALUATION_RESULT__FAILED_CANDIDATE, oldFailedCandidate, failedCandidate));
            }
        }
        return failedCandidate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject basicGetFailedCandidate() {
        return failedCandidate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setFailedCandidate(EObject newFailedCandidate) {
        EObject oldFailedCandidate = failedCandidate;
        failedCandidate = newFailedCandidate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ConditionsPackage.EVALUATION_RESULT__FAILED_CANDIDATE, oldFailedCandidate, failedCandidate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public boolean isMultiResult() {
        if (this.getSubResults().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public boolean isOK() {
        if (EvaluationStatus.SATISFIED.equals(this.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getSubResults()).basicAdd(otherEnd, msgs);
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                if (eInternalContainer() != null) msgs = eBasicRemoveFromContainer(msgs);
                return basicSetParentResult((EvaluationResult) otherEnd, msgs);
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
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                return ((InternalEList<?>) getSubResults()).basicRemove(otherEnd, msgs);
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                return basicSetParentResult(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch(eContainerFeatureID()) {
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                return eInternalContainer().eInverseRemove(this, ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS, EvaluationResult.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case ConditionsPackage.EVALUATION_RESULT__MESSAGE:
                return getMessage();
            case ConditionsPackage.EVALUATION_RESULT__STATUS:
                return getStatus();
            case ConditionsPackage.EVALUATION_RESULT__EXCEPTION:
                return getException();
            case ConditionsPackage.EVALUATION_RESULT__EVALUATOR:
                return getEvaluator();
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                return getSubResults();
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                return getParentResult();
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CONDITION:
                if (resolve) return getFailedCondition();
                return basicGetFailedCondition();
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CANDIDATE:
                if (resolve) return getFailedCandidate();
                return basicGetFailedCandidate();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case ConditionsPackage.EVALUATION_RESULT__MESSAGE:
                setMessage((String) newValue);
                return;
            case ConditionsPackage.EVALUATION_RESULT__STATUS:
                setStatus((EvaluationStatus) newValue);
                return;
            case ConditionsPackage.EVALUATION_RESULT__EXCEPTION:
                setException((Throwable) newValue);
                return;
            case ConditionsPackage.EVALUATION_RESULT__EVALUATOR:
                setEvaluator((String) newValue);
                return;
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                getSubResults().clear();
                getSubResults().addAll((Collection<? extends EvaluationResult>) newValue);
                return;
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                setParentResult((EvaluationResult) newValue);
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
            case ConditionsPackage.EVALUATION_RESULT__MESSAGE:
                setMessage(MESSAGE_EDEFAULT);
                return;
            case ConditionsPackage.EVALUATION_RESULT__STATUS:
                setStatus(STATUS_EDEFAULT);
                return;
            case ConditionsPackage.EVALUATION_RESULT__EXCEPTION:
                setException(EXCEPTION_EDEFAULT);
                return;
            case ConditionsPackage.EVALUATION_RESULT__EVALUATOR:
                setEvaluator(EVALUATOR_EDEFAULT);
                return;
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                getSubResults().clear();
                return;
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                setParentResult((EvaluationResult) null);
                return;
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CONDITION:
                setFailedCondition((Condition) null);
                return;
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CANDIDATE:
                setFailedCandidate((EObject) null);
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
            case ConditionsPackage.EVALUATION_RESULT__MESSAGE:
                return MESSAGE_EDEFAULT == null ? message != null : !MESSAGE_EDEFAULT.equals(message);
            case ConditionsPackage.EVALUATION_RESULT__STATUS:
                return status != STATUS_EDEFAULT;
            case ConditionsPackage.EVALUATION_RESULT__EXCEPTION:
                return EXCEPTION_EDEFAULT == null ? exception != null : !EXCEPTION_EDEFAULT.equals(exception);
            case ConditionsPackage.EVALUATION_RESULT__EVALUATOR:
                return EVALUATOR_EDEFAULT == null ? evaluator != null : !EVALUATOR_EDEFAULT.equals(evaluator);
            case ConditionsPackage.EVALUATION_RESULT__SUB_RESULTS:
                return subResults != null && !subResults.isEmpty();
            case ConditionsPackage.EVALUATION_RESULT__PARENT_RESULT:
                return getParentResult() != null;
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CONDITION:
                return failedCondition != null;
            case ConditionsPackage.EVALUATION_RESULT__FAILED_CANDIDATE:
                return failedCandidate != null;
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
        result.append(" (message: ");
        result.append(message);
        result.append(", status: ");
        result.append(status);
        result.append(", exception: ");
        result.append(exception);
        result.append(", evaluator: ");
        result.append(evaluator);
        result.append(')');
        return result.toString();
    }
}
