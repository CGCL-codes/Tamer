package org.eclipse.uml2.uml.internal.impl;

import java.util.Collection;
import java.util.Map;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ActivityPartition;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Dependency;
import org.eclipse.uml2.uml.ExceptionHandler;
import org.eclipse.uml2.uml.InterruptibleActivityRegion;
import org.eclipse.uml2.uml.StringExpression;
import org.eclipse.uml2.uml.StructuredActivityNode;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Variable;
import org.eclipse.uml2.uml.VariableAction;
import org.eclipse.uml2.uml.VisibilityKind;
import org.eclipse.uml2.uml.internal.operations.VariableActionOperations;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Variable Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.internal.impl.VariableActionImpl#getVariable <em>Variable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class VariableActionImpl extends ActionImpl implements VariableAction {

    /**
	 * The cached value of the '{@link #getVariable() <em>Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVariable()
	 * @generated
	 * @ordered
	 */
    protected Variable variable;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected VariableActionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return UMLPackage.Literals.VARIABLE_ACTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Variable getVariable() {
        if (variable != null && variable.eIsProxy()) {
            InternalEObject oldVariable = (InternalEObject) variable;
            variable = (Variable) eResolveProxy(oldVariable);
            if (variable != oldVariable) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, UMLPackage.VARIABLE_ACTION__VARIABLE, oldVariable, variable));
            }
        }
        return variable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public Variable basicGetVariable() {
        return variable;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setVariable(Variable newVariable) {
        Variable oldVariable = variable;
        variable = newVariable;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, UMLPackage.VARIABLE_ACTION__VARIABLE, oldVariable, variable));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean validateScopeOfVariable(DiagnosticChain diagnostics, Map<Object, Object> context) {
        return VariableActionOperations.validateScopeOfVariable(this, diagnostics, context);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case UMLPackage.VARIABLE_ACTION__EANNOTATIONS:
                return getEAnnotations();
            case UMLPackage.VARIABLE_ACTION__OWNED_ELEMENT:
                return getOwnedElements();
            case UMLPackage.VARIABLE_ACTION__OWNER:
                if (resolve) return getOwner();
                return basicGetOwner();
            case UMLPackage.VARIABLE_ACTION__OWNED_COMMENT:
                return getOwnedComments();
            case UMLPackage.VARIABLE_ACTION__NAME:
                return getName();
            case UMLPackage.VARIABLE_ACTION__VISIBILITY:
                return getVisibility();
            case UMLPackage.VARIABLE_ACTION__QUALIFIED_NAME:
                return getQualifiedName();
            case UMLPackage.VARIABLE_ACTION__CLIENT_DEPENDENCY:
                return getClientDependencies();
            case UMLPackage.VARIABLE_ACTION__NAMESPACE:
                if (resolve) return getNamespace();
                return basicGetNamespace();
            case UMLPackage.VARIABLE_ACTION__NAME_EXPRESSION:
                if (resolve) return getNameExpression();
                return basicGetNameExpression();
            case UMLPackage.VARIABLE_ACTION__IS_LEAF:
                return isLeaf();
            case UMLPackage.VARIABLE_ACTION__REDEFINED_ELEMENT:
                return getRedefinedElements();
            case UMLPackage.VARIABLE_ACTION__REDEFINITION_CONTEXT:
                return getRedefinitionContexts();
            case UMLPackage.VARIABLE_ACTION__IN_STRUCTURED_NODE:
                if (resolve) return getInStructuredNode();
                return basicGetInStructuredNode();
            case UMLPackage.VARIABLE_ACTION__ACTIVITY:
                if (resolve) return getActivity();
                return basicGetActivity();
            case UMLPackage.VARIABLE_ACTION__OUTGOING:
                return getOutgoings();
            case UMLPackage.VARIABLE_ACTION__INCOMING:
                return getIncomings();
            case UMLPackage.VARIABLE_ACTION__IN_PARTITION:
                return getInPartitions();
            case UMLPackage.VARIABLE_ACTION__IN_INTERRUPTIBLE_REGION:
                return getInInterruptibleRegions();
            case UMLPackage.VARIABLE_ACTION__IN_GROUP:
                return getInGroups();
            case UMLPackage.VARIABLE_ACTION__REDEFINED_NODE:
                return getRedefinedNodes();
            case UMLPackage.VARIABLE_ACTION__HANDLER:
                return getHandlers();
            case UMLPackage.VARIABLE_ACTION__OUTPUT:
                return getOutputs();
            case UMLPackage.VARIABLE_ACTION__INPUT:
                return getInputs();
            case UMLPackage.VARIABLE_ACTION__CONTEXT:
                if (resolve) return getContext();
                return basicGetContext();
            case UMLPackage.VARIABLE_ACTION__LOCAL_PRECONDITION:
                return getLocalPreconditions();
            case UMLPackage.VARIABLE_ACTION__LOCAL_POSTCONDITION:
                return getLocalPostconditions();
            case UMLPackage.VARIABLE_ACTION__VARIABLE:
                if (resolve) return getVariable();
                return basicGetVariable();
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
            case UMLPackage.VARIABLE_ACTION__EANNOTATIONS:
                getEAnnotations().clear();
                getEAnnotations().addAll((Collection<? extends EAnnotation>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__OWNED_COMMENT:
                getOwnedComments().clear();
                getOwnedComments().addAll((Collection<? extends Comment>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__NAME:
                setName((String) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__VISIBILITY:
                setVisibility((VisibilityKind) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                getClientDependencies().addAll((Collection<? extends Dependency>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__NAME_EXPRESSION:
                setNameExpression((StringExpression) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__IS_LEAF:
                setIsLeaf((Boolean) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__IN_STRUCTURED_NODE:
                setInStructuredNode((StructuredActivityNode) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__ACTIVITY:
                setActivity((Activity) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__OUTGOING:
                getOutgoings().clear();
                getOutgoings().addAll((Collection<? extends ActivityEdge>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__INCOMING:
                getIncomings().clear();
                getIncomings().addAll((Collection<? extends ActivityEdge>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__IN_PARTITION:
                getInPartitions().clear();
                getInPartitions().addAll((Collection<? extends ActivityPartition>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__IN_INTERRUPTIBLE_REGION:
                getInInterruptibleRegions().clear();
                getInInterruptibleRegions().addAll((Collection<? extends InterruptibleActivityRegion>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__REDEFINED_NODE:
                getRedefinedNodes().clear();
                getRedefinedNodes().addAll((Collection<? extends ActivityNode>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__HANDLER:
                getHandlers().clear();
                getHandlers().addAll((Collection<? extends ExceptionHandler>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__LOCAL_PRECONDITION:
                getLocalPreconditions().clear();
                getLocalPreconditions().addAll((Collection<? extends Constraint>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__LOCAL_POSTCONDITION:
                getLocalPostconditions().clear();
                getLocalPostconditions().addAll((Collection<? extends Constraint>) newValue);
                return;
            case UMLPackage.VARIABLE_ACTION__VARIABLE:
                setVariable((Variable) newValue);
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
            case UMLPackage.VARIABLE_ACTION__EANNOTATIONS:
                getEAnnotations().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__OWNED_COMMENT:
                getOwnedComments().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__NAME:
                unsetName();
                return;
            case UMLPackage.VARIABLE_ACTION__VISIBILITY:
                unsetVisibility();
                return;
            case UMLPackage.VARIABLE_ACTION__CLIENT_DEPENDENCY:
                getClientDependencies().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__NAME_EXPRESSION:
                setNameExpression((StringExpression) null);
                return;
            case UMLPackage.VARIABLE_ACTION__IS_LEAF:
                setIsLeaf(IS_LEAF_EDEFAULT);
                return;
            case UMLPackage.VARIABLE_ACTION__IN_STRUCTURED_NODE:
                setInStructuredNode((StructuredActivityNode) null);
                return;
            case UMLPackage.VARIABLE_ACTION__ACTIVITY:
                setActivity((Activity) null);
                return;
            case UMLPackage.VARIABLE_ACTION__OUTGOING:
                getOutgoings().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__INCOMING:
                getIncomings().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__IN_PARTITION:
                getInPartitions().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__IN_INTERRUPTIBLE_REGION:
                getInInterruptibleRegions().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__REDEFINED_NODE:
                getRedefinedNodes().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__HANDLER:
                getHandlers().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__LOCAL_PRECONDITION:
                getLocalPreconditions().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__LOCAL_POSTCONDITION:
                getLocalPostconditions().clear();
                return;
            case UMLPackage.VARIABLE_ACTION__VARIABLE:
                setVariable((Variable) null);
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
            case UMLPackage.VARIABLE_ACTION__EANNOTATIONS:
                return eAnnotations != null && !eAnnotations.isEmpty();
            case UMLPackage.VARIABLE_ACTION__OWNED_ELEMENT:
                return isSetOwnedElements();
            case UMLPackage.VARIABLE_ACTION__OWNER:
                return isSetOwner();
            case UMLPackage.VARIABLE_ACTION__OWNED_COMMENT:
                return ownedComments != null && !ownedComments.isEmpty();
            case UMLPackage.VARIABLE_ACTION__NAME:
                return isSetName();
            case UMLPackage.VARIABLE_ACTION__VISIBILITY:
                return isSetVisibility();
            case UMLPackage.VARIABLE_ACTION__QUALIFIED_NAME:
                return QUALIFIED_NAME_EDEFAULT == null ? getQualifiedName() != null : !QUALIFIED_NAME_EDEFAULT.equals(getQualifiedName());
            case UMLPackage.VARIABLE_ACTION__CLIENT_DEPENDENCY:
                return clientDependencies != null && !clientDependencies.isEmpty();
            case UMLPackage.VARIABLE_ACTION__NAMESPACE:
                return isSetNamespace();
            case UMLPackage.VARIABLE_ACTION__NAME_EXPRESSION:
                return nameExpression != null;
            case UMLPackage.VARIABLE_ACTION__IS_LEAF:
                return ((eFlags & IS_LEAF_EFLAG) != 0) != IS_LEAF_EDEFAULT;
            case UMLPackage.VARIABLE_ACTION__REDEFINED_ELEMENT:
                return isSetRedefinedElements();
            case UMLPackage.VARIABLE_ACTION__REDEFINITION_CONTEXT:
                return isSetRedefinitionContexts();
            case UMLPackage.VARIABLE_ACTION__IN_STRUCTURED_NODE:
                return basicGetInStructuredNode() != null;
            case UMLPackage.VARIABLE_ACTION__ACTIVITY:
                return basicGetActivity() != null;
            case UMLPackage.VARIABLE_ACTION__OUTGOING:
                return outgoings != null && !outgoings.isEmpty();
            case UMLPackage.VARIABLE_ACTION__INCOMING:
                return incomings != null && !incomings.isEmpty();
            case UMLPackage.VARIABLE_ACTION__IN_PARTITION:
                return inPartitions != null && !inPartitions.isEmpty();
            case UMLPackage.VARIABLE_ACTION__IN_INTERRUPTIBLE_REGION:
                return inInterruptibleRegions != null && !inInterruptibleRegions.isEmpty();
            case UMLPackage.VARIABLE_ACTION__IN_GROUP:
                return isSetInGroups();
            case UMLPackage.VARIABLE_ACTION__REDEFINED_NODE:
                return redefinedNodes != null && !redefinedNodes.isEmpty();
            case UMLPackage.VARIABLE_ACTION__HANDLER:
                return handlers != null && !handlers.isEmpty();
            case UMLPackage.VARIABLE_ACTION__OUTPUT:
                return isSetOutputs();
            case UMLPackage.VARIABLE_ACTION__INPUT:
                return isSetInputs();
            case UMLPackage.VARIABLE_ACTION__CONTEXT:
                return basicGetContext() != null;
            case UMLPackage.VARIABLE_ACTION__LOCAL_PRECONDITION:
                return localPreconditions != null && !localPreconditions.isEmpty();
            case UMLPackage.VARIABLE_ACTION__LOCAL_POSTCONDITION:
                return localPostconditions != null && !localPostconditions.isEmpty();
            case UMLPackage.VARIABLE_ACTION__VARIABLE:
                return variable != null;
        }
        return eDynamicIsSet(featureID);
    }
}
