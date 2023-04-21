package net.sf.japi.progs.jeduca.swing.font;

import java.awt.Component;
import java.awt.Font;
import java.util.Map;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;

/** List cell renderer for letting the user choose the font family.
 * This list cell renderer displays each font in its font.
 * @author $Author: chris $
 * @version $Id: FontFamilyListCellRenderer.java,v 1.2 2005/01/24 15:10:40 chris Exp $
 */
public class FontFamilyListCellRenderer extends DefaultListCellRenderer {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** The fonts to render.
     * @serial include
     */
    private Map<String, Font> fonts;

    /** Set the fonts to render.
     * The key of the map is the family name, the value of the map is the font to render.
     * @param fonts fonts to render
     */
    public void setFonts(final Map<String, Font> fonts) {
        this.fonts = fonts;
    }

    /** {@inheritDoc} */
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        c.setFont(fonts.get((String) value));
        return c;
    }
}
