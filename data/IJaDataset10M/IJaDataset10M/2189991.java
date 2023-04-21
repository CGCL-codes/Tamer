package org.kablink.teaming.module.rss.util;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import org.kablink.teaming.context.request.RequestContext;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.web.util.WebUrlUtil;

public class UrlUtil {

    public static String getFeedURL(PortletRequest req, String binderId) {
        return getFeedURLImpl(WebUrlUtil.getRssRootURL(req), binderId);
    }

    public static String getFeedURLHttp(HttpServletRequest req, String binderId) {
        return getFeedURLImpl(WebUrlUtil.getRssRootURL(req), binderId);
    }

    private static String getFeedURLImpl(String rssRootUrl, String binderId) {
        RequestContext rc = RequestContextHolder.getRequestContext();
        boolean rssEnabled = SPropsUtil.getBoolean("rss.enable", true);
        if (!rssEnabled) return "";
        StringBuffer url = new StringBuffer();
        url.append(rssRootUrl).append("list").append("?bi=").append(binderId).append("&ui=").append(rc.getUserId()).append("&pd=").append(rc.getUser().getPrivateDigest(binderId)).append("&v=1");
        return url.toString();
    }

    public static String getAtomURL(PortletRequest req, String binderId) {
        return getAtomURLImpl(WebUrlUtil.getAtomRootURL(req), binderId);
    }

    public static String getAtomURLHttp(HttpServletRequest req, String binderId) {
        return getAtomURLImpl(WebUrlUtil.getAtomRootURL(req), binderId);
    }

    private static String getAtomURLImpl(String atomRootURL, String binderId) {
        RequestContext rc = RequestContextHolder.getRequestContext();
        boolean rssEnabled = SPropsUtil.getBoolean("rss.enable", true);
        if (!rssEnabled) return "";
        StringBuffer url = new StringBuffer();
        url.append(atomRootURL).append("list").append("?bi=").append(binderId).append("&ui=").append(rc.getUserId()).append("&pd=").append(rc.getUser().getPrivateDigest(binderId)).append("&v=1");
        return url.toString();
    }
}
