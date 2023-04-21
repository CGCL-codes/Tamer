package cn.rui.powermanja.pane.roam.hostile.speciaux;

import static cn.rui.powermanja.Powermanja.RAND;
import static cn.rui.powermanja.Powermanja.VIEW_BOUNDS;
import java.awt.Dimension;
import java.awt.Point;
import cn.rui.powermanja.pane.roam.Stage;
import cn.rui.powermanja.pane.roam.hostile.Speciaux;

public class Perturbians extends Speciaux {

    private int iconInc;

    private int a;

    public Perturbians(Stage stage) {
        super(stage, PERTURBIANS, 6, 50 + RAND.nextInt(50), 0.2f);
        Dimension size = getSize();
        x = VIEW_BOUNDS.x + RAND.nextInt(VIEW_BOUNDS.width - size.width);
        y = VIEW_BOUNDS.y - size.height - 64;
    }

    @Override
    protected boolean takeMove() {
        y += v;
        if (iconIdx != a) if (tempo.tick()) iconIdx = (iconIdx + iconInc + icons.length) % icons.length;
        int da = a - iconIdx;
        iconInc = ((0 < da && da <= 16) || (-32 <= da && da < -16)) ? 1 : -1;
        Point c = getCenter();
        Point target = vessel.getCenter();
        double dx = target.x - c.x;
        double dy = target.y - c.y;
        double d = Math.sqrt(dx * dx + dy * dy);
        float a2 = 0;
        if (d > 0) {
            a2 = (float) Math.acos(dx / d);
            if (Math.asin(dy / d) < 0) a2 = -a2;
        }
        a = ((int) ((a2 + 2 * Math.PI) * 16 / Math.PI)) % icons.length;
        return (y > VIEW_BOUNDS.y + VIEW_BOUNDS.height);
    }

    @Override
    protected void takeFire() {
        if (iconIdx == a) super.takeFire();
    }
}
