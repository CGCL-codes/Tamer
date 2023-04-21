package net.sourceforge.plantuml.skin.bluemodern;

import java.awt.geom.Dimension2D;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.skin.AbstractComponent;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class ComponentBlueModernGroupingBody extends AbstractComponent {

    private final HtmlColor foregroundColor;

    private final HtmlColor generalBackgroundColor;

    public ComponentBlueModernGroupingBody(HtmlColor generalBackgroundColor, HtmlColor foregroundColor) {
        this.foregroundColor = foregroundColor;
        this.generalBackgroundColor = generalBackgroundColor;
    }

    @Override
    protected void drawBackgroundInternalU(UGraphic ug, Area area) {
        final Dimension2D dimensionToUse = area.getDimensionToUse();
        ug.getParam().setColor(generalBackgroundColor);
        ug.getParam().setBackcolor(generalBackgroundColor);
        ug.draw(0, 0, new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));
    }

    @Override
    protected void drawInternalU(UGraphic ug, Area area, boolean withShadow) {
        final Dimension2D dimensionToUse = area.getDimensionToUse();
        ug.getParam().setStroke(new UStroke(2));
        ug.getParam().setColor(foregroundColor);
        ug.draw(0, 0, new ULine(0, dimensionToUse.getHeight()));
        ug.draw(dimensionToUse.getWidth(), 0, new ULine(0, dimensionToUse.getHeight()));
        ug.getParam().setStroke(new UStroke());
    }

    @Override
    public double getPreferredWidth(StringBounder stringBounder) {
        return 0;
    }

    @Override
    public double getPreferredHeight(StringBounder stringBounder) {
        return 5;
    }
}
