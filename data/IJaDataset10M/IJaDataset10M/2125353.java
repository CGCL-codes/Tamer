package asi.elves.script;

import java.util.*;

/**
 * Value in each time is a function of the children's values.
 * <p>
 * There must be at least a child
 * value = f(f(f(f(x1, x2), x3), x4), x5)
 *
 * @author Andrea Odetti <mariofutire@gmail.com>
 */
public class HorizontalFold extends TimewiseFunction {

    private BinaryFunction m_func;

    /**
     * FoldLeft func timewise
     *
     * @param func binary function
     */
    public HorizontalFold(BinaryFunction func, List<TimeSeries> values) {
        super(values, MergerType.EXACT);
        m_func = func;
    }

    public double computeSingleValue(int datePos, Date date, List<Double> values) {
        if (values.isEmpty()) throw new RuntimeException("Cannot fold empty list " + this);
        double value = values.get(0);
        for (int i = 1; i < values.size(); ++i) {
            value = m_func.value(value, values.get(i));
        }
        return value;
    }
}
