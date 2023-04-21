package com.cubusmail.gwtui.client.widgets;

import com.gwtext.client.util.JavaScriptObjectHelper;
import com.gwtextux.client.widgets.grid.plugins.GridSearchPlugin;

/**
 * Quick search plugin for messages.
 * 
 * @author Juergen Schlierf
 */
public class MessageQuickSearchPlugin extends GridSearchPlugin {

    public MessageQuickSearchPlugin(Position toolbarPosition) {
        super(toolbarPosition);
    }

    /**
	 * Empty the search field.
	 * 
	 * @param gridSearchPlugin
	 */
    public native void clearSearchField();

    /**
	 * Disable the search field.
	 * 
	 * @param gridSearchPlugin
	 */
    public native void disableSearchField(boolean disable);

    /**
	 * Set the disabled Indixes
	 * 
	 * @param gridSearchPlugin
	 */
    public void setDisableIndexes(String[] disabled) {
        JavaScriptObjectHelper.setAttribute(configJS, "disableIndexes", disabled);
    }

    ;

    /**
	 * Set the Search Tip Text.
	 * 
	 * @param searchTipText
	 */
    public void setSearchTipText(String searchTipText) {
        JavaScriptObjectHelper.setAttribute(configJS, "searchTipText", searchTipText);
    }
}
