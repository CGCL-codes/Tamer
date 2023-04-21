package org.light.portal.tags;

import static org.light.portal.util.Constants._GROUP_URL_PREFIX;
import static org.light.portal.util.Constants._SPACE_URL_PREFIX;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import org.light.portal.context.Context;
import org.light.portal.core.model.Portal;
import org.light.portal.core.model.PortalTab;
import org.light.portal.core.service.PortalService;
import org.light.portal.core.task.AfterTaskService;
import org.light.portal.user.model.User;
import org.light.portal.user.service.UserService;
import org.light.portal.util.DomainUtil;
import org.light.portlets.blog.service.BlogService;
import org.light.portlets.chat.service.ChatService;
import org.light.portlets.connection.service.ConnectionService;
import org.light.portlets.forum.service.ForumService;
import org.light.portlets.group.model.Group;
import org.light.portlets.group.service.GroupService;
import org.light.portlets.widget.service.WidgetService;

/**
 * 
 * @author Jianmin Liu
 **/
public abstract class BaseTag extends TagSupport {

    public int doStartTag() throws JspException {
        try {
            JspWriter writer = pageContext.getOut();
            writer.flush();
        } catch (Exception e) {
        }
        return EVAL_PAGE;
    }

    protected boolean isVisitingMember(HttpServletRequest request) {
        return Context.getInstance().isVisitingMember(request);
    }

    protected boolean isVisitingGroup(HttpServletRequest request) {
        return Context.getInstance().isVisitingMember(request);
    }

    protected PortalService getPortalService() {
        return Context.getInstance().getPortalService();
    }

    protected WidgetService getWidgetService() {
        return Context.getInstance().getWidgetService();
    }

    protected UserService getUserService() {
        return Context.getInstance().getUserService();
    }

    protected ConnectionService getConnectionService() {
        return Context.getInstance().getConnectionService();
    }

    protected GroupService getGroupService() {
        return Context.getInstance().getGroupService();
    }

    protected ForumService getForumService() {
        return Context.getInstance().getForumService();
    }

    protected BlogService getBlogService() {
        return Context.getInstance().getBlogService();
    }

    protected ChatService getChatService() {
        return Context.getInstance().getChatService();
    }

    protected AfterTaskService getAfterTaskService() {
        return Context.getInstance().getAfterTaskService();
    }

    protected boolean setLocale(HttpServletRequest request, String locale) {
        return Context.getInstance().setLocale(request, locale);
    }

    protected void setUser(HttpServletRequest request, User user) {
        Context.getInstance().setUser(request, user);
    }

    protected Portal getPortal(HttpServletRequest request) {
        return Context.getInstance().getPortal(request);
    }

    protected void setPortal(HttpServletRequest request, Portal portal) {
        Context.getInstance().setPortal(request, portal);
    }

    protected User getUser(HttpServletRequest request) {
        return Context.getInstance().getUser(request);
    }

    protected User getVisitedUser(HttpServletRequest request) {
        return Context.getInstance().getVisitedUser(request);
    }

    protected Group getVisitedGroup(HttpServletRequest request) {
        return Context.getInstance().getVisitedGroup(request);
    }

    protected Portal getVisitedPortal(HttpServletRequest request) {
        return Context.getInstance().getVisitedPortal(request);
    }

    protected PortalTab getVisitedPage(HttpServletRequest request) {
        return Context.getInstance().getVisitedPage(request);
    }

    protected void setVisitedUser(HttpServletRequest request, User user) {
        Context.getInstance().setVisitedUser(request, user);
    }

    protected void setVisitedPortal(HttpServletRequest request, Portal portal) {
        Context.getInstance().setVisitedPortal(request, portal);
    }

    protected void setVisitedPage(HttpServletRequest request, PortalTab tab) {
        Context.getInstance().setVisitedPage(request, tab);
    }

    protected void setVisitedGroup(HttpServletRequest request, Group group) {
        Context.getInstance().setVisitedGroup(request, group);
    }

    protected boolean isAdmin(HttpServletRequest request) {
        return Context.getInstance().isAdmin(request, this.getUser(request));
    }

    protected boolean isAdmin(HttpServletRequest request, User user) {
        return Context.getInstance().isAdmin(request, user);
    }

    protected boolean isGroupTabOwner(HttpServletRequest request, PortalTab tab, User user) {
        return Context.getInstance().isGroupTabOwner(request, tab, user);
    }
}
