package org.xmlhammer.gui.actions;

import javax.swing.ImageIcon;
import org.bounce.RunnableAction;
import org.xmlhammer.gui.XMLHammer;

/**
 * An action that can be used to show Properties for a specific page.
 *
 * @version	$Revision$, $Date$
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class HelpSearchAction extends RunnableAction {

    private static final long serialVersionUID = 3258134648029526321L;

    private XMLHammer parent = null;

    /**
	 * The constructor for the action which shows Properties for a specific page.
     * 
     * @param page the page the properties should be shown for.
	 */
    public HelpSearchAction(XMLHammer parent) {
        super("Search");
        putValue(MNEMONIC_KEY, new Integer('S'));
        putValue(SHORT_DESCRIPTION, "Help Search");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/org/xmlhammer/gui/icons/etool16/helpsearch_co.gif")));
        this.parent = parent;
    }

    /**
	 * The implementation of the Page Properties action, called 
	 * after a user action.
	 */
    public void run() {
        parent.showHelpSearch();
    }
}
