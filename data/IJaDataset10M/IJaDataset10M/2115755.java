package net.sourceforge.customercare.client.server.entities.phonenumbertype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sourceforge.customercare.client.server.entities.EntityCore;
import net.sourceforge.customercare.client.server.entities.Entry;
import net.sourceforge.customercare.client.server.exceptions.CustomerCareException;

/**
 * phonenumbertype core-class
 */
public class PhonenumberTypeCore implements EntityCore {

    private Connection cnn;

    private static final String TABLE_NAME = "tbl_phonenumber_type";

    public PhonenumberTypeCore(Connection cnn) {
        this.cnn = cnn;
    }

    public Connection getConnection() {
        return cnn;
    }

    public boolean insert(Entry entry) throws CustomerCareException {
        PhonenumberType pnt = (PhonenumberType) entry;
        boolean success = false;
        try {
            PreparedStatement statement = cnn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?);");
            statement.setInt(1, pnt.getId());
            statement.setString(2, "");
            statement.setString(3, "");
            statement.setInt(4, 0);
            success = statement.execute();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return success;
    }

    public boolean update(Entry entry) throws CustomerCareException {
        PhonenumberType pnt = (PhonenumberType) entry;
        PreparedStatement statement;
        boolean success = false;
        try {
            statement = cnn.prepareStatement("UPDATE " + TABLE_NAME + " SET name = ?, description = ?, " + "chgctr = ? WHERE pnt_id = ?");
            statement.setString(1, pnt.getName());
            statement.setString(2, pnt.getDescription());
            statement.setInt(3, pnt.getChgctr());
            statement.setInt(4, pnt.getId());
            success = statement.execute();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return success;
    }

    public boolean delete(Integer id) throws CustomerCareException {
        PreparedStatement statement;
        boolean success = false;
        try {
            statement = cnn.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE pnt_id = ?");
            statement.setInt(1, id);
            success = statement.execute();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return success;
    }

    public ResultSet select(Entry entry) throws CustomerCareException {
        ResultSet rs = null;
        PhonenumberType pnt = (PhonenumberType) entry;
        PreparedStatement query = createSelectStatement(pnt);
        try {
            rs = query.executeQuery();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return rs;
    }

    private PreparedStatement createSelectStatement(PhonenumberType pnt) throws CustomerCareException {
        PreparedStatement ps = null;
        try {
            if (!pnt.getId().equals(0)) {
                ps = cnn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE (pnt_id = ?);");
                ps.setInt(1, pnt.getId());
            } else {
                ps = cnn.prepareStatement("SELECT * FROM " + TABLE_NAME + ";");
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return ps;
    }
}
