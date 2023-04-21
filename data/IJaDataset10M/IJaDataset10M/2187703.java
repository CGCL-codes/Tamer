package net.sf.boincecho.boinc;

import static net.sf.metagloss.db.DBUtils.getTableName;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import net.sf.boincecho.db.DataBaseManager;
import net.sf.boincecho.db.UserCreditTable;
import net.sf.boincecho.db.UserHostTable;
import net.sf.boincecho.db.UserTable;
import net.sf.boincecho.net.ProjectRPCService;
import net.sf.boincecho.net.WebRPCException;
import net.sf.metagloss.db.AssistCursor;
import net.sf.metagloss.db.DBUtils;
import net.sf.metagloss.xml.DOMFeeder;
import net.sf.metagloss.xml.DOMUtils;
import net.sf.metagloss.xml.XMLBindException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @todo refactor
 */
public final class UserManager {

    private static final String TAG = UserManager.class.getSimpleName();

    private static UserManager instance;

    private List<IUserUpdateListener> updateListeners;

    private final List<User> users;

    private UserManager() {
        this.users = new ArrayList<User>();
        this.removeDuplicateHostEntries();
        this.initUsers();
        Log.i(TAG, "User Manager instantiated");
    }

    private void removeDuplicateHostEntries() {
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase database = dbm.openDataBase(true);
        String tableName = getTableName(UserHostTable.class);
        List<Integer> redHosts = new ArrayList<Integer>();
        Cursor query = database.query(tableName, new String[] { UserHostTable.Id.getColumn() }, null, null, UserHostTable.UserTableId.getColumn() + ", " + UserHostTable.HostId.getColumn(), "count(*)>1", null);
        while (query.moveToNext()) {
            redHosts.add(query.getInt(0));
        }
        query.close();
        for (int id : redHosts) {
            database.execSQL("DELETE FROM " + tableName + " WHERE id = " + id);
        }
        dbm.release();
    }

    /**
	 * Returns the single instance of the <code>UserManager</code>. Upon calling this method
	 * the first time, all {@link User}s are cached - excluding credit snapshots/credit
	 * history.
	 * 
	 * @return Single instance of the class.
	 */
    public static UserManager getInstance() {
        return instance == null ? instance = new UserManager() : instance;
    }

    /**
	 * Gets the credit history for a user.
	 * 
	 * @param user
	 *            User of interest.
	 * @return A list of a user's credit history.
	 */
    public List<CreditSnapshot> getCreditHistory(User user) {
        DataBaseManager dbm = DataBaseManager.getInstance();
        List<CreditSnapshot> history = new ArrayList<CreditSnapshot>();
        SQLiteDatabase database = dbm.openDataBase(false);
        String selection = String.format("%s=%d", UserCreditTable.UserTableId.getColumn(), user.getDatabaseId());
        String tableName = getTableName(UserCreditTable.class);
        Cursor query = database.query(tableName, DBUtils.getColumns(UserCreditTable.class), selection, null, null, null, null, null);
        AssistCursor<UserCreditTable> ac = new AssistCursor<UserCreditTable>(UserCreditTable.class, query);
        while (ac.next()) {
            CreditSnapshot snapshot = new CreditSnapshot();
            ac.inject(snapshot);
            history.add(snapshot);
        }
        query.close();
        DataBaseManager.getInstance().release();
        return history;
    }

    /**
	 * Add a new user to the database for tracking. This approach omits certain information,
	 * such as hosts, but
	 * requires no email or password.
	 * 
	 * @param boincId
	 *            As given by each project.
	 * @param project
	 * @return Newly created user.
	 */
    public User addUser(int boincId, Project project) {
        User user = new User(project);
        user.setBOINCId(boincId);
        this.commitUser(user);
        return user;
    }

    /**
	 * Authenticate and add a new user to the database for tracking.
	 * 
	 * @param email
	 *            Account email.
	 * @param password
	 *            Account password.
	 * @param project
	 *            Project account.
	 * @return Newly created user.
	 */
    public User addUser(String email, String password, Project project) {
        User user = new User(project);
        user.setEmail(email);
        user.setPasswordHash(this.md5(password + email.toLowerCase()));
        this.setAuthenticator(user);
        this.commitUser(user);
        return user;
    }

