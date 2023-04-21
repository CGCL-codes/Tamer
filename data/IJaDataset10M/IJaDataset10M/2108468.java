package com.sun.lwuit.plaf;

import com.sun.lwuit.Component;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Painter;
import com.sun.lwuit.RGBImage;
import com.sun.lwuit.geom.Rectangle;
import java.lang.ref.WeakReference;

/**
 * Base class that allows us to render a border for a component, a border is drawn before
 * the component and is drawn within the padding region of the component. It is the
 * responsibility of the component not to draw outside of the border line.
 * <p>This class can be extended to provide additional border types and custom made
 * border types.
 * <p>A border can optionally paint the background of the component, this depends on
 * the border type and is generally required for rounded borders that "know" the area
 * that should be filled.
 *
 * @author Shai Almog
 */
public class Border {

    private static Border defaultBorder = Border.createEtchedRaised(0x020202, 0xBBBBBB);

    private static final int TYPE_EMPTY = 0;

    /**
     * Allows clients of LWUIT to subclass Border.
     * 
     * BlueWhaleSystems fix -- Michael Maguire -- 03 Nov 2009
     * 
     * Scope changed to protected because we need to be able to subclass
     * Border to meet requirements for a Border which paints a line of specified 
     * thickness and color underneath the component.
     *
     * See ticket:3734 Client: UI components should be separated by a thin grey line
     */
    protected static final int TYPE_LINE = 1;

    private static final int TYPE_ROUNDED = 2;

    private static final int TYPE_ROUNDED_PRESSED = 3;

    private static final int TYPE_ETCHED_LOWERED = 4;

    private static final int TYPE_ETCHED_RAISED = 5;

    private static final int TYPE_BEVEL_RAISED = 6;

    private static final int TYPE_BEVEL_LOWERED = 7;

    private static final int TYPE_IMAGE = 8;

    /**
     * Allows clients of LWUIT to subclass Border.
     * 
     * BlueWhaleSystems fix -- Michael Maguire -- 03 Nov 2009
     * 
     * Scope changed to protected because we need to be able to subclass
     * Border to meet requirements for a Border which paints a line of specified 
     * thickness and color underneath the component.
     *
     * See ticket:3734 Client: UI components should be separated by a thin grey line
     */
    protected int type;

    Image[] images;

    /**
     * Allows clients of LWUIT to subclass Border.
     * 
     * BlueWhaleSystems fix -- Michael Maguire -- 03 Nov 2009
     * 
     * Scope changed to protected because we need to be able to subclass
     * Border to meet requirements for a Border which paints a line of specified 
     * thickness and color underneath the component.
     *
     * See ticket:3734 Client: UI components should be separated by a thin grey line
     */
    protected boolean themeColors;

    /**
     * Allows clients of LWUIT to subclass Border.
     * 
     * BlueWhaleSystems fix -- Michael Maguire -- 03 Nov 2009
     * 
     * Scope changed to protected because we need to be able to subclass
     * Border to meet requirements for a Border which paints a line of specified 
     * thickness and color underneath the component.
     *
     * See ticket:3734 Client: UI components should be separated by a thin grey line
     */
    protected int colorA;

    int colorB;

    int colorC;

    int colorD;

    /**
     * Allows clients of LWUIT to subclass Border.
     * 
     * BlueWhaleSystems fix -- Michael Maguire -- 03 Nov 2009
     * 
     * Scope changed to protected because we need to be able to subclass
     * Border to meet requirements for a Border which paints a line of specified 
     * thickness and color underneath the component.
     *
     * See ticket:3734 Client: UI components should be separated by a thin grey line
     */
    protected int thickness;

    int arcWidth;

    int arcHeight;

    boolean outline = true;

    Border pressedBorder;

    Border focusBorder;

    private static Border empty;

    /**
     * Prevents usage of new operator, use the factory methods in the class or subclass
     * to create new border types.
     */
    protected Border() {
    }

