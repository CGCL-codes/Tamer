package cz.cuni.mff.ksi.jinfer.crudemdl.processing.automatonmergingstate.regexping.stateremoval.ordering;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.crudemdl.TwoStepUserDescription;

/**
 * Factory interface for RegexpAutomatonSimplifierStateRemovalOrderer.
 *
 * @author anti
 */
public interface RegexpAutomatonSimplifierStateRemovalOrdererFactory extends NamedModule, Capabilities, TwoStepUserDescription {

    <T> RegexpAutomatonSimplifierStateRemovalOrderer<T> create();
}
