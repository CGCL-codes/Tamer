package webstore.actions.createuser;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import database.model.User;
import database.model.UsersManager;
import webstore.actions.common.Action;
import webstore.model.IActionNames;
import webstore.model.IConstants;
import webstore.model.IPageNames;
import webstore.model.SessionUser;
import webstore.servlets.ActionFactory;
import webstore.utility.UserFieldsVerification;

public class SubmitNewUserAction implements Action, IPageNames, IConstants, IActionNames {

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        String act = request.getParameter("Create4");
        if (act.equals("Cancel")) {
            String s = request.getContextPath() + "/" + ACTION_PAGE_ADMIN + ".perform";
            try {
                response.sendRedirect(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        HttpSession session = request.getSession();
        SessionUser user = (SessionUser) session.getAttribute(USER_SESSION);
        String login = request.getParameter("LoginName4");
        String fname = request.getParameter("FirstName4");
        String lname = request.getParameter("LastName4");
        String pass = request.getParameter("Password4");
        String cpass = request.getParameter("ConfirmPassword4");
        String email = request.getParameter("EmailAddress4");
        int region = Integer.parseInt(request.getParameter("ComboRegion4"));
        int role = Integer.parseInt(request.getParameter("Role4"));
        int userId = Integer.parseInt(request.getParameter("id"));
        UserFieldsVerification ufv = new UserFieldsVerification();
        int ersum = 0;
        int code = ufv.getLoginVerification(login, act);
        ersum = ersum + code;
        request.setAttribute("ERRORLOGIN", code);
        code = ufv.getNameVerification(fname);
        ersum = ersum + code;
        request.setAttribute("ERRORFIRSTNAME", code);
        code = ufv.getNameVerification(lname);
        ersum = ersum + code;
        request.setAttribute("ERRORLASTNAME", code);
        code = ufv.getPassVerification(pass);
        ersum = ersum + code;
        request.setAttribute("ERRORPASS", code);
        code = ufv.getCPassVerification(pass, cpass);
        ersum = ersum + code;
        request.setAttribute("ERRORCPASS", code);
        code = ufv.getEmailVerification(email);
        ersum = ersum + code;
        request.setAttribute("ERROREMAIL", code);
        if (ersum == 0) {
            if (act.equals("Create") || act.equals("Duplicate")) {
                UsersManager userman = new UsersManager();
                User newUser = new User(login, fname, lname, pass, email, region, role, 0);
                userman.addUser(newUser);
            }
            if (act.equals("Update")) {
                UsersManager userman = new UsersManager();
                User newUser = new User(login, fname, lname, pass, email, region, role, 0);
                User oldUser = userman.getUser(userId);
                newUser.setId(userId);
                newUser.setAccbalance(oldUser.getAccbalance());
                userman.updateUser(newUser);
            }
            String s = request.getContextPath() + "/" + ACTION_PAGE_ADMIN + ".perform";
            try {
                response.sendRedirect(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            request.setAttribute("UserID", userId);
            request.setAttribute("ACTION", act);
            request.setAttribute("LOGINNAME", login);
            request.setAttribute("FIRSTNAME", fname);
            request.setAttribute("LASTNAME", lname);
            request.setAttribute("PASSWORD", pass);
            request.setAttribute("CPASSWORD", cpass);
            request.setAttribute("EMAIL", email);
            request.setAttribute("Reg1", "");
            request.setAttribute("Reg2", "");
            request.setAttribute("Reg3", "");
            request.setAttribute("Reg4", "");
            request.setAttribute("Reg" + Integer.toString(region), "selected");
            request.setAttribute("Rol1", "");
            request.setAttribute("Rol2", "");
            request.setAttribute("Rol3", "");
            request.setAttribute("Rol4", "");
            request.setAttribute("Rol" + Integer.toString(role), "checked");
            return PAGE_USER;
        }
    }
}
