package org.objectwiz.plugin.uibuilder.runtime.result;

import java.util.HashMap;
import java.util.Map;
import org.objectwiz.plugin.uibuilder.EvaluationContext;
import org.objectwiz.plugin.uibuilder.model.widget.Widget;
import org.objectwiz.plugin.uibuilder.runtime.widget.ParsedWidget;
import org.objectwiz.plugin.uibuilder.runtime.ParsedBoard;
import org.objectwiz.plugin.uibuilder.runtime.ParsedComponent;

/**
 * Result indicating that the current board must be updated.
 * 
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class UpdateBoardActionResult extends ActionResult {

    private Map<Integer, Boolean> newStatuses = new HashMap();

    private Map<Integer, ParsedWidget> updatedWidgets = new HashMap();

    public UpdateBoardActionResult() {
    }

    public UpdateBoardActionResult(ParsedBoard board) {
        for (ParsedWidget widget : board.getWidgets()) {
            registerUpdatedWidget(widget);
        }
    }

    /**
     * Contains the list of widgets that need a full refresh.
     * The key of the map is the ID ({@link Widget#getComponentId()}) of the
     * widget to refresh.
     */
    public Map<Integer, ParsedWidget> getUpdatedWidgets() {
        return updatedWidgets;
    }

    public synchronized void registerUpdatedWidget(ParsedWidget widget) {
        if (updatedWidgets.containsKey(widget.getComponentId())) {
            throw new IllegalStateException("Updated widget with ID=" + widget.getComponentId() + " already registered");
        }
        updatedWidgets.put(widget.getComponentId(), widget);
    }

    /**
     * Contains the new statuses for widgets that need to be enabled/disabled.
     * The key of the map is the ID of the widget ({@link ParsedComponent#getComponentId()}).
     */
    public Map<Integer, Boolean> getNewStatuses() {
        return newStatuses;
    }

    public synchronized void registerUpdatedStatus(int offset, boolean enabled) {
        newStatuses.put(offset, enabled);
    }

    public boolean isEmpty() {
        return (newStatuses == null || newStatuses.isEmpty()) && (updatedWidgets == null || updatedWidgets.isEmpty());
    }

    @Override
    public Object perform(String operationId, EvaluationContext trustedCtx, EvaluationContext parameters) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
