package org.joda.time.chrono.gj;

import org.joda.time.field.FieldUtils;

/**
 * 
 * @author Brian S O'Neill
 */
class TestJulianWeekyearField extends TestGJWeekyearField {

    public TestJulianWeekyearField(TestJulianChronology chrono) {
        super(chrono);
    }

    public long addWrapField(long millis, int value) {
        int weekyear = get(millis);
        int wrapped = FieldUtils.getWrappedValue(weekyear, value, getMinimumValue(), getMaximumValue());
        return add(millis, (long) wrapped - weekyear);
    }

    public long add(long millis, long value) {
        int weekyear = get(millis);
        int newWeekyear = weekyear + FieldUtils.safeToInt(value);
        if (weekyear < 0) {
            if (newWeekyear >= 0) {
                newWeekyear++;
            }
        } else {
            if (newWeekyear <= 0) {
                newWeekyear--;
            }
        }
        return set(millis, newWeekyear);
    }

    public int getMinimumValue() {
        return -100000000;
    }

    public int getMaximumValue() {
        return 100000000;
    }
}
