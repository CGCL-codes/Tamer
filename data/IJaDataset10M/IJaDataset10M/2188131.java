package TBA.Communications;

import TBA.Data.Diary;
import TBA.Data.Entry;
import TBA.Exceptions.DBServerException;
import TBA.Exceptions.PasswordHashingException;
import java.sql.*;
import TBA.Security.*;
import TBA.Data.User;
import java.util.Calendar;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This abstract class handles the connection to SQLite
 *<p>
 * If you wish to talk to SQLite, use this class.
 * It is only thread-safe if you instantiation it as a static class
 * <p>
 * @author Dan McGrath
 *
 * @version $Rev:: 217           $ $Date:: 2009-10-18 #$
 *
 * @see dbserver.SQLite
 */
class DBServer extends SQLite {

    private PreparedStatement prepCheckUser;

    private PreparedStatement prepSaltPassword;

    private PreparedStatement prepSaveSession;

    private PreparedStatement prepNewSession;

    private PreparedStatement prepGetSession;

    private PreparedStatement prepCheckSession;

    private PreparedStatement prepRemoveSession;

    private PreparedStatement prepGetUserDiaries;

    private PreparedStatement prepGetDiary;

    private PreparedStatement prepGetEntries;

    private PreparedStatement prepGetNewEntries;

    private PreparedStatement prepGetModifiedEntries;

    private PreparedStatement prepUpdateEntry;

    private PreparedStatement prepNewEntry;

    private PreparedStatement prepNewLocalEntry;

    private int nextEntryID;

    private int nextNewEntryID;

    private int nextDiaryID;

    private int nextDiariesID;

    private static final Object lock = new Object();

    private PreparedStatement prepCheckUserExist;

    private PreparedStatement prepAddUsr;

    private PreparedStatement prepAddSalt;

    private PreparedStatement prepAddDiary;

    private PreparedStatement prepAddDiaries;

    private PreparedStatement getUsrDiaryID;

    private PreparedStatement rmvAllEntries;

    private PreparedStatement rmvAllDiaries;

    private PreparedStatement rmvAlldiary;

    private PreparedStatement rmvSalt;

    private PreparedStatement rmvUser;

    private PreparedStatement prepRemovePermissions;

    private PreparedStatement chkPremissions;

    private PreparedStatement prepRemoveDiary;

    private PreparedStatement chkDairyID;

    private PreparedStatement prepRemoveEntries;

    private PreparedStatement prepRemoveNewEntries;

    private PreparedStatement rmvDiaries;

    private TBA.Security.PasswordHashing secure;

    private DBServer() {
    }

    /**
     * This is the constructor for DBServer.
     *<p>
     * @throws DBServerException
     *<p>
     * @param user The username to log into the database as
     * @param pass The password to log into the database with
     */
    DBServer(String user, String pass) throws DBServerException {
        super(user, pass);
    }

