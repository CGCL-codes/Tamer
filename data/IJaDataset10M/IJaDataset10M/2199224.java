package calclipse.mcomp.trace;

import calclipse.Resource;

/**
 * Identifies a location in a script.
 * Used for formatting error messages.
 * Any of the properties stored in this class may be null.
 * @author T. Sommerland
 */
public class Trace {

    private static ErrorFormatter errorFormatter = ErrorFormatter.DEFAULT;

    private final String name;

    private final String url;

    private final Location location;

    private final Detail detail;

    public Trace(final String name, final String url, final Location location, final Detail detail) {
        this.name = name;
        this.url = url;
        this.location = location;
        this.detail = detail;
    }

    @Resource("calclipse.mcomp.trace.Trace.errorFormatter")
    public static void setErrorFormatter(final ErrorFormatter errorFormatter) {
        Trace.errorFormatter = errorFormatter;
    }

    public static ErrorFormatter getErrorFormatter() {
        return errorFormatter;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return url;
    }

    public Location getLocation() {
        return location;
    }

    public Detail getDetail() {
        return detail;
    }
}
