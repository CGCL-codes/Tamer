package org.amse.audalov.model.impl;

import org.amse.audalov.math.Real;
import org.amse.audalov.model.*;

public class Distance implements IDistance {

    private IPoint myBegin, myEnd;

    private Real myValue;

    public Distance(IPoint begin, IPoint end, Real value) {
        myBegin = begin;
        myEnd = end;
        myValue = value;
    }

    public IPoint getBegin() {
        return myBegin;
    }

    public IPoint getEnd() {
        return myEnd;
    }

    public Real getValue() {
        return myValue;
    }
}
