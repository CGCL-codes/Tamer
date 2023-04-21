package com.daffodilwoods.daffodildb.server.sql99.dql.iterator.table;

import java.util.*;
import com.daffodilwoods.daffodildb.client.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;
import com.daffodilwoods.database.utility.P;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution._OrderCount;
import com.daffodilwoods.daffodildb.server.sql99.dql.execution.OrderCount;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import java.math.BigDecimal;
import com.daffodilwoods.daffodildb.utils.byteconverter.CCzufDpowfsufs;

/**
 * <p>Title: JoinIterator</p>
 * <p>Description: This Class is responsible for retrieval of records from a
 * query having INNER JOIN of two tables with seekable join condition.
 * A seekable join condition is condition in which two tables are seperated by
 * equals operator having one table on one side and other table on another side
 * of equal operator. For e.g. leftTable.leftTableColumn = rightTable.rightTableColumn.
 * This Class has two Iterators from which records are to be retrieved. Join
 * Condition is shifted to Right Iterator for better(optimized) execution of the
 * condition. The Purpose of this class is to align both underlying iterators at
 * valid record satisfying the given join condition.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Daffodil S/W Ltd.</p>
 * @author SelectTeam
 * @version 1.0
 */
public class JoinIterator extends BaseJoinIterator {

    /**
   * Variable representing join condition column of left Iterator.
   */
    _Reference[] leftColumnReferences;

    /**
   * A mapping of tables involved in the query and flag to denote LEFT or RIGHT
   * tables corresponding to join condition.
   */
    private Object[][] tableDetailsMapping;

    /**
   * Variable representing the number of key columns of left iterator. It assists
   * to move to a particular record for a non-group by query.
   */
    private int leftCount = -1;

    /**
   * Variable representing the number of grouped columns that uniquely specify
   * a record of left iterator. It assists to move to a particular record for a
   * group by query.
   */
    private int leftKeyCount = 0;

    /**
   * Variable representing the number of grouped columns that uniquely specify
   * a record of right iterator. It assists to move to a particular record for a
   * group by query.
   */
    private int rightKeyCount = 0;

    /**
   * Left side Table of join
   */
    private TableDetails[] leftTableDetails = null;

    /**
   * Right side Table of join
   */
    private TableDetails[] rightTableDetails = null;

    /**
   * Initializes the required variables for retrieval of records.
   * @param leftIterator table on left side of join condition
   * @param rightIterator table on right side of join condition
   * @param columnReferences join conditional columns of left iterator
   * @param hasRecordReferences0 hasRecord references
   */
    public JoinIterator(_Iterator leftIterator, _Iterator rightIterator, _Reference[] columnReferences, ColumnDetails[] hasRecordReferences0) {
        super(leftIterator, rightIterator, hasRecordReferences0);
        leftColumnReferences = columnReferences;
        if (leftColumnReferences == null || leftColumnReferences.length == 0) {
            Thread.dumpStack();
        }
        state = INVALIDSTATE;
    }

    /**
    * This method is responsible for retrieving the first record from the join
    * iterator satisfying the join condition. The Algo used is as follows:
    * 1. First record is retrieved from the left iterator, if there is no record,
    * return false.
    * 2. Get the values of conditional columns from left Iterator record and set
    * these conditional columns values to Right Iterator. First record is retrieved
    * from right Iterator which will have matching values satisfying the join condition.
    * 3. If there are no records in right iterator it means there are no records
    * in right iterator that can be matched with current record of left iterator
    * satisfying join condition. Hence, next record is retreived from the left
    * iterator, Goto step 2 to find some next record from left iterator that has
    * its matching row in right iterator satisfying join condition.
    * @return true if a record found, false otherwise.
    * @throws DException
    */
    public boolean first() throws DException {
        if (!leftIterator.first()) {
            state = AFTERLAST;
            return false;
        }
        getAndSetConditionValues();
        while (!rightIterator.first()) {
            if (!leftIterator.next()) {
                state = AFTERLAST;
                return false;
            }
            getAndSetConditionValues();
        }
        state = VALIDSTATE;
        return true;
    }

