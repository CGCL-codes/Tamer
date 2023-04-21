package org.nakedobjects.xat.integ.junit4.injection.fixtures;

import java.util.Vector;

public class AllFixtureSpiesVector extends Vector<Object> {

    private static final long serialVersionUID = 1L;

    public AllFixtureSpiesVector() {
        add(new FixtureSpy());
        add(new FixtureSpy2());
    }
}
