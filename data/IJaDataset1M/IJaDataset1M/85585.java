package com.sitescape.util.lucene;

import com.sitescape.util.StringPool;
import com.sitescape.util.StringUtil;

/**
 * <a href="KeywordsUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @author  Mirco Tamburini
 * @author  Josiah Goh
 * @version $Revision: 1.4 $
 *
 */
public class KeywordsUtil {

    public static final String[] SPECIAL = new String[] { "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "\\" };

    public static String escape(String text) {
        for (int i = SPECIAL.length - 1; i >= 0; i--) {
            text = StringUtil.replace(text, SPECIAL[i], StringPool.BACK_SLASH + SPECIAL[i]);
        }
        return text;
    }

    public static String toWildcard(String keywords) {
        if (keywords == null) {
            return null;
        }
        if (!keywords.endsWith(StringPool.STAR)) {
            keywords = keywords + StringPool.STAR;
        }
        return keywords;
    }
}
