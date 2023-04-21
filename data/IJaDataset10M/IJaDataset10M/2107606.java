package net.infonode.docking.drag;

import java.awt.event.*;

/**
 * Provides a {@link DockingWindowDragger}.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.3 $
 * @since IDW 1.3.0
 */
public interface DockingWindowDraggerProvider {

    /**
   * Returns a {@link DockingWindowDragger}.
   *
   * @param mouseEvent the mouse event that started the drag
   * @return the {@link DockingWindowDragger}
   */
    DockingWindowDragger getDragger(MouseEvent mouseEvent);
}