    /**
     * Returns an empty border, this is mostly useful for overriding components that
     * have a border by default
     * 
     * @return a border than draws nothing
     * @deprecated use createEmpty instead
     */
    public static Border getEmpty() {
        if (empty == null) {
            empty = new Border();
        }
        return empty;
    }

    /**
     * Creates an empty border, this is useful where we don't want a border for a 
     * component but want a focus border etc...
     * 
     * @return a border than draws nothing
     */
    public static Border createEmpty() {
        return new Border();
    }

    /**
     * The given images are tiled appropriately across the matching side of the border and placed
     * as expected in the four corners. The background image is optional and it will be tiled in
     * the background if necessary.
     * <p>By default this border does not override background unless a background image is specified
     * 
     * @param top the image of the top line
     * @param bottom the image of the bottom line
     * @param left the image of the left line
     * @param right the image of the right line
     * @param topLeft the image of the top left corner
     * @param topRight the image of the top right corner
     * @param bottomLeft the image of the bottom left corner
     * @param bottomRight the image of the bottom right corner
     * @param background the image of the background (optional)
     * @return new border instance
     */
    public static Border createImageBorder(Image top, Image bottom, Image left, Image right, Image topLeft, Image topRight, Image bottomLeft, Image bottomRight, Image background) {
        Border b = new Border();
        b.type = TYPE_IMAGE;
        b.images = new Image[] { top, bottom, left, right, topLeft, topRight, bottomLeft, bottomRight, background };
        return b;
    }

    /**
     * The given images are tiled appropriately across the matching side of the border, rotated and placed
     * as expected in the four corners. The background image is optional and it will be tiled in
     * the background if necessary.
     * <p>By default this border does not override background unless a background image is specified.
     * <p>Notice that this version of the method is potentially much more efficient since images
     * are rotated internally and this might save quite a bit of memory!
     * <p><b>The top and topLeft images must be square!</b> The width and height of these images
     * must be equal otherwise rotation won't work as you expect.
     * 
     * @param top the image of the top line
     * @param topLeft the image of the top left corner
     * @param background the image of the background (optional)
     * @return new border instance
     */
    public static Border createImageBorder(Image top, Image topLeft, Image background) {
        Border b = new Border();
        b.type = TYPE_IMAGE;
        b.images = new Image[] { top, top.rotate(180), top.rotate(270), top.rotate(90), topLeft, topLeft.rotate(90), topLeft.rotate(270), topLeft.rotate(180), background };
        return b;
    }

    /**
     * Creates a line border that uses the color of the component foreground for drawing
     * 
     * @param thickness thickness of the boder in pixels
     * @return new border instance
     */
    public static Border createLineBorder(int thickness) {
        Border b = new Border();
        b.type = TYPE_LINE;
        b.themeColors = true;
        b.thickness = thickness;
        return b;
    }

    /**
     * Creates a line border that uses the given color for the component
     * 
     * @param thickness thickness of the boder in pixels
     * @param color the color for the border
     * @return new border instance
     */
    public static Border createLineBorder(int thickness, int color) {
        Border b = new Border();
        b.type = TYPE_LINE;
        b.themeColors = false;
        b.thickness = thickness;
        b.colorA = color;
        return b;
    }

    /**
     * Creates a rounded corner border that uses the color of the component foreground for drawing.
     * Due to technical issues (lack of shaped clipping) performance and memory overhead of round 
     * borders can be low if used with either a bgImage or translucency! 
     * <p>This border overrides any painter used on the component and would ignor such a painter.
     * 
     * @param arcWidth the horizontal diameter of the arc at the four corners.
     * @param arcHeight the vertical diameter of the arc at the four corners.
     * @return new border instance
     */
    public static Border createRoundBorder(int arcWidth, int arcHeight) {
        Border b = new Border();
        b.type = TYPE_ROUNDED;
        b.themeColors = true;
        b.arcHeight = arcHeight;
        b.arcWidth = arcWidth;
        return b;
    }

