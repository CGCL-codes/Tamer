package actions;

import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.EJBGetter;
import logic.addressBook.AddressBookSession;
import logic.addressBook.AddressBookSessionHome;
import logic.addressBook.Contact;
import logic.addressBook.InvalidContactException;
import logic.addressBook.NoAccountException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Sets session atributes necessary to edit selected contact. Goes to EditContactView.
 * @author arachne
 * 
 */
public class ToEditContact extends CommonAddressBookAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            AddressBookSession session;
            ActionErrors errors = new ActionErrors();
            try {
                session = home.create();
            } catch (Exception e1) {
                return mapping.findForward("notConnected");
            }
            Contact details = null;
            String c = request.getParameter("contactToEdit");
            request.getSession().setAttribute("contactToEdit", c);
            long cId = (new Long(c)).longValue();
            try {
                details = session.getContact(cId, getAId(request));
            } catch (RemoteException e) {
                e.printStackTrace();
                return mapping.findForward("notConnected");
            } catch (NoAccountException e) {
                errors.add("editContact", new ActionError("error.no.account"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            } catch (InvalidContactException e) {
                errors.add("editContact", new ActionError("error.no.contact"));
                saveErrors(request, errors);
                return mapping.findForward("error");
            }
            Contact con = new Contact();
            con.accountId = getAId(request);
            con.contactId = details.contactId;
            con.name = details.name;
            con.mail = details.mail;
            con.www = details.www;
            con.telNo = details.telNo;
            request.getSession().setAttribute("contactDetails", con);
            return mapping.findForward("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward("notConnected");
        }
    }
}
