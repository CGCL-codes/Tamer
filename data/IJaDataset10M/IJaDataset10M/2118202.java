package org.snipsnap.semanticweb.rss;

import org.radeox.util.logging.Logger;
import org.radeox.filter.regex.MatchResult;
import snipsnap.api.snip.Snip;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Split snips into sub-units for e.g. RSS. By default header titles
 * are used as seperators.
 *
 * @author Stephan J. Schmidt
 * @version $Id: Rssify.java 1819 2005-04-06 17:56:22Z stephan $
 */
public class Rssify {

    public static List rssify(snipsnap.api.snip.Snip snip) {
        return rssify(snip.getChildrenDateOrder());
    }

    public static List rssify(List snips) {
        List result = new ArrayList();
        Pattern pattern = null;
        MatchResult matchResult;
        try {
            pattern = Pattern.compile("^[\\p{Space}]*(1(\\.1)*)[\\p{Space}]+(.*?)$", Pattern.DOTALL | Pattern.MULTILINE);
        } catch (Exception e) {
            Logger.warn("bad pattern", e);
        }
        Iterator iterator = snips.iterator();
        while (iterator.hasNext() && result.size() <= 10) {
            snipsnap.api.snip.Snip snip = (snipsnap.api.snip.Snip) iterator.next();
            String content = snip.getContent();
            Matcher matcher = pattern.matcher(content);
            int start = 0;
            String title = "";
            while (matcher.find()) {
                matchResult = new MatchResult(matcher);
                String post = content.substring(start, matchResult.beginOffset(0)).trim();
                if (!("".equals(title) && "".equals(post))) {
                    add(result, snip, post, title);
                }
                start = matchResult.endOffset(0);
                title = matchResult.group(3).trim();
            }
            add(result, snip, content.substring(start).trim(), title);
        }
        return result;
    }

    private static void add(List list, Snip snip, String content, String title) {
        if (list.size() < 10) {
            list.add(createSnip(snip, content, title));
        }
    }

    private static Snip createSnip(Snip snip, String content, String title) {
        Snip rssSnip = null;
        if ("".equals(title)) {
            rssSnip = new RssSnip(snip, content);
        } else {
            int anchorIndex = title.indexOf("{anchor:");
            if (anchorIndex != -1) {
                String url = title.substring(anchorIndex + 8, title.indexOf('}', anchorIndex));
                title = title.substring(0, anchorIndex).trim();
                rssSnip = new RssSnip(snip, content, title, url);
            } else {
                rssSnip = new RssSnip(snip, content, title);
            }
        }
        return rssSnip;
    }
}
