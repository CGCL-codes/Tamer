package cz.cuni.mff.ksi.jinfer.crudemdl.processing;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.crudemdl.TwoStepUserDescription;

/**
 * Factory interface for ClusterProcessor. Implementations should be annotated
 *
 * \@ServiceProvider(service = ClusterProcessorFactory.class)
 *
 * to enable simplifier to find implementation by lookups.
 * 
 * @author anti
 */
public interface ClusterProcessorFactory extends NamedModule, Capabilities, TwoStepUserDescription {

    ClusterProcessor<AbstractStructuralNode> create();
}
