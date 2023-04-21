package ms.jasim.model.event.impl;

import ms.jasim.framework.IJasimAppContext;
import ms.jasim.model.event.EventPackage;
import ms.jasim.model.JasimModel;
import ms.jasim.model.ModelPackage;
import ms.jasim.model.event.Event;
import ms.jasim.model.event.ModifiablePredicate;
import ms.jasim.model.event.ModificationType;
import ms.jasim.model.event.ModifyModelReaction;
import ms.jasim.model.items.Item;
import ms.jasim.pddl.PddlPredicate;
import ms.utils.INamedItem;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Modify Model Reaction</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ms.jasim.model.event.impl.ModifyModelReactionImpl#getType <em>Type</em>}</li>
 *   <li>{@link ms.jasim.model.event.impl.ModifyModelReactionImpl#getPredicate <em>Predicate</em>}</li>
 *   <li>{@link ms.jasim.model.event.impl.ModifyModelReactionImpl#getPredicateArg <em>Predicate Arg</em>}</li>
 *   <li>{@link ms.jasim.model.event.impl.ModifyModelReactionImpl#isNegative <em>Negative</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModifyModelReactionImpl extends EventReactionImpl implements ModifyModelReaction {

    /**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected static final ModificationType TYPE_EDEFAULT = ModificationType.ADD_FACT;

    /**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
    protected ModificationType type = TYPE_EDEFAULT;

    /**
	 * The default value of the '{@link #getPredicate() <em>Predicate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicate()
	 * @generated
	 * @ordered
	 */
    protected static final ModifiablePredicate PREDICATE_EDEFAULT = ModifiablePredicate.PR_SATISFIES;

    /**
	 * The cached value of the '{@link #getPredicate() <em>Predicate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicate()
	 * @generated
	 * @ordered
	 */
    protected ModifiablePredicate predicate = PREDICATE_EDEFAULT;

    /**
	 * The default value of the '{@link #getPredicateArg() <em>Predicate Arg</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicateArg()
	 * @generated
	 * @ordered
	 */
    protected static final String PREDICATE_ARG_EDEFAULT = "{Actor} {Goal}";

    /**
	 * The cached value of the '{@link #getPredicateArg() <em>Predicate Arg</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPredicateArg()
	 * @generated
	 * @ordered
	 */
    protected String predicateArg = PREDICATE_ARG_EDEFAULT;

    /**
	 * The default value of the '{@link #isNegative() <em>Negative</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNegative()
	 * @generated
	 * @ordered
	 */
    protected static final boolean NEGATIVE_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isNegative() <em>Negative</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isNegative()
	 * @generated
	 * @ordered
	 */
    protected boolean negative = NEGATIVE_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected ModifyModelReactionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return EventPackage.Literals.MODIFY_MODEL_REACTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModificationType getType() {
        return type;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setType(ModificationType newType) {
        ModificationType oldType = type;
        type = newType == null ? TYPE_EDEFAULT : newType;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EventPackage.MODIFY_MODEL_REACTION__TYPE, oldType, type));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ModifiablePredicate getPredicate() {
        return predicate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPredicate(ModifiablePredicate newPredicate) {
        ModifiablePredicate oldPredicate = predicate;
        predicate = newPredicate == null ? PREDICATE_EDEFAULT : newPredicate;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EventPackage.MODIFY_MODEL_REACTION__PREDICATE, oldPredicate, predicate));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getPredicateArg() {
        return predicateArg;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setPredicateArg(String newPredicateArg) {
        String oldPredicateArg = predicateArg;
        predicateArg = newPredicateArg;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EventPackage.MODIFY_MODEL_REACTION__PREDICATE_ARG, oldPredicateArg, predicateArg));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isNegative() {
        return negative;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setNegative(boolean newNegative) {
        boolean oldNegative = negative;
        negative = newNegative;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, EventPackage.MODIFY_MODEL_REACTION__NEGATIVE, oldNegative, negative));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
    public PddlPredicate getPddlPredicate(IJasimAppContext context) {
        PddlPredicate pred = new PddlPredicate();
        pred.setNegative(isNegative());
        pred.setFunctor(getPredicate().getName());
        String arg = getPredicateArg();
        if (arg != null) {
            for (Item param : this.getEvent().getParameters()) {
                Object value = param.getValue(context);
                String valueLabel = (value instanceof INamedItem) ? ((INamedItem) value).getName() : value != null ? value.toString() : "?";
                arg = arg.replaceAll("\\{" + param.getName() + "\\}", valueLabel);
            }
            pred.getArgs().add(arg);
        }
        return pred;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case EventPackage.MODIFY_MODEL_REACTION__TYPE:
                return getType();
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE:
                return getPredicate();
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE_ARG:
                return getPredicateArg();
            case EventPackage.MODIFY_MODEL_REACTION__NEGATIVE:
                return isNegative();
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
            case EventPackage.MODIFY_MODEL_REACTION__TYPE:
                setType((ModificationType) newValue);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE:
                setPredicate((ModifiablePredicate) newValue);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE_ARG:
                setPredicateArg((String) newValue);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__NEGATIVE:
                setNegative((Boolean) newValue);
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
            case EventPackage.MODIFY_MODEL_REACTION__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE:
                setPredicate(PREDICATE_EDEFAULT);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE_ARG:
                setPredicateArg(PREDICATE_ARG_EDEFAULT);
                return;
            case EventPackage.MODIFY_MODEL_REACTION__NEGATIVE:
                setNegative(NEGATIVE_EDEFAULT);
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
            case EventPackage.MODIFY_MODEL_REACTION__TYPE:
                return type != TYPE_EDEFAULT;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE:
                return predicate != PREDICATE_EDEFAULT;
            case EventPackage.MODIFY_MODEL_REACTION__PREDICATE_ARG:
                return PREDICATE_ARG_EDEFAULT == null ? predicateArg != null : !PREDICATE_ARG_EDEFAULT.equals(predicateArg);
            case EventPackage.MODIFY_MODEL_REACTION__NEGATIVE:
                return negative != NEGATIVE_EDEFAULT;
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
        result.append(" (Type: ");
        result.append(type);
        result.append(", Predicate: ");
        result.append(predicate);
        result.append(", PredicateArg: ");
        result.append(predicateArg);
        result.append(", Negative: ");
        result.append(negative);
        result.append(')');
        return result.toString();
    }

    @Override
    public boolean react(IJasimAppContext context, JasimModel model, Event event) {
        PddlPredicate pred = getPddlPredicate(context);
        model.getPostProcessCommand().add(new Object[] { getType(), pred });
        return false;
    }
}
