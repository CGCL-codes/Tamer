package com.j2biz.compote.plugins.news.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import com.j2biz.compote.CompoteContext;
import com.j2biz.compote.model.HibernateSession;
import com.j2biz.compote.plugins.news.forms.MessageForm;
import com.j2biz.compote.plugins.news.pojos.NewsCategory;
import com.j2biz.compote.plugins.news.pojos.NewsMessage;
import com.j2biz.compote.pojos.Group;
import com.j2biz.compote.util.SystemUtils;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class ManageMessages extends DispatchAction {

    private static final Log log = LogFactory.getLog(ManageMessages.class);

    private static final String MESSAGES_LIST_VIEW = "messagesListView";

    private static final String MESSAGES_FORM_VIEW = "messagesFormView";

    private static final String MESSAGES_CONFIRM_REMOVE_VIEW = "messagesConfirmRemoveView";

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("list(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            Session s = HibernateSession.openSession();
            List messagesList = s.find("from NewsMessage as nm");
            HibernateSession.closeSession(s);
            request.setAttribute("messagesList", messagesList);
            ActionForward returnActionForward = mapping.findForward(MESSAGES_LIST_VIEW);
            if (log.isDebugEnabled()) {
                log.debug("list(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("list(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("list(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    public ActionForward createForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("createForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            MessageForm _form = new MessageForm();
            request.getSession().setAttribute("messageForm", _form);
            Session s = HibernateSession.openSession();
            prepareCategoriesCollection(request, s);
            request.setAttribute("groupCollection", SystemUtils.getFullGroupCollection());
            HibernateSession.closeSession(s);
            ActionForward returnActionForward = mapping.findForward(MESSAGES_FORM_VIEW);
            if (log.isDebugEnabled()) {
                log.debug("createForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("createForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("createForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward editForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("editForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            Session s = HibernateSession.openSession();
            String id = request.getParameter("id");
            NewsMessage sysGroup = (NewsMessage) s.load(NewsMessage.class, new Long(id));
            prepareCategoriesCollection(request, s);
            request.setAttribute("groupCollection", SystemUtils.getFullGroupCollection());
            HibernateSession.closeSession(s);
            MessageForm _form = new MessageForm();
            _form.initFromMessage(sysGroup);
            request.getSession().setAttribute("messageForm", _form);
            ActionForward returnActionForward = mapping.findForward(MESSAGES_FORM_VIEW);
            if (log.isDebugEnabled()) {
                log.debug("editForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("editForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("editForm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            MessageForm messageForm = (MessageForm) form;
            ActionErrors errors = messageForm.validate(mapping, request);
            if (errors != null && errors.size() > 0) {
                saveErrors(request, errors);
                if (messageForm.getId() != null && messageForm.getId().length() > 0) return editForm(mapping, form, request, response); else return createForm(mapping, form, request, response);
            }
            String msgId = messageForm.getId();
            String title = messageForm.getTitle();
            String msgBody = messageForm.getMessage();
            String excerpt = messageForm.getExcerpt();
            Session s = HibernateSession.openSession();
            NewsMessage message = null;
            if (!StringUtils.isEmpty(msgId)) {
                message = (NewsMessage) s.load(NewsMessage.class, Long.valueOf(msgId));
            } else {
                message = new NewsMessage();
            }
            message.setTitle(title);
            message.setMessage(msgBody);
            message.setExcerpt(excerpt);
            message.setCategory((NewsCategory) s.load(NewsCategory.class, Long.valueOf(messageForm.getCategoryId())));
            message.setActivated(messageForm.getActivated());
            message.setAllowComments(messageForm.getAllowComments());
            String commenterGroupId = messageForm.getCommenterGroupId();
            message.setCommenterGroup((Group) s.load(Group.class, Long.valueOf(commenterGroupId)));
            message.setCreateTime(new Date());
            message.setPublisher(CompoteContext.getUser(request));
            s.saveOrUpdate(message);
            s.flush();
            HibernateSession.closeSession(s);
            ActionForward returnActionForward = list(mapping, form, request, response);
            if (log.isDebugEnabled()) {
                log.debug("save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("save(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward removeConfirm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("removeConfirm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            Session s = HibernateSession.openSession();
            String id = request.getParameter("id");
            NewsMessage message = (NewsMessage) s.load(NewsMessage.class, new Long(id));
            request.setAttribute("newsMessage", message);
            HibernateSession.closeSession(s);
            ActionForward returnActionForward = mapping.findForward(MESSAGES_CONFIRM_REMOVE_VIEW);
            if (log.isDebugEnabled()) {
                log.debug("removeConfirm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("removeConfirm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("removeConfirm(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("remove(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - start");
        }
        try {
            SystemUtils.assertIsUserInGroup(CompoteContext.getUser(request), "news_administrators");
            Session s = HibernateSession.openSession();
            String id = request.getParameter("id");
            NewsMessage message = (NewsMessage) s.load(NewsMessage.class, new Long(id));
            s.delete(message);
            s.flush();
            HibernateSession.closeSession(s);
            ActionForward returnActionForward = list(mapping, form, request, response);
            if (log.isDebugEnabled()) {
                log.debug("remove(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        } catch (Exception e) {
            log.error("remove(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)", e);
            ActionForward returnActionForward = SystemUtils.getPreparedErrorForward(request, e);
            if (log.isDebugEnabled()) {
                log.debug("remove(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse) - end");
            }
            return returnActionForward;
        }
    }

    /**
     * @param request
     * @throws HibernateException
     */
    private void prepareCategoriesCollection(HttpServletRequest request, Session session) throws HibernateException {
        if (log.isDebugEnabled()) {
            log.debug("prepareCategoriesCollection(HttpServletRequest, Session) - start");
        }
        ArrayList categoryCollection = new ArrayList();
        List categories = session.find("from NewsCategory as nm");
        for (int i = 0; i < categories.size(); i++) {
            NewsCategory cat = (NewsCategory) categories.get(i);
            categoryCollection.add(new LabelValueBean(cat.getName(), cat.getId().toString()));
        }
        request.setAttribute("categoryCollection", categoryCollection);
        if (log.isDebugEnabled()) {
            log.debug("prepareCategoriesCollection(HttpServletRequest, Session) - end");
        }
    }
}
