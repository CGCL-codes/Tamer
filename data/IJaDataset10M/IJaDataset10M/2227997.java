package cz.cuni.mff.ksi.jinfer.base.automaton;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Class representing deterministic finite automaton.
 * Can simplify itself when given mergingCondition
 *
 * Automaton can be non-deterministic for algorithmic reasons.
 * Of course, there is no constraint that each state has to have transition on
 * each symbol. But what more, there can be multiple transitions from one step
 * on same symbol, pointing to different states (real non-determinism) or to same
 * state (just non-canonical form).
 *
 * When merging occurs, this deviations can appear. Non-canonical form, when
 * there are two-or-more steps on same symbol between two states, is solved
 * (and collapsed to only one step) automatically by merging procedure.
 * Non-deterministic form is not solved by automaton itself. The merging algorithm
 * has to deal with this opportunity is it gives bad order of merging states.
 * But it can let it non-deterministic by design.
 *
 * @author anti
 */
public class Automaton<T> {

    private static final Logger LOG = Logger.getLogger(Automaton.class);

    /**
   * Initial state of automaton, entry point. State in which automaton is
   * when starting reading input. Has to be in delta function keySet.
   *
   * Has no incoming steps.
   */
    protected State<T> initialState;

    /**
   * Transition function of automaton. Maps states to their outgoing steps. KeySet
   * is a set of all states of automaton.
   *
   * State is removed by delta.remove(state) and reverseDelta.remove(state)
   */
    protected final Map<State<T>, Set<Step<T>>> delta;

    /**
   * Transition function of automaton, mathematically the same as delta. For
   * programmatic reasons we have the reverse map, state mapping to ist incoming
   * steps.
   * Loops are therefore findable by using delta and reverseDelta so be careful
   * when counting loops - may count twice.
   */
    protected final Map<State<T>, Set<Step<T>>> reverseDelta;

    /**
   * New state name is an integer that is assigned to any new state created by
   * createNewState. It is always incremented in createNewState so no two states
   * share same name. Numbers freed by state removing are not used again, sequence
   * only grows.
   */
    protected int newStateName;

    /**
   * Merged states - when state X is merged into state Y, we say, X is being
   * merged out (from automaton). Because simplification may require us
   * to merge state X again with another state (using old name of state), we
   * need to remember for each merged out state the destination state.
   * That's what this map is used for, for every merged out state X (merging
   * into Y), entry
   * (X, Y) is added into map. As state can be merged out at most once, using
   * map is all-right for this purpose.
   * When request to merge state X arrives at future, and X is no longer in
   * delta function, the real (correct) state to merge will be searched using
   * this map.
   */
    protected final Map<State<T>, State<T>> mergedStates;

    /**
   * As in mergedStates, but in opposing direction. Holds information about
   * all the states, that merged to "key" in map. When X merges into Y,
   * this is done: reverseMergedStates.get(Y).add(X)
   *
   */
    protected final Map<State<T>, Set<State<T>>> reverseMergedStates;

    /** TODO anti comment */
    protected final Map<Integer, State<T>> nameMap;

    /**
   * Constructor which doesn't create initialState
   */
    public Automaton() {
        this.newStateName = 1;
        this.delta = new LinkedHashMap<State<T>, Set<Step<T>>>();
        this.reverseDelta = new LinkedHashMap<State<T>, Set<Step<T>>>();
        this.mergedStates = new LinkedHashMap<State<T>, State<T>>();
        this.reverseMergedStates = new LinkedHashMap<State<T>, Set<State<T>>>();
        this.nameMap = new LinkedHashMap<Integer, State<T>>();
        this.initialState = null;
    }

    /**
   * @param createInitialState - true= create initial state, false- as default constructor
   */
    public Automaton(final boolean createInitialState) {
        this();
        if (createInitialState) {
            this.initialState = this.createNewState();
        }
    }

    public Automaton(final Automaton<T> anotherAutomaton) {
        this(false);
        final AutomatonCloner<T, T> cloner = new AutomatonCloner<T, T>();
        cloner.convertAutomaton(anotherAutomaton, this, new AutomatonClonerSymbolConverter<T, T>() {

            @Override
            public T convertSymbol(final T symbol) {
                return symbol;
            }
        });
    }

