package jaxlib.time.cron;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.concurrent.Immutable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import jaxlib.col.AbstractXSet;
import jaxlib.conversion.ConversionMethod;
import jaxlib.conversion.ConvertableType;
import jaxlib.lang.Chars;
import jaxlib.text.SimpleIntegerFormat;
import jaxlib.time.Month;
import jaxlib.time.xml.MonthsXml;
import jaxlib.util.CheckBounds;
import jaxlib.util.Strings;

/**
 * An immutable set of months in a year of the gregorian calendar.
 * <p>
 * For compatibility with {@link Date} and {@link Calendar} January has the value {@code 0} and December has
 * the value {@code 11}. This is annoying, but changing this would introduce new bugs. Blame the programmer
 * of the Date and the Calendar classes.
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: Months.java 3009 2011-11-08 05:07:45Z joerg_wassmer $
 */
@Immutable
@XmlRootElement(name = "months", namespace = CronPart.XML_NAMESPACE)
@XmlType(name = "Months", namespace = CronPart.XML_NAMESPACE)
@XmlAccessorType(XmlAccessType.NONE)
@XmlJavaTypeAdapter(MonthsXml.class)
@ConvertableType
public final class Months extends AbstractXSet<Month> implements CronPart, Comparable<Months> {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static final int MASK = 0x1000;

    private static final Months[] cache = new Months[MASK + 1];

    static {
        for (int i = MASK; --i >= 0; ) Months.cache[i] = new Months(i);
    }

    public static final Months EACH = ofBits(MASK - 1);

    public static final Months NONE = ofBits(0);

    static void checkMonth(int v) {
        if ((v < 0) || (v > 11)) throw new IllegalArgumentException("Value is not in range 0..11: " + v);
    }

    public static Months of(final Calendar v) {
        return (v == null) ? null : of(Month.of(v));
    }

    public static Months of(final Date v) {
        return (v == null) ? null : of(Month.of(v));
    }

    public static Months of(final Month month) {
        return (month == null) ? null : Months.cache[month.bit];
    }

    public static Months of(final Month... months) {
        if (months == null) return null;
        int bits = 0;
        for (int i = months.length; --i >= 0; ) bits |= months[i].bit;
        return Months.cache[bits];
    }

    public static Months of(final Iterable<Month> months) {
        if (months == null) return null;
        int bits = 0;
        for (final Iterator<Month> it = months.iterator(); it.hasNext(); ) bits |= it.next().bit;
        return Months.cache[bits];
    }

    public static Months ofBits(final int bits) {
        return Months.cache[bits];
    }

    @ConversionMethod
    public static Months parse(final CharSequence s) {
        return (s == null) ? null : parse(s, 0, s.length());
    }

    public static Months parse(final CharSequence s, int fromIndex, final int toIndex) {
        CheckBounds.range(s, fromIndex, toIndex);
        Months months = NONE;
        int start = -1;
        boolean range = false;
        while (fromIndex <= toIndex) {
            final char c = (fromIndex == toIndex) ? ' ' : s.charAt(fromIndex);
            if (Chars.isWhitespace(c) || (c == ',')) {
                if (start >= 0) {
                    final Month m = Month.parse(s, start, fromIndex);
                    start = -1;
                    if (range) {
                        months = months.include(range(months.last, m));
                        range = false;
                    } else {
                        months = months.include(m);
                    }
                } else if (c == ',') {
                    throw new IllegalArgumentException(s.toString());
                }
            } else if (c == '-') {
                if ((fromIndex == 0) && (toIndex == fromIndex + 1)) return NONE;
                if ((start < 0) || range) throw new IllegalArgumentException(s.toString());
                range = true;
                months = months.include(Month.parse(s, start, fromIndex));
                start = fromIndex + 1;
            } else if (c == '*') {
                if (months != NONE) throw new IllegalArgumentException(s.toString());
                while (++fromIndex < toIndex) {
                    if (!Chars.isWhitespace(s.charAt(fromIndex))) throw new IllegalArgumentException(s.toString());
                }
                return EACH;
            } else if (start == -1) {
                start = fromIndex;
            }
            fromIndex++;
        }
        return months;
    }

    public static Months range(Month from, final Month to) {
        int bits = 0;
        while (true) {
            bits |= from.bit;
            if (from == to) break;
            from = from.next();
        }
        return Months.cache[bits];
    }

    /**
   * @serial
   * @since JaXLib 1.0
   */
    public final int bits;

    /**
   * @since JaXLib 1.0
   */
    public final transient int count;

    /**
   * @since JaXLib 1.0
   */
    public final transient Month first;

    /**
   * @since JaXLib 1.0
   */
    public final transient Month last;

    private final transient int hashCode;

    private transient String string;

    /**
   * Just because of a JAXB bug.
   */
    private Months() {
        super();
        this.bits = 0;
        this.count = 0;
        this.first = null;
        this.last = null;
        this.hashCode = 0;
    }

