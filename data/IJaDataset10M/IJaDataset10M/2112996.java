package ca.llsutherland.squash.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import ca.llsutherland.squash.domain.Administrator;
import ca.llsutherland.squash.domain.Clock;
import ca.llsutherland.squash.domain.DomainObject;
import ca.llsutherland.squash.domain.Gym;
import ca.llsutherland.squash.domain.helper.GymsHelper;
import ca.llsutherland.squash.exceptions.ValidationException;
import ca.llsutherland.squash.utils.StringUtils;

public class GymsDao extends BaseDao {

    private static GymsDao gymDao = new GymsDao();

    public static GymsDao getInstance() {
        return gymDao;
    }

    protected Gym buildGym(String sql) {
        Gym gym = null;
        ResultSet rs = super.findByQuery(sql);
        try {
            if (rs.next()) {
                String name = rs.getString("NAME");
                Clock dateCreated = new Clock(rs.getTimestamp("DATE_CREATED").getTime());
                Clock dateUpdated = new Clock(rs.getTimestamp("DATE_UPDATED").getTime());
                String phoneNumber = rs.getString("PHONE_NUMBER");
                String slogan = rs.getString("SLOGAN");
                String streetAddress = rs.getString("STREET_ADDRESS");
                Long id = Long.valueOf(rs.getLong("ID"));
                Administrator lastUpdatedBy = AdministratorsDao.getInstance().findById(rs.getLong("ADMINISTRATOR_ID"));
                gym = GymsHelper.createNewGym(name, dateCreated, dateUpdated, phoneNumber, slogan, streetAddress, id, lastUpdatedBy);
            }
            rs.close();
        } catch (SQLException e) {
            throw new ValidationException(e.getMessage() + " in buildGym()");
        } finally {
            rs = null;
        }
        return gym;
    }

    public Gym update(Gym gym) {
        String sql = "UPDATE " + getUnderlyingTableName() + " SET ID = " + gym.getId() + ", NAME = " + StringUtils.addSingleQuotes(gym.getName()) + ", DATE_UPDATED = " + StringUtils.addSingleQuotes(gym.getDateUpdated().toString()) + ", SLOGAN = " + StringUtils.addSingleQuotes(gym.getSlogan()) + ", STREET_ADDRESS = " + StringUtils.addSingleQuotes(gym.getStreetAddress()) + ", ADMINISTRATOR_ID = " + gym.getLastUpdatedBy().getId() + " WHERE ID = " + gym.getId() + ";";
        super.update(sql);
        return gym;
    }

    public Gym create(Gym gym) {
        String sql = "INSERT INTO " + getUnderlyingTableName() + "(NAME, DATE_CREATED, DATE_UPDATED, SLOGAN, STREET_ADDRESS, PHONE_NUMBER, ADMINISTRATOR_ID) VALUES (" + StringUtils.addSingleQuotes(gym.getName()) + ", " + StringUtils.addSingleQuotes(gym.getDateCreated().toString()) + ", " + StringUtils.addSingleQuotes(gym.getDateUpdated().toString()) + ", " + StringUtils.addSingleQuotes(gym.getSlogan()) + ", " + StringUtils.addSingleQuotes(gym.getStreetAddress()) + ", " + StringUtils.addSingleQuotes(gym.getPhoneNumber()) + ", " + gym.getLastUpdatedBy().getId() + ")";
        Long lastInsertedId = super.create(sql);
        return findById(lastInsertedId);
    }

    protected Gym findById(Long id) {
        String findByIdQuery = getFindByIdQuery(id);
        return buildGym(findByIdQuery);
    }

    @Override
    public String getUnderlyingTableName() {
        return "GYMS";
    }

    public void delete(DomainObject gym) {
        String sql = "DELETE FROM " + getUnderlyingTableName() + " WHERE ID = " + gym.getId() + ";";
        super.delete(sql, gym.getId());
    }

    public DomainObject findGym(DomainObject gym) {
        return findById(gym.getId());
    }
}
