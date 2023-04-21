package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.db.SQLQueries;
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;

public class StaffMemberDAOSQL implements StaffMemberDAO {

    private final DataSource source = DataSource.getInstance();

    private final SQLQueries queries = SQLQueries.getInstance();

    private final LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();

    private final CompetenceDAO competenceDAO = DaoFactory.SQL.createCompetenceDAO();

    private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.SQL.createMobilePhoneDAO();

    @Override
    public boolean addStaffMember(StaffMember staffMember) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("insert.staffmember"));
            query.setInt(1, staffMember.getStaffMemberId());
            query.setInt(2, staffMember.getPrimaryLocation().getId());
            query.setString(3, staffMember.getFirstName());
            query.setString(4, staffMember.getLastName());
            query.setBoolean(5, staffMember.isMale());
            query.setString(6, staffMember.getPhone1());
            query.setString(7, staffMember.getPhone2());
            query.setString(8, staffMember.getBirthday());
            query.setString(9, staffMember.getEMail());
            query.setString(10, staffMember.getStreetname());
            query.setString(11, staffMember.getCityname());
            query.setString(12, staffMember.getUserName());
            if (query.executeUpdate() == 0) return false;
            if (!updateCompetenceList(staffMember)) return false;
            if (!updateMobilePhoneList(staffMember)) return false;
            return true;
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean updateStaffMember(StaffMember staffmember) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query1 = connection.prepareStatement(queries.getStatment("update.staffmember"));
            query1.setInt(1, staffmember.getPrimaryLocation().getId());
            query1.setString(2, staffmember.getFirstName());
            query1.setString(3, staffmember.getLastName());
            query1.setBoolean(4, staffmember.isMale());
            query1.setString(5, staffmember.getPhone1());
            query1.setString(6, staffmember.getPhone2());
            query1.setString(7, staffmember.getBirthday());
            query1.setString(8, staffmember.getEMail());
            query1.setString(9, staffmember.getStreetname());
            query1.setString(10, staffmember.getCityname());
            query1.setInt(11, staffmember.getStaffMemberId());
            if (query1.executeUpdate() == 0) return false;
            if (!updateCompetenceList(staffmember)) return false;
            if (!updateMobilePhoneList(staffmember)) return false;
            return true;
        } finally {
            connection.close();
        }
    }

    /**
	 * Returns the not locked staff members
	 */
    public List<StaffMember> getAllStaffMembers() throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembers"));
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }

    /**
	 * Returns the not locked staff members
	 */
    public List<StaffMember> getLockedStaffMembers() throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedStaffmembers"));
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }

    /**
	 * Returns the not locked staff members of the given lacation
	 */
    public List<StaffMember> getStaffMembersFromLocation(int locationId) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.staffmembersFromLocation"));
            query.setInt(1, locationId);
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }

    /**
	 * Returns the not locked staff members of the given lacation
	 */
    public List<StaffMember> getLockedStaffMembersFromLocation(int locationId) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedStaffmembersFromLocation"));
            query.setInt(1, locationId);
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }

    public StaffMember getStaffMemberByID(int id) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberByID"));
            query.setInt(1, id);
            final ResultSet rs = query.executeQuery();
            if (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int locationId = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(locationId));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                return staff;
            }
            return null;
        } finally {
            connection.close();
        }
    }

    public StaffMember getStaffMemberByUsername(String username) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("get.staffmemberbyUsername"));
            query.setString(1, username);
            final ResultSet rs = query.executeQuery();
            if (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int locationId = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(locationId));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                return staff;
            }
            return null;
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean updateCompetenceList(StaffMember staff) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.competencesOfStaffMember"));
            clearStmt.setInt(1, staff.getStaffMemberId());
            clearStmt.executeUpdate();
            final PreparedStatement assignQuery = connection.prepareStatement(queries.getStatment("add.competenceToStaffMember"));
            for (Competence comp : staff.getCompetenceList()) {
                assignQuery.setInt(1, staff.getStaffMemberId());
                assignQuery.setInt(2, comp.getId());
                if (assignQuery.executeUpdate() == 0) return false;
            }
            return true;
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean updateMobilePhoneList(StaffMember staff) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement clearStmt = connection.prepareStatement(queries.getStatment("delete.phonesOfStaffMember"));
            clearStmt.setInt(1, staff.getStaffMemberId());
            clearStmt.executeUpdate();
            final PreparedStatement assignPhonesStmt = connection.prepareStatement(queries.getStatment("insert.Phonestaffmember"));
            for (MobilePhoneDetail detail : staff.getPhonelist()) {
                assignPhonesStmt.setInt(1, staff.getStaffMemberId());
                assignPhonesStmt.setInt(2, detail.getId());
                if (assignPhonesStmt.executeUpdate() == 0) return false;
            }
            return true;
        } finally {
            connection.close();
        }
    }

    @Override
    public List<StaffMember> getLockedAndUnlockedStaffMembers() throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedAndUnlockedStaffmembers"));
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }

    @Override
    public List<StaffMember> getLockedAndUnlockedStaffMembersFromLocation(int locationId) throws SQLException {
        Connection connection = source.getConnection();
        try {
            final PreparedStatement query = connection.prepareStatement(queries.getStatment("list.lockedAndUnlockedStaffmembersFromLocation"));
            query.setInt(1, locationId);
            final ResultSet rs = query.executeQuery();
            List<StaffMember> staffMembers = new ArrayList<StaffMember>();
            while (rs.next()) {
                StaffMember staff = new StaffMember();
                staff.setStaffMemberId(rs.getInt("staffmember_ID"));
                staff.setLastName(rs.getString("lastname"));
                staff.setFirstName(rs.getString("firstname"));
                staff.setStreetname(rs.getString("street"));
                staff.setCityname(rs.getString("city"));
                staff.setMale(rs.getBoolean("sex"));
                staff.setPhone1(rs.getString("phone1"));
                staff.setPhone2(rs.getString("phone2"));
                staff.setBirthday(rs.getString("birthday"));
                staff.setEMail(rs.getString("email"));
                staff.setUserName(rs.getString("username"));
                int id = rs.getInt("primaryLocation");
                staff.setPrimaryLocation(locationDAO.getLocation(id));
                staff.setCompetenceList(competenceDAO.listCompetencesOfStaffMember(staff.getStaffMemberId()));
                staff.setPhonelist(mobilePhoneDAO.listMobilePhonesOfStaffMember(staff.getStaffMemberId()));
                staffMembers.add(staff);
            }
            return staffMembers;
        } finally {
            connection.close();
        }
    }
}
