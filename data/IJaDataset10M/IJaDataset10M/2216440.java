package jajt;

import java.awt.*;
import java.awt.image.*;

/**
 * A <code>Block</code> represents a block in this Tetris-game.
 *
 * @author <a href="mailto:lucky@knup.de"></a>
 * @version 1.0
 */
public class Block {

    private Color col;

    private int x, y;

    /**
   * sets the location of the Block
   * @param _x <code>int</code> x-position
   * @param _y <code>int</code> y-position
   */
    public void setPos(int _x, int _y) {
        x = _x;
        y = _y;
    }

    public void setPosX(int _x) {
        x = _x;
    }

    public void setPosY(int _y) {
        y = _y;
    }

    /**
   * sets the color of the block
   * @param _col a <code>Color</code> value
   */
    public void setColor(Color _col) {
        col = _col;
    }

    /**
   * <code>getX</code> returns x-position of this block.
   *
   * @return an <code>int</code> value (x)
   */
    public int getX() {
        return x;
    }

    /**
   * <code>getY</code> returns y-position if this block.
   *
   * @return an <code>int</code> value (y)
   */
    public int getY() {
        return y;
    }

    /**
   * <code>getColor</code> returns the Color of this block.
   *
   * @return a <code>Color</code> value (color)
   */
    public Color getColor() {
        return col;
    }

    /**
   * draws this block
   * @param _g the <code>Graphics</code> context where to draw this block
   */
    public void draw(Graphics _g) {
        draw(_g, col, x, y);
    }

    /**
   * draws a block
   * @param _g a <code>Graphics</code> value: where to draw a block
   * @param _col a <code>Color</code> value
   * @param _x an <code>int</code> value
   * @param _y an <code>int</code> value
   */
    public static void draw(Graphics _g, Color _col, int _x, int _y) {
        _g.setColor(_col);
        _g.fill3DRect(_x, _y, Tetris.iBlockSize_x, Tetris.iBlockSize_y, true);
    }
}
