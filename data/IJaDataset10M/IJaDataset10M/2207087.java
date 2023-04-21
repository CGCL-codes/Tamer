package pl.org.minions.utils.ui.widgets;

import java.awt.Color;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.image.RenderedImage;
import java.util.Arrays;

/**
 * Class for defining the look and feel of a Widget derived
 * from {@link Panel} class.
 */
public final class PanelStyle {

    /**
     * Defines how background of this panel should be
     * painted.
     */
    public static enum Background {

        /**
         * No texture, single color or transparent (if
         * background color is <code>null</code>).
         */
        SINGLE_COLOR, /**
         * Textured with a single, centered image or
         * transparent (if texture image is
         * <code>null</code>).
         */
        TEXTURE, /**
         * Textured with a single image, stretched to fill
         * the panel, or transparent (if texture image is
         * <code>null</code>).
         */
        FIT, /**
         * Textured with tiled copies of an image or
         * transparent (if texture image is
         * <code>null</code>).
         */
        TILED, /**
         * Filled using vertical linear gradient.
         */
        VERTICAL_GRADIENT, /**
         * Filled using horizontal linear gradient.
         */
        HORIZONTAL_GRADIENT, /**
         * Filled with custom {@link Paint}.
         */
        CUSTOM_PAINT
    }

    /**
     * Defines how border of this widget should be painted.
     */
    public static enum Border {

        /**
         * No border.
         */
        NONE, /**
         * Single colored border.
         */
        SINGLE_COLOR, /**
         * A raised border based on background color.
         * <p>
         * Works only with {@link Shape#RECTANGLE}.
         */
        RAISED, /**
         * A lowered border based on background color.
         * <p>
         * Works only with {@link Shape#RECTANGLE}.
         */
        LOWERED
    }

    private static enum CreateToken {

        STYLE, BACKGROUND_COLOR, TEXTURE_IMAGE, GRADIENT_FRACTIONS, GRADIENT_COLORS, CUSTOM_PAINT, BORDER_COLOR
    }

    /**
     * Defines border shape for this widget.
     */
    public static enum Shape {

        /**
         * Rectangle.
         */
        RECTANGLE, /**
         * Rounded rectangle.
         */
        ROUNDED_RECTANGLE, /**
         * Ellipse.
         */
        ELLIPSE
    }

    static final int ROUND_RECTANGLE_CORNER_RADIUS = 8;

    private PanelStyle.Background backgroundStyle;

    private Color backgroundColor;

    private RenderedImage textureImage;

    private float[] gradientFractions;

    private Color[] gradientColors;

    private Paint customPaint;

    private PanelStyle.Border borderStyle;

    private Color borderColor;

    private Float borderWidth;

    private PanelStyle.Shape shape;

    /**
     * Creates a new, empty, panel style instance.
     */
    public PanelStyle() {
    }

