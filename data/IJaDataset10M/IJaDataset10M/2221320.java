package org.genxdm.bridgekit.xs.operations;

import java.math.BigDecimal;
import org.genxdm.typed.types.AtomBridge;
import org.genxdm.xs.types.NativeType;

public final class OpXMLSchemaCompareDuration<A> implements ValueComparator<A> {

    private static final int[] MAX_DAYS_P_MONTH = { 31, 62, 92, 123, 153, 184, 215, 245, 276, 306, 337, 366 };

    private static final int[] MIN_DAYS_P_MONTH = { 28, 59, 89, 120, 150, 181, 212, 242, 273, 303, 334, 365 };

    public static final long SECONDS_PER_DAY_LONG = 86400l;

    private static int compare(int monthDiff, BigDecimal tsDiff) throws UnsupportedOperationException {
        final int timeSpanSign = tsDiff.signum();
        int days = (int) (tsDiff.longValue() / SECONDS_PER_DAY_LONG);
        if (monthDiff < 0) {
            monthDiff = -monthDiff;
        } else {
            days = -days;
        }
        final int yrs = (monthDiff - 1) / 12;
        final int normMonthDiff = (monthDiff + 11) % 12;
        final int minLeapYrs = yrs * 97 / 400;
        final int maxLeapYrs = minLeapYrs > 0 ? minLeapYrs + 1 : minLeapYrs;
        final int monthsToDaysMin = yrs * 365 + MIN_DAYS_P_MONTH[normMonthDiff] + minLeapYrs;
        final int monthsToDaysMax = yrs * 365 + MAX_DAYS_P_MONTH[normMonthDiff] + maxLeapYrs;
        if (days < monthsToDaysMin) {
            return -timeSpanSign;
        } else if (days > monthsToDaysMax) {
            return timeSpanSign;
        } else {
            throw new UnsupportedOperationException(monthDiff + " months vs. " + days + " days");
        }
    }

    private final AtomBridge<A> atomBridge;

    @SuppressWarnings("unused")
    private final NativeType nativeType;

    private final OpXMLSchemaCompare opcode;

    private final int monthsRHS;

    private final BigDecimal secondsRHS;

    public OpXMLSchemaCompareDuration(final OpXMLSchemaCompare opcode, final A rhsAtom, final NativeType nativeType, final AtomBridge<A> atomBridge) {
        this.opcode = opcode;
        this.nativeType = nativeType;
        this.atomBridge = atomBridge;
        monthsRHS = atomBridge.getDurationTotalMonths(rhsAtom);
        secondsRHS = atomBridge.getDurationTotalSeconds(rhsAtom);
    }

    public boolean compare(final A lhsAtom) {
        final int monthsLHS = atomBridge.getDurationTotalMonths(lhsAtom);
        final BigDecimal secondsLHS = atomBridge.getDurationTotalSeconds(lhsAtom);
        switch(opcode) {
            case Gt:
                {
                    return compareTo(monthsLHS, secondsLHS, monthsRHS, secondsRHS) > 0;
                }
            case Ge:
                {
                    return compareTo(monthsLHS, secondsLHS, monthsRHS, secondsRHS) >= 0;
                }
            case Lt:
                {
                    return compareTo(monthsLHS, secondsLHS, monthsRHS, secondsRHS) < 0;
                }
            case Le:
                {
                    return compareTo(monthsLHS, secondsLHS, monthsRHS, secondsRHS) <= 0;
                }
            default:
                {
                    throw new AssertionError(opcode);
                }
        }
    }

    private static int compareTo(final int monthsLHS, final BigDecimal secondsLHS, final int monthsRHS, final BigDecimal secondsRHS) {
        final int months = monthsLHS - monthsRHS;
        final int signumMonths = signum(months);
        final BigDecimal seconds = secondsLHS.subtract(secondsRHS);
        final int signumSeconds = seconds.signum();
        if (0 == signumMonths) {
            return signumSeconds;
        } else if (0 == signumSeconds) {
            return signumMonths;
        } else if (signumMonths == signumSeconds) {
            return signumMonths;
        } else {
            final int retval = compare(months, seconds);
            return retval;
        }
    }

    private static final int signum(final int value) {
        return (value > 0) ? +1 : (value < 0) ? -1 : 0;
    }
}
