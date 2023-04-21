package net.sf.rem65.editor.ZKpalette;

import java.io.IOException;
import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author gw152771
 */
public class ZKPaletteFactory {

    public static final String ZK_PALETTE_FOLDER = "ZKPalette";

    private static PaletteController palette = null;

    public ZKPaletteFactory() {
    }

    public static PaletteController createPalette() {
        try {
            if (null == palette) palette = PaletteFactory.createPalette(ZK_PALETTE_FOLDER, new MyActions());
            return palette;
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private static class MyActions extends PaletteActions {

        public Action[] getImportActions() {
            return null;
        }

        public Action[] getCustomPaletteActions() {
            return null;
        }

        public Action[] getCustomCategoryActions(Lookup arg0) {
            return null;
        }

        public Action[] getCustomItemActions(Lookup arg0) {
            return null;
        }

        public Action getPreferredAction(Lookup arg0) {
            return null;
        }
    }
}