    /**
     * Creates a new Style instance, with all attributes
     * <code>null</code>.
     * <p>
     * Then, applies style attributes according to given
     * parameters.
     * @param params
     *            sequence of any number of following:
     *            <ul>
     *            <li> {@link Background#SINGLE_COLOR}
     *            followed by a {@link Color};</li> <li>
     *            either {@link Background#TEXTURE} or
     *            {@link Background#FIT} or
     *            {@link Background#TILED} followed by an
     *            {@link Image};</li> <li>either
     *            {@link Background#VERTICAL_GRADIENT} or
     *            {@link Background#HORIZONTAL_GRADIENT}
     *            followed by an array of {@code float} and
     *            an array of {@link Color};</li> <li>
     *            {@link Background#CUSTOM_PAINT} followed
     *            by a {@link Paint}</li> <li> either
     *            {@link Border#NONE} or
     *            {@link Border#RAISED} or
     *            {@link Border#LOWERED};</li> <li>
     *            {@link Border#SINGLE_COLOR} followed by a
     *            {@link Color}</li> <li> {@link Shape}</li>
     *            </ul>
     * @return new Style instance
     */
    public static PanelStyle create(Object... params) {
        final String expectedStr = " expected";
        final String orStr = " or ";
        PanelStyle style = new PanelStyle();
        PanelStyle.CreateToken expected = CreateToken.STYLE;
        for (Object param : params) {
            switch(expected) {
                case STYLE:
                    if (param instanceof PanelStyle.Background) {
                        PanelStyle.Background backgroundStyle = (PanelStyle.Background) param;
                        style.setBackgroundStyle(backgroundStyle);
                        switch(backgroundStyle) {
                            case SINGLE_COLOR:
                                expected = CreateToken.BACKGROUND_COLOR;
                                break;
                            case TEXTURE:
                            case FIT:
                            case TILED:
                                expected = CreateToken.TEXTURE_IMAGE;
                                break;
                            case HORIZONTAL_GRADIENT:
                            case VERTICAL_GRADIENT:
                                expected = CreateToken.GRADIENT_FRACTIONS;
                                break;
                            case CUSTOM_PAINT:
                                expected = CreateToken.CUSTOM_PAINT;
                                break;
                            default:
                                break;
                        }
                    } else if (param instanceof PanelStyle.Border) {
                        PanelStyle.Border borderStyle = (PanelStyle.Border) param;
                        style.setBorderStyle(borderStyle);
                        if (borderStyle == Border.SINGLE_COLOR) expected = CreateToken.BORDER_COLOR;
                    } else if (param instanceof PanelStyle.Shape) {
                        PanelStyle.Shape shape = (PanelStyle.Shape) param;
                        style.setShape(shape);
                    } else throw new IllegalArgumentException(PanelStyle.Background.class.getSimpleName() + orStr + PanelStyle.Border.class.getSimpleName() + orStr + PanelStyle.Shape.class.getSimpleName() + expectedStr);
                    break;
                case BACKGROUND_COLOR:
                    if (param == null) {
                        style.setBackgroundColor(null);
                        expected = CreateToken.STYLE;
                    } else if (param instanceof Color) {
                        Color color = (Color) param;
                        style.setBackgroundColor(color);
                        expected = CreateToken.STYLE;
                    } else throw new IllegalArgumentException(Color.class.getSimpleName() + expectedStr);
                    break;
                case BORDER_COLOR:
                    if (param == null) {
                        style.setBorderColor(null);
                        expected = CreateToken.STYLE;
                    } else if (param instanceof Color) {
                        Color color = (Color) param;
                        style.setBorderColor(color);
                        expected = CreateToken.STYLE;
                    } else throw new IllegalArgumentException(Color.class.getSimpleName() + expectedStr);
                    break;
                case CUSTOM_PAINT:
                    if (param == null) {
                        style.setCustomPaint(null);
                        expected = CreateToken.STYLE;
                    } else if (param instanceof Paint) {
                        Paint paint = (Paint) param;
                        style.setCustomPaint(paint);
                        expected = CreateToken.STYLE;
                    } else throw new IllegalArgumentException(Paint.class.getSimpleName() + expectedStr);
                    break;
                case TEXTURE_IMAGE:
                    if (param == null) {
                        style.setTextureImage(null);
                        expected = CreateToken.STYLE;
                    } else if (param instanceof RenderedImage) {
                        RenderedImage image = (RenderedImage) param;
                        style.setTextureImage(image);
                        expected = CreateToken.STYLE;
                    } else throw new IllegalArgumentException(RenderedImage.class.getSimpleName() + expectedStr);
                    break;
                case GRADIENT_FRACTIONS:
                    if (param instanceof float[]) {
                        float[] fractions = (float[]) param;
                        style.setGradientFractions(fractions);
                        expected = CreateToken.GRADIENT_COLORS;
                    } else throw new IllegalArgumentException(float[].class.getSimpleName() + expectedStr);
                    break;
                case GRADIENT_COLORS:
                    if (param instanceof Color[]) {
                        Color[] colors = (Color[]) param;
                        style.setGradientColors(colors);
                        expected = CreateToken.STYLE;
                    } else throw new IllegalArgumentException(Color[].class.getSimpleName() + expectedStr);
                    break;
                default:
                    break;
            }
        }
        switch(expected) {
            case BACKGROUND_COLOR:
            case BORDER_COLOR:
                throw new IllegalArgumentException(Color.class.getSimpleName() + expectedStr);
            case CUSTOM_PAINT:
                throw new IllegalArgumentException(Paint.class.getSimpleName() + expectedStr);
            case TEXTURE_IMAGE:
                throw new IllegalArgumentException(RenderedImage.class.getSimpleName() + expectedStr);
            case GRADIENT_FRACTIONS:
                throw new IllegalArgumentException(float[].class.getSimpleName() + expectedStr);
            case GRADIENT_COLORS:
                throw new IllegalArgumentException(Color[].class.getSimpleName() + expectedStr);
            case STYLE:
            default:
                return style;
        }
    }

