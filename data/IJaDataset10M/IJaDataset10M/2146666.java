package org.compiere.plaf;

import java.awt.*;
import javax.swing.plaf.basic.*;

/**
 *  Split Pane Divider
 *
 *  @author     Jorg Janke
 *  @version    $Id: AdempiereSplitPaneDivider.java,v 1.2 2006/07/30 00:52:24 jjanke Exp $
 */
class CompiereSplitPaneDivider extends BasicSplitPaneDivider {

    /**
	 *  Constructor
	 *  @param ui
	 */
    public CompiereSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
        setBorder(null);
    }

    /**
	 *  Paints the divider.
	 *  If the border is painted, it creates a light gray bar on top/button.
	 *  Still, a light gray 1 pt shaddow border is painted on top/button
	 *  @param g
	 */
    public void paint(Graphics g) {
        setBorder(null);
        super.paint(g);
    }
}
