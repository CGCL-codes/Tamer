package manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import pojo.User;
import utilities.*;

public class UserManager {

    Connection con;

    /**
	 * Default Constructor with no parameters. 
	 */
    public UserManager() {
    }

    /**
	 * @param newUser to be inserted.
	 * @return Id of the newly inserted User. 
	 * @throws SQLException 
	 */
    public int insert(User newUser) throws SQLException {
        int key = 0;
        try {
            con = DBconn.getConnection();
            PreparedStatement pstat = con.prepareStatement("INSERT INTO User (FirstName,LastName,Email,Street,City,Country,State,Zip," + "Password,CreationDt,LastModifiedDt) VALUES (?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            pstat.setString(1, newUser.getFirstName());
            pstat.setString(2, newUser.getLastName());
            pstat.setString(3, newUser.getEmail());
            pstat.setString(4, newUser.getStreet());
            pstat.setString(5, newUser.getCity());
            pstat.setString(6, newUser.getCountry());
            pstat.setString(7, newUser.getState());
            pstat.setString(8, newUser.getZip());
            pstat.setString(9, newUser.getPassword());
            java.util.Date creationDt = DateUtilities.stringToDate(newUser.getCreationDt(), DateUtilities.DATETIME);
            java.util.Date lastModifiedDt = DateUtilities.stringToDate(newUser.getLastModifiedDt(), DateUtilities.DATETIME);
            pstat.setTimestamp(10, DateUtilities.convertUtilToSqlDate(creationDt));
            pstat.setTimestamp(11, DateUtilities.convertUtilToSqlDate(lastModifiedDt));
            pstat.executeUpdate();
            ResultSet keys = pstat.getGeneratedKeys();
            keys.next();
            key = keys.getInt(1);
            pstat.close();
            con.close();
        } catch (SQLException ex) {
            if (con != null && !con.isClosed()) {
                con.close();
            }
            throw ex;
        }
        return key;
    }

    /**
	 * @param existingUser to be deleted.
	 * @return true: User deleted Successfully, else false.
	 */
    public boolean delete(User existingUser) {
        return true;
    }

    /**
	 * @param existingUser to be updated.
	 * @return true: User updated Successfully, else false.
	 */
    public boolean update(User existingUser) {
        return true;
    }
}
