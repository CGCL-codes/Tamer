package org.spantus.math.services;

import java.util.List;
import java.util.Map;

public interface ConvexHullService {

    public Map<Integer, Double> calculateConvexHull(List<Double> signal);

    public List<Double> calculateConvexHullTreshold(List<Double> signalDouble);
}
