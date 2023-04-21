package org.rapla.gui.toolkit;

/**  All classes implementing this Interface must call
     FrameControllerList.addFrameController(this) on initialization
     FrameControllerList.removeFrameController(this) on close
     This Class is used for automated close of all Frames on Logout.
*/
public interface FrameController {

    void close();
}
