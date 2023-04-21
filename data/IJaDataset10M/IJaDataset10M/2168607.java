package apollo.gui.genomemap;

import java.awt.event.*;

/**
 * Interface defining methods for handling a drop in a View
 */
public interface DropTargetViewI {

    public boolean interpretDrop(DragViewI dragView, MouseEvent evt);

    public boolean interpretDrop(DragViewI dragView, MouseEvent evt, boolean doFlag, StringBuffer action);

    public void registerDragSource(TierViewI sourceView);

    public boolean isValidDragSource(TierViewI sourceView);
}
