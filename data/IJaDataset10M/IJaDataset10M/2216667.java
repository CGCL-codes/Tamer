package org.gjt.sp.jedit.syntax;

import javax.swing.JPopupMenu;
import java.awt.Color;

/**
 * Encapsulates default settings for a text area. This can be passed
 * to the constructor once the necessary fields have been filled out.
 * The advantage of doing this over calling lots of set() methods after
 * creating the text area is that this method is faster.
 */
public class TextAreaDefaults {

    private static TextAreaDefaults DEFAULTS;

    public SyntaxDocument document;

    public boolean caretVisible;

    public boolean caretBlinks;

    public boolean blockCaret;

    public Color[] colors;

    public Color caretColor;

    public Color selectionColor;

    public Color lineHighlightColor;

    public boolean lineHighlight;

    public Color bracketHighlightColor;

    public boolean bracketHighlight;

    public Color eolMarkerColor;

    public boolean eolMarkers;

    public JPopupMenu popup;

    /**
	 * Returns a new TextAreaDefaults object with the default values filled
	 * in.
	 */
    public static TextAreaDefaults getDefaults() {
        if (DEFAULTS == null) {
            DEFAULTS = new TextAreaDefaults();
            DEFAULTS.document = new DefaultSyntaxDocument();
            DEFAULTS.caretVisible = true;
            DEFAULTS.caretBlinks = true;
            DEFAULTS.colors = SyntaxUtilities.getDefaultSyntaxColors();
            DEFAULTS.caretColor = Color.red;
            DEFAULTS.selectionColor = new Color(0xccccff);
            DEFAULTS.lineHighlightColor = new Color(204, 204, 204);
            DEFAULTS.lineHighlight = true;
            DEFAULTS.bracketHighlightColor = Color.black;
            DEFAULTS.bracketHighlight = true;
            DEFAULTS.eolMarkerColor = new Color(0x009999);
            DEFAULTS.eolMarkers = true;
        }
        return DEFAULTS;
    }
}
