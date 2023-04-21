package org.expasy.jpl.matching.algo.model;

public interface JPLICoupleMatchModel<T> extends JPLIMatchView<T>, JPLReinitialisable {

    void addMatch(int i, int j);

    void removeMatch(int i, int j);
}
