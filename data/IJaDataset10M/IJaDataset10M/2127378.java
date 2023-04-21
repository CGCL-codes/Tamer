package com.daffodilwoods.daffodildb.server.sessionsystem.sessioncondition;

import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.database.general.SystemFields;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator._Iterator;
import com.daffodilwoods.daffodildb.server.sessionsystem.SystemFieldsCharacteristics;

/**
 *
 * <p>Description: Condition for newly inserted records </p>
 * <p>Company: Daffodil Software Ltd.</p>
 * @version 1.0
 */
public class InsertedCondition implements _SessionCondition {

    Object[] condition;

    private static int[] ST = { SystemFields.sessionId, SystemFields.transactionId };

    public InsertedCondition(Object presentConditionValues) {
        condition = new Object[2];
        condition[0] = presentConditionValues;
        condition[1] = (FieldBase) SystemFields.maxIntegerValue;
    }

    /**
 * Evaluates the condition given above.
 * @param values
 * @return
 * @throws DException
 */
    public boolean evaluate(_Iterator nonIndexedIterator) throws DException {
        Object[] valuesToCompare = (Object[]) nonIndexedIterator.getColumnValues(ST);
        return (StaticConditionHelper.compare(condition[0], valuesToCompare[0]) == 0) && (StaticConditionHelper.compare(condition[1], valuesToCompare[1]) == 0);
    }

    /**
     * refreshes the condition with new sessionId.
     * @param values
     * @throws DException
     */
    public void setParameterValues(Object[] values) throws DException {
        condition[0] = values[0];
    }

    public String toString() {
        return "[ INSERTED_CONDITION =>  sessionId = " + condition[0] + " and transactionId = maxIntegerValue ]";
    }
}
