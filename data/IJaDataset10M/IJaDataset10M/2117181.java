package calclipse.caldron.gui.theme.themes.corona.skins;

import java.util.HashMap;
import java.util.Map;
import calclipse.caldron.gui.skinning.StylableProperties;
import calclipse.caldron.gui.theme.themes.corona.CoronaSkin;
import calclipse.core.gui.desktop.DesktopGUI;
import calclipse.core.gui.skin.Selection;
import calclipse.core.gui.skin.Skin;
import calclipse.core.gui.skin.SkinManager;
import calclipse.core.gui.skin.Style;
import calclipse.core.gui.skin.style.BackgroundStyle;

/**
 * The skin used for the desktop background.
 * @author T. Sommerland
 */
public class CoronaDesktopSkin implements CoronaSkin {

    private final Skin skin;

    public CoronaDesktopSkin(final BackgroundStyle bgStyle) {
        final Selection styleSel = new Selection(DesktopGUI.STYLABLE_SELECTOR);
        final Selection skinSel = new Selection(DesktopGUI.SKINNABLE_SELECTOR);
        final Style style = new Style(styleSel, getPropertyValues(bgStyle));
        skin = new Skin(skinSel, style);
    }

    @Override
    public void apply() {
        SkinManager.MANAGER.apply(skin);
    }

    @Override
    public void reset() {
        SkinManager.MANAGER.reset(skin);
    }

    private static Map<String, Object> getPropertyValues(final BackgroundStyle bgStyle) {
        final Map<String, Object> props = new HashMap<String, Object>();
        props.put(StylableProperties.BACKGROUND, bgStyle);
        return props;
    }
}
