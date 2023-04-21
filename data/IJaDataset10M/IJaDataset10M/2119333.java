package org.vizzini.ui.graphics;

import java.awt.Color;

/**
 * Defines methods required by a 3D viewer. The coordinate system is screen
 * based (+Z into the screen).
 *
 * <pre>
         ^ +Z
        /
       /
      +-----> +X
      |
      |
      V +Y
 * </pre>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public interface IViewer {

    /** Magnification factor. */
    double MAGNIFICATION_FACTOR = 1.5;

    /**
     * Add the given shape.
     *
     * @param  shape  Shape.
     *
     * @since  v0.2
     */
    void add(IShape shape);

    /**
     * @return  the magnification.
     *
     * @since   v0.2
     */
    double getMagnify();

    /**
     * @return  the perspective constant.
     *
     * @since   v0.2
     */
    double getPerspectiveConstant();

    /**
     * @return  the shape canvas.
     *
     * @since   v0.4
     */
    IShapeCanvas getShapeCanvas();

    /**
     * @return  Return isViewPointLightUsed.
     *
     * @since   v0.3
     */
    boolean isViewPointLightUsed();

    /**
     * Remove the given shape.
     *
     * @param  shape  Shape.
     *
     * @since  v0.2
     */
    void remove(IShape shape);

    /**
     * Set the canvas background color.
     *
     * @param  color  Color.
     *
     * @since  v0.2
     */
    void setCanvasBackground(Color color);

    /**
     * Set the magnification.
     *
     * @param  magnify  Magnification.
     *
     * @since  v0.2
     */
    void setMagnify(double magnify);

    /**
     * Set the visibility of the magnification panel.
     *
     * @param  isVisible  Flag indicating visibility.
     *
     * @since  v0.2
     */
    void setMagnifyPanelVisible(boolean isVisible);

    /**
     * Set the perspective constant.
     *
     * @param  d  Perspective constant.
     *
     * @since  v0.2
     */
    void setPerspectiveConstant(double d);

    /**
     * @param  isViewPointLightUsed  the isViewPointLightUsed to set
     *
     * @since  v0.3
     */
    void setViewPointLightUsed(boolean isViewPointLightUsed);

    /**
     * Set the block increment on the X scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setXScrollBarBlockIncrement(int increment);

    /**
     * Set the unit increment on the X scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setXScrollBarUnitIncrement(int increment);

    /**
     * Set the values on the X scroll bar.
     *
     * @param  value    The position in the current window.
     * @param  visible  The amount visible per page.
     * @param  minimum  The minimum value of the scroll bar.
     * @param  maximum  The maximum value of the scroll bar.
     *
     * @since  v0.2
     */
    void setXScrollBarValues(int value, int visible, int minimum, int maximum);

    /**
     * Set the visibility of the X scroll bar.
     *
     * @param  isVisible  Flag indicating if the scroll bar is visible.
     *
     * @since  v0.2
     */
    void setXScrollBarVisible(boolean isVisible);

    /**
     * Set the block increment on the Y scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setYScrollBarBlockIncrement(int increment);

    /**
     * Set the unit increment on the Y scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setYScrollBarUnitIncrement(int increment);

    /**
     * Set the values on the Y scroll bar.
     *
     * @param  value    The position in the current window.
     * @param  visible  The amount visible per page.
     * @param  minimum  The minimum value of the scroll bar.
     * @param  maximum  The maximum value of the scroll bar.
     *
     * @since  v0.2
     */
    void setYScrollBarValues(int value, int visible, int minimum, int maximum);

    /**
     * Set the visibility of the Y scroll bar.
     *
     * @param  isVisible  Flag indicating if the scroll bar is visible.
     *
     * @since  v0.2
     */
    void setYScrollBarVisible(boolean isVisible);

    /**
     * Set the block increment on the Z scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setZScrollBarBlockIncrement(int increment);

    /**
     * Set the unit increment on the Z scroll bar.
     *
     * @param  increment  The amount by which to increment or decrement the
     *                    scroll bar's value.
     *
     * @since  v0.2
     */
    void setZScrollBarUnitIncrement(int increment);

    /**
     * Set the values on the Z scroll bar.
     *
     * @param  value    The position in the current window.
     * @param  visible  The amount visible per page.
     * @param  minimum  The minimum value of the scroll bar.
     * @param  maximum  The maximum value of the scroll bar.
     *
     * @since  v0.2
     */
    void setZScrollBarValues(int value, int visible, int minimum, int maximum);

    /**
     * Set the visibility of the Z scroll bar.
     *
     * @param  isVisible  Flag indicating if the scroll bar is visible.
     *
     * @since  v0.2
     */
    void setZScrollBarVisible(boolean isVisible);

    /**
     * Update the shapes in this viewer.
     *
     * @since  v0.3
     */
    void update();
}
