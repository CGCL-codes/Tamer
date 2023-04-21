package org.inigma.waragent.crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.inigma.iniglet.utils.QueryHandler;
import org.inigma.utopia.Account;

final class AccountResultSetHandler {

    @SuppressWarnings("unused")
    private Account handleAccount(ResultSet rs, Connection conn) throws SQLException {
        String id = rs.getString("id");
        String provinceId = rs.getString("province_id");
        Account account = new Account(id, provinceId);
        account.setLastSync(QueryHandler.toCalendar(rs.getTimestamp("last_sync")));
        account.setSyncUrl(rs.getString("sync_url"));
        account.setSyncLogin(rs.getString("sync_login"));
        account.setSyncPassword(rs.getString("sync_password"));
        return account;
    }
}
