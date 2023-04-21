package cn.vlabs.umtssologout.client;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * Receive UMT SessionId and startup UmtRefresh thread
 * @author yhw
 *Jul 11, 2009
 */
public class RecUmtSessIdTag extends TagSupport {

    public int doStartTag() {
        String umtSessId = pageContext.getRequest().getParameter("umtSessionId");
        String appLogoutUrl = pageContext.getRequest().getParameter("umtrefresh");
        if (umtSessId == null || umtSessId.length() < 1) return EVAL_PAGE;
        if (appLogoutUrl == null || appLogoutUrl.length() < 1) return EVAL_PAGE;
        UmtRefreshClient urc = new UmtRefreshClient(pageContext.getSession().getId(), appLogoutUrl, umtSessId);
        urc.start();
        return EVAL_PAGE;
    }
}
