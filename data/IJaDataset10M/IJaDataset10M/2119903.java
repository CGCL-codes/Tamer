package org.ximtec.igesture.tool.view.admin.action;

import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import org.ximtec.igesture.core.GestureSet;
import org.ximtec.igesture.tool.GestureConstants;
import org.ximtec.igesture.tool.core.Controller;
import org.ximtec.igesture.tool.core.TreePathAction;
import org.ximtec.igesture.tool.view.admin.wrapper.GestureSetList;

public class RemoveGestureSetAction extends TreePathAction {

    public RemoveGestureSetAction(Controller controller, TreePath treePath) {
        super(GestureConstants.GESTURE_SET_DEL, controller, treePath);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        GestureSet gestureSet = (GestureSet) getTreePath().getLastPathComponent();
        GestureSetList rootSet = (GestureSetList) getTreePath().getParentPath().getLastPathComponent();
        rootSet.removeGestureSet(gestureSet);
    }
}
