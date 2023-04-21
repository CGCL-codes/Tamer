package hello;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

public final class HelloAction extends Action {

    /**
	 * Process the specified HTTP request, and create the corresponding HTTP
	 * response (or forward to another web component that will create it).
	 * Return an <code>ActionForward</code> instance describing where and how
	 * control should be forwarded, or <code>null</code> if the response has
	 * already been completed.
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        MessageResources messages = getResources(request);
        ActionMessages errors = new ActionMessages();
        String userName = (String) ((HelloForm) form).getUserName();
        String badUserName = "Monster";
        if (userName.equalsIgnoreCase(badUserName)) {
            errors.add("username", new ActionMessage("hello.dont.talk.to.monster", badUserName));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        PersonBean pb = new PersonBean();
        pb.setUserName(userName);
        pb.saveToPersistentStore();
        request.setAttribute(Constants.PERSON_KEY, pb);
        return (mapping.findForward("SayHello"));
    }
}
