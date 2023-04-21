package yaw.cjef.util;

import java.util.regex.Pattern;

public class CJEFUtil {

    public static final Pattern SERIALVERSION_RE = Pattern.compile("private\\s+static\\s+final\\s+long\\s+serialVersionUID");

    public static final String SERIALVERSION_LINE = "  private static final long serialVersionUID = 1L;";

    public static final String NL = "\r\n";

    public static final String OVERRIDE = "@Override" + NL;
}