    /**
    * This method is responsible for retrieving the last record from the join
    * iterator satisfying the join condition. The Algo used is as follows:
    * 1. Last record is retrieved from the left iterator, if there is no record,
    * return false.
    * 2. Get the values of conditional columns from left Iterator record and set
    * these conditional columns values to Right Iterator. Last record is retrieved
    * from right Iterator which will have matching values satisfying the join condition.
    * 3. If there are no records in right iterator it means there are no records
    * in right iterator that can be matched with current record of left iterator
    * satisfying join condition. Hence, previous record is retreived from the left
    * iterator, Goto step 2 to find some previous record from left iterator that has
    * its matching row in right iterator satisfying join condition.
    * @return true if a record found, false otherwise.
    * @throws DException
    */
    public boolean last() throws DException {
        if (!leftIterator.last()) {
            state = BEFOREFIRST;
            return false;
        }
        getAndSetConditionValues();
        while (!rightIterator.last()) {
            if (!leftIterator.previous()) {
                state = BEFOREFIRST;
                return false;
            }
            getAndSetConditionValues();
        }
        state = VALIDSTATE;
        return true;
    }

    /**
    * This method is responsible for retrieving the next record from the join
    * iterator satisfying the join condition. The Algo used is as follows:
    * 1. If next record is retrieved from the right iterator. It means more
    * records are satisfied for the current row of left iteratror. Return true.
    * 2. Next record is retrieved from LEFT iterator, if no record found, return false.
    * 3. Get the values of conditional columns from left Iterator record and set
    * these conditional columns values to Right Iterator.
    * 4. First record is retrieved from right Iterator which will have matching
    * values satisfying the join condition, return true.
    * 5. If there are no records in right iterator it means there are no records
    * in right iterator that can be matched with current record of left iterator
    * satisfying join condition. Hence, next record is retreived from the left
    * iterator, Goto step 2 to find some next record from left iterator that has
    * its matching row in right iterator satisfying join condition.
    * @return true if a record found, false otherwise.
    * @throws DException
    */
    public boolean next() throws DException {
        switch(state) {
            case INVALIDSTATE:
                throw new DException("DSE4116", null);
            case AFTERLAST:
                return false;
            case BEFOREFIRST:
                return first();
            default:
                {
                    boolean left = rightIterator.next();
                    if (!left) {
                        do {
                            if (!leftIterator.next()) {
                                state = AFTERLAST;
                                return false;
                            }
                            getAndSetConditionValues();
                            left = rightIterator.first();
                        } while (!left);
                    }
                    state = VALIDSTATE;
                    return true;
                }
        }
    }

    /**
    * This method is responsible for retrieving previous record from the join
    * iterator satisfying the join condition. The Algo used is as follows:
    * 1. If previous record is retrieved from the right iterator,return true, since,
    * join condition is satisfied, it is previous record of Join Iterator.
    * 2. Previous record is retrieved from LEFT iterator, if no record found, return false.
    * 3. Get the values of conditional columns from left Iterator record and set
    * these conditional column values to Right Iterator.
    * 4. Last record is retrieved from right Iterator which will have matching
    * values satisfying the join condition, return true.
    * 5. If there are no records in right iterator it means there are no records
    * in right iterator that can be matched with current record of left iterator
    * satisfying join condition. Hence, previous record is retreived from the left
    * iterator and goto step 2 to find some next record from left iterator that has
    * its matching row in right iterator satisfying join condition.
    * @return true if a record found, false otherwise.
    * @throws DException
    */
    public boolean previous() throws DException {
        switch(state) {
            case INVALIDSTATE:
                throw new DException("DSE4117", null);
            case AFTERLAST:
                return last();
            case BEFOREFIRST:
                return false;
            default:
                {
                    boolean right = rightIterator.previous();
                    if (!right) {
                        do {
                            if (!leftIterator.previous()) {
                                state = BEFOREFIRST;
                                return false;
                            }
                            getAndSetConditionValues();
                            right = rightIterator.last();
                        } while (!right);
                    }
                    state = VALIDSTATE;
                    return true;
                }
        }
    }

