package com.dotmarketing.portlets.htmlpageviews.action;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.UserProxy;
import com.dotmarketing.factories.HostFactory;
import com.dotmarketing.factories.IdentifierFactory;
import com.dotmarketing.factories.InodeFactory;
import com.dotmarketing.factories.UserProxyFactory;
import com.dotmarketing.portal.struts.DotPortletAction;
import com.dotmarketing.portlets.htmlpages.model.HTMLPage;
import com.dotmarketing.portlets.htmlpageviews.factories.HTMLPageViewFactory;
import com.dotmarketing.portlets.mailinglists.factories.MailingListFactory;
import com.dotmarketing.portlets.mailinglists.model.MailingList;
import com.dotmarketing.portlets.virtuallinks.factories.VirtualLinkFactory;
import com.dotmarketing.portlets.virtuallinks.model.VirtualLink;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.WebKeys;
import com.liferay.portal.ejb.UserLocalManagerUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.struts.ActionException;
import com.liferay.portal.util.Constants;
import com.liferay.portlet.ActionResponseImpl;
import com.liferay.util.servlet.SessionMessages;

/**
 * <a href="ViewQuestionsAction.java.html"> <b><i>View Source </i> </b> </a>
 * 
 * @author Maria Ahues
 * @version $Revision: 1.5 $
 *  
 */
