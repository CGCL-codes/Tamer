package customer.GUI.components.panels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NetPlayCustomFilterPanel extends JPanel {

    public NetPlayCustomFilterPanel() {
        this.setBackground(Color.RED);
        this.add(new JLabel("FILTER"));
        this.setPreferredSize(new Dimension(800, 150));
    }
}
