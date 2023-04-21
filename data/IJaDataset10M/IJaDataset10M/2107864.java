package co.fxl.gui.filter.impl;

import java.util.Date;
import java.util.List;
import co.fxl.gui.filter.api.IFilterConstraints.IRange;

public interface IFilterConstraint {

    public interface INamedConstraint extends IFilterConstraint {

        String column();
    }

    public interface ISizeConstraint extends IFilterConstraint {

        int size();
    }

    interface IBooleanConstraint extends INamedConstraint {

        Boolean value();
    }

    interface IRelationConstraint extends INamedConstraint {

        List<Object> values();
    }

    interface IRangeConstraint<T> extends INamedConstraint {

        T lowerBound();

        T upperBound();
    }

    interface IDoubleRangeConstraint extends IRangeConstraint<Double>, IRange<Double> {
    }

    interface IDateRangeConstraint extends IRangeConstraint<Date>, IRange<Date> {
    }

    interface IIntegerRangeConstraint extends IRangeConstraint<Integer>, IRange<Integer> {
    }
}
