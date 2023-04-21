package jiso;

import jiso.window.*;
import jiso.util.HealthBar;
import java.awt.*;

/**
 *
 * @author Neil
 */
public class JISOGame extends AbstractAnimationPanel {

    HealthBar hb, hb2;

    public JISOGame() {
        super(new Dimension(600, 500));
        hb = HealthBar.createHPBar(0, 100, 100);
        hb2 = HealthBar.createMPBar(0, 100, 85);
        hb.setX(50 + 12);
        hb.setY(50);
        hb2.setX(50);
        hb2.setY(62);
    }

    @Override
    protected Graphics renderFrame(Graphics g) {
        hb.paint(g);
        hb2.paint(g);
        return g;
    }
}
