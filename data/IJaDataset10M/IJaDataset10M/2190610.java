package org.tolven.api.vestibule;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.tolven.api.security.AbstractVestibule;
import org.tolven.core.entity.AccountUser;
import org.tolven.core.entity.TolvenUser;
import org.tolven.security.LoginLocal;

@ManagedBean(value = "org.tolven.api.vestibule.SelectAccountVestibule")
public class SelectAccountVestibule extends AbstractVestibule {

    public static String VESTIBULE_NAME = "org.tolven.vestibule.selectaccount";

    @EJB
    private LoginLocal loginBean;

    @Override
    public void abort(ServletRequest servletRequest) {
    }

    @Override
    public void enter(ServletRequest servletRequest) {
    }

    @Override
    public void exit(ServletRequest servletRequest) {
    }

    @Override
    public String getName() {
        return VESTIBULE_NAME;
    }

    @Override
    public String validate(ServletRequest servletRequest) {
        Long proposedAccountId = getSessionProposedAccountUserId();
        if (proposedAccountId == null) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            TolvenUser user = loginBean.findUser(request.getUserPrincipal().getName());
            if (user == null) {
                throw new RuntimeException("Could not find TolvenUser: " + request.getUserPrincipal());
            }
            AccountUser defaultAccountUser = findDefaultAccountUser(user);
            if (defaultAccountUser == null) {
                return "/vestibule/selectAccount.jsf";
            } else {
                setSessionProposedAccountUserId(defaultAccountUser.getId());
            }
        }
        return null;
    }
}
