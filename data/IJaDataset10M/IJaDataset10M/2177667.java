package de.uni_muenster.cs.sev.lethal.hedgeautomaton;

import de.uni_muenster.cs.sev.lethal.states.HedgeState;
import de.uni_muenster.cs.sev.lethal.states.State;
import de.uni_muenster.cs.sev.lethal.symbol.common.UnrankedSymbol;
import de.uni_muenster.cs.sev.lethal.symbol.special.HedgeSymbol;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTA;
import de.uni_muenster.cs.sev.lethal.treeautomata.common.FTARule;
import java.util.Set;

/**
 * A simple version of the HedgeAutomaton, where the types of the symbols and 
 * states are of no importance.
 *
 * @author Anton Reis, Maria Schatz
 */
public class EasyHedgeAutomaton extends HedgeAutomaton<UnrankedSymbol, State> {

    /**
	 * Creates an EasyHedgeAutomaton.
	 *
	 * @param States			states for the automaton
	 * @param FinalStates final states for the automaton
	 * @param Rules			 the rules to follow for the automaton
	 */
    public EasyHedgeAutomaton(final Set<State> States, final Set<State> FinalStates, final Set<HedgeRule<UnrankedSymbol, State>> Rules) {
        super(States, FinalStates, Rules);
    }

    /**
	 * Internal constructor to create hedge automaton from a finite tree automaton.
	 *
	 * @param TA the FTA to create HA from
	 */
    EasyHedgeAutomaton(final FTA<HedgeSymbol<UnrankedSymbol>, HedgeState<State>, ? extends FTARule<HedgeSymbol<UnrankedSymbol>, HedgeState<State>>> TA) {
        super(TA);
    }
}
