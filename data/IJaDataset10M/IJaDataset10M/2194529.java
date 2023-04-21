package at.fhj.itm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import at.fhj.itm.model.Location;

public class MySqlLocationDAO implements LocationDAO {

    private static final String UPDATE_LOCATION = "UPDATE locations SET zip = ?, city = ? WHERE id = ?";

    private static final String INSERT_LOCATION = "INSERT INTO locations (zip,city) VALUES(?,?)";

    private static final String SELECT_ALL = "SELECT id, zip, city FROM locations";

    private static final String SELECT_BY_ID = "SELECT id, zip, city FROM locations WHERE id = ?";

    private static final String DELETE_BY_ID = "DELETE FROM locations WHERE id = ?";

    public MySqlLocationDAO() {
    }

    @Override
    public String getIdentifier() {
        return "MYSQL";
    }

    private Location getLocationFromResultSet(ResultSet set) throws SQLException {
        int id = set.getInt("id");
        int zip = set.getInt("zip");
        String city = set.getString("city");
        return new Location(id, zip, city);
    }

    @Override
    public void update(Location entity, Connection connection) {
        try {
            if (entity.getId() >= 0) {
                PreparedStatement updateStmt = connection.prepareStatement(UPDATE_LOCATION);
                updateStmt.setLong(1, entity.getZip());
                updateStmt.setString(2, entity.getCity());
                updateStmt.setInt(3, entity.getId());
                updateStmt.execute();
                updateStmt.close();
            }
            if (entity.getId() == -1) {
                PreparedStatement updateStmt = connection.prepareStatement(INSERT_LOCATION);
                updateStmt.setLong(1, entity.getZip());
                updateStmt.setString(2, entity.getCity());
                updateStmt.execute();
                int id = MySqlUtil.getLastInsertedID(connection);
                entity.setId(id);
                updateStmt.close();
            }
        } catch (SQLException e) {
            throw new DAOException("Error updating/inserting DAO", e);
        }
    }

    @Override
    public void delete(Location entity, Connection connection) {
        if (entity.getId() < 0) {
            throw new DAOException("Location can't be deleted because it isn't persisted.");
        }
        try {
            PreparedStatement stmt = connection.prepareStatement(DELETE_BY_ID);
            stmt.setInt(1, entity.getId());
            stmt.execute();
            stmt.close();
            entity.setId(-1);
        } catch (SQLException ex) {
            throw new DAOException("Error deleting location", ex);
        }
    }

    @Override
    public List<Location> selectAll(Connection connection) {
        try {
            List<Location> locations = new ArrayList<Location>();
            PreparedStatement stmt = connection.prepareStatement(SELECT_ALL);
            ResultSet set = stmt.executeQuery();
            while (set.next()) {
                Location curLocation = getLocationFromResultSet(set);
                locations.add(curLocation);
            }
            set.close();
            stmt.close();
            return Collections.unmodifiableList(locations);
        } catch (SQLException ex) {
            throw new DAOException("Error getting all locations.", ex);
        }
    }

    @Override
    public Location getByID(Integer id, Connection connection) {
        try {
            PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID);
            stmt.setInt(1, id);
            ResultSet set = stmt.executeQuery();
            set.next();
            Location loc = getLocationFromResultSet(set);
            set.close();
            stmt.close();
            return loc;
        } catch (SQLException ex) {
            throw new DAOException("Error getting  location by id..", ex);
        }
    }
}
