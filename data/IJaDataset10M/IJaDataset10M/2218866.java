package ru.cos.sim.meters.impl;

import java.util.ArrayList;
import ru.cos.cs.lengthy.Lengthy;
import ru.cos.sim.utils.Pair;

/**
 *
 * @author zroslaw
 */
public class LengthyDetector extends VehiclesDetector {

    public LengthyDetector(Lengthy lengthy, float position) {
        this.lengthiesAndPositions = new ArrayList<Pair<Lengthy, Float>>(1);
        this.lengthiesAndPositions.add(new Pair<Lengthy, Float>(lengthy, position));
    }
}
