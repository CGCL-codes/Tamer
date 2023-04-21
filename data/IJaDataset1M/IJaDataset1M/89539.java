package fhj.itm05.seminarswe.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import fhj.itm05.seminarswe.domain.AvailableCategory;
import fhj.itm05.seminarswe.domain.UserData;

;

public class UserDAOHsqldb implements UserDAO {

    private static UserDAOHsqldb instance;

    public static final String QUERIES_BUNDLE_PATH = "fhj.itm05.seminarswe.database.queries";

    /**
	 * Returns an instance of singleton UserDAOHsqldb 
	 * @return instance of UserDAOHsqldb 
	 */
    public static UserDAOHsqldb getInstance() {
        if (instance == null) {
            instance = new UserDAOHsqldb();
        }
        return instance;
    }

    private UserDAOHsqldb() {
    }

    @Override
    public List<UserData> getRegisteredUsers() {
        final List<UserData> users = new ArrayList<UserData>();
        try {
            final PreparedStatement listUserData = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.userdata"));
            final ResultSet listUserResult = listUserData.executeQuery();
            while (listUserResult.next()) {
                final UserData userData = new UserData();
                userData.setId(listUserResult.getInt("persondata_pd_id"));
                userData.setUserName(listUserResult.getString("username"));
                users.add(userData);
            }
            for (UserData user : users) {
                final PreparedStatement personStatement = DataSource.getInstance().getConnection().prepareStatement("select * from persondata where pd_id=?");
                personStatement.setInt(1, user.getId());
                final ResultSet personResult = personStatement.executeQuery();
                if (personResult.next()) {
                    user.setTitle(personResult.getString("title"));
                    user.setSex(personResult.getString("sex"));
                    user.setFirstName(personResult.getString("first_name"));
                    user.setLastName(personResult.getString("last_name"));
                    user.setMail(personResult.getString("email"));
                }
            }
            return users;
        } catch (SQLException sqle) {
            System.out.println("Failed query the user list from the database");
            System.out.println(sqle.getErrorCode() + " : " + sqle.getMessage());
            return null;
        }
    }

    /**
	 * returns a list of currently used categories
	 * @param user name from current session
	 * @return list of categories or empty list
	 */
    @Override
    public List<String> listCurrentCategories(String user) {
        final List<String> categoryList = new ArrayList<String>();
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.currentcategories"));
            query.setString(1, user);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                final String category = new String(rs.getString(1));
                categoryList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    /**
	 * returns a list of available categories
	 * @param user name from current session
	 * @return list of categories or empty list
	 */
    @Override
    public List<AvailableCategory> listAvailableCategories(String user) {
        final List<AvailableCategory> categoryList = new ArrayList<AvailableCategory>();
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("list.availablecategories"));
            query.setString(1, user);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                final AvailableCategory category = new AvailableCategory(rs.getLong(1), rs.getString(2));
                categoryList.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    @Override
    public UserData getUser(String username) {
        try {
            UserData userData = new UserData();
            final PreparedStatement userStatement = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("getPersonIdFromUserdata"));
            userStatement.setString(1, username);
            final ResultSet userResult = userStatement.executeQuery();
            if (!userResult.next()) return null;
            int userDataId = userResult.getInt(1);
            final PreparedStatement personStatement = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("getPersonDataById"));
            personStatement.setInt(1, userDataId);
            final ResultSet personResult = personStatement.executeQuery();
            if (!personResult.next()) return null;
            System.out.println("get a person result");
            userData.setId(userDataId);
            userData.setUserName(username);
            userData.setTitle(personResult.getString("title"));
            userData.setSex(personResult.getString("sex"));
            userData.setFirstName(personResult.getString("first_name"));
            userData.setLastName(personResult.getString("last_name"));
            userData.setMail(personResult.getString("email"));
            return userData;
        } catch (SQLException sqle) {
            System.out.println("Failed to get the userdata for: " + username);
            System.out.println(sqle.getErrorCode() + " : " + sqle.getMessage());
            return null;
        }
    }

    @Override
    public boolean checkUserLogin(String username, String pwdHash) {
        try {
            System.out.println(DataSource.getInstance());
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("checkUserLogin"));
            query.setString(1, username);
            query.setString(2, pwdHash);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addUser(UserData userData) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("addPersonData"));
            if (userData.getId() == -1) query.setString(1, null); else {
                query.setInt(1, userData.getId());
            }
            query.setString(2, userData.getSex());
            query.setString(3, userData.getTitle());
            query.setString(4, userData.getFirstName());
            query.setString(5, userData.getLastName());
            query.setString(6, userData.getMail());
            query.executeUpdate();
            final PreparedStatement query1 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("getPdId"));
            query1.setString(1, userData.getMail());
            final ResultSet rsPerson = query1.executeQuery();
            Integer pd_id = -1;
            if (rsPerson.next()) pd_id = rsPerson.getInt("pd_id");
            if (pd_id != null) {
                final PreparedStatement query2 = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("addUserData"));
                query2.setString(1, userData.getUserName());
                query2.setString(2, userData.getPassword1());
                query2.setInt(3, pd_id);
                query2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean editUser(UserData userData) {
        try {
            final PreparedStatement updatePerson = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("editPersonData"));
            updatePerson.setString(1, userData.getSex());
            updatePerson.setString(2, userData.getTitle());
            updatePerson.setString(3, userData.getFirstName());
            updatePerson.setString(4, userData.getLastName());
            updatePerson.setString(5, userData.getMail());
            updatePerson.setInt(6, userData.getId());
            updatePerson.executeUpdate();
            return true;
        } catch (SQLException sqle) {
            System.out.println("Failed to update the user " + userData.getUserName());
            System.out.println(sqle.getErrorCode() + " : " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(UserData userData) {
        try {
            PreparedStatement loginStatement = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("deleteUserData"));
            loginStatement.setString(1, userData.getUserName());
            int result = loginStatement.executeUpdate();
            if (result == 0) return false;
            PreparedStatement personStatement = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("deletePersonData"));
            personStatement.setInt(1, userData.getId());
            result = personStatement.executeUpdate();
            if (result == 0) return false;
            return true;
        } catch (SQLException sqle) {
            System.out.println("Failed to delete the user: " + userData.getUserName());
            System.out.println(sqle.getErrorCode() + " : " + sqle.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkUserId(Integer id) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("checkPersonalId"));
            query.setInt(1, id);
            final ResultSet rs = query.executeQuery();
            while (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public boolean checkUserData(UserData userData) {
        try {
            final PreparedStatement query = DataSource.getInstance().getConnection().prepareStatement(ResourceBundle.getBundle(UserDAOHsqldb.QUERIES_BUNDLE_PATH).getString("checkUserData"));
            query.setString(1, userData.getUserName());
            query.setString(2, userData.getTitle());
            query.setString(3, userData.getFirstName());
            query.setString(4, userData.getLastName());
            query.setString(5, userData.getSex());
            query.setString(6, userData.getMail());
            final ResultSet rs = query.executeQuery();
            while (rs.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
