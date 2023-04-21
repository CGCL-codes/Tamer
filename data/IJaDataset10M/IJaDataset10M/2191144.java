package com.doxological.doxquery.types;

import java.util.*;
import javax.xml.namespace.QName;
import com.doxological.doxquery.utils.XQueryConstants;

/**
 * <p>.</p>
 *
 * @author John Snelson
 */
public class TypeCompiler {

    private Set<CompiledType.State> states_ = new HashSet<CompiledType.State>();

    public static class Result {

        public CompiledType.State start;

        public CompiledType.State end;

        public Result(CompiledType.State s, CompiledType.State e) {
            start = s;
            end = e;
        }
    }

    public Result compile(Type type) {
        states_.clear();
        CompiledType.State startState = makeState(null);
        Set<CompiledType.State> parents = new HashSet<CompiledType.State>();
        parents.add(startState);
        Set<CompiledType.State> entryStates = new HashSet<CompiledType.State>();
        Set<CompiledType.State> exitStates = compile(parents, entryStates, type);
        startState.addStates(entryStates);
        CompiledType.State exitState = makeState(null);
        for (CompiledType.State s : exitStates) {
            s.addState(exitState);
        }
        removeEquivalentStates();
        return new Result(startState, exitState);
    }

    private void removeEquivalentStates() {
        boolean loop = true;
        while (loop) {
            Set<CompiledType.State> unique = new HashSet<CompiledType.State>();
            for (CompiledType.State a : states_) {
                CompiledType.State found = null;
                for (CompiledType.State b : unique) {
                    if (a.equivalent(b)) {
                        found = b;
                        break;
                    }
                }
                if (found == null) {
                    unique.add(a);
                } else {
                    replaceUsage(a, found, states_);
                }
            }
            loop = states_.size() != unique.size();
            states_ = unique;
        }
    }

    private void replaceUsage(CompiledType.State a, CompiledType.State b, Collection<CompiledType.State> states) {
        for (CompiledType.State s : states) {
            if (s.nextStates.contains(a)) {
                s.nextStates.remove(a);
                s.nextStates.add(b);
            }
        }
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> nextStates, Type type) {
        Set<CompiledType.State> entryStates = new HashSet<CompiledType.State>();
        Set<CompiledType.State> exitStates = null;
        switch(type.getKind()) {
            case EMPTY:
                {
                    exitStates = compile(parents, entryStates, (EmptyType) type);
                    break;
                }
            case SEQUENCE:
                {
                    exitStates = compile(parents, entryStates, (SequenceType) type);
                    break;
                }
            case CHOICE:
                {
                    exitStates = compile(parents, entryStates, (ChoiceType) type);
                    break;
                }
            case ALL:
                {
                    exitStates = compile(parents, entryStates, (AllType) type);
                    break;
                }
            case COMPILED:
                {
                    exitStates = compile(parents, entryStates, (CompiledType) type);
                    break;
                }
            default:
                {
                    CompiledType.State state = makeState(type);
                    entryStates.add(state);
                    exitStates = new HashSet<CompiledType.State>();
                    exitStates.add(state);
                    break;
                }
        }
        switch(type.getOccurrence()) {
            case ZERO_OR_MORE:
            case ONE_OR_MORE:
                {
                    for (CompiledType.State exit : exitStates) {
                        for (CompiledType.State entry : entryStates) {
                            exit.addState(entry);
                        }
                    }
                    break;
                }
        }
        switch(type.getOccurrence()) {
            case ZERO_OR_MORE:
            case ZERO_OR_ONE:
                {
                    exitStates.addAll(parents);
                    break;
                }
        }
        nextStates.addAll(entryStates);
        return exitStates;
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> entryStates, EmptyType type) {
        Set<CompiledType.State> exitStates = new HashSet<CompiledType.State>();
        exitStates.addAll(parents);
        return exitStates;
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> entryStates, ChoiceType type) {
        Set<CompiledType.State> exitStates = new HashSet<CompiledType.State>();
        for (Type t : type.getChildren()) {
            exitStates.addAll(compile(parents, entryStates, t));
        }
        return exitStates;
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> nextStates, SequenceType type) {
        Set<CompiledType.State> entryStates = new HashSet<CompiledType.State>();
        Set<CompiledType.State> exitStates = null;
        Iterator<Type> i = type.getChildren().iterator();
        if (i.hasNext()) {
            Type t = i.next();
            exitStates = compile(parents, nextStates, t);
            while (i.hasNext()) {
                t = i.next();
                entryStates.clear();
                Set<CompiledType.State> tmp = compile(exitStates, entryStates, t);
                for (CompiledType.State s : exitStates) {
                    s.addStates(entryStates);
                }
                exitStates = tmp;
            }
        } else {
            exitStates = new HashSet<CompiledType.State>();
        }
        return exitStates;
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> nextStates, AllType type) {
        return compileAll(parents, nextStates, type.getChildren());
    }

    private Set<CompiledType.State> compileAll(Set<CompiledType.State> parents, Set<CompiledType.State> nextStates, Collection<Type> children) {
        if (children.size() == 0) {
            return new HashSet<CompiledType.State>();
        }
        if (children.size() == 1) {
            return compile(parents, nextStates, children.iterator().next());
        }
        Set<CompiledType.State> entryStates = new HashSet<CompiledType.State>();
        Set<CompiledType.State> exitStates = new HashSet<CompiledType.State>();
        Set<Type> newChildren = new HashSet<Type>(children.size());
        for (Type t : children) {
            newChildren.clear();
            newChildren.addAll(children);
            newChildren.remove(t);
            Set<CompiledType.State> newParents = compile(parents, nextStates, t);
            nextStates.addAll(newParents);
            entryStates.clear();
            exitStates.addAll(compileAll(newParents, entryStates, newChildren));
            for (CompiledType.State s : newParents) {
                s.addStates(entryStates);
            }
        }
        return exitStates;
    }

    private Set<CompiledType.State> compile(Set<CompiledType.State> parents, Set<CompiledType.State> nextStates, CompiledType type) {
        Set<CompiledType.State> states = new HashSet<CompiledType.State>();
        getUsedStates(type.getStartState(), states);
        Map<CompiledType.State, CompiledType.State> map = new HashMap<CompiledType.State, CompiledType.State>();
        for (CompiledType.State s : states) {
            if (s != type.getStartState() && s != type.getEndState()) {
                map.put(s, cloneState(s));
            }
        }
        for (CompiledType.State s : map.keySet()) {
            replaceUsage(s, map.get(s), map.values());
        }
        for (CompiledType.State s : type.getStartState().nextStates) {
            nextStates.add(map.get(s));
        }
        Set<CompiledType.State> exitStates = new HashSet<CompiledType.State>();
        for (CompiledType.State s : map.values()) {
            if (s.nextStates.contains(type.getEndState())) {
                s.nextStates.remove(type.getEndState());
                exitStates.add(s);
            }
        }
        return exitStates;
    }

    private static void getUsedStates(CompiledType.State current, Set<CompiledType.State> states) {
        if (states.add(current)) {
            for (CompiledType.State s : current.nextStates) {
                getUsedStates(s, states);
            }
        }
    }

    private CompiledType.State makeState(Type match) {
        CompiledType.State state = new CompiledType.State(match);
        states_.add(state);
        return state;
    }

    private CompiledType.State cloneState(CompiledType.State s) {
        CompiledType.State newState = makeState(s.type);
        newState.nextStates.addAll(s.nextStates);
        return newState;
    }
}
