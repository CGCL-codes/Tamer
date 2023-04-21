package au.edu.mq.comp.itec800.mgc.lib;

import au.edu.mq.comp.itec800.mgc.util.StringUtils;
import com.google.gdata.client.calendar.CalendarService;

/**
 * A extension class for the GData API's CalendarService.
 *
 * This class embodies the feature of the classic CalendarService class,
 * and also permits to an external program using the MGC Framework
 * to configure how the GData Java Client Library communicates with the
 * Google Calendar Service.
 *
 *
 * @see CalendarService
 *
 * @author Laurent Malvert [laurent.malvert@students.mq.edu.au]
 */
public class GCalendarService extends CalendarService {

    public static final String APPLICATION = "mgc";

    public static final String FRAMEWORK = "com.googlecode.com.mq-itec800-2008-1.mgc";

    public static final String VERSION = "0.3";

    public static final String REVISION = "$Revision: 573 $";

    private static String appName = "";

    public GCalendarService() {
        this(null);
    }

    public GCalendarService(final String appName) {
        super(version(GCalendarService.appName = StringUtils.clean(appName)));
    }

    /**
   * Returns a revision string with the following pattern:<BR />
   *  r&lt;revision&gt;<BR />
   * like:<BR />
   *  r567<BR />
   * where &lt;revision&gt; is being parsed from the REVISION static
   * variable (formatted as a SubVersioN revision keyword).
   *
   * If the REVISION public static variable isn't set, a default
   * value is used for parsing.
   *
   * @return
   *        the revision String object
   */
    public static String revision() {
        final String rev = (REVISION != null) ? REVISION : "_Revision: ??? $";
        return (("r" + rev.substring(11, rev.lastIndexOf("$"))).trim());
    }

    /**
   * Returns a version string with the following pattern:<BR />
   *  &lt;framwork&gt;~&lt;version&gt;.&lt;revision&gt;~&lt;application&gt;<BR />
   * like:<BR />
   *  mq-itec800-2008-1~mgc~0.1.r567<BR />
   *
   *
   * @return
   *        the version String object
   */
    private static String version(final String appName) {
        final StringBuffer version = new StringBuffer();
        final String cleanAppName = StringUtils.clean(appName);
        version.append(FRAMEWORK);
        version.append("~");
        version.append(VERSION);
        version.append(".");
        version.append(revision());
        version.append("~");
        version.append((cleanAppName.length() > 0) ? cleanAppName : APPLICATION);
        return (version.toString());
    }

    /**
   * Returns a version string with the following pattern:
   *  &lt;framwork&gt;~&lt;version&gt;.&lt;revision&gt;~&lt;application&gt;
   * like:
   *  mq-itec800-2008-1~mgc~0.1.r567
   *
   *
   * @return
   *        the version String object
   */
    public static String version() {
        return (version(getApplicationName()));
    }

    public static String getApplicationName() {
        return (appName);
    }

    public static void setApplicationName(final String appName) {
        GCalendarService.appName = StringUtils.clean(appName).replaceAll("-", "_").replaceAll(" ", "-");
    }
}
