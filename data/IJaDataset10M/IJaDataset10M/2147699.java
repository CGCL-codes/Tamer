package unibg.overencrypt.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Class that manage the server side Update process
 * @author Marco Guarnieri
 */
public class UpdateManager {

    private ACLList notelist;

    /** ACLList contains all the ACL involved into the update process */
    private ACLList current;

    /** ACLList contains the ACL on which the process is working */
    private String user;

    /** ID of the user that is doing the Update process */
    private String currentDestination;

    /** Destination ACL */
    private boolean belType;

    /** Level of the resource. True if BEL, false if SEL*/
    private LinkedList<String> users;

    /** All the users in the ACL */
    private LinkedList<String> noteToDelete;

    /** NoteID that will be deleted from NOTETABLE when the Update process is finish*/
    private DBConnection connection;

    /** The connection to the DB */
    private boolean commited = false;

    /** boolean that indicates if the transaction is already commited or not */
    private Connection conn;

    /**
	 * Class constructor
	 * @param user - ID of the user that is doing the Update process
	 * @param level - Level of the resource. True if BEL, false if SEL
	 * @param ACL - destination ACL
	 * @throws Exception
	 */
    public UpdateManager(String user, boolean level, String ACL) throws Exception {
        this();
        notelist = new ACLList();
        current = new ACLList();
        this.user = user;
        currentDestination = "";
        this.belType = level;
        users = new LinkedList<String>();
        noteToDelete = new LinkedList<String>();
        if (!ACL.isEmpty()) {
            String v[] = ACL.split("-");
            for (int i = 0; i < v.length; i++) {
                if (v[i].compareTo(user) != 0) users.add(v[i]);
            }
        } else {
            Statement stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rset;
            rset = stmt.executeQuery("SELECT subjectid from usertable");
            while (rset.next()) {
                String id = rset.getString("subjectid");
                users.add(id);
            }
        }
    }

    /**
	 * Class constructor
	 * @param user - ID of the user that is doing the Update process
	 * @param level - Level of the resource. True if BEL, false if SEL
	 * @throws Exception
	 */
    public UpdateManager(String user, boolean level) throws Exception {
        this(user, level, "");
    }

    /**
	 * Set the properties of the UpdateManager
	 * @param user - ID of the user that is doing the Update process
	 * @param level - Level of the resource. True if BEL, false if SEL
	 * @param ACL - destination ACL
	 * @throws Exception
	 */
    public void setUpdateManager(String user, boolean level, String ACL) throws Exception {
        connection = new DBConnection();
        commited = false;
        conn = connection.getConnection();
        conn.setAutoCommit(true);
        notelist = new ACLList();
        current = new ACLList();
        this.user = user;
        currentDestination = "";
        this.belType = level;
        users = new LinkedList<String>();
        noteToDelete = new LinkedList<String>();
        if (!ACL.isEmpty()) {
            String v[] = ACL.split("-");
            for (int i = 0; i < v.length; i++) {
                if (v[i].compareTo(user) != 0) users.add(v[i]);
            }
        } else {
            Statement stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rset;
            rset = stmt.executeQuery("SELECT subjectid from usertable");
            while (rset.next()) {
                String id = rset.getString("subjectid");
                users.add(id);
            }
        }
    }

    /**
	 * Set the properties of the UpdateManager
	 * @param user - ID of the user that is doing the Update process
	 * @param level - Level of the resource. True if BEL, false if SEL
	 * @throws Exception
	 */
    public void setUpdateManager(String user, boolean level) throws Exception {
        this.setUpdateManager(user, level, "");
    }

    /**
	 * Class constructor
	 */
    public UpdateManager() throws SQLException {
        notelist = new ACLList();
        current = new ACLList();
        noteToDelete = new LinkedList<String>();
    }

