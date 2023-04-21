package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;

/**
 * Servlet for handling editing user profiles
 * 
 * @author Robert Tansley
 * @version $Revision: 1189 $
 */
public class EditProfileServlet extends DSpaceServlet {

    /** Logger */
    private static Logger log = Logger.getLogger(EditProfileServlet.class);

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        log.info(LogManager.getHeader(context, "view_profile", ""));
        request.setAttribute("eperson", context.getCurrentUser());
        JSPManager.showJSP(request, response, "/register/edit-profile.jsp");
    }

    protected void doDSPost(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        EPerson eperson = context.getCurrentUser();
        boolean settingPassword = false;
        if ((eperson.getRequireCertificate() == false) && (request.getParameter("password") != null) && !request.getParameter("password").equals("")) {
            settingPassword = true;
        }
        boolean ok = updateUserProfile(eperson, request);
        if (!ok) {
            request.setAttribute("missing.fields", new Boolean(true));
        }
        String passwordProblem = null;
        if (ok && settingPassword) {
            ok = confirmAndSetPassword(eperson, request);
            if (!ok) {
                request.setAttribute("password.problem", new Boolean(true));
            }
        }
        if (ok) {
            log.info(LogManager.getHeader(context, "edit_profile", "password_changed=" + settingPassword));
            eperson.update();
            request.setAttribute("password.updated", new Boolean(settingPassword));
            JSPManager.showJSP(request, response, "/register/profile-updated.jsp");
            context.complete();
        } else {
            log.info(LogManager.getHeader(context, "view_profile", "problem=true"));
            request.setAttribute("eperson", eperson);
            JSPManager.showJSP(request, response, "/register/edit-profile.jsp");
        }
    }

    /**
     * Update a user's profile information with the information in the given
     * request. This assumes that authentication has occurred. This method
     * doesn't write the changes to the database (i.e. doesn't call update.)
     * 
     * @param eperson
     *            the e-person
     * @param request
     *            the request to get values from
     * 
     * @return true if the user supplied all the required information, false if
     *         they left something out.
     */
    public static boolean updateUserProfile(EPerson eperson, HttpServletRequest request) {
        String lastName = request.getParameter("last_name");
        String firstName = request.getParameter("first_name");
        String phone = request.getParameter("phone");
        eperson.setFirstName(firstName);
        eperson.setLastName(lastName);
        eperson.setMetadata("phone", phone);
        if ((lastName == null) || lastName.equals("") || (firstName == null) || firstName.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Set an eperson's password, if the passwords they typed match and are
     * acceptible. If all goes well and the password is set, null is returned.
     * Otherwise the problem is returned as a String.
     * 
     * @param eperson
     *            the eperson to set the new password for
     * @param request
     *            the request containing the new password
     * 
     * @return true if everything went OK, or false
     */
    public static boolean confirmAndSetPassword(EPerson eperson, HttpServletRequest request) {
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        if ((password == null) || (password.length() < 6)) {
            return false;
        }
        if (!password.equals(passwordConfirm)) {
            return false;
        }
        eperson.setPassword(password);
        return true;
    }
}
