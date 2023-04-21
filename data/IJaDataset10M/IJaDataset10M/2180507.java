package net.sourceforge.plantuml.posimo;

import java.awt.geom.Dimension2D;
import java.util.List;
import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Frame implements Component {

    private final List<? extends CharSequence> name;

    private final ISkinParam skinParam;

    private final Rose rose = new Rose();

    public Frame(List<? extends CharSequence> name, ISkinParam skinParam) {
        this.name = name;
        this.skinParam = skinParam;
    }

    public void drawU(UGraphic ug, Area area, Context2D context) {
        final Dimension2D dimensionToUse = area.getDimensionToUse();
        final HtmlColor lineColor = rose.getHtmlColor(skinParam, ColorParam.packageBorder);
        ug.getParam().setColor(lineColor);
        ug.getParam().setBackcolor(null);
        ug.getParam().setStroke(new UStroke(1.4));
        ug.draw(0, 0, new URectangle(dimensionToUse.getWidth(), dimensionToUse.getHeight()));
        ug.getParam().setStroke(new UStroke());
        final TextBlock textBlock = createTextBloc();
        textBlock.drawU(ug, 2, 2);
        final Dimension2D textDim = getTextDim(ug.getStringBounder());
        final double x = textDim.getWidth() + 6;
        final double y = textDim.getHeight() + 6;
        final UPolygon poly = new UPolygon();
        poly.addPoint(x, 0);
        poly.addPoint(x, y - 6);
        poly.addPoint(x - 6, y);
        poly.addPoint(0, y);
        poly.addPoint(0, 0);
        ug.getParam().setColor(lineColor);
        ug.getParam().setStroke(new UStroke(1.4));
        ug.draw(0, 0, poly);
        ug.getParam().setStroke(new UStroke());
    }

    public double getPreferredHeight(StringBounder stringBounder) {
        final Dimension2D dim = getTextDim(stringBounder);
        return dim.getHeight() + 8;
    }

    public double getPreferredWidth(StringBounder stringBounder) {
        final Dimension2D dim = getTextDim(stringBounder);
        return dim.getWidth() + 8;
    }

    public Dimension2D getTextDim(StringBounder stringBounder) {
        final TextBlock bloc = createTextBloc();
        return bloc.calculateDimension(stringBounder);
    }

    private TextBlock createTextBloc() {
        final UFont font = skinParam.getFont(FontParam.PACKAGE, null);
        final HtmlColor textColor = skinParam.getFontHtmlColor(FontParam.PACKAGE, null);
        final TextBlock bloc = TextBlockUtils.create(name, new FontConfiguration(font, textColor), HorizontalAlignement.LEFT, new SpriteContainerEmpty());
        return bloc;
    }
}
