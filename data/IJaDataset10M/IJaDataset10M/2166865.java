package com.dukesoftware.utils.agent.drawmode;

import java.awt.Point;
import com.dukesoftware.utils.agent.Agent;
import com.dukesoftware.utils.common.IPairCallBack;
import com.dukesoftware.utils.math.MathUtils;

public class FindCallBack implements IPairCallBack<Agent, Point> {

    private final double w2, h2;

    public FindCallBack(double w2, double h2) {
        this.w2 = w2;
        this.h2 = h2;
    }

    @Override
    public void exec(Agent target, Point p) {
        if (target.x - w2 < p.x && target.x + w2 > p.x && target.y - h2 < p.y && target.y + h2 > p.y && MathUtils.hypotSq((target.x - p.x) / w2, (target.y - p.y) / h2) < 1) {
            System.out.println(target.x + ":" + target.y);
        }
    }
}