    /**
     * Creates a rounded corner border that uses the color of the component foreground for drawing.
     * Due to technical issues (lack of shaped clipping) performance and memory overhead of round
     * borders can be low if used with either a bgImage or translucency!
     * <p>This border overrides any painter used on the component and would ignor such a painter.
     *
     * @param arcWidth the horizontal diameter of the arc at the four corners.
     * @param arcHeight the vertical diameter of the arc at the four corners.
     * @param outline whether the round rect border outline should be drawn
     * @return new border instance
     */
    public static Border createRoundBorder(int arcWidth, int arcHeight, boolean outline) {
        Border b = createRoundBorder(arcWidth, arcHeight);
        b.outline = outline;
        return b;
    }

    /**
     * Creates a rounded border that uses the given color for the component.
     * Due to technical issues (lack of shaped clipping) performance and memory overhead of round 
     * borders can be low if used with either a bgImage or translucency! 
     * <p>This border overrides any painter used on the component and would ignor such a painter.
     * 
     * @param arcWidth the horizontal diameter of the arc at the four corners.
     * @param arcHeight the vertical diameter of the arc at the four corners.
     * @param color the color for the border
     * @return new border instance
     */
    public static Border createRoundBorder(int arcWidth, int arcHeight, int color) {
        Border b = new Border();
        b.type = TYPE_ROUNDED;
        b.themeColors = false;
        b.colorA = color;
        b.arcHeight = arcHeight;
        b.arcWidth = arcWidth;
        return b;
    }

    /**
     * Creates a rounded border that uses the given color for the component.
     * Due to technical issues (lack of shaped clipping) performance and memory overhead of round
     * borders can be low if used with either a bgImage or translucency!
     * <p>This border overrides any painter used on the component and would ignor such a painter.
     *
     * @param arcWidth the horizontal diameter of the arc at the four corners.
     * @param arcHeight the vertical diameter of the arc at the four corners.
     * @param color the color for the border
     * @param outline whether the round rect border outline should be drawn
     * @return new border instance
     */
    public static Border createRoundBorder(int arcWidth, int arcHeight, int color, boolean outline) {
        Border b = createRoundBorder(arcWidth, arcHeight, color);
        b.outline = outline;
        return b;
    }

    /**
     * Creates a lowered etched border with default colors, highlight is derived
     * from the component and shadow is a plain dark color
     * 
     * @return new border instance
     */
    public static Border createEtchedLowered() {
        Border b = new Border();
        b.type = TYPE_ETCHED_LOWERED;
        b.themeColors = true;
        return b;
    }

    /**
     * Creates a raised etched border with the given colors
     * 
     * @param highlight color RGB value
     * @param shadow color RGB value
     * @return new border instance
     */
    public static Border createEtchedLowered(int highlight, int shadow) {
        Border b = new Border();
        b.type = TYPE_ETCHED_LOWERED;
        b.themeColors = false;
        b.colorA = shadow;
        b.colorB = highlight;
        return b;
    }

    /**
     * Creates a lowered etched border with default colors, highlight is derived
     * from the component and shadow is a plain dark color
     * 
     * @return new border instance
     */
    public static Border createEtchedRaised() {
        Border b = new Border();
        b.type = TYPE_ETCHED_RAISED;
        b.themeColors = true;
        b.thickness = 2;
        return b;
    }

    /**
     * Creates a raised etched border with the given colors
     * 
     * @param highlight color RGB value
     * @param shadow color RGB value
     * @return new border instance
     */
    public static Border createEtchedRaised(int highlight, int shadow) {
        Border b = new Border();
        b.type = TYPE_ETCHED_RAISED;
        b.themeColors = false;
        b.colorA = highlight;
        b.colorB = shadow;
        b.thickness = 2;
        return b;
    }

    /**
     * Returns true if installing this border will override the painting of the component background
     * 
     * @return true if this border replaces the painter
     */
    public boolean isBackgroundPainter() {
        return type == TYPE_ROUNDED || type == TYPE_ROUNDED_PRESSED || (type == TYPE_IMAGE);
    }