    /**
    * This method is used to get the key of the record in bytes.
    * @usage to persist the temporary index keys in the database file
    * @return byte key of the record
    * @throws DException
    */
    public byte[] getByteKey() throws DException {
        byte[] leftKeys = leftIterator.getByteKey();
        byte[] rightKeys = rightIterator.getByteKey();
        byte[] resultantKeys = new byte[leftKeys.length + rightKeys.length + 4];
        short leftLength = (short) leftKeys.length;
        System.arraycopy(CCzufDpowfsufs.getBytes(leftLength), 0, resultantKeys, 0, 2);
        System.arraycopy(leftKeys, 0, resultantKeys, 2, leftKeys.length);
        System.arraycopy(rightKeys, 0, resultantKeys, leftKeys.length + 2, rightKeys.length);
        return resultantKeys;
    }

    /**
   * This method is used to move the iterator according to the passed byte key.
   * First Byte of the passed key represents the byte key length of the left
   * iterator. It extractes the byte keys of left and right iterators and moves
   * the underlying iterators acc. to extracted keys.
   * @param key byte key where to point the underlying iterator
   * @throws DException
   */
    public void moveByteKey(byte[] key) throws DException {
        if (leftKeyCount != 0 || rightKeyCount != 0) {
            moveAcctoGroupByteKey(key);
            return;
        }
        byte[] leftKeys = new byte[CCzufDpowfsufs.getShortValue(key, 0)];
        System.arraycopy(key, 2, leftKeys, 0, leftKeys.length);
        leftIterator.moveByteKey(leftKeys);
        getAndSetConditionValues();
        byte[] rightKeys = new byte[key.length - leftKeys.length - 2];
        System.arraycopy(key, leftKeys.length + 2, rightKeys, 0, rightKeys.length);
        rightIterator.moveByteKey(rightKeys);
        state = VALIDSTATE;
    }

    /**
   * THIS METHOD IS NOT USED
   * @param keys
   * @param leftTdCount
   * @param rightTdCount
   * @throws DException
   */
    private void moveAcctoGroupByteKeyold(byte[] keys, int leftTdCount, int rightTdCount) throws DException {
        int k = 0;
        int[] t = null;
        if (leftKeyCount == 0) leftIterator.first(); else {
            t = getLegthOfArray(keys, leftTdCount, rightTdCount, 0);
            byte[] leftKeys = new byte[t[0]];
            for (int j = 0; j < leftTdCount; j++) {
                leftKeys[k] = (byte) keys[k];
                System.arraycopy(keys, k + 1, leftKeys, k + 1, keys[0]);
                k += keys[k];
            }
            leftIterator.move(leftKeys);
        }
        getAndSetConditionValues();
        if (rightKeyCount == 0) rightIterator.first(); else {
            int rightKeysLength = 0;
            byte[] rightKeys = new byte[t[1]];
            for (int j = 0; j < rightTdCount; j++) {
                rightKeys[k] = (byte) keys[k];
                System.arraycopy(keys, 1, rightKeys, k + 1, keys[0]);
                k += keys[k];
            }
            rightIterator.move(rightKeys);
        }
        state = VALIDSTATE;
    }

    /**
     * This method is responsible to align both underlying iterator acc. to byte
     * key pasesed. It is used for a Join Iterator created for query having groupby
     * clause. If any of the iterator's key length is zero, its first record is
     * retrieved, since for this group, aggregates will be computed for all the
     * records belonging to that group. Otherwise, key for left/right iterators
     * are extracted from passed keys and move both the iterators to its corresponding keys.
     * @param keys represents key where to align underling iterators. keys
     * have the values of group by columns.
     * @throws DException
     */
    private void moveAcctoGroupByteKey(byte[] keys) throws DException {
        int index = 0;
        if (leftKeyCount == 0) leftIterator.first(); else {
            index = moveBytes(leftTableDetails, leftIterator, index, keys);
        }
        getAndSetConditionValues();
        if (rightKeyCount == 0) rightIterator.first(); else {
            index = moveBytes(rightTableDetails, rightIterator, index, keys);
        }
        state = VALIDSTATE;
    }

