package org.objectwiz.uibuilder.model.value;

import org.objectwiz.uibuilder.EvaluationContext;

/**
 * Exception indicating that the user needs to select a value from a collection
 * in order for the component that raised the exception to work.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class MissingSelectionException extends UserActionRequiredException {

    public MissingSelectionException(EvaluationContext context, CollectionWidgetSelectionValue value) {
        super(context, "Missing: " + value.getTarget().buildSelectionId());
    }
}
