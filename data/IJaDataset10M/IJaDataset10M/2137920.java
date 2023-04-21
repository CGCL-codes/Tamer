package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.userinteractive;

import cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.simplifying.AutomatonSimplifier;
import cz.cuni.mff.ksi.jinfer.autoeditor.AutoEditor;
import cz.cuni.mff.ksi.jinfer.autoeditor.SymbolToString;
import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Works simply - renders automaton to user. When returned list of states user
 * had selected, merges them. And cycle goes on. If user select no states,
 * it is considered that user is satisfied with automaton, so automaton is returned
 * in current form.
 *
 * @author anti
 */
public class AutomatonSimplifierUserInteractive<T> implements AutomatonSimplifier<T> {

    private static final Logger LOG = Logger.getLogger(AutomatonSimplifierUserInteractive.class);

    @Override
    public Automaton<T> simplify(final Automaton<T> inputAutomaton, final SymbolToString<T> symbolTostring) throws InterruptedException {
        final AutoEditor<T> gui = new AutoEditor<T>(symbolTostring);
        List<State<T>> mergeLst;
        do {
            mergeLst = gui.drawAutomatonToPickStates(inputAutomaton);
            if ((!BaseUtils.isEmpty(mergeLst)) && (mergeLst.size() >= 2)) {
                LOG.debug("AUTO EDITOR selected: " + mergeLst.toString());
                inputAutomaton.mergeStates(mergeLst);
                LOG.debug("After merge:");
                LOG.debug(inputAutomaton);
            }
        } while (!BaseUtils.isEmpty(mergeLst));
        return inputAutomaton;
    }
}
