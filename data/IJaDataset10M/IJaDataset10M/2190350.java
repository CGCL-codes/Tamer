package org.bpmn;

import java.util.List;

/**
 * @author jassuncao
 * @model abstract="true"
 */
public interface Activity extends FlowObject {

    /**
	 * @model changeable="false"
	 */
    ActivityType getActivityType();

    /**
	 * @model default="Status.NONE"
	 */
    Status getStatus();

    /**
	 * @model type="Property" containment="false" required="false"
	 */
    List getProperties();

    /**
	 * @model type="Artifact" containment="false" required="true" lowerBound="1"
	 */
    List getInputs();

    /**
	 * @model type="Artifact" containment="false" required="true" lowerBound="1"
	 */
    List getOutputs();

    /**
	 * @model type="Expression" containment="true"
	 */
    List getIORules();

    /**
	 * @model default="1"
	 */
    int getStartQuantity();

    /**
	 * @model default="LoopType.NONE"
	 */
    LoopType getLoopType();
}
