package uk.ac.lkl.migen.system.expresser.ui.uievent;

import java.util.List;
import uk.ac.lkl.common.util.ID;
import uk.ac.lkl.common.util.IDObject;
import uk.ac.lkl.common.util.Timestamp;
import uk.ac.lkl.common.util.event.EventObject;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;

/**
 * Base class for representing user events. E.g. button clicks, drag start and
 * end, drop, menu actions
 * 
 * @author Ken Kahn
 * 
 */
public abstract class UIEvent<O extends Object> extends EventObject<O> implements IDObject {

    protected ID id;

    protected Timestamp timestamp;

    public UIEvent(O source, Timestamp timestamp) {
        super(source);
        if (timestamp == null) {
            this.timestamp = new Timestamp();
        } else {
            this.timestamp = timestamp;
        }
    }

    public UIEvent(O source) {
        this(source, null);
    }

    public abstract String logMessage();

    public ID getId() {
        return id;
    }

    public String getIdName() {
        return "UIEvent";
    }

    public static String getIDListAsString(List<BlockShape> shapes) {
        StringBuffer list = new StringBuffer("(shapes: ");
        for (BlockShape shape : shapes) {
            list.append(shape.getID() + " ");
        }
        list.append(")");
        return list.toString();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
