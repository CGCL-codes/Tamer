package raptor.util;

import java.util.regex.Pattern;
import raptor.international.L10n;
import raptor.util.RaptorLogger;

public class RegExUtils {

    private static final RaptorLogger LOG = RaptorLogger.getLog(RegExUtils.class);

    public static Pattern getPattern(String regularExpression) {
        try {
            return Pattern.compile(regularExpression, Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        } catch (Throwable t) {
            LOG.warn("RegularExpression pattern creation threw an exception. regex=" + regularExpression, t);
            return null;
        }
    }

    public static String getRegularExpressionHelpHtml() {
        return L10n.getInstance().getString("regexHelp");
    }

    public static boolean matches(Pattern pattern, String stringToTest) {
        try {
            return pattern.matcher(stringToTest).matches();
        } catch (Throwable t) {
            LOG.warn("matches threw exception. regex=" + pattern.pattern() + " test=" + stringToTest, t);
            return false;
        }
    }

    public static boolean matches(String regularExpression, String stringToTest) {
        try {
            return getPattern(regularExpression).matcher(stringToTest).matches();
        } catch (Throwable t) {
            LOG.warn("matches threw exception. regex=" + regularExpression + " test=" + stringToTest, t);
            return false;
        }
    }
}
