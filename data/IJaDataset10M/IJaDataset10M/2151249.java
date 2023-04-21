package net.bervini.rasael.galacticfreedom.gui;

import java.awt.*;

/**
 *
 * @author Rasael Bervini
 */
public class DisplayModeInList {

    private DisplayMode displayMode;

    /** Creates a new instance of DisplayModeInList */
    public DisplayModeInList(DisplayMode displayMode) {
        setDisplayMode(displayMode);
    }

    public DisplayMode getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
    }

    public String toString() {
        return getDisplayMode().getWidth() + "x" + getDisplayMode().getHeight() + " - " + getDisplayMode().getBitDepth() + "bit " + getDisplayMode().getRefreshRate() + "hz";
    }
}
