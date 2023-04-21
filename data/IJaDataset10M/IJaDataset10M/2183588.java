package com.nokia.ats4.appmodel.grapheditor.event;

/**
 * PasteCellsEvent
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 2 $
 */
public class PasteCellsEvent extends GraphEditorEvent {

    /** horizontal location of the point */
    private int x = 1;

    /** vertical location point */
    private int y = 1;

    /** Creates a new instance of PasteCellsEvent */
    public PasteCellsEvent(Object source, int x, int y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return y;
    }
}
