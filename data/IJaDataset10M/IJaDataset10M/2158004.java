package monkey.scanner.automaton;

import monkey.scanner.TokenDefinition;
import monkey.scanner.alphabet.Alphabet;
import monkey.scanner.alphabet.SymbolSet;
import monkey.scanner.regexpr.*;

/**
 * Regular expression visitor which builds nondeterministi finit automatons.
 */
public final class ThompsonVisitor implements RegularExpressionVisitor {

    private Alphabet alphabet;

    private State startState;

    private State acceptorState;

    private ThompsonVisitor(Alphabet alphabet) {
        this.alphabet = alphabet;
        startState = new State();
        acceptorState = new State();
    }

    public void visit(Alternation alternation) {
        ThompsonVisitor firstVisitor = new ThompsonVisitor(alphabet);
        alternation.firstExpressionAccept(firstVisitor);
        ThompsonVisitor secondVisitor = new ThompsonVisitor(alphabet);
        alternation.secondExpressionAccept(secondVisitor);
        startState.createTransition(firstVisitor.startState, Alphabet.EPSILON_ELEMENT);
        startState.createTransition(secondVisitor.startState, Alphabet.EPSILON_ELEMENT);
        firstVisitor.acceptorState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
        secondVisitor.acceptorState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
    }

    public void visit(Concatenation concatenation) {
        ThompsonVisitor firstVisitor = new ThompsonVisitor(alphabet);
        concatenation.firstExpressionAccept(firstVisitor);
        ThompsonVisitor secondVisitor = new ThompsonVisitor(alphabet);
        concatenation.secondExpressionAccept(secondVisitor);
        startState.createTransition(firstVisitor.startState, Alphabet.EPSILON_ELEMENT);
        firstVisitor.acceptorState.createTransition(secondVisitor.startState, Alphabet.EPSILON_ELEMENT);
        secondVisitor.acceptorState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
    }

    public void visit(KleeneClosure kleeneClosure) {
        ThompsonVisitor visitor = new ThompsonVisitor(alphabet);
        kleeneClosure.expressionAccept(visitor);
        startState.createTransition(visitor.startState, Alphabet.EPSILON_ELEMENT);
        startState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
        visitor.acceptorState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
        visitor.acceptorState.createTransition(visitor.startState, Alphabet.EPSILON_ELEMENT);
    }

    public void visit(SymbolSet symbolSet) {
        int[] elements = alphabet.getElements(symbolSet);
        for (int index = 0; index < elements.length; index++) {
            int element = elements[index];
            startState.createTransition(acceptorState, element);
        }
    }

    public void visit(Epsilon epsilon) {
        startState.createTransition(acceptorState, Alphabet.EPSILON_ELEMENT);
    }

    /**
     * Creates a nondeterministic finit automaton following Thompson's rules.
     * The created automaton will have only one acceptor state.
     *
     * @param expression the regular expression
     * @param alphabet   the alphabet
     * @return a dynamic automaton
     */
    private static DynamicAutomaton createAutomaton(RegularExpression expression, Alphabet alphabet) {
        ThompsonVisitor visitor = new ThompsonVisitor(alphabet);
        expression.accept(visitor);
        DynamicAutomaton automaton = new DynamicAutomaton(alphabet);
        automaton.setStartState(visitor.startState);
        automaton.addAcceptorState(visitor.acceptorState);
        return automaton;
    }

    /**
     * Creates a nondeterministic finit automaton following Thompson's rules.
     * The created automaton will have one or more acceptor states.
     *
     * @param tokenDefinitions token definitions
     * @param alphabet         the alphabet
     * @return a dynamic automaton
     */
    public static DynamicAutomaton createAutomaton(TokenDefinition[] tokenDefinitions, Alphabet alphabet) {
        DynamicAutomaton automaton = new DynamicAutomaton(alphabet);
        State startState = new State();
        automaton.setStartState(startState);
        for (int index = 0; index < tokenDefinitions.length; index++) {
            TokenDefinition tokenDefinition = tokenDefinitions[index];
            DynamicAutomaton dynamicAutomaton = createAutomaton(tokenDefinition.getExpression(), alphabet);
            dynamicAutomaton.setAction(tokenDefinition.getAction());
            startState.createTransition(dynamicAutomaton.getStartState(), Alphabet.EPSILON_ELEMENT);
            automaton.addAcceptorStates(dynamicAutomaton.getAcceptorStates());
        }
        return automaton;
    }
}
