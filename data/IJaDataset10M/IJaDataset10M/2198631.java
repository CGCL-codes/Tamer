package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping;

import cz.cuni.mff.ksi.jinfer.autoeditor.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;

/**
 * Interface for regexpAutomaton simplifiers. Given input automaton with
 * Regexp<T> on transition, method simplify has to return Regexp<T>, which
 * corresponds to language accepted by automaton. On input, all regexps on
 * transitions are by definition tokens (it is not enforced anywhere however).
 *
 *
 * @author anti
 */
public interface RegexpAutomatonSimplifier<T> {

    Regexp<T> simplify(final RegexpAutomaton<T> inputAutomaton, final SymbolToString<Regexp<T>> symbolToString) throws InterruptedException;
}
