package com.liferay.util;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <a href="XSSUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 * @author Clarence Shen
 *
 */
public class XSSUtil {

    public static final String XSS_REGEXP_PATTERN = GetterUtil.getString(SystemProperties.get(XSSUtil.class.getName() + ".regexp.pattern"));

    public static final Pattern XSS_PATTERN = Pattern.compile(XSS_REGEXP_PATTERN);

    public static String strip(String text) {
        if (text == null) {
            return null;
        }
        CharSequence sequence = text.subSequence(0, text.length());
        Matcher matcher = XSS_PATTERN.matcher(sequence);
        return matcher.replaceAll(StringPool.BLANK);
    }
}
