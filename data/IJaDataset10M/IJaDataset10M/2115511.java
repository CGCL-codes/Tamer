package org.fonteditor.options.display;

import org.fonteditor.options.coords.Coords;
import org.fonteditor.options.pen.Pen;
import org.fonteditor.options.pen.PenRound;

/**
 * DisplayOptions -default values...
 */
public interface DisplayOptionsConstants {

    int DEFAULT_SLANT = 0x00;

    int DEFAULT_EXPAND = 0x400;

    boolean DEFAULT_HINT = true;

    boolean DEFAULT_FILL = true;

    Pen DEFAULT_PEN = new PenRound(0x300);

    Coords DEFAULT_COORDS = new Coords(40, 40, 12, 20);
}
