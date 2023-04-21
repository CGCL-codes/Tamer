package mipt.math.fuzzy.discretizer;

import mipt.math.Number;
import mipt.math.fuzzy.FuzzyNumber;

/**
 * @author korotkov
 */
public interface FuzzyNumberDiscretizer {

    public void setPointsCount(int count);

    public int getPointsCount();

    public Number[] transform(FuzzyNumber number);

    public Number getElement(FuzzyNumber number, int index);
}