    /**
	 * Return the id of the token into the IDS table
	 * @param token - the token
	 * @return String contianing the ID
	 * @throws Exception 
	 */
    private String get_token_id(String token) throws Exception {
        Statement stmt = Configuration.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rset = stmt.executeQuery("SELECT num FROM IDS WHERE acl='" + token + "'");
        String id = "";
        if (rset.next()) id = rset.getString(1);
        stmt.close();
        return id;
    }

    /**
	 * Check if the passed token is equals to the destination ACL
	 * @param next_token - next_token - string containing the token with the ACL of the token in JSON format
	 * @return true if the destination ACL is reached, false otherwise
	 */
    public boolean finished(String next_token) throws Exception {
        String dest_id = get_token_id(currentDestination);
        BufferedReader input = new BufferedReader(new StringReader(next_token));
        JSONTokener j = new JSONTokener(input);
        j.nextString('\"');
        j.nextString('\"');
        j.nextString('\"');
        String token = j.nextString('\"');
        boolean result = false;
        if ((token == null) || (token.compareTo("") == 0)) result = true; else {
            String query = "SELECT * FROM IDS WHERE acl = '" + token + "'";
            Statement stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rset = stmt.executeQuery(query);
            if (rset.next()) {
                String id = rset.getString("num");
                if (id.compareTo(dest_id) == 0) result = true; else result = false;
            } else throw new Exception("Token don't exists");
        }
        return result;
    }

