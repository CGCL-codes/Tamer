package de.schlund.pfixcore.editor2.core.spring.internal;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.pustefixframework.editor.common.dom.AbstractThemeList;
import org.pustefixframework.editor.common.dom.Theme;
import org.pustefixframework.editor.common.dom.ThemeList;
import de.schlund.pfixcore.editor2.core.spring.ThemeFactoryService;
import de.schlund.pfixxml.targets.Themes;

/**
 * Implementation of ThemeList using de.schlund.pfixxml.targets.Themes internally.
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 * @see de.schlund.pfixxml.targets.Themes
 */
public class ThemeListImpl extends AbstractThemeList implements ThemeList {

    private ArrayList<Theme> themes;

    /**
     * Creates a ThemeList object
     * 
     * @param themes Themes object as used by the Pustefix generator
     */
    public ThemeListImpl(ThemeFactoryService themefactory, Themes themes) {
        if (themes.getThemesArr().length == 0) {
            String msg = "Themes array should not be empty!";
            Logger.getLogger(this.getClass()).warn(msg);
        }
        this.themes = new ArrayList<Theme>();
        String[] array = themes.getThemesArr();
        for (int i = 0; i < array.length; i++) {
            Theme theme = themefactory.getTheme(array[i]);
            this.themes.add(theme);
        }
    }

    public List<Theme> getThemes() {
        return new ArrayList<Theme>(this.themes);
    }
}
