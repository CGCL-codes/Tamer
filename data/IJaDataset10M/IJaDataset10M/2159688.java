package bank.model.dao.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import bank.model.dao.ClientDAO;
import bank.model.pool.DBConnectionPool;
import bank.model.properties.DBTables;
import bank.model.sql.builder.SqlString;
import bank.model.vo.Client;

public class MySqlClientDAO implements ClientDAO, DBTables {

    private DBConnectionPool connectionPool = DBConnectionPool.getInstance();

    private SqlString sql = new SqlString();

    public List getAll() throws Exception {
        List clients = new ArrayList();
        sql.clear_All();
        sql.set_Select();
        sql.set_Table_Name(TBL_CLIENTS);
        sql.set_all();
        sql.generate();
        Connection con = connectionPool.getConnection();
        Statement s = con.prepareStatement(sql.get_Command());
        ResultSet resultSet = s.executeQuery(sql.get_Command());
        while (resultSet.next()) {
            Client client = new Client();
            client.setId(resultSet.getInt(CLIENT_ID));
            client.setFirstname(resultSet.getString(CLIENT_FIRSTNAME));
            client.setLastname(resultSet.getString(CLIENT_LASTNAME));
            client.setSubname(resultSet.getString(CLIENT_SUBNAME));
            clients.add(client);
        }
        connectionPool.freeConnection(con);
        return clients;
    }

    public List findByName(int searchfield, String text) throws Exception {
        List clients = new ArrayList();
        sql.clear_All();
        sql.set_Select();
        sql.set_Table_Name(TBL_CLIENTS);
        sql.set_all();
        sql.add_Cond(CLIENT_LASTNAME, text);
        sql.generate();
        Connection con = connectionPool.getConnection();
        Statement s = con.prepareStatement(sql.get_Command());
        ResultSet resultSet = s.executeQuery(sql.get_Command());
        while (resultSet.next()) {
            Client client = new Client();
            client.setId(resultSet.getInt(CLIENT_ID));
            client.setFirstname(resultSet.getString(CLIENT_FIRSTNAME));
            client.setLastname(resultSet.getString(CLIENT_LASTNAME));
            client.setSubname(resultSet.getString(CLIENT_SUBNAME));
            clients.add(client);
        }
        connectionPool.freeConnection(con);
        return clients;
    }
}
