package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;

/**
 * A look-up for font and color details.
 * 
 */
public interface ColorsAndFonts {

    public static final String COLOR_BLACK = "color.black";

    public static final String COLOR_WHITE = "color.white";

    public static final String COLOR_PRIMARY1 = "color.primary1";

    public static final String COLOR_PRIMARY2 = "color.primary2";

    public static final String COLOR_PRIMARY3 = "color.primary3";

    public static final String COLOR_SECONDARY1 = "color.secondary1";

    public static final String COLOR_SECONDARY2 = "color.secondary2";

    public static final String COLOR_SECONDARY3 = "color.secondary3";

    public static final String COLOR_APPLICATION = "color.background.application";

    public static final String COLOR_WINDOW = "color.background.window";

    public static final String COLOR_MENU_VALUE = "color.background.menu.value";

    public static final String COLOR_MENU_CONTENT = "color.background.menu.content";

    public static final String COLOR_MENU_VIEW = "color.background.menu.view";

    public static final String COLOR_MENU_WORKSPACE = "color.background.menu.workspace";

    public static final String COLOR_MENU = "color.menu.normal";

    public static final String COLOR_MENU_DISABLED = "color.menu.disabled";

    public static final String COLOR_MENU_REVERSED = "color.menu.reversed";

    public static final String COLOR_LABEL = "color.label.normal";

    public static final String COLOR_LABEL_DISABLED = "color.label.disabled";

    public static final String COLOR_LABEL_MANDATORY = "color.label.mandatory";

    public static final String COLOR_IDENTIFIED = "color.identified";

    public static final String COLOR_VALID = "color.valid";

    public static final String COLOR_INVALID = "color.invalid";

    public static final String COLOR_ERROR = "color.error";

    public static final String COLOR_ACTIVE = "color.active";

    public static final String COLOR_OUT_OF_SYNC = "color.out-of-sync";

    public static final String COLOR_TEXT_SAVED = "color.text.saved";

    public static final String COLOR_TEXT_EDIT = "color.text.edit";

    public static final String COLOR_TEXT_CURSOR = "color.text.cursor";

    public static final String COLOR_TEXT_HIGHLIGHT = "color.text.highlight";

    public static final String COLOR_DEBUG_BASELINE = "color.debug.baseline";

    public static final String COLOR_DEBUG_BOUNDS_BORDER = "color.debug.bounds.border";

    public static final String COLOR_DEBUG_BOUNDS_DRAW = "color.debug.bounds.draw";

    public static final String COLOR_DEBUG_BOUNDS_REPAINT = "color.debug.bounds.repaint";

    public static final String COLOR_DEBUG_BOUNDS_VIEW = "color.debug.bounds.view";

    public static final String TEXT_CONTROL = "text.control";

    public static final String TEXT_TITLE = "text.title";

    public static final String TEXT_TITLE_SMALL = "text.title.small";

    public static final String TEXT_DEBUG = "text.debug";

    public static final String TEXT_STATUS = "text.status";

    public static final String TEXT_ICON = "text.icon";

    public static final String TEXT_LABEL = "text.label";

    public static final String TEXT_LABEL_MANDATORY = "text.label.mandatory";

    public static final String TEXT_LABEL_DISABLED = "text.label.disabled";

    public static final String TEXT_MENU = "text.menu";

    public static final String TEXT_NORMAL = "text.normal";

    int defaultBaseline();

    int defaultFieldHeight();

    Color getColor(int rgbColor);

    Color getColor(String name);

    Text getText(String name);

    void init();
}