    private void commitUser(User user) {
        this.updateSync(user);
        this.saveUser(user, DataBaseManager.getInstance());
        this.users.add(user);
        ProjectRPCService.getInstance().addUserToTimer(user);
    }

    public void updateSync(User user) {
        String rpc;
        if (user.getAuthKey() != null) {
            rpc = user.getProject().getBaseURL() + "show_user.php?format=xml&auth=%s";
            rpc = String.format(rpc, user.getAuthKey());
        } else {
            rpc = user.getProject().getBaseURL() + "show_user.php?format=xml&userid=%d";
            rpc = String.format(rpc, user.getBOINCId());
        }
        InputStream stream = null;
        try {
            URL url = new URL(rpc);
            URLConnection urlConnection = url.openConnection();
            stream = urlConnection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(stream);
            Element root = document.getDocumentElement();
            assertXMLDocumentStatus(user, root);
            DOMFeeder userFeeder = new DOMFeeder(root);
            userFeeder.inject(user);
            userFeeder.inject(user.getSnapshot());
            user.getSnapshot().setTimeStamp(System.currentTimeMillis() / 1000);
            List<Element> hostElements = DOMUtils.getChildren(root, "host");
            List<Host> hosts = new ArrayList<Host>();
            DOMFeeder feeder = new DOMFeeder(hostElements);
            while (feeder.next()) {
                Host host = new Host();
                feeder.inject(host);
                hosts.add(host);
            }
            user.setHosts(hosts);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (SAXException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (DOMException e) {
            Log.e(TAG, "For user on project " + user.getProject(), e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }

    private void assertXMLDocumentStatus(User user, Element root) {
        if (root.getNodeName().equals("error")) {
            String error = DOMUtils.getTextOrNull(root, "error_msg");
            throw new WebRPCException(user, (error != null ? error : "unknown error"));
        } else if (DOMUtils.hasChild(root, "error_num")) {
            String error = DOMUtils.getTextOrNull(root, "message");
            throw new WebRPCException(user, (error != null ? error : "unknown error"));
        }
    }

    /**
	 * Tries to find the user's project (BOINC) id for a specific project. If the
	 * email address is tied to several different CPID (Cross-Project ID) hashes,
	 * it will attempt to run through each of them until it finds a matching
	 * id.
	 * <p/>
	 * This method retrieves its data from {@link http://boinc.netsoft-online.com/}.
	 * 
	 * @todo finish
	 * @todo refactor
	 * @param email
	 *            Email to check for in database.
	 * @param project
	 *            Project to match against.
	 * @return Account id for project.
	 */
    public int findUserProjectId(String email, Project project) {
        int boincId = -1;
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase db = dbm.openDataBase(false);
        Set<String> cpidSet = new HashSet<String>();
        Cursor query = db.query(true, getTableName(UserTable.class), new String[] { UserTable.CPID.getColumn() }, UserTable.Email.getColumn() + "='" + email + "'", null, null, null, null, null);
        while (query.moveToNext()) {
            cpidSet.add(query.getString(0));
        }
        dbm.release(query);
        if (cpidSet.size() == 0) {
            throw new BOINCException(String.format("No users matching '%s' where found in the database.", email));
        }
        InputStream stream = null;
        for (String cpid : cpidSet) {
            try {
                URL url = new URL("http://boinc.netsoft-online.com/get_user.php?cpid=" + cpid);
                URLConnection urlConnection = url.openConnection();
                stream = urlConnection.getInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document document = documentBuilder.parse(stream);
                Element root = document.getDocumentElement();
                List<Element> projectElements = DOMUtils.getChildren(root, "project");
                String projectName;
                for (Element e : projectElements) {
                    projectName = DOMUtils.getTextOrNull(e, "name");
                    if (projectName != null && project.toString().equals(projectName)) {
                        String tmp = DOMUtils.getTextOrNull(e, "id");
                        if (tmp != null) {
                            boincId = Integer.parseInt(tmp);
                            break;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SAXException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return boincId;
    }

    /**
	 * Gets a user with the matching {@link UserTable#Id}. The actual user instance
	 * is not created upon calling this method, all users are cached in {@link #initUsers()}.
	 * 
	 * @param databaseRowId
	 *            Database id of user.
	 * @return User identified by database id.
	 */
    public User getUser(int databaseRowId) {
        User user = null;
        for (User u : this.users) {
            if (u.getDatabaseId() == databaseRowId) {
                user = u;
            }
        }
        if (user == null) {
            Log.e(TAG, String.format("getUser(databaseRowId=%d): user was null", databaseRowId));
        }
        return user;
    }

    /**
	 * Gets a list of all users currently in the database.
	 * 
	 * @return List of all users.
	 */
    public List<User> getUsers() {
        return new ArrayList<User>(this.users);
    }

    /**
	 * Gets all unique email addresses found in the database.
	 * 
	 * @return All unique emails.
	 */
    public String[] getEmails() {
        Set<String> emailSet = new HashSet<String>();
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase dataBase = dbm.openDataBase(false);
        Cursor cursor = null;
        try {
            String email = UserTable.Email.getColumn();
            cursor = dataBase.query(getTableName(UserTable.class), new String[] { email }, email + " NOT NULL", null, email, null, email + " ASC");
            while (cursor.moveToNext()) {
                emailSet.add(cursor.getString(0));
            }
        } finally {
            dbm.release(cursor);
        }
        String[] emailArray = emailSet.toArray(new String[emailSet.size()]);
        return emailArray;
    }

    public String setAuthenticator(User user) {
        String rpc = user.getProject().getBaseURL() + "lookup_account.php?email_addr=%s&passwd_hash=%s";
        String auth = null;
        InputStream stream = null;
        try {
            URL url = new URL(String.format(rpc, user.getEmail(), user.getPasswordHash()));
            Log.d(TAG, String.format(user.getProject().name() + ": " + rpc, user.getEmail(), user.getPasswordHash()));
            URLConnection urlConnection = url.openConnection();
            stream = urlConnection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(stream);
            Element root = document.getDocumentElement();
            assertXMLDocumentStatus(user, root);
            auth = DOMUtils.getText(root, "authenticator");
        } catch (MalformedURLException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (IOException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (SAXException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (XMLBindException e) {
            throw new WebRPCException(user, e.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        user.setAuthKey(auth);
        return auth;
    }

    /**
	 * Creates an MD5 hash from the source string. The resulting hash is returned as a
	 * hexadecimal string.
	 * <p/>
	 * Original snippet from <a
	 * href="http://www.androidsnippets.org/snippets/52/index.html">android snippets</a>.
	 * 
	 * @param s
	 *            String to hash.
	 * @return MD5 hash, or null if unable to generate.
	 */
    private String md5(String s) {
        StringBuffer hexString = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hashPart = Integer.toHexString(0xFF & messageDigest[i]);
                if (hashPart.length() == 1) {
                    hashPart = "0" + hashPart;
                }
                hexString.append(hashPart);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(this.getClass().getSimpleName(), "MD5 algorithm not present");
        }
        return hexString != null ? hexString.toString() : null;
    }

    public void remove(User user) {
        if (!this.users.remove(user)) {
            throw new RuntimeException("Failed to remove user: " + user.getName());
        }
    }

    public void deleteUser(User user) {
        DataBaseManager dbm = DataBaseManager.getInstance();
        this.users.remove(user);
        SQLiteDatabase db = dbm.openDataBase(true);
        try {
            int id = user.getDatabaseId();
            db.beginTransaction();
            db.delete(getTableName(UserTable.class), "id=" + id, null);
            db.delete(getTableName(UserCreditTable.class), "user_id=" + id, null);
            db.delete(getTableName(UserHostTable.class), "user_id=" + id, null);
            db.setTransactionSuccessful();
            Log.i(TAG, String.format("DB records deleted for %d:%s", id, user.getName()));
        } finally {
            db.endTransaction();
            dbm.release();
        }
    }

    public synchronized void saveUser(User user, DataBaseManager dbm) {
        SQLiteDatabase db = dbm.openDataBase(true);
        try {
            if (user.getDatabaseId() == -1) {
                this.saveNewUser(user, db);
            }
            this.saveSnapshot(user, db);
            this.saveHosts(user, db);
            this.notifyUpdateListeners(user);
        } finally {
            dbm.release();
        }
    }

    private void saveHosts(User user, SQLiteDatabase database) {
        String tableName = getTableName(UserHostTable.class);
        for (Host host : user.getHosts()) {
            ContentValues data = new ContentValues();
            DBUtils.buildContentValues(data, host);
            data.put(UserHostTable.UserTableId.getColumn(), user.getDatabaseId());
            if (host.getDatabaseId() != Host.NO_DATABASE_ID) {
                database.update(tableName, data, "id=" + host.getDatabaseId(), null);
                Log.i(TAG, String.format("Updated host for user %s", user.getDescription()));
            } else {
                data.putNull(UserHostTable.Id.getColumn());
                int rowId = (int) database.insertOrThrow(tableName, null, data);
                host.setDatabaseId(rowId);
                Log.i(TAG, String.format("Saved host for user '%s' (%s)", user.getName(), user.getProject()));
            }
        }
    }

    private void saveSnapshot(User user, SQLiteDatabase database) {
        String tableName = getTableName(UserCreditTable.class);
        CreditSnapshot snapshot = user.getSnapshot();
        ContentValues data = new ContentValues();
        DBUtils.buildContentValues(data, snapshot);
        data.putNull(UserCreditTable.Id.getColumn());
        data.put(UserCreditTable.UserTableId.getColumn(), user.getDatabaseId());
        database.insert(tableName, null, data);
        Log.i(TAG, String.format("Saved snapshot for user %s", user.getDescription()));
    }

    private void saveNewUser(User user, SQLiteDatabase database) {
        String tableName = getTableName(UserTable.class);
        ContentValues data = new ContentValues();
        DBUtils.buildContentValues(data, user);
        data.putNull(UserTable.Id.getColumn());
        int id = (int) database.insertOrThrow(tableName, null, data);
        user.setDatabaseId(id);
        Log.i(TAG, String.format("Saved new user to database: '%s'", user.getDescription()));
    }

    private void allocateSnapshot(User user, SQLiteDatabase database) {
        String selection = String.format("%s=%d", UserCreditTable.UserTableId.getColumn(), user.getDatabaseId());
        String orderBy = String.format("%s DESC", UserCreditTable.RetrievedOn.getColumn());
        String tableName = getTableName(UserCreditTable.class);
        Cursor query = database.query(tableName, DBUtils.getColumns(UserCreditTable.class), selection, null, null, null, orderBy, "1");
        AssistCursor<UserCreditTable> ac = new AssistCursor<UserCreditTable>(UserCreditTable.class, query);
        ac.next();
        ac.inject(user.getSnapshot());
        query.close();
    }

    private void allocateHosts(User user, SQLiteDatabase database) {
        List<Host> hosts = new ArrayList<Host>();
        String tableName = getTableName(UserHostTable.class);
        String selection = "user_id=" + user.getDatabaseId();
        Cursor query = database.query(tableName, DBUtils.getColumns(UserHostTable.class), selection, null, null, null, null, null);
        AssistCursor<UserHostTable> ac = new AssistCursor<UserHostTable>(UserHostTable.class, query);
        while (ac.next()) {
            Host host = new Host();
            ac.inject(host);
            hosts.add(host);
        }
        user.setHosts(hosts);
        query.close();
    }

    private void initUsers() {
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase database = dbm.openDataBase(false);
        Cursor query = database.query(getTableName(UserTable.class), DBUtils.getColumns(UserTable.class), null, null, null, null, null);
        AssistCursor<UserTable> ac = new AssistCursor<UserTable>(UserTable.class, query);
        while (ac.next()) {
            User user = new User(Project.parse(ac.getString(UserTable.Project)));
            ac.inject(user);
            this.allocateSnapshot(user, database);
            this.allocateHosts(user, database);
            this.users.add(user);
        }
        dbm.release(query);
    }

    public void addUpdateListener(IUserUpdateListener updateListener) {
        if (this.updateListeners == null) this.updateListeners = new LinkedList<IUserUpdateListener>();
        this.updateListeners.add(updateListener);
    }

    public void unregisterUpdateListeners() {
        if (this.updateListeners != null) {
            this.updateListeners.clear();
            Log.d(TAG, "UpdateListener(s) unregistered.");
        }
    }

    private void notifyUpdateListeners(User user) {
        if (this.updateListeners == null) return;
        for (IUserUpdateListener listener : this.updateListeners) {
            try {
                listener.onUserUpdate(user);
            } catch (Exception e) {
                Log.e(TAG, "Failed to update listener: " + e.getMessage());
            }
        }
    }
}
