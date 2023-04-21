package org.docflower.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;

public interface INavigableUIPart {

    public final int NO_NAVIGATION_NEEDED = -1;

    /**
	 * Returns the full path to this navigation part.
	 */
    public String[] getPath();

    /**
	 * Used to generate the unique information for this part for creating the
	 * full path to some navigation place. Each navigation can have some unique
	 * among its siblings string information which can be used to choose this
	 * part among its siblings by parent navigation routine.
	 * 
	 * @param pathList
	 *            the list of all navigation parts information. Before this
	 *            method will put some part's info it must be sure all parents
	 *            put its information first.
	 */
    public void fillPathPart(ArrayList<String> pathList);

    /**
	 * The parent navigation part where this part is registered.
	 * 
	 * @return the parten navigation part.
	 */
    public INavigableUIPart getParent();

    /**
	 * The root part of whole navigation hierarchy.
	 * 
	 * @return the root part.
	 */
    public INavigableUIPart getRoot();

    /**
	 * Get the selected child. Only one child can be selected at the given time.
	 * 
	 * @return currently selected child or null if there is no one child
	 *         selected or no childs at all.
	 */
    public INavigableUIPart getSelectedChild();

    /**
	 * Whether this part is selected.
	 * 
	 * @return true if this part currently is selected or false otherwise
	 */
    public boolean isSelected();

    public int navigate(UpdateInfo updateInfo, int currentNavigationPart);

    public List<INavigableUIPart> getChildren();

    public void addChild(INavigableUIPart child);

    public void removeChild(INavigableUIPart child);

    /**
	 * Clears all cached internal data. Can be called any time and doesn't
	 * affect on the execution. Just recalculation of such data will be forced
	 * next time it will be needed.
	 */
    public void clearCachedData();

    /**
	 * Update the contents of this part and all its children. if updateInfo ==
	 * null means full update with current state.
	 * 
	 * @param updateInfo
	 *            the info how to update.
	 * @param currentNavigationPart
	 *            the index of navigation part to be used for navigate this
	 *            INavigableUIPart. If it is NO_NAVIGATION_NEEDED the no
	 *            navigation is needed.
	 */
    public void update(UpdateInfo updateInfo, int currentNavigationPart);

    /**
	 * Returns the map for the UIPart local actions map. This actions will be
	 * applicable to this part and all of its children.
	 * 
	 * @return the local actions map
	 */
    public Map<String, Node> getLocalActions();
}
