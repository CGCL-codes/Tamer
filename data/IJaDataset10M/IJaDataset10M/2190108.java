package tms.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.servlet.http.HttpSession;
import tms.client.accesscontrol.User;
import tms.client.accesscontrol.UserCategory;
import tms.client.exceptions.DataOperationException;
import tms.client.exceptions.RetrievalException;
import tms.client.i18n.TMSMessages;
import tms.server.i18n.MessagesFactory;
import tms.server.logging.LogUtility;

public class UserCategoriesManager {

    private static TMSMessages _messages = MessagesFactory.createInstance(TMSMessages.class);

    public static ArrayList<UserCategory> getAllUserCategories(Connection connection) throws SQLException {
        ArrayList<UserCategory> userCategories = new ArrayList<UserCategory>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select * from tms.usercategories");
        while (results.next()) {
            UserCategory category = newUserCategory(results);
            userCategories.add(category);
        }
        return userCategories;
    }

    /**
	 * Retrieves all the user categories including the users the belong to each.
	 * Used by the User & Categories panel in the Administration Interface.
	 * @param connection
	 * @return
	 * @throws SQLException 
	 * @throws RetrievalException
	 */
    public static ArrayList<UserCategory> getAllUserCategoriesWithUsers(Connection connection, String authToken, HttpSession session) throws SQLException, RetrievalException {
        ArrayList<UserCategory> userCategories = new ArrayList<UserCategory>();
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select * from tms.usercategories");
        while (results.next()) {
            int userCategoryId = results.getInt("usercategoryid");
            ArrayList<User> users = UsersManager.getAllUsersByCategory(connection, userCategoryId);
            UserCategory category = newUserCategoryWithUsers(results, users);
            userCategories.add(category);
        }
        return userCategories;
    }

    public static UserCategory getUserCategory(Connection connection, int userCategoryId) throws RetrievalException, SQLException {
        UserCategory userCategory = null;
        String sql = "select * from tms.usercategories where usercategoryid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userCategoryId);
        ResultSet results = statement.executeQuery();
        if (results.next()) userCategory = newUserCategory(results);
        return userCategory;
    }

    public static boolean getUserCategoryIsAdmin(Connection connection, int userCategoryId, String authToken, HttpSession session) throws RetrievalException {
        boolean isAdmin = false;
        try {
            String sql = "select isadmin from tms.usercategories where usercategoryid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userCategoryId);
            ResultSet results = statement.executeQuery();
            if (results.next()) isAdmin = results.getBoolean("isadmin");
        } catch (Exception e) {
            LogUtility.log(Level.SEVERE, session, _messages.log_user_cat_is_admin(Long.toString(userCategoryId)), e, authToken);
            throw new RetrievalException(e);
        }
        return isAdmin;
    }

    public static UserCategory createUserCategory(Connection connection, UserCategory userCategory) throws DataOperationException, SQLException {
        UserCategory created = null;
        String sql = "insert into tms.usercategories (usercategory,isadmin) values(?,?) returning usercategoryid";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, userCategory.getUserCategoryName());
        statement.setBoolean(2, userCategory.isAdmin());
        ResultSet returned = statement.executeQuery();
        returned.next();
        created = getUserCategory(connection, returned.getInt("usercategoryid"));
        return created;
    }

    public static void updateUserCategory(Connection connection, UserCategory userCategory) throws DataOperationException, SQLException {
        String sql = "update tms.usercategories set usercategory = ?, isadmin = ? where usercategoryid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, userCategory.getUserCategoryName());
        statement.setBoolean(2, userCategory.isAdmin());
        statement.setInt(3, userCategory.getUserCategoryId());
        statement.executeUpdate();
    }

    public static void deleteUserCategory(Connection connection, UserCategory userCategory, String authToken, HttpSession session) throws DataOperationException {
        try {
            String sql = "delete from tms.usercategories where usercategoryid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userCategory.getUserCategoryId());
            statement.executeUpdate();
        } catch (Exception e) {
            LogUtility.log(Level.SEVERE, session, _messages.log_delete_user_cat(userCategory.getUserCategoryName()), e, authToken);
            throw new DataOperationException(e);
        }
    }

    private static UserCategory newUserCategory(ResultSet results) throws SQLException {
        UserCategory userCategory = new UserCategory();
        userCategory.setUserCategoryId(results.getInt("usercategoryid"));
        userCategory.setUserCategoryName(results.getString("usercategory"));
        userCategory.setAdmin(results.getBoolean("isadmin"));
        return userCategory;
    }

    private static UserCategory newUserCategoryWithUsers(ResultSet results, ArrayList<User> users) throws SQLException {
        UserCategory userCategory = new UserCategory();
        userCategory.setUserCategoryId(results.getInt("usercategoryid"));
        userCategory.setUserCategoryName(results.getString("usercategory"));
        userCategory.setAdmin(results.getBoolean("isadmin"));
        userCategory.setUsers(users);
        return userCategory;
    }
}
