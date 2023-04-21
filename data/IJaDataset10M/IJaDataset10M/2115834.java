package com.itth.ttraq.actions;

import com.itth.commons.dao.DAOException;
import com.itth.commons.dao.PersistentableDAOFactory;
import com.itth.commons.dao.hibernate.DAOFactoryHibernate;
import com.itth.commons.hibernate.HibernateUtil;
import com.itth.ttraq.TTraqService;
import com.itth.ttraq.beans.LogonDataBean;
import com.itth.ttraq.exceptions.NoProjectsException;
import com.itth.ttraq.exceptions.UnprivilegedAccessException;
import com.itth.ttraq.om.User;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.*;
import org.apache.struts.actions.LookupDispatchAction;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SessionAction is the super class of all user actions.
 */
public abstract class SessionAction extends LookupDispatchAction {

    static DAOFactoryHibernate dao;

    private static Logger logger = Logger.getLogger(SessionAction.class);

    @SuppressWarnings({ "FieldNameHidesFieldInSuperclass" })
    protected ActionMessages messages;

    protected ActionMessages errors;

    protected User user;

    private static Map<String, String> methodMap;

    static {
        Pattern patternKey = Pattern.compile("button\\.(.*)");
        methodMap = new HashMap<String, String>();
        ResourceBundle bundle = ResourceBundle.getBundle("com.itth.ttraq.Messages");
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Matcher matcher = patternKey.matcher(key);
            if (matcher.find()) {
                logger.debug("add action method: " + key + " -> " + matcher.group(1));
                methodMap.put(key, matcher.group(1));
            }
        }
    }

    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        messages = (ActionMessages) (request.getAttribute(Globals.MESSAGE_KEY) == null ? new ActionMessages() : request.getAttribute(Globals.MESSAGE_KEY));
        errors = (ActionMessages) (request.getAttribute(Globals.ERROR_KEY) == null ? new ActionMessages() : request.getAttribute(Globals.ERROR_KEY));
        if (request.getParameterMap().containsKey("_action")) {
            try {
                final ActionForward forwardSuper = super.execute(mapping, form, request, response);
                if (forwardSuper != null) return forwardSuper;
            } catch (Exception e) {
                logger.error(e, e);
            }
        }
        final ActionForward[] fw = new ActionForward[1];
        try {
            if (TTraqService.isAutoSetup() && !TTraqService.isInit()) {
                fw[0] = sessionExecute(mapping, form, request, response);
            } else {
                LogonDataBean logonDataBean = ((LogonDataBean) request.getSession().getAttribute("logonDataBean"));
                user = null;
                try {
                    String id = null;
                    User userLogin = null;
                    if (logonDataBean != null) {
                        id = logonDataBean.getUserDataBean().getId();
                        userLogin = getDao().getOM(User.class, logonDataBean.getUserId());
                        user = getDao().getOM(User.class, id);
                        user.setUserDataBean(logonDataBean.getUserDataBean());
                        user.setUserLogin(userLogin);
                        request.setAttribute("user", user);
                    }
                    fw[0] = sessionExecute(mapping, form, request, response);
                    if (logonDataBean != null) {
                        if (!id.equals(user.getUserDataBean().getId())) {
                            user = getDao().getOM(User.class, logonDataBean.getUserDataBean().getId());
                            user.setUserDataBean(logonDataBean.getUserDataBean());
                            user.setUserLogin(userLogin);
                        }
                        request.getSession().setAttribute("userDataBean", logonDataBean.getUserDataBean());
                    }
                } catch (NullPointerException npe) {
                    if (logonDataBean == null) {
                        TTraqService.logger.info(npe, npe);
                        errors.add(Globals.ERROR_KEY, new ActionMessage("error.session"));
                        fw[0] = mapping.findForward("logon");
                    } else {
                        throw npe;
                    }
                } catch (NoProjectsException npe) {
                    TTraqService.logger.debug(npe, npe);
                    errors.add(Globals.ERROR_KEY, new ActionMessage("error.no.projects.available"));
                    fw[0] = mapping.findForward("noprojects");
                } catch (UnprivilegedAccessException uae) {
                    TTraqService.logger.error("error unpriviliged access of:" + user + "to user: " + uae.getViewUser(), uae);
                    errors.add(Globals.ERROR_KEY, new ActionMessage("error.user.access.invalid"));
                    fw[0] = mapping.findForward("error");
                } catch (Throwable t) {
                    TTraqService.logger.error(t, t);
                    errors.add(Globals.ERROR_KEY, new ActionMessage("error.internal.message", t));
                    fw[0] = mapping.findForward("error");
                }
            }
        } catch (Throwable t) {
            TTraqService.logger.error(t, t);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.internal.message", t));
            fw[0] = mapping.findForward("error");
        }
        saveMessages(request, messages);
        saveErrors(request, errors);
        return fw[0];
    }

    public static PersistentableDAOFactory getDao() {
        HibernateUtil.beginTransaction();
        return dao;
    }

    protected abstract ActionForward sessionExecute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws NoProjectsException, DAOException, NoSuchAlgorithmException, NamingException, UnprivilegedAccessException;

    public static void setDao(DAOFactoryHibernate dao) {
        SessionAction.dao = dao;
    }

    public Map getKeyMethodMap() {
        return methodMap;
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return mapping.findForward("main");
    }
}
