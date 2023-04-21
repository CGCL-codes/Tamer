package org.openscience.cdk.applications.taverna.qsar.descriptors.molecular;

import org.openscience.cdk.applications.taverna.qsar.AbstractMolecularDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.AutocorrelationDescriptorPolarizability;

/**
 * Class which provides the implementation for a cdk-taverna molecular qsar
 * descriptor.
 * 
 * @author Thomas Kuhn
 * 
 */
public class Autocorrelation_Charge extends AbstractMolecularDescriptor {

    protected IMolecularDescriptor getDescriptor() {
        return new AutocorrelationDescriptorPolarizability();
    }
}
