package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.shared.*;
import com.bluebrim.transact.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-06-29 09:20:13)
 * @author: Dennis
 */
public abstract class CoShapePageItemUndoCommand extends CoUndoCommand {

    protected final CoShapePageItemIF m_target;

    public CoShapePageItemUndoCommand(String name, CoShapePageItemIF target) {
        super(name);
        m_target = target;
    }
}
