package com.incendiaryblue.commerce.order;

import java.util.*;

public class State {

    private static Supplier NO_SUPPLIER = new SupplierAdapter("no supplier", false);

    private Supplier supplier;

    private boolean completed;

    private String id;

    private String name;

    public State(String id, String name, Supplier supplier, boolean completed) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.completed = completed;
    }

    public String getID() {
        return (id);
    }

    public String getName() {
        return (name);
    }

    public Supplier getSupplier() {
        return (supplier);
    }

    public boolean isCompleted() {
        return (completed);
    }

    public boolean equals(Object obj) {
        return ((obj != null) && (getClass() == obj.getClass()) && supplier == ((State) obj).supplier && id.equals(((State) obj).id));
    }

    public int hashCode() {
        return (id.hashCode());
    }

    public static class Transition {

        Set initialStates = new HashSet();

        State finalState;

        String name;

        Transition(String name, State[] initialStates, State finalState) {
            this.initialStates.addAll(Arrays.asList(initialStates));
            this.finalState = finalState;
            this.name = name;
        }

        public String getName() {
            return (name);
        }

        public State getFinalState() {
            return (finalState);
        }

        public Set getInitialStates() {
            return (initialStates);
        }
    }

    public static class Manager {

        private static Map validStates = new HashMap();

        private static Map transitions = new HashMap();

        private static Map validTransitions = new HashMap();

        Manager() {
        }

        public State addState(String id, String name, Supplier supplier, boolean completed) throws OrderException {
            State vs = new State(id, name, supplier, completed);
            if (validStates.containsKey(vs)) throw new OrderException(OrderException.DUPLICATESTATE, "com.incendiaryblue.commerce.order.State.Manager");
            validStates.put(vs, vs);
            return (vs);
        }

        public State addState(String id, Supplier supplier, boolean completed) throws OrderException {
            return (addState(id, id, supplier, completed));
        }

        public State addState(String id, String name, Supplier supplier) throws OrderException {
            return (addState(id, name, supplier, false));
        }

        public State addState(String name, Supplier supplier) throws OrderException {
            return (addState(name, name, supplier, false));
        }

        public State addState(String id, String name) throws OrderException {
            return (addState(id, name, NO_SUPPLIER, false));
        }

        public State addState(String name) throws OrderException {
            return (addState(name, name, NO_SUPPLIER, false));
        }

        public State addState(String id, String name, boolean completed) throws OrderException {
            return (addState(id, name, NO_SUPPLIER, completed));
        }

        public State addState(String name, boolean completed) throws OrderException {
            return (addState(name, name, NO_SUPPLIER, completed));
        }

        public Transition addTransition(String name, State[] initialStates, State finalState) {
            Transition trans = new Transition(name, initialStates, finalState);
            transitions.put(trans.getName(), trans);
            for (Iterator ii = trans.initialStates.iterator(); ii.hasNext(); ) {
                State s = (State) ii.next();
                List transitionSet = (List) validTransitions.get(s);
                if (transitionSet == null) {
                    transitionSet = new ArrayList();
                    validTransitions.put(s, transitionSet);
                }
                transitionSet.add(trans);
            }
            return (trans);
        }

        public State getState(String id, Supplier supplier) throws OrderException {
            State vs = new State(id, "", supplier, false);
            State result = (State) validStates.get(vs);
            if (result != null) return (result);
            vs.supplier = NO_SUPPLIER;
            result = (State) validStates.get(vs);
            if (result != null) return (result);
            throw new OrderException(OrderException.INVALIDSTATE, "com.incendiaryblue.commerce.order.State.Manager " + id + "/" + supplier.getName());
        }

        public State getState(String id) throws OrderException {
            State vs = new State(id, id, State.NO_SUPPLIER, false);
            State result = (State) validStates.get(vs);
            if (result != null) return (result);
            throw new OrderException(OrderException.INVALIDSTATE, "com.incendiaryblue.commerce.order.State.Manager");
        }

        public boolean checkTransition(State from, State to) {
            List transitionSet = (List) validTransitions.get(from);
            if (transitionSet == null) return (false);
            for (Iterator ii = transitionSet.iterator(); ii.hasNext(); ) {
                Transition t = (Transition) ii.next();
                if (to == t.finalState) return (true);
            }
            return (false);
        }

        public List getTransitions(State from) {
            List transitionSet = (List) validTransitions.get(from);
            return (transitionSet);
        }
    }
}
