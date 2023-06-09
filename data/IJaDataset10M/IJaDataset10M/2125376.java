package com.limegroup.gnutella.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import junit.framework.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.limegroup.gnutella.downloader.Interval;

/**
 * Unit tests for IntervalSet
 */
public class IntervalSetTest extends BaseTestCase {

    private static final Log LOG = LogFactory.getLog(IntervalSetTest.class);

    private static IntervalSet iSet = null;

    private static Interval interval = null;

    private static List list = null;

    private static Iterator iter = null;

    public IntervalSetTest(String name) {
        super(name);
    }

    public static Test suite() {
        return buildTestSuite(IntervalSetTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testGetNumberOfIntervals() {
        iSet = IntervalSet.createSingletonSet(0, 4);
        iSet.add(new Interval(17));
        iSet.add(new Interval(20));
        iSet.add(new Interval(28));
        iSet.add(new Interval(31, 32));
        assertEquals(5, iSet.getNumberOfIntervals());
        iSet = new IntervalSet();
        assertEquals(0, iSet.getNumberOfIntervals());
        iSet.add(new Interval(17, 192000));
        assertEquals(1, iSet.getNumberOfIntervals());
        iSet.delete(new Interval(129));
        assertEquals(2, iSet.getNumberOfIntervals());
    }

    public void testCreateSingletonSet() {
        iSet = IntervalSet.createSingletonSet(17, 28417);
        assertEquals(1, iSet.getNumberOfIntervals());
        assertEquals(new Interval(17, 28417), iSet.getFirst());
    }

    public void testBasic() throws Exception {
        iSet = new IntervalSet();
        interval = new Interval(40, 45);
        iSet.add(interval);
        assertEquals("add method broken", 1, iSet.getNumberOfIntervals());
        assertEquals("getSize() broken", 6, iSet.getSize());
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 39, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testLowerBoundaryColation() throws Exception {
        interval = new Interval(35, 39);
        iSet.add(interval);
        assertEquals("lower boundry colating failed", 1, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals("lower boundry colating failed", 45, interval.high);
        assertEquals("lower boundry colating failed", 35, interval.low);
        assertEquals("getSize() broken", 11, iSet.getSize());
    }

    public void testLowerOverlapColation() throws Exception {
        interval = new Interval(30, 37);
        iSet.add(interval);
        assertEquals("lower overlap colating failed", 1, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals("lower boundry colating failed", 45, interval.high);
        assertEquals("lower boundry colating failed", 30, interval.low);
    }

    public void testLowerNonOverlap() throws Exception {
        interval = new Interval(20, 25);
        iSet.add(interval);
        assertEquals("lower non-overlap add failed", 2, iSet.getNumberOfIntervals());
        assertEquals("getSize() broken", 22, iSet.getSize());
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 19, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 26, interval.low);
        assertEquals("getNeededInterval broken", 29, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testUpperNonOverlap() throws Exception {
        interval = new Interval(50, 60);
        iSet.add(interval);
        assertEquals("upper non-overlap add failed", 3, iSet.getNumberOfIntervals());
        assertEquals("getSize() broken", 33, iSet.getSize());
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 19, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 26, interval.low);
        assertEquals("getNeededInterval broken", 29, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 49, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 61, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testGetOverlapIntervalsMidRange() throws Exception {
        interval = new Interval(23, 32);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 2, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 23, interval.low);
        assertEquals("getOverlapIntervals broken", 25, interval.high);
        interval = (Interval) list.get(1);
        assertEquals("getOverlapIntervals broken", 30, interval.low);
        assertEquals("getOverlapIntervals broken", 32, interval.high);
    }

    public void testGetOverlapIntervalsBoundary() throws Exception {
        interval = new Interval(25, 30);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 2, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 25, interval.low);
        assertEquals("getOverlapIntervals broken", 25, interval.high);
        interval = (Interval) list.get(1);
        assertEquals("getOverlapIntervals broken", 30, interval.low);
        assertEquals("getOverlapIntervals broken", 30, interval.high);
    }

    public void testGetOverlapIntervalsLowRange() throws Exception {
        interval = new Interval(16, 23);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 1, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 20, interval.low);
        assertEquals("getOverlapIntervals broken", 23, interval.high);
    }

    public void testGetOverlapIntervalsLowBoundary() throws Exception {
        interval = new Interval(16, 20);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 1, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 20, interval.low);
        assertEquals("getOverlapIntervals broken", 20, interval.high);
    }

    public void testGetOverlapIntervalsHighRange() throws Exception {
        interval = new Interval(23, 29);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 1, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 23, interval.low);
        assertEquals("getOverlapIntervals broken", 25, interval.high);
    }

    public void testGetOverlapIntervalsHighBoundary() throws Exception {
        interval = new Interval(25, 29);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 1, list.size());
        interval = (Interval) list.get(0);
        assertEquals("getOverlapIntervals broken", 25, interval.low);
        assertEquals("getOverlapIntervals broken", 25, interval.high);
    }

    public void testGetOverlapIntervalsNoRange() throws Exception {
        interval = new Interval(13, 19);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("reported false overlap", 0, list.size());
        interval = new Interval(26, 29);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("reported false overlap", 0, list.size());
    }

    public void testGetOverlapIntervalsMidAllRanges() throws Exception {
        interval = new Interval(23, 53);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 3, list.size());
        interval = (Interval) list.get(0);
        assertEquals(23, interval.low);
        assertEquals(25, interval.high);
        interval = (Interval) list.get(1);
        assertEquals(30, interval.low);
        assertEquals(45, interval.high);
        interval = (Interval) list.get(2);
        assertEquals(50, interval.low);
        assertEquals(53, interval.high);
    }

