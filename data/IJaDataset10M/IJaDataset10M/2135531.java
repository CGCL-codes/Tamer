package source;

import java.sql.*;
import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.faces.model.SelectItem;

public class RegisterBean {

    /** Creates a new instance of RegisterBean */
    public RegisterBean() {
    }

    @Resource(name = "jdbc/team2DB")
    private DataSource ds;

    Connection conn = null;

    private boolean takenUser;

    public boolean getTakenUser() {
        return takenUser;
    }

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String newVal) {
        user = newVal;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String newVal) {
        password = newVal;
    }

    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newVal) {
        firstName = newVal;
    }

    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newVal) {
        lastName = newVal;
    }

    private String address1;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String newVal) {
        address1 = newVal;
    }

    private String address2 = "";

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String newVal) {
        address2 = newVal;
    }

    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String newVal) {
        city = newVal;
    }

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String newVal) {
        state = newVal;
    }

    private String zip;

    public String getZip() {
        return zip;
    }

    public void setZip(String newVal) {
        zip = newVal;
    }

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String newVal) {
        phone = newVal;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String newVal) {
        email = newVal;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String newVal) {
        type = newVal;
    }

    public SelectItem[] getStateItems() {
        return stateItems;
    }

    public SelectItem[] getTypeItems() {
        return typeItems;
    }

    public String submit() {
        try {
            if (isUserTaken()) {
                takenUser = true;
                return "";
            } else {
                takenUser = false;
                doRegister();
                return "login";
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return "";
        }
    }

    private boolean isUserTaken() throws SQLException {
        if (ds == null) {
            throw new SQLException("No Data Source");
        }
        conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("No Connection Found");
        }
        try {
            String sql = "SELECT username FROM users WHERE username = '" + user + "'";
            Statement findUser = conn.createStatement();
            ResultSet rs = findUser.executeQuery(sql);
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return true;
        }
    }

    private void doRegister() throws SQLException {
        if (ds == null) {
            throw new SQLException("No Data Source");
        }
        conn = ds.getConnection();
        if (conn == null) {
            throw new SQLException("No Connection Found");
        }
        try {
            String newUser = "INSERT INTO users(username, password, privilege)" + " VALUES('" + user + "', '" + password + "', 'Reviewer')";
            Statement addUser = conn.createStatement();
            addUser.executeUpdate(newUser);
            String updateInfo = "INSERT INTO contactinformation(username, fullname, addressfirstline, addresssecondline, city, state, zip, phonenumber," + " requestedstatus, status) VALUES('" + user + "', '" + firstName + " " + lastName + "', '" + address1 + "', '" + address2 + "', '" + city + "', '" + state + "', '" + zip + "', '" + phone + "', '" + type + "', 'Reviewer')";
            Statement addInfo = conn.createStatement();
            addInfo.executeUpdate(updateInfo);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        } finally {
            conn.close();
        }
    }

    private static SelectItem[] stateItems = new SelectItem[] { new SelectItem("Alabama"), new SelectItem("Alaska"), new SelectItem("Arizona"), new SelectItem("Arkansas"), new SelectItem("California"), new SelectItem("Colorado"), new SelectItem("Connecticut"), new SelectItem("Delaware"), new SelectItem("Florida"), new SelectItem("Georgia"), new SelectItem("Hawaii"), new SelectItem("Idaho"), new SelectItem("Illinois"), new SelectItem("Indiana"), new SelectItem("Iowa"), new SelectItem("Kansas"), new SelectItem("Kentucky"), new SelectItem("Louisiana"), new SelectItem("Maine"), new SelectItem("Maryland"), new SelectItem("Massachusetts"), new SelectItem("Michigan"), new SelectItem("Minnesota"), new SelectItem("Mississippi"), new SelectItem("Missouri"), new SelectItem("Montana"), new SelectItem("Nebraska"), new SelectItem("Nevada"), new SelectItem("New Hampshire"), new SelectItem("New Jersey"), new SelectItem("New Mexico"), new SelectItem("New York"), new SelectItem("North Carolina"), new SelectItem("North Dakota"), new SelectItem("Ohio"), new SelectItem("Oklahoma"), new SelectItem("Oregon"), new SelectItem("Pennsylvania"), new SelectItem("Rhode Island"), new SelectItem("Sourth Carolina"), new SelectItem("South Dakota"), new SelectItem("Tennessee"), new SelectItem("Texas"), new SelectItem("Utah"), new SelectItem("Vermont"), new SelectItem("Virginia"), new SelectItem("Washington"), new SelectItem("West Virginia"), new SelectItem("Wisconsin"), new SelectItem("Wyoming") };

    private static SelectItem[] typeItems = new SelectItem[] { new SelectItem("Reviewer"), new SelectItem("Publisher"), new SelectItem("Inventory Manager"), new SelectItem("Editor") };
}
