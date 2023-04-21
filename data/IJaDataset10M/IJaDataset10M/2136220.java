package de.objectcode.time4u.server.web.gwt.admin.server.dao;

import java.util.List;
import de.objectcode.time4u.server.web.gwt.admin.client.service.UserAccount;

public interface IAdminUserAccountDao {

    UserAccount.Page findUserAccountPage(int pageNumber, int pageSize, UserAccount.Projections sorting, boolean ascending);

    List<UserAccount> findUserAccountsOfPerson(String personId);
}