public class ViewHTMLPageViewsAction extends DotPortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        Logger.debug(this, "Running ViewHTMLPagesAction!!!!");
        try {
            User user = _getUser(req);
            if (req.getWindowState().equals(WindowState.NORMAL)) {
                return mapping.findForward("portlet.ext.htmlpageviews.view");
            } else {
                List list = MailingListFactory.getMailingListsByUser(user);
                list.add(MailingListFactory.getUnsubscribersMailingList());
                req.setAttribute(WebKeys.MAILING_LIST_VIEW, list);
                _viewWebAssets(req, user);
                return mapping.findForward("portlet.ext.htmlpageviews.view_htmlpage_views");
            }
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(Constants.COMMON_ERROR);
        }
    }

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        ActionResponseImpl resImpl = (ActionResponseImpl) res;
        HttpServletResponse httpRes = resImpl.getHttpServletResponse();
        _retrieveUsers(req);
        if (Constants.SAVE.equals(req.getParameter(Constants.CMD))) {
            _saveMailingList(form, req, res);
        }
        if (Constants.UPDATE.equals(req.getParameter(Constants.CMD))) {
            _saveMailingList(form, req, res);
        }
        if (Constants.DELETE.equals(req.getParameter(Constants.CMD))) {
            _removeFromMailingList(form, req, res);
        }
        return;
    }

    private void _viewWebAssets(RenderRequest req, User user) throws Exception {
        com.liferay.portlet.RenderRequestImpl reqImpl = (com.liferay.portlet.RenderRequestImpl) req;
        HttpServletRequest httpReq = reqImpl.getHttpServletRequest();
        HttpSession session = httpReq.getSession();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("M/d/yyyy");
        java.util.Date startDate = null;
        java.util.Date startDateToShow = null;
        if (req.getParameter("searchStartDate") != null) {
            Logger.debug(this, "searchStartDate" + req.getParameter("searchStartDate"));
            startDate = sdf.parse(req.getParameter("searchStartDate"));
            GregorianCalendar calendarToShow = new GregorianCalendar();
            calendarToShow.setTimeInMillis(startDate.getTime());
            calendarToShow.add(Calendar.YEAR, 1);
            startDateToShow = calendarToShow.getTime();
        } else {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.set(Calendar.DAY_OF_MONTH, 1);
            gcal.set(Calendar.HOUR, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.SECOND, 0);
            startDate = gcal.getTime();
            startDateToShow = startDate;
        }
        Logger.debug(this, "startDate" + startDate);
        req.setAttribute("startDate", startDateToShow);
        java.util.Date endDate = null;
        if (req.getParameter("searchEndDate") != null) {
            Logger.debug(this, "searchEndDate" + req.getParameter("searchEndDate"));
            endDate = sdf.parse(req.getParameter("searchEndDate"));
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(endDate);
            gcal.set(Calendar.HOUR, 23);
            gcal.set(Calendar.MINUTE, 59);
            gcal.set(Calendar.SECOND, 59);
            endDate = gcal.getTime();
        } else {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.add(Calendar.MONTH, 1);
            gcal.set(Calendar.DAY_OF_MONTH, 1);
            gcal.set(Calendar.HOUR, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.SECOND, 0);
            endDate = gcal.getTime();
        }
        Logger.debug(this, "endDate" + endDate);
        req.setAttribute("endDate", endDate);
        String uri = null;
        if (req.getParameter("htmlpage") != null) {
            HTMLPage myHTMLPage = (HTMLPage) InodeFactory.getInode(req.getParameter("htmlpage"), HTMLPage.class);
            uri = IdentifierFactory.getIdentifierByInode(myHTMLPage).getURI();
            req.setAttribute("htmlPage", myHTMLPage);
        } else if (req.getParameter("pageIdentifier") != null) {
            Identifier id = (Identifier) InodeFactory.getInode(req.getParameter("pageIdentifier"), Identifier.class);
            uri = id.getURI();
            HTMLPage myHTMLPage = (HTMLPage) IdentifierFactory.getLiveChildOfClass(id, HTMLPage.class);
            req.setAttribute("htmlPage", myHTMLPage);
        }
        if (req.getParameter("pageURL") != null) {
            uri = req.getParameter("pageURL");
            String[] parts = uri.split(":");
            Host host = null;
            if (parts.length > 1) {
                host = HostFactory.getHostByHostName(parts[0]);
                uri = parts[1];
            } else {
                host = HostFactory.getDefaultHost();
            }
            Identifier id = IdentifierFactory.getIdentifierByURI(uri, host);
            HTMLPage myHTMLPage = (HTMLPage) IdentifierFactory.getLiveChildOfClass(id, HTMLPage.class);
            req.setAttribute("htmlPage", myHTMLPage);
            if (id.getInode() == 0) {
                VirtualLink vl = VirtualLinkFactory.getVirtualLinkByURL(uri);
                if (vl.getInode() == 0) {
                    myHTMLPage.setTitle("Page Not Found");
                    SessionMessages.add(req, "message", "message.htmlpageviews.pagenotfound");
                } else {
                    req.setAttribute(WebKeys.VIRTUAL_LINK_EDIT, vl);
                }
            }
        }
        try {
            if (UtilMethods.isSet(uri)) {
                String encodedURI = UtilMethods.encodeURIComponent(uri);
                int totalPageViews = HTMLPageViewFactory.getTotalHTMLPageViewsBetweenDates(encodedURI, startDate, endDate);
                int uniqueVisitors = HTMLPageViewFactory.getUniqueVisitorsBetweenDates(encodedURI, startDate, endDate);
                java.util.List totalPageViewsByLanguage = HTMLPageViewFactory.getTotalHTMLPageViewsByLanguageBetweenDates(encodedURI, startDate, endDate);
                java.util.List internalReferers = HTMLPageViewFactory.getTopInternalReferringPages(encodedURI, startDate, endDate);
                java.util.List internalOutgoing = HTMLPageViewFactory.getTopInternalOutgoingPages(encodedURI, startDate, endDate);
                java.util.List externalReferers = HTMLPageViewFactory.getTopExternalReferringPages(encodedURI, startDate, endDate);
                java.util.List topUsers = HTMLPageViewFactory.getTopUsers(encodedURI, startDate, endDate);
                java.util.List<Long> contentsInodesViews = HTMLPageViewFactory.getContentsInodesViewsBetweenDates(encodedURI, startDate, endDate);
                java.util.List<Long> contentsInodesUniqueVisitors = HTMLPageViewFactory.getContentsInodesUniqueVisitorsBetweenDates(encodedURI, startDate, endDate);
                java.util.HashMap<Long, Integer> countContentsInodesViews = _countNumEachLongFromList(contentsInodesViews);
                java.util.HashMap<Long, Integer> countContentsInodesUniqueVisitors = _countNumEachLongFromList(contentsInodesUniqueVisitors);
                java.util.HashSet<Long> contentsInodes = new java.util.HashSet<Long>(contentsInodesViews);
                contentsInodes.addAll(contentsInodesUniqueVisitors);
                req.setAttribute("totalPageViews", (new Integer(totalPageViews)));
                req.setAttribute("uniqueVisitors", (new Integer(uniqueVisitors)));
                req.setAttribute("totalPageViewsByLanguage", totalPageViewsByLanguage);
                req.setAttribute("internalReferers", internalReferers);
                req.setAttribute("externalReferers", externalReferers);
                req.setAttribute("internalOutgoing", internalOutgoing);
                req.setAttribute("topUsers", topUsers);
                req.setAttribute("uri", uri);
                req.setAttribute("contentsInodes", contentsInodes);
                req.setAttribute("countContentsInodesViews", countContentsInodesViews);
                req.setAttribute("countContentsInodesUniqueVisitors", countContentsInodesUniqueVisitors);
            }
        } catch (Exception e) {
            Logger.error(this, e.getMessage(), e);
            throw new ActionException(e.getMessage());
        }
        Logger.debug(this, "Done with ViewHTMLPageViewsAction");
    }

    private void _retrieveUsers(ActionRequest req) {
        User user = _getUser(req);
        java.util.Date startDate = null;
        if (req.getParameter("searchStartDate") != null) {
            Logger.debug(this, "searchStartDate" + req.getParameter("searchStartDate"));
            startDate = UtilMethods.htmlToDate(req.getParameter("searchStartDate"));
        } else {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.set(Calendar.DAY_OF_MONTH, 1);
            gcal.set(Calendar.HOUR, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.SECOND, 0);
            startDate = gcal.getTime();
        }
        Logger.debug(this, "startDate" + startDate);
        req.setAttribute("startDate", startDate);
        java.util.Date endDate = null;
        if (req.getParameter("searchEndDate") != null) {
            Logger.debug(this, "searchEndDate" + req.getParameter("searchEndDate"));
            endDate = UtilMethods.htmlToDate(req.getParameter("searchEndDate"));
        } else {
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.add(Calendar.MONTH, 1);
            gcal.set(Calendar.DAY_OF_MONTH, 1);
            gcal.set(Calendar.HOUR, 0);
            gcal.set(Calendar.MINUTE, 0);
            gcal.set(Calendar.SECOND, 0);
            endDate = gcal.getTime();
        }
        Logger.debug(this, "endDate" + endDate);
        req.setAttribute("endDate", endDate);
        String uri = null;
        java.util.List users = null;
        if (req.getParameter("virtualLinkInode") != null) {
            VirtualLink vl = VirtualLinkFactory.getVirtualLink(req.getParameter("virtualLinkInode"));
            users = HTMLPageViewFactory.getAllUsers(vl.getUrl(), startDate, endDate);
        } else if (req.getParameter("htmlpage") != null) {
            HTMLPage myHTMLPage = (HTMLPage) InodeFactory.getInode(req.getParameter("htmlpage"), HTMLPage.class);
            uri = IdentifierFactory.getIdentifierByInode(myHTMLPage).getURI();
            req.setAttribute("htmlPage", myHTMLPage);
            users = HTMLPageViewFactory.getAllUsers(uri, startDate, endDate);
        } else if (req.getParameter("pageIdentifier") != null) {
            Identifier id = (Identifier) InodeFactory.getInode(req.getParameter("pageIdentifier"), Identifier.class);
            uri = id.getURI();
            HTMLPage myHTMLPage = (HTMLPage) IdentifierFactory.getLiveChildOfClass(id, HTMLPage.class);
            req.setAttribute("htmlPage", myHTMLPage);
            users = HTMLPageViewFactory.getAllUsers(uri, startDate, endDate);
        }
        req.setAttribute(WebKeys.MAILING_LIST_SUBSCRIBERS, users);
    }

    private void _saveMailingList(ActionForm form, ActionRequest req, ActionResponse res) throws Exception {
        Logger.debug(this, "ViewHTMLPageViews: Saving List");
        User user = _getUser(req);
        java.util.List users = (List) req.getAttribute(WebKeys.MAILING_LIST_SUBSCRIBERS);
        String mailingListInode = (req.getParameter("mailingListInode") == null || req.getParameter("mailingListInode").equals("")) ? "0" : req.getParameter("mailingListInode");
        String mailingListTitle = req.getParameter("mailingListTitle");
        String allowPublicToSubscribe = req.getParameter("allowPublicToSubscribe");
        MailingList ml = (MailingList) InodeFactory.getInode(mailingListInode, MailingList.class);
        String cmd = req.getParameter(com.liferay.portal.util.Constants.CMD);
        if (com.liferay.portal.util.Constants.SAVE.equals(cmd)) {
            ml.setTitle(mailingListTitle);
            if (allowPublicToSubscribe.equals("yes")) ml.setPublicList(true); else ml.setPublicList(false);
            ml.setUserId(user.getUserId());
        }
        InodeFactory.saveInode(ml);
        Logger.debug(this, "Saving: " + users.size() + " subscribers.");
        java.util.Iterator userIter = users.iterator();
        while (userIter.hasNext()) {
            java.util.HashMap userCounts = (java.util.HashMap) userIter.next();
            if (userCounts.get("user_id") != null) {
                String userId = (String) userCounts.get("user_id");
                User webUser = UserLocalManagerUtil.getUserById(userId);
                UserProxy s = UserProxyFactory.getUserProxy(webUser);
                MailingListFactory.addMailingSubscriber(ml, s, false);
            }
        }
        InodeFactory.saveInode(ml);
        if (com.liferay.portal.util.Constants.SAVE.equals(cmd)) {
            SessionMessages.add(req, "message", "message.mailinglist.save");
        } else {
            SessionMessages.add(req, "message", "message.mailinglist.subscribers.added");
        }
    }

    private void _removeFromMailingList(ActionForm form, ActionRequest req, ActionResponse res) throws Exception {
        Logger.debug(this, "_removeFromMailingList: Removing subscribers From List");
        java.util.List users = (List) req.getAttribute(WebKeys.MAILING_LIST_SUBSCRIBERS);
        String mailingListInode = (req.getParameter("mailingListInode") == null || req.getParameter("mailingListInode").equals("")) ? "0" : req.getParameter("mailingListInode");
        MailingList ml = (MailingList) InodeFactory.getInode(mailingListInode, MailingList.class);
        Logger.debug(this, "Removing: " + users.size() + " subscribers. If they are in the list.");
        java.util.Iterator userIter = users.iterator();
        while (userIter.hasNext()) {
            java.util.HashMap userCounts = (java.util.HashMap) userIter.next();
            if (userCounts.get("user_id") != null) {
                String userId = (String) userCounts.get("user_id");
                User webUser = UserLocalManagerUtil.getUserById(userId);
                UserProxy s = UserProxyFactory.getUserProxy(webUser);
                if (s.getInode() > 0) {
                    MailingListFactory.deleteUserFromMailingList(ml, s);
                }
            }
        }
        InodeFactory.saveInode(ml);
        SessionMessages.add(req, "message", "message.mailinglist.subscribers.deleted");
    }

    private java.util.HashMap<Long, Integer> _countNumEachLongFromList(java.util.List<Long> inodesList) {
        java.util.HashMap<Long, Integer> result = new java.util.HashMap<Long, Integer>();
        if (0 < inodesList.size()) {
            Long lastInode = inodesList.get(0);
            int count = 1;
            Long inode;
            for (int pos = 1; pos < inodesList.size(); ++pos) {
                inode = inodesList.get(pos);
                if (lastInode.longValue() != inode.longValue()) {
                    result.put(lastInode, count);
                    lastInode = inode;
                    count = 1;
                } else {
                    ++count;
                }
            }
            result.put(lastInode, count);
        }
        return result;
    }
}
