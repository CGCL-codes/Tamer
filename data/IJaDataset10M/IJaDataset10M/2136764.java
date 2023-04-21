package ircam.jmax.editors.patcher.actions;

import java.awt.*;
import java.awt.event.*;
import ircam.jmax.*;
import ircam.jmax.editors.patcher.*;
import ircam.jmax.editors.patcher.objects.*;
import ircam.jmax.toolkit.*;
import ircam.jmax.toolkit.actions.*;

public class BringToFrontObjectAction extends EditorAction {

    public void doAction(EditorContainer container) {
        ((ErmesSketchPad) container.getEditor()).getDisplayList().objectToFront(ObjectPopUp.getPopUpTarget());
        ObjectPopUp.getPopUpTarget().redraw();
    }
}
