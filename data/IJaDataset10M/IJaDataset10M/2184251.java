package org.dlib.gui.treeview;

import java.awt.Cursor;

public interface TreeViewDndHandler {

    public static final int ACCEPT_NONE = 0;

    public static final int ACCEPT_INSIDE = 1;

    public static final int ACCEPT_BEFORE = 2;

    public static final int ACCEPT_AFTER = 3;

    /** @param source source node of DnD operation (always != null)
	  * @param dest destination node of DnD operation (may be null)
	  */
    public int acceptDrop(TreeViewNode source, TreeViewNode dest);

    public void handleDrop(TreeViewNode source, TreeViewNode dest);

    public Cursor getNoDropCursor();
}
