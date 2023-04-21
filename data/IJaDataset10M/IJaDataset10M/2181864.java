package example.action;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.azaleait.asterion.action.Ambit;
import com.azaleait.asterion.action.ViewController;
import com.azaleait.asterion.exception.AmbitNotAllowedException;
import com.azaleait.asterion.exception.CanNotChangeAmbitException;
import example.model.Customer;

public class LoginAction extends ViewController {

    private static Logger logger = Logger.getLogger(LoginAction.class);

    protected int allowAmbit() {
        logger.debug("do not allow unbound requests.");
        return Ambit.ALLOW_BOUND_AUTHENTICATED;
    }

    protected Class onNotAllowed(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session, final AmbitNotAllowedException e) throws AmbitNotAllowedException {
        logger.info("sesion expirada.");
        return ExpiredSessionAction.class;
    }

    protected boolean checkRequestSequence() {
        return true;
    }

    protected Class onRequestOutOfSequence(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) throws CanNotChangeAmbitException {
        this.invalidateSessionAndRebind(request);
        return DisconnectedAction.class;
    }

    protected boolean allowInitiatePageGraph() {
        return false;
    }

    protected Class preRender(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) {
        session.setMaxInactiveInterval(20);
        return this.getClass();
    }

    public Class accept(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, CanNotChangeAmbitException {
        String user = (String) PropertyUtils.getProperty(form, "user");
        String pass = (String) PropertyUtils.getProperty(form, "pass");
        if ("scott".equals(user) && "tiger".equals(pass)) {
            this.registerAuthentication(request);
            Customer c = new Customer();
            c.setName("John Smith");
            c.setAddress("2631 Oak St, Moscow");
            c.setSex("M");
            c.setAgerange("b");
            c.setMarried("true");
            session.setAttribute("currentcustomer", c);
            return EditProfileAction.class;
        } else {
            ActionMessages messages = new ActionMessages();
            messages.add("user", new ActionMessage("login.invaliduser"));
            saveErrors(request, messages);
            return LoginAction.class;
        }
    }
}
