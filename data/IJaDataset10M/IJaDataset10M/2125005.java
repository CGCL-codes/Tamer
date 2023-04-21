package com.dukesoftware.utils.agent.alignment.function;

import com.dukesoftware.utils.agent.Agent;
import com.dukesoftware.utils.math.SimplePoint2d;

public interface IUpdateFunction<T extends Agent> {

    void update(T agent, SimplePoint2d other, double distanceSq, double dx, double dy);
}