    /**
     * This sets up all the prepared statements that will be possibly used by
     * DBServer
     *<p>
     * @throws DBServerException
     *<p>
     * @see java.sql.PreparedStatement
     */
    public void setup() throws DBServerException {
        LOGIT.fine("Setting up prepared statements");
        try {
            String sql = null;
            tmpDummyDatabase();
            sql = "select DISPLAYNAME, DEFAULTDIARYID from users where USER = ? and PWDHASH = ?;";
            prepCheckUser = connex.prepareStatement(sql);
            sql = "select SALT from passwordSalts where USER = ?;";
            prepSaltPassword = connex.prepareStatement(sql);
            sql = "select SESSIONID from userSessions where USER = ?;";
            prepGetSession = connex.prepareStatement(sql);
            sql = "select USER from userSessions where SESSIONID = ?;";
            prepCheckSession = connex.prepareStatement(sql);
            sql = "insert INTO userSessions(SESSIONID, USER) values (?, ?);";
            prepNewSession = connex.prepareStatement(sql);
            sql = "update userSessions SET SESSIONID=? WHERE USER=?;";
            prepSaveSession = connex.prepareStatement(sql);
            sql = "update userSessions SET SESSIONID='' WHERE SESSIONID=?;";
            prepRemoveSession = connex.prepareStatement(sql);
            sql = "select DIARYID, PERMISSIONS from diaries where USER = ?;";
            prepGetUserDiaries = connex.prepareStatement(sql);
            sql = "select NAME, OWNER, REVISION from diary where ID = ?;";
            prepGetDiary = connex.prepareStatement(sql);
            sql = "select ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, LOCALMOD from entries where DIARYID = ?;";
            prepGetEntries = connex.prepareStatement(sql);
            LOGIT.finest("Setup get entries PreparedStatement");
            sql = "select ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY from newentries where DIARYID = ?;";
            prepGetNewEntries = connex.prepareStatement(sql);
            sql = "select ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY from entries where DIARYID = ? and LOCALMOD=1;";
            prepGetModifiedEntries = connex.prepareStatement(sql);
            sql = "update entries SET START=?, END=?, USER=?, LOCKED=?, TITLE=?, BODY=?, DELETED=? WHERE ID=?;";
            prepUpdateEntry = connex.prepareStatement(sql);
            sql = "insert INTO entries(START, END, USER, LOCKED, TITLE, BODY, DELETED, ID, DIARYID, LOCALMOD) values(?,?,?,?,?,?,?,?,?,?);";
            prepNewEntry = connex.prepareStatement(sql);
            sql = "insert INTO newentries(START, END, USER, LOCKED, TITLE, BODY, DELETED, ID, DIARYID) values(?,?,?,?,?,?,?,?,?);";
            prepNewLocalEntry = connex.prepareStatement(sql);
            sql = "SELECT COUNT(*) FROM USERS WHERE USER = ? ;";
            prepCheckUserExist = connex.prepareStatement(sql);
            sql = "INSERT INTO USERS(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH) VALUES (?,?,?,?);";
            prepAddUsr = connex.prepareStatement(sql);
            sql = "INSERT INTO PASSWORDSALTS(USER, SALT) VALUES (?,?);";
            prepAddSalt = connex.prepareStatement(sql);
            sql = "INSERT INTO DIARY(ID, NAME, OWNER, REVISION) VALUES (?,?,?,?);";
            prepAddDiary = connex.prepareStatement(sql);
            sql = "INSERT INTO DIARIES(ID, USER, DIARYID, PERMISSIONS) VALUES (?,?,?,?);";
            prepAddDiaries = connex.prepareStatement(sql);
            sql = " SELECT ID FROM DIARY WHERE NAME = ? AND OWNER = ?;";
            getUsrDiaryID = connex.prepareStatement(sql);
            sql = " DELETE FROM ENTRIES WHERE USER=?;";
            rmvAllEntries = connex.prepareStatement(sql);
            sql = " DELETE FROM DIARIES WHERE ID = ?;";
            rmvAllDiaries = connex.prepareStatement(sql);
            sql = " DELETE FROM DIARY WHERE OWNER = ?;";
            rmvAlldiary = connex.prepareStatement(sql);
            sql = " DELETE FROM PASSWORDSALTS WHERE USER = ?;";
            rmvSalt = connex.prepareStatement(sql);
            sql = " DELETE FROM USERS WHERE USER = ?;";
            rmvUser = connex.prepareStatement(sql);
            sql = "DELETE FROM DIARIES WHERE DIARYID = ? AND USER = ?";
            prepRemovePermissions = connex.prepareStatement(sql);
            sql = " SELECT DIARY.ID FROM DIARY JOIN DIARIES ON DIARY.ID = DIARIES.DIARYID WHERE DIARIES.USER = ? AND DIARY.NAME = ?; ";
            chkPremissions = connex.prepareStatement(sql);
            sql = " SELECT ID FROM DIARY WHERE NAME = ? AND OWNER = ?;";
            chkDairyID = connex.prepareStatement(sql);
            sql = "DELETE FROM DIARY WHERE ID = ?";
            prepRemoveDiary = connex.prepareStatement(sql);
            sql = "DELETE FROM ENTRIES WHERE DIARYID = ?;";
            prepRemoveEntries = connex.prepareStatement(sql);
            sql = "DELETE FROM NEWENTRIES WHERE DIARYID = ?;";
            prepRemoveNewEntries = connex.prepareStatement(sql);
            sql = "DELETE FROM DIARIES WHERE DIARYID = ?;";
            rmvDiaries = connex.prepareStatement(sql);
            secure = new TBA.Security.PasswordHashing();
            ResultSet res = executeQuery("select Max(ID) from entries");
            if (res.next()) {
                nextEntryID = res.getInt(1) + 1;
            } else {
                nextEntryID = 1;
            }
            res = executeQuery("select Max(ID) from newentries");
            if (res.next()) {
                nextNewEntryID = res.getInt(1) + 1;
            } else {
                nextNewEntryID = 1;
            }
            res = executeQuery("select Max(ID) from diary");
            if (res.next()) {
                nextDiaryID = res.getInt(1) + 1;
            } else {
                nextDiaryID = 1;
            }
            res = executeQuery("select Max(ID) from diaries");
            if (res.next()) {
                nextDiariesID = res.getInt(1) + 1;
            } else {
                nextDiariesID = 1;
            }
        } catch (Exception ex) {
            LOGIT.severe("Unable to create an SQL prepared statement");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex);
        }
    }

    /**
     * This checks a user's credentials and returns a User object which contains
     * the details of the said user.
     *<p>
     * @param username The user name
     * @param pwdhash The password hash sent from the client
     *<p>
     * @throws DBServerException
     *<p>
     * @return User object if the credentials match, null if they don't
     */
    public User CheckUser(String username, String pwdhash) throws DBServerException {
        User userData = new User();
        try {
            ResultSet res;
            String salt;
            prepSaltPassword.setString(1, username);
            res = executeQuery(prepSaltPassword);
            if (res.next()) {
                salt = res.getString("SALT");
            } else {
                return null;
            }
            PasswordHashing pHash = new PasswordHashing();
            pwdhash = pHash.getServerHash(pwdhash, salt);
            prepCheckUser.setString(1, username);
            prepCheckUser.setString(2, pwdhash);
            res = executeQuery(prepCheckUser);
            if (res.next()) {
                userData.setDisplayName(res.getString("DISPLAYNAME"));
                userData.setDefaultDiaryID(res.getInt("DEFAULTDIARYID"));
            } else {
                return null;
            }
            PreparedStatement giveSession;
            prepGetSession.setString(1, username);
            res = executeQuery(prepGetSession);
            if (res.next()) {
                giveSession = prepSaveSession;
            } else {
                giveSession = prepNewSession;
            }
            String sessionID = UUID.randomUUID().toString();
            giveSession.setString(1, sessionID);
            giveSession.setString(2, username);
            if (executeUpdate(giveSession) == -1) {
                throw new SQLException("Give the user a session failed!");
            }
            userData.setSessionID(sessionID);
            userData.setDiaries(GetDiaries(username));
            return userData;
        } catch (SQLException ex) {
            LOGIT.severe("Unable to set values in an SQL prepared statement");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        } catch (NullPointerException ex) {
            LOGIT.severe("DB Instance not active...");
            throw new DBServerException(ex.getLocalizedMessage());
        } catch (PasswordHashingException ex) {
            LOGIT.severe("Password hashing services unavailable");
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    /**
     * This gets all the diary information for a particular user, excluding the
     * actual diary entries.
     *<p>
     * @param username The user name
     *<p>
     * @throws DBServerException
     *<p>
     * @return A Vector of Diaries, with just there headers
     */
    public Vector<Diary> GetDiaries(String username) throws DBServerException {
        Vector<Diary> diaries = new Vector<Diary>();
        Diary diaryData;
        try {
            ResultSet res;
            int diaryID = -1;
            prepGetUserDiaries.setString(1, username);
            res = executeQuery(prepGetUserDiaries);
            while (res.next()) {
                diaryID = res.getInt("DIARYID");
                diaryData = new Diary();
                diaryData.setID(diaryID);
                diaryData.setPermissions(res.getInt("PERMISSIONS"));
                diaries.add(diaryData);
            }
            if (diaryID == -1) {
                return null;
            }
            for (Diary aDiary : diaries) {
                diaryID = aDiary.getID();
                prepGetDiary.setInt(1, diaryID);
                res = executeQuery(prepGetDiary);
                if (res.next()) {
                    aDiary.setName(res.getString("NAME"));
                    aDiary.setOwnerName(res.getString("OWNER"));
                    aDiary.setRevision(res.getInt("REVISION"));
                }
            }
            return diaries;
        } catch (SQLException ex) {
            LOGIT.severe("Unable to set values in an SQL prepared statement");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        } catch (NullPointerException ex) {
            LOGIT.severe("DB Instance not active...");
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    /**
     * This gets all the entries for a particular diary, excluding the
     * deleted entries. It passes back the Diary, including it's header
     * info.
     *<p>
     * @param ID The Diary ID to retrieve the entries for.
     *<p>
     * @throws DBServerException
     *<p>
     * @return A Vector of Entries
     */
    public Diary GetDiary(int ID) throws DBServerException {
        Vector<Entry> entries = new Vector<Entry>();
        Diary diary = new Diary();
        Entry aEntry;
        try {
            prepGetDiary.setInt(1, ID);
            ResultSet res;
            res = executeQuery(prepGetDiary);
            if (res.next()) {
                diary.setID(ID);
                diary.setName(res.getString("NAME"));
                diary.setOwnerName(res.getString("OWNER"));
                diary.setRevision(res.getInt("REVISION"));
            }
            Calendar timedate = Calendar.getInstance();
            prepGetEntries.setInt(1, ID);
            res = executeQuery(prepGetEntries);
            while (res.next()) {
                aEntry = new Entry();
                aEntry.setID(res.getInt("ID"));
                timedate.setTimeInMillis(res.getLong("START"));
                aEntry.setStart((Calendar) timedate.clone());
                timedate.setTimeInMillis(res.getLong("END"));
                aEntry.setEnd((Calendar) timedate.clone());
                aEntry.setSubject(res.getString("TITLE"));
                aEntry.setBody(res.getString("BODY"));
                aEntry.setLocked(res.getBoolean("LOCKED"));
                aEntry.setOwner(res.getString("USER"));
                aEntry.setModified(res.getInt("LOCALMOD") == 1);
                entries.add(aEntry);
            }
            timedate = Calendar.getInstance();
            prepGetNewEntries.setInt(1, ID);
            res = executeQuery(prepGetNewEntries);
            while (res.next()) {
                aEntry = new Entry();
                aEntry.setID(0);
                timedate.setTimeInMillis(res.getLong("START"));
                aEntry.setStart((Calendar) timedate.clone());
                timedate.setTimeInMillis(res.getLong("END"));
                aEntry.setEnd((Calendar) timedate.clone());
                aEntry.setSubject(res.getString("TITLE"));
                aEntry.setBody(res.getString("BODY"));
                aEntry.setLocked(res.getInt("LOCKED") == 1);
                aEntry.setOwner(res.getString("USER"));
                aEntry.setModified(true);
                entries.add(aEntry);
            }
            diary.setEntries(entries);
        } catch (SQLException ex) {
            LOGIT.info(ex.getLocalizedMessage());
        }
        return diary;
    }

    /**
     * This gets any new/modified entries for a particular diary, that are stored
     * in the local cache.
     *<p>
     * @param currentDiary The diary to update.
     *<p>
     * @throws DBServerException
     *<p>
     * @return true if entries were added to the diary from the local cache.
     */
    public boolean getLocalEntries(Diary currentDiary) throws DBServerException {
        Entry aEntry;
        boolean modified = false;
        try {
            Calendar timedate = Calendar.getInstance();
            prepGetModifiedEntries.setInt(1, currentDiary.getID());
            ResultSet res = executeQuery(prepGetModifiedEntries);
            while (res.next()) {
                modified = true;
                aEntry = new Entry();
                aEntry.setID(res.getInt("ID"));
                timedate.setTimeInMillis(res.getLong("START"));
                aEntry.setStart((Calendar) timedate.clone());
                timedate.setTimeInMillis(res.getLong("END"));
                aEntry.setEnd((Calendar) timedate.clone());
                aEntry.setSubject(res.getString("TITLE"));
                aEntry.setBody(res.getString("BODY"));
                aEntry.setLocked(res.getBoolean("LOCKED"));
                aEntry.setOwner(res.getString("USER"));
                aEntry.setModified(true);
                currentDiary.addEntry(aEntry);
            }
            prepGetNewEntries.setInt(1, currentDiary.getID());
            res = executeQuery(prepGetNewEntries);
            while (res.next()) {
                modified = true;
                aEntry = new Entry();
                aEntry.setID(0);
                timedate.setTimeInMillis(res.getLong("START"));
                aEntry.setStart((Calendar) timedate.clone());
                timedate.setTimeInMillis(res.getLong("END"));
                aEntry.setEnd((Calendar) timedate.clone());
                aEntry.setSubject(res.getString("TITLE"));
                aEntry.setBody(res.getString("BODY"));
                aEntry.setLocked(res.getBoolean("LOCKED"));
                aEntry.setOwner(res.getString("USER"));
                aEntry.setModified(true);
                currentDiary.addEntry(aEntry);
            }
            prepRemoveEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveEntries);
            prepRemoveNewEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveNewEntries);
            ResetNextNewEntryID();
        } catch (SQLException ex) {
            LOGIT.severe("Error attempting to retrieve new/modified from the local cache");
            LOGIT.info(ex.getLocalizedMessage());
        }
        return modified;
    }

    /**
     * This clears any entries for the diary that are stored in the local cache.
     *<p>
     * @param ID The diary ID to delete local entries for.
     */
    public void deleteLocalEntries(int ID) {
        try {
            prepRemoveEntries.setInt(1, ID);
            executeUpdate(prepRemoveEntries);
            prepRemoveNewEntries.setInt(1, ID);
            executeUpdate(prepRemoveNewEntries);
            ResetNextNewEntryID();
        } catch (SQLException ex) {
            LOGIT.severe("Error attempting to retrieve new/modified from the local cache");
            LOGIT.info(ex.getLocalizedMessage());
        }
    }

    /**
    /**
     * This returns the next available Diary ID
     *<p>
     * @return The next available Diary ID
     */
    public int GetNextDiaryID() {
        int nextID;
        synchronized (lock) {
            nextID = nextDiaryID;
            nextDiaryID += 1;
        }
        return nextID;
    }

    /**
     * This returns the next available Entry ID
     *<p>
     * @return The next available Entry ID
     */
    public int GetNextEntryID() {
        int nextID;
        synchronized (lock) {
            nextID = nextEntryID;
            nextEntryID += 1;
        }
        return nextID;
    }

    /**
     * This returns the next available New Entry ID
     *<p>
     * @return The next available New Entry ID
     */
    public int GetNextNewEntryID() {
        int nextID;
        synchronized (lock) {
            nextID = nextNewEntryID;
            nextNewEntryID += 1;
        }
        return nextID;
    }

    /**
     * This resets the next available New Entry ID
     */
    public void ResetNextNewEntryID() {
        synchronized (lock) {
            try {
                ResultSet res = executeQuery("select Max(ID) from newentries");
                if (res.next()) {
                    nextNewEntryID = res.getInt(1) + 1;
                } else {
                    nextNewEntryID = 1;
                }
            } catch (SQLException ex) {
            }
        }
    }

    /**
     * This returns the next available Diaries ID for use with user permissions
     *<p>
     * @throws DBServerException
     *<p>
     * @return The next available Diaries ID for use with user permissions
     */
    public int GetNextDiariesID() {
        int nextID;
        synchronized (lock) {
            nextID = nextDiariesID;
            nextDiariesID += 1;
        }
        return nextID;
    }

    /**
     * This logs out a session
     *<p>
     * @param sessionID The sessionID to sign out
     *<p>
     * @throws DBServerException
     *<p>
     */
    public void Logout(String sessionID) throws DBServerException {
        try {
            prepGetUserDiaries.setString(1, sessionID);
            executeUpdate(prepGetUserDiaries);
        } catch (SQLException ex) {
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    /**
     * This retrieves a session
     *<p>
     * @param sessionID The sessionID to retrieve
     *<p>
     * @throws DBServerException
     */
    public String GetSession(String sessionID) throws DBServerException {
        try {
            prepCheckSession.setString(1, sessionID);
            ResultSet res = executeQuery(prepCheckSession);
            if (res.next()) {
                return res.getString("USER");
            }
        } catch (SQLException ex) {
            throw new DBServerException(ex.getLocalizedMessage());
        }
        return null;
    }

    /**
     * This updates an entry in the database. It does not update the DIARYID
     * field as this is not a valid client action.
     *<p>
     * @param updatedEntry The updated entry to write to the DB.
     */
    public void UpdateEntry(Entry updatedEntry, int diaryID) throws DBServerException {
        try {
            PreparedStatement prepSaveEntry;
            if (updatedEntry.getID() == 0) {
                prepSaveEntry = prepNewLocalEntry;
                updatedEntry.setID(GetNextEntryID());
                LOGIT.severe("New entry: " + Integer.toString(updatedEntry.getID()));
                prepSaveEntry.setInt(9, diaryID);
            } else {
                prepSaveEntry = prepUpdateEntry;
            }
            prepSaveEntry.setLong(1, updatedEntry.getStart().getTimeInMillis());
            prepSaveEntry.setLong(2, updatedEntry.getEnd().getTimeInMillis());
            prepSaveEntry.setString(3, updatedEntry.getOwner());
            prepSaveEntry.setString(4, "N");
            prepSaveEntry.setString(5, updatedEntry.getSubject());
            prepSaveEntry.setString(6, updatedEntry.getBody());
            if (updatedEntry.isDeleted()) {
                prepSaveEntry.setInt(7, 1);
            } else {
                prepSaveEntry.setInt(7, 0);
            }
            prepSaveEntry.setInt(8, updatedEntry.getID());
            int res = executeUpdate(prepSaveEntry);
        } catch (SQLException ex) {
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    /**
     * This updates modified entries in the database. It does not update the DIARYID
     * field as this is not a valid client action.
     *<p>
     * @param updatedEntries The vector of entries to write to the DB.
     */
    public void UpdateEntries(Diary currentDiary) throws DBServerException {
        try {
            prepRemoveEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveEntries);
            prepRemoveNewEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveNewEntries);
            ResetNextNewEntryID();
        } catch (SQLException ex) {
        }
        addDiary(currentDiary);
    }

    private void tmpDummyDatabase() {
        ResultSet res = executeQuery("select * from SQLite_Master;");
        try {
            if (!res.next()) {
                executeUpdate("Create table users(USER, DISPLAYNAME, DEFAULTDIARYID, PWDHASH);");
                executeUpdate("Create table passwordSalts(USER, SALT);");
                executeUpdate("Create table userSessions(USER, SESSIONID);");
                executeUpdate("Create table diaries(ID, USER, DIARYID, PERMISSIONS);");
                executeUpdate("Create table diary(ID, NAME, OWNER, REVISION);");
                executeUpdate("Create table entries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED, LOCALMOD);");
                executeUpdate("Create table newentries(ID, DIARYID, START, END, USER, LOCKED, TITLE, BODY, DELETED);");
                LOGIT.info("Database Created Successfully.");
            }
        } catch (SQLException ex) {
            LOGIT.severe("Error when checking for databases existance");
            LOGIT.info(ex.getLocalizedMessage());
        }
    }

    private void RealDatabase() {
        DropTables();
        DropTriggers();
        CreateTables();
        CreateTriggers();
    }

    public void addUser(String usr, String pws, User currentUser) throws DBServerException {
        try {
            prepCheckUserExist.setString(1, usr);
            ResultSet rset = prepCheckUserExist.executeQuery();
            if (rset.next()) {
                rmvUser.setString(1, usr);
                executeUpdate(rmvUser);
                rmvSalt.setString(1, usr);
                rmvSalt.executeUpdate();
            }
            String salt = UUID.randomUUID().toString();
            String hash = secure.getClientHash(pws);
            hash = secure.getServerHash(hash, salt);
            prepAddUsr.setString(1, usr);
            prepAddUsr.setString(2, currentUser.getDisplayName());
            prepAddUsr.setInt(3, currentUser.getDefaultDiaryID());
            prepAddUsr.setString(4, hash);
            prepAddUsr.executeUpdate();
            prepAddSalt.setString(1, usr);
            prepAddSalt.setString(2, salt);
            prepAddSalt.executeUpdate();
            for (Diary nextDiary : currentUser.getDiaries()) {
                prepRemoveDiary.setInt(1, nextDiary.getID());
                executeUpdate(prepRemoveDiary);
                prepRemovePermissions.setInt(1, nextDiary.getID());
                prepRemovePermissions.setString(2, usr);
                executeUpdate(prepRemovePermissions);
                prepAddDiary.setInt(1, nextDiary.getID());
                prepAddDiary.setString(2, nextDiary.getName());
                prepAddDiary.setString(3, nextDiary.getOwnerName());
                prepAddDiary.setInt(4, nextDiary.getRevision());
                executeUpdate(prepAddDiary);
                prepAddDiaries.setInt(1, GetNextDiariesID());
                prepAddDiaries.setString(2, usr);
                prepAddDiaries.setInt(3, nextDiary.getID());
                prepAddDiaries.setInt(4, nextDiary.getPermissions());
                executeUpdate(prepAddDiaries);
            }
        } catch (SQLException ex) {
            LOGIT.severe("Unable to set values in an SQL prepared statement");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        } catch (PasswordHashingException ex) {
            LOGIT.severe("Password hashing services unavailable");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    public void addDiary(Diary currentDiary) throws DBServerException {
        PreparedStatement prepAddEntry;
        try {
            prepRemoveEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveEntries);
            prepRemoveNewEntries.setInt(1, currentDiary.getID());
            executeUpdate(prepRemoveNewEntries);
            for (Entry nextEntry : currentDiary.getEntries()) {
                int entryID = 0;
                switch(nextEntry.getID()) {
                    case 0:
                        prepAddEntry = prepNewLocalEntry;
                        prepAddEntry.setInt(8, GetNextNewEntryID());
                        break;
                    default:
                        prepAddEntry = prepNewEntry;
                        prepAddEntry.setInt(8, nextEntry.getID());
                        prepAddEntry.setBoolean(10, nextEntry.isModified());
                        break;
                }
                prepAddEntry.setLong(1, nextEntry.getStart().getTimeInMillis());
                prepAddEntry.setLong(2, nextEntry.getEnd().getTimeInMillis());
                prepAddEntry.setString(3, nextEntry.getOwner());
                prepAddEntry.setString(4, "N");
                prepAddEntry.setString(5, nextEntry.getSubject());
                prepAddEntry.setString(6, nextEntry.getBody());
                if (nextEntry.isDeleted()) {
                    prepAddEntry.setInt(7, 1);
                } else {
                    prepAddEntry.setInt(7, 0);
                }
                prepAddEntry.setInt(9, currentDiary.getID());
                executeUpdate(prepAddEntry);
            }
        } catch (SQLException ex) {
            LOGIT.severe("Error added a diaries entries to the local cache");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    private String addDiaries(String usr, String diaryid, String premissions) throws DBServerException {
        try {
            prepAddDiaries.setString(1, usr);
            prepAddDiaries.setString(2, diaryid);
            prepAddDiaries.setString(3, premissions);
            prepAddDiaries.executeUpdate();
            return "Succefully Added premission ";
        } catch (SQLException ex) {
            LOGIT.severe("Unable to set values in an SQL prepared statement");
            LOGIT.info(ex.getLocalizedMessage());
            throw new DBServerException(ex.getLocalizedMessage());
        }
    }

    private void CreateTriggers() {
        executeUpdate("CREATE TRIGGER fki_passwordSalts_USER_users_USER" + "BEFORE INSERT ON [passwordSalts]" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"passwordSalts\" violates foreign key constraint \"fki_passwordSalts_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fku_passwordSalts_USER_users_USER" + "BEFORE UPDATE ON [passwordSalts] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"passwordSalts\" violates foreign key \"constraint \"fku_passwordSalts_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fkd_passwordSalts_USER_users_USER" + "BEFORE DELETE ON users" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_passwordSalts_USER_users_USER\"')" + "WHERE (SELECT USER FROM passwordSalts WHERE USER = OLD.USER) IS NOT NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fki_userSessions_USER_users_USER" + "BEFORE INSERT ON [userSessions]" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"userSessions\" violates foreign key constraint \"fki_userSessions_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fku_userSessions_USER_users_USER" + "BEFORE UPDATE ON [userSessions] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"userSessions\" violates foreign key \"constraint \"fku_userSessions_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fkd_userSessions_USER_users_USER" + "BEFORE DELETE ON users" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_userSessions_USER_users_USER\"')" + "WHERE (SELECT USER FROM userSessions WHERE USER = OLD.USER) IS NOT NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fki_diary_OWNER_users_USER" + "BEFORE INSERT ON [diary]" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"diary\" violates foreign key constraint \"fki_diary_OWNER_users_USER\"')" + "WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.OWNER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fku_diary_OWNER_users_USER" + "BEFORE UPDATE ON [diary] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"diary\" violates foreign key constraint \"fku_diary_OWNER_users_USER\"')" + "WHERE NEW.OWNER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.OWNER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fkd_diary_OWNER_users_USER" + "BEFORE DELETE ON users" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_diary_OWNER_users_USER\"')" + "WHERE (SELECT OWNER FROM diary WHERE OWNER = OLD.USER) IS NOT NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fki_diaries_USER_users_USER" + "BEFORE INSERT ON [diaries]" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"diaries\" violates foreign key constraint \"fki_diaries_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fku_diaries_USER_users_USER" + "BEFORE UPDATE ON [diaries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"diaries\" violates foreign key constraint \"fku_diaries_USER_users_USER\"')" + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fkd_diaries_USER_users_USER" + "BEFORE DELETE ON users" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_diaries_USER_users_USER\"')" + "WHERE (SELECT USER FROM diaries WHERE USER = OLD.USER) IS NOT NULL;" + "END;");
        executeUpdate("CREATE TRIGGER fki_diaries_DIARYID_diary_ID" + "BEFORE INSERT ON [diaries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"diaries\" violates foreign key constraint \"fki_diaries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = \"NEW.DIARYID) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fku_diaries_DIARYID_diary_ID" + "BEFORE UPDATE ON [diaries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"diaries\" violates foreign key constraint \"fku_diaries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = \"NEW.DIARYID) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fkd_diaries_DIARYID_diary_ID" + "BEFORE DELETE ON diary" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"diary\" violates foreign key constraint \"fkd_diaries_DIARYID_diary_ID\"') " + "WHERE (SELECT DIARYID FROM diaries WHERE DIARYID = OLD.ID) IS NOT NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fki_entries_DIARYID_diary_ID" + "BEFORE INSERT ON [entries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"entries\" violates foreign key constraint \"fki_entries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = \"NEW.DIARYID) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fku_entries_DIARYID_diary_ID" + "BEFORE UPDATE ON [entries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"entries\" violates foreign key constraint \"fku_entries_DIARYID_diary_ID\"') " + "WHERE NEW.DIARYID IS NOT NULL AND (SELECT ID FROM diary WHERE ID = \"NEW.DIARYID) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fkd_entries_DIARYID_diary_ID" + "BEFORE DELETE ON diary" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"diary\" violates foreign key constraint \"fkd_entries_DIARYID_diary_ID\"') " + "WHERE (SELECT DIARYID FROM entries WHERE DIARYID = OLD.ID) IS NOT NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fki_entries_USER_users_USER" + "BEFORE INSERT ON [entries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'insert on table \"entries\" violates foreign key constraint \"fki_entries_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fku_entries_USER_users_USER" + "BEFORE UPDATE ON [entries] " + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'update on table \"entries\" violates foreign key constraint \"fku_entries_USER_users_USER\"') " + "WHERE NEW.USER IS NOT NULL AND (SELECT USER FROM users WHERE USER = \"NEW.USER) IS NULL; " + "END;");
        executeUpdate("CREATE TRIGGER fkd_entries_USER_users_USER" + "BEFORE DELETE ON users" + "FOR EACH ROW BEGIN" + "SELECT RAISE(ROLLBACK, 'delete on table \"users\" violates foreign key constraint \"fkd_entries_USER_users_USER\"') " + "WHERE (SELECT USER FROM entries WHERE USER = OLD.USER) IS NOT NULL; " + "END;");
    }

    private void DropTables() {
        executeUpdate("drop table users");
        executeUpdate("drop table passwordSalts");
        executeUpdate("drop table userSessions");
        executeUpdate("drop table diaries");
        executeUpdate("drop table diary");
        executeUpdate("drop table entries");
    }

    private void DropTriggers() {
        executeUpdate("DROP TRIGGER fki_passwordSalts_USER_users_USER");
        executeUpdate("DROP TRIGGER fku_passwordSalts_USER_users_USER");
        executeUpdate("DROP TRIGGER fkd_passwordSalts_USER_users_USER");
        executeUpdate("DROP TRIGGER fki_userSessions_USER_users_USER");
        executeUpdate("DROP TRIGGER fku_userSessions_USER_users_USER");
        executeUpdate("DROP TRIGGER fkd_userSessions_USER_users_USER");
        executeUpdate("DROP TRIGGER fki_diary_OWNER_users_USER");
        executeUpdate("DROP TRIGGER fku_diary_OWNER_users_USER");
        executeUpdate("DROP TRIGGER fkd_diary_OWNER_users_USER");
        executeUpdate("DROP TRIGGER fki_diaries_USER_users_USER");
        executeUpdate("DROP TRIGGER fku_diaries_USER_users_USER");
        executeUpdate("DROP TRIGGER fkd_diaries_USER_users_USER");
        executeUpdate("DROP TRIGGER fki_diaries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fku_diaries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fkd_diaries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fki_entries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fku_entries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fkd_entries_DIARYID_diary_ID");
        executeUpdate("DROP TRIGGER fki_entries_USER_users_USER");
        executeUpdate("DROP TRIGGER fku_entries_USER_users_USER");
        executeUpdate("DROP TRIGGER fkd_entries_USER_users_USER");
    }

    private void CreateTables() {
        executeUpdate("CREATE TABLE users(" + "        USER TEXT PRIMARY KEY ON CONFLICT FAIL," + "        DISPLAYNAME NOT NULL," + "        DEFAULTDIARYID," + "        PWDHASH NOT NULL" + ")");
        executeUpdate("CREATE TABLE passwordSalts(" + "        USER TEXT CONSTRAINT FK_passwordSalts_USER REFERENCES users(USER)," + "        SALT NOT NULL	" + ")");
        executeUpdate("CREATE TABLE userSessions(" + "        USER TEXT CONSTRAINT FK_userSessions_USER REFERENCES users(USER)," + "        SESSIONID" + ")");
        executeUpdate("CREATE TABLE diary(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        NAME NOT NULL," + "        OWNER TEXT CONSTRAINT FK_diary_USER REFERENCES users(USER)," + "        REVISION NOT NULL" + ")");
        executeUpdate("CREATE TABLE diaries(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        USER TEXT CONSTRAINT FK_diaries_USER REFERENCES users(USER)," + "        DIARYID INTEGER CONSTRAINT FK_diaries_DIARYID REFERENCES diary(ID)," + "        PERMISSIONS NOT NULL" + ")");
        executeUpdate("CREATE TABLE entries(" + "        ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "        DIARYID INTEGER CONSTRAINT FK_entries_DIARYID REFERENCES diary(ID)," + "        START NOT NULL," + "        END NOT NULL," + "        USER TEXT CONSTRAINT FK_entries_USER REFERENCES users(USER)," + "        LOCKED NOT NULL," + "        TITLE NOT NULL," + "        BODY," + "        DELETED NOT NULL" + ")");
    }
}