    /**
     * This method is used to move left or right iterator acc. to passed key.
     * @param td left or right side tables
     * @param iter left or right side iterators
     * @param index it is 0
     * @param keys where to move left or right iterator
     * @return total keys length including all the tables of left/right hand side
     * @throws DException
     */
    private int moveBytes(TableDetails[] td, _Iterator iter, int index, byte[] keys) throws DException {
        byte[] temp;
        int length = 0;
        for (int j = 0; j < td.length; j++) {
            length += CCzufDpowfsufs.getShortValue(keys, index);
            index = length + 2;
        }
        byte[] resultantKeys = new byte[length];
        System.arraycopy(keys, 0, resultantKeys, 0, resultantKeys.length);
        iter.move(resultantKeys);
        return index;
    }

    /**
     * THIS METHOD IS NOT USED
     * @param keys
     * @param lefTdCount
     * @param rightTdCount
     * @param index
     * @return
     */
    private int[] getLegthOfArray(byte[] keys, int lefTdCount, int rightTdCount, int index) {
        int leftKeysLength = 0, rightKeysLength = 0;
        for (int i = 0; i < lefTdCount; i++) {
            leftKeysLength += keys[index];
            index += keys[index];
        }
        int lengthOfLeftAndRight[] = new int[2];
        lengthOfLeftAndRight[0] = leftKeysLength + lefTdCount;
        for (int i = 0; i < rightTdCount; i++) {
            rightKeysLength += keys[index];
            index += keys[index];
        }
        lengthOfLeftAndRight[1] = rightKeysLength + rightTdCount;
        return lengthOfLeftAndRight;
    }

    /**
    * This method returns the key by concating the key of both underlying Iterator.
    * @return Array of Object having the key
    * @throws DException
    */
    public Object getKey() throws DException {
        ArrayList list = new ArrayList();
        Object[] leftKeys = (Object[]) leftIterator.getKey();
        this.leftCount = leftKeys.length;
        list.addAll(Arrays.asList(leftKeys));
        list.addAll(Arrays.asList((Object[]) rightIterator.getKey()));
        return list.toArray();
    }

    /**
    * This method is responsible to align both underlying iterator acc. to key
    * pasesed. For a Join Iterator created for query having group by clause,
    * transfers the call to moveAcctoGroupKey method.
    * For a non-group by Join Query, left iterator and right keys are extracted
    * from the passed key. First, left iterator is aligned to the left iterator
    * key and gets the conditional column values from left iterator record, and
    * sets these values in to rightIterator. After that, rightIterator is aligned
    * to key of right iterator.
    * @param keys key where to point to Join iterator
    * @throws DException
    */
    public void move(Object keys) throws DException {
        if (leftKeyCount != 0 || rightKeyCount != 0) {
            moveAcctoGroupKey((Object[]) keys);
            return;
        }
        Object[] leftKeys = new Object[leftCount];
        Object[] rightKeys = new Object[((Object[]) keys).length - leftCount];
        System.arraycopy(keys, 0, leftKeys, 0, leftCount);
        System.arraycopy(keys, leftCount, rightKeys, 0, rightKeys.length);
        leftIterator.move(leftKeys);
        getAndSetConditionValues();
        rightIterator.move(rightKeys);
        state = VALIDSTATE;
    }

    /**
    * This method is responsible to align both underlying iterator acc. to key
    * pasesed. It is used for a Join Iterator created for query having groupby
    * clause. If any of the iterator's key length is zero, its first record is
    * retrieved, since for this group, aggregates will be computed for all the
    * records belonging to that group. Otherwise, key for left/right iterators
    * are extracted from passed keys and move both the iterators to its corresponding keys.
    * @param keys represents key where to align, underling iterators. Also keys
    * have the values of group by columns.
    * @throws DException
    */
    private void moveAcctoGroupKey(Object[] keys) throws DException {
        if (leftKeyCount == 0) {
            leftIterator.first();
        } else {
            Object[] leftKeys = new Object[leftKeyCount];
            System.arraycopy(keys, 0, leftKeys, 0, leftKeyCount);
            leftIterator.move(leftKeys);
        }
        getAndSetConditionValues();
        if (rightKeyCount == 0) {
            rightIterator.first();
        } else {
            int rightLength = keys.length - leftKeyCount;
            Object[] rightKeys = new Object[rightLength];
            System.arraycopy(keys, leftKeyCount, rightKeys, 0, rightLength);
            rightIterator.move(rightKeys);
        }
        state = VALIDSTATE;
    }

