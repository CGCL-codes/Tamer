package de.schwarzrot.dvd.theme.standard;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import de.schwarzrot.dvd.theme.domain.data.MenueElementCategory;
import de.schwarzrot.dvd.theme.domain.data.MenueElementType;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;
import de.schwarzrot.dvd.theme.support.AbstractElementBase;

public class StdTitle extends AbstractElementBase<StdTheme> {

    private static final long serialVersionUID = 713L;

    private static final String SAMPLE_TEXT_ID = "Standard.title.sample.text";

    public StdTitle() {
        setType(MenueElementType.TITLE_ITEM);
        setCategory(MenueElementCategory.TITLE);
        setText(SAMPLE_TEXT_ID);
    }

    public StdTitle(StdTheme theme, MenuePageType pageType, Rectangle r) {
        super(theme, pageType, MenueElementType.TITLE_ITEM, MenueElementCategory.TITLE, r);
        setText(SAMPLE_TEXT_ID);
    }

    @Override
    public void drawItem(Graphics2D g2) {
        Color oc = g2.getColor();
        Font of = g2.getFont();
        Shape ocr = g2.getClip();
        Stroke os = g2.getStroke();
        drawBox(g2);
        drawStringInBox(g2, getText(), 0, 0, getWidth(), getHeight());
        g2.setColor(oc);
        g2.setFont(of);
        g2.setStroke(os);
        g2.setClip(ocr);
    }

    @Override
    public Class<StdTheme> getParentType() {
        return StdTheme.class;
    }
}
