package com.scholardesk.abstracts.welcome;

import javax.servlet.http.HttpServletRequest;
import com.scholardesk.abstracts.AbstractsTask;
import com.scholardesk.abstracts.constants.AccountRole;
import com.scholardesk.abstracts.mapper.RoleMapper;
import com.scholardesk.abstracts.model.Role;

public class AssociateTask extends AbstractsTask {

    @Override
    public void process() {
        if (account == null) return;
        if (!account.isRole(AccountRole.USER.getId())) {
            Role _role = account.addRole(AccountRole.USER.getId(), AccountRole.ACTIVE);
            new RoleMapper().insert(_role);
        }
        account.setProgramId(program.getId());
        http_session.setAttribute("account", account);
    }

    @Override
    public String getRedirect() {
        return "welcome";
    }

    @Override
    public HttpServletRequest setRequest() {
        return http_request;
    }
}
