package org.openscience.cdk.applications.taverna.qsar.descriptors.atomic;

import org.openscience.cdk.applications.taverna.qsar.AbstractAtomicDescriptor;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.descriptors.atomic.PartialPiChargeDescriptor;

/**
 * Class which provides the implementation for a cdk-taverna atomic qsar descriptor. 
 * 
 * @author Thomas Kuhn
 *
 */
public class PartialPiCharge extends AbstractAtomicDescriptor {

    protected IAtomicDescriptor getDescriptor() {
        return new PartialPiChargeDescriptor();
    }
}