    public void testGetOverlapIntervalsAllRanges() throws Exception {
        interval = new Interval(16, 65);
        list = iSet.getOverlapIntervals(interval);
        assertEquals("getOverlapIntervals broken", 3, list.size());
        interval = (Interval) list.get(0);
        assertEquals(20, interval.low);
        assertEquals(25, interval.high);
        interval = (Interval) list.get(1);
        assertEquals(30, interval.low);
        assertEquals(45, interval.high);
        interval = (Interval) list.get(2);
        assertEquals(50, interval.low);
        assertEquals(60, interval.high);
    }

    public void testAddHighColation() throws Exception {
        iSet = IntervalSet.createSingletonSet(20, 25);
        iSet.add(new Interval(30, 45));
        iSet.add(new Interval(50, 60));
        interval = new Interval(54, 70);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(2);
        assertEquals(50, interval.low);
        assertEquals(70, interval.high);
        assertEquals("getSize() broken", 43, iSet.getSize());
    }

    public void testAddHighBoundaryColation() throws Exception {
        iSet = IntervalSet.createSingletonSet(20, 25);
        iSet.add(new Interval(30, 45));
        iSet.add(new Interval(50, 70));
        interval = new Interval(71, 75);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(2);
        assertEquals(50, interval.low);
        assertEquals(75, interval.high);
        assertEquals("getSize() broken", 48, iSet.getSize());
        interval = new Interval(75, 80);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(2);
        assertEquals(50, interval.low);
        assertEquals(80, interval.high);
    }

    public void testAddLowBoundaryColation() {
        interval = new Interval(15, 20);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals(15, interval.low);
        assertEquals(25, interval.high);
        assertEquals("getSize() broken", 58, iSet.getSize());
    }

