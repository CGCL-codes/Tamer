package com.nokia.ats4.appmodel.perspective.modeldesign.event;

import com.nokia.ats4.appmodel.view.event.ViewEvent;

/**
 * NodeNameUpdateEvent
 * 
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class NodeNameUpdateEvent extends ViewEvent {

    private String name;

    private Object renamedObject;

    public NodeNameUpdateEvent(Object source, Object renamedObject, String name) {
        super(source);
        this.name = name;
        this.renamedObject = renamedObject;
    }

    /**
     * Creates a new instance of NodeNameUpdateEvent
     */
    private NodeNameUpdateEvent(Object source, String name) {
        this(source, null, name);
    }

    /**
     * Returns the updated name of the node.
     *
     * @return Name String
     */
    public String getName() {
        return this.name;
    }

    public Object getObject() {
        return this.renamedObject;
    }
}
