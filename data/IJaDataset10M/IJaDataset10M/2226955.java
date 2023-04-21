package strutter.optional;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import strutter.config.ActionMappingExtended;
import strutter.optional.interceptor.ActionInterceptorInterface;

public class ActionHelper {

    private static final ThreadLocal mem = new ThreadLocal();

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ActionMessages errormsgs;

    private ActionMessages infomsgs;

    private Locale locale = null;

    private ActionMappingExtended mapping;

    static ActionHelper instance = new ActionHelper();

    public static synchronized ActionHelper getInstance() {
        return instance;
    }

    public ActionHelper() {
    }

    ActionForward startExt(ActionMappingExtended mapping, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionHelper helper = new ActionHelper();
        helper.request = request;
        helper.locale = (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY);
        helper.errormsgs = getErrors(request);
        helper.infomsgs = getMessages(request);
        helper.mapping = mapping;
        mem.set(helper);
        for (int i = 0; i < mapping.getInterceptors().size(); i++) {
            ActionInterceptorInterface interceptor = (ActionInterceptorInterface) mapping.getInterceptors().get(i);
            ActionForward forward = interceptor.beforeMethod(me().mapping, request, response);
            if (forward != null) {
                return forward;
            }
        }
        return null;
    }

    private final ActionHelper me() {
        return (ActionHelper) mem.get();
    }

    ActionForward endExt() {
        try {
            me().addErrors();
            me().addMessages();
            for (int i = 0; i < me().mapping.getInterceptors().size(); i++) {
                ActionInterceptorInterface interceptor = (ActionInterceptorInterface) me().mapping.getInterceptors().get(i);
                ActionForward forward = interceptor.afterMethod(me().mapping, me().getRequest(), me().getResponse());
                if (forward != null) {
                    return forward;
                }
            }
        } finally {
            mem.remove();
        }
        return null;
    }

    protected ActionMessages getErrors(HttpServletRequest request) {
        ActionMessages errors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (errors == null) {
            errors = new ActionMessages();
        }
        return errors;
    }

    protected void addErrors() {
        HttpServletRequest request = me().request;
        ActionMessages errors = me().errormsgs;
        if (errors == null) return;
        ActionMessages requestErrors = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (requestErrors == null) {
            requestErrors = new ActionMessages();
        }
        requestErrors.add(errors);
        if (requestErrors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }
        request.setAttribute(Globals.ERROR_KEY, requestErrors);
    }

    protected ActionMessages getMessages(HttpServletRequest request) {
        ActionMessages messages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
        if (messages == null) {
            messages = new ActionMessages();
        }
        return messages;
    }

    protected void addMessages() {
        HttpServletRequest request = me().request;
        ActionMessages messages = me().infomsgs;
        if (messages == null) return;
        ActionMessages requestMessages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
        if (requestMessages == null) {
            requestMessages = new ActionMessages();
        }
        requestMessages.add(messages);
        if (requestMessages.isEmpty()) {
            request.removeAttribute(Globals.MESSAGE_KEY);
            return;
        }
        request.setAttribute(Globals.MESSAGE_KEY, requestMessages);
    }

    public void addError(String alias, String text) {
        ActionMessage msg = new ActionMessage(text);
        getErrormsgs().add(alias, msg);
    }

    public void addError(String text) {
        ActionMessage msg = new ActionMessage(text);
        getErrormsgs().add("", msg);
    }

    public void addError(String alias, String text, Object[] objs) {
        ActionMessage msg = new ActionMessage(text, objs);
        getErrormsgs().add(alias, msg);
    }

    public void addError(String text, Object[] objs) {
        ActionMessage msg = new ActionMessage(text, objs);
        getErrormsgs().add("", msg);
    }

    public void addMessage(String alias, String text) {
        ActionMessage msg = new ActionMessage(text);
        getInfomsgs().add(alias, msg);
    }

    public void addMessage(String text) {
        ActionMessage msg = new ActionMessage(text);
        getInfomsgs().add("", msg);
    }

    public void addMessage(String alias, String text, Object[] objs) {
        ActionMessage msg = new ActionMessage(text, objs);
        getInfomsgs().add(alias, msg);
    }

    public void addMessage(String text, Object[] objs) {
        ActionMessage msg = new ActionMessage(text, objs);
        getInfomsgs().add("", msg);
    }

    public boolean hasErrors() {
        return !getErrormsgs().isEmpty();
    }

    public boolean hasMessages() {
        return !getInfomsgs().isEmpty();
    }

    public ActionMessages getErrormsgs() {
        return me().errormsgs;
    }

    public ActionMessages getInfomsgs() {
        return me().infomsgs;
    }

    public Locale getLocale() {
        return locale;
    }

    public HttpServletRequest getRequest() {
        return me().request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
