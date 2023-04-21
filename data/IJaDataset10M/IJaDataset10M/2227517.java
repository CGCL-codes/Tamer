package jcontrol.conect.gui.project;

import jcontrol.conect.gui.design.*;

/**
 * Instances of the ProjectEvent class represent specific design-events
 * that need to be handled by the ProjectDataManager.
 *
 * @author T. F�rster
 * @version $Revision: 1.1.1.1 $
 */
public class ProjectEvent {

    public static final int ITEM_ADDED = 1;

    public static final int ITEM_REMOVED = 2;

    public static final int ITEM_CHANGED = 3;

    public static final int ITEM_MOVED = 4;

    public static final int LINK_TO_FUNCTION = 6;

    public static final int LINK_TO_ROOM = 7;

    public static final int PROJECT = 1;

    public static final int GROUPS = 2;

    public static final int TOPOLOGY = 3;

    public static final int FUNCTION = 4;

    public static final int GLOBAL = 5;

    protected DragDropItem source;

    protected int action;

    protected int type;

    protected String roomPath;

    protected String functionPath;

    public ProjectEvent(DragDropItem source, int action, int type, String roomPath, String functionPath) {
        this.source = source;
        this.action = action;
        this.type = type;
        this.roomPath = roomPath;
        this.functionPath = functionPath;
    }

    public ProjectEvent(DragDropItem source, int action, int type) {
        this(source, action, type, null, null);
    }

    public ProjectEvent(DragDropItem source, String roomPath, String functionPath) {
        this(source, ITEM_ADDED, GLOBAL, roomPath, functionPath);
    }

    public DragDropItem getSource() {
        return source;
    }

    public int getAction() {
        return action;
    }

    public int getType() {
        return type;
    }

    public String getRoomPath() {
        return roomPath;
    }

    public String getFunctionPath() {
        return functionPath;
    }

    /** Descriptions for action-ids */
    protected static final String[] ACTIONS = new String[] { "ITEM_ADDED", "ITEM_REMOVED", "ITEM_CHANGED", "ITEM_MOVED", "???", "LINK_TO_FUNCTION", "LINK_TO_ROOM" };

    /** Descriptions for type-ids */
    protected static final String[] TYPES = new String[] { "PROJECT", "GROUPS", "TOPOLOGY", "FUNCTION", "GLOBAL" };

    public String toString() {
        if ((roomPath != null) || (functionPath != null)) {
            return "ProjectEvent:[src=" + source + ", " + ACTIONS[action - 1] + ", " + TYPES[type - 1] + ", room=" + roomPath + ", function=" + functionPath + "]";
        } else {
            return "ProjectEvent:[src=" + source + ", " + ACTIONS[action - 1] + ", " + TYPES[type - 1] + "]";
        }
    }
}
