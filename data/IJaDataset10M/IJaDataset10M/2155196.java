package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.regexping;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;

/**
 * Factory for  {@link RegexpAutomatonSimplifier}.
 *
 * @author anti
 */
public interface RegexpAutomatonSimplifierFactory extends NamedModule, Capabilities, UserModuleDescription {

    /**
   * Create simplifier.
   * 
   * @param <T> type of automaton symbol.
   * @return
   */
    <T> RegexpAutomatonSimplifier<T> create();
}
