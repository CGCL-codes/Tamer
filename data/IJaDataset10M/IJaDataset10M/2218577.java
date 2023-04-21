package org.yccheok.jstock.analysis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author yccheok
 */
public class EqualityOperator extends AbstractOperator {

    public enum Equality {

        Equal, Greater, Lesser, GreaterOrEqual, LesserOrEqual
    }

    /** Creates a new instance of LogicalOperator */
    public EqualityOperator() {
        this.equality = Equality.Equal;
    }

    protected Object calculate() {
        Object object0 = inputs[0].getValue();
        Object object1 = inputs[1].getValue();
        try {
            Double d0 = Double.parseDouble(object0.toString());
            Double d1 = Double.parseDouble(object1.toString());
            int result = d0.compareTo(d1);
            switch(equality) {
                case Equal:
                    return new Boolean(result == 0);
                case Greater:
                    return new Boolean(result > 0);
                case Lesser:
                    return new Boolean(result < 0);
                case GreaterOrEqual:
                    return new Boolean(result >= 0);
                case LesserOrEqual:
                    return new Boolean(result <= 0);
                default:
                    assert (false);
            }
        } catch (NumberFormatException exp) {
            log.error("", exp);
        }
        return null;
    }

    public int getNumOfInputConnector() {
        return 2;
    }

    public Equality getEquality() {
        return equality;
    }

    public void setEquality(Equality equality) {
        Equality old = this.equality;
        this.equality = equality;
        if (old != this.equality) {
            this.firePropertyChange("attribute", old, this.equality);
        }
    }

    @Override
    public Class getInputClass(int index) {
        return Double.class;
    }

    @Override
    public Class getOutputClass(int index) {
        return Boolean.class;
    }

    private Equality equality;

    private static final Log log = LogFactory.getLog(EqualityOperator.class);
}
