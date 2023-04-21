package org.compiere.grid;

import org.compiere.apps.*;

/**
 *	Application Panel Tab Interface.
 *  Interface for CPanels displayed as a Tab in APanel
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: APanelTab.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public interface APanelTab {

    /**
	 * 	Load Data
	 *  Called when tab is displayed.
	 */
    public void loadData();

    /**
	 * 	Save Data
	 *  Called when tab is swiched to another tab.
	 */
    public void saveData();

    /**
	 * 	Register APanel
	 * 	@param panel panel
	 */
    public void registerAPanel(APanel panel);

    /**
	 * 	Unregister APanel
	 */
    public void unregisterPanel();
}
