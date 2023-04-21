package de.nb.musikplan.gui.prefs;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Contains constants and static method for the
 * outer appearance of the GUI: Fonts, Colors etc.
 *
 * @author nbudzyn 2008
 */
public class GUIAppearance {

    public static final Font MENU_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public static final Font STATUS_ROW_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public static final Font HEADER_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    public static final Font NORMAL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

    public static final Color EDITABLE_CELL_COLOR = Color.WHITE;

    public static final Color UNEDITABLE_CELL_COLOR = UIManager.getColor("info");

    /** not to be called */
    private GUIAppearance() {
        super();
    }

    public static final Border createWorkingAreaBorder() {
        return new EmptyBorder(4, 4, 4, 4);
    }

    public static final Border createDetailsLineBorder() {
        return new EmptyBorder(2, 12, 0, 0);
    }

    /**
	 * @param level level of hierarchy (1 / 2)
	 * @param quote whether we are quoting / citing something
	 * @return
	 */
    public static final Font getEmptyPanelFont(final int level, final boolean quote) {
        Font res = null;
        switch(level) {
            case 1:
                res = HEADER_FONT;
                break;
            case 2:
                res = NORMAL_FONT;
                break;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
        }
        if (quote) {
            return res.deriveFont(Font.ITALIC);
        }
        return res;
    }
}
