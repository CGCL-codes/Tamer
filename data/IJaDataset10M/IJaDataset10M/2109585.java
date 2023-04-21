package saadadb.newdatabase;

import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author laurentmichel
 * * @version $Id: ProgressPanel.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 */
public class ProgressPanel extends JPanel {

    protected JFrame frame;

    JLabel[] labels = { new JLabel("1- Check SaadaDB Directory"), new JLabel("2- Check Database Connectivity"), new JLabel("3- Check Database reader role"), new JLabel("4- Check Tomcat Installation"), new JLabel("5- Add Collection User Attributes"), new JLabel("6- Set Coordinate Systems and Units"), new JLabel("7- Create SaadaDB") };

    ProgressPanel(JFrame frame) {
        super();
        this.frame = frame;
        this.setPreferredSize(new Dimension(250, 300));
        this.setBorder(BorderFactory.createTitledBorder("Progress Panel"));
        this.setLayout(new GridLayout(labels.length, 1));
        labels[0].setEnabled(true);
        this.add(labels[0], 0, 0);
        for (int i = 1; i < labels.length; i++) {
            labels[i].setEnabled(false);
            this.add(labels[i], 0, i);
        }
    }

    public void jumpToStep(int screen_number) {
        if (screen_number >= labels.length) {
            return;
        }
        for (int i = 0; i <= screen_number; i++) {
            labels[i].setEnabled(true);
        }
        for (int i = (screen_number + 1); i < labels.length; i++) {
            labels[i].setEnabled(false);
        }
    }
}
