package org.vizzini.ui.game.boardgame;

import org.vizzini.game.IPosition;
import org.vizzini.util.AbstractBean;
import java.awt.Color;

/**
 * Provides highlight data for a grid board position.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class HighlightData extends AbstractBean {

    /** Color. */
    private Color _color = Color.blue;

    /** Position. */
    private IPosition _position;

    /** Width of the highlight line. */
    private int _width = -1;

    /**
     * @return  the color.
     *
     * @since   v0.1
     */
    public Color getColor() {
        return _color;
    }

    /**
     * @return  the position.
     *
     * @since   v0.1
     */
    public IPosition getPosition() {
        return _position;
    }

    /**
     * @return  the line width.
     *
     * @since   v0.1
     */
    public int getWidth() {
        return _width;
    }

    /**
     * Set the color.
     *
     * @param  color  Color.
     *
     * @since  v0.1
     */
    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("color == null");
        }
        _color = color;
    }

    /**
     * Set the position.
     *
     * @param  position  Position.
     *
     * @since  v0.1
     */
    public void setPosition(IPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("position == null");
        }
        _position = position;
    }

    /**
     * Set the line width.
     *
     * @param  width  Width.
     *
     * @since  v0.1
     */
    public void setWidth(int width) {
        if (width < 0) {
            throw new IllegalArgumentException("width < 0");
        }
        _width = width;
    }
}
