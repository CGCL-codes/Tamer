package org.radeox.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.LocaleRegexTokenFilter;
import org.radeox.regex.MatchResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ListFilter extends LocaleRegexTokenFilter implements CacheFilter {

    private static Log log = LogFactory.getLog(ListFilter.class);

    private static final Map openList = new HashMap();

    private static final Map closeList = new HashMap();

    private static final String UL_CLOSE = "</ul>";

    private static final String OL_CLOSE = "</ol>";

    protected String getLocaleKey() {
        return "filter.list";
    }

    protected boolean isSingleLine() {
        return false;
    }

    public ListFilter() {
        super();
        openList.put(new Character('-'), "<ul class=\"minus\">");
        openList.put(new Character('*'), "<ul class=\"star\">");
        openList.put(new Character('#'), "<ol>");
        openList.put(new Character('i'), "<ol class=\"roman\">");
        openList.put(new Character('I'), "<ol class=\"ROMAN\">");
        openList.put(new Character('a'), "<ol class=\"alpha\">");
        openList.put(new Character('A'), "<ol class=\"ALPHA\">");
        openList.put(new Character('g'), "<ol class=\"greek\">");
        openList.put(new Character('h'), "<ol class=\"hiragana\">");
        openList.put(new Character('H'), "<ol class=\"HIRAGANA\">");
        openList.put(new Character('k'), "<ol class=\"katakana\">");
        openList.put(new Character('K'), "<ol class=\"KATAKANA\">");
        openList.put(new Character('j'), "<ol class=\"HEBREW\">");
        openList.put(new Character('1'), "<ol>");
        closeList.put(new Character('-'), UL_CLOSE);
        closeList.put(new Character('*'), UL_CLOSE);
        closeList.put(new Character('#'), OL_CLOSE);
        closeList.put(new Character('i'), OL_CLOSE);
        closeList.put(new Character('I'), OL_CLOSE);
        closeList.put(new Character('a'), OL_CLOSE);
        closeList.put(new Character('A'), OL_CLOSE);
        closeList.put(new Character('1'), OL_CLOSE);
        closeList.put(new Character('g'), OL_CLOSE);
        closeList.put(new Character('G'), OL_CLOSE);
        closeList.put(new Character('h'), OL_CLOSE);
        closeList.put(new Character('H'), OL_CLOSE);
        closeList.put(new Character('k'), OL_CLOSE);
        closeList.put(new Character('K'), OL_CLOSE);
        closeList.put(new Character('j'), OL_CLOSE);
    }

    ;

    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(result.group(0)));
            addList(buffer, reader);
        } catch (Exception e) {
            log.warn("ListFilter: unable get list content", e);
        }
    }

    /**
   * Adds a list to a buffer
   *
   * @param buffer The buffer to write to
   * @param reader Input is read from this Reader
   */
    private void addList(StringBuffer buffer, BufferedReader reader) throws IOException {
        char[] lastBullet = new char[0];
        String line = null;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                continue;
            }
            int bulletEnd = line.indexOf(' ');
            if (bulletEnd < 1) {
                continue;
            }
            if (line.charAt(bulletEnd - 1) == '.') {
                bulletEnd--;
            }
            char[] bullet = line.substring(0, bulletEnd).toCharArray();
            int sharedPrefixEnd;
            for (sharedPrefixEnd = 0; ; sharedPrefixEnd++) {
                if (bullet.length <= sharedPrefixEnd || lastBullet.length <= sharedPrefixEnd || +bullet[sharedPrefixEnd] != lastBullet[sharedPrefixEnd]) {
                    break;
                }
            }
            for (int i = sharedPrefixEnd; i < lastBullet.length; i++) {
                buffer.append(closeList.get(new Character(lastBullet[i]))).append("\n");
            }
            for (int i = sharedPrefixEnd; i < bullet.length; i++) {
                buffer.append(openList.get(new Character(bullet[i]))).append("\n");
            }
            buffer.append("<li>");
            buffer.append(line.substring(line.indexOf(' ') + 1));
            buffer.append("</li>\n");
            lastBullet = bullet;
        }
        for (int i = lastBullet.length - 1; i >= 0; i--) {
            buffer.append(closeList.get(new Character(lastBullet[i])));
        }
    }
}
