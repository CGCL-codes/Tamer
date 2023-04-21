package crazy.java.Aetas;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Ashish (Java Crazy)
 */
public class AnimationHandler implements ActionListener {

    JFrame frame;

    Point loc;

    Dimension d;

    int i = 640, j = 479, mi, mj;

    Timer minimizeTimer = new Timer(30, this);

    Timer moveTimer = new Timer(30, this);

    public void actionPerformed(ActionEvent ae) {
        loc = frame.getLocation();
        if (ae.getSource().equals(minimizeTimer)) {
            frame.getGlassPane().setVisible(false);
            frame.remove(AetasMain.jLabel1);
            frame.remove(AetasMain.panel);
            if (i >= 300) {
                frame.setSize(i, 479);
                i -= 15;
            } else {
                if (j >= 225) {
                    frame.setSize(300, j);
                    j -= 15;
                } else {
                    minimizeTimer.stop();
                    mi = loc.x;
                    mj = loc.y;
                    frame.setVisible(false);
                    javax.swing.JFrame m = new MiniFrame();
                    m.setSize(300, 225);
                    m.setLocation(loc);
                }
            }
        }
        if (ae.getSource().equals(moveTimer)) {
            if (mi <= (d.width - 300)) {
                frame.setLocation(mi, mj);
                mi += 10;
            } else {
                if (mj <= (d.height - 225)) {
                    frame.setLocation(mi, mj);
                    mj += 10;
                } else moveTimer.stop();
            }
        }
    }

    public AnimationHandler(JFrame f) {
        frame = f;
    }

    public void minimize() {
        minimizeTimer.start();
    }

    public void move() {
        d = Toolkit.getDefaultToolkit().getScreenSize();
        mi = ((d.width) / 5) - 5;
        mj = ((d.height) / 5) + 2;
        moveTimer.start();
    }
}