    public void testGetNeededIntervals() {
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 14, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 26, interval.low);
        assertEquals("getNeededInterval broken", 29, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 49, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 81, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testAddOverlappingInterval() {
        interval = new Interval(49, 81);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(2);
        assertEquals(49, interval.low);
        assertEquals(81, interval.high);
        assertEquals("getSize() broken", 60, iSet.getSize());
    }

    public void testAddInternalInterval() {
        interval = new Interval(55, 60);
        iSet.add(interval);
        assertEquals(3, iSet.getNumberOfIntervals());
        interval = getIntervalAt(2);
        assertEquals(49, interval.low);
        assertEquals(81, interval.high);
    }

    public void testAddFullBetweenInterval() {
        interval = new Interval(26, 29);
        iSet.add(interval);
        assertEquals(2, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals(15, interval.low);
        assertEquals(45, interval.high);
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 14, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 48, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 82, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testGetNeededIntervalsAgain() {
        interval = new Interval(3, 5);
        iSet.add(interval);
        interval = new Interval(7, 9);
        iSet.add(interval);
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 2, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 6, interval.low);
        assertEquals("getNeededInterval broken", 6, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 10, interval.low);
        assertEquals("getNeededInterval broken", 14, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 46, interval.low);
        assertEquals("getNeededInterval broken", 48, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 82, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testAddFullLowCoveringInterval() {
        assertEquals(4, iSet.getNumberOfIntervals());
        interval = new Interval(2, 17);
        iSet.add(interval);
        assertEquals(2, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals(2, interval.low);
        assertEquals(45, interval.high);
        assertEquals("getSize() broken", 77, iSet.getSize());
    }

    public void testAddCoverFromMiddle() {
        interval = new Interval(40, 50);
        iSet.add(interval);
        assertEquals(1, iSet.getNumberOfIntervals());
        interval = getIntervalAt(0);
        assertEquals(2, interval.low);
        assertEquals(81, interval.high);
        assertEquals("getSize() broken", 80, iSet.getSize());
        iter = iSet.getNeededIntervals(100);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 0, interval.low);
        assertEquals("getNeededInterval broken", 1, interval.high);
        interval = (Interval) iter.next();
        assertEquals("getNeededInterval broken", 82, interval.low);
        assertEquals("getNeededInterval broken", 99, interval.high);
    }

    public void testGetNeededWithNoneAndSome() {
        iSet.clear();
        iSet.add(new Interval(0, 5));
        iter = iSet.getNeededIntervals(6);
        assertTrue(!iter.hasNext());
        iSet.add(new Interval(6, 10));
        iter = iSet.getNeededIntervals(20);
        interval = (Interval) iter.next();
        assertEquals(11, interval.low);
        assertEquals(19, interval.high);
        assertTrue(!iter.hasNext());
    }

    public void testSetUpForDelete() {
        iSet.clear();
        iSet.add(new Interval(0, 4));
        iSet.add(new Interval(8, 12));
        iSet.add(new Interval(16, 20));
        iSet.add(new Interval(24));
        iSet.add(new Interval(28, 32));
        iSet.add(new Interval(36, 40));
        assertEquals(6, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(12, interval.high);
        interval = (Interval) iter.next();
        assertEquals(16, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(24, interval.low);
        assertEquals(24, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(32, interval.high);
        interval = (Interval) iter.next();
        assertEquals(36, interval.low);
        assertEquals(40, interval.high);
    }

    public void testDeleteNothing() {
        iSet.delete(new Interval(5, 7));
        assertEquals(6, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(12, interval.high);
        interval = (Interval) iter.next();
        assertEquals(16, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(24, interval.low);
        assertEquals(24, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(32, interval.high);
        interval = (Interval) iter.next();
        assertEquals(36, interval.low);
        assertEquals(40, interval.high);
    }

    public void testDeleteBoundaries() {
        iSet.delete(new Interval(12, 16));
        assertEquals(6, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(11, interval.high);
        interval = (Interval) iter.next();
        assertEquals(17, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(24, interval.low);
        assertEquals(24, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(32, interval.high);
        interval = (Interval) iter.next();
        assertEquals(36, interval.low);
        assertEquals(40, interval.high);
    }

    public void testDeleteFullInterval() {
        iSet.delete(new Interval(24, 25));
        assertEquals(5, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(11, interval.high);
        interval = (Interval) iter.next();
        assertEquals(17, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(32, interval.high);
        interval = (Interval) iter.next();
        assertEquals(36, interval.low);
        assertEquals(40, interval.high);
    }

    public void testDeleteSplitsIntoTwo() {
        iSet.delete(new Interval(29, 30));
        assertEquals(6, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(11, interval.high);
        interval = (Interval) iter.next();
        assertEquals(17, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(28, interval.high);
        interval = (Interval) iter.next();
        assertEquals(31, interval.low);
        assertEquals(32, interval.high);
        interval = (Interval) iter.next();
        assertEquals(36, interval.low);
        assertEquals(40, interval.high);
    }

    public void testDeleteFullUpper() {
        iSet.delete(new Interval(35, 41));
        assertEquals(5, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(8, interval.low);
        assertEquals(11, interval.high);
        interval = (Interval) iter.next();
        assertEquals(17, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(28, interval.high);
        interval = (Interval) iter.next();
        assertEquals(31, interval.low);
        assertEquals(32, interval.high);
    }

    public void testDeleteIntervalSet() {
        IntervalSet toDelete = new IntervalSet();
        toDelete.add(new Interval(8, 15));
        toDelete.add(new Interval(18, 19));
        iSet.delete(toDelete);
        assertEquals(5, iSet.getNumberOfIntervals());
        iter = iSet.getAllIntervals();
        interval = (Interval) iter.next();
        assertEquals(0, interval.low);
        assertEquals(4, interval.high);
        interval = (Interval) iter.next();
        assertEquals(17, interval.low);
        assertEquals(17, interval.high);
        interval = (Interval) iter.next();
        assertEquals(20, interval.low);
        assertEquals(20, interval.high);
        interval = (Interval) iter.next();
        assertEquals(28, interval.low);
        assertEquals(28, interval.high);
        interval = (Interval) iter.next();
        assertEquals(31, interval.low);
        assertEquals(32, interval.high);
    }

    public void testGetFirstAndIsEmpty() {
        iSet = IntervalSet.createSingletonSet(0, 4);
        iSet.add(new Interval(17));
        iSet.add(new Interval(20));
        iSet.add(new Interval(28));
        iSet.add(new Interval(31, 32));
        assertEquals(new Interval(0, 4), iSet.getFirst());
        iSet.delete(iSet.getFirst());
        assertFalse(iSet.isEmpty());
        assertEquals(new Interval(17, 17), iSet.getFirst());
        iSet.delete(iSet.getFirst());
        assertFalse(iSet.isEmpty());
        assertEquals(new Interval(20, 20), iSet.getFirst());
        iSet.delete(iSet.getFirst());
        assertFalse(iSet.isEmpty());
        assertEquals(new Interval(28, 28), iSet.getFirst());
        iSet.delete(iSet.getFirst());
        assertFalse(iSet.isEmpty());
        assertEquals(new Interval(31, 32), iSet.getFirst());
        iSet.delete(iSet.getFirst());
        assertTrue(iSet.isEmpty());
        try {
            iSet.getFirst();
            fail("expected exception");
        } catch (NoSuchElementException nsee) {
        }
    }

    public void testGetLast() {
        iSet = IntervalSet.createSingletonSet(0, 4);
        iSet.add(new Interval(17));
        iSet.add(new Interval(20));
        iSet.add(new Interval(28));
        iSet.add(new Interval(31, 32));
        assertEquals(5, iSet.getNumberOfIntervals());
        assertEquals(new Interval(31, 32), iSet.getLast());
        iSet.delete(iSet.getLast());
        assertEquals(new Interval(28, 28), iSet.getLast());
        iSet.delete(iSet.getLast());
        assertEquals(new Interval(20, 20), iSet.getLast());
        iSet.delete(iSet.getLast());
        assertEquals(new Interval(17, 17), iSet.getLast());
        iSet.delete(iSet.getLast());
        assertEquals(new Interval(0, 4), iSet.getLast());
        iSet.delete(iSet.getLast());
        try {
            iSet.getLast();
            fail("expected exception");
        } catch (NoSuchElementException nsee) {
        }
    }

    public void testInvert() {
        iSet = new IntervalSet();
        assertEquals(0, iSet.getNumberOfIntervals());
        IntervalSet inverted = iSet.invert(100);
        assertEquals(1, inverted.getAllIntervalsAsList().size());
        assertEquals(new Interval(0, 99), inverted.getAllIntervals().next());
        iSet.add(new Interval(0, 100));
        inverted = iSet.invert(0);
        assertEquals(0, inverted.getAllIntervalsAsList().size());
    }

    public void testClone() {
        iSet = IntervalSet.createSingletonSet(0, 100);
        assertEquals(1, iSet.getNumberOfIntervals());
        IntervalSet clone = (IntervalSet) iSet.clone();
        assertEquals(1, clone.getAllIntervalsAsList().size());
        assertEquals(new Interval(0, 100), clone.getAllIntervals().next());
        clone.delete(clone.getFirst());
        assertEquals(0, clone.getAllIntervalsAsList().size());
        assertEquals(1, iSet.getNumberOfIntervals());
        assertEquals(new Interval(0, 100), getIntervalAt(0));
    }

    public void testToBytes() throws Exception {
        IntervalSet set = new IntervalSet();
        Random r = new Random();
        for (int i = 1; i < 10; i++) {
            int low = Math.abs(r.nextInt(100));
            Interval inter = new Interval(low, low + i);
            set.add(inter);
        }
        byte[] asByte = set.toBytes();
        IntervalSet set2 = IntervalSet.parseBytes(asByte);
        assertEquals(set.getSize(), set2.getSize());
        assertEquals(set.getAllIntervalsAsList(), set2.getAllIntervalsAsList());
    }

    public void testContains() {
        LOG.info("-- testing contains()");
        iSet = IntervalSet.createSingletonSet(1542374, 1572863);
        iSet.add(new Interval(1645963, 1835007));
        iSet.add(new Interval(2077669, 2228223));
        assertTrue(iSet.contains(new Interval(1542374, 1543397)));
    }

    private Interval getIntervalAt(int i) {
        return (Interval) iSet.getAllIntervalsAsList().get(i);
    }
}
