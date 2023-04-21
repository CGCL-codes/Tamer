package org.mazix.splashscreen.component.simple;

import static org.mazix.constants.ColorConstants.DEFAULT_COLOR;
import static org.mazix.constants.ColorConstants.SUPPORTED_COLORS;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import org.mazix.splashscreen.component.AbstractSplashScreenComponent;

/**
 * The class which represents a simple rectangle filled by a color to render in the game splash screen.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * 
 * @since 0.7
 * @version 0.7
 */
public class UniqueColorRectangleSplashScreenComponent extends AbstractSplashScreenComponent {

    /**
     * The text {@link Color} to render.
     */
    private Color color = null;

    /**
     * Constructor with {@link SplashScreen} and bounds coordinates. The color will be set to its
     * default value.
     * 
     * @param splash
     *            the {@link SplashScreen} on which to render this component, mustn't be
     *            <code>null</code>.
     * @param widthBeginPercentage
     *            the horizontal begin percentage (i.e. the left side) where this component will
     *            start to be rendered, must be positive and inferior to the splash screen maximum
     *            width. Must also be inferior to <code>widthEndPercentage</code>.
     * @param widthEndPercentage
     *            the horizontal end percentage (i.e. the right side) where this component will
     *            start to be rendered, must be positive and inferior to the splash screen maximum
     *            width. Must also be superior to <code>widthBeginPercentage</code>.
     * @param heightBeginPercentage
     *            the vertical begin percentage (i.e. the top) where this component will start to be
     *            rendered, must be positive and inferior to the splash screen maximum height. Must
     *            also be inferior to <code>heightEndPercentage</code>.
     * @param heightEndPercentage
     *            the vertical end percentage (i.e. the bottom) where this component will start to
     *            be rendered, must be positive and inferior to the splash screen maximum height.
     *            Must also be superior to <code>heightBeginPercentage</code>.
     * @since 0.7
     */
    public UniqueColorRectangleSplashScreenComponent(final SplashScreen splash, final float widthBeginPercentage, final float widthEndPercentage, final float heightBeginPercentage, final float heightEndPercentage) {
        this(splash, widthBeginPercentage, widthEndPercentage, heightBeginPercentage, heightEndPercentage, DEFAULT_COLOR);
    }

    /**
     * Constructor with {@link SplashScreen}, bounds coordinates and {@link Color}.
     * 
     * @param splash
     *            the {@link SplashScreen} on which to render this component, mustn't be
     *            <code>null</code>.
     * @param widthBeginPercentage
     *            the horizontal begin percentage (i.e. the left side) where this component will
     *            start to be rendered, must be positive and inferior to the splash screen maximum
     *            width. Must also be inferior to <code>widthEndPercentage</code>.
     * @param widthEndPercentage
     *            the horizontal end percentage (i.e. the right side) where this component will
     *            start to be rendered, must be positive and inferior to the splash screen maximum
     *            width. Must also be superior to <code>widthBeginPercentage</code>.
     * @param heightBeginPercentage
     *            the vertical begin percentage (i.e. the top) where this component will start to be
     *            rendered, must be positive and inferior to the splash screen maximum height. Must
     *            also be inferior to <code>heightEndPercentage</code>.
     * @param heightEndPercentage
     *            the vertical end percentage (i.e. the bottom) where this component will start to
     *            be rendered, must be positive and inferior to the splash screen maximum height.
     *            Must also be superior to <code>heightBeginPercentage</code>.
     * @param col
     *            the {@link Color} background to fill in the passed bounds coordinates, mustn't be
     *            <code>null</code>.
     * @since 0.7
     */
    public UniqueColorRectangleSplashScreenComponent(final SplashScreen splash, final float widthBeginPercentage, final float widthEndPercentage, final float heightBeginPercentage, final float heightEndPercentage, final Color col) {
        super(splash, widthBeginPercentage, widthEndPercentage, heightBeginPercentage, heightEndPercentage);
        assert col != null : "col is null";
        setColor(col);
    }

    /**
     * Gets the value of color.
     * 
     * @return the value of color.
     * @see #setColor(Color)
     * @since 0.7
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the value of color.
     * 
     * @param value
     *            the new value of color to be set, mustn't be <code>null</code> and must be a supported color.
     * @see #getColor()
     * @see org.mazix.constants.ColorConstants#SUPPORTED_COLORS
     * @since 0.7
     */
    public void setColor(final Color value) {
        assert value != null : "value is null";
        assert SUPPORTED_COLORS.get(value) != null : "Color isn't supported";
        color = value;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Updates the whole graphics of this component by redrawing it on the {@link SplashScreen}.
     * 
     * @param graphics
     *            the component {@link Graphics2D} to draw on.
     * @param bounds
     *            the {@link Rectangle} physical bounds to draw into.
     * @since 0.7
     */
    private void updateGraphics(final Graphics2D graphics, final Rectangle bounds) {
        assert graphics != null : "graphics is null";
        assert bounds != null : "bounds is null";
        graphics.setColor(getColor());
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        getSplashScreen().update();
    }

    /**
     * @see java.lang.Object#toString()
     * @since 0.7
     */
    @Override
    public String toString() {
        return super.toString() + ", Color: " + getColor();
    }
}