    /**
   * Creates new state and return it. This is preferred way to extend automaton
   * states. It initializes delta map, and revesre delta map with empty linkedhashsets
   * of steps, increments newstatename.
   *
   * Process of extending automaton is therefore:
   * 1. create state using this function
   * 2. create steps giving them proper sources,destinations
   * 3. put step instances correctly in delta map and reverseDelta map.
   *
   * @return
   */
    protected final State<T> createNewState() {
        final State<T> newState = new State<T>(0, this.newStateName);
        this.nameMap.put(newStateName, newState);
        this.newStateName++;
        this.delta.put(newState, new LinkedHashSet<Step<T>>());
        this.reverseDelta.put(newState, new LinkedHashSet<Step<T>>());
        this.reverseMergedStates.put(newState, new LinkedHashSet<State<T>>());
        return newState;
    }

    /**
   * Returns first step from state, which accepts given symbol. If there are
   * multiple such steps, return only one found.
   *
   * @param state
   * @param symbol
   * @return
   */
    public Step<T> getOutStepOnSymbol(final State<T> state, final T symbol) {
        final Set<Step<T>> steps = this.delta.get(state);
        for (Step<T> step : steps) {
            if (step.getAcceptSymbol().equals(symbol)) {
                return step;
            }
        }
        return null;
    }

    /**
   * Preferred way to create new steps. Gives 1 as useCount to step.
   *
   * @param onSymbol
   * @param source
   * @param destination
   * @return
   */
    private Step<T> createNewStep(final T onSymbol, final State<T> source, final State<T> destination) {
        final Step<T> newStep = new Step<T>(onSymbol, source, destination, 1, 1);
        this.delta.get(source).add(newStep);
        this.reverseDelta.get(destination).add(newStep);
        return newStep;
    }

    /**
   * Given symbolString, iterates through it and follows steps in automaton. When there
   * isn't a step to follow, new state and step is created. Resulting in tree-formed automaton.
   *
   * @param symbolString - list of symbols (one word from accepting language)
   */
    public void buildPTAOnSymbol(final List<T> symbolString) {
        State<T> xState = this.getInitialState();
        for (T symbol : symbolString) {
            final Step<T> xStep = this.getOutStepOnSymbol(xState, symbol);
            if (xStep != null) {
                xStep.incUseCount();
                xStep.incMinUseCount();
                xStep.addInputString(symbolString);
                xState = xStep.getDestination();
            } else {
                final State<T> newState = this.createNewState();
                final Step<T> newStep = this.createNewStep(symbol, xState, newState);
                assert newStep.getDestination().equals(newState);
                newStep.addInputString(symbolString);
                xState = newStep.getDestination();
            }
        }
        xState.incFinalCount();
    }

    private State<T> buildPTAOnRegexpExpandStep(final State<T> fromState, final Regexp<T> regexp) {
        switch(regexp.getType()) {
            case TOKEN:
                final Step<T> outStep = this.getOutStepOnSymbol(fromState, regexp.getContent());
                State<T> newState;
                if (outStep == null) {
                    newState = this.createNewState();
                    final Step<T> newStep = this.createNewStep(regexp.getContent(), fromState, newState);
                    newStep.setUseCount(0);
                    newStep.setMinUseCount(0);
                } else {
                    newState = outStep.getDestination();
                }
                if (regexp.getInterval().isUnbounded()) {
                    mergeStates(fromState, newState);
                    return fromState;
                }
                return newState;
            case CONCATENATION:
                State<T> prevState = fromState;
                for (Regexp<T> ch : regexp.getChildren()) {
                    prevState = buildPTAOnRegexpExpandStep(prevState, ch);
                }
                if (regexp.getInterval().isUnbounded()) {
                    mergeStates(fromState, prevState);
                    return fromState;
                }
                return prevState;
            case ALTERNATION:
                final List<State<T>> endStates = new LinkedList<State<T>>();
                for (Regexp<T> ch : regexp.getChildren()) {
                    endStates.add(buildPTAOnRegexpExpandStep(fromState, ch));
                }
                this.mergeStates(endStates);
                if (regexp.getInterval().isUnbounded()) {
                    mergeStates(fromState, endStates.get(0));
                    return fromState;
                }
                return endStates.get(0);
            case PERMUTATION:
                throw new IllegalArgumentException("Permutation not possible.");
            default:
                throw new IllegalArgumentException("Unkown regexp type");
        }
    }

