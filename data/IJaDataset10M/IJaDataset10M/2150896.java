package de.esoco.j2me.ui;

import de.esoco.j2me.model.HierarchyNode;
import javax.microedition.lcdui.Choice;

/*****************************************************************************
 * Interface for a Displayable instance that displays the children of a
 * HierarchyNode It is used by the {@link de.esoco.j2me.ui.HierarchyView} class
 * to display the child nodes of a particular hierarchy node. To be usable
 * implementations of this interface must also be subclasses of the
 * {@link javax.microedition.lcdui.Displayable} class.
 *
 * @author  Elmar Sonnenschein
 * @version $Revision: 1.1 $
 */
public interface HierarchyNodeDisplay {

    /***************************************
	 * Returns the Choice implementation that is used to display the list of
	 * child nodes.
	 *
	 * @return The Choice that is used to display child nodes
	 */
    public Choice getListView();

    /***************************************
	 * Returns the root node of which the children are displayed in this
	 * display.
	 *
	 * @return The root node
	 */
    public HierarchyNode getRoot();

    /***************************************
	 * Returns the hierarchy node corresponding to the currently selected list
	 * entry.
	 *
	 * @return The currently selected node or NULL for none
	 */
    public HierarchyNode getSelectedNode();
}
