package org.openmi.utilities.buffer;

import junit.framework.TestCase;
import org.openmi.backbone.ScalarSet;
import org.openmi.backbone.TimeSpan;
import org.openmi.backbone.TimeStamp;
import org.openmi.standard.IScalarSet;
import org.openmi.standard.ITimeSpan;
import org.openmi.standard.ITimeStamp;

/**
 * Unit test for the Buffer class.
 */
public class TestBuffer extends TestCase {

    public void testSmartBuffer() {
        try {
            SmartBuffer smartBufferA = new SmartBuffer();
            smartBufferA.addValues(new TimeStamp(1), new ScalarSet(1, 2, 3));
            smartBufferA.addValues(new TimeStamp(3), new ScalarSet(3, 4, 5));
            smartBufferA.addValues(new TimeStamp(6), new ScalarSet(6, 7, 8));
            SmartBuffer buffer1 = new SmartBuffer(smartBufferA);
            assertEquals(smartBufferA.getTimesCount(), buffer1.getTimesCount());
            for (int i = 0; i < smartBufferA.getTimesCount(); i++) {
                assertEquals(((ITimeStamp) smartBufferA.getTimeAt(i)).getModifiedJulianDay(), ((ITimeStamp) buffer1.getTimeAt(i)).getModifiedJulianDay());
                for (int n = 0; n < smartBufferA.getValuesCount(); n++) {
                    assertEquals(((IScalarSet) smartBufferA.getValuesAt(i)).getScalar(n), ((IScalarSet) buffer1.getValuesAt(i)).getScalar(n));
                }
            }
            SmartBuffer smartBufferB = new SmartBuffer();
            smartBufferB.addValues(new TimeSpan(new TimeStamp(1), new TimeStamp(2)), new ScalarSet(11, 12, 13));
            smartBufferB.addValues(new TimeSpan(new TimeStamp(2), new TimeStamp(3)), new ScalarSet(13, 14, 15));
            smartBufferB.addValues(new TimeSpan(new TimeStamp(3), new TimeStamp(5)), new ScalarSet(16, 17, 18));
            SmartBuffer buffer2 = new SmartBuffer(smartBufferB);
            assertEquals(smartBufferB.getTimesCount(), buffer2.getTimesCount());
            for (int i = 0; i < smartBufferB.getTimesCount(); i++) {
                assertEquals(((ITimeSpan) smartBufferB.getTimeAt(i)).getStart().getModifiedJulianDay(), ((ITimeSpan) buffer2.getTimeAt(i)).getStart().getModifiedJulianDay());
                assertEquals(((ITimeSpan) smartBufferB.getTimeAt(i)).getEnd().getModifiedJulianDay(), ((ITimeSpan) buffer2.getTimeAt(i)).getEnd().getModifiedJulianDay());
                for (int n = 0; n < smartBufferB.getValuesCount(); n++) {
                    assertEquals(((IScalarSet) smartBufferB.getValuesAt(i)).getScalar(n), ((IScalarSet) buffer2.getValuesAt(i)).getScalar(n));
                }
            }
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeStampsToTimeStamp_01() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.addValues(new TimeStamp(1), new ScalarSet(1, 2, 3));
            smartBuffer.addValues(new TimeStamp(3), new ScalarSet(3, 4, 5));
            double a = smartBuffer.getRelaxationFactor();
            assertEquals(new ScalarSet(new double[] { (1 - 3) / (3 - 1) * (1 - a) + 1, (2 - 4) / (3 - 1) * (1 - a) + 2, (3 - 5) / (3 - 1) * (1 - a) + 3 }), smartBuffer.getValues(new TimeStamp(0)));
            assertEquals(new ScalarSet(1, 2, 3), smartBuffer.getValues(new TimeStamp(1)));
            assertEquals(new ScalarSet(2, 3, 4), smartBuffer.getValues(new TimeStamp(2)));
            assertEquals(new ScalarSet(3, 4, 5), smartBuffer.getValues(new TimeStamp(3)));
            assertEquals(new ScalarSet(new double[] { (3 - 1) / (3 - 1) * (1 - a) + 3, (4 - 2) / (3 - 1) * (1 - a) + 4, (5 - 3) / (3 - 1) * (1 - a) + 5 }), smartBuffer.getValues(new TimeStamp(4)));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeStampsToTimeStamp_02() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.addValues(new TimeStamp(10), new ScalarSet(11, 2, 6));
            smartBuffer.addValues(new TimeStamp(13), new ScalarSet(5, 5, 6));
            smartBuffer.addValues(new TimeStamp(16), new ScalarSet(2, 14, 6));
            smartBuffer.addValues(new TimeStamp(20), new ScalarSet(2, 2, 6));
            smartBuffer.addValues(new TimeStamp(27), new ScalarSet(2, 9, 6));
            smartBuffer.addValues(new TimeStamp(30), new ScalarSet(-4, 9, 6));
            smartBuffer.addValues(new TimeStamp(48), new ScalarSet(8, -3, 6));
            smartBuffer.setRelaxationFactor(0.0);
            assertEquals(new ScalarSet(13, 1, 6), smartBuffer.getValues(new TimeStamp(9)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(9, 3, 6), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(7, 4, 6), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(5, 5, 6), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(4, 8, 6), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(3, 11, 6), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(2, 14, 6), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(2, 11, 6), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 2, 6), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 3, 6), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 5, 6), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(2, 9, 6), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(0, 9, 6), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-2, 9, 6), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(-4, 9, 6), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(-2, 7, 6), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(0, 5, 6), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(4, 1, 6), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(8, -3, 6), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(12, -7, 6), smartBuffer.getValues(new TimeStamp(54)));
            smartBuffer.setRelaxationFactor(1.0);
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(9)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(9, 3, 6), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(7, 4, 6), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(5, 5, 6), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(4, 8, 6), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(3, 11, 6), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(2, 14, 6), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(2, 11, 6), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 2, 6), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 3, 6), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 5, 6), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(2, 9, 6), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(0, 9, 6), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-2, 9, 6), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(-4, 9, 6), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(-2, 7, 6), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(0, 5, 6), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(4, 1, 6), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(8, -3, 6), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(8, -3, 6), smartBuffer.getValues(new TimeStamp(54)));
            double a = 0.7;
            smartBuffer.setRelaxationFactor(a);
            assertEquals(new ScalarSet(11 + (1 - a) * 2, 2 - (1 - a) * 1, 6), smartBuffer.getValues(new TimeStamp(9)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(9, 3, 6), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(7, 4, 6), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(5, 5, 6), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(4, 8, 6), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(3, 11, 6), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(2, 14, 6), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(2, 11, 6), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 2, 6), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 3, 6), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 5, 6), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(2, 9, 6), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(0, 9, 6), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-2, 9, 6), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(-4, 9, 6), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(-2, 7, 6), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(0, 5, 6), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(4, 1, 6), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(8, -3, 6), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(8 + (1 - a) * 4, -3 - (1 - a) * 4, 6), smartBuffer.getValues(new TimeStamp(54)));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeStampsToTimeStamp_05() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.setRelaxationFactor(0);
            smartBuffer.addValues(new TimeStamp(10), new ScalarSet(11, 2, 6));
            smartBuffer.addValues(new TimeStamp(13), new ScalarSet(5, 5, 6));
            assertEquals(new ScalarSet(13, 1, 6), smartBuffer.getValues(new TimeStamp(9)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(9, 3, 6), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(7, 4, 6), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(5, 5, 6), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(3, 6, 6), smartBuffer.getValues(new TimeStamp(14)));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeStampsToTimeStamp_06() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.setRelaxationFactor(0);
            smartBuffer.addValues(new TimeStamp(10), new ScalarSet(11, 2, 6));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(9)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(11, 2, 6), smartBuffer.getValues(new TimeStamp(14)));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeSpansToTimeStamp_01() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.addValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)), new ScalarSet(5, 12, 5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(13), new TimeStamp(16)), new ScalarSet(7, 11, 5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(16), new TimeStamp(20)), new ScalarSet(9, 10, 5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(20), new TimeStamp(27)), new ScalarSet(2, 7, 5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(27), new TimeStamp(30)), new ScalarSet(-5, 6, 5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(30), new TimeStamp(48)), new ScalarSet(7, 3, 5));
            smartBuffer.setRelaxationFactor(0.0);
            assertEquals(new ScalarSet(1, 14, 5), smartBuffer.getValues(new TimeStamp(4)));
            assertEquals(new ScalarSet(3, 13, 5), smartBuffer.getValues(new TimeStamp(7)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(11, 2, 5), smartBuffer.getValues(new TimeStamp(54)));
            assertEquals(new ScalarSet(15, 1, 5), smartBuffer.getValues(new TimeStamp(60)));
            smartBuffer.setRelaxationFactor(1.0);
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(4)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(7)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(54)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(60)));
            double a = 0.7;
            smartBuffer.setRelaxationFactor(a);
            assertEquals(new ScalarSet(5 - (1 - a) * (5 - 1), 12 - (1 - a) * (12 - 14), 5), smartBuffer.getValues(new TimeStamp(4)));
            assertEquals(new ScalarSet(5 - (1 - a) * (5 - 3), 12 - (1 - a) * (12 - 13), 5), smartBuffer.getValues(new TimeStamp(7)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(10)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(11)));
            assertEquals(new ScalarSet(5, 12, 5), smartBuffer.getValues(new TimeStamp(12)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(13)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(14)));
            assertEquals(new ScalarSet(7, 11, 5), smartBuffer.getValues(new TimeStamp(15)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(16)));
            assertEquals(new ScalarSet(9, 10, 5), smartBuffer.getValues(new TimeStamp(17)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(20)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(21)));
            assertEquals(new ScalarSet(2, 7, 5), smartBuffer.getValues(new TimeStamp(23)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(27)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(28)));
            assertEquals(new ScalarSet(-5, 6, 5), smartBuffer.getValues(new TimeStamp(29)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(30)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(33)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(36)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(42)));
            assertEquals(new ScalarSet(7, 3, 5), smartBuffer.getValues(new TimeStamp(48)));
            assertEquals(new ScalarSet(7 + (1 - a) * (11 - 7), 3 + (1 - a) * (2 - 3), 5), smartBuffer.getValues(new TimeStamp(54)));
            assertEquals(new ScalarSet(7 + (1 - a) * (15 - 7), 3 + (1 - a) * (1 - 3), 5), smartBuffer.getValues(new TimeStamp(60)));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeSpansToTimeSpans() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.setRelaxationFactor(1.0);
            smartBuffer.addValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)), new ScalarSet(1));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(13), new TimeStamp(16)), new ScalarSet(2));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(16), new TimeStamp(19)), new ScalarSet(3));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(19), new TimeStamp(27)), new ScalarSet(4));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(27), new TimeStamp(30)), new ScalarSet(5));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(30), new TimeStamp(48)), new ScalarSet(6));
            assertEquals(1.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(9), new TimeStamp(13)))).getScalar(0));
            assertEquals(1.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)))).getScalar(0));
            assertEquals(1.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(11), new TimeStamp(12)))).getScalar(0));
            assertEquals(2.5, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(13), new TimeStamp(19)))).getScalar(0));
            assertEquals(2.5, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(14.5), new TimeStamp(17.5)))).getScalar(0));
            smartBuffer.setRelaxationFactor(1.0);
            assertEquals(1.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)))).getScalar(0));
            assertEquals(1.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(11), new TimeStamp(12)))).getScalar(0));
            assertEquals(2.5, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(13), new TimeStamp(19)))).getScalar(0));
            assertEquals(2.5, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(14.5), new TimeStamp(17.5)))).getScalar(0));
        } catch (Exception e) {
            fail();
        }
    }

    public void testGetValues_TimeStampsToTimeSpan_01() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.addValues(new TimeStamp(2), new ScalarSet(2));
            smartBuffer.addValues(new TimeStamp(4), new ScalarSet(4));
            smartBuffer.addValues(new TimeStamp(7), new ScalarSet(4));
            smartBuffer.addValues(new TimeStamp(11), new ScalarSet(6));
            smartBuffer.addValues(new TimeStamp(13), new ScalarSet(4));
            smartBuffer.addValues(new TimeStamp(15), new ScalarSet(3));
            smartBuffer.setRelaxationFactor(1.0);
            assertEquals(24.5 / 6.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(3), new TimeStamp(9)))).getScalar(0), 0.00001);
            assertEquals(49.25 / 11.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(3), new TimeStamp(14)))).getScalar(0), 0.00001);
            assertEquals(13.0 / 4.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(13), new TimeStamp(17)))).getScalar(0), 0.00001);
            smartBuffer.setRelaxationFactor(0.0);
            assertEquals(3.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(13), new TimeStamp(17)))).getScalar(0), 0.00001);
            smartBuffer.setRelaxationFactor(1.0);
            assertEquals(3.0, ((ScalarSet) smartBuffer.getValues(new TimeSpan(new TimeStamp(2), new TimeStamp(4)))).getScalar(0), 0.00001);
        } catch (Exception e) {
            fail();
        }
    }

    public void testAddValues_01() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.setDoExtendedDataVerification(true);
            ScalarSet scalarSet = new ScalarSet(0, 1, 2);
            TimeStamp timeStamp = new TimeStamp(1);
            smartBuffer.addValues(timeStamp, scalarSet);
            timeStamp.setModifiedJulianDay(2);
            scalarSet.setValue(0, 10.0);
            scalarSet.setValue(1, 11.0);
            scalarSet.setValue(2, 12.0);
            smartBuffer.addValues(timeStamp, scalarSet);
            smartBuffer.addValues(new TimeStamp(3), new ScalarSet(110, 111, 112));
            smartBuffer.addValues(new TimeStamp(4), new ScalarSet(1110, 1111, 1112));
            assertEquals(0.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(1.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(1));
            assertEquals(2.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(2));
            assertEquals(10.0, ((IScalarSet) smartBuffer.getValuesAt(1)).getScalar(0));
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(1)).getScalar(1));
            assertEquals(12.0, ((IScalarSet) smartBuffer.getValuesAt(1)).getScalar(2));
            assertEquals(110.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(0));
            assertEquals(111.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(1));
            assertEquals(112.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(2));
            assertEquals(1110.0, ((IScalarSet) smartBuffer.getValuesAt(3)).getScalar(0));
            assertEquals(1111.0, ((IScalarSet) smartBuffer.getValuesAt(3)).getScalar(1));
            assertEquals(1112.0, ((IScalarSet) smartBuffer.getValuesAt(3)).getScalar(2));
            assertEquals(4, smartBuffer.getTimesCount());
            assertEquals(3, smartBuffer.getValuesCount());
        } catch (Exception e) {
            fail();
        }
    }

    public void testClearAfter() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.setDoExtendedDataVerification(true);
            smartBuffer.addValues(new TimeStamp(10), new ScalarSet(11, 21));
            smartBuffer.addValues(new TimeStamp(13), new ScalarSet(12, 22));
            smartBuffer.addValues(new TimeStamp(16), new ScalarSet(13, 23));
            smartBuffer.addValues(new TimeStamp(20), new ScalarSet(14, 24));
            smartBuffer.addValues(new TimeStamp(27), new ScalarSet(15, 25));
            smartBuffer.addValues(new TimeStamp(30), new ScalarSet(16, 26));
            smartBuffer.addValues(new TimeStamp(48), new ScalarSet(17, 27));
            TimeStamp time = new TimeStamp();
            time.setModifiedJulianDay(50);
            smartBuffer.clearAfter(time);
            assertEquals(7, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(27.0, ((IScalarSet) smartBuffer.getValuesAt(6)).getScalar(1));
            time.setModifiedJulianDay(30);
            smartBuffer.clearAfter(time);
            assertEquals(5, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(25.0, ((IScalarSet) smartBuffer.getValuesAt(4)).getScalar(1));
            time.setModifiedJulianDay(16.5);
            smartBuffer.clearAfter(time);
            assertEquals(3, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(23.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(1));
            time.setModifiedJulianDay(9);
            smartBuffer.clearAfter(time);
            assertEquals(0, smartBuffer.getTimesCount());
            time.setModifiedJulianDay(9);
            smartBuffer.clearAfter(time);
            assertEquals(0, smartBuffer.getTimesCount());
            smartBuffer.addValues(new TimeStamp(10), new ScalarSet(11, 21));
            smartBuffer.addValues(new TimeStamp(13), new ScalarSet(12, 22));
            smartBuffer.addValues(new TimeStamp(16), new ScalarSet(13, 23));
            smartBuffer.addValues(new TimeStamp(20), new ScalarSet(14, 24));
            smartBuffer.addValues(new TimeStamp(27), new ScalarSet(15, 25));
            smartBuffer.addValues(new TimeStamp(30), new ScalarSet(16, 26));
            smartBuffer.addValues(new TimeStamp(48), new ScalarSet(17, 27));
            TimeStamp start = new TimeStamp(50);
            TimeStamp end = new TimeStamp(55);
            TimeSpan timeSpan = new TimeSpan(start, end);
            start.setModifiedJulianDay(50);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(7, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(27.0, ((IScalarSet) smartBuffer.getValuesAt(6)).getScalar(1));
            start.setModifiedJulianDay(30);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(5, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(25.0, ((IScalarSet) smartBuffer.getValuesAt(4)).getScalar(1));
            start.setModifiedJulianDay(16.5);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(3, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(23.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(1));
            start.setModifiedJulianDay(9);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(0, smartBuffer.getTimesCount());
            start.setModifiedJulianDay(9);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(0, smartBuffer.getTimesCount());
            smartBuffer.addValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)), new ScalarSet(11, 21));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(13), new TimeStamp(16)), new ScalarSet(12, 22));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(16), new TimeStamp(20)), new ScalarSet(13, 23));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(20), new TimeStamp(27)), new ScalarSet(14, 24));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(27), new TimeStamp(30)), new ScalarSet(15, 25));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(30), new TimeStamp(48)), new ScalarSet(16, 26));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(48), new TimeStamp(55)), new ScalarSet(17, 27));
            time.setModifiedJulianDay(50);
            smartBuffer.clearAfter(time);
            assertEquals(7, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(27.0, ((IScalarSet) smartBuffer.getValuesAt(6)).getScalar(1));
            time.setModifiedJulianDay(30);
            smartBuffer.clearAfter(time);
            assertEquals(5, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(25.0, ((IScalarSet) smartBuffer.getValuesAt(4)).getScalar(1));
            time.setModifiedJulianDay(16.5);
            smartBuffer.clearAfter(time);
            assertEquals(3, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(23.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(1));
            time.setModifiedJulianDay(9);
            smartBuffer.clearAfter(time);
            assertEquals(0, smartBuffer.getTimesCount());
            time.setModifiedJulianDay(9);
            smartBuffer.clearAfter(time);
            assertEquals(0, smartBuffer.getTimesCount());
            smartBuffer.addValues(new TimeSpan(new TimeStamp(10), new TimeStamp(13)), new ScalarSet(11, 21));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(13), new TimeStamp(16)), new ScalarSet(12, 22));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(16), new TimeStamp(20)), new ScalarSet(13, 23));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(20), new TimeStamp(27)), new ScalarSet(14, 24));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(27), new TimeStamp(30)), new ScalarSet(15, 25));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(30), new TimeStamp(48)), new ScalarSet(16, 26));
            smartBuffer.addValues(new TimeSpan(new TimeStamp(48), new TimeStamp(55)), new ScalarSet(17, 27));
            start.setModifiedJulianDay(50);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(7, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(27.0, ((IScalarSet) smartBuffer.getValuesAt(6)).getScalar(1));
            start.setModifiedJulianDay(30);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(5, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(25.0, ((IScalarSet) smartBuffer.getValuesAt(4)).getScalar(1));
            start.setModifiedJulianDay(16.5);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(3, smartBuffer.getTimesCount());
            assertEquals(11.0, ((IScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(23.0, ((IScalarSet) smartBuffer.getValuesAt(2)).getScalar(1));
            start.setModifiedJulianDay(9);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(0, smartBuffer.getTimesCount());
            start.setModifiedJulianDay(9);
            smartBuffer.clearAfter(timeSpan);
            assertEquals(0, smartBuffer.getTimesCount());
        } catch (Exception e) {
            fail();
        }
    }

    public void testClearBefore() {
        try {
            SmartBuffer smartBuffer = new SmartBuffer();
            smartBuffer.addValues(new TimeStamp(1), new ScalarSet(new double[] { 1.1, 2.1, 3.1 }));
            smartBuffer.addValues(new TimeStamp(3), new ScalarSet(new double[] { 4.1, 5.1, 6.1 }));
            smartBuffer.addValues(new TimeStamp(4), new ScalarSet(new double[] { 7.1, 8.1, 9.1 }));
            smartBuffer.addValues(new TimeStamp(5), new ScalarSet(new double[] { 10.1, 11.1, 12.1 }));
            smartBuffer.clearBefore(new TimeStamp(0.5));
            assertEquals(4, smartBuffer.getTimesCount());
            smartBuffer.checkBuffer();
            smartBuffer.clearBefore(new TimeStamp(1));
            assertEquals(4, smartBuffer.getTimesCount());
            smartBuffer.checkBuffer();
            smartBuffer.clearBefore(new TimeStamp(1.1));
            assertEquals(4, smartBuffer.getTimesCount());
            assertEquals(1.1, ((ScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(1.0, ((ITimeStamp) smartBuffer.getTimeAt(0)).getModifiedJulianDay());
            smartBuffer.checkBuffer();
            smartBuffer.clearBefore(new TimeStamp(4.1));
            assertEquals(2, smartBuffer.getTimesCount());
            assertEquals(7.1, ((ScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(4.0, ((ITimeStamp) smartBuffer.getTimeAt(0)).getModifiedJulianDay());
            smartBuffer.checkBuffer();
            smartBuffer.clearBefore(new TimeStamp(5.1));
            assertEquals(1, smartBuffer.getTimesCount());
            assertEquals(10.1, ((ScalarSet) smartBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(5.0, ((ITimeStamp) smartBuffer.getTimeAt(0)).getModifiedJulianDay());
            smartBuffer.checkBuffer();
            SmartBuffer timeSpanBuffer = new SmartBuffer();
            timeSpanBuffer.addValues(new TimeSpan(new TimeStamp(1), new TimeStamp(3)), new ScalarSet(new double[] { 1.1, 2.1, 3.1 }));
            timeSpanBuffer.addValues(new TimeSpan(new TimeStamp(3), new TimeStamp(5)), new ScalarSet(new double[] { 4.1, 5.1, 6.1 }));
            timeSpanBuffer.addValues(new TimeSpan(new TimeStamp(5), new TimeStamp(7)), new ScalarSet(new double[] { 7.1, 8.1, 9.1 }));
            timeSpanBuffer.addValues(new TimeSpan(new TimeStamp(7), new TimeStamp(9)), new ScalarSet(new double[] { 10.1, 11.1, 12.1 }));
            timeSpanBuffer.clearBefore(new TimeStamp(0.5));
            assertEquals(4, timeSpanBuffer.getTimesCount());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(1.0));
            assertEquals(4, timeSpanBuffer.getTimesCount());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(2.0));
            assertEquals(4, timeSpanBuffer.getTimesCount());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(3.0));
            assertEquals(4, timeSpanBuffer.getTimesCount());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(4.0));
            assertEquals(4, timeSpanBuffer.getTimesCount());
            assertEquals(1.1, ((ScalarSet) timeSpanBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(1.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getStart().getModifiedJulianDay());
            assertEquals(3.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getEnd().getModifiedJulianDay());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(7.0));
            assertEquals(3, timeSpanBuffer.getTimesCount());
            assertEquals(4.1, ((ScalarSet) timeSpanBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(3.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getStart().getModifiedJulianDay());
            assertEquals(5.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getEnd().getModifiedJulianDay());
            timeSpanBuffer.checkBuffer();
            timeSpanBuffer.clearBefore(new TimeStamp(10.0));
            assertEquals(1, timeSpanBuffer.getTimesCount());
            assertEquals(10.1, ((ScalarSet) timeSpanBuffer.getValuesAt(0)).getScalar(0));
            assertEquals(7.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getStart().getModifiedJulianDay());
            assertEquals(9.0, ((ITimeSpan) timeSpanBuffer.getTimeAt(0)).getEnd().getModifiedJulianDay());
            timeSpanBuffer.checkBuffer();
        } catch (Exception e) {
            fail();
        }
    }
}
