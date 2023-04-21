package de.morknet.mrw.rcc;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Dieses Interface stellt einige h�ufig gebrauchte Farben als Konstanten bereit.
 * @author sm
 *
 */
public interface Colors {

    /**
	 * Wei�.
	 */
    public static final Color WHITE = new Color(null, 255, 255, 255);

    /**
	 * Hellgrau.
	 */
    public static final Color LIGHT_GREY = new Color(null, 192, 192, 192);

    /**
	 * Grau.
	 */
    public static final Color GREY = new Color(null, 128, 128, 128);

    /**
	 * Dunkelgrau.
	 */
    public static final Color DARK_GREY = new Color(null, 64, 64, 64);

    /**
	 * Schwarz.
	 */
    public static final Color BLACK = new Color(null, 0, 0, 0);

    /**
	 * Dunkelgelb.
	 */
    public static final Color DARK_YELLOW = new Color(null, 128, 128, 0);

    /**
	 * Gelb.
	 */
    public static final Color YELLOW = new Color(null, 255, 255, 0);

    /**
	 * Hellgelb.
	 */
    public static final Color LIGHT_YELLOW = new Color(null, 255, 255, 180);

    /**
	 * Dunkelrot.
	 */
    public static final Color DARK_RED = new Color(null, 128, 0, 0);

    /**
	 * Rot.
	 */
    public static final Color RED = new Color(null, 255, 0, 0);

    /**
	 * Hellrot.
	 */
    public static final Color LIGHT_RED = new Color(null, 255, 180, 180);

    /**
	 * Dunkelgr�n.
	 */
    public static final Color DARK_GREEN = new Color(null, 0, 128, 0);

    /**
	 * Gr�n.
	 */
    public static final Color GREEN = new Color(null, 0, 255, 0);

    /**
	 * Hellgr�n.
	 */
    public static final Color LIGHT_GREEN = new Color(null, 180, 255, 180);

    /**
	 * Dunkelblau.
	 */
    public static final Color DARK_BLUE = new Color(null, 0, 0, 128);

    /**
	 * Blau.
	 */
    public static final Color BLUE = new Color(null, 0, 0, 255);

    /**
	 * Hellblau.
	 */
    public static final Color LIGHT_BLUE = new Color(null, 192, 192, 255);

    /**
	 * Orange.
	 */
    public static final Color ORANGE = new Color(null, 255, 160, 0);

    /**
	 * Die systemweite Vordergrundfarbe.
	 */
    public static final Color FG_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);

    /**
	 * Die systemweite Hintergrundfarbe.
	 */
    public static final Color BG_COLOR = Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

    /**
	 * Farbdefinition f�r <em>Fahrstra�e wird geschaltet</em>.
	 */
    public static final Color SEGMENT_SWITCHING = YELLOW;

    /**
	 * Farbdefinition f�r <em>Gleisabschnitt ist verriegelt und Strom eingeschaltet</em>.
	 */
    public static final Color SEGMENT_ENABLED = GREEN;

    /**
	 * Farbdefinition f�r <em>Gleisabschnitt ist verriegelt und Strom ausgeschaltet</em>.
	 */
    public static final Color SEGMENT_DISABLED = LIGHT_GREEN;

    /**
	 * Farbdefinition f�r <em>Gleisabschnitt ist belegt</em>.
	 */
    public static final Color SEGMENT_OCCUPIED = ORANGE;

    /**
	 * Farbdefinition f�r <em>Gleisabschnitt ist frei</em>.
	 */
    public static final Color SEGMENT_FREE = WHITE;
}