    /**
     * Creates a lowered bevel border with default colors, highlight is derived
     * from the component and shadow is a plain dark color
     * 
     * @return new border instance
     */
    public static Border createBevelLowered() {
        Border b = new Border();
        b.type = TYPE_BEVEL_LOWERED;
        b.themeColors = true;
        b.thickness = 2;
        return b;
    }

    /**
     * Creates a raised bevel border with the given colors
     * 
     * @param highlightOuter  RGB of the outer edge of the highlight area
     * @param highlightInner  RGB of the inner edge of the highlight area
     * @param shadowOuter     RGB of the outer edge of the shadow area
     * @param shadowInner     RGB of the inner edge of the shadow area
     * @return new border instance
     */
    public static Border createBevelLowered(int highlightOuter, int highlightInner, int shadowOuter, int shadowInner) {
        Border b = new Border();
        b.type = TYPE_BEVEL_LOWERED;
        b.themeColors = false;
        b.colorA = highlightOuter;
        b.colorB = highlightInner;
        b.colorC = shadowOuter;
        b.colorD = shadowInner;
        b.thickness = 2;
        return b;
    }

    /**
     * Creates a lowered bevel border with default colors, highlight is derived
     * from the component and shadow is a plain dark color
     * 
     * @return new border instance
     */
    public static Border createBevelRaised() {
        Border b = new Border();
        b.type = TYPE_BEVEL_RAISED;
        b.themeColors = true;
        b.thickness = 2;
        return b;
    }

    /**
     * Creates a raised bevel border with the given colors
     * 
     * @param highlightOuter  RGB of the outer edge of the highlight area
     * @param highlightInner  RGB of the inner edge of the highlight area
     * @param shadowOuter     RGB of the outer edge of the shadow area
     * @param shadowInner     RGB of the inner edge of the shadow area
     * @return new border instance
     */
    public static Border createBevelRaised(int highlightOuter, int highlightInner, int shadowOuter, int shadowInner) {
        Border b = new Border();
        b.type = TYPE_BEVEL_RAISED;
        b.themeColors = false;
        b.colorA = highlightOuter;
        b.colorB = highlightInner;
        b.colorC = shadowOuter;
        b.colorD = shadowInner;
        b.thickness = 2;
        return b;
    }

    /**
     * Allows us to define a border that will act as the pressed version of this border
     * 
     * @param pressed a border that will be returned by the pressed version method
     */
    public void setPressedInstance(Border pressed) {
        pressedBorder = pressed;
    }

    /**
     * Allows us to define a border that will act as the focused version of this border
     * 
     * @param focused a border that will be returned by the focused version method
     * @deprecated use the getSelectedStyle() method in the component class
     */
    public void setFocusedInstance(Border focused) {
        focusBorder = focused;
    }

    /**
     * Returns the focused version of the border if one is installed
     * 
     * @return a focused version of this border if one exists
     * @deprecated use the getSelectedStyle() method in the component class
     */
    public Border getFocusedInstance() {
        if (focusBorder != null) {
            return focusBorder;
        }
        return this;
    }

    /**
     * Returns the pressed version of the border if one is set by the user
     * 
     * @return the pressed instance of this border
     */
    public Border getPressedInstance() {
        if (pressedBorder != null) {
            return pressedBorder;
        }
        return this;
    }

