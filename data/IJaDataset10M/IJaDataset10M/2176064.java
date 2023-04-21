package jtq.column;

import java.util.Date;
import java.sql.Timestamp;
import jtq.ICondition;

public interface TimeStampColumn extends IColumn<Timestamp, Date> {

    public ICondition greaterThan(Date pValue);

    public ICondition lessThan(Date pValue);

    public ICondition greaterThanOrEqual(Date pValue);

    public ICondition lessThanOrEqual(Date pValue);

    public ICondition greaterThan(AColumn<?, Date> pColumnB);

    public ICondition lessThan(AColumn<?, Date> pColumnB);

    public ICondition greaterThanOrEqual(AColumn<?, Date> pColumnB);

    public ICondition lessThanOrEqual(AColumn<?, Date> pColumnB);
}
