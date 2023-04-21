package com.elibera.m.xml;

import de.enough.polish.android.lcdui.Font;
import de.enough.polish.ui.TextField;
import de.enough.polish.ui.StyleSheet;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.utils.Tag;
import com.elibera.m.xml.display.BoxElement;
import com.elibera.m.xml.display.DisplayElement;
import com.elibera.m.xml.proc.ProcBoxElement;

/**
 * parst ein TextBox Tag
 */
public class XMLTagTextBox {

    public static Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);

    /**
	 * parst ein TextBox Tag
	 */
    public static DisplayElement parseTag(Tag tag, ParsingStruct p, PageSuite ps) {
        String id = null;
        int size = -1;
        int maxCharSize = 0, minCharSize = 0;
        String value = "", autovalue = null;
        boolean number = false;
        boolean isPassword = false;
        boolean allowEdit = true;
        for (int i = 0; i < tag.attrs.length; i++) {
            char c = tag.attrs[i].charAt(0);
            if (c == 'i') id = tag.values[i]; else if (c == 'v') value = tag.values[i]; else if (c == 's') size = HelperStd.parseInt(tag.values[i], size); else if (c == 'm') maxCharSize = HelperStd.parseInt(tag.values[i], maxCharSize); else if (c == 'f') minCharSize = HelperStd.parseInt(tag.values[i], minCharSize); else if (c == 'n') number = HelperStd.isXMLAttributValueTrue(tag.values[i]); else if (c == 'p') isPassword = HelperStd.isXMLAttributValueTrue(tag.values[i]); else if (c == 'a') allowEdit = HelperStd.isXMLAttributValueTrue(tag.values[i]); else if (c == 'o') autovalue = tag.values[i]; else if (c == 'd') allowEdit = !HelperStd.isXMLAttributValueTrue(tag.values[i]);
        }
        if (id == null) return null;
        if (autovalue != null) {
            int p2 = autovalue.indexOf('|');
            value = XMLTagOut.getOutTagValue(autovalue.substring(0, p2).charAt(0), autovalue.substring(p2 + 1), true, p, ps);
            if (value == null) value = "";
        }
        int constraint = TextField.ANY;
        if (number) constraint = TextField.DECIMAL;
        if (isPassword) constraint = TextField.PASSWORD;
        return new BoxElement(getBoxPositions(size, p), value, id, resetSize(size, p.canvasWidth), maxCharSize, minCharSize, constraint, allowEdit, isPassword, p.curFormID, (ProcBoxElement) HelperXMLParser.getProcDisplayElement(ProcBoxElement.CLASS_ID));
    }

    /**
	 * gibt die Größe für ein BoxElement zurück, anhand der Font-Größe
	 */
    public static int[] getBoxPositions(int size, ParsingStruct p) {
        int h = font.getHeight() + 4;
        int w = p.canvasWidth - 2 - p.curElementTopLeftX + p.internalNewStartXForNewLine;
        if (size > 0) w = (font.charWidth('m') + 1) * (size + 1) + 4;
        return HelperXMLParser.getObjectPositions(w, h, p);
    }

    /**
	 * setzt die Größe, sollte die breite größer als der canvas sein
	 */
    public static int resetSize(int size, int canvasWidth) {
        int mw = font.charWidth('m');
        if (size > 0) {
            int w = (mw + 1) * (size + 1) + 4;
            if (w < canvasWidth) return size;
        }
        int c = 1;
        int ws = mw + 8;
        while (ws < canvasWidth) {
            ws += mw + 1;
            c++;
        }
        return c;
    }
}
