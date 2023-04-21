package com.byterefinery.rmbench.external.model.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.byterefinery.rmbench.external.model.IDataType;

/**
 * SQL99: interval
 * 
 * @author sell
 */
public class Interval extends SizeScaleDataType {

    private static final Pattern WORD_PATTERN = Pattern.compile("interval\\s+(year|month|day|hour|minute|second)\\P{Alpha}*" + "(to\\s+(month|hour|minute|second))?\\P{Alpha}*");

    private static final Pattern SIZESCALE_PATTERN = Pattern.compile("\\(\\s*([0-9]+)\\s*(\\,?\\s*([0-9]+)\\s*)?\\)");

    private static final String INTERVAL = "INTERVAL";

    private static final String TO = " TO ";

    private static final String SECOND = "SECOND";

    private static final String MINUTE = "MINUTE";

    private static final String HOUR = "HOUR";

    private static final String DAY = "DAY";

    private static final String MONTH = "MONTH";

    private static final String YEAR = "YEAR";

    public static final Interval year() {
        return new Interval(YEAR, null);
    }

    public static Interval yearToMonth() {
        return new Interval(YEAR, MONTH);
    }

    public static Interval month() {
        return new Interval(MONTH, null);
    }

    public static Interval day() {
        return new Interval(DAY, null);
    }

    public static Interval hour() {
        return new Interval(HOUR, null);
    }

    public static Interval minute() {
        return new Interval(MINUTE, null);
    }

    public static Interval second() {
        return new Interval(SECOND, null);
    }

    public static Interval dayToHour() {
        return new Interval(DAY, HOUR);
    }

    public static Interval dayToMinute() {
        return new Interval(DAY, MINUTE);
    }

    public static Interval dayToSecond() {
        return new Interval(DAY, SECOND);
    }

    public static Interval hourToMinute() {
        return new Interval(HOUR, MINUTE);
    }

    public static Interval hourToSecond() {
        return new Interval(HOUR, SECOND);
    }

    public static Interval minuteToSecond() {
        return new Interval(MINUTE, SECOND);
    }

    private final String firstElement, toElement;

    protected Interval(String firstElement, String toElement) {
        super(createDisplayName(firstElement, toElement), IDataType.UNLIMITED_SIZE, false, IDataType.UNSPECIFIED_SIZE, IDataType.UNLIMITED_SCALE, false, IDataType.UNSPECIFIED_SCALE);
        this.firstElement = firstElement;
        this.toElement = toElement;
    }

    private static String[] createDisplayName(String element1, String element2) {
        String[] result = new String[1];
        result[0] = element2 == null ? INTERVAL + " " + element1 : INTERVAL + " " + element1 + TO + element2;
        return result;
    }

    public IDataType concreteInstance() {
        return new Interval(firstElement, toElement);
    }

    /**
     * parse the given string into an interval value. The input string is expected to conform
     * to the standard SQL99 syntax for interval values. The parsing is not guaranteed to reject
     * invalid syntax.<p/>
     * Non-mutable values (e.g. <code>maxSize</code>) are copied from this object. 
     * 
     * @param name an interval conforming to the SQL99 interval syntax
     * @return an interval value, or <code>null</code> if the input does not start with 
     * the <code>interval</code> keyword
     * @throws IllegalArgumentException if the syntax is found to be invalid 
     */
    public Interval parse(String name) {
        name = name.toLowerCase();
        Matcher matcher = WORD_PATTERN.matcher(name);
        if (!matcher.matches()) return null;
        String firstElement = matcher.group(1).toUpperCase();
        String secondElement = matcher.group(3);
        if (secondElement != null) secondElement = secondElement.toUpperCase();
        long precision = IDataType.UNSPECIFIED_SIZE;
        int secondsPrecision = IDataType.UNSPECIFIED_SCALE;
        matcher = SIZESCALE_PATTERN.matcher(name);
        int secondNumberStart = 0;
        if (matcher.find()) {
            String sizeString = matcher.group(1);
            precision = Long.parseLong(sizeString);
            String scaleString = matcher.group(3);
            if (scaleString != null) {
                try {
                    secondsPrecision = Integer.parseInt(scaleString);
                } catch (NumberFormatException x) {
                    throw new IllegalArgumentException("invalid syntax: " + name);
                }
            }
            secondNumberStart = matcher.end();
        }
        if (secondsPrecision == IDataType.UNSPECIFIED_SCALE) {
            long num = parseNumber(name, secondNumberStart);
            if (num != IDataType.UNSPECIFIED_SIZE) secondsPrecision = (int) num;
        }
        Interval result = new Interval(firstElement, secondElement);
        result.setSize(precision);
        result.setScale(secondsPrecision);
        return result;
    }

    public String getDDLName() {
        if (toElement == null) return super.getDDLName();
        StringBuffer buf = new StringBuffer(INTERVAL);
        buf.append(" ");
        buf.append(firstElement);
        if (size != IDataType.UNSPECIFIED_SIZE) {
            buf.append(" (");
            buf.append(String.valueOf(size));
            buf.append(")");
        }
        buf.append(TO);
        buf.append(toElement);
        if (scale != IDataType.UNSPECIFIED_SCALE) {
            buf.append(" (");
            buf.append(String.valueOf(scale));
            buf.append(")");
        }
        return buf.toString();
    }
}
