package cz.cuni.mff.ksi.jinfer.twostep.processing;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.interfaces.UserModuleDescription;

/**
 * Factory interface for 
 * {@link cz.cuni.mff.ksi.jinfer.twostep.processing.ClusterProcessor}.
 *
 * Implementations should be annotated
 *<code>
 * @ServiceProvider(service = ClusterProcessorFactory.class)
 *</code>
 * to enable simplifier to find implementation by lookups.
 * 
 * @author anti
 */
public interface ClusterProcessorFactory extends NamedModule, Capabilities, UserModuleDescription {

    /**
   * Creates new worker instance.
   *
   * @return cluster processor worker instance
   */
    ClusterProcessor<AbstractStructuralNode> create();
}
