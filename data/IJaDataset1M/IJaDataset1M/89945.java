package net.sf.doolin.gui.core.view;

import javax.swing.JToolBar;
import net.sf.doolin.gui.core.View;

/**
 * Defines a toolbar separator
 * 
 * @author Damien Coraboeuf
 * @version $Id$
 */
public class ToolbarSep implements ToolbarItem {

    /**
	 * Creates and installs a Swing action.
	 * 
	 * @see net.sf.doolin.gui.core.view.ToolbarItem#createToolbarItem(javax.swing.JToolBar,
	 *      net.sf.doolin.gui.core.View)
	 */
    public void createToolbarItem(JToolBar j, View view) {
        j.addSeparator();
    }
}
