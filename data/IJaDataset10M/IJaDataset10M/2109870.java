package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2001-07-03 09:19:48)
 * @author: 
 */
public class CoShapePageItemSetLayoutSpecUndoCommand extends CoShapePageItemSetObjectUndoCommand {

    protected double m_x;

    protected double m_y;

    protected double m_width;

    protected double m_height;

    public CoShapePageItemSetLayoutSpecUndoCommand(String name, CoShapePageItemIF target, CoShapePageItemSetObjectCommand command, Object originalValue, Object newValue) {
        super(name, target, command, originalValue, newValue);
        m_x = target.getX();
        m_y = target.getY();
        java.awt.geom.Rectangle2D d = target.getCoShape().getBounds2D();
        m_width = d.getWidth();
        m_height = d.getHeight();
    }

    public boolean doUndo() {
        Object o = m_originalValue;
        boolean b = super.doUndo();
        if (o instanceof CoImmutableNoLocationIF) {
            m_target.setPosition(m_x, m_y);
        } else if (o instanceof CoImmutableNoSizeSpecIF) {
            m_target.setPosition(m_x, m_y);
            m_target.getMutableCoShape().setSize(m_width, m_height);
        } else if (o instanceof CoImmutableFillSizeSpecIF) {
            m_target.setPosition(m_x, m_y);
        }
        return b;
    }
}
