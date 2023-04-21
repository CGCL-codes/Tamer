package org.ash.gui;

import javax.swing.JSplitPane;

/**
 * The Class EganttSplitPane.
 */
public class GanttSplitPane extends JSplitPane {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3104977481978858679L;

    /**
	 * Instantiates a new egantt split pane.
	 * 
	 * @param type the type
	 */
    public GanttSplitPane(int type) {
        super(type);
        setDividerSize(10);
    }
}