    /**
    * This method is used to retrieve the values of passed references.
    * @param leftColumnReferences reference for which values are to be retrived.
    * Reference may be column or parameter.
    * @return NonShared FieldBases denoting the value of References. Non Shared
    * FieldBases are those for which BufferRange is not shared with some other
    * FieldBase.
    * @throws DException
    */
    public Object getColumnValues(_Reference[] leftColumnReferences) throws DException {
        int len = leftColumnReferences.length;
        Object[] values = new Object[len];
        for (int i = 0; i < len; i++) {
            values[i] = getColumnValues(leftColumnReferences[i]);
        }
        return values;
    }

    /**
    * This method is used to retrieve the values of passed column indexes.
    * @param parm1 column indexes for which values are to be retrived.
    * @return NonShared FieldBases denoting the value of References. Non Shared
    * FieldBases are those for which BufferRange is not shared with some other
    * FieldBase.
    * @throws DException
    */
    public Object getColumnValues(int[] parm1) throws DException {
        return getColumnValues();
    }

    /**
    * This method is used to retrieve all the values from the join query.
    * This method retrives all the values from the left iterator and values from
    * right iterator and merge these values to return the valid record of join iterator.
    * @return NonShared FieldBases denoting the values of all columns of join query.
    * Non Shared FieldBases are those for which BufferRange is not shared with
    * some other FieldBase.
    * @throws DException
    */
    public Object getColumnValues() throws DException {
        ArrayList aList = new ArrayList();
        Object obj = leftIterator.getColumnValues();
        if (obj != null) {
            aList.addAll(Arrays.asList((Object[]) obj));
        }
        obj = rightIterator.getColumnValues();
        if (obj != null) {
            aList.addAll(Arrays.asList((Object[]) obj));
        }
        return aList.toArray();
    }

    /**
    * This method is used to retrieve the value of passed reference.
    * It checks the table of column, if the column corresponds to LEFT table, values
    * are retrived from left iterator, otherwise from right table.
    * @param references reference for which value is to be retrived.
    * Reference may be column or parameter.
    * @return NonShared FieldBase denoting the value of Reference. Non Shared
    * FieldBases are those for which BufferRange is not shared with some other
    * FieldBase.
    * @throws DException
    */
    public Object getColumnValues(_Reference references) throws DException {
        TableDetails tDetails = ((ColumnDetails) references).getTable();
        int index = searchInMapping(tDetails);
        return index != -1 ? index == SimpleConstants.LEFT ? leftIterator.getColumnValues(references) : rightIterator.getColumnValues(references) : GeneralPurposeStaticClass.getColumnValuesFromBoth(leftIterator, rightIterator, references);
    }

    /**
    * This method return the shared FieldBase for the passed reference. By Shared
    * FieldBase we mean, BufferRange of FieldBase is shared with other FieldBase
    * objects.
    * @param references reference for which value is to be retrived
    * @return shared field base correspondition to passed column reference
    * @throws DException
    */
    public FieldBase field(_Reference references) throws DException {
        TableDetails tDetails = ((ColumnDetails) references).getTable();
        int index = searchInMapping(tDetails);
        return index != -1 ? index == SimpleConstants.LEFT ? leftIterator.field(references) : rightIterator.field(references) : GeneralPurposeStaticClass.getFieldValuesFromBoth(leftIterator, rightIterator, references);
    }

