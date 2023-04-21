package de.mediumster.controller;

import java.util.List;
import de.mediumster.model.User;
import de.mediumster.model.acl.UserGroup;
import de.mediumster.model.hibernate.UserHandler;

/**
 * @author Hannes Rempel, Matthias Schirmacher, Alexander Esslinger
 */
public class UserController extends Controller {

    private User user;

    private int userId;

    @Override
    public void doTask() throws Exception {
        switch(task) {
            case activate:
                user = UserHandler.getUser(getCaller(), userId);
                user.setActive(true);
                UserHandler.updateUser(getCaller(), user);
                task = Task.list;
                doTask();
                break;
            case create:
                user = new User();
                fillUser(user);
                setAttribute("user", user);
                break;
            case deactivate:
                user = UserHandler.getUser(getCaller(), userId);
                if (user.equals(getCaller())) {
                    throwException("Sie k�nnen sich nicht selber deaktivieren!");
                } else {
                    user.setActive(false);
                    UserHandler.updateUser(getCaller(), user);
                }
                task = Task.list;
                doTask();
                break;
            case edit:
                user = UserHandler.getUser(getCaller(), userId);
                fillUser(user);
                setAttribute("user", user);
                break;
            case list:
                setAttribute("users", UserHandler.getAllUsers(getCaller()));
                break;
            case print:
                try {
                    user = UserHandler.getUser(getCaller(), userId);
                    getResponse().setContentType("application/pdf");
                    getResponse().setHeader("Content-disposition", "attachment; filename=" + "identityCard_" + user.getUserId() + ".pdf");
                    DocumentOutputCreator.createIdentityCard(getResponse().getOutputStream(), user);
                } catch (Exception e) {
                    getResponse().reset();
                    throw e;
                }
                break;
            case save:
                task = Task.create;
                doTask();
                if (UserGroup.admins.isMember(getCaller())) {
                    user.setActive(true);
                }
                UserHandler.createUser(getCaller(), user);
                sendRedirect();
                break;
            case search:
                task = Task.list;
                user = new User();
                fillUser(user);
                List<User> userList = UserHandler.getUsersByExample(getCaller(), user);
                setAttribute("users", userList);
                break;
            case select:
                getSession().setAttribute("currentUser", user = UserHandler.getUser(getCaller(), userId));
                task = Task.list;
                doTask();
                break;
            case unselect:
                sendRedirect();
                getSession().setAttribute("currentUser", getCaller());
                break;
            case unknown:
                task = Task.list;
                doTask();
                break;
            case update:
                task = Task.edit;
                doTask();
                UserHandler.updateUser(getCaller(), user);
                sendRedirect();
                break;
            case view:
                setAttribute("user", UserHandler.getUser(getCaller(), userId));
                break;
        }
    }

    private User fillUser(User user) {
        if (getParameter("password") != null && !getParameter("password").equals(getParameter("password2"))) {
            throwException("Es m�ssen beide Passw�rter �bereinstimmen!");
        }
        return user.setBirthday(getParameter("birthday")).setFirstName(getParameter("firstName")).setLastName(getParameter("lastName")).setStreetName(getParameter("street")).setStreetNumber(getParameter("house_number")).setPostalCode(getParameter("postal_code")).setCity(getParameter("city")).setPhone(getParameter("phone")).setEmail(getParameter("email")).setLogin(getParameter("login")).setPassword(getParameter("password"));
    }

    @Override
    protected void init() {
        super.init();
        try {
            userId = new Integer(getParameter("userId"));
        } catch (NumberFormatException e1) {
        }
    }
}
