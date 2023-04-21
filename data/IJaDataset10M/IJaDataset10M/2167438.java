package randomwalk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Random;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import qgd.util.Dual;
import qgd.util.InputField;

public class Rwalk extends Dual {

    public Histog hx, hy, hr;

    public Contour cr;

    public InputField step, samp;

    public JComboBox distribution;

    public JLabel distroLabel;

    public JPanel p;

    public JPanel controlPanel;

    public void main() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        step = new InputField("steps", 10, "30", controlPanel);
        controlPanel.add(step);
        samp = new InputField("samp", 10, "10000", controlPanel);
        controlPanel.add(samp);
        distroLabel = new JLabel("distribution");
        controlPanel.add(distroLabel);
        distribution = new JComboBox();
        distribution.addItem("Classic Random Walk");
        distribution.addItem("+/- 0.5 RW");
        controlPanel.add(distribution);
        controlPanel.add(runButton);
        controlPanel.add(pauseButton);
        mainPane.add(controlPanel, BorderLayout.NORTH);
        p = new JPanel();
        hx = new Histog();
        hy = new Histog();
        hr = new Histog();
        p.add(hx.getPanel());
        p.add(hy.getPanel());
        p.add(hr.getPanel());
        cr = new Contour();
        p.add(cr.getPanel());
        mainPane.add(p, BorderLayout.CENTER);
        show("Random Walk UH", "Random Walk");
    }

    public void run() {
        int N = step.readInt(1, 98);
        int S = samp.readInt(1, 9999999);
        double[][] pos = new double[3][S];
        Random randomGenerator = new Random();
        for (int i = 0; i < S; ++i) {
            if (!isRunning()) {
                break;
            }
            double x = 0;
            double y = 0;
            for (int ii = 0; ii < N; ++ii) {
                if (distribution.getSelectedIndex() == 0) {
                    double angle = Math.random() * 2 * Math.PI;
                    y += Math.sin(angle);
                    x += Math.cos(angle);
                } else {
                    y += 0.5 - randomGenerator.nextInt(2);
                    x += 0.5 - randomGenerator.nextInt(2);
                }
            }
            double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            pos[0][i] = r;
            pos[1][i] = x;
            pos[2][i] = y;
        }
        hx.draw(N, pos[1], 1);
        hy.draw(N, pos[2], 1);
        hr.draw(N, pos[0], 2);
        cr.draw(N, pos);
        finishRun();
    }
}
