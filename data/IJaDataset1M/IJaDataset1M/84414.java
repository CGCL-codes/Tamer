package pokeglobal.client.ui.base.theme;

import org.newdawn.slick.Color;
import pokeglobal.client.ui.base.Theme;
import pokeglobal.client.ui.base.skin.ColorUIResource;

/**
 *
 * @author Matt
 */
public class BitterLemonTheme implements Theme {

    private Color activeTitleBar1 = new ColorUIResource(195, 221, 95);

    private Color activeTitleBar2 = new ColorUIResource(150, 153, 155);

    private Color background = new ColorUIResource(244, 248, 255);

    private Color disabledMask = new ColorUIResource(.55f, .55f, .55f, .5f);

    private Color foreground = new ColorUIResource(57, 59, 62);

    private Color primary1 = new ColorUIResource(202, 205, 218);

    private Color primary2 = new ColorUIResource(228, 231, 246);

    private Color primary3 = new ColorUIResource(242, 240, 249);

    private Color primaryBorder1 = new ColorUIResource(138, 164, 68);

    private Color primaryBorder2 = new ColorUIResource(81, 100, 47);

    private Color secondary1 = new ColorUIResource(175, 201, 75);

    private Color secondary2 = new ColorUIResource(210, 240, 93);

    private Color secondary3 = new ColorUIResource(111, 133, 21);

    private Color secondaryBorder1 = new ColorUIResource(147, 153, 169);

    private Color secondaryBorder2 = new ColorUIResource(46, 53, 50);

    private Color titleBar1 = new ColorUIResource(208, 211, 226);

    private Color titleBar2 = new ColorUIResource(150, 153, 155);

    public BitterLemonTheme() {
        primary3.a = .8f;
    }

    public String getName() {
        return "Bitter Lemon";
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
