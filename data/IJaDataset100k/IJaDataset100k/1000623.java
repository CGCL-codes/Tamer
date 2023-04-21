package org.objectwiz.uibuilder.model.action.result;

import org.objectwiz.uibuilder.EvaluationContext;
import org.objectwiz.uibuilder.model.action.CloseBoardAction;

/**
 * Result that indicates to the UI that it should close the board that triggered
 * the action.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class CloseBoardActionResult extends ActionResult {

    public CloseBoardActionResult(boolean refreshParentBoard) {
        setRefreshParent(refreshParentBoard);
    }

    @Override
    public Object perform(String operationId, EvaluationContext trustedCtx, EvaluationContext parameters) throws Exception {
        throw new UnsupportedOperationException("No operation available");
    }
}
