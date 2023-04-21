package com.nokia.ats4.appmodel.util.graph;

import com.nokia.ats4.appmodel.exception.KendoException;
import com.nokia.ats4.appmodel.model.KendoApplicationModel;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.domain.ExitPoint;
import com.nokia.ats4.appmodel.model.domain.InGate;
import com.nokia.ats4.appmodel.model.domain.OutGate;
import com.nokia.ats4.appmodel.model.domain.StartState;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.domain.Transition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * NewChinesePostman is a replacement for old chinesepostman algorithm. Mainly
 * to simplify the complexity of the code, as well as the problem.
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class NewChinesePostman {

    /** Use abstract adt so that the real model isn't modified when going trouch model */
    private Stack<NodeADT> queue = new Stack<NodeADT>();

    /** Creates a new instance of NewChinesePostman */
    public NewChinesePostman() {
    }

    /**
     * This returns paths that originated from startState.
     *
     * @throws com.nokia.ats4.appmodel.exception.KendoException
     * @param start the root for all paths
     * @return list of paths
     *
     */
    public List<List<Transition>> createPaths(State start) throws KendoException {
        if (!(start instanceof StartState)) {
            throw new KendoException("Graph does not have start state (sub models canno't be executed)", "error.cpt.noStartState");
        }
        return createPathsFromAnyState(start);
    }

    /**
     * Creates a list of all paths originated from the specified state.
     * We use depth first iterator to create paths that originate form arg start.
     * The paths that are returned are paths that start from arg start and end in
     * dead ends (states with no children) or loopbacks (states that are allready in the path).
     * 
     * @param start the origin of all paths
     * @return a list of all paths originated at the specified state
     * @throws com.nokia.ats4.appmodel.exception.KendoException if no paths begin from
     * the specified state
     */
    public List<List<Transition>> createPathsFromAnyState(State start) throws KendoException {
        List<List<Transition>> retval = new ArrayList<List<Transition>>();
        Map<InGate, OutGate> gateStack = new HashMap<InGate, OutGate>();
        NodeADT active = null;
        KendoModel scopeModel = start.getContainingModel().getContainingModel();
        if (start.getOutgoingTransitions().size() < 1) {
            throw new KendoException("No states in given model", "warning.cpt.nothingToDo");
        } else {
            active = new NodeADT(start, new ArrayList<Transition>(start.getOutgoingTransitions()), null);
            queue.add(active);
        }
        while (!queue.empty()) {
            Transition activeTransition = getActive();
            State immediateTarget = null;
            State target = null;
            if (activeTransition != null) {
                target = activeTransition.getTargetState();
                immediateTarget = activeTransition.getTargetPort().getOwnerState();
            } else {
                break;
            }
            if (checkForLoopBacks(target)) {
                retval.add(buildPathFromQueue(activeTransition));
            } else if (target.getOutgoingTransitions().isEmpty()) {
                retval.add(buildPathFromQueue(activeTransition));
            } else if (scopeModel instanceof KendoApplicationModel && immediateTarget instanceof ExitPoint && !(immediateTarget instanceof OutGate) && immediateTarget.getContainingModel().getContainingModel() instanceof KendoApplicationModel) {
                retval.add(buildPathFromQueue(activeTransition));
            } else if (immediateTarget instanceof OutGate) {
                if (mapOutgatesAndInGates((OutGate) immediateTarget, gateStack)) {
                    queue.push(new NodeADT(target, new ArrayList<Transition>(target.getOutgoingTransitions()), activeTransition));
                }
            } else if (immediateTarget instanceof InGate) {
                InGate targetInGate = (InGate) immediateTarget;
                if (gateStack.containsKey(targetInGate) && gateStack.get(targetInGate).getTransitionToNextState() != null) {
                    State nextState = gateStack.get(targetInGate).getTransitionToNextState().getTargetState();
                    if (!nextState.getOutgoingTransitions().isEmpty() && !checkForLoopBacks(nextState)) {
                        queue.add(new NodeADT(nextState, new ArrayList<Transition>(nextState.getOutgoingTransitions()), activeTransition));
                    } else {
                        retval.add(buildPathFromQueue(activeTransition));
                    }
                    gateStack.remove(targetInGate);
                }
            } else {
                queue.push(new NodeADT(target, new ArrayList<Transition>(target.getOutgoingTransitions()), activeTransition));
            }
        }
        for (int i = 0; i < retval.size(); i++) {
            if (retval.get(i) == null || retval.get(i).size() < 1) {
                retval.remove(i);
                i--;
            }
        }
        return retval;
    }

    /**
     * This goes trough queue and checks wheter the path ends in loopback
     *
     * @param target
     */
    private boolean checkForLoopBacks(State target) {
        boolean result = false;
        for (NodeADT adt : queue) {
            if (adt.state == target) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * This iterates trough queue and creates path from the stack
     *
     * @param active    this is the active transition that is added to the created path
     * @return  path
     */
    private List<Transition> buildPathFromQueue(Transition active) {
        List<Transition> retval = new ArrayList<Transition>();
        for (NodeADT node : queue) {
            if (node.transition != null) {
                retval.add(node.transition);
            }
        }
        retval.add(active);
        return retval;
    }

    /**
     * This updates queue and and returns the active transition
     * 
     * @return  active transition
     */
    private Transition getActive() {
        NodeADT retval = queue.pop();
        Transition returnValue = null;
        while (retval.children.isEmpty() && !queue.empty()) {
            retval = queue.pop();
        }
        if (!retval.children.isEmpty()) {
            returnValue = retval.children.remove(0);
        }
        queue.push(retval);
        return returnValue;
    }

    /**
     * This creates pair from ingate and outgate. The pair is used to determine where
     * to go after the path returns to ingate.
     */
    private boolean mapOutgatesAndInGates(OutGate gate, Map<InGate, OutGate> gateStack) {
        boolean result = false;
        if (gate.getCallTransition() != null && gate.getCallTransition().getImmediateTargetState() instanceof InGate) {
            gateStack.put((InGate) gate.getCallTransition().getImmediateTargetState(), gate);
            result = true;
        }
        return result;
    }

    /**
     * This returns paths that originated from startState.
     * Paths are ordered so that the returned paths cover as much of the model
     * as possible when the model is affected by flags.
     *
     * @throws com.nokia.ats4.appmodel.exception.KendoException
     * @param startState the root for all paths
     * @return list of paths that are ordered so that flags are used in optimal way
     *
     */
    public List<List<Transition>> createPathsWithFlags(State startState) throws KendoException {
        PostProcessor pp = new PostProcessor(startState);
        return pp.doFlagPostProcess(this.createPaths(startState));
    }

    /**
     * This returns paths that originated from startState.
     * Paths are ordered so that the returned paths cover as much of the model
     * as possible when the model is affected by flags.
     *
     * @throws com.nokia.ats4.appmodel.exception.KendoException
     * @param startState the root for all paths
     * @return list of paths (separated with null) that are ordered so that flags are used in optimal way
     *
     */
    public List<Transition> createNullSeparatedPathWithFlags(State startState) throws KendoException {
        List<Transition> retval = new ArrayList<Transition>();
        PostProcessor pp = new PostProcessor(startState);
        List<List<Transition>> temp = pp.doFlagPostProcess(this.createPaths(startState));
        for (List<Transition> path : temp) {
            retval.addAll(path);
            retval.add(null);
        }
        if (!retval.isEmpty() && retval.get(retval.size() - 1) == null) {
            retval.remove(retval.size() - 1);
        }
        return retval;
    }

    /**
     * This temporary abstract data type that is used to represent node in the graph
     * This mapps to state in following way:
     * -State is the field state
     * -Ingomming transitions is represented by transition
     * -Outgoing transitions are copyed from the state (so that editin them wont affect real model)
     */
    class NodeADT {

        State state;

        List<Transition> children;

        Transition transition;

        public NodeADT(State state, List<Transition> children, Transition Transition) {
            this.state = state;
            this.children = children;
            this.transition = Transition;
        }
    }
}