    /**
     * Applies values from other panel style to this style.
     * @param changes
     *            style to take values from
     * @param ignoreNullValues
     *            should null values be ignored or applied
     * @return whether this style was changed as a result
     */
    boolean apply(PanelStyle changes, boolean ignoreNullValues) {
        boolean changed = false;
        if (changes.backgroundStyle != null || !ignoreNullValues) {
            if (backgroundStyle != changes.backgroundStyle) {
                changed = true;
                backgroundStyle = changes.backgroundStyle;
            }
        }
        if (changes.backgroundColor != null || !ignoreNullValues) {
            if (backgroundColor == null || !backgroundColor.equals(changes.backgroundColor)) {
                changed = true;
                backgroundColor = changes.backgroundColor;
            }
        }
        if (changes.textureImage != null || !ignoreNullValues) {
            if (textureImage != changes.textureImage) {
                changed = true;
                textureImage = changes.textureImage;
            }
        }
        if (changes.gradientFractions != null || !ignoreNullValues) {
            if (gradientFractions == null || !Arrays.equals(gradientFractions, changes.gradientFractions)) {
                changed = true;
                gradientFractions = changes.gradientFractions;
            }
        }
        if (changes.gradientColors != null || !ignoreNullValues) {
            if (gradientColors == null || !Arrays.equals(gradientColors, changes.gradientColors)) {
                changed = true;
                gradientColors = changes.gradientColors;
            }
        }
        if (changes.customPaint != null || !ignoreNullValues) {
            if (customPaint != changes.customPaint) {
                changed = true;
                customPaint = changes.customPaint;
            }
        }
        if (changes.shape != null || !ignoreNullValues) {
            if (shape != changes.shape) {
                changed = true;
                shape = changes.shape;
            }
        }
        if (changes.borderStyle != null || !ignoreNullValues) {
            if (borderStyle != changes.borderStyle) {
                changed = true;
                borderStyle = changes.borderStyle;
            }
        }
        if (changes.borderColor != null || !ignoreNullValues) {
            if (borderColor == null || !borderColor.equals(changes.borderColor)) {
                changed = true;
                borderColor = changes.borderColor;
            }
        }
        if (changes.borderWidth != null || !ignoreNullValues) {
            if (borderWidth == null || !borderWidth.equals(changes.borderWidth)) {
                changed = true;
                borderWidth = changes.borderWidth;
            }
        }
        return changed;
    }

    /**
     * Returns background color.
     * @return background color
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Returns background style.
     * @return background style
     */
    public PanelStyle.Background getBackgroundStyle() {
        return backgroundStyle;
    }

    /**
     * Returns the border color.
     * @return border color
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Returns the border style.
     * @return border style
     */
    public PanelStyle.Border getBorderStyle() {
        return borderStyle;
    }

    /**
     * Returns border width.
     * @return border width, in pixels
     */
    public Float getBorderWidth() {
        return borderWidth;
    }

    /**
     * Returns the custom paint.
     * @return custom paint
     */
    public Paint getCustomPaint() {
        return customPaint;
    }

    /**
     * Returns gradient colors.
     * @see LinearGradientPaint#getColors()
     * @return gradient colors
     */
    public Color[] getGradientColors() {
        return gradientColors;
    }

    /**
     * Returns fractions for colors used by gradient.
     * @see LinearGradientPaint#getFractions()
     * @return gradient fractions
     */
    public float[] getGradientFractions() {
        return gradientFractions;
    }

    /**
     * Returns the type of border shape.
     * @return border shape type
     */
    public PanelStyle.Shape getShape() {
        return shape;
    }

    /**
     * Returns texture image.
     * @return texture image
     */
    public RenderedImage getTextureImage() {
        return textureImage;
    }

    /**
     * Sets the background color.
     * @param backgroundColor
     *            the background color to set
     * @return <code>this</code>
     */
    public PanelStyle setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Sets the style of background.
     * @param backgroundStyle
     *            the background style to set
     * @return <code>this</code>
     */
    public PanelStyle setBackgroundStyle(PanelStyle.Background backgroundStyle) {
        this.backgroundStyle = backgroundStyle;
        return this;
    }

    /**
     * Sets the border color.
     * @param borderColor
     *            the border color to set
     * @return <code>this</code>
     */
    public PanelStyle setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    /**
     * Sets the border style.
     * @param borderStyle
     *            the border style to set
     * @return <code>this</code>
     */
    public PanelStyle setBorderStyle(PanelStyle.Border borderStyle) {
        this.borderStyle = borderStyle;
        return this;
    }

    /**
     * Sets border width.
     * @param borderWidth
     *            new border width, in pixels
     * @return <code>this</code>
     */
    public PanelStyle setBorderWidth(Float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * Sets the custom paint.
     * @param customPaint
     *            the custom paint to set
     * @return <code>this</code>
     */
    public PanelStyle setCustomPaint(Paint customPaint) {
        this.customPaint = customPaint;
        return this;
    }

    /**
     * Sets colors for gradient.
     * @see LinearGradientPaint
     * @param gradientColors
     *            the gradient colors to set. Should be
     *            exactly the same length as fractions
     *            array.
     * @return <code>this</code>
     */
    public PanelStyle setGradientColors(Color[] gradientColors) {
        this.gradientColors = gradientColors;
        return this;
    }

    /**
     * Sets fractions for colors used by gradient.
     * @see LinearGradientPaint
     * @param gradientFractions
     *            the gradient fractions to set. Should be
     *            exactly the same length as colors array.
     * @return <code>this</code>
     */
    public PanelStyle setGradientFractions(float[] gradientFractions) {
        this.gradientFractions = gradientFractions;
        return this;
    }

    /**
     * Sets the type of border shape.
     * @param shape
     *            the border shape type to set
     * @return <code>this</code>
     */
    public PanelStyle setShape(PanelStyle.Shape shape) {
        this.shape = shape;
        return this;
    }

    /**
     * Sets the texture image.
     * @param textureImage
     *            the texture image to set
     * @return <code>this</code>
     */
    public PanelStyle setTextureImage(RenderedImage textureImage) {
        this.textureImage = textureImage;
        return this;
    }
}
