package org.makumba.list.html;

import java.util.Dictionary;
import org.makumba.HtmlUtils;
import org.makumba.commons.formatters.FieldFormatter;
import org.makumba.commons.formatters.RecordFormatter;

/** Default HTML formatting of fields */
public class FieldViewer extends FieldFormatter {

    private static final class SingletonHolder {

        static final FieldFormatter singleton = new FieldViewer();
    }

    /** Don't use this, use getInstance() */
    protected FieldViewer() {
    }

    public static FieldFormatter getInstance() {
        return SingletonHolder.singleton;
    }

    static final String defaultEllipsis = "...";

    /**
	 * Returns a substring of maximum length by cutting at the end; if cut, an
	 * ellipsis is added on the end. Note: uses only J2 1.3 supported functions.
	 * An ellipsis is 3 dots (...) TODO:add support for fixedLength=N and
	 * fixedLengthAlign=left|center|right, fixedLengthPadChar='.'
	 * 
	 * @param s
	 *            string to format
	 * @param formatParams
	 *            formatting parameters
	 */
    public String formatMaxLengthEllipsis(RecordFormatter rf, int fieldIndex, String s, Dictionary formatParams) {
        String prefix = "";
        String postfix = "";
        String sOut = s;
        int maxLen = getIntParam(rf, fieldIndex, formatParams, "maxLength");
        String ellipsis = (String) formatParams.get("ellipsis");
        if (ellipsis == null) ellipsis = defaultEllipsis;
        int ellipsisLen = getIntParam(rf, fieldIndex, formatParams, "ellipsisLength");
        if (ellipsisLen == -1) ellipsisLen = ellipsis.length();
        String addTitle = (String) formatParams.get("addTitle");
        if (addTitle == null) addTitle = "false";
        if (maxLen != -1 && s.length() > maxLen) {
            int cutAt = maxLen - ellipsisLen;
            if (cutAt < 0) cutAt = 0;
            sOut = sOut.substring(0, cutAt);
            postfix = ellipsis;
        }
        if (addTitle.equals("true") || (addTitle.equals("auto") && maxLen != -1 && s.length() > maxLen)) {
            prefix = "<span title=\"" + s.replace('\"', '\'') + "\">";
            postfix = postfix + "</span>";
        }
        return prefix + HtmlUtils.string2html(sOut) + postfix;
    }
}
