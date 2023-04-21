package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.geom.Dimension2D;
import java.util.List;
import net.sourceforge.plantuml.SpriteContainer;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.AbstractTextualComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public class ComponentBlueModernDelayText extends AbstractTextualComponent {

    public ComponentBlueModernDelayText(HtmlColor fontColor, UFont font, List<? extends CharSequence> stringsToDisplay, SpriteContainer spriteContainer) {
        super(stringsToDisplay, fontColor, font, HorizontalAlignement.CENTER, 4, 4, 4, spriteContainer);
    }

    @Override
    protected void drawInternalU(UGraphic ug, Area area, boolean withShadow) {
        final Dimension2D dimensionToUse = area.getDimensionToUse();
        final TextBlock textBlock = getTextBlock();
        final StringBounder stringBounder = ug.getStringBounder();
        final double textWidth = getTextWidth(stringBounder);
        final double textHeight = getTextHeight(stringBounder);
        final double xpos = (dimensionToUse.getWidth() - textWidth) / 2;
        final double ypos = (dimensionToUse.getHeight() - textHeight) / 2;
        ug.getParam().setColor(getFontColor());
        textBlock.drawU(ug, xpos, ypos + getMarginY());
    }

    @Override
    public double getPreferredHeight(StringBounder stringBounder) {
        return getTextHeight(stringBounder) + 20;
    }

    @Override
    public double getPreferredWidth(StringBounder stringBounder) {
        return getTextWidth(stringBounder) + 30;
    }
}
