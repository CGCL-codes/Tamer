package xtrememp.skin.button.shaper;

import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;

/**
 *
 * @author Besmir Beqiri
 */
public class PreviousButtonShaper extends ButtonShaper {

    public String getDisplayName() {
        return "Previous";
    }

    public GeneralPath getButtonOutline(AbstractButton button, Insets insets, int w, int h) {
        int width = w - 1;
        int height = h - 1;
        int z = height / 3;
        Shape shape = new Ellipse2D.Double(width - z, 0, z, height);
        Area area = new Area(new RoundRectangle2D.Double(z / 2, 0, width - z, height, z, z));
        area.subtract(new Area(shape));
        return new GeneralPath(area);
    }

    public GeneralPath getButtonOutline(AbstractButton button, Insets insets) {
        return getButtonOutline(button, insets, button.getWidth(), button.getHeight());
    }

    public GeneralPath getButtonOutline(AbstractButton button) {
        return getButtonOutline(button, button.getInsets());
    }
}
