package org.gyx.company;

import org.gyx.common.html.interfaces.IHTMLCombo;
import org.gyx.common.sql.SqlAccess;
import org.gyx.common.utils.StringTools;
import org.gyx.elips.internationalization.LangMsg;
import org.gyx.elips.repository.ElipsRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class User implements IHTMLCombo {

    private String trigram = "";

    private String name = "";

    private String firstName = "";

    private String email = "";

    private String login = "";

    private String password = "";

    private String department = "";

    private int team = -1;

    private SqlAccess theSqlAccess;

    public static User get(String strLogin, SqlAccess aSqlAccess) throws Exception {
        User aUser = new User(aSqlAccess);
        aUser.setLogin(strLogin);
        aUser.select();
        return aUser;
    }

    public static User getTrig(String strTrigram, SqlAccess aSqlAccess) throws Exception {
        User aUser = new User(aSqlAccess);
        aUser.setTrigram(strTrigram);
        aUser.select();
        return aUser;
    }

    public User(SqlAccess aSqlAccess) {
        theSqlAccess = aSqlAccess;
    }

    public User() {
    }

    public User(String aTrigram) {
        trigram = aTrigram;
    }

    public void select() throws Exception {
        String aRequest = "";
        if (!login.equals("")) aRequest = "SELECT * FROM " + ElipsRepository.db.getCompanyDatabase() + ".users WHERE UCASE(login)='" + login.toUpperCase().trim() + "'";
        if (!trigram.equals("")) aRequest = "SELECT * FROM " + ElipsRepository.db.getCompanyDatabase() + ".users WHERE UCASE(trigram)='" + trigram.toUpperCase().trim() + "'";
        if (aRequest.equals("")) throw (new Exception(LangMsg.getString("User.NoLoginNoTrigram")));
        ResultSet theResultSet = theSqlAccess.executeQuery(aRequest);
        if (theResultSet.next()) {
            fillFromResultSet(this, theResultSet);
        }
        theResultSet.close();
    }

    public static Vector selectList(String strSql, SqlAccess aSqlAccess) throws Exception {
        User unUser;
        Vector vListeUsers = new Vector();
        try {
            ResultSet theResultSet = aSqlAccess.executeQuery(strSql);
            while (theResultSet.next()) {
                unUser = new User(aSqlAccess);
                fillFromResultSet(unUser, theResultSet);
                vListeUsers.add(unUser);
            }
            theResultSet.close();
        } catch (SQLException ex) {
            throw (new Exception(LangMsg.getString("User.ErrorSelectUser") + ex + "\n\n" + strSql));
        }
        return vListeUsers;
    }

    public static String GetCompanyAdministrators_SQL() {
        String strSql = " SELECT u.firstName, u.trigram FROM " + ElipsRepository.db.getCompanyDatabase() + ".company_admin c, " + ElipsRepository.db.getCompanyDatabase() + ".users u";
        strSql += " WHERE u.trigram = c.trigram ";
        strSql += " ORDER BY u.firstName";
        return strSql;
    }

    public void update() throws Exception {
        String strSql = "";
        try {
            String aDbType = theSqlAccess.getDBType();
            strSql = "UPDATE " + ElipsRepository.db.getCompanyDatabase() + ".users SET";
            strSql += " name=" + StringTools.SqlString(name, aDbType) + ",";
            strSql += " firstName=" + StringTools.SqlString(firstName, aDbType) + ",";
            strSql += " department=" + StringTools.SqlString(department, aDbType) + ",";
            strSql += " email=" + StringTools.SqlString(email, aDbType) + ",";
            strSql += " teamId=" + team + ",";
            strSql += " login=" + StringTools.SqlString(login, aDbType) + ",";
            strSql += " password=" + StringTools.SqlString(password, aDbType);
            strSql += " WHERE trigram=" + StringTools.SqlString(trigram, aDbType);
            theSqlAccess.executeUpdate(strSql);
        } catch (SQLException ex) {
            throw (new Exception(LangMsg.getString("User.ChangeImpossible") + ex + "\n\n" + strSql));
        }
    }

    public void insert() throws Exception {
        String strSql = "";
        try {
            String aDbType = theSqlAccess.getDBType();
            strSql = "INSERT INTO " + ElipsRepository.db.getCompanyDatabase() + ".users (trigram, name, firstName, department, email, teamId, login, password) VALUES (";
            strSql += StringTools.SqlString(trigram, aDbType) + ",";
            strSql += StringTools.SqlString(name, aDbType) + ",";
            strSql += StringTools.SqlString(firstName, aDbType) + ",";
            strSql += StringTools.SqlString(department, aDbType) + ",";
            strSql += StringTools.SqlString(email, aDbType) + ",";
            strSql += team + ",";
            strSql += StringTools.SqlString(login, aDbType) + ",";
            strSql += StringTools.SqlString(password, aDbType) + ")";
            theSqlAccess.executeUpdate(strSql);
        } catch (SQLException ex) {
            throw (new Exception("User.java : insertion impossible:" + ex + "\n\n" + strSql));
        }
    }

    public void delete() throws Exception {
        String strSql = "";
        try {
            String aDbType = theSqlAccess.getDBType();
            strSql = "DELETE FROM " + ElipsRepository.db.getCompanyDatabase() + ".users";
            strSql += " WHERE trigram=" + StringTools.SqlString(trigram, aDbType);
            theSqlAccess.executeUpdate(strSql);
        } catch (SQLException ex) {
            throw (new Exception(LangMsg.getString("User.ErrorWhileDeleting") + ex + "\n\n" + strSql));
        }
    }

    private static void fillFromResultSet(User unUser, ResultSet unResultSet) throws Exception {
        unUser.setTrigram(unResultSet.getString("trigram"));
        unUser.setLogin(unResultSet.getString("login"));
        unUser.setPassword(unResultSet.getString("password"));
        unUser.setName(unResultSet.getString("name"));
        unUser.setFirstName(unResultSet.getString("firstName"));
        unUser.setDepartment(StringTools.nullToEmpty(unResultSet.getString("department")));
        unUser.setTeam(unResultSet.getInt("teamId"));
        unUser.setEmail(unResultSet.getString("email"));
    }

    public boolean checkPassword(String unPassword) {
        return unPassword.toUpperCase().trim().equals(password.toUpperCase().trim());
    }

    /**
	 * This method indicates if the current user can receive or not emails
	 * @return boolean
	 */
    public boolean emailAuthorized() throws Exception {
        boolean aResult = false;
        String aRequest = "SELECT * FROM user_domain WHERE UCASE(trigram)='" + trigram.toUpperCase().trim() + "' AND notification='Y'";
        ResultSet theResultSet = theSqlAccess.executeQuery(aRequest);
        aResult = theResultSet.next();
        theResultSet.close();
        return aResult;
    }

    /**
	 * This method indicates if the  user passed in argument can receive or not emails
	 * @return boolean
	 */
    public boolean emailAuthorized(String aTrigram) throws Exception {
        boolean aResult = false;
        String aRequest = "SELECT * FROM user_domain WHERE trigram='" + aTrigram.toLowerCase().trim() + "' AND notification='Y'";
        ResultSet theResultSet = theSqlAccess.executeQuery(aRequest);
        aResult = theResultSet.next();
        theResultSet.close();
        return aResult;
    }

    public String toString() {
        return toString("\n");
    }

    public String toString(String separateur) {
        StringBuffer strOut = new StringBuffer();
        strOut.append(LangMsg.getString("User.USER"));
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.Trigram") + trigram);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.Name") + name);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.FirtsName") + firstName);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.Team") + team);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.Email") + email);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.Login") + login);
        strOut.append(separateur);
        strOut.append(LangMsg.getString("User.USER"));
        strOut.append(separateur);
        return strOut.toString();
    }

    public void setPassword(String aPassword) {
        password = aPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setTrigram(String aTrigram) {
        trigram = aTrigram;
    }

    public String getTrigram() {
        return trigram;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }

    public void setFirstName(String aFirstName) {
        firstName = aFirstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setDepartment(String aDepartment) {
        department = aDepartment;
    }

    public String getDepartment() {
        return department;
    }

    public void setTeam(int aTeam) {
        team = aTeam;
    }

    public int getTeam() {
        return team;
    }

    public void setEmail(String aEmail) {
        email = aEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setLogin(String aLogin) {
        login = aLogin;
    }

    public String getLogin() {
        return login;
    }

    public String getComboId() {
        return trigram;
    }

    public String getComboCaption() {
        String str = firstName + " " + name;
        return str;
    }

    public int getId() {
        return 0;
    }
}
