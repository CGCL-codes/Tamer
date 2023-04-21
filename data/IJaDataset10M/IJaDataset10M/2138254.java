package org.loon.framework.android.game.srpg.effect;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.srpg.SRPGDelta;

public class SRPGForceEffect extends SRPGEffect {

    private int s_x;

    private int s_y;

    private LColor color;

    private int[] max;

    private boolean[] exist;

    private SRPGDelta[] arrow;

    public SRPGForceEffect(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, 100, LColor.red);
    }

    public SRPGForceEffect(int x1, int y1, int x2, int y2, int v, LColor color) {
        this.setExist(true);
        this.s_x = x1;
        this.s_y = y1;
        this.color = color;
        this.arrow = new SRPGDelta[v];
        this.max = new int[v];
        this.exist = new boolean[v];
        double[][] res = { { -16D, -12D }, { -8D, -16D }, { -8D, -8D } };
        double[] res1 = { 0.0D, 0.0D };
        for (int i = 0; i < arrow.length; i++) {
            max[i] = 0;
            exist[i] = true;
            int r1 = (x2 + (LSystem.random.nextInt(64)) - 32);
            int r2 = (y2 + (LSystem.random.nextInt(64)) - 32);
            double d = r1 - x1;
            double d1 = r2 - y1;
            double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
            double d3 = (d / d2) * 4D;
            double d4 = (d1 / d2) * 4D;
            max[i] = (int) (d2 / 4D + 0.5D);
            arrow[i] = new SRPGDelta(res, res1, d3, d4 * -1D, 36D);
            arrow[i].setVector((int) (SRPGDelta.getDegrees(x1 - r1, (y1 - r2) * -1) + 0.5D + (double) (i * -15)));
        }
    }

    public void draw(LGraphics g, int tx, int ty) {
        next();
        g.setColor(color);
        boolean flag = false;
        for (int j = 0; j < arrow.length; j++) {
            if (0 > max[j]) {
                exist[j] = false;
            }
            if (!exist[j]) {
                continue;
            }
            flag = true;
            if (j * 1 <= super.frame) {
                arrow[j].drawPaint(g, s_x - tx, LSystem.screenRect.height - (s_y - ty));
                max[j]--;
            }
        }
        if (!flag) {
            setExist(false);
        }
    }
}
