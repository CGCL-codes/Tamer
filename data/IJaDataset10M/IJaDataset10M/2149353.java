package mdes.slick.sui.skin.simple;

import mdes.slick.sui.Component;
import mdes.slick.sui.Theme;
import mdes.slick.sui.skin.AbstractComponentAppearance;
import mdes.slick.sui.skin.SkinUtil;
import mdes.slick.sui.Skin;
import org.newdawn.slick.gui.GUIContext;

/**
 * A basic appearance that plugs into a component.
 * @author davedes
 */
public abstract class SimpleComponentAppearance extends AbstractComponentAppearance {

    public void update(GUIContext ctx, int delta, Component comp, Skin skin, Theme theme) {
    }

    public void install(Component comp, Skin skin, Theme theme) {
        SkinUtil.installFont(comp, ((SimpleSkin) skin).getFont());
        SkinUtil.installColors(comp, theme.getBackground(), theme.getForeground());
    }

    public void uninstall(Component comp, Skin skin, Theme theme) {
    }
}
