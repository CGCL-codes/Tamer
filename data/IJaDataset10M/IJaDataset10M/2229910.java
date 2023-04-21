package org.regenstrief.ucum.functions;

import org.hl7.types.REAL;
import org.hl7.types.ValueFactory;
import org.regenstrief.ucum.FunctionPair;

public final class lgTimes2 implements FunctionPair {

    static final REAL TEN = ValueFactory.getInstance().REALvalueOfLiteral("10");

    static final REAL LN_TEN = TEN.log();

    static final REAL TWO = ValueFactory.getInstance().REALvalueOfLiteral("2");

    public REAL f_to(REAL x) {
        return TWO.times(x.log().dividedBy(LN_TEN));
    }

    public REAL f_from(REAL x) {
        return TEN.power(x.dividedBy(TWO));
    }
}