    public void buildPTAOnRegexp(final Regexp<T> regexp) {
        switch(regexp.getType()) {
            case LAMBDA:
                this.initialState.incFinalCount();
                return;
            case TOKEN:
            case CONCATENATION:
            case ALTERNATION:
                final Regexp<T> nR = AutomatonRegexpIntervalExpander.expandIntervalsRegexp(regexp);
                final State<T> fS = buildPTAOnRegexpExpandStep(initialState, nR);
                fS.incFinalCount();
                break;
            default:
                throw new IllegalArgumentException("Unknown regexp type.");
        }
    }

    private void collapseStepsAfterMerge(final State<T> mainState) {
        final Map<State<T>, Map<T, Step<T>>> inBuckets = new LinkedHashMap<State<T>, Map<T, Step<T>>>();
        final Set<Step<T>> inSteps = new LinkedHashSet<Step<T>>(this.reverseDelta.get(mainState));
        for (Step<T> inStep : inSteps) {
            if (inBuckets.containsKey(inStep.getSource())) {
                if (inBuckets.get(inStep.getSource()).containsKey(inStep.getAcceptSymbol())) {
                    inBuckets.get(inStep.getSource()).get(inStep.getAcceptSymbol()).incUseCount(inStep.getUseCount());
                    inBuckets.get(inStep.getSource()).get(inStep.getAcceptSymbol()).incMinUseCount(inStep.getMinUseCount());
                    inBuckets.get(inStep.getSource()).get(inStep.getAcceptSymbol()).addAllInputStrings(inStep.getInputStrings());
                    this.delta.get(inStep.getSource()).remove(inStep);
                    this.reverseDelta.get(inStep.getDestination()).remove(inStep);
                } else {
                    inBuckets.get(inStep.getSource()).put(inStep.getAcceptSymbol(), inStep);
                }
            } else {
                inBuckets.put(inStep.getSource(), new LinkedHashMap<T, Step<T>>());
                inBuckets.get(inStep.getSource()).put(inStep.getAcceptSymbol(), inStep);
            }
        }
        final Map<State<T>, Map<T, Step<T>>> outBuckets = new LinkedHashMap<State<T>, Map<T, Step<T>>>();
        final Set<Step<T>> outSteps = new LinkedHashSet<Step<T>>(this.delta.get(mainState));
        for (Step<T> outStep : outSteps) {
            if (outBuckets.containsKey(outStep.getDestination())) {
                if (outBuckets.get(outStep.getDestination()).containsKey(outStep.getAcceptSymbol())) {
                    outBuckets.get(outStep.getDestination()).get(outStep.getAcceptSymbol()).incUseCount(outStep.getUseCount());
                    outBuckets.get(outStep.getDestination()).get(outStep.getAcceptSymbol()).incMinUseCount(outStep.getMinUseCount());
                    outBuckets.get(outStep.getDestination()).get(outStep.getAcceptSymbol()).addAllInputStrings(outStep.getInputStrings());
                    this.delta.get(outStep.getSource()).remove(outStep);
                    this.reverseDelta.get(outStep.getDestination()).remove(outStep);
                } else {
                    outBuckets.get(outStep.getDestination()).put(outStep.getAcceptSymbol(), outStep);
                }
            } else {
                outBuckets.put(outStep.getDestination(), new LinkedHashMap<T, Step<T>>());
                outBuckets.get(outStep.getDestination()).put(outStep.getAcceptSymbol(), outStep);
            }
        }
    }

    protected State<T> getRealState(final State<T> state) {
        if (state == null) {
            return null;
        }
        if (this.delta.containsKey(state)) {
            return state;
        }
        if (this.mergedStates.get(state) != null) {
            final State<T> realState = this.getRealState(this.mergedStates.get(state));
            this.mergedStates.put(state, realState);
            this.reverseMergedStates.get(realState).add(state);
            return realState;
        }
        if (this.nameMap.containsKey(state.getName())) {
            return getRealState(this.nameMap.get(state.getName()));
        }
        throw new IllegalStateException("Not possible");
    }

    public void mergeStates(final List<State<T>> lst) {
        if (lst == null) {
            throw new IllegalArgumentException("List with states to merge has to be non-null" + " and at have at least 2 items.");
        }
        if (lst.size() <= 1) {
            throw new IllegalArgumentException("List with states to merge has to have" + " at least 2 items.");
        }
        final State<T> state = lst.get(0);
        for (State<T> anotherState : lst.subList(1, lst.size())) {
            this.mergeStates(state, anotherState);
        }
    }

