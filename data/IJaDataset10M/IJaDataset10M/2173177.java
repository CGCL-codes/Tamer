package com.mindtree.techworks.insight.receiver;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.mindtree.techworks.insight.InsightConstants;
import com.mindtree.techworks.insight.spi.LogNamespace;

/**
*
* The <code>RuntimeNamespaceContainer</code> class is a container for runtime
* namespace data. Holds data on loaded namespaces and available namespace
* colors 
*
* @author  Regunath B
* @version 1.0, 05/08/08
* @see     com.mindtree.techworks.insight.gui.Insight
*/
public class RuntimeNamespaceContainer {

    /**
	 * LinkedList that contains the namespace colors
	 */
    private static LinkedList availableNamespaceColors = new LinkedList();

    /**
	 * Hashmap that contains the namepace color for display
	 */
    private static HashMap namespaceColorMap = new HashMap();

    /**
	 * Set that contains the list of namespaces from which events have been loaded already
	 */
    private static List loadedNamespaces = new LinkedList();

    /**
	 * Gets the Color for distinguishing the specified LogNamespace in the display
	 * @param namespace the LogNamespace for which a Color is needed
	 * @return default Insight background color if all available colors are exhausted or the Color to distinguish the specified LogNamespace
	 */
    public static Color getNamespaceColor(LogNamespace namespace) {
        String namespaceString = namespace.getNamespaceAsString();
        Color namespaceColor = (Color) namespaceColorMap.get(namespaceString);
        if (namespaceColor == null) {
            if (!availableNamespaceColors.isEmpty()) {
                namespaceColor = (Color) availableNamespaceColors.removeFirst();
            } else {
                namespaceColor = InsightConstants.DEFAULT_BACKGROUND;
            }
            namespaceColorMap.put(namespaceString, namespaceColor);
        }
        return namespaceColor;
    }

    /**
	 * Returns the list of loaded namespaces
	 * @return Returns the loadedNamespaces.
	 */
    public static List getLoadedNamespaces() {
        return loadedNamespaces;
    }

    /**
	 * Returns the Map of loaded namespaces
	 * @return Returns the namespaceColorMap.
	 */
    public static HashMap getNamespaceColorMap() {
        return namespaceColorMap;
    }

    /**
	 * Returns the list of vailable namespace colors
	 * @return Returns the availableNamespaceColors.
	 */
    public static LinkedList getAvailableNamespaceColors() {
        return availableNamespaceColors;
    }

    /**
	 * Initializes the available namespace colors
	 */
    public static final void initializeAvailableNamespaceColorList() {
        availableNamespaceColors.clear();
        for (int i = 0; i < InsightConstants.NAMESPACE_COLORS.length; i++) {
            availableNamespaceColors.add(InsightConstants.NAMESPACE_COLORS[i]);
        }
    }

    /**
	 * Clears the set of namespaces contained by this container
	 */
    public static void clearLoadedNamespaces() {
        loadedNamespaces.clear();
        namespaceColorMap.clear();
        initializeAvailableNamespaceColorList();
    }
}
