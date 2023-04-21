package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.PeriodsDAO;
import at.rc.tacos.model.Period;

public class PeriodsDAOSQL implements PeriodsDAO {

    private final DataSource source = DataSource.getInstance();

    private final SQLQueries queries = SQLQueries.getInstance();

    @Override
    public Period getPeriod(int periodID) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.periodByID"));
            query.setInt(1, periodID);
            final ResultSet rs = query.executeQuery();
            System.out.println("periods dao sql nach executeQuery");
            if (rs.next()) {
                Period period = new Period();
                period.setPeriod(rs.getString("period"));
                period.setServiceTypeCompetence(rs.getString("serviceTypeCompetence"));
                return period;
            }
            return null;
        } finally {
            connection.close();
        }
    }

    @Override
    public int addPeriod(Period period) throws SQLException {
        Connection connection = source.getConnection();
        try {
            int id = 0;
            final PreparedStatement stmt = connection.prepareStatement(queries.getStatment("get.nextPeriodID"));
            final ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return -1;
            id = rs.getInt(1);
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.period"));
            query.setInt(1, id);
            query.setString(2, period.getPeriod());
            query.setString(3, period.getServiceTypeCompetence());
            if (query.executeUpdate() == 0) return -1;
            return id;
        } finally {
            connection.close();
        }
    }

    @Override
    public List<Period> getPeriodListByServiceTypeCompetence(String serviceTypeCompetence) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.periods"));
            query.setString(1, serviceTypeCompetence);
            final ResultSet rs = query.executeQuery();
            List<Period> periods = new ArrayList<Period>();
            while (rs.next()) {
                Period period = new Period();
                period.setPeriodId(rs.getInt("period_ID"));
                period.setPeriod(rs.getString("period"));
                period.setServiceTypeCompetence(rs.getString("serviceTypeCompetence"));
                periods.add(period);
            }
            return periods;
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean removePeriod(int id) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("delete.period"));
            query.setInt(1, id);
            if (query.executeUpdate() == 0) return false;
            return true;
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean updatePeriod(Period period) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("update.period"));
            query.setString(1, period.getPeriod());
            query.setString(2, period.getServiceTypeCompetence());
            query.setInt(3, period.getPeriodId());
            if (query.executeUpdate() == 0) return false;
            return true;
        } finally {
            connection.close();
        }
    }
}
