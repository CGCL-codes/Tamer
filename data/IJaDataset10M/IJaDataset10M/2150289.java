package net.simpleframework.web.page.component.ui.tabs;

import java.util.List;
import net.simpleframework.util.HTTPUtils;
import net.simpleframework.util.StringUtils;
import net.simpleframework.web.page.PageRequestResponse;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public abstract class TabsUtils {

    public static String tabs(final PageRequestResponse requestResponse, final TabHref... tabHrefs) {
        return tabs(requestResponse, 0, tabHrefs);
    }

    public static String tabs(final PageRequestResponse requestResponse, int defaultIndex, final TabHref... tabHrefs) {
        if (tabHrefs == null) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("<ul class=\"simple_tabs\">");
        final String requestURI = HTTPUtils.getRequestAndQueryStringUrl(requestResponse.request);
        for (int i = 0; i < tabHrefs.length; i++) {
            final TabHref tab = tabHrefs[i];
            boolean match = match(requestResponse, tab, requestURI);
            if (!match) {
                final List<TabHref> children = tab.getChildren();
                if (children.size() > 0) {
                    for (final TabHref tab2 : children) {
                        if (match(requestResponse, tab2, requestURI)) {
                            match = true;
                            break;
                        }
                    }
                }
            }
            if (match) {
                defaultIndex = i;
                break;
            }
        }
        for (int i = 0; i < tabHrefs.length; i++) {
            final TabHref tab = tabHrefs[i];
            sb.append("<li><a href=\"");
            sb.append(tab.getHref()).append("\"");
            if (i == defaultIndex) {
                sb.append(" class=\"active\"");
            }
            sb.append(">");
            sb.append(tab.toString());
            sb.append("</a>");
            final String html2 = tab.getHtml2();
            if (StringUtils.hasText(html2)) {
                sb.append(html2);
            }
            sb.append("</li>");
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private static boolean match(final PageRequestResponse requestResponse, final TabHref tab, final String requestURI) {
        final EMatchMethod method = tab.getMatchMethod();
        final String href = requestResponse.wrapContextPath(tab.getHref());
        if (method == EMatchMethod.endsWith) {
            return requestURI.endsWith(href);
        } else if (method == EMatchMethod.startsWith) {
            return requestURI.startsWith(href);
        } else if (method == EMatchMethod.contains) {
            return requestURI.contains(href);
        } else {
            return requestURI.equals(href);
        }
    }
}
