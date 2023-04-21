package aidc.aigui.box.abstr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class BoxLabel extends JLabel {

    private AbstractBox aBox;

    BoxLabel(AbstractBox abx, ImageIcon icon) {
        super(icon);
        aBox = abx;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawString(Integer.toString(aBox.getBoxNumber()), 3, getIcon().getIconHeight() - 4);
        if (aBox.isModified()) g2d.drawString("*", getIcon().getIconWidth() - 8, getIcon().getIconHeight() - 4);
    }
}
