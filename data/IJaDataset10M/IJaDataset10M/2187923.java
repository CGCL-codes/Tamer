package mipt.math.fuzzy.op.impl;

import mipt.math.fuzzy.*;
import mipt.math.fuzzy.op.*;

/**
 * @author Lee
 */
public class EAdd extends Add {

    public final FuzzyNumber calc(FuzzyNumber a, FuzzyNumber b) {
        if (b.getType() == Fuzzy.EXACT_NUMBER) return a.addFuzzy(b.doubleValue());
        if (a.getType() == Fuzzy.EXACT_NUMBER) return b.addFuzzy(a.doubleValue());
        return null;
    }
}
