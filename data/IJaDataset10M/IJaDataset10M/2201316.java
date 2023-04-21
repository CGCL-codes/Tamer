package org.openscience.cdk.applications.taverna.qsar;

import org.openscience.cdk.applications.taverna.scuflworkers.cdk.CDKLocalWorker;
import org.openscience.cdk.qsar.DescriptorSpecification;

/**
 * Interface which extend the LocalWorkerCDK interface to get the Descriptor Specification
 * @author Thomas Kuhn
 *
 */
public interface IQSAR extends CDKLocalWorker {

    /**
	 * Get the specification of the of the descriptors which will be calculated by this worker
	 * @return The descriptor specification.
	 */
    public DescriptorSpecification getDescriptorSpecification();
}
