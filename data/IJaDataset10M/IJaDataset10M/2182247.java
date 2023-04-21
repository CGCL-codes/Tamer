package com.nokia.ats4.appmodel.grapheditor.event;

import org.jgraph.JGraph;

/**
 * CreateTestCasePathEvent holds the selected nodes that form the tes<tcasepath
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class CreateTestCasePathEvent extends GraphEditorEvent {

    /** The graph cells that will be included in the created model */
    private Object[] cells = null;

    private int startOrEnd = -1;

    /**
     * Creates a new instance of CreatesTestCasePathEvent
     */
    public CreateTestCasePathEvent(JGraph source, Object[] cells, int startOrEnd) {
        super(source);
        this.cells = cells;
        this.startOrEnd = startOrEnd;
    }

    /**
     * Get the graph cells that are used to form the use case path.
     *
     * @return Object array of graph cells.
     */
    public Object[] getCells() {
        return this.cells;
    }

    public int getStartOrEnd() {
        return this.startOrEnd;
    }
}
