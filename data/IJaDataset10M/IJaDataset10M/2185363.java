package mipt.math.fuzzy.op.impl.smooth;

import mipt.math.fuzzy.*;

/**
 * @author Lee
 */
public class DMinus extends DAdd {

    public FuzzyNumber calc(FuzzyNumber a, FuzzyNumber b) {
        return super.calc(a, b.minusFuzzy());
    }
}
