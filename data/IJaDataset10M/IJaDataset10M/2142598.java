package net.ekology.ekosystem;

import net.ekology.core.datatypes.Automaton;
import java.util.Calendar;

/**
 * Every abiotic agent in the ecosystem must extend this class
 * <p>
 * An <code>AbioticAgent</code> can have an associated <code>Automaton</code> but
 * no genome. A XML codified genome is used to initialize the <code>Automaton</code>
 * 
 * @author Aarón Tavío - aaron.tavio at gmail.com
 * @version 1.0.0 - 20081019-1700
 */
public abstract class AbioticAgent extends Agent {

    private Automaton oAutomaton;

    public AbioticAgent() {
        super();
        oAutomaton = null;
    }

    public AbioticAgent(String sID, Biotope oBiotope) {
        super(sID, oBiotope);
        oAutomaton = null;
    }

    public AbioticAgent(String sID, Biotope oBitope, Automaton oAutomaton) {
        super(sID, oBitope);
        this.oAutomaton = oAutomaton;
    }

    public AbioticAgent(String sID, Biotope oBitope, Calendar oCreationDate, Automaton oAutomaton) {
        super(sID, oBitope, oCreationDate);
        this.oAutomaton = oAutomaton;
    }

    @Override
    public Automaton getAutomaton() {
        return oAutomaton;
    }

    public void setAutomaton(Automaton oAutomaton) {
        if (this.oAutomaton == null) this.oAutomaton = oAutomaton;
    }
}
