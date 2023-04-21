package cummingsm.abstracts;

import cummingsm.interfaces.Baracadable;

public abstract class Baracade implements Baracadable {

    private double _baracadeLevel = 0;

    public double getBaracadeStrength() {
        return _baracadeLevel;
    }

    public void modBaracdeLevel(double mod) {
        _baracadeLevel += mod;
    }
}
