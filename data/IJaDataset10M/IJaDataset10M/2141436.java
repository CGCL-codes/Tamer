package org.matsim.core.mobsim.qsim.qnetsimengine;

import java.util.Queue;

public interface VehicleQ<E> extends Queue<E> {

    void addFirst(E previous);
}
