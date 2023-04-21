package com.nokia.ats4.appmodel.grapheditor.event;

import org.jgraph.JGraph;

/**
 * AddChildModelEvent
 * 
 * 
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class AddChildModelEvent extends GraphEditorEvent {

    public static final int SUB_MODEL = 0;

    public static final int APPLICATION_MODEL = 1;

    private int x;

    private int y;

    private int type;

    /**
     * Creates a new instance of AddChildModelEvent
     */
    public AddChildModelEvent(JGraph source, int x, int y, int type) {
        super(source);
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getType() {
        return this.type;
    }
}