    public void mergeStates(final State<T> _mainState, final State<T> _mergedState) {
        final State<T> mainState = this.getRealState(_mainState);
        final State<T> mergedState = this.getRealState(_mergedState);
        LOG.debug("mergeStates: Got to merge states: " + _mainState + " + " + _mergedState + "\n");
        LOG.debug("mergeStates: Real states merging: " + mainState + " + " + mergedState + "\n");
        if (mergedState.equals(mainState)) {
            LOG.debug("mergeStates: States equal, doing nothing\n");
            return;
        }
        final Set<Step<T>> mergedStateInSteps = this.reverseDelta.get(mergedState);
        for (Step<T> mergedStateInStep : mergedStateInSteps) {
            mergedStateInStep.setDestination(mainState);
        }
        this.reverseDelta.remove(mergedState);
        this.reverseDelta.get(mainState).addAll(mergedStateInSteps);
        final Set<Step<T>> mergedStateOutSteps = this.delta.get(mergedState);
        for (Step<T> mergedStateOutStep : mergedStateOutSteps) {
            mergedStateOutStep.setSource(mainState);
        }
        this.delta.remove(mergedState);
        this.delta.get(mainState).addAll(mergedStateOutSteps);
        mainState.incFinalCount(mergedState.getFinalCount());
        this.mergedStates.put(mergedState, mainState);
        this.reverseMergedStates.get(mainState).add(mergedState);
        for (State<T> t : this.reverseMergedStates.get(mergedState)) {
            this.mergedStates.put(t, mainState);
            this.reverseMergedStates.get(mainState).add(t);
        }
        this.reverseMergedStates.get(mergedState).clear();
        if (mergedState.equals(initialState)) {
            this.initialState = mainState;
        }
        LOG.debug("after merge");
        LOG.debug(this);
        this.collapseStepsAfterMerge(mainState);
        LOG.debug("after collapse");
        LOG.debug(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Automaton\n");
        for (State<T> state : delta.keySet()) {
            sb.append(state);
            for (State<T> mergedState : this.reverseMergedStates.get(state)) {
                sb.append(" + ");
                sb.append(mergedState);
            }
            sb.append(" outSteps:\n");
            for (Step<T> step : this.delta.get(state)) {
                sb.append(step);
                sb.append("\n");
            }
        }
        sb.append("reversed:\n");
        for (State<T> state : reverseDelta.keySet()) {
            sb.append(state);
            for (State<T> mergedState : this.reverseMergedStates.get(state)) {
                sb.append(" + ");
                sb.append(mergedState);
            }
            sb.append(" inSteps:\n");
            for (Step<T> step : this.reverseDelta.get(state)) {
                sb.append(step);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String toTestString() {
        final StringBuilder sb = new StringBuilder();
        for (State<T> state : delta.keySet()) {
            sb.append(state);
            for (State<T> mergedState : this.reverseMergedStates.get(state)) {
                sb.append("+");
                sb.append(mergedState);
            }
            if (this.delta.get(state).isEmpty()) {
                sb.append("   ");
            } else {
                sb.append(">>");
            }
            for (Step<T> step : this.delta.get(state)) {
                sb.append(step.toTestString());
                sb.append("   ");
            }
        }
        sb.append("@@@");
        for (State<T> state : reverseDelta.keySet()) {
            sb.append(state);
            for (State<T> mergedState : this.reverseMergedStates.get(state)) {
                sb.append("+");
                sb.append(mergedState);
            }
            if (this.reverseDelta.get(state).isEmpty()) {
                sb.append("   ");
            } else {
                sb.append("<<");
            }
            for (Step<T> step : this.reverseDelta.get(state)) {
                sb.append(step.toTestString());
                sb.append("   ");
            }
        }
        return sb.toString();
    }

    /**
   * @return the initialState
   */
    public State<T> getInitialState() {
        return initialState;
    }

    /**
   * @return the delta
   *
   */
    public Map<State<T>, Set<Step<T>>> getDelta() {
        return delta;
    }

    /**
   * @return the reverseDelta
   */
    public Map<State<T>, Set<Step<T>>> getReverseDelta() {
        return reverseDelta;
    }

    /**
   * @return the newStateName
   */
    protected Integer getNewStateName() {
        return newStateName;
    }

    /**
   * @return the mergedStates
   */
    public Map<State<T>, State<T>> getMergedStates() {
        return mergedStates;
    }

    /**
   * @return the reverseMergedStates
   */
    public Map<State<T>, Set<State<T>>> getReverseMergedStates() {
        return reverseMergedStates;
    }
}