    /**
    * This method return the shared FieldBases for the passed references. By Shared
    * FieldBase we mean, BufferRange of FieldBase is shared with other FieldBase
    * objects.
    * @param leftColumnReferences reference for which values are to be retrived
    * @return shared field base correspondition to passed column reference
    * @throws DException
    */
    public FieldBase[] fields(_Reference[] leftColumnReferences) throws DException {
        int len = leftColumnReferences.length;
        FieldBase[] values = new FieldBase[len];
        for (int i = 0; i < len; i++) {
            values[i] = field(leftColumnReferences[i]);
        }
        return values;
    }

    /**
    * This method is responsible to check whether the passed tables
    * corresponds to left iterator or right iterator.
    * @param tDetails  table to check
    * @return table belongs to LEFT or RIGHT iterator.
    * @throws DException
    */
    private int searchInMapping(TableDetails tDetails) throws DException {
        for (int i = 0; i < tableDetailsMapping.length; i++) {
            if (tableDetailsMapping[i][0] == tDetails) {
                return ((Integer) tableDetailsMapping[i][1]).intValue();
            }
        }
        return -1;
    }

    /**
    * This method is used to align the right iterator based on the current value
    * of left iterator and join condition. It retrieves the conditional columns
    * values from left iterator and sets these values to Right Iterator.
    * ConditionalColumns of left Iterator will act as parameters in the join
    * condition placed in right iterator
    * E.g if condition is RightTable.rightColumn = LeftTable.leftColumn,
    * then it will be placed in rightIterator. The condition will act as
    * RightTable.rightColumn = ? and the value of '?' is provided by JoinIterator.
    * @throws DException
    */
    public void getAndSetConditionValues() throws DException {
        Object[] values = (Object[]) leftIterator.getColumnValues(leftColumnReferences);
        rightIterator.setConditionVariableValue(leftColumnReferences, values, 1);
    }

    /**
    * This method is responsible for setting the length of left and right
    * iterators's group key.
    * @param tableAndKeyCount a mapping of tables involved in the query and
    * length of their corresponding key.
    * @throws DException
    */
    public void setKeyCount(Object[][] tableAndKeyCount) throws DException {
        int[] counts = GeneralPurposeStaticClass.getLeftandRightKeyCount(leftIterator, rightIterator, tableAndKeyCount, tableDetailsMapping);
        leftKeyCount = counts[0];
        rightKeyCount = counts[1];
    }

    /**
    * This method returns all the tables involved in the join iterator.
    * It retrieves the tables involved in left iterator and right iterator
    * and merges the tables involved with flag denoting the LEFT or RIGHT
    * side.
    * @return resultant tables involved in the left and right iterator
    * @throws DException
    */
    public TableDetails[] getTableDetails() throws DException {
        TableDetails[] tableDetails1 = leftIterator.getTableDetails();
        TableDetails[] tableDetails2 = rightIterator.getTableDetails();
        int len = tableDetails1.length + tableDetails2.length;
        tableDetailsMapping = new Object[len][2];
        int i = 0;
        Integer flag = new Integer(SimpleConstants.LEFT);
        for (int j = 0; j < tableDetails1.length; j++) {
            tableDetailsMapping[i][0] = tableDetails1[j];
            tableDetailsMapping[i++][1] = flag;
        }
        flag = new Integer(SimpleConstants.RIGHT);
        for (int j = 0; j < tableDetails2.length; j++) {
            tableDetailsMapping[i][0] = tableDetails2[j];
            tableDetailsMapping[i++][1] = flag;
        }
        TableDetails[] resultantTableDetails = new TableDetails[len];
        System.arraycopy(tableDetails1, 0, resultantTableDetails, 0, tableDetails1.length);
        System.arraycopy(tableDetails2, 0, resultantTableDetails, tableDetails1.length, tableDetails2.length);
        return resultantTableDetails;
    }

