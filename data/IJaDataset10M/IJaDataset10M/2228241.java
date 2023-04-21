package net.sf.solarnetwork.util;

import java.beans.PropertyEditorSupport;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * PropertyEditor using Joda Time's DateTimeFormatter for thread-safe date
 * parsing and formatting.
 * 
 * <p>This class has been designed with {@link CloningPropertyEditorRegistrar}
 * in mind, so that one instance of a {@link DateTimeFormatter} can be shared
 * between multiple threads to parse or format Joda date objects.</p>
 * 
 * <p>The configurable properties of this class are:</p>
 * 
 * <dl class="class-properties">
 *   <dt>datePattern</dt>
 *   <dd>The date pattern format string to use for parsing / formatting dates.
 *   See the {@link org.joda.time.format.DateTimeFormat} JavaDocs for 
 *   information on how the patterns work. Defaults to {@code yyyy-MM-dd}.</dd>
 *   
 *   <dt>timeZone</dt>
 *   <dd>The TimeZone to use for parsing/formatting all dates. Defaults to 
 *   the platform's default time zone.</dd>
 *   
 *   <dt>parseMode</dt>
 *   <dd>A mode flag for handling different Joda date instance types.</dd>
 *   
 * </dl>
 *
 * @author matt
 * @version $Revision: 369 $ $Date: 2009-09-24 02:24:47 -0400 (Thu, 24 Sep 2009) $
 */
public class JodaDateFormatEditor extends PropertyEditorSupport implements Cloneable {

    /**
	 * A mode flag to determine the type of dates to parse.
	 */
    public enum ParseMode {

        /** Parse text into DateTime objects. */
        DateTime, /** Parse text into LocalDate objects. */
        LocalDate, /** Parse text into LocalTime objects. */
        LocalTime
    }

    private TimeZone timeZone = TimeZone.getDefault();

    private String[] datePatterns = new String[] { "yyyy-MM-dd" };

    private ParseMode parseMode = ParseMode.DateTime;

    private DateTimeFormatter[] dateFormatters = null;

    /**
	 * Default constructor.
	 */
    public JodaDateFormatEditor() {
        super();
    }

    /**
	 * Construct from a single date pattern value in the default time zone.
	 * 
	 * <p>This will also call the {@link #init()} method.</p>
	 * 
	 * @param datePattern the date pattern
	 */
    public JodaDateFormatEditor(String datePattern) {
        this(datePattern, ParseMode.DateTime);
    }

    /**
	 * Construct from a single date pattern value in the default time zone.
	 * 
	 * <p>This will also call the {@link #init()} method.</p>
	 * 
	 * @param datePattern the date pattern
	 * @param parseMode the specific parse mode to use
	 */
    public JodaDateFormatEditor(String datePattern, ParseMode parseMode) {
        this(datePattern, parseMode == ParseMode.DateTime ? TimeZone.getDefault() : null);
        this.parseMode = parseMode;
    }

    /**
	 * Construct from a single date pattern value.
	 * 
	 * <p>This will also call the {@link #init()} method.</p>
	 * 
	 * @param datePattern the date pattern
	 * @param timeZone the time zone
	 */
    public JodaDateFormatEditor(String datePattern, TimeZone timeZone) {
        this(new String[] { datePattern }, timeZone);
    }

    /**
	 * Construct from multiple date pattern values.
	 * 
	 * <p>This will also call the {@link #init()} method.</p>
	 * 
	 * @param datePatterns the date patterns
	 * @param timeZone the time zone
	 */
    public JodaDateFormatEditor(String[] datePatterns, TimeZone timeZone) {
        super();
        this.datePatterns = datePatterns;
        this.timeZone = timeZone;
        init();
    }

    /**
	 * Initialize after properties set.
	 */
    public void init() {
        this.dateFormatters = new DateTimeFormatter[this.datePatterns.length];
        for (int i = 0; i < this.dateFormatters.length; i++) {
            this.dateFormatters[i] = DateTimeFormat.forPattern(datePatterns[i]);
            if (timeZone != null) {
                this.dateFormatters[i] = dateFormatters[i].withZone(DateTimeZone.forTimeZone(timeZone));
            }
        }
    }

    @Override
    public String getAsText() {
        Object val = getValue();
        if (val == null) {
            return null;
        }
        DateTimeFormatter format = this.dateFormatters[0];
        if (val instanceof ReadableInstant) {
            return format.print((ReadableInstant) val);
        } else if (val instanceof ReadablePartial) {
            return format.print((ReadablePartial) val);
        } else if (val instanceof Date) {
            return format.print(((Date) val).getTime());
        } else if (val instanceof Calendar) {
            return format.print(((Calendar) val).getTimeInMillis());
        }
        throw new IllegalArgumentException("Unsupported date object [" + val.getClass() + "]: " + val);
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        IllegalArgumentException iae = null;
        for (DateTimeFormatter df : this.dateFormatters) {
            try {
                DateTime dt = df.parseDateTime(text);
                Object val = dt;
                switch(this.parseMode) {
                    case LocalDate:
                        val = dt.toLocalDate();
                        break;
                    case LocalTime:
                        val = dt.toLocalTime();
                        break;
                    default:
                }
                setValue(val);
                iae = null;
                break;
            } catch (IllegalArgumentException e) {
                iae = e;
            }
        }
        if (iae != null) {
            throw iae;
        }
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Get the first date pattern.
	 * 
	 * @return the datePattern
	 */
    public String getDatePattern() {
        if (datePatterns == null || datePatterns.length < 1) {
            return null;
        }
        return datePatterns[0];
    }

    /**
	 * Set a single date pattern.
	 * 
	 * @param datePattern the datePattern to set
	 */
    public void setDatePattern(String datePattern) {
        this.datePatterns = new String[] { datePattern };
    }

    /**
	 * @return the timeZone
	 */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
	 * @param timeZone the timeZone to set
	 */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    /**
	 * @return the datePatterns
	 */
    public String[] getDatePatterns() {
        return datePatterns;
    }

    /**
	 * @param datePatterns the datePatterns to set
	 */
    public void setDatePatterns(String[] datePatterns) {
        this.datePatterns = datePatterns;
    }

    /**
	 * @return the parseMode
	 */
    public ParseMode getParseMode() {
        return parseMode;
    }

    /**
	 * @param parseMode the parseMode to set
	 */
    public void setParseMode(ParseMode parseMode) {
        this.parseMode = parseMode;
    }
}
