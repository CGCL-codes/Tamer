package geovista.colorbrewer;

import java.awt.Color;

/**
 * The <CODE>Palette2D</CODE> interface.
 *
 * @author  Chris Weaver
 * 
 */
public interface Palette2D extends Palette {

    int SEQUENTIAL_SEQUENTIAL = 1;

    int SEQUENTIAL_DIVERGING = 2;

    int SEQUENTIAL_QUALITATIVE = 3;

    int QUALITATIVE_SEQUENTIAL = 4;

    int DIVERGING_DIVERGING = 5;

    int DIVERGING_SEQUENTIAL = 6;

    public int getType();

    public Color[][] getColors(int cols, int rows);
}
