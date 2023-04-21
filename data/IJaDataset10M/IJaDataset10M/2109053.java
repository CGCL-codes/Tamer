package pl.wat.wcy.sna.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SNAToolTipPanel extends JPanel {

    public static final long serialVersionUID = 1;

    public SNAToolTipPanel(String item, java.awt.Point point, String path) {
        super();
        SNAImagePanel img = new SNAImagePanel();
        img.setSize(40, 50);
        img.setLocation(10, 5);
        this.add(img);
        int c = 1;
        String[] p = item.split(",");
        this.setSize(110, 15 * (p.length - 1) + 60);
        for (int i = 1; i < p.length; i++) {
            if (i == 1) {
                p[i] = String.valueOf((Integer.parseInt(p[i]) + 1));
            } else if (p[i].substring(0, 3).equals("img")) {
                c = 2;
                try {
                    BufferedImage l = ImageIO.read(new File(path + p[i].substring(4)));
                    img.SetImage(l);
                    l.flush();
                    l = null;
                } catch (Exception ex) {
                }
                continue;
            }
            JLabel l = new JLabel(p[i]);
            l.setLocation(10, 15 * (i - c) + 60);
            l.setSize(90, 20);
            this.add(l);
        }
        this.setBackground(new java.awt.Color(255, 255, 51));
        this.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 102), 1, true));
        this.setLocation(point.x + 20, point.y + 20);
    }
}