    /**
     * When applied to buttons borders produce a version that reverses the effects 
     * of the border providing a pressed feel
     * 
     * @return a border that will make the button feel pressed
     */
    public Border createPressedVersion() {
        if (pressedBorder != null) {
            return pressedBorder;
        }
        switch(type) {
            case TYPE_LINE:
                return createLineBorder(thickness + 1, colorA);
            case TYPE_ETCHED_LOWERED:
                {
                    Border b = createEtchedRaised(colorA, colorB);
                    b.themeColors = themeColors;
                    return b;
                }
            case TYPE_ETCHED_RAISED:
                {
                    Border b = createEtchedLowered(colorA, colorB);
                    b.themeColors = themeColors;
                    return b;
                }
            case TYPE_BEVEL_RAISED:
                {
                    Border b = createBevelLowered(colorA, colorB, colorC, colorD);
                    b.themeColors = themeColors;
                    return b;
                }
            case TYPE_BEVEL_LOWERED:
                {
                    Border b = createBevelRaised(colorA, colorB, colorC, colorD);
                    b.themeColors = themeColors;
                    return b;
                }
            case TYPE_ROUNDED:
                {
                    Border b = createRoundBorder(arcWidth, arcHeight, colorA);
                    b.themeColors = themeColors;
                    b.type = TYPE_ROUNDED_PRESSED;
                    return b;
                }
            case TYPE_ROUNDED_PRESSED:
                {
                    Border b = createRoundBorder(arcWidth, arcHeight, colorA);
                    b.themeColors = themeColors;
                    return b;
                }
        }
        return this;
    }

    /**
     * Has effect when the border demands responsibility for background painting
     * normally the painter will perform this work but in this case the border might
     * do it instead.
     * 
     * @param g graphics context to draw onto
     * @param c component whose border should be drawn
     */
    public void paintBorderBackground(Graphics g, Component c) {
        int originalColor = g.getColor();
        int x = c.getX();
        int y = c.getY();
        int width = c.getWidth();
        int height = c.getHeight();
        switch(type) {
            case TYPE_ROUNDED_PRESSED:
                x++;
                y++;
                width -= 2;
                height -= 2;
            case TYPE_ROUNDED:
                width--;
                height--;
                Style s = c.getStyle();
                if ((s.getBgImage() != null && s.getBackgroundType() == Style.BACKGROUND_IMAGE_SCALED) || s.getBackgroundType() > 1) {
                    WeakReference w = (WeakReference) s.roundRectCache;
                    Image i = null;
                    if (w != null) {
                        i = (Image) w.get();
                    }
                    if (i != null && i.getWidth() == width && i.getHeight() == height) {
                        g.drawImage(i, x, y);
                    } else {
                        i = Image.createImage(width, height);
                        Graphics imageG = i.getGraphics();
                        imageG.setColor(0);
                        imageG.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
                        int[] rgb = i.getRGBCached();
                        int transColor = rgb[0];
                        int[] imageRGB;
                        if (s.getBackgroundType() == Style.BACKGROUND_IMAGE_SCALED) {
                            imageRGB = s.getBgImage().scaled(width, height).getRGBCached();
                        } else {
                            Image bgPaint = Image.createImage(width, height);
                            Painter p = s.getBgPainter();
                            if (p == null) {
                                return;
                            }
                            p.paint(bgPaint.getGraphics(), new Rectangle(0, 0, width, height));
                            imageRGB = bgPaint.getRGB();
                        }
                        for (int iter = 0; iter < rgb.length; iter++) {
                            if (rgb[iter] == transColor) {
                                imageRGB[iter] = 0;
                            }
                        }
                        i = Image.createImage(imageRGB, width, height);
                        s.roundRectCache = new WeakReference(i);
                        g.drawImage(i, x, y);
                    }
                } else {
                    int foreground = g.getColor();
                    g.setColor(s.getBgColor());
                    if (s.getBgTransparency() == ((byte) 0xff)) {
                        g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
                    } else {
                        if (g.isAlphaSupported()) {
                            int alpha = g.getAlpha();
                            g.setAlpha(s.getBgTransparency() & 0xff);
                            g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
                            g.setAlpha(alpha);
                        } else {
                            if (s.getBgTransparency() != 0) {
                                Image i = Image.createImage(width, height);
                                int[] imageRgb;
                                if (g.getColor() != 0xffffff) {
                                    Graphics imageG = i.getGraphics();
                                    imageG.setColor(g.getColor());
                                    imageG.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
                                    imageRgb = i.getRGBCached();
                                } else {
                                    Graphics imageG = i.getGraphics();
                                    imageG.setColor(0);
                                    imageG.fillRect(0, 0, width, height);
                                    imageG.setColor(g.getColor());
                                    imageG.fillRoundRect(0, 0, width, height, arcWidth, arcHeight);
                                    imageRgb = i.getRGBCached();
                                }
                                int removeColor = imageRgb[0];
                                int size = width * height;
                                int alphaInt = ((s.getBgTransparency() & 0xff) << 24) & 0xff000000;
                                for (int iter = 0; iter < size; iter++) {
                                    if (removeColor == imageRgb[iter]) {
                                        imageRgb[iter] = 0;
                                        continue;
                                    }
                                    if ((imageRgb[iter] & 0xff000000) != 0) {
                                        imageRgb[iter] = (imageRgb[iter] & 0xffffff) | alphaInt;
                                    }
                                }
                                g.drawImage(new RGBImage(imageRgb, width, height), x, y);
                            }
                        }
                    }
                    g.setColor(foreground);
                }
                break;
            case TYPE_IMAGE:
                int clipX = g.getClipX();
                int clipY = g.getClipY();
                int clipWidth = g.getClipWidth();
                int clipHeight = g.getClipHeight();
                Image topLeft = images[4];
                Image topRight = images[5];
                Image bottomLeft = images[6];
                Image center = images[8];
                x += topLeft.getWidth();
                y += topLeft.getHeight();
                height -= (topLeft.getHeight() + bottomLeft.getHeight());
                width -= (topLeft.getWidth() + topRight.getWidth());
                g.clipRect(x, y, width, height);
                if (center != null) {
                    int centerWidth = center.getWidth();
                    int centerHeight = center.getHeight();
                    for (int xCount = x; xCount < x + width; xCount += centerWidth) {
                        for (int yCount = y; yCount < y + height; yCount += centerHeight) {
                            g.drawImage(center, xCount, yCount);
                        }
                    }
                }
                Image top = images[0];
                Image bottom = images[1];
                Image left = images[2];
                Image right = images[3];
                Image bottomRight = images[7];
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                x = c.getX();
                y = c.getY();
                width = c.getWidth();
                height = c.getHeight();
                g.drawImage(topLeft, x, y);
                g.drawImage(bottomLeft, x, y + height - bottomLeft.getHeight());
                g.drawImage(topRight, x + width - topRight.getWidth(), y);
                g.drawImage(bottomRight, x + width - bottomRight.getWidth(), y + height - bottomRight.getHeight());
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                drawImageBorderLine(g, topLeft, topRight, top, x, y, width);
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                drawImageBorderLine(g, bottomLeft, bottomRight, bottom, x, y + height - bottom.getHeight(), width);
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                drawImageBorderColumn(g, topLeft, bottomLeft, left, x, y, height);
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                drawImageBorderColumn(g, topRight, bottomRight, right, x + width - right.getWidth(), y, height);
                g.setClip(clipX, clipY, clipWidth, clipHeight);
                break;
        }
        g.setColor(originalColor);
    }

