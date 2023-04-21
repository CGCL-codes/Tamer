package wsmg.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import wsmg.config.WSMGParameter;
import xsul.MLogger;

/**
 * @author Chathura Herath (cherath@cs.indiana.edu)
 */
public class MessageBoxDB {

    private static final MLogger logger = MLogger.getLogger();

    private static JdbcStorage db = new JdbcStorage(WSMGParameter.DB_CONFIG_NAME, true);

    public static final String TABLE_NAME = "msgbox";

    public static final String SQL_INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + " (xml, msgboxid) " + "VALUES (?,?)";

    public static final String SQL_DELETE_ALL_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE msgboxid='";

    public static final String SQL_DELETE_ONE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE id ='";

    public static final String SQL_SELECT_STATEMENT1 = "SELECT * FROM " + TABLE_NAME + " WHERE msgboxid='";

    public static final String SQL_SELECT_STATEMENT2 = "' ORDER BY id";

    public MessageBoxDB() {
    }

    public void addMessage(String msgBoxID, String message) throws Exception {
        Connection connection = db.connect();
        PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_STATEMENT);
        byte[] buffer;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(output);
        out.writeObject(message);
        buffer = output.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        stmt.setBinaryStream(1, in, buffer.length);
        stmt.setString(2, msgBoxID);
        db.executeUpdate(stmt);
        stmt.close();
        connection.commit();
        db.closeConnection(connection);
    }

    public void removeAllMessages(String key) throws SQLException {
        Connection connection = db.connect();
        PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_ALL_STATEMENT + key + "'");
        db.executeUpdate(stmt);
        stmt.close();
        connection.commit();
        db.closeConnection(connection);
    }

    public String removeOneMessage(String key) throws SQLException, IOException {
        Connection connection = db.connect();
        PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_STATEMENT1 + key + SQL_SELECT_STATEMENT2);
        ResultSet resultSet = stmt.executeQuery();
        String obj = null;
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            InputStream in = resultSet.getAsciiStream(2);
            ObjectInputStream s = new ObjectInputStream(in);
            try {
                obj = (String) s.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        resultSet.close();
        stmt.close();
        stmt = connection.prepareStatement(SQL_DELETE_ONE_STATEMENT + key + "'");
        db.executeUpdate(stmt);
        stmt.close();
        connection.commit();
        db.closeConnection(connection);
        return obj;
    }
}
