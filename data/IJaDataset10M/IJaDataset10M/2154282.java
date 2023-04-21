package com.daffodilwoods.daffodildb.server.sessionsystem.sessioncondition;

import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.utils.byteconverter.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.daffodildb.server.sessionsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator._Iterator;

/**
 *
 * <p>Description: Condition for getting valid records from all the sessions weather committed or uncommitted </p>
 * <p>Company: Daffodil Software Ltd.</p>
 * @version 1.0
 */
public class RecordValidityCondition implements _SessionCondition {

    Object[] condition;

    public RecordValidityCondition() throws DException {
        condition = new Object[2];
        FieldBase bytesForMaxIntegerValue = (FieldBase) SystemFields.maxIntegerValue;
        condition[0] = bytesForMaxIntegerValue;
        condition[1] = bytesForMaxIntegerValue;
    }

    /**
  * Evaluates he condition above.
  * @param values
  * @return
  * @throws DException
  */
    public boolean evaluate(_Iterator nonIndexedIterator) throws DException {
        Object[] valuesToCompare = (Object[]) nonIndexedIterator.getColumnValues(SystemFieldsCharacteristics.ISIT);
        return (StaticConditionHelper.compare(condition[0], valuesToCompare[0]) == 0) && (StaticConditionHelper.compare(condition[1], valuesToCompare[1]) == 0);
    }

    public void setParameterValues(Object[] values) throws DException {
        throw new UnsupportedOperationException(" NOT POSSIBLE ");
    }

    public String toString() {
        return "[ RECORD_VALIDITY_CONDITION =>   invalidSessionId = maxIntegerValue and invalidTransactionId = maxIntegerValue ]";
    }
}
