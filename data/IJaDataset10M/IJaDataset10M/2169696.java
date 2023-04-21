package org.mazix.splashscreen.component.complex;

import static org.mazix.constants.ColorConstants.DEFAULT_COLOR;
import static org.mazix.constants.ColorConstants.REVERSE_SUPPORTED_COLORS;
import static org.mazix.constants.ColorConstants.SUPPORTED_COLORS;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import org.mazix.splashscreen.component.AbstractSplashScreenComponent;

/**
 * The class which represents a progress bar with its attributes to render in the game splash
 * screen.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * 
 * @since 0.7
 * @version 0.7
 */
public class ProgressBarSplashScreenComponent extends AbstractSplashScreenComponent {

    /**
     * The progress bar background {@link Color} to render.
     */
    private Color backgroundColor = null;

    /**
     * The progress bar background {@link Image} to render.
     */
    private Image backgroundImage = null;

    /**
     * The progress bar step {@link Color} to render.
     */
    private Color stepColor = null;

    /**
     * The progress bar step {@link Image} to render.
     */
    private Image stepImage = null;

    /**
     * The current progress percentage. Must be between 0 and 1.
     */
    private float progress = 0;

    /**
     * Constructor with {@link SplashScreen} and bounds coordinates. The color, the progress step
     * and the background will be set to their default value.
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
    public ProgressBarSplashScreenComponent(final SplashScreen splash, final float widthBeginPercentage, final float widthEndPercentage, final float heightBeginPercentage, final float heightEndPercentage) {
        super(splash, widthBeginPercentage, widthEndPercentage, heightBeginPercentage, heightEndPercentage);
        setAttributes(DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Gets the value of background color.
     * 
     * @return the value of background color.
     * @see #setBackgroundColor(Color)
     * @since 0.7
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Gets the value of background image.
     * 
     * @return the value of background image.
     * @see #setBackgroundImage(Image)
     * @since 0.7
     */
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Gets the value of progress.
     * 
     * @return the value of progress.
     * @see #setProgress(float)
     * @since 0.7
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Gets the value of step color.
     * 
     * @return the value of step color.
     * @see #setStepColor(Color)
     * @since 0.7
     */
    public Color getStepColor() {
        return stepColor;
    }

    /**
     * Gets the value of step image.
     * 
     * @return the value of step image.
     * @see #setStepImage(Image)
     * @since 0.7
     */
    public Image getStepImage() {
        return stepImage;
    }

    /**
     * Sets the value of background color and step color.
     * 
     * @param backgroundCol
     *            the background color to set, mustn't be <code>null</code> and must be a supported
     *            color.
     * @param stepCol
     *            the step color to set, mustn't be <code>null</code> and must be a supported
     *            color.
     * @see #setAttributes(Image, Image)
     * @see org.mazix.constants.ColorConstants#SUPPORTED_COLORS
     * @since 0.7
     */
    public void setAttributes(final Color backgroundCol, final Color stepCol) {
        assert backgroundCol != null : "backgroundCol is null";
        assert SUPPORTED_COLORS.get(backgroundCol) != null : "Color isn't supported";
        assert stepCol != null : "stepCol is null";
        assert SUPPORTED_COLORS.get(stepCol) != null : "Color isn't supported";
        backgroundColor = backgroundCol;
        stepColor = stepCol;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of background image and step image.
     * 
     * @param backgroundImg
     *            the background {@link Image} to set, can be {@code null}.
    * @param setpImg
     *            the step {@link Image} to set, can be {@code null}.
     * @see #setAttributes(Color, Color)
     * @since 0.7
     */
    public void setAttributes(final Image backgroundImg, final Image setpImg) {
        assert backgroundImg != null : "backgroundImg is null";
        assert setpImg != null : "setpImg is null";
        backgroundImage = backgroundImg;
        stepImage = setpImg;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of background color.
     * 
     * @param value
     *            the background color to set, mustn't be <code>null</code> and must be a supported
     *            color.
     * @see #getBackgroundColor()
     * @see org.mazix.constants.ColorConstants#SUPPORTED_COLORS
     * @since 0.7
     */
    public void setBackgroundColor(final Color value) {
        assert value != null : "value is null";
        assert SUPPORTED_COLORS.get(value) != null : "Color isn't supported";
        backgroundColor = value;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of backgroundImage.
     * 
     * @param value
     *            the backgroundImage to set, can be {@code null}.
     * @see #getBackgroundImage()
     * @since 0.7
     */
    public void setBackgroundImage(final Image value) {
        assert value != null : "value is null";
        backgroundImage = value;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of progress.
     * 
     * @param value
     *            the progress to set, must be a valid percentage (between 0 and 1).
     * @see #getProgress()
     * @since 0.7
     */
    public void setProgress(final float value) {
        assert value > 0 && value < 1 : "value must be a valid percentage (between 0 and 1)";
        progress = value;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of step color.
     * 
     * @param value
     *            the step color to set, mustn't be <code>null</code> and must be a supported color.
     * @see #getStepcolor()
     * @see org.mazix.constants.ColorConstants#SUPPORTED_COLORS
     * @since 0.7
     */
    public void setStepColor(final Color value) {
        assert value != null : "value is null";
        assert SUPPORTED_COLORS.get(value) != null : "Color isn't supported";
        stepColor = value;
        updateGraphics(getGraphics(), getBounds());
    }

    /**
     * Sets the value of step image.
     * 
     * @param value
     *            the step image to set, can be {@code null}.
     * @see #getStepImage()
     * @since 0.7
     */
    public void setStepImage(final Image value) {
        stepImage = value;
    }

    /**
     * @see java.lang.Object#toString()
     * @since 0.7
     */
    @Override
    public String toString() {
        return super.toString() + ", Background color: " + REVERSE_SUPPORTED_COLORS.get(getBackgroundColor()) + ", Background image: " + getBackgroundImage() + ", Step color: " + REVERSE_SUPPORTED_COLORS.get(getStepColor()) + ", Step image: " + getStepImage() + ", Progress percentage: " + getProgress();
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
        if (getBackgroundImage() == null) {
            graphics.setColor(getBackgroundColor());
            graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            graphics.drawImage(getBackgroundImage(), bounds.x, bounds.y, bounds.width, bounds.height, null);
        }
        if (getStepImage() == null) {
            graphics.setColor(getStepColor());
            graphics.fillRect(bounds.x, bounds.y, (int) (bounds.width * getProgress()), bounds.height);
        } else {
            graphics.drawImage(getStepImage(), bounds.x, bounds.y, (int) (bounds.width * getProgress()), bounds.height, null);
        }
        getSplashScreen().update();
    }
}
