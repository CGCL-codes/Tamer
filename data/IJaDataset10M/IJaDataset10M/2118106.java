package configuration.game.trainers;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import configuration.AbstractCfgBean;

public class QuasiNewtonLimitedMemoryBfgsConfig extends AbstractCfgBean {

    private static final long serialVersionUID = 9077307123817518140L;

    public void setValues() {
    }

    public JPanel showPanel() {
        JPanel p = new JPanel();
        JPanel p1 = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p1.setLayout(new FlowLayout());
        p1.add(new JLabel("No parameters are required"));
        return p;
    }
}
