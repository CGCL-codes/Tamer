package org.localstorm.mcc.ejb.people.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.localstorm.mcc.ejb.dao.Guard;
import org.localstorm.mcc.ejb.dao.JdbcDaoHelper;
import org.localstorm.mcc.ejb.dao.QueriesLoader;
import org.localstorm.mcc.ejb.users.User;

/**
 * @author localstorm
 */
public class PeopleReportsDao {

    private static final Logger log = Logger.getLogger(PeopleReportsDao.class);

    private static final String REMAINS = "remains";

    private static final String ID = "id";

    private static final String FIRST_NAME = "name";

    private static final String LAST_NAME = "lname";

    private static final String PATRO_NAME = "pname";

    private static final String BIRTH = "birth_date";

    private DataSource ds;

    public PeopleReportsDao(DataSource ds) {
        Guard.checkDataSourceNotNull(ds);
        this.ds = ds;
    }

    public DashboardReportBean getDashboardReport(User u) throws SQLException {
        QueriesLoader ql = QueriesLoader.getInstance();
        String rptSql = ql.getQuery(QueriesLoader.PPL_DASHBOARD_REPORT);
        Connection conn = null;
        try {
            conn = ds.getConnection();
            Guard.checkConnectionNotNull(conn);
            PreparedStatement ps = conn.prepareStatement(rptSql);
            ps.setInt(1, u.getId());
            ResultSet rs = ps.executeQuery();
            DashboardReportBean drb = new DashboardReportBean();
            while (rs.next()) {
                Integer pid = JdbcDaoHelper.getInteger(rs, ID);
                String lname = JdbcDaoHelper.getString(rs, LAST_NAME);
                String fname = JdbcDaoHelper.getString(rs, FIRST_NAME);
                String pname = JdbcDaoHelper.getString(rs, PATRO_NAME);
                Date birth = JdbcDaoHelper.getDate(rs, BIRTH);
                int remains = JdbcDaoHelper.getInteger(rs, REMAINS);
                PersonWrapper pw = new PersonWrapper(pid);
                {
                    pw.setLastName(lname);
                    pw.setName(fname);
                    pw.setPatronymicName(pname);
                    pw.setBirthDate(birth);
                    pw.setRemains(remains);
                }
                drb.addPerson(pw);
            }
            return drb;
        } finally {
            JdbcDaoHelper.safeClose(conn, log);
        }
    }
}
