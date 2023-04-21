package org.bissa.weatherMonitor.ui.monitor;

import org.bissa.weatherMonitor.ui.Utils;
import javax.swing.*;
import javax.swing.border.Border;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class MainPanel extends JPanel {

    Image hotspotsImg;

    GridBagConstraints c = new GridBagConstraints();

    protected UIConfig uiConfig = new UIConfig();

    public MainPanel() {
        super(new GridBagLayout());
        c.insets.set(50, 50, 0, 0);
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.setAlignmentX(0);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1724 / 3 + 200, 1804 / 3 + 30);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            hotspotsImg = ImageIO.read(new File("modules/apps/weatherMonitor/pics/hotspots2.jpg"));
        } catch (IOException e) {
            Utils.showExecption(e);
        }
        g.drawImage(hotspotsImg, 0, 0, 1724 / 3 + 200, 1804 / 3 + 30, new Canvas());
    }

    public void createMonitorPanel() {
    }
}
