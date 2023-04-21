package net.sf.RecordEditor.utils.swing;

/**
 * table rendor for 3 line hex display
 *
 * @author Bruce Martin
 *
 */
public class HexTwoLineRender extends HexGenericRender {

    /**
     * @param font fontname
     */
    public HexTwoLineRender(final String font) {
        super(font, new HexTwoLineField(font));
    }
}
