package org.ximtec.igesture.tool.view.admin.action;

import java.awt.event.ActionEvent;
import javax.swing.tree.TreePath;
import org.ximtec.igesture.core.GestureClass;
import org.ximtec.igesture.core.SampleDescriptor;
import org.ximtec.igesture.tool.GestureConstants;
import org.ximtec.igesture.tool.core.Controller;
import org.ximtec.igesture.tool.core.TreePathAction;

public class AddSampleDescriptorAction extends TreePathAction {

    public AddSampleDescriptorAction(Controller controller, TreePath treePath) {
        super(GestureConstants.SAMPLE_DESCRIPTOR_ADD, controller, treePath);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        GestureClass gestureClass = (GestureClass) getTreePath().getLastPathComponent();
        gestureClass.addDescriptor(new SampleDescriptor());
    }
}
