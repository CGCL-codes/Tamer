package com.rapidminer.gui.look.icons;

import java.awt.Dimension;
import javax.swing.Icon;

/**
 * The factory used for creating and holding icon objects. All icons are singletons
 * delivered by the methods of this class.
 *
 * @author Ingo Mierswa
 */
public class IconFactory {

    public static final Dimension MENU_ICON_SIZE = new Dimension(10, 10);

    private static final Icon LIST_VIEW_ICON = new ListViewIcon();

    private static final Icon SLIDER_THUMB_ICON = new SliderThumb();

    private static final Icon RADIO_BUTTON_ICON = new RadioButtonIcon();

    private static final Icon CHECK_BOX_ICON = new CheckBoxIcon();

    private static final Icon CHECK_BOX_MENU_ITEM_ICON = new CheckBoxMenuItemIcon();

    private static final Icon EXPANDED_TREE_ICON = new ExpandedTreeIcon();

    private static final Icon RADIO_BUTTON_MENU_ITEM_ICON = new RadioButtonMenuItemIcon();

    public static Icon getListViewIcon() {
        return LIST_VIEW_ICON;
    }

    public static Icon getSliderThumb() {
        return SLIDER_THUMB_ICON;
    }

    public static Icon getRadioButtonIcon() {
        return RADIO_BUTTON_ICON;
    }

    public static Icon getCheckBoxMenuItemIcon() {
        return CHECK_BOX_MENU_ITEM_ICON;
    }

    public static Icon getCheckBoxIcon() {
        return CHECK_BOX_ICON;
    }

    public static Icon getRadioButtonMenuItemIcon() {
        return RADIO_BUTTON_MENU_ITEM_ICON;
    }

    static Icon getExpandedTreeIcon() {
        return EXPANDED_TREE_ICON;
    }
}
