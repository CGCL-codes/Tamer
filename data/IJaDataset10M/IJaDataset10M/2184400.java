package org.eclipse.uml2.uml.internal.impl;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.uml2.common.util.CacheAdapter;
import org.eclipse.uml2.common.util.DerivedUnionEObjectEList;
import org.eclipse.uml2.uml.CombinedFragment;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Gate;
import org.eclipse.uml2.uml.GeneralOrdering;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.InteractionOperand;
import org.eclipse.uml2.uml.InteractionOperatorKind;
import org.eclipse.uml2.uml.Lifeline;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.operations.CombinedFragmentOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Combined Fragment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl#getOwnedElements <em>Owned Element</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl#getInteractionOperator <em>Interaction Operator</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl#getOperands <em>Operand</em>}</li>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.CombinedFragmentImpl#getCfragmentGates <em>Cfragment Gate</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CombinedFragmentImpl extends InteractionFragmentImpl implements CombinedFragment {

    /**
	 * The default value of the '{@link #getInteractionOperator() <em>Interaction Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInteractionOperator()
	 * @generated
	 * @ordered
	 */
    protected static final InteractionOperatorKind INTERACTION_OPERATOR_EDEFAULT = InteractionOperatorKind.SEQ_LITERAL;

    /**
	 * The offset of the flags representing the value of the '{@link #getInteractionOperator() <em>Interaction Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected static final int INTERACTION_OPERATOR_EFLAG_OFFSET = 12;

    /**
	 * The flags representing the default value of the '{@link #getInteractionOperator() <em>Interaction Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    protected static final int INTERACTION_OPERATOR_EFLAG_DEFAULT = INTERACTION_OPERATOR_EDEFAULT.ordinal() << INTERACTION_OPERATOR_EFLAG_OFFSET;

    /**
	 * The array of enumeration values for '{@link InteractionOperatorKind Interaction Operator Kind}'
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    private static final InteractionOperatorKind[] INTERACTION_OPERATOR_EFLAG_VALUES = InteractionOperatorKind.values();

    /**
	 * The flags representing the value of the '{@link #getInteractionOperator() <em>Interaction Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInteractionOperator()
	 * @generated
	 * @ordered
	 */
    protected static final int INTERACTION_OPERATOR_EFLAG = 0xf << INTERACTION_OPERATOR_EFLAG_OFFSET;

    /**
	 * The cached value of the '{@link #getOperands() <em>Operand</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperands()
	 * @generated
	 * @ordered
	 */
    protected EList<InteractionOperand> operands;

    /**
	 * The cached value of the '{@link #getCfragmentGates() <em>Cfragment Gate</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCfragmentGates()
	 * @generated
	 * @ordered
	 */
    protected EList<Gate> cfragmentGates;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CombinedFragmentImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.COMBINED_FRAGMENT;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public EList<Element> getOwnedElements() {
        CacheAdapter cache = getCacheAdapter();
        if (cache != null) {
            Resource eResource = eResource();
            @SuppressWarnings("unchecked") EList<Element> ownedElements = (EList<Element>) cache.get(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT);
            if (ownedElements == null) {
                cache.put(eResource, this, UMLPackage.Literals.ELEMENT__OWNED_ELEMENT, ownedElements = new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.COMBINED_FRAGMENT__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS));
            }
            return ownedElements;
        }
        return new DerivedUnionEObjectEList<Element>(Element.class, this, UMLPackage.COMBINED_FRAGMENT__OWNED_ELEMENT, OWNED_ELEMENT_ESUBSETS);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InteractionOperatorKind getInteractionOperator() {
        return INTERACTION_OPERATOR_EFLAG_VALUES[(eFlags & INTERACTION_OPERATOR_EFLAG) >>> INTERACTION_OPERATOR_EFLAG_OFFSET];
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInteractionOperator(InteractionOperatorKind newInteractionOperator) {
        InteractionOperatorKind oldInteractionOperator = INTERACTION_OPERATOR_EFLAG_VALUES[(eFlags & INTERACTION_OPERATOR_EFLAG) >>> INTERACTION_OPERATOR_EFLAG_OFFSET];
        if (newInteractionOperator == null) newInteractionOperator = INTERACTION_OPERATOR_EDEFAULT;
        eFlags = eFlags & ~INTERACTION_OPERATOR_EFLAG | newInteractionOperator.ordinal() << INTERACTION_OPERATOR_EFLAG_OFFSET;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.COMBINED_FRAGMENT__INTERACTION_OPERATOR, oldInteractionOperator, newInteractionOperator));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<InteractionOperand> getOperands() {
        if (operands == null) {
            operands = new EObjectContainmentEList.Resolving<InteractionOperand>(InteractionOperand.class, this, UMLPackage.COMBINED_FRAGMENT__OPERAND);
        }
        return operands;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InteractionOperand createOperand(String name) {
        InteractionOperand newOperand = (InteractionOperand) create(UMLPackage.Literals.INTERACTION_OPERAND);
        getOperands().add(newOperand);
        if (name != null) newOperand.setName(name);
        return newOperand;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InteractionOperand getOperand(String name) {
        return getOperand(name, false, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public InteractionOperand getOperand(String name, boolean ignoreCase, boolean createOnDemand) {
        operandLoop: for (InteractionOperand operand : getOperands()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(operand.getName()) : name.equals(operand.getName()))) continue operandLoop;
            return operand;
        }
        return createOnDemand ? createOperand(name) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Gate> getCfragmentGates() {
        if (cfragmentGates == null) {
            cfragmentGates = new EObjectContainmentEList.Resolving<Gate>(Gate.class, this, UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE);
        }
        return cfragmentGates;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gate createCfragmentGate(String name) {
        Gate newCfragmentGate = (Gate) create(UMLPackage.Literals.GATE);
        getCfragmentGates().add(newCfragmentGate);
        if (name != null) newCfragmentGate.setName(name);
        return newCfragmentGate;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gate getCfragmentGate(String name) {
        return getCfragmentGate(name, false, false);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Gate getCfragmentGate(String name, boolean ignoreCase, boolean createOnDemand) {
        cfragmentGateLoop: for (Gate cfragmentGate : getCfragmentGates()) {
            if (name != null && !(ignoreCase ? name.equalsIgnoreCase(cfragmentGate.getName()) : name.equals(cfragmentGate.getName()))) continue cfragmentGateLoop;
            return cfragmentGate;
        }
        return createOnDemand ? createCfragmentGate(name) : null;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateOptLoopBreakNeg(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return CombinedFragmentOperations.validateOptLoopBreakNeg(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateMinintAndMaxint(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return CombinedFragmentOperations.validateMinintAndMaxint(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateBreak(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return CombinedFragmentOperations.validateBreak(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateConsiderAndIgnore(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return CombinedFragmentOperations.validateConsiderAndIgnore(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case UMLPackage.COMBINED_FRAGMENT__EANNOTATIONS:
                return ((InternalEList<?>) getEAnnotations()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT:
                return ((InternalEList<?>) getOwnedComments()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__CLIENT_DEPENDENCY:
                return ((InternalEList<?>) getClientDependencies()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION:
                return basicSetNameExpression(null, msgs);
            case UMLPackage.COMBINED_FRAGMENT__COVERED:
                return ((InternalEList<?>) getCovereds()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING:
                return ((InternalEList<?>) getGeneralOrderings()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_INTERACTION:
                return basicSetEnclosingInteraction(null, msgs);
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_OPERAND:
                return basicSetEnclosingOperand(null, msgs);
            case UMLPackage.COMBINED_FRAGMENT__OPERAND:
                return ((InternalEList<?>) getOperands()).basicRemove(otherEnd, msgs);
            case UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE:
                return ((InternalEList<?>) getCfragmentGates()).basicRemove(otherEnd, msgs);
        }
        return eDynamicInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLPackage.COMBINED_FRAGMENT__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.COMBINED_FRAGMENT__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.COMBINED_FRAGMENT__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.COMBINED_FRAGMENT__NAME:
                return getName();
            case UMLPackage.COMBINED_FRAGMENT__VISIBILITY:
                return getVisibility();
            case UMLPackage.COMBINED_FRAGMENT__QUALIFIED_NAME:
                return getQualifiedName();
            case UMLPackage.COMBINED_FRAGMENT__CLIENT_DEPENDENCY:
                return getClientDependencies();
            case UMLPackage.COMBINED_FRAGMENT__NAMESPACE:
                if (resolve) return getNamespace();
                return basicGetNamespace();
            case UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION:
                if (resolve) return getNameExpression();
                return basicGetNameExpression();
            case UMLPackage.COMBINED_FRAGMENT__COVERED:
                return getCovereds();
            case UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING:
                return getGeneralOrderings();
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_INTERACTION:
                if (resolve) return getEnclosingInteraction();
                return basicGetEnclosingInteraction();
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_OPERAND:
                if (resolve) return getEnclosingOperand();
                return basicGetEnclosingOperand();
            case UMLPackage.COMBINED_FRAGMENT__INTERACTION_OPERATOR:
                return getInteractionOperator();
            case UMLPackage.COMBINED_FRAGMENT__OPERAND:
                return getOperands();
            case UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE:
                return getCfragmentGates();
        }
        return eDynamicGet(featureID, resolve, coreType);
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
            case UMLPackage.COMBINED_FRAGMENT__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__NAME:
                setName((String) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__VISIBILITY:
                setVisibility((VisibilityKind) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                getClientDependencies().addAll((Collection<? extends Dependency>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION:
                setNameExpression((StringExpression) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__COVERED:
                getCovereds().clear();
                getCovereds().addAll((Collection<? extends Lifeline>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING:
                getGeneralOrderings().clear();
                getGeneralOrderings().addAll((Collection<? extends GeneralOrdering>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_INTERACTION:
                setEnclosingInteraction((Interaction) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_OPERAND:
                setEnclosingOperand((InteractionOperand) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__INTERACTION_OPERATOR:
                setInteractionOperator((InteractionOperatorKind) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__OPERAND:
                getOperands().clear();
                getOperands().addAll((Collection<? extends InteractionOperand>) newValue);
                return;
            case UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE:
                getCfragmentGates().clear();
                getCfragmentGates().addAll((Collection<? extends Gate>) newValue);
                return;
        }
        eDynamicSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case UMLPackage.COMBINED_FRAGMENT__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__NAME:
                unsetName();
                return;
            case UMLPackage.COMBINED_FRAGMENT__VISIBILITY:
                unsetVisibility();
                return;
            case UMLPackage.COMBINED_FRAGMENT__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION:
                setNameExpression((StringExpression) null);
                return;
            case UMLPackage.COMBINED_FRAGMENT__COVERED:
                getCovereds().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING:
                getGeneralOrderings().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_INTERACTION:
                setEnclosingInteraction((Interaction) null);
                return;
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_OPERAND:
                setEnclosingOperand((InteractionOperand) null);
                return;
            case UMLPackage.COMBINED_FRAGMENT__INTERACTION_OPERATOR:
                setInteractionOperator(INTERACTION_OPERATOR_EDEFAULT);
                return;
            case UMLPackage.COMBINED_FRAGMENT__OPERAND:
                getOperands().clear();
                return;
            case UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE:
                getCfragmentGates().clear();
                return;
        }
        eDynamicUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case UMLPackage.COMBINED_FRAGMENT__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.COMBINED_FRAGMENT__OWNER:
                return isSetOwner();
            case UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__NAME:
                return isSetName();
            case UMLPackage.COMBINED_FRAGMENT__VISIBILITY:
                return isSetVisibility();
            case UMLPackage.COMBINED_FRAGMENT__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case UMLPackage.COMBINED_FRAGMENT__CLIENT_DEPENDENCY:
                return clientDependencies != null && !clientDependencies.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__NAMESPACE:
                return isSetNamespace();
            case UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION:
                return nameExpression != null;
            case UMLPackage.COMBINED_FRAGMENT__COVERED:
                return covereds != null && !covereds.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING:
                return generalOrderings != null && !generalOrderings.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_INTERACTION:
                return basicGetEnclosingInteraction() != null;
            case UMLPackage.COMBINED_FRAGMENT__ENCLOSING_OPERAND:
                return basicGetEnclosingOperand() != null;
            case UMLPackage.COMBINED_FRAGMENT__INTERACTION_OPERATOR:
                return (eFlags & INTERACTION_OPERATOR_EFLAG) != INTERACTION_OPERATOR_EFLAG_DEFAULT;
            case UMLPackage.COMBINED_FRAGMENT__OPERAND:
                return operands != null && !operands.isEmpty();
            case UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE:
                return cfragmentGates != null && !cfragmentGates.isEmpty();
        }
        return eDynamicIsSet(featureID);
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
        result.append(" (interactionOperator: ");
        result.append(INTERACTION_OPERATOR_EFLAG_VALUES[(eFlags & INTERACTION_OPERATOR_EFLAG) >>> INTERACTION_OPERATOR_EFLAG_OFFSET]);
        result.append(')');
        return result.toString();
    }

    /**
	 * The array of subset feature identifiers for the '{@link #getOwnedElements() <em>Owned Element</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedElements()
	 * @generated
	 * @ordered
	 */
    protected static final int[] OWNED_ELEMENT_ESUBSETS = new int[] { UMLPackage.COMBINED_FRAGMENT__OWNED_COMMENT, UMLPackage.COMBINED_FRAGMENT__NAME_EXPRESSION, UMLPackage.COMBINED_FRAGMENT__GENERAL_ORDERING, UMLPackage.COMBINED_FRAGMENT__OPERAND, UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE };

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isSetOwnedElements() {
        return super.isSetOwnedElements() || eIsSet(UMLPackage.COMBINED_FRAGMENT__OPERAND) || eIsSet(UMLPackage.COMBINED_FRAGMENT__CFRAGMENT_GATE);
    }
}
