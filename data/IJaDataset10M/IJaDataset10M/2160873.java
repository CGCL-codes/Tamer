package com.ryanm.trace.game.ai.behaviours;

import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import com.ryanm.trace.game.Arena;
import com.ryanm.trace.game.Trace;
import com.ryanm.trace.game.TraceSegment;
import com.ryanm.util.geom.VectorUtils;
import com.ryanm.util.math.Trig;

/**
 * Seeks the nearest reachable gap
 * 
 * @author ryanm
 */
public class SeekGap extends Behaviour {

    /***/
    public float seekWeight = 0.3f;

    @Override
    public void process(Trace t, Arena a) {
        reset();
        List<TraceSegment> gaps = a.getGapList();
        if (!gaps.isEmpty()) {
            float turnTime = 360 / t.rotate;
            float turnCirc = t.speed * turnTime;
            float turnRad = turnCirc / Trig.PI / 2;
            float xOff = turnRad * Trig.cos(t.angleRads + Trig.PI / 2);
            float yOff = turnRad * Trig.sin(t.angleRads + Trig.PI / 2);
            Vector2f leftDeadZone = new Vector2f(t.position.x + xOff, t.position.y + yOff);
            Vector2f rightDeadZone = new Vector2f(t.position.x - xOff, t.position.y - yOff);
            Vector2f travelDir = new Vector2f(t.position);
            travelDir.x += Trig.cos(t.angleRads);
            travelDir.y += Trig.sin(t.angleRads);
            TraceSegment closest = null;
            float minD = Float.MAX_VALUE;
            Vector2f gapMid = new Vector2f();
            Vector2f toGapMid = new Vector2f();
            for (int i = 0; i < gaps.size(); i++) {
                TraceSegment gap = gaps.get(i);
                gapMid.set(gap.ax + gap.bx, gap.ay + gap.by);
                gapMid.scale(0.5f);
                Vector2f.sub(gapMid, t.position, toGapMid);
                if (VectorUtils.distance(gapMid, leftDeadZone) > turnRad) {
                    if (VectorUtils.distance(gapMid, rightDeadZone) > turnRad) {
                        {
                            if (a.collision(t.position.x, t.position.y, gapMid.x, gapMid.y).length == 0) {
                                float d = VectorUtils.distance(t.position, gapMid);
                                if (d < minD) {
                                    minD = d;
                                    closest = gap;
                                }
                            }
                        }
                    }
                }
            }
            if (closest != null) {
                weight = seekWeight;
                gapMid.set(closest.ax + closest.bx, closest.ay + closest.by);
                gapMid.scale(0.5f);
                action = Waypoint.seek(t, gapMid);
            }
        }
    }

    @Override
    public float[] getGenome() {
        return new float[] { seekWeight };
    }

    @Override
    public void setGenome(float[] gene) {
        seekWeight = gene[0];
    }
}
