package net.sf.rcpforms.formatter;

import java.util.Locale;
import org.eclipse.nebula.widgets.formattedtext.DateTimeFormatter;

/**
 * This class provides formatting of <code>Date</code> values in a
 * <code>FormattedText</code>. Supports a subset of date and time patterns
 * defined in <code>SimpleDateFormat</code> for input.
 * <p>
 * 
 * <h4>Edit Patterns</h4>
 * Edit patterns are composed of letters defining the parts of the mask, and
 * characters defining the separators.
 * <p>
 * The following pattern letters are defined: <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 * <tr bgcolor="#ccccff">
 * <th align=left>Letter</th>
 * <th align=left>Date or Time Part</th>
 * <th align=left>Examples</th>
 * </tr>
 * <tr>
 * <td><code>y</code></td>
 * <td>Year</td>
 * <td><code>2006</code>; <code>06</code></td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>M</code></td>
 * <td>Month in year</td>
 * <td><code>07</code></td>
 * </tr>
 * <tr>
 * <td><code>d</code></td>
 * <td>Day in month</td>
 * <td><code>10</code></td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>H</code></td>
 * <td>Hour in day (0-23)</td>
 * <td><code>0</code></td>
 * </tr>
 * <tr>
 * <td><code>h</code></td>
 * <td>Hour in am/pm (1-12)</td>
 * <td><code>12</code></td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>m</code></td>
 * <td>Minute in hour</td>
 * <td><code>30</code></td>
 * </tr>
 * <tr>
 * <td><code>s</code></td>
 * <td>Second in minute</td>
 * <td><code>55</code></td>
 * </tr>
 * <tr bgcolor="#eeeeff">
 * <td><code>S</code></td>
 * <td>Millisecond</td>
 * <td><code>978</code></td>
 * </tr>
 * <tr>
 * <td><code>a</code></td>
 * <td>Am/pm marker</td>
 * <td><code>PM</code></td>
 * </tr>
 * </table>
 * </blockquote> Edit patterns are limited to numeric formats (except am/pm
 * marker). Variable length fields and separators composed of more than one
 * character are supported for input.
 * 
 * <h4>Display Patterns</h4>
 * Display patterns are associated to a <code>SimpleDateFormat</code> object. So
 * they have to be compatible with it.
 * 
 * <h4>Examples</h4>
 * <ul>
 * <li><code>new DateTimeFormatter("MM/dd/yyyy")</code> - 8 jul 2006 will edit
 * and display as "07/08/2006".</li>
 * <li><code>new DateTimeFormatter("d/M/yyyy H:m, "dd MMM yyyy HH:mm")</code>- 8 jul
 * 2006, 15:05 will edit as "8/7/2006 15:5" and display as "08 Jul 2006 15:05".</li>
 * </ul>
 */
public class RCPDateTimeFormatter extends DateTimeFormatter {

    /**
	 * Constructs a new instance with all defaults :
	 * <ul>
	 * <li>edit mask in SHORT format for both date and time parts for the
	 * default locale</li>
	 * <li>display mask identical to the edit mask</li>
	 * <li>default locale</li>
	 * </ul>
	 */
    public RCPDateTimeFormatter() {
        super(null, null, Locale.getDefault());
    }

    /**
	 * Constructs a new instance with default edit and display masks for the
	 * given locale.
	 * 
	 * @param loc
	 *            locale
	 */
    public RCPDateTimeFormatter(Locale loc) {
        super(null, null, loc);
    }

    /**
	 * Constructs a new instance with the given edit mask. Display mask is
	 * identical to the edit mask, and locale is the default one.
	 * 
	 * @param editPattern
	 *            edit mask
	 */
    public RCPDateTimeFormatter(String editPattern) {
        super(editPattern, null, Locale.getDefault());
    }

    /**
	 * Constructs a new instance with the given edit mask and locale. Display
	 * mask is identical to the edit mask.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param loc
	 *            locale
	 */
    public RCPDateTimeFormatter(String editPattern, Locale loc) {
        super(editPattern, null, loc);
    }

    /**
	 * Constructs a new instance with the given edit and display masks. Uses the
	 * default locale.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param displayPattern
	 *            display mask
	 */
    public RCPDateTimeFormatter(String editPattern, String displayPattern) {
        super(editPattern, displayPattern, Locale.getDefault());
    }

    /**
	 * Constructs a new instance with the given masks and locale.
	 * 
	 * @param editPattern
	 *            edit mask
	 * @param displayPattern
	 *            display mask
	 * @param loc
	 *            locale
	 */
    public RCPDateTimeFormatter(String editPattern, String displayPattern, Locale loc) {
        super(editPattern, displayPattern, loc);
    }
}
