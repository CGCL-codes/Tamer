package org.blue.shard.graphics.input.keybinding;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class SwitchScreenModeApplicationAction implements IKeyBindingAction {

    private static Logger log = Logger.getLogger(SwitchScreenModeApplicationAction.class);

    public void execute() {
        try {
            Display.setFullscreen(true);
        } catch (LWJGLException e) {
            log.error("LWJGLException while switching to full screen", e);
        }
    }
}
