package de.joergjahnke.common.jme;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

/**
 * Creates a form with formatted text.
 * The following formatting options can be used:
 * <UL>
 * <LI>b    will display bold text
 * <LI>i    will display italic text
 * <LI>u    will display underlined text
 * <LI>-    will reduce the font size to smaller text
 * <LI>+    will increase the font size to larger text
 * <LI>h    will display a hyperlink
 * <LI>m    will use a monospaced font
 * <LI>p    will use a proportional font
 * </UL>
 * Formatted text must be started using '{' plus the formatting character
 * and ended using a '}. E.g. '{-{bThis is a text}} will display the text
 * 'This is a test' with a small bold font.
 * 
 * @author  Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public class FormattedTextForm extends Form {

    private final MIDlet midlet;

    /**
     * Create a new formatted text form
     * 
     * @param   title   form title
     * @param   is  input stream containing the text to display
     * @throws IOException if the stream cannot be read from
     */
    public FormattedTextForm(final MIDlet midlet, final String title, final InputStream is) throws IOException {
        super(title);
        this.midlet = midlet;
        final Stack modes = new Stack();
        StringBuffer buffer = new StringBuffer();
        int c;
        while ((c = is.read()) >= 0) {
            switch((char) c) {
                case '{':
                    if (buffer.length() > 0) {
                        append(createStringItem(buffer, modes));
                    }
                    buffer = new StringBuffer();
                    modes.push(new Character((char) is.read()));
                    break;
                case '}':
                    if (buffer.length() > 0) {
                        append(createStringItem(buffer, modes));
                    }
                    buffer = new StringBuffer();
                    modes.pop();
                    break;
                case '\r':
                    break;
                default:
                    buffer.append((char) c);
            }
        }
        if (buffer.length() > 0) {
            append(createStringItem(buffer, modes));
        }
    }

    /**
     * Creates a string item for the form
     * 
     * @param   buffer  contains the text of the item
     * @param   formatting  contains the formatting options
     */
    private StringItem createStringItem(final StringBuffer buffer, final Vector formatting) {
        int style = Font.STYLE_PLAIN;
        int appearance = Item.PLAIN;
        int size = Font.SIZE_MEDIUM;
        int face = Font.FACE_SYSTEM;
        for (final Enumeration en = formatting.elements(); en.hasMoreElements(); ) {
            final Character c = (Character) en.nextElement();
            switch(c.charValue()) {
                case 'b':
                    style = Font.STYLE_BOLD;
                    break;
                case 'i':
                    style = Font.STYLE_ITALIC;
                    break;
                case 'u':
                    style = Font.STYLE_UNDERLINED;
                    break;
                case 'h':
                    appearance = Item.HYPERLINK;
                    break;
                case '-':
                    size = size == Font.SIZE_LARGE ? Font.SIZE_MEDIUM : Font.SIZE_SMALL;
                    break;
                case '+':
                    size = size == Font.SIZE_SMALL ? Font.SIZE_MEDIUM : Font.SIZE_LARGE;
                    break;
                case 'm':
                    face = Font.FACE_MONOSPACE;
                    break;
                case 'p':
                    style = Font.FACE_PROPORTIONAL;
                    break;
            }
        }
        final StringItem stringItem = new StringItem(null, LocalizationSupport._convertString(buffer.toString()), appearance);
        stringItem.setFont(Font.getFont(face, style, size));
        if (appearance == Item.HYPERLINK) {
            final Command browseCommand = new Command(LocalizationSupport.getMessage("Browse"), Command.ITEM, 2);
            stringItem.addCommand(browseCommand);
            stringItem.setItemCommandListener(new ItemCommandListener() {

                public void commandAction(final Command c, final Item item) {
                    try {
                        midlet.platformRequest(((StringItem) item).getText());
                    } catch (Exception e) {
                    }
                }
            });
        }
        return stringItem;
    }
}
