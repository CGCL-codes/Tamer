package com.generalynx.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ChoiceFormat;
import java.text.Collator;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import com.generalynx.common.resources.Resources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tools {

    protected static final Log logger = LogFactory.getLog(Tools.class);

    public static DecimalFormat INT_DOT_FORMAT;

    private static final String COMMA = ", ";

    private static InheritableThreadLocal m_threadLocalHttpSessionId;

    private static Map m_webSessionIdMap;

    private static final NumberFormat[] FORMAT = { new DecimalFormat("#,##0"), new DecimalFormat("#,##0.0"), new DecimalFormat("#,##0.00"), new DecimalFormat("#,##0.000"), new DecimalFormat("#,##0.0000"), new DecimalFormat("#,##0.00000"), new DecimalFormat("#,##0.000000") };

    public static final String DEFAULT_BOOLEAN_FORMAT = "0#OFF|1#ON";

    private static final Collator COLLATOR_LOCAL = Collator.getInstance();

    public static final LocallyComparator COMPARATOR_LOCAL = new LocallyComparator();

    static {
        m_threadLocalHttpSessionId = new InheritableThreadLocal();
        m_webSessionIdMap = new HashMap();
    }

    public static void registerCurrentThreadHttpSession(String httpSessionId) {
        m_threadLocalHttpSessionId.set(httpSessionId);
    }

    public static void unregisterCurrentThreadHttpSession() {
        m_threadLocalHttpSessionId.set(null);
    }

    public static int getSessionId() {
        Integer sessionId = (Integer) m_webSessionIdMap.get(m_threadLocalHttpSessionId.get());
        return sessionId != null ? sessionId.intValue() : 0;
    }

    public static void setSessionId(int nSessionId) {
        m_webSessionIdMap.put(m_threadLocalHttpSessionId.get(), new Integer(nSessionId));
    }

    public static Object getMappedValueFromList(Object key, List mappedElementsList) {
        Map map = new HashMap();
        Iterator iter = mappedElementsList.iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            map.put(entry.getKey(), entry.getValue());
        }
        return map.get(key);
    }

    private static Map m_progressMap;

    public static int getProgress() {
        try {
            Integer progress = (Integer) m_progressMap.get(m_threadLocalHttpSessionId.get());
            return progress != null ? progress.intValue() : 0;
        } catch (Throwable t) {
            return 0;
        }
    }

    public static void setProgress(int progress) {
        try {
            if (m_progressMap == null) {
                m_progressMap = new HashMap();
            }
            m_progressMap.put(m_threadLocalHttpSessionId.get(), new Integer(progress));
        } catch (Throwable t) {
        }
    }

    public static void clearProgress() {
        if (m_progressMap == null) return;
        try {
            m_progressMap.remove(m_threadLocalHttpSessionId.get());
        } catch (Throwable t) {
        }
    }

    /**
     * @param s
     * @return non-null string
     */
    public static String createString(String s) {
        return (s != null) ? s : "";
    }

    public static String right(String str, int len) {
        if (len < 0) throw new IllegalArgumentException("Requested String length " + len + " is less than zero");
        if (str == null || str.length() <= len) return str; else return str.substring(str.length() - len);
    }

    public static void trace(String info) {
        System.out.println("-----------------------------------");
        System.out.println(info);
        System.out.println("-----------------------------------");
    }

    public static String toStringTime(long time_millis) {
        return toStringTime(time_millis, false);
    }

    public static String toStringTime(long time_millis, boolean showMillis) {
        StringBuffer buf = new StringBuffer();
        boolean set = false;
        if (set || time_millis / Const.MILLIS_PER_DAY > 0) {
            buf.append((time_millis / Const.MILLIS_PER_DAY) + "day ");
            time_millis %= Const.MILLIS_PER_DAY;
            set = true;
        }
        if (set || time_millis / Const.MILLIS_PER_HOUR > 0) {
            buf.append((time_millis / Const.MILLIS_PER_HOUR) + "h ");
            time_millis %= Const.MILLIS_PER_HOUR;
            set = true;
        }
        if (set || time_millis / Const.MILLIS_PER_MINUTE > 0) {
            buf.append((time_millis / Const.MILLIS_PER_MINUTE) + "min ");
            time_millis %= Const.MILLIS_PER_MINUTE;
            set = true;
        }
        if (set || time_millis / Const.MILLIS_PER_SECOND > 0) {
            buf.append((time_millis / Const.MILLIS_PER_SECOND) + "s");
            time_millis %= Const.MILLIS_PER_SECOND;
            set = true;
        }
        if (!set || showMillis) {
            buf.append(time_millis + "ms");
        }
        return buf.toString();
    }

    public static int diffInDays(Date from, Date till) {
        if (from == null || till == null) return 0;
        long ft = from.getTime();
        long tt = till.getTime();
        return (int) ((tt - ft) / Const.MILLIS_PER_DAY);
    }

    public static int diffInDays(DateData from, DateData till) {
        if (from == null || till == null) return 0;
        long ft = from.getDate().getTime();
        long tt = till.getDate().getTime();
        return (int) ((tt - ft) / Const.MILLIS_PER_DAY);
    }

    /**
     * puts prefix infront of methodName ... first letter of method name is capitalized
     *
     * @param prefix
     * @param methodName
     * @return prefix + MethodName
     */
    public static String nameMethod(String prefix, String methodName) {
        return nameMethod(prefix, methodName, "");
    }

    /**
     * puts prefix infront of methodName and suffix after ... first letter of method name is capitalized
     *
     * @param prefix
     * @param methodName
     * @param suffix
     * @return prefix + MethodName + suffix
     */
    public static String nameMethod(String prefix, String methodName, String suffix) {
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        if (methodName == null || methodName.length() == 0) return "doDefault";
        return (prefix + capitalize(methodName) + suffix);
    }

    public static String capitalize(String value) {
        if (value == null) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }

    public static String strictCapitalize(String value) {
        if (value == null) {
            return value;
        }
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
    }

    public static Timestamp toSQL(Date date) {
        return new java.sql.Timestamp(date.getTime());
    }

    public static String commaSeparate(int[] array) {
        return commaSeparate(array, false);
    }

    public static String commaSeparate(int[] array, boolean par) {
        IntegerArray integerArray = new IntegerArray();
        integerArray.addAll(array);
        return commaSeparate(integerArray.getIntegerList(), par, "(", ")");
    }

    public static String commaSeparate(Collection collection) {
        return commaSeparate(collection, false);
    }

    public static String implode(Collection collection, String separator) {
        return implode(collection, separator, false);
    }

    public static String implode(Collection collection, String separator, boolean par) {
        return implode(collection, separator, par, null, null);
    }

    public static String commaSeparate(Collection collection, boolean par) {
        return implode(collection, COMMA, par, null, null);
    }

    public static String implode(Collection collection, String separator, boolean par, String prefix, String sufix) {
        return implode(collection.toArray(), separator, par, prefix, sufix);
    }

    public static String commaSeparate(Collection collection, boolean par, String prefix, String sufix) {
        return implode(collection.toArray(), COMMA, par, prefix, sufix);
    }

    public static String commaSeparate(Object[] array, boolean par, String prefix, String sufix) {
        return implode(array, COMMA, par, prefix, sufix);
    }

    public static String implode(Object[] array, String separator, boolean par, String prefix, String sufix) {
        return implode(array, separator, par, prefix, sufix, DefaultValueFormat.INSTANCE);
    }

    public static String implode(Object[] array, String separator, boolean par, String prefix, String sufix, ValueFormat vf) {
        boolean first = true;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            if (!first) buf.append(separator);
            if (first) {
                if (par && prefix != null) buf.append(prefix);
                first = false;
            }
            buf.append(vf.toString(array[i]));
        }
        if (!first && par && sufix != null) buf.append(sufix);
        return buf.toString();
    }

    /**
     * wraps string but only in space char
     *
     * @param s             to be wrapped
     * @param maxLineLength
     * @return wrapped String
     */
    public static String wrap(String s, int maxLineLength) {
        int[] indexes = indexesOf(s, ' ');
        char[] chars = s.toCharArray();
        int lastGoodIndex = 0;
        int inLine = 0;
        for (int i = 0; i < indexes.length; i++) {
            int index = indexes[i];
            inLine += index - lastGoodIndex;
            if (inLine > maxLineLength) {
                inLine = 0;
                if (lastGoodIndex == 0) {
                    chars[index] = '\n';
                } else {
                    chars[lastGoodIndex] = '\n';
                    lastGoodIndex = 0;
                }
                continue;
            }
            lastGoodIndex = index;
        }
        if (inLine + (s.length() - lastGoodIndex + 1) > maxLineLength) {
            chars[lastGoodIndex] = '\n';
        }
        return new String(chars);
    }

    public static int[] indexesOf(String s, char c) {
        List indexes = new ArrayList();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (aChar == c) {
                indexes.add(new Integer(i));
            }
        }
        return CollectionUtils.asArray(indexes);
    }

    /**
     * Compares strings locally.
     *
     * @see Collator
     */
    public static int locallyCompare(String source, String target) {
        return COLLATOR_LOCAL.compare(source, target);
    }

    private static final class LocallyComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            return COLLATOR_LOCAL.compare(o1, o2);
        }
    }

    public static String removeCaron(String s) {
        final String BAD_CHARS = "čè�";
        final String GOOD_CHARS = "ccdsz";
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int ix = BAD_CHARS.indexOf(c);
            if (ix > -1) {
                sb.append(GOOD_CHARS.charAt(ix));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getImgLabel(String imgSrc, String alt) {
        StringBuffer buffer = new StringBuffer("<img ");
        buffer.append("src=\"" + imgSrc + "\" ");
        buffer.append("alt=\"" + alt + "\" ");
        buffer.append("/>");
        return buffer.toString();
    }

    private static final String timePattern = "HH:mm";

    public static DateFormat time() {
        return new SimpleDateFormat(timePattern);
    }

    private static final String sloOnlyDatePattern = "dd.MM.yyyy";

    public static DateFormat sloOnlyDate() {
        return new SimpleDateFormat(sloOnlyDatePattern);
    }

    private static final String sloDatePattern = "dd.MM.yyyy HH:mm";

    public static final DateFormat sloDate() {
        return new SimpleDateFormat(sloDatePattern);
    }

    private static final String sloDateTimeExactPattern = "dd.MM.yyyy HH:mm:ss";

    public static final DateFormat sloDateTimeExact() {
        return new SimpleDateFormat(sloDateTimeExactPattern);
    }

    private static final String engOnlyDatePattern = "yyyy-MM-dd";

    public static final DateFormat engOnlyDate() {
        return new SimpleDateFormat(engOnlyDatePattern);
    }

    private static final String engDatePattern = "yyyy-MM-dd HH:mm";

    public static final DateFormat engDate() {
        return new SimpleDateFormat(engDatePattern);
    }

    private static DateFormat utcDate() {
        DateFormat dateFormat = engDate();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }

    private static final String[] c_sloMonthNames = { "januar", "februar", "marec", "april", "maj", "junij", "julij", "avgust", "september", "oktober", "november", "december" };

    private static Date FUTURE;

    public static Date getFuture() {
        if (FUTURE == null) {
            try {
                FUTURE = sloOnlyDate().parse("1.1.2100");
            } catch (ParseException e) {
                logger.fatal(e.getMessage());
            }
        }
        return FUTURE;
    }

    public static Date parseDate(String date) {
        try {
            return sloDate().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseOnlyDate(String date) throws ParseException {
        return sloOnlyDate().parse(date);
    }

    public static Date forceParse(String sDate, DateFormat[] formats) throws ParseException {
        Date date = null;
        for (int i = 0; i < formats.length; i++) {
            try {
                date = formats[i].parse(sDate);
                return date;
            } catch (ParseException e) {
            }
        }
        throw new ParseException(sDate, 0);
    }

    public static int week_of_year(int day, int year) {
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try {
            calendar.setTime(df.parse("01.01." + year));
        } catch (ParseException e) {
            logger.error("Exception parsing date: " + e.getMessage());
        }
        int jan1DayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int diff = 0;
        if (jan1DayWeek == Calendar.SUNDAY) diff = 1; else if (jan1DayWeek > Calendar.MONDAY) {
            diff = 7 + Calendar.MONDAY - jan1DayWeek;
        }
        return ((day - diff) / 7 + (diff > 0 && (day > diff) ? 1 : 0) + 1);
    }

    public static Date now(int days) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, days);
        return now.getTime();
    }

    public static String getTime(Date date) {
        return time().format(date);
    }

    public static String getDate(Date date) {
        return sloDate().format(date);
    }

    public static String getDateOnly(Date date) {
        return sloOnlyDate().format(date);
    }

    public static Date getDateOnlyObject(Date date) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(date);
        clipToDate(newCalendar);
        return newCalendar.getTime();
    }

    public static int getHour(Date date) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(date);
        return newCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Calendar getGasDate(Date date) {
        Calendar gasDateNow = Calendar.getInstance();
        gasDateNow.setTime(date);
        boolean isPrevGasDay = gasDateNow.get(Calendar.HOUR_OF_DAY) < 8;
        clipToDate(gasDateNow);
        if (isPrevGasDay) {
            gasDateNow.add(Calendar.DAY_OF_YEAR, -1);
        }
        return gasDateNow;
    }

    public static Integer getOlapUra(Date dateLocal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateLocal);
        Integer olapUraId = null;
        int timePastHour = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND);
        if (timePastHour == 0) {
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0) {
                olapUraId = new Integer(24);
            } else {
                olapUraId = new Integer(calendar.get(Calendar.HOUR_OF_DAY));
            }
        } else {
            olapUraId = new Integer(calendar.get(Calendar.HOUR_OF_DAY) + 1);
        }
        return olapUraId;
    }

    public static Date getOlapDate(Date dateLocal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateLocal);
        int timePastDay = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND);
        if (timePastDay > 0) {
            return getDateOnlyObject(dateLocal);
        } else {
            calendar.add(Calendar.DATE, -1);
            return calendar.getTime();
        }
    }

    private static NumberFormat getNumberFormat() {
        if (INT_DOT_FORMAT == null) {
            Locale locale = Locale.getDefault();
            logger.debug("Setting dot format, locale: " + locale);
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(locale);
            df.applyPattern("#,##0");
            INT_DOT_FORMAT = df;
        }
        return INT_DOT_FORMAT;
    }

    public static String dotFormat(int number) {
        return getNumberFormat().format(number);
    }

    public static String dotFormat(double number) {
        return getNumberFormat().format(number);
    }

    public static String dotFormat(int number, int N) {
        StringBuffer buf = new StringBuffer();
        boolean negative = (number < 0);
        if (negative) number = Math.abs(number);
        while (number > N - 1) {
            if (number / N > 0) {
                buf.insert(0, "." + prependZeros(number % N, N));
                number /= N;
            }
        }
        buf.insert(0, number);
        if (negative) buf.insert(0, "-");
        return buf.toString();
    }

    /**
     * Prepend zeros to match (log_10 N) size.
     *
     * @param number
     * @param N
     * @return number with (log_10 N) - 1 prepended zeros
     */
    public static String prependZeros(int number, int N) {
        String result = "";
        if (number > 0) result += number;
        N /= 10;
        while (N > number) {
            result = "0" + result;
            N /= 10;
        }
        return result;
    }

    /**
     * @param month: 1-based index of month
     * @return Slovene month name in lower-case letters
     * @deprecated Use Resources.getString("MONTH" + month) instead
     */
    public static String getSloMonthName(int month) {
        return c_sloMonthNames[month - 1];
    }

    /**
     * Sets property on object (uses reflection)
     *
     * @param obj      object on witch property will be set
     * @param property name of property (without set)
     * @param newValue
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setProperty(Object obj, String property, Object newValue) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class[] classes = new Class[] { newValue.getClass() };
        Method method = obj.getClass().getMethod(nameMethod("set", property), classes);
        method.invoke(obj, new Object[] { newValue });
    }

    /**
     * Discards time info (h, min, sec, millis) and only maintains date info (day, month, year).
     *
     * @param cal
     */
    public static void clipToDate(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static String formatDouble(double value, int digits) {
        return getDoubleFormat(digits).format(value);
    }

    public static NumberFormat getDoubleFormat(int digits) {
        if (digits < 0 || digits >= FORMAT.length) {
            throw new IndexOutOfBoundsException("No format for " + digits + " digits.");
        }
        return FORMAT[digits];
    }

    /**
     * @param date
     * @return UTC date string
     */
    public static String getUTCDate(Date date) {
        return utcDate().format(date);
    }

    /**
     * Interprets the given date string as UTC and returns corresponding Date object.
     *
     * @param date UTC string
     * @return date
     * @throws ParseException
     */
    public static Date parseUTCDate(String date) throws ParseException {
        return utcDate().parse(date);
    }

    /**
     * Compares two Comparables and checks for null values first.
     *
     * @param nullBottom specifies that null values are smaller than all other values.
     * @return negative value if o1 is smaller etc.
     */
    public static int nullSafeCompare(Comparable o1, Comparable o2, boolean nullBottom) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            }
            return nullBottom ? -1 : 1;
        }
        if (o2 == null) {
            return nullBottom ? 1 : -1;
        }
        return o1.compareTo(o2);
    }

    /**
     * Compares two Comparables and checks for null values first. Null values are considered
     * smaller than any other values.
     *
     * @return negative value if o1 is smaller etc.
     */
    public static int nullSafeCompare(Comparable o1, Comparable o2) {
        return nullSafeCompare(o1, o2, true);
    }

    public static String formatMessage(String key, Object[] arguments) {
        return MessageFormat.format(Resources.getString(key), arguments);
    }

    public static String formatMessage(String key, Object argument) {
        return formatMessage(key, new Object[] { argument });
    }

    /**
     * Returns a {@link NumberFormat}: either {@link ChoiceFormat} or {@link DecimalFormat}
     *
     * @param isBoolean if true, {@link ChoiceFormat} will be returned.
     * @param format    the format string. May be null: a default format will be returned then.
     */
    public static NumberFormat getNumberFormat(boolean isBoolean, String format) {
        NumberFormat numberFormat = null;
        if (isBoolean) {
            if (format != null) {
                try {
                    numberFormat = new ChoiceFormat(format);
                } catch (Exception e) {
                    logger.error("Illegal choice format: " + e + "; will return default boolean format.");
                }
            }
            if (numberFormat == null) {
                numberFormat = new ChoiceFormat(DEFAULT_BOOLEAN_FORMAT);
            }
        } else {
            if (format != null) {
                try {
                    numberFormat = new DecimalFormat(format);
                } catch (Exception e) {
                    logger.error("Illegal decimal format: " + e + "; will return default decimal format.");
                }
            }
            if (numberFormat == null) {
                numberFormat = getDoubleFormat(2);
            }
        }
        return numberFormat;
    }

    public static String formatWithToStringTransform(String pattern, Object[] args) {
        return formatWithToStringTransform(pattern, args, "");
    }

    public static String formatWithToStringTransform(String pattern, Object[] args, String nullReplacement) {
        Object[] toStringArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object obj = args[i];
            if (obj == null) {
                toStringArgs[i] = nullReplacement;
            } else if (obj instanceof Date) {
                toStringArgs[i] = ApplicationFactory.getComponentFactory().getDefaultDateTimeFormat().format(obj);
            } else {
                toStringArgs[i] = String.valueOf(obj);
            }
        }
        return MessageFormat.format(pattern, toStringArgs);
    }
}
