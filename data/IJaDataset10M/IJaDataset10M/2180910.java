package com.triplea.dao;

public interface Consolidation {

    public Element getParent();

    public Element getChild();

    public double getWeight();

    public void setWeight(double weight) throws DataException;
}
