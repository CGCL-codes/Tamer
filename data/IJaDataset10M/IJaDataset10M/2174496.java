package com.varaneckas.hawkscope.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.varaneckas.hawkscope.util.OSUtils;

/**
 * Shared SWT GUI style elements and graphical resources
 * 
 * @author Tomas Varaneckas
 * @version $Id: SharedStyle.java 452 2009-03-16 19:56:43Z tomas.varaneckas $
 */
public class SharedStyle {

    /**
	 * Shared layout 
	 */
    public static final FormLayout LAYOUT = new FormLayout();

    /**
	 * Minimum Button width
	 */
    public static final int BUTTON_MIN_WIDTH = 50;

    /**
     * Bold font
     */
    public static Font FONT_BOLD;

    /**
     * Fixed font
     */
    public static Font FONT_FIXED;

    /**
     * Red color
     */
    public static Color COLOR_RED;

    /**
     * Green color
     */
    public static Color COLOR_GREEN;

    /**
     * Blue color
     */
    public static Color COLOR_BLUE;

    /**
     * Hand cursor
     */
    public static Cursor CURSOR_HAND;

    /**
     * Offset for text top margin when text is centered with a label
     */
    public static final int TEXT_TOP_OFFSET_ADJUST = OSUtils.getTextTopOffsetAdjust();

    /**
     * Creates shared resources
     */
    private static void createResources() {
        LAYOUT.spacing = 12;
        LAYOUT.marginWidth = 12;
        LAYOUT.marginHeight = 12;
        final Display display = Display.getDefault();
        final FontData data = new FontData();
        data.setHeight(10);
        data.setStyle(SWT.BOLD);
        FONT_BOLD = new Font(display, data);
        data.setName(OSUtils.getFixedFontName());
        data.setStyle(SWT.NONE);
        FONT_FIXED = new Font(display, data);
        COLOR_RED = new Color(display, 255, 0, 0);
        COLOR_GREEN = new Color(display, 0, 128, 0);
        COLOR_BLUE = new Color(display, 0, 0, 255);
        CURSOR_HAND = new Cursor(display, SWT.CURSOR_HAND);
    }

    /**
     * Releases shared resources
     */
    public static void releaseResources() {
        FONT_BOLD.dispose();
        FONT_FIXED.dispose();
        COLOR_RED.dispose();
        COLOR_GREEN.dispose();
        COLOR_BLUE.dispose();
        CURSOR_HAND.dispose();
    }

    /**
	 * Initialize the resources
	 */
    static {
        createResources();
    }

    /**
     * Gets layout data ({@link FormData}) for a Widget that should be 
     * positioned relative to top-left corner.
     * 
     * @param top Control above the target Widget
     * @param left Control in the left of target Widget
     * @return layout data
     */
    public static FormData relativeTo(final Control top, final Control left) {
        final FormData layout = new FormData();
        layout.top = new FormAttachment(top);
        layout.left = new FormAttachment(left);
        return layout;
    }

    /**
     * Gets layout data ({@link FormData}) for a Widget that should be relative
     * to four corners of the {@link Shell} container. Useful for resizing.
     * 
     * @param top Top Control. null means top edge.
     * @param right Right Control. null means right edge.
     * @param bottom Bottom Control. null means bottom edge.
     * @param left Left Control. null means left edge.
     * @return layout data
     */
    public static FormData relativeTo(final Control top, final Control right, final Control bottom, final Control left) {
        final FormData layout = relativeTo(top, left);
        if (bottom != null) {
            layout.bottom = new FormAttachment(bottom);
        } else {
            layout.bottom = new FormAttachment(100, 0);
        }
        if (right != null) {
            layout.right = new FormAttachment(right);
        } else {
            layout.right = new FormAttachment(100, 0);
        }
        return layout;
    }

    /**
     * Gets layout data ({@link FormData}) for a Widget that should be 
     * positioned relative to botom-right corner.
     * 
     * @param right Control in the right of target Widget
     * @return layout data
     */
    public static FormData relativeToBottomRight(final Control right) {
        final FormData layout = new FormData();
        layout.bottom = new FormAttachment(100, 0);
        if (right == null) {
            layout.right = new FormAttachment(100, 0);
        } else {
            layout.right = new FormAttachment(right);
        }
        return layout;
    }
}
