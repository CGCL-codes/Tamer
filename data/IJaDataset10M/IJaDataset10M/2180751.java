package edu.osu.cse.be.model.bd;

import java.util.Collection;
import javax.mail.MessagingException;
import edu.osu.cse.be.localization.LocalizedStringAccessor;
import edu.osu.cse.be.model.User;
import edu.osu.cse.be.model.bd.i.*;
import edu.osu.cse.be.model.exceptions.InvalidUserException;
import edu.osu.cse.be.persistence.HibernateDAOFactory;
import edu.osu.cse.be.persistence.dao.UserDAO;
import edu.osu.cse.be.services.MailingService;
import edu.osu.cse.be.services.PasswordService;
import edu.osu.cse.be.services.exceptions.UnitializedServiceException;

/**
 * @author Todd Sahl
 */
public class UserManagementDelegateImpl implements UserManagementDelegate {

    UserManagementDelegateImpl() {
    }

    public void updateUser(User user) throws InvalidUserException {
        if (user == null || user.getId() == null || user.getUserName() == null || user.getPassword() == null) throw new InvalidUserException("Referee requires username and password");
        UserDAO uDAO = HibernateDAOFactory.getUserDAO();
        uDAO.updateUser(user);
    }

    public User authenticate(String userName, String password) throws InvalidUserException {
        if (userName == null || password == null) throw new InvalidUserException("Referee Authentication requires username and password");
        UserDAO uDAO = HibernateDAOFactory.getUserDAO();
        Collection result = uDAO.findUser(userName, password);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            User user = (User) result.iterator().next();
            return user;
        }
    }

    public User findUser(String userName) throws InvalidUserException {
        if (userName == null) throw new InvalidUserException("Finding a user requires username");
        UserDAO uDAO = HibernateDAOFactory.getUserDAO();
        Collection result = uDAO.findUser(userName);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return (User) result.iterator().next();
        }
    }

    public void resetPassword(User user) throws InvalidUserException, UnitializedServiceException, MessagingException {
        String newPassword = PasswordService.getRandomPassword();
        user.setPassword(PasswordService.getInstance().encrypt(newPassword));
        updateUser(user);
        MailingService ms = MailingService.getInstance();
        String[] recepients = { user.getEmail() };
        ms.postMail(recepients, LocalizedStringAccessor.getString("ResetPassword.Email.Subject"), LocalizedStringAccessor.getString("ResetPassword.Email.Body") + newPassword, LocalizedStringAccessor.getString("ResetPassword.Email.Sender"));
    }

    public void changePassword(User user, String newPassword) throws InvalidUserException, UnitializedServiceException, MessagingException {
        user.setPassword(PasswordService.getInstance().encrypt(newPassword));
        updateUser(user);
        MailingService ms = MailingService.getInstance();
        String[] recepients = { user.getEmail() };
        ms.postMail(recepients, LocalizedStringAccessor.getString("ChangePassword.Email.Subject"), LocalizedStringAccessor.getString("ChangePassword.Email.Body") + newPassword, LocalizedStringAccessor.getString("ChangePassword.Email.Sender"));
    }
}
