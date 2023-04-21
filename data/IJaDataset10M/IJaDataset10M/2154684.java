package de.schwarzrot.dvd.theme;

import de.schwarzrot.dvd.theme.domain.ElementSkin;
import de.schwarzrot.dvd.theme.domain.data.MenueElementCategory;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;

/**
 * interface to provide the functionality used by the {@code ThemeElement}s to access their skinned
 * properties.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 */
public interface SkinProvider {

    public ElementSkin getSkin4Element(MenuePageType mpt, MenueElementCategory mec);
}
