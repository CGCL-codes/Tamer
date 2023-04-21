package com.bluemarsh.jswat.breakpoint;

import java.util.EventObject;

/**
 * An event which indicates that a group has changed status.
 *
 * @author  Nathan Fiedler
 */
public class GroupEvent extends EventObject {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** The group added event type. */
    public static final int TYPE_ADDED = 1;

    /** The group disabled event type. */
    public static final int TYPE_DISABLED = 2;

    /** The group enabled event type. */
    public static final int TYPE_ENABLED = 3;

    /** The group removed event type. */
    public static final int TYPE_REMOVED = 4;

    /** The group that changed.
     * @serial */
    protected BreakpointGroup group;

    /** The type of group change.
     * @serial */
    protected int type;

    /**
     * Constructs a new GroupEvent.
     *
     * @param  source  Source of this event.
     * @param  bp      Group that changed.
     * @param  type    Type of group change.
     */
    public GroupEvent(Object source, BreakpointGroup group, int type) {
        super(source);
        this.group = group;
        this.type = type;
    }

    /**
     * Get the group that changed.
     *
     * @return  group request.
     */
    public BreakpointGroup getGroup() {
        return group;
    }

    /**
     * Get the group change type.
     *
     * @return  group change type (one of TYPE_* from GroupEvent).
     */
    public int getType() {
        return type;
    }

    /**
     * Returns a String representation of this GroupEvent.
     *
     * @return  string representation of this GroupEvent.
     */
    public String toString() {
        return "GroupEvent=[source=" + getSource() + ", group=" + group + ", type=" + type + "]";
    }
}
