package openfuture.bugbase.actions;
/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.<p>
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.<p>
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA<br>
 * http://www.gnu.org/copyleft/lesser.html
 */

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import openfuture.bugbase.domain.User;
import openfuture.bugbase.forms.RegistrationForm;
import openfuture.bugbase.servlet.BugBaseServletClient;
import openfuture.util.error.I18NException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Action class handling the registration at Bug Base.
 * <p>
 *
 *
 * Created: Tue Jan 09 12:52:15 2001
 *
 * @author <a href="mailto:wolfgang@openfuture.de">Wolfgang Reissenberger</a>
 * @version $Revision: 1.7 $
 */

public class RegistrationAction extends BugBaseAction {

    private boolean success;
    
    /**
     * Registration at Bug Base. The following forwards are generated:
     * <ul>
     *   <li><strong>success</strong>: the registration was successful.
     *   <li><strong>error</strong>: the registration failed or
     *       an error occured.
     * </ul>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return an <code>ActionForward</code> value
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
        throws IOException, ServletException {

        ActionErrors errors = initialize(mapping, form, request, response);
        if (! errors.empty()) return (mapping.findForward("error"));

        if (form instanceof RegistrationForm) {

            RegistrationForm myForm = (RegistrationForm) form;
     
            BugBaseServletClient client = getClient(request);

            if (client == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, 
                           new ActionError("errors.registration.failed"));
                saveErrors(request, errors);
                servlet.log("registration failure: cannot find BugBaseServletClient.");
  
                Enumeration enum = getServlet().getServletContext().getAttributeNames();
                while(enum.hasMoreElements()) {
                    servlet.log("RegistrationAction: known servlet context attribute: " + 
                                enum.nextElement());
                }
                return (mapping.findForward("error"));
            }


            // create the new user
            User user = new User();
            user.setUserid(myForm.getUserid());
            user.setPassword(myForm.getPassword());
            user.setName(myForm.getName());
            user.setEmail(myForm.getEmail());
            user.getGroupList().add("users");


            try {

                client.registerUser(user);

                // clear password
                myForm.setPassword(null);
                myForm.setPassword2(null);
  
                // Forward control to the specified success URI
                return (mapping.findForward("success"));
            } catch (I18NException e) {
                errors.add(ActionErrors.GLOBAL_ERROR, 
                           new ActionError(e.getDescription().getKey()));
                servlet.log("registration failure:" + e.getDescription().getKey());
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }

        }
        return (mapping.findForward("error"));
    }

} // RegistrationAction
// Configuration Management Information: 
// -------------------------------------
// $Id: RegistrationAction.java,v 1.7 2003/09/01 18:27:54 wreissen Exp $
//
// Version History:
// ----------------
// $Log: RegistrationAction.java,v $
// Revision 1.7  2003/09/01 18:27:54  wreissen
// access to application context changed to make it available for Servlet API 2.2
//
// Revision 1.6  2003/09/01 15:54:09  wreissen
// made thread and session safe
//
// Revision 1.5  2003/08/31 12:46:43  wreissen
// imports corrected
//
// Revision 1.4  2001/11/21 21:42:40  wreissen
// - BugBaseServletClient.registerUser used
// - changes from BugBaseException to I18NException implemented
//
// Revision 1.3  2001/07/01 06:46:30  wreissen
// fixed errors in DOS/Unix-translation.
//
// Revision 1.2  2001/05/29 18:20:47  wreissen
// updated to version 1.0b2 of Struts
//
// Revision 1.1  2001/01/14 16:58:11  wreissen
// Usage of org.apache.struts started.
//
//
// ***********************************************************************************
