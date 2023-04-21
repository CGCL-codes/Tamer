package net.community.chest.awt.menu;

import java.awt.MenuShortcut;
import net.community.chest.awt.dom.converter.KeyCodeValueInstantiator;
import net.community.chest.dom.AbstractXmlValueStringInstantiator;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Sep 7, 2008 3:24:21 PM
 */
public class MenuShortcutValueStringInstantiator extends AbstractXmlValueStringInstantiator<MenuShortcut> {

    public MenuShortcutValueStringInstantiator() {
        super(MenuShortcut.class);
    }

    public static final String toString(final MenuShortcut inst) {
        if (null == inst) return null;
        final int k = inst.getKey();
        final String ks = KeyCodeValueInstantiator.toString(k);
        if ((null == ks) || (ks.length() <= 0)) throw new IllegalArgumentException("toString(" + inst + ") unknown key: " + k);
        if (inst.usesShiftModifier()) return "SHIFT+" + ks; else return ks;
    }

    @Override
    public String convertInstance(MenuShortcut inst) throws Exception {
        return (null == inst) ? null : inst.toString();
    }

    public static final MenuShortcut fromString(final String v) {
        final String s = StringUtil.getCleanStringValue(v);
        final int sLen = (null == s) ? 0 : s.length();
        if (sLen <= 0) return null;
        final int sPos = s.indexOf('+');
        final String ks = (sPos < 0) ? s : s.substring(sPos + 1);
        final Integer k = KeyCodeValueInstantiator.fromString(ks);
        if (null == k) throw new IllegalArgumentException("fromString(" + s + ") unknown key code");
        if (sPos < 0) return new MenuShortcut(k.intValue());
        final String mod = (sPos < 0) ? null : s.substring(0, sPos);
        if (!"SHIFT".equalsIgnoreCase(mod)) throw new IllegalArgumentException("fromString(" + s + ") unknown shift state");
        return new MenuShortcut(k.intValue(), true);
    }

    @Override
    public MenuShortcut newInstance(String s) throws Exception {
        return fromString(s);
    }

    public static final MenuShortcutValueStringInstantiator DEFAULT = new MenuShortcutValueStringInstantiator();
}
