package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Adjust the content scale of an bounded contet box to the bounds of the shape
 * 
 * @author: Dennis
 */
public class CoAdjustContentToShape extends CoLayoutEditorAction {

    public CoAdjustContentToShape(String name, CoLayoutEditor e) {
        super(name, e);
    }

    public CoAdjustContentToShape(CoLayoutEditor e) {
        super(e);
    }

    public void actionPerformed(java.awt.event.ActionEvent arg1) {
        CoPageItemBoundedContentView v = m_editor.getCurrentBoundedContentView();
        if (v != null) {
            CoPageItemCommands.ADJUST_BOUNDED_CONTENT.prepare(v.getOwner(), CoPageItemBoundedContentIF.XY, 0, 0, 0);
            m_editor.getCommandExecutor().doit(CoPageItemCommands.ADJUST_BOUNDED_CONTENT, v);
        }
    }
}
