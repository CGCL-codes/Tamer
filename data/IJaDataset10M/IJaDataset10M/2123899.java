package de.tu_darmstadt.informatik.rbg.klein.osgi.navigator;

import prefuse.action.assignment.ColorAction;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;

/**
 * 
 * @author Tim Klein
 * @version 0.1
 *
 */
public class TextColorAction extends ColorAction {

    /**
	 * Set node text colors
	 * 
	 */
    public TextColorAction(String group) {
        super(group, VisualItem.TEXTCOLOR, ColorLib.gray(0));
        add("_hover", ColorLib.rgb(255, 0, 0));
    }
}
