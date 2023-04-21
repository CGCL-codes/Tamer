package net.java.dev.joode.metamorphic3D.command;

import net.java.dev.joode.metamorphic3D.Segment;

/**
 * @author Tom Larkworthy
 */
public interface CommandListener {

    public void notifyExecuted(Segment target, boolean error);
}
