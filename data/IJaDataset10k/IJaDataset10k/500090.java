package digix.gui;

import digix.model.Switch;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * A kapcsolok megjeleniteseert felelos osztaly
 * @author Andris
 */
public class SwitchView extends UnitView {

    /** a kirajzolando kep */
    protected static Image viewImage;

    static {
        String filename = null;
        SwitchView.viewImage = Toolkit.getDefaultToolkit().getImage(filename);
    }

    public SwitchView(Switch switch_) {
        super(switch_);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.black);
        g.drawString(this.modelUnit.GetName(), this.modelUnit.location.x, this.modelUnit.location.y - 5);
        g.fillRect(this.modelUnit.location.x, this.modelUnit.location.y, 15, 15);
        if (this.modelUnit.GetValue() == false) g.setColor(Color.white); else g.setColor(Color.red);
        g.fillOval(this.modelUnit.location.x + 2, this.modelUnit.location.y + 2, 10, 10);
    }
}
