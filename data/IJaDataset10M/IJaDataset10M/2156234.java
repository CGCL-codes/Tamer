package com.sitescape.team.taglib;

import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.dao.ProfileDao;
import com.sitescape.team.domain.Principal;
import com.sitescape.team.domain.User;
import com.sitescape.team.presence.PresenceService;
import com.sitescape.team.util.NLT;
import com.sitescape.team.util.SpringContextUtil;
import com.sitescape.team.web.WebKeys;
import com.sitescape.util.servlet.StringServletResponse;

/**
 * @author Roy Klein
 *
 */
public class PresenceInfo extends BodyTagSupport {

    private Principal user = null;

    private Boolean showTitle = false;

    private String titleStyle = "";

    private String zonName = null;

    private String componentId;

    private int userStatus = -1;

    private Boolean showOptionsInline = false;

    private String optionsDivId = "";

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            HttpServletRequest httpReq = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse httpRes = (HttpServletResponse) pageContext.getResponse();
            if (this.componentId == null) this.componentId = "";
            if (this.showOptionsInline == null) this.showOptionsInline = false;
            User user1 = null;
            if (user != null) {
                ProfileDao profileDao = (ProfileDao) SpringContextUtil.getBean("profileDao");
                try {
                    user1 = profileDao.loadUser(user.getId(), user.getZoneId());
                } catch (Exception e) {
                }
            }
            PresenceService presenceService = (PresenceService) SpringContextUtil.getBean("presenceService");
            if (zonName != null) {
                userStatus = presenceService.getPresenceInfo(zonName);
            } else if (user != null && user1 != null) {
                userStatus = presenceService.getPresenceInfo(user1);
            } else {
                userStatus = -99;
            }
            if (userStatus != -99) {
                String dudeGif = "sym_s_white_dude.gif";
                String altText = NLT.get("presence.none");
                if (userStatus > 0) {
                    if ((userStatus & 16) == 16) {
                        dudeGif = "sym_s_yellow_dude.gif";
                        altText = NLT.get("presence.away");
                    } else {
                        dudeGif = "sym_s_green_dude.gif";
                        altText = NLT.get("presence.online");
                    }
                } else if (userStatus == 0) {
                    dudeGif = "sym_s_gray_dude.gif";
                    altText = NLT.get("presence.offline");
                }
                httpReq.setAttribute(WebKeys.PRESENCE_USER, user1);
                httpReq.setAttribute(WebKeys.PRESENCE_SHOW_TITLE, this.showTitle);
                httpReq.setAttribute(WebKeys.PRESENCE_TITLE_STYLE, this.titleStyle);
                httpReq.setAttribute(WebKeys.PRESENCE_STATUS, new Integer(userStatus));
                httpReq.setAttribute(WebKeys.PRESENCE_SWEEP_TIME, new Date());
                httpReq.setAttribute(WebKeys.PRESENCE_DUDE, dudeGif);
                httpReq.setAttribute(WebKeys.PRESENCE_TEXT, altText);
                httpReq.setAttribute(WebKeys.PRESENCE_ZON_BRIDGE, "enabled");
                httpReq.setAttribute(WebKeys.PRESENCE_COMPONENT_ID, this.componentId);
                httpReq.setAttribute(WebKeys.PRESENCE_DIV_ID, this.optionsDivId);
                httpReq.setAttribute(WebKeys.PRESENCE_SHOW_OPTIONS_INLINE, this.showOptionsInline);
                String jsp = "/WEB-INF/jsp/tag_jsps/presence/show_dude.jsp";
                RequestDispatcher rd = httpReq.getRequestDispatcher(jsp);
                ServletRequest req = pageContext.getRequest();
                StringServletResponse res = new StringServletResponse(httpRes);
                rd.include(req, res);
                pageContext.getOut().print(res.getString().trim());
            }
        } catch (Exception e) {
            throw new JspTagException(e.getMessage());
        } finally {
            userStatus = -1;
            showTitle = false;
            titleStyle = "";
            componentId = "";
            user = null;
            zonName = null;
            this.optionsDivId = "";
        }
        return EVAL_PAGE;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public void setUser(Principal user) {
        this.user = user;
    }

    public void setShowTitle(Boolean showTitle) {
        this.showTitle = showTitle;
    }

    public void setTitleStyle(String titleStyle) {
        this.titleStyle = titleStyle;
    }

    public void setZonName(String zonName) {
        this.zonName = zonName;
    }

    public void setShowOptionsInline(Boolean showOptionsInline) {
        this.showOptionsInline = showOptionsInline;
    }

    public void setOptionsDivId(String optionsDivId) {
        this.optionsDivId = optionsDivId;
    }
}
