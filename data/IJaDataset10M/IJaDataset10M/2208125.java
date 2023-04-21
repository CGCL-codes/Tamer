package be.oxys.itimesheets.spring;

import java.util.Collection;
import java.util.logging.Logger;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import be.oxys.itimesheets.GlobalData;
import be.oxys.itimesheets.daos.DAOException;
import be.oxys.itimesheets.daos.DAOFactory;
import be.oxys.itimesheets.tos.UserTO;

/**
 * 
 * @author jnc
 */
public class CustomUserDetailsMapper extends LdapUserDetailsMapper {

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<GrantedAuthority> authorities) {
        Logger.getLogger("CustomUserDetailsMapper").info("opening session for " + username);
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setFirstName(ctx.getStringAttribute("givenName"));
        userDetails.setLastName(ctx.getStringAttribute("sn"));
        userDetails.setUserName(username);
        userDetails.getAuthorities().addAll(authorities);
        userDetails.setDn(ctx.getDn().toString());
        userDetails.setEmail(ctx.getStringAttribute("mail"));
        UserTO user = null;
        try {
            user = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO().get(username);
        } catch (DAOException e) {
            Logger.getLogger("CustomUserDetailsMapper").severe("couldn't find user: " + e.getMessage());
        }
        if (user == null) {
            try {
                user = new UserTO();
                user.setUsername(username);
                user.setFirstName(userDetails.getFirstName());
                user.setLastName(userDetails.getLastName());
                user.setEmail(userDetails.getEmail());
                long key = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO().store(user, -1);
                user.setUserid(key);
                Logger.getLogger("CustomUserDetailsMapper").info("New user added: " + username + "(" + key + ")");
            } catch (DAOException e) {
                Logger.getLogger("CustomUserDetailsMapper").severe("Couldn't store the user");
                e.printStackTrace();
                return null;
            }
        }
        userDetails.setUserId(user.getUserid());
        if (user.getUserLevel().equals(GlobalData.USERLEVEL_SUPER)) {
            userDetails.getAuthorities().add(createAuthority("SUPER"));
        } else if (user.getUserLevel().equals(GlobalData.USERLEVEL_ADMIN)) {
            userDetails.getAuthorities().add(createAuthority("ADMIN"));
        } else {
            userDetails.getAuthorities().add(createAuthority("USER"));
        }
        try {
            boolean isTeamLeader = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO().isTeamLeader(user.getUserid());
            userDetails.setTeamLeader(isTeamLeader);
            if (isTeamLeader) {
                userDetails.getAuthorities().add(createAuthority("MANAGER"));
            }
            boolean isProjectManager = DAOFactory.getDAOFactory(DAOFactory.MYSQL).getUserDAO().isProjectManager(user.getUserid());
            userDetails.setProjectManager(isProjectManager);
        } catch (DAOException e) {
            Logger.getLogger("CustomUserDetailsMapper").severe("Couldn't get user management responsibilities");
            e.printStackTrace();
        }
        Logger.getLogger("CustomUserDetailsMapper").fine(userDetails.toString());
        return userDetails;
    }
}
