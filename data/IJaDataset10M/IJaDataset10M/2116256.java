package net.sourceforge.fluxion.pussycat.entity;

import net.sourceforge.fluxion.pussycat.util.exceptions.PussycatException;
import java.util.Set;
import java.util.Collections;

/**
 * The AbstractDockable abstract class
 * Defines a skeleton class that allows widgets to be docked in dockable areas
 * of a web page
 *
 * @author Rob Davey
 */
public abstract class AbstractDockable implements DockableInPussycat {

    boolean isDocked;

    String parentId;

    String id;

    String buildStr;

    public void dock() {
        isDocked = true;
    }

    public void undock() {
        isDocked = false;
    }

    public final boolean isDocked() {
        return isDocked;
    }

    public RenderableInPussycat getDockedEntity() {
        return null;
    }

    public String getDockingParent() {
        return parentId;
    }

    public void setDockingParent(String id) {
        parentId = id;
    }

    public Set<DockableInPussycat> getDockedChildren() {
        return Collections.emptySet();
    }
}
