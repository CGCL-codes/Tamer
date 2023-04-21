package de.schwarzrot.dvd.theme.standard;

import de.schwarzrot.app.domain.VideoPageFormat;
import de.schwarzrot.dvd.theme.domain.ThemeBase;
import de.schwarzrot.dvd.theme.domain.ThemeElement;
import de.schwarzrot.dvd.theme.domain.data.MenueElementType;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 */
public class StdTheme extends ThemeBase {

    private static final long serialVersionUID = 713L;

    private static final Map<Object, String> elementVariantMap;

    public StdTheme() {
        setThemeName("standard");
        setAspect(VideoPageFormat.STANDARD);
    }

    @Override
    public ThemeElement<StdTheme> createThemeElement(MenuePageType mpt, MenueElementType met, Rectangle r) {
        ThemeElement<StdTheme> rv = null;
        switch(met) {
            case TITLE_ITEM:
                rv = new StdTitle(this, mpt, r);
                break;
            case HEADER_ITEM:
                rv = new StdHeader(this, mpt, r);
                break;
            case SKIN_IMAGE:
                rv = new StdSkinImage(this, mpt, r);
                break;
            case THEME_IMAGE:
                rv = new StdThemeImage(this, mpt, r);
                break;
            case JOB_IMAGE:
                rv = new StdJobImage(this, mpt, r);
                break;
            case REC_IMAGE:
                rv = new StdRecImage(this, mpt, r);
                break;
            case ITEM:
                rv = new StdItem(this, mpt, r);
                break;
            case ITEM_AREA:
                rv = new StdItemRectangle(this, mpt, r);
                break;
            case DESCRIPTION:
                rv = new StdDescriptionBox(this, mpt, r);
                break;
            case MAINMENUE_BUTTON:
                rv = new StdMainMenueButton(this, mpt, r);
                break;
            case NEXT_BUTTON:
                rv = new StdNextButton(this, mpt, r);
                break;
            case PREVIOUS_BUTTON:
                rv = new StdPreviousButton(this, mpt, r);
                break;
            case PLAY_BUTTON:
                rv = new StdPlayButton(this, mpt, r);
                break;
            case PLAY_ALL_BUTTON:
                rv = new StdPlayAllButton(this, mpt, r);
                break;
            case BONUSMENUE_BUTTON:
                rv = new StdBonusMenueButton(this, mpt, r);
                break;
            case LANGUAGE_BUTTON:
                rv = new StdLanguageButton(this, mpt, r);
                break;
            case SUBTITLE_BUTTON:
                rv = new StdSubTitleButton(this, mpt, r);
                break;
            case CHAPTER_BUTTON:
                rv = new StdChapterButton(this, mpt, r);
                break;
            case ITEM_ELLIPSE:
                rv = new StdEllipse(this, mpt, r);
                break;
            case ITEM_COMPOUND:
            case ITEM_RECTANGLE:
                rv = new StdRectangle(this, mpt, r);
                break;
            case BUTTON_ELLIPSE:
                rv = new StdEllipse(this, mpt, r);
                break;
            case BUTTON_COMPOUND:
            case BUTTON_RECTANGLE:
                rv = new StdRectangle(this, mpt, r);
                break;
        }
        return rv;
    }

    @Override
    public Map<Object, String> getElementVariantMap() {
        return elementVariantMap;
    }

    static {
        elementVariantMap = new HashMap<Object, String>();
        elementVariantMap.put(MenueElementType.TITLE_ITEM, StdTitle.class.getName());
        elementVariantMap.put(MenueElementType.HEADER_ITEM, StdHeader.class.getName());
        elementVariantMap.put(MenueElementType.SKIN_IMAGE, StdSkinImage.class.getName());
        elementVariantMap.put(MenueElementType.THEME_IMAGE, StdThemeImage.class.getName());
        elementVariantMap.put(MenueElementType.JOB_IMAGE, StdJobImage.class.getName());
        elementVariantMap.put(MenueElementType.REC_IMAGE, StdRecImage.class.getName());
        elementVariantMap.put(MenueElementType.ITEM, StdItem.class.getName());
        elementVariantMap.put(MenueElementType.ITEM_AREA, StdItemRectangle.class.getName());
        elementVariantMap.put(MenueElementType.DESCRIPTION, StdDescriptionBox.class.getName());
        elementVariantMap.put(MenueElementType.MAINMENUE_BUTTON, StdMainMenueButton.class.getName());
        elementVariantMap.put(MenueElementType.NEXT_BUTTON, StdNextButton.class.getName());
        elementVariantMap.put(MenueElementType.PREVIOUS_BUTTON, StdPreviousButton.class.getName());
        elementVariantMap.put(MenueElementType.PLAY_BUTTON, StdPlayButton.class.getName());
        elementVariantMap.put(MenueElementType.PLAY_ALL_BUTTON, StdPlayAllButton.class.getName());
        elementVariantMap.put(MenueElementType.BONUSMENUE_BUTTON, StdBonusMenueButton.class.getName());
        elementVariantMap.put(MenueElementType.LANGUAGE_BUTTON, StdLanguageButton.class.getName());
        elementVariantMap.put(MenueElementType.SUBTITLE_BUTTON, StdSubTitleButton.class.getName());
        elementVariantMap.put(MenueElementType.CHAPTER_BUTTON, StdChapterButton.class.getName());
        elementVariantMap.put(MenueElementType.ITEM_RECTANGLE, StdRectangle.class.getName());
        elementVariantMap.put(MenueElementType.ITEM_ELLIPSE, StdEllipse.class.getName());
        elementVariantMap.put(MenueElementType.ITEM_COMPOUND, StdRectangle.class.getName());
        elementVariantMap.put(MenueElementType.BUTTON_RECTANGLE, StdRectangle.class.getName());
        elementVariantMap.put(MenueElementType.BUTTON_ELLIPSE, StdEllipse.class.getName());
        elementVariantMap.put(MenueElementType.BUTTON_COMPOUND, StdRectangle.class.getName());
    }
}
