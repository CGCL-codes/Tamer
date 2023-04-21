package net.infonode.docking.internal;

import net.infonode.docking.*;

/**
 * @author $Author: jesper $
 * @version $Revision: 1.2 $
 */
public class WindowAncestors {

    private DockingWindow[] ancestors;

    private boolean minimized;

    private boolean undocked;

    public WindowAncestors(DockingWindow[] ancestors, boolean minimized, boolean undocked) {
        this.ancestors = ancestors;
        this.minimized = minimized;
        this.undocked = undocked;
    }

    public DockingWindow[] getAncestors() {
        return ancestors;
    }

    public boolean isMinimized() {
        return minimized;
    }

    public boolean isUndocked() {
        return undocked;
    }
}
