package fr.loria.ecoo.wookiApp.web.wiki;

import fr.loria.ecoo.lpbcast.util.NetUtil;
import fr.loria.ecoo.wookiApp.web.WookiSite;
import org.radeox.filter.CacheFilter;
import org.radeox.filter.context.FilterContext;
import org.radeox.filter.regex.RegexTokenFilter;
import org.radeox.regex.MatchResult;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class WookiSemanticLinkFilter extends RegexTokenFilter implements CacheFilter {

    private String serverUrl;

    /**
     * Creates a new WookiLinkFilter object.
     *
     * @throws Exception DOCUMENT ME!
     */
    public WookiSemanticLinkFilter() throws Exception {
        super("\\[[^\\[\\]::]*::[^\\[\\]::]*\\]", false);
        this.serverUrl = WookiSite.getInstance().getWookiEngine().getSiteUrl().toString() + "/ViewPage?page=";
    }

    /**
     * DOCUMENT ME!
     *
     * @param sb DOCUMENT ME!
     * @param result DOCUMENT ME!radeox+add filter
     * @param ctx DOCUMENT ME!
     */
    @Override
    public void handleMatch(StringBuffer sb, MatchResult result, FilterContext ctx) {
        String content = result.group(0).substring(1, result.group(0).length() - 1);
        String link;
        String label;
        if (content.indexOf("|") == -1) {
            link = content.substring(content.indexOf(":") + 2);
            label = null;
        } else {
            link = content.substring(content.indexOf(":") + 2, content.indexOf("|"));
            label = content.substring(content.indexOf("|") + 1);
        }
        String httpLinkPattern = "([^\"'=]|^)((http|ftp)s?://(%[\\p{Digit}A-Fa-f][\\p{Digit}A-Fa-f]|[-_.!~*';/?:@#&=+$,\\p{Alnum}])+)";
        try {
            if (link.matches(httpLinkPattern)) {
                sb.append("<a class=\"externalLink\" href=\"" + NetUtil.normalize(link) + "\">" + ((label == null) ? link : label) + "</a>");
                return;
            }
            sb.append("<a class=\"wookiLink\" href=\"" + this.serverUrl + link + "\">" + ((label == null) ? link : label) + "</a>");
            return;
        } catch (Exception e) {
            sb.append("<span class=\"error\">invalid link : " + link + "(" + e.getMessage() + ")</span>");
        }
    }
}
