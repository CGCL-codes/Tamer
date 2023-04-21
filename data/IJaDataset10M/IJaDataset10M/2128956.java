package org.xito.desktop;

import org.xito.dcf.*;

/**
 *
 * @author $Author: drichan $
 * @author ocd_dino - ocd_dino@users.sourceforge.net (initial author)
 * @version $Revision: 1.1.1.1 $
 * @since $Date: 2003/09/13 04:39:26 $
 */
public interface DesktopDropListener {

    /**
   * Item was Dropped onto the Desktop
   * This event is only fired after the Desktop has accepted the dropped component
   * @param Component that was dropped on the desktop or the Component that the Desktop Created.
   *
   */
    public void drop(DCComponent pComp);
}