    /**
	 * Get all the notes for the user from NOTETABLE
	 * @throws SQLException
	 */
    public void getAllNotes() throws SQLException {
        PreparedStatement prepStmt = connection.getConnection().prepareStatement("SELECT * from notetable where userID= ? and ownerID ? ORDER BY NOTEID ASC", ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        prepStmt.setString(1, user);
        prepStmt.setString(2, usersToSQL());
        ResultSet rset;
        rset = prepStmt.executeQuery();
        while (rset.next()) {
            String token = rset.getString("acl");
            String id = rset.getString("noteid");
            noteToDelete.add(id);
            notelist.add(token);
        }
    }

    /**
	 * Translate the list of users into a IN(user,user,..,user) statement to use in SQL query
	 * @return String containing the IN statement
	 */
    private String usersToSQL() {
        String s = "";
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) s = s + "in ("; else if (!s.endsWith(",")) s = s + ",";
            s = s + "'" + users.get(i) + "'";
            if (i == users.size() - 1) s = s + ")";
        }
        return s;
    }

    /**
	 * Create a string in JSON format containing the notes for the Update process 
	 * @return String containing the notes
	 * @throws Exception
	 */
    public String createJsonNote() throws Exception {
        String json = "";
        if (!notelist.isEmpty()) {
            String token = notelist.remove();
            currentDestination = token;
            Statement stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rset;
            rset = stmt.executeQuery("SELECT * from notetable where acl='" + token + "' and userID='" + user + "' and ownerID " + usersToSQL() + "  ORDER BY NOTEID ASC");
            json = "{\"values\":[";
            while (rset.next()) {
                String owner = rset.getString("ownerID");
                String info = rset.getString("note");
                stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
                rset = stmt.executeQuery("SELECT publickey from dhkatable where subjectid='" + owner + "'");
                rset.next();
                String publicKey = rset.getString(1);
                stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
                rset = stmt.executeQuery("SELECT privatekey from dhkatable where subjectid='" + user + "'");
                rset.next();
                String privateKey = rset.getString(1);
                json = json + "{\"grpsrc\":\"" + token + "\",\"info\":\"" + info + "\",\"private\":\"" + privateKey + "\",\"public\":\"" + publicKey + "\"}";
            }
            json = json + "]}";
            StartUpdate(token);
        }
        return json;
    }

    /**
	 * Start the Update process for the passed ACL. The Update process is run for the owner
	 * and the notes of all the user included into the passe ACLnote
	 * @param ACLnote - the String containing the ACL 
	 */
    private void StartUpdate(String ACLnote) throws Exception {
        Set<String> ACL = new HashSet<String>();
        String[] v = ACLnote.split("-");
        for (int i = 0; i < v.length; i++) ACL.add(v[i]);
        ACL.remove(user);
        current = new ACLList();
        Statement stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rset;
        rset = stmt.executeQuery("SELECT subjectid from usertable");
        while (rset.next()) {
            String id = rset.getString("subjectid");
            String token = "";
            if (ACL.contains(id)) {
                if (Integer.parseInt(id) > Integer.parseInt(user)) token = user + "-" + id; else token = id + "-" + user;
                current.add(token);
            }
        }
    }

    /**
	 * Get the string containing the requested tokens. 
	 * If firstStep == true the requested token is user-first of the user into current  ACLList
	 * If firstStep == false the requested token is passed into the next_token 
	 * @param next_token - the token to extract from DB
	 * @param firstStep - if true the requested token is user--first of the user into current ACLList,
	 * if false the requested token is into next_token
	 * @return String containing the requested token informations in JSON format
	 * @throws Exception
	 */
    public String CreateJsonUpdate(String next_token, boolean firstStep) throws Exception {
        String owner = "";
        if (firstStep) {
            if (!current.isEmpty()) {
                String token = current.remove();
                String[] vs = token.split("-");
                if (vs[0].compareTo(user) == 0) owner = vs[1]; else if (vs[1].compareTo(user) == 0) owner = vs[0];
                return createJson(next_token, firstStep, owner);
            }
            return "";
        } else return createJson(next_token, firstStep, user);
    }

    /**
	 * Check if the current ACLList is empty. 
	 * @return true if is empty, false otherwise
	 */
    public boolean currentIsEmpty() {
        return current.isEmpty();
    }

    /**
	 * Get the string containing the requested tokens. 
	 * If firstStep == true the requested token is user-owner and the string contains also the public key of the owner
	 * and the private key of the user.
	 * If firstStep == false the requested token is passed into the next_token 
	 * @param next_token - the token to extract from DB
	 * @param firstStep - if true the requested token is user-owner and the string contains also the public key of the owner
	 * and the private key of the user, if false the requested token is into next_token
	 * @param owner - the owner of the resource
	 * @return String containing the requested token informations in JSON format
	 * @throws Exception
	 */
    private String createJson(String next_token, boolean firstStep, String owner) throws Exception {
        Statement stmt;
        ResultSet rset;
        String token;
        String json;
        String query = "SELECT * FROM DHKATABLE WHERE subjectid = '" + user + "'";
        stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        rset = stmt.executeQuery(query);
        rset.next();
        String privateKey = rset.getString("privatekey");
        query = "SELECT * FROM DHKATABLE WHERE subjectid = '" + owner + "'";
        stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
        rset = stmt.executeQuery(query);
        rset.next();
        String publicKey = rset.getString("publickey");
        if (firstStep) {
            if (Integer.parseInt(owner) > Integer.parseInt(user)) token = user + "-" + owner; else token = owner + "-" + user;
            query = "SELECT * FROM TOKENTABLEENCRYPT WHERE groupidsource = '" + token + "' and LEVEL='BEL'";
            stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            rset = stmt.executeQuery(query);
            json = "{\"values\":[";
            boolean first = true;
            while (rset.next()) {
                String info = rset.getString("info");
                if (!first) json = json + ","; else first = false;
                String tokenapp = token + ";" + rset.getString("tokenid");
                json = json + "{\"grpsrc\":\"" + tokenapp + "\",\"info\" : \"" + info + "\",\"private\":\"" + privateKey + "\",\"public\":\"" + publicKey + "\"}";
            }
            json = json + "]}";
        } else {
            BufferedReader input = new BufferedReader(new StringReader(next_token));
            JSONTokener j = new JSONTokener(input);
            j.nextString('\"');
            j.nextString('\"');
            j.nextString('\"');
            token = j.nextString('\"');
            query = "SELECT * FROM TOKENTABLEENCRYPT WHERE groupidsource = '" + token + "' and LEVEL='BEL'";
            stmt = connection.getConnection().createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.CONCUR_UPDATABLE);
            rset = stmt.executeQuery(query);
            json = "{\"values\":[";
            boolean first = true;
            while (rset.next()) {
                String info = rset.getString("info");
                if (!first) json = json + ","; else first = false;
                String tokenapp = token + ";" + rset.getString("tokenid");
                json = json + "{\"grpsrc\":\"" + tokenapp + "\",\"info\" : \"" + info + "\",\"private\":\"" + privateKey + "\",\"public\":\"" + publicKey + "\"}";
            }
            json = json + "]}";
        }
        return json;
    }

    /**
	 * Put the new or updated tokens into the TOKENTABLEENCRYPT
	 * @param input - the String containing the new or updated tokens in JSON format 
	 * @return false in case of error, true otherwise 
	 * @throws Exception
	 */
    public boolean putTokensInDBWPES(String input) throws Exception {
        Statement stmt = connection.getConnection().createStatement();
        JSONObject tokens = new JSONObject(input);
        JSONArray array = tokens.getJSONArray("values");
        JSONObject temp;
        try {
            for (int i = 0; i < array.length(); i++) {
                temp = array.getJSONObject(i);
                String grpsrc = temp.getString("grpsrc");
                if (grpsrc.contains(";M")) {
                    grpsrc = grpsrc.replace(";M", "");
                    if (grpsrc.contains(";")) {
                        String v[] = grpsrc.split(";");
                        grpsrc = v[0];
                        int tokenid = Integer.parseInt(v[1]);
                        stmt.execute("UPDATE tokentableencrypt SET groupidsource = '" + grpsrc + "',info='" + temp.getString("info") + "' WHERE tokenid ='" + tokenid + "'");
                    } else {
                        stmt.execute("Insert into tokentableencrypt (groupidsource,info,level)" + "values ( '" + grpsrc + "','" + temp.getString("info") + "','" + getLevel() + "')");
                    }
                } else {
                    if (grpsrc.contains(";")) {
                        String v[] = grpsrc.split(";");
                        grpsrc = v[0];
                    }
                    stmt.execute("Insert into tokentableencrypt (groupidsource,info,level)" + "values ( '" + grpsrc + "','" + temp.getString("info") + "','" + getLevel() + "')");
                }
            }
            stmt.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * Get the token id of current Destination
	 * @return String contianing the ID
	 * @throws Exception
	 */
    public String get_current_id() throws Exception {
        return get_token_id(currentDestination);
    }

    /**
	 * Get the current destination ACL
	 * @return String containing current destination
	 */
    public String get_current_destination() {
        return this.currentDestination;
    }

    /**
	 * Check if the notelist is empty
	 * @return true if notelist is empty, false otherwise
	 */
    public boolean isEmpty() {
        return notelist.isEmpty();
    }

    /**
	 * Get the level of the resource. 
	 * @return If belType==true return "BEL", "SEL" otherwise
	 */
    private String getLevel() {
        if (belType) return "BEL"; else return "SEL";
    }

    /**
	 * Delete the updated notes from NOTETABLE 
	 */
    public void deleteNotes() {
        try {
            for (Iterator<String> it = noteToDelete.iterator(); it.hasNext(); ) {
                Statement stmt = connection.getConnection().createStatement();
                String id = (String) it.next();
                stmt.execute("DELETE FROM notetable WHERE noteid='" + id + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rollBack() throws SQLException {
        if (!commited) {
            System.out.println("UPDATE ROLLBACK");
            this.conn.rollback();
        } else {
            System.out.println("NO ROLLBACK");
        }
        conn.setAutoCommit(true);
        conn.close();
    }

    public void commit() throws SQLException {
        try {
            System.out.println("UPDATE COMMIT");
            commited = true;
            conn.commit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