    /**
     * Draws the border for the given component, this method is called before a call to
     * background painting is made.
     * 
     * @param g graphics context to draw onto
     * @param c component whose border should be drawn
     */
    public void paint(Graphics g, Component c) {
        int originalColor = g.getColor();
        int x = c.getX();
        int y = c.getY();
        int width = c.getWidth();
        int height = c.getHeight();
        if (!themeColors) {
            g.setColor(colorA);
        }
        switch(type) {
            case TYPE_LINE:
                width--;
                height--;
                for (int iter = 0; iter < thickness; iter++) {
                    g.drawRect(x + iter, y + iter, width, height);
                    width -= 2;
                    height -= 2;
                }
                break;
            case TYPE_ROUNDED_PRESSED:
                x++;
                y++;
                width -= 2;
                height -= 2;
            case TYPE_ROUNDED:
                width--;
                height--;
                if (outline) {
                    g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
                }
                break;
            case TYPE_ETCHED_LOWERED:
            case TYPE_ETCHED_RAISED:
                g.drawRect(x, y, width - 2, height - 2);
                if (themeColors) {
                    if (type == TYPE_ETCHED_LOWERED) {
                        g.lighterColor(40);
                    } else {
                        g.darkerColor(40);
                    }
                } else {
                    g.setColor(colorB);
                }
                g.drawLine(x + 1, y + height - 3, x + 1, y + 1);
                g.drawLine(x + 1, y + 1, x + width - 3, y + 1);
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y + height - 1, x + width - 1, y);
                break;
            case TYPE_BEVEL_RAISED:
                if (themeColors) {
                    g.setColor(getBackgroundColor(c));
                    g.lighterColor(50);
                } else {
                    g.setColor(colorA);
                }
                g.drawLine(x, y, x, y + height - 2);
                g.drawLine(x + 1, y, x + width - 2, y);
                if (themeColors) {
                    g.darkerColor(25);
                } else {
                    g.setColor(colorB);
                }
                g.drawLine(x + 1, y + 1, x + 1, y + height - 3);
                g.drawLine(x + 2, y + 1, x + width - 3, y + 1);
                if (themeColors) {
                    g.darkerColor(50);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y, x + width - 1, y + height - 2);
                if (themeColors) {
                    g.darkerColor(25);
                } else {
                    g.setColor(colorD);
                }
                g.drawLine(x + 1, y + height - 2, x + width - 2, y + height - 2);
                g.drawLine(x + width - 2, y + 1, x + width - 2, y + height - 3);
                break;
            case TYPE_BEVEL_LOWERED:
                if (themeColors) {
                    g.setColor(getBackgroundColor(c));
                    g.darkerColor(50);
                } else {
                    g.setColor(colorD);
                }
                g.drawLine(x, y, x, y + height - 1);
                g.drawLine(x + 1, y, x + width - 1, y);
                if (themeColors) {
                    g.lighterColor(25);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x + 1, y + 1, x + 1, y + height - 2);
                g.drawLine(x + 2, y + 1, x + width - 2, y + 1);
                if (themeColors) {
                    g.lighterColor(50);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
                if (themeColors) {
                    g.lighterColor(25);
                } else {
                    g.setColor(colorA);
                }
                g.drawLine(x + 2, y + height - 2, x + width - 2, y + height - 2);
                g.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
                break;
            case TYPE_IMAGE:
                break;
        }
        g.setColor(originalColor);
    }

