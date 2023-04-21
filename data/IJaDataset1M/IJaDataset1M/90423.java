package com.scholardesk.filtering;

public interface FilterStrategy {

    public void addProbability(double _probability);

    public void clear();

    public double getProbability();
}
