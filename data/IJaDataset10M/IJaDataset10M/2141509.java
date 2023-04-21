package net.deytan.wofee.gwt.convert;

import net.deytan.wofee.gwt.bean.ThemeBean;
import net.deytan.wofee.persistable.Theme;

public class ThemeConverter {

    public ThemeBean convert(final Theme theme) {
        final ThemeBean newTheme = new ThemeBean();
        newTheme.setKey(theme.getKey());
        newTheme.setName(theme.getName());
        newTheme.setIcon(theme.getIcon());
        newTheme.setDesign(theme.getDesign());
        return newTheme;
    }
}
