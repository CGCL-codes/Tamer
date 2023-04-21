package com.mia.sct.view.action;

import java.awt.event.ActionEvent;
import com.mia.sct.view.swing.CollapsibleCardPanel;

/**
 * MiaTabAction
 * 
 * Opens (Expands/Collapses) MIA tabs in <code>CollapsibleCardPanel</code>
 *
 * @author Devon Bryant
 * @since Mar 23, 2009
 */
@SuppressWarnings("serial")
public class MiaTabAction extends MiaAction {

    private CollapsibleCardPanel panel = null;

    /**
	 * Constructor
	 * @param inName the tab name
	 * @param inDescription the tab description
	 */
    public MiaTabAction(String inName, String inDescription, CollapsibleCardPanel inPanel) {
        super(inName, inDescription, null, null, null);
        panel = inPanel;
    }

    public void actionPerformed(ActionEvent inE) {
        if (panel != null) {
            panel.showView(getActionName());
        }
    }
}
