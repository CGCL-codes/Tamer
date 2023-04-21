package org.slasoi.seval.prediction.configuration.impl;

import java.util.LinkedList;
import java.util.List;
import org.slasoi.seval.prediction.configuration.ComponentSpecification;
import org.slasoi.seval.prediction.configuration.ResourceContainerSpecification;
import org.slasoi.seval.prediction.configuration.PredictionModelModification;
import org.slasoi.seval.prediction.configuration.UsageScenarioSpecification;

/**
 * A setup that describes a specific service setup that should be predicted. An instance of this class contains the
 * prediction relevant information provided to the prediction component.
 * 
 * @author Benjamin Klatt
 * 
 */
public class PredictionModelModificationImpl implements PredictionModelModification {

    /** The resource container specifications of the service setup for the prediction. */
    private List<ResourceContainerSpecification> resourceContainerSpecifications = new LinkedList<ResourceContainerSpecification>();

    /** The usage scenario specifications of the service setup for the prediction. */
    private List<UsageScenarioSpecification> usageScenarioSpecifications = new LinkedList<UsageScenarioSpecification>();

    /** The component specifications of the service setup for the prediction. */
    private List<ComponentSpecification> componentSpecifications = new LinkedList<ComponentSpecification>();

    /**
     * {@inheritDoc}
     * 
     * @see org.slasoi.seval.prediction.extractor.IServicePredictionSetup#getResourceContainerSpecifications()
     */
    public final List<ResourceContainerSpecification> getResourceContainerSpecifications() {
        return resourceContainerSpecifications;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the usageScenarioSpecifications
     */
    public final List<UsageScenarioSpecification> getUsageScenarioSpecifications() {
        return usageScenarioSpecifications;
    }

    /**
     * Get the component specifications.
     * 
     * @return The list of component specifications.
     */
    public final List<ComponentSpecification> getComponentSpecifications() {
        return componentSpecifications;
    }
}
