package com.bluebrim.layout.impl.client.editor;

import com.bluebrim.layout.impl.client.command.*;
import com.bluebrim.layout.impl.shared.view.*;

/**
 * Layout editor operation: Move a page item one position (in z-order) towards back.
 * 
 * @author: Dennis
 */
public class CoSendBackwards extends CoLayoutEditorAction {

    public CoSendBackwards(String name, CoLayoutEditor e) {
        super(name, e);
    }

    public CoSendBackwards(CoLayoutEditor e) {
        this(null, e);
    }

    public void actionPerformed(java.awt.event.ActionEvent arg1) {
        CoShapePageItemView v = m_editor.getCurrentShapePageItemView();
        if (v != null) {
            CoPageItemCommands.SEND_BACKWARDS.prepare(v);
            m_editor.getCommandExecutor().doit(CoPageItemCommands.SEND_BACKWARDS, v);
        }
    }
}
