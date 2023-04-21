package security;

import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;
import appointments.view.beans.FacesUtils;
import appointments.view.beans.SessionBean;
import users.domain.Role;
import users.domain.RoleUserRepository;
import users.domain.User;
import users.domain.UserRepository;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * This class gives Acegi all the users information from database. (See
 * userDetailsService bean defined in spring-manage-acegi-security.xml)
 */
public class CuretaUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleUserRepository roleUserRepository;

    public CuretaUserDetailsService(UserRepository userRepository, RoleUserRepository roleUserRepository) {
        super();
        this.userRepository = userRepository;
        this.roleUserRepository = roleUserRepository;
    }

    /**
	 * @param userRepository
	 *            the userRepository to set
	 */
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
	 * @param roleUserRepository
	 *            the roleUserRepository to set
	 */
    public void setRoleUserRepository(RoleUserRepository roleUserRepository) {
        this.roleUserRepository = roleUserRepository;
    }

    /**
	 * Loads an user by nickname
	 * 
	 * @param userName
	 *            required not null
	 * 
	 * @return not null
	 * 
	 * @throws UsernameNotFoundException
	 *             to report the exception
	 * @throws DataAccessException
	 *             to report the exception
	 */
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        User user = userRepository.getUserByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userName);
        }
        List<Role> roles = roleUserRepository.getRolesByUserId(user.getId());
        SessionBean sessionBean = new SessionBean();
        sessionBean.setUserName(userName);
        sessionBean.setUserId(user.getId());
        FacesUtils.setManagedBeanInSession("sessionBean", sessionBean);
        Exception ex = (Exception) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY);
        if (ex != null) FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage()));
        return new UserDetailsAdapter(user, roles);
    }
}
