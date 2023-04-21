package org.openscience.cdk.applications.taverna.qsar.descriptors.molecular;

import org.openscience.cdk.applications.taverna.qsar.AbstractMolecularDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.RotatableBondsCountDescriptor;

/**
 * Class which provides the implementation for a cdk-taverna molecular qsar 
 * descriptor.
 * 
 * @author Thomas Kuhn
 * 
 */
public class RotatableBondsCount extends AbstractMolecularDescriptor {

    protected IMolecularDescriptor getDescriptor() {
        return new RotatableBondsCountDescriptor();
    }
}
