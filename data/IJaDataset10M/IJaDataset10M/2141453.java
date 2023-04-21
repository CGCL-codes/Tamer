package M3Actions.impl;

import hub.sam.mof.simulator.MRuntimeException;
import hub.sam.mof.simulator.Simulator;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import M3Actions.M3ActionsPackage;
import M3Actions.MAction;
import M3Actions.MActivityEdge;
import M3Actions.MActivityNode;
import M3Actions.MActivityParameter;
import M3Actions.MBehaviour;
import M3Actions.MControlNode;
import M3Actions.MDecisionMergeNode;
import M3Actions.MFinalNode;
import M3Actions.MIterateAction;
import M3Actions.MObjectNode;
import M3Actions.MPin;
import M3Actions.Runtime.MContext;
import Traces.Change;
import Traces.TracesFactory;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>MBehaviour</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link M3Actions.impl.MBehaviourImpl#getOwnedNodes <em>Owned Nodes</em>}</li>
 *   <li>{@link M3Actions.impl.MBehaviourImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link M3Actions.impl.MBehaviourImpl#getRedefinedBehaviour <em>Redefined Behaviour</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBehaviourImpl extends EObjectImpl implements MBehaviour {

    /**
	 * The cached value of the '{@link #getOwnedNodes() <em>Owned Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getOwnedNodes()
	 * @generated
	 * @ordered
	 */
    protected EList<MActivityNode> ownedNodes;

    /**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
    protected EList<MActivityParameter> parameters;

    /**
	 * The cached value of the '{@link #getRedefinedBehaviour() <em>Redefined Behaviour</em>}' reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getRedefinedBehaviour()
	 * @generated
	 * @ordered
	 */
    protected EList<MBehaviour> redefinedBehaviour;

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    protected MBehaviourImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return M3ActionsPackage.Literals.MBEHAVIOUR;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<MActivityNode> getOwnedNodes() {
        if (ownedNodes == null) {
            ownedNodes = new EObjectContainmentWithInverseEList<MActivityNode>(MActivityNode.class, this, M3ActionsPackage.MBEHAVIOUR__OWNED_NODES, M3ActionsPackage.MACTIVITY_NODE__OWNING_BEHAVIOUR);
        }
        return ownedNodes;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<MActivityParameter> getParameters() {
        if (parameters == null) {
            parameters = new EObjectContainmentEList<MActivityParameter>(MActivityParameter.class, this, M3ActionsPackage.MBEHAVIOUR__PARAMETERS);
        }
        return parameters;
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    public EList<MBehaviour> getRedefinedBehaviour() {
        if (redefinedBehaviour == null) {
            redefinedBehaviour = new EObjectResolvingEList<MBehaviour>(MBehaviour.class, this, M3ActionsPackage.MBEHAVIOUR__REDEFINED_BEHAVIOUR);
        }
        return redefinedBehaviour;
    }

    /**
	 * Implementation of the execution semantics of MBehaviour. Do to single inheritance
	 * restrictions, the actual implementation is delegated to
	 * {@link MBehaviourImpl#doExecute(MBehaviour, MContext)} and subclasses should delegate their
	 * implementation by invoking <code>doExecute(this, context);</code>
	 * 
	 * @generated NOT
	 */
    public void mExecute(MContext context) {
        doExecute(this, context);
    }

    /**
	 * Delegate method that performs the actual implementation of
	 * {@link MBehaviour#mExecute(MContext)}.
	 * 
	 * @param thisBehaviour
	 *            actual 'this' object that was invoked during execution
	 * @param context
	 *            context passed as mExecute parameter
	 */
    public static void doExecute(MBehaviour thisBehaviour, MContext context) {
        MActivityNode current = context.getCurrentNode();
        while (current != null) {
            context.setCurrentNode(current);
            if (current instanceof MFinalNode) {
                current = null;
            } else if (current instanceof MAction) {
                MAction action = (MAction) current;
                action.preMExecute(context);
                action.mExecute(context);
                action.postMExecute(context);
                EList edges = new BasicEList(action.getOutgoing());
                EList pins = action.getOwnedPins();
                for (Iterator i = pins.iterator(); i.hasNext(); ) {
                    MPin pin = (MPin) i.next();
                    EList outgoing = pin.getOutgoing();
                    edges.addAll(outgoing);
                }
                current = null;
                if (action instanceof MIterateAction) {
                    for (Object element : action.getOutgoing()) {
                        MActivityEdge edge = (MActivityEdge) element;
                        MActivityNode node = edge.getTarget();
                        if (node instanceof MPin) {
                            MActivityNode ac = ((MPin) node).getAction();
                            if (ac.eContainer() == thisBehaviour) {
                                current = node;
                                break;
                            }
                        } else {
                            if (node.eContainer() == thisBehaviour) {
                                current = node;
                                break;
                            }
                        }
                    }
                } else {
                    if (edges.size() == 1) {
                        current = ((MActivityEdge) edges.get(0)).getTarget();
                    } else {
                        for (Iterator i = edges.iterator(); i.hasNext(); ) {
                            MActivityEdge edge = (MActivityEdge) i.next();
                            if ("main".equals(edge.getGuardExpression())) {
                                current = edge.getTarget();
                            }
                        }
                    }
                }
            } else if (current instanceof MControlNode) {
                if (current instanceof MDecisionMergeNode) {
                    EList nextNodes = ((MDecisionMergeNode) current).getNextNodes(context);
                    if (nextNodes.isEmpty()) {
                        throw new MRuntimeException("There is nothing to do after a decision node.");
                    }
                    current = (MActivityNode) nextNodes.get(0);
                }
            } else if (current instanceof MPin) {
                current = ((MPin) current).getAction();
            } else if (current instanceof MObjectNode) {
                EList edges = new BasicEList(current.getOutgoing());
                current = null;
                if (edges.size() == 1) {
                    current = ((MActivityEdge) edges.get(0)).getTarget();
                } else {
                    for (Iterator i = edges.iterator(); i.hasNext(); ) {
                        MActivityEdge edge = (MActivityEdge) i.next();
                        if ("main".equals(edge.getGuardExpression())) {
                            current = edge.getTarget();
                        }
                    }
                }
            }
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public void preMExecute(MContext context) {
        doPreMExecute(this, context);
    }

    public static void doPreMExecute(MBehaviour thisBehaviour, MContext context) {
        if (Simulator.INSTANCE.isTracingEnabled()) {
            Change callingChange = context.getThread().getCurrentChange();
            if (callingChange == null) {
                callingChange = TracesFactory.eINSTANCE.createChange();
                context.getThread().getTrace().getChanges().add(callingChange);
            }
            Simulator.INSTANCE.dumpRecentChanges(context, callingChange);
            Change change = TracesFactory.eINSTANCE.createChange();
            change.setActionURI(thisBehaviour.eResource().getURIFragment(thisBehaviour));
            callingChange.getNestedChanges().add(change);
            context.getThread().setCurrentChange(change);
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
    public void postMExecute(MContext context) {
        doPostMExecute(this, context);
    }

    public static void doPostMExecute(MBehaviour thisBehaviour, MContext context) {
        if (Simulator.INSTANCE.isTracingEnabled()) {
            Change currentChange = context.getThread().getCurrentChange();
            if (currentChange instanceof Change) {
                context.getThread().setCurrentChange((Change) currentChange.eContainer());
            }
        }
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getOwnedNodes()).basicAdd(otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                return ((InternalEList<?>) getOwnedNodes()).basicRemove(otherEnd, msgs);
            case M3ActionsPackage.MBEHAVIOUR__PARAMETERS:
                return ((InternalEList<?>) getParameters()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                return getOwnedNodes();
            case M3ActionsPackage.MBEHAVIOUR__PARAMETERS:
                return getParameters();
            case M3ActionsPackage.MBEHAVIOUR__REDEFINED_BEHAVIOUR:
                return getRedefinedBehaviour();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                getOwnedNodes().clear();
                getOwnedNodes().addAll((Collection<? extends MActivityNode>) newValue);
                return;
            case M3ActionsPackage.MBEHAVIOUR__PARAMETERS:
                getParameters().clear();
                getParameters().addAll((Collection<? extends MActivityParameter>) newValue);
                return;
            case M3ActionsPackage.MBEHAVIOUR__REDEFINED_BEHAVIOUR:
                getRedefinedBehaviour().clear();
                getRedefinedBehaviour().addAll((Collection<? extends MBehaviour>) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                getOwnedNodes().clear();
                return;
            case M3ActionsPackage.MBEHAVIOUR__PARAMETERS:
                getParameters().clear();
                return;
            case M3ActionsPackage.MBEHAVIOUR__REDEFINED_BEHAVIOUR:
                getRedefinedBehaviour().clear();
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case M3ActionsPackage.MBEHAVIOUR__OWNED_NODES:
                return ownedNodes != null && !ownedNodes.isEmpty();
            case M3ActionsPackage.MBEHAVIOUR__PARAMETERS:
                return parameters != null && !parameters.isEmpty();
            case M3ActionsPackage.MBEHAVIOUR__REDEFINED_BEHAVIOUR:
                return redefinedBehaviour != null && !redefinedBehaviour.isEmpty();
        }
        return super.eIsSet(featureID);
    }
}
