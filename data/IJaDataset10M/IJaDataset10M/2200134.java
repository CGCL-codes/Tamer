package jmax.editors.patcher.interactions;

import java.awt.*;
import jmax.editors.patcher.*;
import jmax.editors.patcher.objects.*;

/** The interaction handling moving controllers in Run mode; 
  started by a control click on an object;
  Note that since the proper event sequence is guaranteed by the
  input system (i.e. Ctrl-Down [Drag]* Up), we don't need to keep track
  of the status here, so we have no real state-machine.
  */
class RunCtrlInteraction extends Interaction {

    GraphicObject object;

    void configureInputFilter(InteractionEngine filter) {
        filter.setFollowingMoves(true);
    }

    void gotSqueack(ErmesSketchPad editor, int squeack, SensibilityArea area, Point mouse, Point oldMouse) {
        if (Squeack.isDown(squeack) && (Squeack.onObject(squeack) || Squeack.onText(squeack))) object = (GraphicObject) area.getTarget();
        if (object != null) {
            squeack &= (~Squeack.LOCATION_MASK);
            if (Squeack.isDrag(squeack)) object.gotSqueack(squeack, mouse, oldMouse); else if (Squeack.isDown(squeack)) object.gotSqueack(squeack, mouse, oldMouse); else if (Squeack.isUp(squeack)) {
                object.gotSqueack(squeack, mouse, oldMouse);
                editor.endInteraction();
            }
        }
    }
}
