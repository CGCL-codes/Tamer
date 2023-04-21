package ua.org.nuos.sdms.middle.service;

import org.apache.commons.lang.Validate;
import ua.org.nuos.configuration.SdmsConfiguration;
import ua.org.nuos.sdms.middle.Role;
import ua.org.nuos.sdms.middle.dao.AbstractBean;
import ua.org.nuos.sdms.middle.dao.UserDaoBean;
import ua.org.nuos.sdms.middle.entity.User;
import ua.org.nuos.sdms.middle.util.CodeUtil;
import ua.org.nuos.sdms.middle.util.HashUtil;
import ua.org.nuos.sdms.middle.util.RegistrationMailMessageConstructor;
import ua.org.nuos.sdms.middle.util.exception.CreateEntityException;
import ua.org.nuos.sdms.middle.util.exception.DuplicateEmailException;
import ua.org.nuos.sdms.middle.util.exception.FindEntityException;
import ua.org.nuos.sdms.middle.util.exception.InvalidCodeException;
import ua.org.nuos.sdms.middle.vo.CustomMessageVO;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 06.11.11
 * Time: 13:12
 * To change this template use File | Settings | File Templates.
 */
@Local
@Stateless
public class RegistrationServiceBean extends AbstractBean {

    @EJB(beanName = "UserDaoBean")
    protected UserDaoBean userDaoBean;

    @EJB(beanName = "MailSenderServiceBean")
    protected MailSenderServiceBean mailSenderServiceBean;

    @EJB(beanName = "SdmsConfiguration")
    protected SdmsConfiguration configuration;

    /**
     * Register new user in the system
     *
     * @param user user with password
     * @param role user role (Role.*)
     * @throws CreateEntityException if user can`t be created
     */
    public void registerNewUser(User user, Role role) throws CreateEntityException, DuplicateEmailException {
        Validate.notNull(role);
        try {
            User nullUser = userDaoBean.findByEmail(user.getEmail());
            if (nullUser != null) {
                throw new DuplicateEmailException(user.getEmail());
            }
            user.setPassword(HashUtil.hash(user.getPassword()));
            userDaoBean.createUser(user);
            user = userDaoBean.findByEmail(user.getEmail());
            userDaoBean.addUserRole(user.getId(), role);
            String code = CodeUtil.createRandomCode();
            userDaoBean.createUserRegistration(user.getId(), code);
            CustomMessageVO message = RegistrationMailMessageConstructor.createRegistrationMessage(user.getEmail(), configuration.getClientGuiHost(), code);
            mailSenderServiceBean.sendMessage(message);
        } catch (NoSuchAlgorithmException e) {
            throw new CreateEntityException("Can`t register (create) user", e);
        } catch (FindEntityException e) {
            throw new CreateEntityException("Can`t register (create) user", e);
        }
    }

    public User completeRegisterUser(String code) throws InvalidCodeException {
        User user = userDaoBean.findByRegistrationCode(code);
        if (user == null) {
            throw new InvalidCodeException(code);
        }
        userDaoBean.removeUserRegistration(user.getId());
        return user;
    }

    /**
     * Verify user registration status
     *
     * @param userId user id
     * @return true if registration is complete
     */
    public boolean isUserRegistrationComplete(long userId) {
        return userDaoBean.isUserRegistrationComplete(userId);
    }
}
