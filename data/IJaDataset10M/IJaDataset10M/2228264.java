package views.widgets.geographic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ColorItem extends RectangleItem {

    private static final long serialVersionUID = 7912515586132829875L;

    protected static int squareSize = 20;

    public ColorItem(Color c, String t) {
        super(c, squareSize, new BasicStroke(1), Color.black, t);
    }

    @Override
    public void change(WorldMap worldMap, int continentNb, float value) {
        colors[continentNb] = new Color((int) (value * maxColor.getRed()), (int) (value * maxColor.getGreen()), (int) (value * maxColor.getBlue()));
    }
}
