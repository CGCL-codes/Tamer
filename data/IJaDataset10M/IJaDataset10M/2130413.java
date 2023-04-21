package com.tapina.robe.swi.wimp;

import com.tapina.robe.swi.os.SpriteArea;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class holds a RISC OS Wimp window block. A window block is the definition of a window, either created or not.
 */
public class WindowBlock {

    /** Special constant for handleOfWindowAbove */
    public static final int TOP = -1, BOTTOM = -2, ICONISED = -3;

    /** Window flags constant */
    public static final int OLD_TITLE_BAR = 1, MOVEABLE = 2, OLD_VERTICAL_SCROLL_BAR = 4, OLD_HORIZONTAL_SCROLL_BAR = 8, NO_USER_REDRAW = 16, PANE = 32, UNBOUNDED_MOVE = 64, OLD_NO_CLOSE = 128, SCROLL_REQUEST_AUTO = 256, SCROLL_REQUEST_NON_AUTO = 512, GCOL_COLOURS = 1024, BACKGROUND_WINDOW = 2048, TRAP_KEYPRESSES = 4096, KEEP_ON_SCREEN = 8192, NO_RESIZE_RIGHT = 1 << 14, NO_RESIZE_DOWN = 1 << 15, OPEN = 1 << 16, FULLY_VISIBLE = 1 << 17, FULL_SIZE = 1 << 18, OPEN_WINDOW_REQUEST = 1 << 19, HAS_INPUT_FOCUS = 1 << 20, FORCE_TO_SCREEN = 1 << 21, TOGGLE_METHOD = 1 << 22, FURNITURE_WINDOW = 1 << 23, FOREGROUND_WINDOW = 1 << 23, BACK_ICON = 1 << 24, CLOSE_ICON = 1 << 25, TITLE_BAR = 1 << 26, TOGGLE_SIZE = 1 << 27, VERTICAL_SCROLL_BAR = 1 << 28, ADJUST_SIZE = 1 << 29, HORIZONTAL_SCROLL_BAR = 1 << 30, NEW_FORMAT = 1 << 31;

    /** Special constant for titleForegroundColour */
    public static final int NO_FRAME = 0xff;

    /** Special constant for workAreaBackgroundColour */
    public static final int NO_WIMP_CLEAR = 0xff;

    /** Window extra flags constant */
    public static final int USE_24_BIT_COLOUR = 1, NO_3D_BORDER = 4, FORCE_3D_BORDER = 8;

    private Rectangle visibleArea;

    private Rectangle workArea;

    private Point scrollOffset;

    private int handleOfWindowAbove;

    private int windowFlags;

    private int windowExtraFlags;

    private int titleForegroundColour;

    private int titleBackgroundColour;

    private int workAreaForegroundColour;

    private int workAreaBackgroundColour;

    private int scrollBarOuterColour;

    private int scrollBarInnerColour;

    private int activeTitleBackgroundColour;

    private int titleBarIconFlags;

    private int workAreaButtonFlags;

    private SpriteArea spriteArea;

    private Dimension minimumSize;

    private String titleText;

    private int titleIndirectAddress;

    private java.util.List icons = new ArrayList();

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getTitleIndirectAddress() {
        return titleIndirectAddress;
    }

    public void setTitleIndirectAddress(int titleIndirectAddress) {
        this.titleIndirectAddress = titleIndirectAddress;
    }

    public Rectangle getVisibleArea() {
        return visibleArea;
    }

    public void setVisibleArea(Rectangle visibleArea) {
        this.visibleArea = visibleArea;
    }

    public Rectangle getWorkArea() {
        return workArea;
    }

    public void setWorkArea(Rectangle workArea) {
        this.workArea = workArea;
    }

    public Point getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(Point scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public int getHandleOfWindowAbove() {
        return handleOfWindowAbove;
    }

    public void setHandleOfWindowAbove(int handleOfWindowAbove) {
        this.handleOfWindowAbove = handleOfWindowAbove;
    }

    public int getWindowFlags() {
        return windowFlags;
    }

    public void setWindowFlags(int windowFlags) {
        this.windowFlags = windowFlags;
    }

    public int getWindowExtraFlags() {
        return windowExtraFlags;
    }

    public void setWindowExtraFlags(int windowExtraFlags) {
        this.windowExtraFlags = windowExtraFlags;
    }

    public int getTitleForegroundColour() {
        return titleForegroundColour;
    }

    public void setTitleForegroundColour(int titleForegroundColour) {
        this.titleForegroundColour = titleForegroundColour;
    }

    public int getTitleBackgroundColour() {
        return titleBackgroundColour;
    }

    public void setTitleBackgroundColour(int titleBackgroundColour) {
        this.titleBackgroundColour = titleBackgroundColour;
    }

    public int getWorkAreaForegroundColour() {
        return workAreaForegroundColour;
    }

    public void setWorkAreaForegroundColour(int workAreaForegroundColour) {
        this.workAreaForegroundColour = workAreaForegroundColour;
    }

    public int getWorkAreaBackgroundColour() {
        return workAreaBackgroundColour;
    }

    public void setWorkAreaBackgroundColour(int workAreaBackgroundColour) {
        this.workAreaBackgroundColour = workAreaBackgroundColour;
    }

    public int getScrollBarOuterColour() {
        return scrollBarOuterColour;
    }

    public void setScrollBarOuterColour(int scrollBarOuterColour) {
        this.scrollBarOuterColour = scrollBarOuterColour;
    }

    public int getScrollBarInnerColour() {
        return scrollBarInnerColour;
    }

    public void setScrollBarInnerColour(int scrollBarInnerColour) {
        this.scrollBarInnerColour = scrollBarInnerColour;
    }

    public int getActiveTitleBackgroundColour() {
        return activeTitleBackgroundColour;
    }

    public void setActiveTitleBackgroundColour(int activeTitleBackgroundColour) {
        this.activeTitleBackgroundColour = activeTitleBackgroundColour;
    }

    public int getTitleBarIconFlags() {
        return titleBarIconFlags;
    }

    public void setTitleBarIconFlags(int titleBarIconFlags) {
        this.titleBarIconFlags = titleBarIconFlags;
    }

    public int getWorkAreaButtonFlags() {
        return workAreaButtonFlags;
    }

    public void setWorkAreaButtonFlags(int workAreaButtonFlags) {
        this.workAreaButtonFlags = workAreaButtonFlags;
    }

    public SpriteArea getSpriteArea() {
        return spriteArea;
    }

    public void setSpriteArea(SpriteArea spriteArea) {
        this.spriteArea = spriteArea;
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    public void setMinimumSize(Dimension minimumSize) {
        this.minimumSize = minimumSize;
    }

    public void addIcon(Icon icon) {
        this.icons.add(icon);
    }

    public void removeIcon(int index) {
        icons.remove(index);
    }

    public Icon getIcon(int index) {
        return (Icon) icons.get(index);
    }

    public int getNumberOfIcons() {
        return icons.size();
    }

    public Iterator icons() {
        return icons.iterator();
    }
}