    private int getBackgroundColor(Component c) {
        return c.getStyle().getBgColor();
    }

    private void drawImageBorderLine(Graphics g, Image left, Image right, Image center, int x, int y, int width) {
        int currentWidth = width - right.getWidth();
        if (currentWidth > 0) {
            x += left.getWidth();
            int destX = x + currentWidth;
            g.clipRect(x, y, currentWidth - left.getWidth(), center.getHeight());
            int centerWidth = center.getWidth();
            for (; x < destX; x += centerWidth) {
                g.drawImage(center, x, y);
            }
        }
    }

    private void drawImageBorderColumn(Graphics g, Image top, Image bottom, Image center, int x, int y, int height) {
        int currentHeight = height - bottom.getHeight();
        if (currentHeight > 0) {
            y += top.getHeight();
            int destY = y + currentHeight;
            g.clipRect(x, y, center.getWidth(), currentHeight - top.getHeight());
            int centerHeight = center.getHeight();
            for (; y < destY; y += centerHeight) {
                g.drawImage(center, x, y);
            }
        }
    }

    /**
     * Sets the default border to the given value
     * 
     * @param border new border value
     */
    public static void setDefaultBorder(Border border) {
        defaultBorder = border;
    }

    /**
     * Gets the default border to the given value
     * 
     * @return the default border
     */
    public static Border getDefaultBorder() {
        return defaultBorder;
    }

    /**
     * This methos returns how thick is the border in pixels
     * @return the Border thickness
     */
    public int getThickness() {
        return thickness;
    }
}
