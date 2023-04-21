package mipt.math.fuzzy.op;

import mipt.math.fuzzy.FuzzyNumber;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public interface Operator {

    FuzzyNumber calc(FuzzyNumber a, FuzzyNumber b);
}
