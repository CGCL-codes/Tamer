package transfert;

import java.awt.Font;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 * Un nouveau thème Metal.
 */
class TransfertTheme extends DefaultMetalTheme {

    private FontUIResource font = new FontUIResource(new Font("Sans", Font.PLAIN, 11));

    public TransfertTheme() {
        super();
    }

    public FontUIResource getMenuTextFont() {
        return font;
    }

    public FontUIResource getSystemTextFont() {
        return font;
    }

    public FontUIResource getUserTextFont() {
        return font;
    }

    public FontUIResource getWindowTitleFont() {
        return font;
    }
}
