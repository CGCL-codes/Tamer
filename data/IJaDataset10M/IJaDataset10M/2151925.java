package polr.client.ui.base.theme;

import org.newdawn.slick.Color;
import polr.client.ui.base.Theme;
import polr.client.ui.base.skin.ColorUIResource;

/**
 * 
 * @author davedes
 */
public class CopperTheme implements Theme {

    private Color activeTitleBar1 = new ColorUIResource(204, 158, 124);

    private Color activeTitleBar2 = new ColorUIResource(170, 173, 175);

    private Color background = new ColorUIResource(144, 144, 144);

    private Color disabledMask = new ColorUIResource(.55f, .55f, .55f, .5f);

    private Color foreground = new ColorUIResource(57, 29, 5);

    private Color primary1 = new ColorUIResource(179, 141, 127);

    private Color primary2 = new ColorUIResource(229, 177, 139);

    private Color primary3 = new ColorUIResource(153, 137, 125);

    private Color primaryBorder1 = new ColorUIResource(133, 117, 105);

    private Color primaryBorder2 = new ColorUIResource(57, 29, 5);

    private Color secondary1 = new ColorUIResource(210, 111, 22);

    private Color secondary2 = new ColorUIResource(229, 177, 139);

    private Color secondary3 = new ColorUIResource(142, 149, 156);

    private Color secondaryBorder1 = new ColorUIResource(96, 103, 113);

    private Color secondaryBorder2 = new ColorUIResource(70, 71, 55);

    private Color titleBar1 = new ColorUIResource(180, 176, 178);

    private Color titleBar2 = new ColorUIResource(170, 173, 175);

    public String getName() {
        return "Copper";
    }

    public Color getActiveTitleBar1() {
        return activeTitleBar1;
    }

    public Color getActiveTitleBar2() {
        return activeTitleBar2;
    }

    public Color getBackground() {
        return background;
    }

    public Color getDisabledMask() {
        return disabledMask;
    }

    public Color getForeground() {
        return foreground;
    }

    public Color getPrimary1() {
        return primary1;
    }

    public Color getPrimary2() {
        return primary2;
    }

    public Color getPrimary3() {
        return primary3;
    }

    public Color getPrimaryBorder1() {
        return primaryBorder1;
    }

    public Color getPrimaryBorder2() {
        return primaryBorder2;
    }

    public Color getSecondary1() {
        return secondary1;
    }

    public Color getSecondary2() {
        return secondary2;
    }

    public Color getSecondary3() {
        return secondary3;
    }

    public Color getSecondaryBorder1() {
        return secondaryBorder1;
    }

    public Color getSecondaryBorder2() {
        return secondaryBorder2;
    }

    public Color getTitleBar1() {
        return titleBar1;
    }

    public Color getTitleBar2() {
        return titleBar2;
    }
}