    /**
    * This method is responsible to retrive the composite key column information
    * of Join iterator. This method sets the variable leftCount i.e. the number
    * of key columns in the left iterator, which is used in moving to a particular
    * record of Join Iterator.
    * @return Merged key column information of iterator
    * @throws DException
    */
    public _KeyColumnInformation[] getKeyColumnInformations() throws DException {
        _KeyColumnInformation[] leftKeyColumnInformation = leftIterator.getKeyColumnInformations();
        _KeyColumnInformation[] rightKeyColumnInformation = rightIterator.getKeyColumnInformations();
        this.leftCount = leftKeyColumnInformation.length;
        int len = leftKeyColumnInformation.length + rightKeyColumnInformation.length;
        _KeyColumnInformation[] resultantKeyColumnInformation = new _KeyColumnInformation[len];
        System.arraycopy(leftKeyColumnInformation, 0, resultantKeyColumnInformation, 0, leftKeyColumnInformation.length);
        System.arraycopy(rightKeyColumnInformation, 0, resultantKeyColumnInformation, leftKeyColumnInformation.length, rightKeyColumnInformation.length);
        return resultantKeyColumnInformation;
    }

    /**
    * This method is used to get the lowest level iterator of the passed column.
    * This is used for optimised retrieval of record from the maintained
    * mapping of iterators rather than navigating in iterators hierarchy.
    * @usage - top level resulset of select query.
    * @param column corresponding to which, iterator has to return
    * @return base table iterator corresponding to column passed
    * @throws DException
    */
    public _Iterator getBaseIterator(ColumnDetails column) throws DException {
        int len = tableDetailsMapping.length;
        for (int i = 0; i < len; i++) {
            if (tableDetailsMapping[i][0] == column.getTableDetails()) {
                return (tableDetailsMapping[i][1].hashCode() == SimpleConstants.LEFT) ? leftIterator.getBaseIterator(column) : rightIterator.getBaseIterator(column);
            }
        }
        _Iterator left = leftIterator.getBaseIterator(column);
        return left != null ? left : rightIterator.getBaseIterator(column);
    }

    /**
    * This method is responsible to display the executionPlan of a Select Query.
    * @return _ExecutionPlan
    * @throws DException
    */
    public _ExecutionPlan getExecutionPlan() throws DException {
        _ExecutionPlan plan = leftIterator.getExecutionPlan();
        _ExecutionPlan plan1 = rightIterator.getExecutionPlan();
        _ExecutionPlan cplans[] = new _ExecutionPlan[] { plan, plan1 };
        return new ExecutionPlan("JoinIterator", cplans, null, null, null);
    }

    /**
    * This method is responsible to display the iterators hierarchy of a Select Query.
    * @return ExecutionPlanForBrowser
    * @throws DException
    */
    public ExecutionPlanForBrowser getExecutionPlanForBrowser() throws DException {
        int length = 2;
        ExecutionPlanForBrowser cplans[] = new ExecutionPlanForBrowser[length];
        cplans[0] = leftIterator.getExecutionPlanForBrowser();
        cplans[1] = rightIterator.getExecutionPlanForBrowser();
        String refer = leftColumnReferences == null ? "" : "" + Arrays.asList(leftColumnReferences);
        refer = " Outer Reference  " + refer;
        return new ExecutionPlanForBrowser("Nested Join/Inner Join" + refer, "Join Iterator", cplans, null, null, null);
    }

    /**
    * The following methods of this class are used a intermediate in the
    * iterator hierarchy. These methods simply transfer the call to the
    * underlying iterator with the same arguments.
    */
    public Object[] getUniqueColumnReference() throws DException {
        return GeneralPurposeStaticClass.getUniqueColumnReference(leftIterator, rightIterator);
    }

    public boolean seek(Object indexKey) throws DException {
        return GeneralPurposeStaticClass.seek(indexKey, leftIterator, rightIterator);
    }

    /**
    * Returns the default order for this join.
    * @return _Order
    * @throws DException
    */
    public _Order getDefaultOrder() throws DException {
        return GeneralPurposeStaticClass.getJoinOrdered(leftIterator.getDefaultOrder(), rightIterator.getDefaultOrder());
    }

    public String toString() {
        return "JoinIterator[" + leftIterator + "]\n\n\n[" + rightIterator + "]";
    }
}
