package calclipse.caldron.gui.theme.themes.corona;

import java.awt.Color;
import java.util.EnumMap;

/**
 * A map containing colors used by the Corona theme,
 * such as foreground (text) colors, etc.
 * @see calclipse.caldron.gui.theme.themes.corona.CoronaImageBundle
 * @author T. Sommerland
 */
public class CoronaColorMap extends EnumMap<CoronaColorKey, Color> {

    private static final long serialVersionUID = 1L;

    public CoronaColorMap() {
        super(CoronaColorKey.class);
    }
}
