package org.slasoi.businessManager.common.dao;

import java.util.List;
import org.slasoi.businessManager.common.model.EmUsers;

public interface UserDAO extends AbstractHibernateDAO<EmUsers, Long> {

    public List<EmUsers> getUsersByLoginPass(String user, String pass);

    public EmUsers getUser(String login, String pass);

    public EmUsers getUserByParty(Long partyId, String userLogin);
}
