package com.velocityme.session;

import com.velocityme.entity.Login;
import com.velocityme.entity.User;
import com.velocityme.enums.LoginStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rcrida
 */
@Stateless
public class LoginBean implements LoginLocal {

    @PersistenceContext
    private EntityManager em;

    public void limitLoginCount(User user) {
        int loginCountLimit = 6;
        List<Login> logins;
        boolean finished = false;
        while (!finished && (logins = new ArrayList<Login>(user.getLogins())).size() > loginCountLimit) {
            finished = true;
            Collections.sort(logins, new Comparator<Login>() {

                public int compare(Login login1, Login login2) {
                    return login1.getLoginTime().compareTo(login2.getLoginTime());
                }
            });
            for (Login login : logins) {
                if (!login.getLoginStatus().equals(LoginStatus.LOGGED_ON)) {
                    finished = false;
                    login.getUser().getLogins().remove(login);
                    em.remove(login);
                    em.merge(login.getUser());
                    break;
                }
            }
        }
    }

    @Override
    public List<Login> findAllLogins(KeyLocal key) {
        key.validateKey();
        return em.createNamedQuery("findAllLogins").getResultList();
    }
}
