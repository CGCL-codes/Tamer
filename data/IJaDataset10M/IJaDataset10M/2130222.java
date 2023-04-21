package cn.jsprun.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import cn.jsprun.domain.Crons;

public interface CronsDao {

    public List<Crons> queryAllCrons();

    public Crons queryCronsById(Short id);

    public List<Crons> queryCronsByCondition(String sqlStatement);

    public boolean addCrons(Crons crons);

    public boolean updateCrons(Crons crons);

    public boolean updateCrons(List<Crons> cronsList);

    public boolean delAnyCrons(List<Crons> cronsListDelete);

    public Connection getConnection() throws Exception;

    public void begingTransaction(Connection connection, int level) throws SQLException;

    public List<Map<String, String>> getRunningCronsInfo(Connection connection, int timestamp) throws SQLException;

    public Map<String, String> getRunningCronInfo(Connection connection, Object cronsid) throws SQLException;

    public void commitTransaction(Connection connection);

    public void rollbackTransaction(Connection connection);

    public void closeConnection(Connection connection);

    public void execute(Connection connection, String sql) throws SQLException;

    public List<Map<String, String>> executeQuery(Connection connection, String sql) throws SQLException;
}
