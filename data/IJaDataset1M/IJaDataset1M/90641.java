package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.simplifying;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;

/**
 * Factory for {@link AutomatonSimplifier}.
 *
 * @author anti
 */
public interface AutomatonSimplifierFactory extends NamedModule, Capabilities, UserModuleDescription {

    /**
   * Create generic {@link AutomatonSimplifier} instance. Simplifying automaton
   * does not depend on symbol type T.
   * 
   * @param <T> type of symbol in automaton (alphabet domain)
   * @return instance of {@link AutomatonSimplifier} implementation
   */
    <T> AutomatonSimplifier<T> create();
}