    private Months(final int bits) {
        super();
        this.bits = bits;
        this.count = Integer.bitCount(bits);
        this.hashCode = super.hashCode();
        if (bits == 0) {
            this.first = null;
            this.last = null;
        } else {
            this.first = Month.ofOrdinal(Integer.numberOfTrailingZeros(bits));
            this.last = Month.ofOrdinal(31 - Integer.numberOfLeadingZeros(bits));
        }
    }

    /**
   * @serialData
   * @since JaXLib 1.0
   */
    private Object readResolve() {
        return ofBits(this.bits);
    }

    @Override
    public final int compareTo(final Months b) {
        return (b == this) ? 0 : (b == null) ? 1 : Group.compareByEarlier(this.bits, b.bits);
    }

    @Override
    public final boolean contains(final Object e) {
        return (e instanceof Month) && ((this.bits & ((Month) e).bit) != 0);
    }

    @Override
    public final boolean contains(final Calendar c) {
        return (c != null) && contains(c.get(Calendar.MONTH));
    }

    public final boolean contains(final Month month) {
        return (month != null) && ((this.bits & month.bit) != 0);
    }

    @Override
    public final boolean contains(final int month) {
        return (this.bits & (1 << month)) != 0;
    }

    @Override
    public final boolean containsAll(final Collection<?> src) {
        if (src == this) return true;
        if (src instanceof Months) {
            final int b = ((Months) src).bits;
            return (this.bits & b) == b;
        }
        return super.containsAll(src);
    }

    public final boolean containsAll(final Months src) {
        return (this.bits & src.bits) == src.bits;
    }

    @Override
    public final boolean equals(final Object o) {
        return (o == this) || (!(o instanceof Months) && super.equals(o));
    }

    public final boolean equals(final Months b) {
        return b == this;
    }

    public final Months exclude(final Month b) {
        return Months.cache[this.bits & ~b.bit];
    }

    public final Months exclude(final Months b) {
        return Months.cache[this.bits & ~b.bits];
    }

    public final Months excluded() {
        return Months.cache[(~this.bits) & MASK];
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    public final Month next(Month prev) {
        if (prev == null) prev = Month.JANUARY; else prev = prev.nextInSameYear();
        while ((prev != null) && !contains(prev)) prev = prev.nextInSameYear();
        return prev;
    }

    @Override
    public final int next(int prev) {
        if (prev < -1) prev = -1;
        while (prev < 12) {
            if ((this.bits & (1 << ++prev)) != 0) return prev;
        }
        return -1;
    }

    public final Month prev(Month next) {
        if (next == null) next = Month.DECEMBER; else next = next.prevInSameYear();
        while ((next != null) && !contains(next)) next = next.prevInSameYear();
        return next;
    }

    @Override
    public final int prev(int next) {
        if (next > 12) next = 12;
        while (next > 0) {
            if ((this.bits & (1 << --next)) != 0) return next;
        }
        return -1;
    }

    public final Months include(final Month b) {
        return Months.cache[this.bits | b.bit];
    }

    public final Months include(final Months b) {
        return Months.cache[this.bits | b.bits];
    }

    public final Months intersection(final Months b) {
        return Months.cache[this.bits & b.bits];
    }

    @Override
    public final boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public final Iterator<Month> iterator() {
        return new Months.IteratorImpl(this);
    }

    @Override
    public final int size() {
        return this.count;
    }

    @Override
    public final String toString() {
        String s = this.string;
        if (s == null) this.string = s = toString0();
        return s;
    }

    @Override
    public final void toString(final StringBuilder sb) {
        sb.append(toString());
    }

    private String toString0() {
        if (this.count == 0) return "-";
        if (this.count == 1) return SimpleIntegerFormat.DECIMAL.toString(this.first.number);
        if (this.count == 12) return "*";
        if (this.count == this.last.number - this.first.number + 1) {
            return Strings.concat(SimpleIntegerFormat.DECIMAL.toString(this.first.number), "-", SimpleIntegerFormat.DECIMAL.toString(this.last.number)).intern();
        }
        final Month[] a = toArray(new Month[this.count]);
        final StringBuilder sb = new StringBuilder(this.count * 4);
        sb.append(this.first.number);
        boolean range = false;
        for (int i = 1, hi = this.count - 1; i <= hi; i++) {
            final Month m = a[i];
            if (range) {
                if (m.prevInSameYear() != a[i - 1]) {
                    range = false;
                    sb.append('-').append(a[i - 1].number).append(',').append(m.number);
                }
            } else if (m.prevInSameYear() == a[i - 1]) {
                range = true;
                if (i == hi) sb.append('-').append(m.number);
            } else {
                sb.append(',').append(m.number);
            }
        }
        return sb.toString().intern();
    }

    private static final class IteratorImpl extends Object implements Iterator<Month> {

        private final int bits;

        private Month next;

        IteratorImpl(final Months src) {
            super();
            this.bits = src.bits;
            this.next = src.first;
        }

        @Override
        public final boolean hasNext() {
            return this.next != null;
        }

        @Override
        public final Month next() {
            final Month next = this.next;
            if (next == null) throw new NoSuchElementException();
            Month m = next;
            do m = m.nextInSameYear(); while ((m != null) && ((this.bits & m.bit) == 0));
            this.next = m;
            return next;
        }

        @Override
        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
