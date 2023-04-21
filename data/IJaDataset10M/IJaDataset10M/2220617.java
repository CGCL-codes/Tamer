package gnu.testlet.java2.util.GregorianCalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class internal implements Testlet {

    private static class TestCalendar extends GregorianCalendar {

        /**
     * Creates a calendar with all values unset and zeroed.
     */
        public static TestCalendar getTestCalendar(TimeZone zone, Locale locale) {
            Locale defaultLocale = Locale.getDefault();
            Locale.setDefault(locale);
            try {
                TestCalendar c = new TestCalendar();
                c.setTimeZone(zone);
                return c;
            } finally {
                Locale.setDefault(defaultLocale);
            }
        }

        private TestCalendar() {
            super(2007, Calendar.APRIL, 11);
            clear();
        }

        /**
     * Compare the internal state of this calendar with the supplied values.
     */
        public void checkState(TestHarness harness, boolean areFieldsSet, int isSetBitmask, int[] fields, boolean isTimeSet, long time) {
            String expected, actual;
            boolean success;
            expected = stateString(areFieldsSet, isSetBitmask, isTimeSet, time);
            harness.debug("expecting " + expected);
            actual = stateString(this.areFieldsSet, isSet, this.isTimeSet, this.time);
            success = expected.equals(actual);
            if (!success) harness.debug("      got " + actual);
            harness.check(success);
            expected = stateString(fields);
            harness.debug("expecting " + expected);
            actual = stateString(this.fields);
            success = expected.equals(actual);
            if (!success) harness.debug("      got " + actual);
            harness.check(success);
        }

        /**
     * Return a string representation of part of the internal state.
     */
        private String stateString(boolean areFieldsSet, int isSetBitmask, boolean isTimeSet, long time) {
            String result = areFieldsSet + " (";
            for (int i = 0; i < FIELD_COUNT; i++) result += ((isSetBitmask & (1 << i)) != 0) ? "S" : "-";
            result += "), " + isTimeSet + " (" + time + ")";
            return result;
        }

        /**
     * Return a string representation of part of the internal state.
     */
        private String stateString(boolean areFieldsSet, boolean[] isSet, boolean isTimeSet, long time) {
            int isSetBitmask = 0;
            for (int i = 0; i < FIELD_COUNT; i++) {
                if (isSet[i]) isSetBitmask |= 1 << i;
            }
            return stateString(areFieldsSet, isSetBitmask, isTimeSet, time);
        }

        /**
     * Return a string representation of part of the internal state.
     */
        private String stateString(int[] fields) {
            String result = "{";
            for (int i = 0; i < FIELD_COUNT; i++) {
                if (i > 0) result += ", ";
                result += fields[i];
            }
            result += "}";
            return result;
        }
    }

    /**
   * Generate every possible permutation of a specified length
   */
    private static class Permutator implements Iterator {

        private int size, length, pos;

        public Permutator(int s) {
            size = s;
            length = 1;
            for (int i = 2; i <= size; i++) length *= i;
            pos = 0;
        }

        public boolean hasNext() {
            return pos < length;
        }

        public Object next() {
            int[] result = new int[size];
            boolean[] used = new boolean[size];
            for (int tmp = pos, j = size; j > 0; tmp /= j, j--) {
                int choice = tmp % j;
                for (int k = 0; k < size; k++) {
                    if (!used[k]) {
                        if (choice == 0) {
                            result[size - j] = k;
                            used[k] = true;
                            break;
                        }
                        choice--;
                    }
                }
            }
            pos++;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public void test(TestHarness harness) {
        int[][] checkFields = { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 1970, 0, 1, 1, 1, 1, 5, 1, 0, 0, 0, 0, 0, 0, -18000000, 0 }, { 1, 1969, 11, 1, 5, 31, 365, 4, 5, 1, 10, 22, 0, 0, 0, -25200000, 0 }, { 1, 1970, 11, 1, 5, 31, 365, 5, 5, 1, 10, 22, 0, 0, 0, -25200000, 0 }, { 1, 1969, 0, 5, 5, 31, 31, 6, 5, 1, 10, 22, 0, 0, 0, -25200000, 0 }, { 1, 1970, 0, 1, 1, 1, 1, 5, 1, 0, 0, 0, 0, 0, 0, -25200000, 0 }, { 0, 2007, 3, 15, 4, 18, 107, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0 } };
        TestCalendar c = TestCalendar.getTestCalendar(TimeZone.getTimeZone("EST"), Locale.US);
        c.checkState(harness, false, 0, checkFields[0], false, 0);
        harness.check(c.getFirstDayOfWeek() == Calendar.SUNDAY);
        harness.check(c.getMinimalDaysInFirstWeek() == 1);
        c.get(Calendar.YEAR);
        c.checkState(harness, true, 0x1ffff, checkFields[1], true, 18000000);
        c.setTimeZone(TimeZone.getTimeZone("MST"));
        c.checkState(harness, false, 0x1ffff, checkFields[1], true, 18000000);
        c.get(Calendar.YEAR);
        c.checkState(harness, true, 0x1ffff, checkFields[2], true, 18000000);
        for (int i = 0; i < Calendar.FIELD_COUNT; i++) {
            TestCalendar d = (TestCalendar) c.clone();
            int[] expectFields = (int[]) checkFields[2].clone();
            long expectTime = 18000000;
            d.checkState(harness, true, 0x1ffff, expectFields, true, expectTime);
            d.clear(i);
            expectFields[i] = 0;
            d.checkState(harness, false, ~(1 << i), expectFields, false, expectTime);
            d.get(Calendar.YEAR);
            if (i == Calendar.YEAR) {
                expectFields = checkFields[3];
                expectTime = 31554000000L;
            } else if (i == Calendar.MONTH) {
                expectFields = checkFields[4];
                expectTime = -28839600000L;
            } else {
                expectFields = checkFields[2];
                expectTime = 18000000;
            }
            d.checkState(harness, true, 0x1ffff, expectFields, true, expectTime);
        }
        c.clear();
        c.checkState(harness, false, 0, checkFields[0], false, 18000000);
        c.get(Calendar.YEAR);
        c.checkState(harness, true, 0x1ffff, checkFields[5], true, 25200000);
        c.setTimeInMillis(18000000);
        c.checkState(harness, true, 0x1ffff, checkFields[2], true, 18000000);
        for (Iterator iter = new Permutator(8); iter.hasNext(); ) {
            int[] order = (int[]) iter.next();
            for (int i = 0; i < order.length; i++) order[i] += Calendar.YEAR;
            c.setTimeInMillis(0);
            c.clear();
            for (int i = 0; i < order.length; i++) c.set(order[i], checkFields[6][order[i]]);
            c.checkState(harness, false, 0x1fe, checkFields[6], false, 0);
            int[] setOrder = new int[Calendar.AM_PM - Calendar.WEEK_OF_YEAR];
            for (int i = 0, j = setOrder.length - 1; j >= 0; i++) {
                if (order[i] >= Calendar.WEEK_OF_YEAR && order[i] <= Calendar.DAY_OF_WEEK_IN_MONTH) setOrder[j--] = order[i];
            }
            long expectTime = -1;
            if (setOrder[0] == Calendar.DAY_OF_MONTH) {
                expectTime = 1176879600000L;
            } else if (setOrder[0] == Calendar.DAY_OF_WEEK_IN_MONTH) {
                expectTime = 1178175600000L;
            } else if (setOrder[0] == Calendar.DAY_OF_YEAR) {
                expectTime = 1176793200000L;
            } else if (setOrder[0] == Calendar.WEEK_OF_YEAR) {
                expectTime = 1176361200000L;
            } else if (setOrder[0] == Calendar.DAY_OF_WEEK) {
                for (int i = 1; i < setOrder.length; i++) {
                    if (setOrder[i] == Calendar.DAY_OF_MONTH || setOrder[i] == Calendar.DAY_OF_YEAR) continue;
                    if (setOrder[i] == Calendar.WEEK_OF_YEAR) {
                        expectTime = 1176361200000L;
                    } else if (setOrder[i] == Calendar.WEEK_OF_MONTH) {
                        expectTime = 1177570800000L;
                    } else {
                        expectTime = 1178175600000L;
                    }
                    break;
                }
            } else {
                expectTime = 1177570800000L;
            }
            long actualTime = c.getTimeInMillis();
            harness.check(actualTime == expectTime);
        }
    }
}
