package de.schwarzrot.dvd.theme.standard;

import java.awt.Rectangle;
import de.schwarzrot.dvd.theme.domain.data.MenueElementCategory;
import de.schwarzrot.dvd.theme.domain.data.MenueElementType;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;
import de.schwarzrot.dvd.theme.support.AbstractGraphicsBase;

public class StdRectangle extends AbstractGraphicsBase<StdTheme> {

    private static final long serialVersionUID = 713L;

    public StdRectangle() {
    }

    public StdRectangle(StdTheme theme, MenuePageType mpt, Rectangle r) {
        super(theme, mpt, MenueElementType.ITEM_RECTANGLE, MenueElementCategory.ITEM_GRAPHIC, MenueElementCategory.SELECTED_ITEM_GRAPHIC, r);
    }

    @Override
    public Class<StdTheme> getParentType() {
        return StdTheme.class;
    }
}
