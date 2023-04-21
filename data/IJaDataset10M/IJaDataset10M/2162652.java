package net.sourceforge.customercare.client.server.entities.personproduct;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sourceforge.customercare.client.server.entities.EntityCore;
import net.sourceforge.customercare.client.server.entities.Entry;
import net.sourceforge.customercare.client.server.exceptions.CustomerCareException;

/**
 * person product core-class
 */
public class PersonProductCore implements EntityCore {

    private Connection cnn;

    private static final String TABLE_NAME = "tbl_person_product";

    public PersonProductCore(Connection cnn) {
        this.cnn = cnn;
    }

    public Connection getConnection() {
        return cnn;
    }

    public boolean insert(Entry entry) throws CustomerCareException {
        PersonProduct pp = (PersonProduct) entry;
        boolean success = false;
        try {
            PreparedStatement statement = cnn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?);");
            statement.setInt(1, pp.getId());
            statement.setInt(2, 0);
            statement.setInt(3, 0);
            statement.setInt(4, 0);
            statement.setInt(5, 0);
            success = statement.execute();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return success;
    }

    public boolean update(Entry entry) throws CustomerCareException {
        PersonProduct pp = (PersonProduct) entry;
        PreparedStatement statement;
        boolean success = false;
        try {
            statement = cnn.prepareStatement("UPDATE " + TABLE_NAME + " SET per_id = ?, prd_id = ?, " + "quantity = ?, chgctr = ? " + "WHERE pp_id = ?");
            statement.setInt(1, pp.getPerId());
            statement.setInt(2, pp.getPrdId());
            statement.setInt(3, pp.getQuantity());
            statement.setInt(4, pp.getChgctr());
            statement.setInt(5, pp.getId());
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
            statement = cnn.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE pp_id = ?");
            statement.setInt(1, id);
            success = statement.execute();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return success;
    }

    public ResultSet select(Entry entry) throws CustomerCareException {
        ResultSet rs = null;
        PersonProduct pp = (PersonProduct) entry;
        PreparedStatement query = createSelectStatement(pp);
        try {
            rs = query.executeQuery();
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return rs;
    }

    private PreparedStatement createSelectStatement(PersonProduct pp) throws CustomerCareException {
        PreparedStatement ps = null;
        try {
            if (!pp.getId().equals(0)) {
                ps = cnn.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE (pp_id = ?);");
                ps.setInt(1, pp.getId());
            } else {
                ps = cnn.prepareStatement("SELECT * FROM " + TABLE_NAME + ";");
            }
        } catch (SQLException sqlEx) {
            throw new CustomerCareException(CustomerCareException.DATABASE_ERROR);
        }
        return ps;
    }
}
