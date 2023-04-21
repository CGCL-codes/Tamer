package org.randomtask.yafumato.struts.action.messaging;

import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.randomtask.yafumato.messaging.*;

/**
 * Disconnects from a messenger service.
 * 
 * @author  Rand
 */
public class DisconnectAction extends org.randomtask.yafumato.struts.action.Action {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it),
     * with provision for handling exceptions thrown by the business logic.
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response
     * has already been completed.
     *
     * @param   mapping     The <code>ActionMapping</code>
     *                      used to select this instance.
     * @param   form        The optional <code>ActionForm</code>
     *                      bean for this request (if any).
     * @param   request     The HTTP request we are processing.
     * @param   response    The HTTP response we are creating.
     *
     * @return  An <code>ActionForward</code> instance describing where
     *          and how control should be forwarded, or <code>null</code>
     *          if the response has already been completed.
     *
     * @throws  Exception   If the application business
     *                      logic throws an exception.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean failure = false;
        HttpSession session = request.getSession();
        ActionMessages errors = new ActionMessages();
        String messenger = request.getParameter(KEY_MESSAGING_MESSENGER);
        if (messenger == null || messenger.equals("")) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.parameter.invalid"));
            saveErrors(request, errors);
            return mapping.findForward(FORWARD_FAILURE);
        }
        Identity[] identities = (Identity[]) session.getAttribute(MessagingConstants.SESSION_KEY_IDENTITIES);
        Identity identity = SessionManager.getIdentity(identities, messenger);
        if (identity != null) {
            Messenger messengerImpl = SessionManager.getMessenger(identity);
            if (messengerImpl != null) {
                try {
                    messengerImpl.disconnect(identity);
                } catch (Throwable t) {
                    failure = true;
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.disconnect.throwable", messenger, t.getMessage()));
                }
                SessionManager.removeSession(identity);
            } else {
                failure = true;
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.messenger.notfound", messenger));
            }
        } else {
            failure = true;
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.identity.notfound", messenger));
        }
        if (failure) {
            saveErrors(request, errors);
            return mapping.findForward(FORWARD_FAILURE);
        }
        return mapping.findForward(FORWARD_SUCCESS);
    }
}
