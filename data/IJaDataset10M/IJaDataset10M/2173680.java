package ircam.jmax.editors.patcher.interactions;

import java.awt.*;
import ircam.jmax.fts.*;
import ircam.jmax.dialogs.*;
import ircam.jmax.editors.patcher.*;
import ircam.jmax.editors.patcher.objects.*;

/** The interaction handling help patches; 
  started, and completed, by a AltClick on an object.
  */
class RunPopUpInteraction extends Interaction {

    boolean locked = false;

    void gotSqueack(ErmesSketchPad editor, int squeack, SensibilityArea area, Point mouse, Point oldMouse) {
        GraphicObject object = null;
        if (Squeack.isPopUp(squeack)) {
            locked = true;
            object = (GraphicObject) area.getTarget();
            object.runModePopUpEdit(mouse);
            locked = false;
            editor.endInteraction();
        }
    }
}
