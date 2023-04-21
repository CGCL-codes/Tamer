package jaxlib.time;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import jaxlib.conversion.ConversionEngine;
import jaxlib.conversion.ConversionMethod;
import jaxlib.conversion.ConvertableType;
import jaxlib.lang.Chars;
import jaxlib.text.DateFormatISO8601;
import jaxlib.text.SimpleIntegerFormat;
import jaxlib.text.Stringifiable;

/**
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @version $Id: ImmutableCalendarDate.java 3009 2011-11-08 05:07:45Z joerg_wassmer $
 */
@Immutable
@ConvertableType
public class ImmutableCalendarDate extends java.sql.Date implements ImmutableDateType<ImmutableCalendarDate>, Stringifiable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private static final ImmutableCalendarDate ZERO = new ImmutableCalendarDate(0);

    @ConversionMethod
    private static ImmutableCalendarDate convert(final Object v) throws Exception {
        return valueOf(ConversionEngine.getDefault().convert(v, java.util.Date.class));
    }

    @ConversionMethod
    public static ImmutableCalendarDate valueOf(final long v) {
        return (v == 0) ? ZERO : new ImmutableCalendarDate(v);
    }

    @ConversionMethod
    public static ImmutableCalendarDate valueOf(@Nullable final java.util.Date v) {
        return (v == null) ? null : (v instanceof ImmutableCalendarDate) ? ((ImmutableCalendarDate) v) : valueOf(v.getTime());
    }

    @Nonnull
    public static ImmutableCalendarDate valueOf(final String s) {
        if ((s != null) && (s.length() == 10) && (s.charAt(4) == '-') && (s.charAt(7) == '-') && Chars.Ascii.isDigit(s.charAt(5)) && Chars.Ascii.isDigit(s.charAt(6)) && Chars.Ascii.isDigit(s.charAt(8)) && Chars.Ascii.isDigit(s.charAt(9))) {
            final int year = SimpleIntegerFormat.DECIMAL.tryParseInt(s, 0, 4, Integer.MIN_VALUE);
            if (year > Integer.MIN_VALUE) {
                final int month = SimpleIntegerFormat.DECIMAL.tryParseInt(s, 5, 7, 0);
                if ((month >= 1) && (month <= 12)) {
                    final int maxDay;
                    switch(month) {
                        case 2:
                            if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) maxDay = 31 - 2; else maxDay = 31 - 3;
                            break;
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            maxDay = 31 - 1;
                            break;
                        default:
                            maxDay = 31;
                            break;
                    }
                    final int day = SimpleIntegerFormat.DECIMAL.tryParseInt(s, 8, s.length(), 0);
                    if ((day >= 1) && (day <= maxDay)) return new ImmutableCalendarDate(year - 1900, month - 1, day);
                }
            }
        }
        throw new IllegalArgumentException("malformed SQL date: '" + s + "'");
    }

    protected ImmutableCalendarDate(final long timeAsMillis) {
        super(timeAsMillis);
    }

    private ImmutableCalendarDate(final int year, final int month, final int day) {
        super(year, month, day);
    }

    @Override
    public Object clone() {
        return this;
    }

    @Override
    public ImmutableCalendarDate set(final long t) {
        return (t == getTime()) ? this : new ImmutableCalendarDate(t);
    }

    @Override
    @Deprecated
    public final void setDate(final int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setMinutes(final int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setMonth(final int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setSeconds(final int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setTime(final long v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    public final void setYear(final int v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String toString() {
        return DateFormatISO8601.getDateInstance().format(this);
    }

    @Override
    public final void toString(final StringBuilder sb) {
        DateFormatISO8601.getDateInstance().format(this, sb);
    }
}
