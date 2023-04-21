package ircam.jmax.editors.patcher.actions;

import java.awt.*;
import java.awt.event.*;
import ircam.jmax.*;
import ircam.jmax.editors.patcher.*;
import ircam.jmax.editors.patcher.objects.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.toolkit.actions.*;

public class ScalePatcherAction extends EditorAction {

    public ScalePatcherAction() {
        super("Rescale patcher", "scale", KeyEvent.VK_UNDEFINED, KeyEvent.VK_UNDEFINED, true);
    }

    public void doAction(EditorContainer container) {
        Point aPoint = container.getContainerLocation();
        ScaleDialog dialog = new ScaleDialog((ErmesSketchWindow) container);
        dialog.setBounds(aPoint.x + 200, aPoint.y + 200, 200, 100);
        dialog.setVisible(true);
        dialog = null;
    }
}
