package jtq.implementation.common.columns;

import java.util.Date;
import jtq.ICondition;
import jtq.column.AColumn;
import jtq.core.ATable;
import jtq.core.OperatorEnum;

abstract class ADateColumn<TYPE> extends AColumn<TYPE, Date> {

    ADateColumn(String pColumnName, ATable<?> pTable, boolean pIsPrimaryKey) {
        super(pColumnName, pTable, pIsPrimaryKey);
    }

    ADateColumn(String pColumnName, ATable<?> pTable, boolean pIsPrimaryKey, boolean pIsGenerated) {
        super(pColumnName, pTable, pIsPrimaryKey, pIsGenerated);
    }

    public ICondition greaterThan(Date pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.Greater_Than, pValue);
    }

    public ICondition lessThan(Date pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.Less_Than, pValue);
    }

    public ICondition greaterThanOrEqual(Date pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.Greater_Than_Or_Equal, pValue);
    }

    public ICondition lessThanOrEqual(Date pValue) {
        assertNotNull(pValue);
        return getConditionFactory().getValueCondition(this, OperatorEnum.Less_Than_Or_Equal, pValue);
    }

    public ICondition greaterThan(AColumn<?, Date> pColumnB) {
        if (pColumnB == null) throw new IllegalArgumentException("pColumnB can not be null");
        return getConditionFactory().columnCondition(this, OperatorEnum.Greater_Than, pColumnB);
    }

    public ICondition lessThan(AColumn<?, Date> pColumnB) {
        if (pColumnB == null) throw new IllegalArgumentException("pColumnB can not be null");
        return getConditionFactory().columnCondition(this, OperatorEnum.Less_Than, pColumnB);
    }

    public ICondition greaterThanOrEqual(AColumn<?, Date> pColumnB) {
        if (pColumnB == null) throw new IllegalArgumentException("pColumnB can not be null");
        return getConditionFactory().columnCondition(this, OperatorEnum.Greater_Than_Or_Equal, pColumnB);
    }

    public ICondition lessThanOrEqual(AColumn<?, Date> pColumnB) {
        if (pColumnB == null) throw new IllegalArgumentException("pColumnB can not be null");
        return getConditionFactory().columnCondition(this, OperatorEnum.Less_Than_Or_Equal, pColumnB);
    }
}
