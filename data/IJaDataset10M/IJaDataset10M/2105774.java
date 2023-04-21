package game.trainers;

import game.gui.MyConfig;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class LevenbergMarquardtConfig implements MyConfig {

    int itermax;

    double tolerr;

    transient JTextField r;

    transient JTextField d;

    public LevenbergMarquardtConfig() {
        itermax = 200;
        tolerr = 0.5;
    }

    public JPanel showPanel() {
        JPanel p = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p1.setLayout(new FlowLayout());
        p2.setLayout(new FlowLayout());
        p1.add(new JLabel("Iterations maximum:"));
        r = new JTextField(Integer.toString(itermax));
        p1.add(r);
        p1.add(new JLabel("Tolerance:"));
        d = new JTextField(Double.toString(tolerr));
        p1.add(d);
        p.add(p2);
        p.add(p1);
        return p;
    }

    public void setValues() {
        itermax = Integer.valueOf(r.getText()).intValue();
        tolerr = Integer.valueOf(d.getText()).doubleValue();
    }

    public int getIterMax() {
        return itermax;
    }

    public double getTolerance() {
        return tolerr;
    }
}
