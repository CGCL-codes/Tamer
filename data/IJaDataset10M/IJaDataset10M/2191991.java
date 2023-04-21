package com.cofluent.web.client.widget;

import com.cofluent.web.client.util.StyleHelper;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * This class contains primarily constants related to Widgets. Most constants here are the names of various styles for the available
 * widgets.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class WidgetConstants {

    /**
     * The top level (project name) that prefixes all widget classNames.
     */
    public static final String ROCKET = "rocket";

    /**
     * This style is applied to the container OL element of a OrderedListPanel {@link rocket.widget.client.OrderedListPanel}
     */
    public static final String ORDERED_LIST_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "orderedListPanel");

    public static final String ORDERED_LIST = "ol";

    public static final String ORDERED_LIST_ITEM = "li";

    /**
     * This style is applied to the container UL element of a UnOrderedListPanel {@link rocket.widget.client.UnorderedListPanel}
     */
    public static final String UNORDERED_LIST_PANEL_STYLE = StyleHelper.buildCompound(ROCKET, "unorderedListPanel");

    public static final String UNORDERED_LIST = "ul";

    public static final String UNORDERED_LIST_ITEM = "li";
}
