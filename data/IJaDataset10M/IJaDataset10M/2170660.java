package com.daffodilwoods.daffodildb.server.sql99.dql.iterator.set;

import com.daffodilwoods.daffodildb.client.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.comparator.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.utility.P;

/**
 * <p>Title: UnionDistinctIterator </p>
 * <p>This class is used for retrieval of records from a query having UNION
 * DISTINCT set operator. UNION DISTINCT set operator returns all the records
 * from both of the underlying iterators except duplicate records. In UNION
 * DISTINCT Iterator, data of both underlying itertors is sorted, values of
 * selected columns from both iterators are compared and duplicate values are
 * skipped while navigating either of the iterator in any of the direction. </p>
 *
 * <p>This class is meant for merging and retrieval of the combined result for
 * <UNION DISTINCT Clause> set Operator . In UNION DISTINCT, All records from
 * both the iterator 'll be retrieved taking no duplicate record. A flag called
 * changeInDirectionOfNavigation is also used here in order to handle any reverse
 * iteration, i.e calling next after previous or calling previous after next method.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class UnionDistinctIterator extends UnionAllOrderedIterator {

    /**
   * The comparator that is used to compare one set of values of selected
   * columns from left iterator with another set of values from same iterator.
   */
    private SuperComparator leftComparator;

    /**
   * The comparator that is used to compare one set of values of selected
   * columns from right iterator with another set of values from same iterator.
   */
    private SuperComparator rightComparator;

    private int[] stateToSet = { FIRSTISCURRENT, FIRSTISCURRENT, BOTHHAVESAMEDATA, SECONDISCURRENT, SECONDISCURRENT };

    /**
    * A public constructor, that takes four arguments, two are left and right iterators, and
    * other two are left and right side selected column References. A state is also maintained,
    * that specifies the current status of the current level iterator(this).
    *
    * The State INVALIDSTATE means iterator is not aligned properly and needs to be aligned
    * before doing any further operations.
    *
    * The State VALIDSTATE means iterator is aligned properly and can be moved next()/ previous()
    * as well as values can also be retrieved
    *
    * The State BEFOREFIRST means, iterator has reached before the first record or there is no record in the result set.
    * Iterator is not aligned properly and needs to be aligned through calling first()/ last() before doing any further operations.
    *
    * The State AFTERLAST means, iterator has reached after the last record or there is no record in the result set.
    * Iterator is not aligned properly and needs to be aligned through calling first()/ last() before doing any further operations.
    *
    * The State FIRSTISCURRENT means, left iterator is the current iterator and is ready to provide the value for the current row.
    *
    * The State SECONDISCURRENT means, right iterator is the current iterator and is ready to provide the value for the current row.
    *
    * The State BOTHHAVESAMEDATA means, that both the left and right iterator have the same data
    *
    * @param leftIterator0
    * @param rightIterator0
    * @param leftColumnReferences0
    * @param rightColumnReferences0
    */
    public UnionDistinctIterator(_Iterator leftIterator, _Iterator rightIterator, _Reference[] leftColumnReferences, _Reference[] rightColumnReferences, SuperComparator comparator0, int[] appropriateDataTypes, int[] appropriateSizes, _Reference[] orderLeftCD0, _Reference[] orderRightCD0, SuperComparator leftComparator0, SuperComparator rightComparator0) throws DException {
        super(leftIterator, rightIterator, leftColumnReferences, rightColumnReferences, comparator0, appropriateDataTypes, appropriateSizes, orderLeftCD0, orderRightCD0);
        leftComparator = leftComparator0;
        rightComparator = rightComparator0;
    }

    /**
    * This method is responsible for retriving first record of Union Iterator.
    * First record is retrived from first and second iterator and a flag is
    * maintained that represents that whether first or second iterator is
    * CURRENT iterator (Iterator for which, values of selected columns is
    * smaller than that of other iterator). For equal values, state is set
    * to BOTHHAVESAMEDATA. If either of the iterator has no data, flag is
    * initialized to ONLYFIRSTHASDATA or ONLYSECONDHASDATA accordingly.
    *
    * DETAILED DOCUMENTATION
    *
    * This method aligns the iterator to the first valid record.
    * ALGO :::::
    * the direction flag is set to FORWARD
    * calls the first of both the iterators
    * if both first are true then
    *    compare the values
    *    the one with the smaller value is set as the current iterator.
    * if the only one of them is true then the state is set as ONLYFIRSTHASDATA or ONLYSECONDHASDATA according to which is current
    * if neither is true then the state is set to AFTERLAST.
    * it returns true if the the state is not AFTERLAST.
    *
    * @return true if either of the iterator has record, false, if none of the
    * iterator has records.
    * @throws com.daffodilwoods.database.resource.DException
    */
    public boolean first() throws com.daffodilwoods.database.resource.DException {
        direction = FORWARD;
        boolean leftFlag = leftIterator.first();
        boolean rightFlag = rightIterator.first();
        state = leftFlag ? rightFlag ? getState(compare(leftIterator.getColumnValues(orderLeftCD), rightIterator.getColumnValues(orderRightCD))) : ONLYFIRSTHAVEDATA : rightFlag ? ONLYSECONDHAVEDATA : AFTERLAST;
        return state != AFTERLAST;
    }

    /**
    * This method is responsible for retriving last record of Union Iterator.
    * Last record is retrived from first and second iterator and a flag is
    * maintained that represents that whether first or second iterator is
    * CURRENT iterator (Iterator for which, values of selected columns is
    * larger than that of other iterator). For equal values, state is set
    * to BOTHHAVESAMEDATA. If either of the iterator has no data, flag is
    * initialized to ONLYFIRSTHASDATA or ONLYSECONDHASDATA accordingly.
    *
    * DETAILED DOCUMENTATION
    *
    * This method aligns the iterator to the last valid record.
    * ALGO:::
    * it calls the last of both iterator
    * if both are true then
    *    compare the values the one with the
    *      larger value is set as current.
    * else if only one is true then the state is set as ONLYFIRSTHASDATA or ONLYRIGHTHASDATA
    * if neither is true then set the state as BEFOREFIRST
    * returns false if the state is BEFOREFIRST.
    * @return true if either of the iterator has record, false, if none of the
    * iterator has records.
    * @throws com.daffodilwoods.database.resource.DException
    */
    public boolean last() throws com.daffodilwoods.database.resource.DException {
        direction = BACKWARD;
        boolean leftFlag = leftIterator.last();
        boolean rightFlag = rightIterator.last();
        state = leftFlag ? rightFlag ? getState(-compare(leftIterator.getColumnValues(orderLeftCD), rightIterator.getColumnValues(orderRightCD))) : ONLYFIRSTHAVEDATA : rightFlag ? ONLYSECONDHAVEDATA : BEFOREFIRST;
        return state != BEFOREFIRST;
    }

    /**
    * This method is responsible for retriving next record of Union Iterator.
    * 1. If the state denotes that one of the iterator is current. Next distinct
    * record is retrived from CURRENT iterator. Goto 3.
    * 2. If the state is BOTHHAVESAMEDATA, Next distinct record is retrived from
    * both of the underlying iterators. Goto 3
    * 3. Compare the values of selected columns, Make one of iterator to CURRENT,
    * which has smaller values of selected columns as compared to that of other
    * iterator and update the state acc. to compare result.
    * 4. If state is ONLYFIRSTHASDATA or ONLYSECONDHASDATA it means only one of
    * the iterator has data that must be navigated in forward direction to
    * retrieve next distinct record.
    *
    * For forward fetching after backward fetching (if last or previous method
    * has called before call to next method), special handing has been done.
    * Both underlying iterators are navigated in such a way that produce state
    * of both underlying iterators as same as that of before call to backward
    * fetching method (last or previous method). In backward fetching, records
    * from the iterator having larger values of selected columns are returned
    * first, if any one of the iterator has no records, it's last record's values
    * are compared with that of the selected column values of the another iterator.
    * Larger Values must be returned first. Hence, the iterator having smaller
    * values is navigated back(next method called) to its previous state. Thus,
    * maintaining the previous state of both underlying iterators and returning
    * the appropriate selected columns values.
    *
    * DETAILED DOCUMENTATION
    *
    * This method aligns the iterator to the next distinct record.
    * Cases that involve mixed fetching i.e. next after previous are
    * handled differently as it is necessary for the state to be restored to the
    * state it was in.
    * ALGO:::
    * if the state is
    * INVALIDSTATE :
    *  throw exception
    * BEFOREFIRST :
    *  call first to align the iterator to the first record
    * AFTERLAST
    *  return false as there are no valid records left.
    * ONLYFIRSTHASDATA :
    *   if the direction of navigation is BACKWARD i.e. it is a case of mixed fetching.
    *          call the iteratePositiveAfterNegativeWhenOneHasData method
    *   else
    *          set the direction flag = FORWARD.
    *          call the moveForward method with the left iterator i.e. aligns the left iterator to the next distinct record.
    * ONLYSECONDHASDATA:
    *    same as the above case.
    * FIRSTISCURRENT :
    *    if the direction is backward i.e. mixed fetching
    *           then call the iteratePositiveAfterNegativeWhenOneIsCurrent method
    *    else
    *           call the iteratePositive method.
    * SECONDISCURRENT :
    *    same as the above case.
    * BOTHHAVESAMEDATA :
    *    if direction is backward then
    *       call the iteratePositiveAfterNegativeWhenBothHaveSameData method.
    *    else
    *       call the iteratePositiveWhenBoth method.
    *
    * @return true if either of the iterator has more distinct record in
    * same forward navigation, false, otherwise.
    * @throws com.daffodilwoods.database.resource.DException
    */
    public boolean next() throws com.daffodilwoods.database.resource.DException {
        switch(state) {
            case INVALIDSTATE:
                throw new DException("DSE4116", null);
            case BEFOREFIRST:
                return first();
            case AFTERLAST:
                return false;
            case ONLYFIRSTHAVEDATA:
                if (direction == BACKWARD) {
                    return iteratePositiveAfterNegativeWhenOneHasData(leftIterator, rightIterator, orderLeftCD, orderRightCD, 1);
                }
                direction = FORWARD;
                Object val = leftIterator.getColumnValues(orderLeftCD);
                boolean flag = moveForward(leftIterator, val, orderLeftCD, 1);
                state = flag ? state : AFTERLAST;
                return flag;
            case ONLYSECONDHAVEDATA:
                if (direction == BACKWARD) {
                    return iteratePositiveAfterNegativeWhenOneHasData(rightIterator, leftIterator, orderRightCD, orderLeftCD, -1);
                }
                direction = FORWARD;
                Object val1 = rightIterator.getColumnValues(orderRightCD);
                boolean flag1 = moveForward(rightIterator, val1, orderRightCD, -1);
                state = flag1 ? state : AFTERLAST;
                return flag1;
            case FIRSTISCURRENT:
                if (direction == BACKWARD) {
                    return iteratePositiveAfterNegativeWhenOneIsCurrent(leftIterator, rightIterator, orderLeftCD, orderRightCD, 1);
                }
                return iteratePositive(leftIterator, rightIterator, 1, orderLeftCD, orderRightCD);
            case SECONDISCURRENT:
                if (direction == BACKWARD) {
                    return iteratePositiveAfterNegativeWhenOneIsCurrent(rightIterator, leftIterator, orderRightCD, orderLeftCD, -1);
                }
                return iteratePositive(rightIterator, leftIterator, -1, orderRightCD, orderLeftCD);
            case BOTHHAVESAMEDATA:
                if (direction == BACKWARD) {
                    return iteratePositiveAfterNegativeWhenBothHaveSameData(leftIterator, rightIterator, orderLeftCD, orderRightCD, 1);
                }
                return iteratePositiveWhenBoth();
        }
        return false;
    }

    /**
    * This method is responsible to navigate one of the underlying iterator in
    * forward direction to next distinct record, when both of the underlying
    * iterators have data but one of the iterator is current. The purpose of
    * this method is to navigate current iterator in forward direction to next
    * distinct record. If there are no distinct records in current iterator,
    * state is set to ONLYFIRSTHASDATA or ONLYSECONDHASDATA accordingly.
    *
    * DETAILED DOCUMENTAION
    *
    * This method is called by the next method when the state is either FIRSTISCURRENT or SECONDISCURRENT and it is not the case of mixed fetching
    * Algo ::::::
    * First the direction flag is changed set to FORWARD
    * calls the moveForward with the currentIterator i.e. align the current iterator to the next distinct record
    * if we cannot find any more disticnt record then
    *     state is set as ONLYSECONDHASDATA or ONLYFIRSTHASDATA according to which is current
    * else
    *     we compare the values pointed to by both the iterators the one with the smaller value is set as current.
    * @throws DException
    * @usage assistance in next method
    * @param firstIterator current iterator
    * @param secondIterator other iterator which is not current
    * @param reverse {1 or -1} 1 when left iterator is current, -1 when
    * right iterator is current.
    * @param firstReferences given selected columns of current iterator
    * @param secondReferences given selected columns of other iterator
    * @return TRUE
    */
    private boolean iteratePositive(_Iterator currentIterator, _Iterator otherIterator, int reverse, _Reference[] currentReferences, _Reference[] otherReferences) throws DException {
        direction = FORWARD;
        Object toCompare = currentIterator.getColumnValues(currentReferences);
        boolean flag = moveForward(currentIterator, toCompare, currentReferences, reverse);
        if (!flag) {
            state = reverse == 1 ? ONLYSECONDHAVEDATA : ONLYFIRSTHAVEDATA;
            return true;
        }
        Object first = currentIterator.getColumnValues(currentReferences);
        Object second = otherIterator.getColumnValues(otherReferences);
        state = reverse == 1 ? getState(compare(first, second)) : getState(compare(second, first));
        return true;
    }

    /**
    * This method is responsible to navigate current underlying iterator in
    * forward direction to distinct record. Selected column values of next record
    * from current iterator are compared with that of other iterator. Iterator
    * with smaller values is made as Current Iterator. If there are no records
    * in current iterator, state is set to ONLYFIRSTHASDATA or ONLYSECONDHASDATA
    * accordingly.
    *
    * DETAILED DOCUMENTAION
    *
    * this method is called by next when the state is BOTHHAVESAMEDATA  and it not a case of
    * mixed fetching
    * ALGO::::
    * the direction flag is set as FORWARD
    * then we call the moveForward for both the iterators i.e. both are move to the next distinct record
    * if current iterator's moveForward is true
    *    if the moveForward of the other iterator is true then
    *       compare the data of the two iterators then
    *       if the data are the same then
    *          state is set to BOTHHAVESAMEDATA
    *       else if the iterator with the smaller value is set as current
    *    else
    *        state is set as ONLYFIRSTHAVEDATA
    * else i.e. the moveForward()  of the current iterator is not true
    *     if the moveForward() of the other iterator is true then
    *        state is set as ONLYSECONDHAVEDATA
    *     else ie. neither of the two is true then
    *        state is set as AFTERLAST
    *
    * @return
    * @throws DException
    */
    private boolean iteratePositiveWhenBoth() throws DException {
        direction = FORWARD;
        boolean flag1 = moveForward(leftIterator, leftIterator.getColumnValues(orderLeftCD), orderLeftCD, 1);
        boolean flag2 = moveForward(rightIterator, rightIterator.getColumnValues(orderRightCD), orderRightCD, -1);
        if (flag1) {
            if (flag2) {
                int cmp = compare(leftIterator.getColumnValues(orderLeftCD), rightIterator.getColumnValues(orderRightCD));
                state = cmp == 0 ? BOTHHAVESAMEDATA : cmp < 0 ? FIRSTISCURRENT : SECONDISCURRENT;
            } else {
                state = ONLYFIRSTHAVEDATA;
            }
        } else {
            if (flag2) {
                state = ONLYSECONDHAVEDATA;
            } else {
                state = AFTERLAST;
            }
        }
        return state != AFTERLAST;
    }

    /**
    * This method is called by next in the case of mixed fetching when next
    * method is called after previous method and state of iterator corresponds
    * to case when only one of the iterator has data. Other iterator's selected
    * column values of first record will be checked with that of next distinct
    * record of current iterator. If the other iterator's first record will have
    * smaller values these values must be retruned first, hence, it is made as
    * current iterator and current iterator is navigated revert back to previous
    * state, otherwise, state remains to ONEISCURRENT (FIRST or SECOND same as
    * before) and other iterator is set before first.
    *
    * DETAILED DOCUMENTATION
    *
    * this method is called by next in the case when only one of the iterator has data.
    * ALGO ::::
    * calls the moveForward method with the current iterator so the current iterator aligns itself to
    * next record
    * calls the first of the other iterator's first
    * if both are true then
    *    compares the values of the two iterators
    *         if both are equal then
    *             it sets the state = BOTHHAVESAMEDATA.
    *         else
    *              the iterator with the smaller value is set as current and the previous of the other iterator is called
    * if only the first of the other iterator is true then
    *    calls the last of the current iterator and the other iterator is set as current.
    * if neither is true then the state is set as AFTERLAST
    * @usage assistance in next method when fetching direction
    *        changes from backward to forward
    * @param currentIterator current iterator
    * @param otherIterator other iterator which is not current
    * @param currentReferences given selected columns of current iterator
    * @param otherReferences given selected columns of other iterator
    * @param reverse true if left iterator is has data, false when
    *        right iterator has data
    * @return FALSE if current iterator has no more records, true otherwise.
    * @throws DException
    */
    private boolean iteratePositiveAfterNegativeWhenOneHasData(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentNext = moveForward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherFirst = otherIterator.first();
        if (currentNext && otherFirst) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp > 0) {
                state = state == ONLYSECONDHAVEDATA ? FIRSTISCURRENT : state == ONLYFIRSTHAVEDATA ? SECONDISCURRENT : state;
                currentIterator.previous();
            } else {
                otherIterator.previous();
            }
        } else if (currentNext) {
        } else if (otherFirst) {
            currentIterator.last();
            state = state == ONLYSECONDHAVEDATA ? FIRSTISCURRENT : state == ONLYFIRSTHAVEDATA ? SECONDISCURRENT : state;
        } else {
            state = AFTERLAST;
            return false;
        }
        return true;
    }

    /**
    * This method is called by next method when the state is FIRSTISCURRENT and
    * previously fetching direction was backward (last or previous called).
    * Next distinct record is retrieved from both of the underlying iterator.
    * If both values are same, state is set to BOTHHAVESAMEDATA. Otherwise,
    * The previous of underlying iterator is called which has larger values of
    * selected columns, since larger values will be returned in the next call
    * of next method.
    *
    * DETAILED DOCUMENTATION
    *
    * this method is called by next () when the state is either FIRSTISCURRENT or  SECONDISCURRENT
    * ALGO::::
    * it calls the moveForward method in order to align the current iterator to the next disticnt record
    * it calls the moveForward method in order to align the other iterator to the next distinct record
    * if both are true then
    *    we compare the values of the records of the two iterators
    *    if the values are equal then
    *        state is set as BOTHHAVESAMEDATA.
    *    else if the current iterator has a larger value then
    *                the other iterator is set as current and the previous of the current iterator.
    *         else
    *                the state remains the same and the previous of the other iterator is called
    *    else if only the moveForward of the current is true
    *            then the last of the other iterator is called
    *    else if the moveForward of the other iterator is true then
    *            the last of the current is called and the other iterator is set as current.
    *    else i.e. neither of the moveForward is true then
    *            the state is AFTERLAST
    *  return true.
    * @usage assistance in next method when fetching direction
    *        changes from backward to forward
    * @param currentIterator current iterator
    * @param otherIterator other iterator which is not current
    * @param currentReferences given selected columns of current iterator
    * @param otherReferences given selected columns of other iterator
    * @param reverse true if left iterator is current iterator, false when
    *        right iterator is current iterator.
    * @return FALSE if both underlying iterator has no nore records, true otherwise.
    * @throws DException
    */
    private boolean iteratePositiveAfterNegativeWhenOneIsCurrent(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentNext = moveForward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherNext = moveForward(otherIterator, otherIterator.getColumnValues(otherReferences), otherReferences, reverse);
        if (currentNext && otherNext) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp > 0) {
                state = -state;
                currentIterator.previous();
            } else {
                otherIterator.previous();
            }
        } else if (currentNext) {
            otherIterator.last();
        } else if (otherNext) {
            currentIterator.last();
            state = -state;
        } else {
            state = AFTERLAST;
            return false;
        }
        return true;
    }

    /**
    * This method is responsible to navigate both underlying iterators in forward
    * direction to next distinct record. Selected column values of next records
    * are compared. Iterator with smaller values is made as Current Iterator.
    * If there are no records in current iterator, state is set to ONLYFIRSTHASDATA
    * or ONLYSECONDHASDATA accordingly. state is set to BOTHHAVESAMEDATA, if
    * selected column values of next distinct records of both iterators are same.
    *
    * DETAILED DOCUMENTAION
    *
    * this method is called by next when the state is BOTHHAVESAMEDATA
    * Algo :::::
    *  calls the moveForward for both the iterators
    *   if the moveForward for both the iterators are true then.
    *      we compare the records pointed to by the iterators
    *      if both are same then
    *         set state = BOTHHAVESAMEDATA
    *      else
    *         the iterator with the smaller value is set as current and the previous of the other iterator is called
    *   else if the current iterator's forward is true then
    *           we call the last of the other iterator
    *           and set the state as SECONDISCURRENT
    *        else if the other itertor's forward is true then
    *           we call the last of the current iterator
    *           and set the state as FIRSTISCURRENT
    *  else i.e. neither of the two is true then
    *      state is set as AFTERLAST
    * @param currentIterator
    * @param otherIterator
    * @param currentReferences
    * @param otherReferences
    * @param reverse
    * @return
    * @throws DException
    */
    private boolean iteratePositiveAfterNegativeWhenBothHaveSameData(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentNext = moveForward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherNext = moveForward(otherIterator, otherIterator.getColumnValues(otherReferences), otherReferences, reverse);
        if (currentNext && otherNext) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp < 0) {
                state = SECONDISCURRENT;
                currentIterator.previous();
            } else {
                state = FIRSTISCURRENT;
                otherIterator.previous();
            }
        } else if (currentNext) {
            otherIterator.last();
            state = SECONDISCURRENT;
        } else if (otherNext) {
            currentIterator.last();
            state = FIRSTISCURRENT;
        } else {
            state = AFTERLAST;
            return false;
        }
        return true;
    }

    /**
    * This method moves the iterator to the next distinct record.
    * it calls next of the current iterator until it finds a distinct record if it cannot it returns false.
    * @param iterator
    * @param value the value that is compared to the next records until a different record is found.
    * @param references
    * @param reverse  1 means left, -1 means right
    * @return
    * @throws DException
    */
    private boolean moveForward(_Iterator iterator, Object value, _Reference[] references, int reverse) throws DException {
        while (iterator.next()) {
            SuperComparator comparator = reverse == 1 ? leftComparator : rightComparator;
            if (comparator.compare(iterator.getColumnValues(references), value) != 0) {
                return true;
            }
        }
        return false;
    }

    /**
    * This method is responsible for retriving previous record of Union Iterator.
    * 1. If the state denotes that one of the iterator is current. Previous
    * distinct record is retrived from CURRENT iterator. Goto 3.
    * 2. If the state is BOTHHAVESAMEDATA, previous distinct record is retrived
    * from both of the underlying iterators. Goto 3
    * 3. Compare the values of selected columns, Make one of iterator to CURRENT,
    * which has larger values of selected columns as compared to that of other
    * iterator) and update the state acc. to new compare result.
    * 4. If state is ONLYFIRSTHASDATA or ONLYSECONDHASDATA it means only one of
    * the iterator has data that is navigated in backward direction to retrieve
    * previous distinct record.
    *
    * For backward fetching after forward fetching (if first or next method
    * has called before call to this previous method),special handing has been done.
    * Both underlying iterators are navigated in such a way to produce the same
    * state of both underlying iterators same as before call to forwards fetching
    * method (first or next method) . In forward fetching records from the
    * iterator having smaller values of selected columns are returned, if any one
    * of the iterator has no records, it's first record's values are compared with
    * that of the selected column values of the another iterator. Smaller Values
    * must be returned first. Hence, the iterator having larger values is navigated
    * back to its previous state till distinct selected column values are found.
    * Thus, maintaining the previous state of both underlying iterators and
    * returning the appropriate selected columns values.
    *
    * DETAILED DOCUMENTATION
    *
    * this method aligns the iterator to the previous distinct record.
    * if the state is :
    *  INVALIDSTATE : throw Exception
    *  BEFOREFIRST : return false
    *  AFTERLAST : call last()
    *  ONLYFIRSTHAVEDATA :
    *            if the direction is FORWARD i.e. it is a case of mixed fetching
    *                 then call the iterateNegativeAfterPositiveWhenOneHasData() method
    *            else
    *                 change the direction flag to BACKWARD
    *                 call the moveBackward() method with the leftIterator
    *                 if moveBackward()   is false then set state  = AFTERLAST.
    *  FIRSTISCURRENT :
    *            if the direction flag = FORWARD i.e. it is case of mixed fetching then
    *                call the iterateNegativeAfterPositiveWhenOneIsCurrent() method
    *            else
    *                call the iterateNegative() method
    * SECONDISCURRENT :
    *            same as the above case
    * BOTHHAVESAMEDATA :
    *            if the direction flag = FORWARD
    *               call the  iterateNegativeAfterPositiveWhenBothHaveSameData()
    *            else
    *               call the iterateNegativeWhenBoth()
    *
    * @return true if either of the iterator has more distinct record in
    * same backward navigation, false, otherwise.
    * @throws com.daffodilwoods.database.resource.DException
    */
    public boolean previous() throws com.daffodilwoods.database.resource.DException {
        switch(state) {
            case INVALIDSTATE:
                throw new DException("DSE4117", null);
            case BEFOREFIRST:
                return false;
            case AFTERLAST:
                return last();
            case ONLYFIRSTHAVEDATA:
                if (direction == FORWARD) {
                    return iterateNegativeAfterPositiveWhenOneHasData(leftIterator, rightIterator, orderLeftCD, orderRightCD, 1);
                }
                direction = BACKWARD;
                Object val = leftIterator.getColumnValues(orderLeftCD);
                boolean flag = moveBackward(leftIterator, val, orderLeftCD, 1);
                state = flag ? state : BEFOREFIRST;
                return flag;
            case ONLYSECONDHAVEDATA:
                if (direction == FORWARD) {
                    return iterateNegativeAfterPositiveWhenOneHasData(rightIterator, leftIterator, orderRightCD, orderLeftCD, -1);
                }
                direction = BACKWARD;
                Object val1 = rightIterator.getColumnValues(orderRightCD);
                boolean flag1 = moveBackward(rightIterator, val1, orderRightCD, -1);
                state = flag1 ? state : BEFOREFIRST;
                return flag1;
            case FIRSTISCURRENT:
                if (direction == FORWARD) {
                    return iterateNegativeAfterPositiveWhenOneIsCurrent(leftIterator, rightIterator, orderLeftCD, orderRightCD, 1);
                }
                return iterateNegative(leftIterator, rightIterator, 1, orderLeftCD, orderRightCD);
            case SECONDISCURRENT:
                if (direction == FORWARD) {
                    return iterateNegativeAfterPositiveWhenOneIsCurrent(rightIterator, leftIterator, orderRightCD, orderLeftCD, -1);
                }
                return iterateNegative(rightIterator, leftIterator, -1, orderRightCD, orderLeftCD);
            case BOTHHAVESAMEDATA:
                if (direction == FORWARD) {
                    return iterateNegativeAfterPositiveWhenBothHaveSameData(rightIterator, leftIterator, orderRightCD, orderLeftCD, -1);
                }
                return iterateNegativeWhenBoth();
        }
        return false;
    }

    /**
    * This method is responsible to navigate one of the underlying iterator in
    * backward direction to previous distinct record, when both of the underlying
    * iterators have data but one of the iterator is current. The purpose of
    * this method is to navigate current iterator in backward direction to previous
    * distinct record. If there are no distinct records in current iterator,
    * state is set to ONLYFIRSTHASDATA or ONLYSECONDHASDATA accordingly.
    *
    * DETAILED DOCUMENTAION
    *
    * this method is called by the previous() when the state is FIRSTISCURRENT or SECONDISCURRENT and it is not a mixed fetching case
    * ALGO :::::::::
    * the direction flag is changed to BACKWARD
    * we call the moveBackward() for the first iterator this aligns first iterator to the previous distinct record
    * if the moveBackward() is false
    *     state is set as the other iterator has data i.e either ONLYFIRSTHAVEDATA or ONLYSECONDHAVEDATA according to which is current
    * else
    *     we compare the records
    *     the state is set accordingly that is the one with the larger value is set as current
    *     if the data are same then the state is set = BOTHHAVESAMEDATA
    * @throws DException
    * @usage assistance in previous method
    * @param firstIterator current iterator
    * @param secondIterator other iterator which is not current
    * @param reverse {1 or -1} 1 when left iterator is current, -1 when
    * right iterator is current.
    * @param firstReferences given selected columns of current iterator
    * @param secondReferences given selected columns of other iterator
    * @return TRUE
    */
    private boolean iterateNegative(_Iterator firstIterator, _Iterator secondIterator, int reverse, _Reference[] firstReferences, _Reference[] secondReferences) throws DException {
        direction = BACKWARD;
        Object toCompare = firstIterator.getColumnValues(firstReferences);
        int cmp = -1;
        boolean flag = moveBackward(firstIterator, toCompare, firstReferences, reverse);
        if (!flag) {
            state = reverse == 1 ? ONLYSECONDHAVEDATA : ONLYFIRSTHAVEDATA;
            return true;
        }
        Object first = firstIterator.getColumnValues(firstReferences);
        Object second = secondIterator.getColumnValues(secondReferences);
        state = reverse == 1 ? getState(compare(second, first)) : getState(compare(first, second));
        return true;
    }

    /**
    * This method is responsible to navigate current underlying iterator in
    * backward direction to distinct record. Selected column values of previous
    * record of current iterator are compared with that of other iterator. Iterator
    * with larger values is made as Current Iterator. If there are no records
    * in current iterator, state is set to ONLYFIRSTHASDATA or ONLYSECONDHASDATA
    * accordingly.
    *
    * DETAILED DOCUMENTAION
    *
    * this method is called by the previous() method when the state is BOTHHAVESAMEDATA and it is not a case of mixed fetching
    * ALGO::::::::::
    * direction flag is reset to BACKWARD
    * moveBackward() is called for both iterators
    * if moveBackward() for the current iterator is true then
    *     if the moveBackward() for the other iterator is true then
    *         compare the values of the iterators
    *         if both are same then
    *            state = BOTHHAVESAMEDATA
    *         else
    *            the iterator with the larger value is set as current
    *     else i.e. the moveBackward() of the other itertor is false
    *          state is set to ONLYFIRSTHAVEDATA
    * else i.e. the moveBackward() of the current iterator is false
    *      if the moveBackward() of the other iterator is true
    *          state is set ONLYSECONDHAVEDATA
    *      else i.e. neither of the two is true
    *          state is set to BEFOREFIRST
    *
    * @return it returns true if the state is not BEFOREFIRST
    * @throws DException
    */
    private boolean iterateNegativeWhenBoth() throws DException {
        direction = BACKWARD;
        boolean flag1 = moveBackward(leftIterator, leftIterator.getColumnValues(orderLeftCD), orderLeftCD, 1);
        boolean flag2 = moveBackward(rightIterator, rightIterator.getColumnValues(orderRightCD), orderRightCD, -1);
        if (flag1) {
            if (flag2) {
                int cmp = compare(leftIterator.getColumnValues(orderLeftCD), rightIterator.getColumnValues(orderRightCD));
                state = cmp == 0 ? BOTHHAVESAMEDATA : cmp > 0 ? FIRSTISCURRENT : SECONDISCURRENT;
            } else {
                state = ONLYFIRSTHAVEDATA;
            }
        } else {
            if (flag2) {
                state = ONLYSECONDHAVEDATA;
            } else {
                state = BEFOREFIRST;
            }
        }
        return state != BEFOREFIRST;
    }

    /**
    * This method is called by previous in the case of mixed fetching when
    * previous method is called after next method and state of iterator corresponds
    * to case when only one of the iterator has data. Other iterator's selected
    * column values of last record will be checked with that of previous distinct
    * record of current iterator. If the other iterator's last record will have
    * larger values, these values must be retruned first, hence, it is made as
    * current iterator and current iterator is navigated revert back to next
    * state, otherwise, state remains to ONEISCURRENT (FIRST or SECOND same as
    * before) and other iterator is set after last.
    *
    * DETAILED DOCUMENTATION
    *
    * This method is called by the previous() method when the state is ONLYFIRSTHAVEDATA  and it is a case of mixed fetching
    * ALGO ::::
    *   moveBackward() is called for the current iterator this aligns the current iterator to the previous distinct record
    *   last of the other iterator is called to align it to it's last record
    *   if both moveBackward()  and last are true then
    *      compare the two values
    *      if both are same then
    *         set state = BOTHHAVESAMEDATA
    *      else
    *         the iterator with the larger value is set as current
    *         and the other iterator's next is called
    *    else if the other iterator's last is true and moveBackward() of the current iterator is false
    *            current iterator' first() is called and the other iterator is set as current
    *    else i.e. neither is true
    *        then  the state  is set as BEFOREFIRST
    * @usage assistance in previous method when fetching direction
    *        changes from forward to backward
    * @param currentIterator current iterator
    * @param otherIterator other iterator which is not current
    * @param currentReferences given selected columns of current iterator
    * @param otherReferences given selected columns of other iterator
    * @param reverse true if left iterator is has data, false when
    *        right iterator has data
    * @return FALSE if current iterator has no more records, true otherwise.
    * @throws DException
    */
    private boolean iterateNegativeAfterPositiveWhenOneHasData(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentPrevious = moveBackward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherLast = otherIterator.last();
        if (currentPrevious && otherLast) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp < 0) {
                state = state == ONLYSECONDHAVEDATA ? FIRSTISCURRENT : state == ONLYFIRSTHAVEDATA ? SECONDISCURRENT : state;
                currentIterator.next();
            } else {
                otherIterator.next();
            }
        } else if (currentPrevious) {
        } else if (otherLast) {
            currentIterator.first();
            state = state == ONLYSECONDHAVEDATA ? FIRSTISCURRENT : state == ONLYFIRSTHAVEDATA ? SECONDISCURRENT : state;
        } else {
            state = BEFOREFIRST;
            return false;
        }
        return true;
    }

    /**
    * This method is called by previous when the state is FIRSTISCURRENT and
    * previously fetching direction was forward (first or next called).
    * Previous distinct record is retrieved from both of the underlying iterator.
    * If both values are same, state is set to BOTHHAVESAMEDATA. Otherwise,
    * The next of underlying iterator is called which has smaller values of
    * selected columns, since smaller values will be returned in the next call
    * of previous method.
    *
    * DETAILED DOCUMENTATION
    *
    * This method is called by previous() when the state is FIRSTISCURRENT or SECONDISCURRENT and it is a case of mixed fetching
    * ALGO ::::
    *   moveBackward() is called for the current iterator and the other iterator
    *   if both the moveBackward() are true then
    *      the two values are compared
    *      if the two values are same then
    *         state is set equal to BOTHHAVESAMEDATA
    *      else
    *         the iterator with the larger value is set as current and the next of the other iterator is called.
    *   else if only the moveBackward() of the current iterator is true then
    *            we call the other iterator's first()
    *        else if only the moveBackward() of the other iterator is true then
    *            we call the current iterator's first() and the other iterator is set as current
    *   else i.e. neither of the two are true then
    *       the state is set as BEFOREFIRST

    * @usage assistance in previous method when fetching direction
    *        changes from forward to backward
    * @param currentIterator current iterator
    * @param otherIterator other iterator which is not current
    * @param currentReferences given selected columns of current iterator
    * @param otherReferences given selected columns of other iterator
    * @param reverse true if left iterator is current iterator, false when
    *        right iterator is current iterator.
    * @return FALSE if both underlying iterator has no nore records, true otherwise.
    */
    private boolean iterateNegativeAfterPositiveWhenOneIsCurrent(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentPrevious = moveBackward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherPrevious = moveBackward(otherIterator, otherIterator.getColumnValues(otherReferences), otherReferences, reverse);
        if (currentPrevious && otherPrevious) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp < 0) {
                state = -state;
                currentIterator.next();
            } else {
                otherIterator.next();
            }
        } else if (currentPrevious) {
            otherIterator.first();
        } else if (otherPrevious) {
            currentIterator.first();
            state = -state;
        } else {
            state = BEFOREFIRST;
            return false;
        }
        return true;
    }

    /**
    * This method is responsible to navigate both underlying iterators in backward
    * direction to previous distinct record. Selected column values of previous
    * records from both iterators are compared. Iterator with larger values is
    * made as Current Iterator. If there are no records in current iterator,
    * state is set to ONLYFIRSTHASDATA or ONLYSECONDHASDATA accordingly. state
    * is set to BOTHHAVESAMEDATA, if selected column values of previous distinct
    * records of both iterators are same.
    *
    * DETAILED DOCUMENTAION
    *
    * this method is called by the previous() method when the state is BOTHHAVESAMEDATA and it is a case of mixed fetching
    * ALGO::::::::
    * moveBackward() is called for both the iterators
    * if both moveBackward() return true then
    *       we compare the values of the iterators
    *       if both have same data then
    *          state is set = BOTHHAVESAMEDATA
    *       else
    *          the iterator with the larger value is set as current and the next of the other iterator is called
    * if only the moveBackward() of the current iterator is true then
    *       we call the other iterator's first
    *       and the state is set as FIRSTISCURRENT
    * else if only the moveBackward() of the other iterator is true
    *       current iterator's first is called and the other iterator is set as current
    * else i.e. neither of the two is true
    *       the state is set to BEFOREFIRST
    *       false is returned
    * @param currentIterator
    * @param otherIterator
    * @param currentReferences
    * @param otherReferences
    * @param reverse
    * @return
    * @throws DException
    */
    private boolean iterateNegativeAfterPositiveWhenBothHaveSameData(_Iterator currentIterator, _Iterator otherIterator, _Reference[] currentReferences, _Reference[] otherReferences, int reverse) throws DException {
        boolean currentPrevious = moveBackward(currentIterator, currentIterator.getColumnValues(currentReferences), currentReferences, reverse);
        boolean otherPrevious = moveBackward(otherIterator, otherIterator.getColumnValues(otherReferences), otherReferences, reverse);
        if (currentPrevious && otherPrevious) {
            Object currentObject = currentIterator.getColumnValues(currentReferences);
            Object otherObject = otherIterator.getColumnValues(otherReferences);
            int cmp = compare(currentObject, otherObject);
            if (cmp == 0) {
                state = BOTHHAVESAMEDATA;
            } else if (cmp < 0) {
                state = SECONDISCURRENT;
                currentIterator.next();
            } else {
                state = FIRSTISCURRENT;
                otherIterator.next();
            }
        } else if (currentPrevious) {
            otherIterator.first();
            state = FIRSTISCURRENT;
        } else if (otherPrevious) {
            currentIterator.first();
            state = SECONDISCURRENT;
        } else {
            state = BEFOREFIRST;
            return false;
        }
        return true;
    }

    /**
    * this method is called by the previous() method
    * this method aligns the iterator to the previous disticnt record
    * ALGO:::::
    * it calls the previous of the iterator until a different record is found
    * @param iterator
    * @param value
    * @param references
    * @param reverse
    * @return
    * @throws DException
    */
    private boolean moveBackward(_Iterator iterator, Object value, _Reference[] references, int reverse) throws DException {
        while (iterator.previous()) {
            SuperComparator comparator = reverse == 1 ? leftComparator : rightComparator;
            if (comparator.compare(iterator.getColumnValues(references), value) != 0) {
                return true;
            }
        }
        return false;
    }

    /**
    * This method is used to get the lowest level iterator.
    * @param column column corresponding to which iterator is to return.
    * @return same iterator.
    * @throws DException
    */
    public _Iterator getBaseIterator(ColumnDetails column) throws com.daffodilwoods.database.resource.DException {
        return this;
    }

    /**
    * This method is responsible to display the executionPlan of a Select Query.
    * @return _ExecutionPlan
    * @throws DException
    */
    public _ExecutionPlan getExecutionPlan() throws DException {
        _ExecutionPlan cplans[] = new _ExecutionPlan[2];
        cplans[0] = leftIterator.getExecutionPlan();
        cplans[1] = rightIterator.getExecutionPlan();
        return new ExecutionPlan("UnionDistinctIterator", cplans, null, null, null);
    }

    /**
    * This method is responsible to display the iterators hierarchy of a Select Query.
    * @return ExecutionPlanForBrowser
    * @throws DException
    */
    public ExecutionPlanForBrowser getExecutionPlanForBrowser() throws DException {
        ExecutionPlanForBrowser cplans[] = new ExecutionPlanForBrowser[2];
        cplans[0] = leftIterator.getExecutionPlanForBrowser();
        cplans[1] = rightIterator.getExecutionPlanForBrowser();
        return new ExecutionPlanForBrowser("Union Distinct", "Union Distinct Iterator", cplans, null, null, null);
    }

    public String toString() {
        return "UnionDistinctIterator [" + leftIterator + "] [" + rightIterator + "] ";
    }

    private int getState(int compareResult) {
        return stateToSet[compareResult + 2];
    }
}
