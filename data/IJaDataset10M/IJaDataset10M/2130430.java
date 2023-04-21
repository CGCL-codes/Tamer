package edu.udo.scaffoldhunter.view.scaffoldtree;

import edu.udo.scaffoldhunter.gui.util.I18nResourceBundleWrapper;

/**
 * @author Philipp Lewe
 * 
 */
public class ScaffoldTreeViewConfigRB extends I18nResourceBundleWrapper {

    @Override
    protected String[] getKeyArray() {
        String keys[] = { "hideSubtreeEdges", "hideSubtreeEdges.shortDescription", "layout" };
        return keys;
    }
}
