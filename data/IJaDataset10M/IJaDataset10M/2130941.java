package news_rack.database;

import news_rack.GlobalConstants;
import news_rack.news_filter.Issue;
import news_rack.util.Misc;
import news_rack.util.ParseUtils;
import news_rack.util.DB4oWorkaroundIterator;
import news_rack.archiver.Source;
import news_rack.user_interface.PublicFile;
import news_rack.user_interface.UnknownUserException;
import news_rack.user_interface.InvalidPasswordException;
import news_rack.user_interface.EditProfileException;
import news_rack.news_filter.parser.NRLanguageParser;
import java.io.File;
import java.net.URL;
import java.lang.String;
import java.util.Map;
import java.util.List;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Collections;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.digester.*;
import org.apache.commons.digester.xmlrules.*;

/**
 * class <code>User</code> encapsulates information about
 * an individual user -- personal details and the user's profile
 *
 * @author Subramanya Sastry
 * @version 1.0 12/05/04
 */
public class User {

    private static Log _log = LogFactory.getLog("news_rack.database.User.class");

    private static final String[] _reservedUids = { "admin", "Admin", "administrator", "Administrator", "library", "Library" };

    private static final Hashtable _reservedUsers = null;

    private static Hashtable _userTable;

    private static final LinkedList _publicFiles = new LinkedList();

    private static final LinkedList _allIssues = new LinkedList();

    private static LinkedList _usersToInitialize = new LinkedList();

    private static User GetUser(final String uid) {
        return (User) _userTable.get(uid);
    }

    public static User GetDummyUser() {
        return DUMMY_USER;
    }

    /**
	 * This method returns the user object, given the uid.
	 *
	 * @param  uid  User id whose object is being requested
	 * @return      Returns the user object for the user who has been signed in.
	 */
    public static User GetUser_WithGuestAccess(final String uid) {
        return GetUser(uid);
    }

    /**
	 * This method returns an iterator with all registered users
	 */
    public static Iterator GetAllRegisteredUsers() {
        return _userTable.values().iterator();
    }

    public static Iterator GetAllIssues() {
        return _allIssues.iterator();
    }

    public static void SetUserTable(final Hashtable utbl) {
        _userTable = utbl;
    }

    public static void InitializeReservedUsers() {
        for (int i = 0; i < _reservedUids.length; i++) {
            final String uid = _reservedUids[i];
            final User u = new User();
            _userTable.put(uid, u);
        }
    }

    public static void InitializeUsers() {
        while (!_usersToInitialize.isEmpty()) {
            final User u = (User) _usersToInitialize.removeFirst();
            try {
                GlobalConstants.GetDBInterface().InitProfile(u);
            } catch (final Exception e) {
                _log.error("Unexpected error initializing profile for user " + u._uid + ".  Exception: " + e.toString());
            }
        }
        _usersToInitialize = null;
    }

    /**
	 * This method tries to sign in a user with a specified password.
	 *
	 * @param  uname   User name that has been specified in the sign-in form
	 * @param  passwd  Password that has been specified in the sign-in form
	 * @return         Returns the user object for the user who has been signed in.
	 * @throws UnknownUserException if the user name is unknown
	 * @throws InvalidPasswordException if the specified password is incorrect
	 */
    public static synchronized User SignInUser(final String uname, final String passwd) throws UnknownUserException, InvalidPasswordException, Exception {
        final User u = GetUser(uname);
        if (u == null) throw new UnknownUserException(uname);
        if (!u.PasswordMatches(passwd)) throw new InvalidPasswordException(uname);
        u._isAdmin = uname.equals("admin");
        return u;
    }

    /**
	 * This method checks if the requested user id is available
	 *
	 * @param uid  uid which the user has requested
	 * @return     Returns true if the user id is available, false otherwise.
	 */
    public static boolean UserIdAvailable(final String uid) {
        return (GetUser(uid) == null) && !(uid.equals("admin") || uid.equals("library"));
    }

    /**
	 * This method allocates space in the underlying database for a new user
	 *
	 * @param u   The user who needs to be registered
	 * @return    Returns true if the user was successfully registered,
	 *            false otherwise.
	 */
    public static synchronized boolean RegisterUser(final User u) {
        if (!UserIdAvailable(u.getUid())) return false;
        _userTable.put(u.getUid(), u);
        Misc.CreateDir(GlobalConstants.GetBaseRssDir() + File.separator + u.getUid());
        GlobalConstants.GetDBInterface().RegisterUser(u);
        return true;
    }

    public static Iterator GetPublicFiles() {
        return _publicFiles.iterator();
    }

    private boolean _isAdmin;

    private String _name;

    private String _uid;

    private String _password;

    private String _email;

    private String _workDir;

    private Map _issues;

    private List _files;

    private boolean _isInitialized;

    private boolean _isParsed;

    private boolean _filesModified;

    private transient boolean _reclassificationInProgress = false;

    private transient boolean _downloadInProgress = false;

    private transient boolean _validationInProgress = false;

    private transient boolean _concurrentProfileChange = false;

    private transient Hashtable _feedIdToSrcMap;

    private final transient Log log = LogFactory.getLog(this.getClass());

    public static User DUMMY_USER = new User();

    /**
	 * Dummy constructor
	 */
    public User() {
        final DB_Interface db = GlobalConstants.GetDBInterface();
        _workDir = (db == null) ? "" : db.GetUserSpaceWorkDir(this);
        _issues = db.NewMap();
        _files = db.NewList();
        _isInitialized = false;
        _filesModified = false;
    }

    public User(final String uname, final String pwd) throws Exception {
        final DB_Interface db = GlobalConstants.GetDBInterface();
        _uid = uname;
        _password = PasswordService.encrypt(pwd);
        _workDir = (db == null) ? "" : db.GetUserSpaceWorkDir(this);
        _issues = db.NewMap();
        _files = db.NewList();
        _isInitialized = false;
        _filesModified = false;
    }

    /**
	 * This method checks if the user is an administrator!
	 */
    public boolean IsAdmin() {
        return _isAdmin;
    }

    /**
	 * Change the password for the user!
	 */
    public void ChangePassword(final String oldPwd, final String newPwd) throws InvalidPasswordException, Exception {
        if (!PasswordMatches(oldPwd)) throw new InvalidPasswordException(_uid);
        _password = PasswordService.encrypt(newPwd);
    }

    /**
	 * Reset the password for the user!
	 */
    public void ResetPassword(final String newPwd) throws Exception {
        _password = PasswordService.encrypt(newPwd);
    }

    /**
	 * If an admin, sign in as another user!
	 */
    public synchronized User SignInAsUser(final String uname) throws Exception {
        if (!IsAdmin()) throw new Exception("No Administrative Privileges to sign in as user " + uname);
        final User u = GetUser(uname);
        if (u == null) throw new UnknownUserException(uname);
        u._isAdmin = false;
        return u;
    }

    /**
	 * This method spits out user information in XML-format
	 */
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("\t<user name=\"" + _name + "\"\n");
        sb.append("\t      uid=\"" + _uid + "\"\n");
        sb.append("\t      password=\"" + _password + "\"\n");
        sb.append("\t      email=\"" + _email + "\">\n");
        final Iterator it = GetFiles();
        if (it.hasNext()) {
            sb.append("\t\t<files>\n");
            while (it.hasNext()) sb.append("\t\t\t<file name=\"" + it.next() + "\" />\n");
            sb.append("\t\t</files>\n");
            if (IsValidated()) {
                sb.append("\t\t<profile-validated val=\"true\" />\n");
                sb.append("\t\t<frozen-issues>\n");
                final Iterator issues = GetIssues();
                while (issues.hasNext()) {
                    final Issue i = (Issue) issues.next();
                    if (i.IsFrozen()) {
                        sb.append("\t\t\t<issue name=\"");
                        sb.append(i.GetName());
                        sb.append("\" />\n");
                    }
                }
                sb.append("\t\t</frozen-issues>\n");
            }
        }
        sb.append("\t</user>\n");
        return sb.toString();
    }

    public void Parse() throws Exception {
        if (_isInitialized) _concurrentProfileChange = true;
        _isInitialized = false;
        (new NRLanguageParser()).ParseFiles(this, _files.iterator());
        _isInitialized = true;
    }

    public boolean HasFile(final String f) {
        return _files.contains(f);
    }

    public void RemoveFile(final String f) {
        _files.remove(f);
    }

    public Iterator GetFiles() {
        return _files.iterator();
    }

    public boolean IsValidated() {
        return _isInitialized;
    }

    public boolean ConcurrentProfileModification() {
        return _concurrentProfileChange;
    }

    private void RemoveFileFromList(final List l, final String f) {
        l.remove(new PublicFile(f, this._uid));
    }

    private void AddFileToList(final List l, final String f) {
        l.add(new PublicFile(f, this._uid));
    }

    private void UpdatePublicFileList(final Iterator it, final List pfList) {
        if (it == null) return;
        while (it.hasNext()) AddFileToList(pfList, (String) it.next());
    }

    private void RemoveFilesFromList(final Iterator it, final List pfList) {
        if (it == null) return;
        while (it.hasNext()) RemoveFileFromList(pfList, (String) it.next());
    }

    /**
	 * Sets the name of the user
	 * @param name  User's name
	 */
    public void setName(final String name) {
        _name = name;
    }

    /**
	 * Sets the uid of the user
	 * @param uid   User id of the user
	 */
    public void setUid(final String uid) {
        _uid = uid;
    }

    /**
	 * Sets the password of the user
	 * @param password   User's password
	 */
    public void setPassword(final String password) {
        _password = password;
    }

    /**
	 * Sets the email of the user
	 * @param email   User's email
	 */
    public void setEmail(final String email) {
        _email = email;
    }

    /**
	 * Sets the list of profile files for the user.  This method is called
	 * by the Jakarta XML digester when the user table is being loaded up.
	 * @param o  The list of profile files
	 */
    public void setFiles(final Object o) {
        final DB_Interface db = GlobalConstants.GetDBInterface();
        _workDir = (db == null) ? "" : db.GetUserSpaceWorkDir(this);
        _issues = db.NewMap();
        _files = db.NewList();
        _files.addAll((List) o);
    }

    public void RecordForInitialization(final Object o) {
        if (o.equals("true")) _usersToInitialize.add(this);
    }

    private void InitProfileFromDatabase(final Object o) {
        if (o.equals("true")) try {
            GlobalConstants.GetDBInterface().InitProfile(this);
        } catch (final Exception e) {
            _log.error("Unexpected error initializing profile for user " + _uid + ".  Exception: " + e.toString());
        }
    }

    public void SetFrozenIssue(final String name) {
        try {
            GetIssue(name).Freeze();
        } catch (final Exception e) {
            _log.error("Error freezing issue " + name + " for user " + _uid + ".  Exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
	 * Gets the user name
	 */
    public String getName() {
        return _name;
    }

    /**
	 * Gets the user's id
	 */
    public String getUid() {
        return _uid;
    }

    /**
	 * Gets the user's email id
	 */
    public String getEmail() {
        return _email;
    }

    /**
	 * Gets the list of profile files
	 */
    public Iterator getFiles() {
        return GetFiles();
    }

    /**
	 * Gets the active profile
	 */
    public String GetLastDownloadTime() {
        return GlobalConstants.DF.format(news_rack.archiver.DownloadNewsTask.GetLastDownloadTime());
    }

    public String GetWorkDir() {
        return _workDir;
    }

    private void Invalidate() {
        RemoveFilesFromList(GetFiles(), _publicFiles);
        GlobalConstants.GetDBInterface().RemoveMap(_issues);
        GlobalConstants.GetDBInterface().RemoveAllProfileCollectionsForUser(getUid());
        _issues = GlobalConstants.GetDBInterface().NewMap();
        _feedIdToSrcMap = null;
        _isInitialized = false;
        _isParsed = false;
        _reclassificationInProgress = false;
        _downloadInProgress = false;
    }

    /**
	 * Add a issue to the profile
	 */
    public void AddIssue(final Issue i) throws Exception {
        if (_issues.get(i.GetName()) != null) throw new Exception("ERROR! An issue already exists with name " + i.GetName()); else _issues.put(i.GetName(), i);
    }

    /**
	 * Gets all the issues defined in this profile
	 */
    public Iterator GetIssues() {
        return (IsValidated()) ? DB4oWorkaroundIterator.GetMapValuesIterator(_issues) : null;
    }

    /**
	 * Fetches the issue object given an issue name
	 * @param n  Name of the issue
	 */
    public Issue GetIssue(final String n) {
        return (Issue) _issues.get(n);
    }

    public Iterator GetUsedSources() {
        final Hashtable h = new Hashtable();
        for (final Iterator issues = GetIssues(); issues.hasNext(); ) {
            final Issue i = (Issue) issues.next();
            for (final Iterator it = i.GetUsedSources(); it.hasNext(); ) {
                final Source s = (Source) it.next();
                h.put(s, s);
            }
        }
        return h.values().iterator();
    }

    /**
	 * Disables the active profile
	 */
    public void InvalidateIssues(boolean filesModified) {
        _validationInProgress = false;
        if (_log.isDebugEnabled()) _log.debug("Validated value is " + IsValidated());
        if (!IsValidated()) return;
        _filesModified = filesModified;
        final Iterator it = GetIssues();
        while (it.hasNext()) {
            final Issue i = (Issue) it.next();
            i.Invalidate();
            _allIssues.remove(i);
        }
        Invalidate();
    }

    public Source GetNewsSourceByID(final String feedId) {
        return (IsValidated()) ? (Source) _feedIdToSrcMap.get(feedId) : null;
    }

    public String GetNewsSourceNameByID(final String feedId) {
        final Source s = GetNewsSourceByID(feedId);
        if (s != null) return s._name; else {
            Misc.Error("For user " + _uid + ", no news source found by id " + feedId + "!  Perhaps the news source id has been removed from the profile (but the archive has news that refers to this older id)?");
            return feedId;
        }
    }

    /**
	 * This method adds a file to the list of profile files
	 * Throws an EditProfileException if the addition fails for some reason
	 * @param f  the file
	 */
    public void AddFile(final String f) throws EditProfileException {
        if (HasFile(f)) throw new EditProfileException("A file exists with name <b>" + f + "</b>.  Cannot overwrite existing file.  You have to delete the existing file before you can add the new file with the same name."); else _files.add(f);
    }

    public void RenameFile(final String oldName, final String newName) throws EditProfileException {
        if (oldName.indexOf(File.separator) != -1) throw new EditProfileException("Invalid source file name: " + oldName); else if (newName.indexOf(File.separator) != -1) throw new EditProfileException("No " + File.separator + " allowed in file name! Invalid file name: " + newName);
        if (!HasFile(oldName)) throw new EditProfileException("File " + oldName + " does not exist");
        if (HasFile(newName)) throw new EditProfileException("A file with name " + newName + " already exists!  Please pick a different name!");
        final String rpath = GlobalConstants.GetDBInterface().GetFileUploadArea(this);
        final File f1 = new File(rpath + File.separator + oldName);
        final File f2 = new File(rpath + File.separator + newName);
        if (!f1.renameTo(f2)) throw new EditProfileException("Renaming to " + newName + " failed!");
        RemoveFile(oldName);
        AddFile(newName);
        RemoveFileFromList(_publicFiles, oldName);
        AddFileToList(_publicFiles, newName);
    }

    /**
	 * This method deletes a file from the user's profile-related files
	 * @param name  the name of the file to be deleted 
	 * @throws EditProfileException if the file type is not one of the above
	 */
    public void DeleteFile(final String name) throws EditProfileException {
        if (!HasFile(name)) throw new EditProfileException("File " + name + " does not exist");
        RemoveFile(name);
        InvalidateIssues(true);
    }

    public void InitializeUserObject() {
        _feedIdToSrcMap = new Hashtable();
        UpdatePublicFileList(GetFiles(), _publicFiles);
        Iterator it = GetIssues();
        while (it.hasNext()) {
            final Object o = it.next();
            final int p = Collections.binarySearch(_allIssues, o);
            if (p >= 0) {
                if (_log.isInfoEnabled()) _log.info("Ho Ho Ho! Found the issue " + o + " in the issues list!");
            } else {
                final Issue i = (Issue) o;
                final int posn = -(p + 1);
                if (_log.isInfoEnabled()) _log.info("Adding " + i.GetUser().getUid() + ":" + i.GetName() + " to the issues list!");
                _allIssues.add(posn, o);
            }
            ((Issue) o).ResetMaxNewsID();
        }
        it = GetSources();
        while (it.hasNext()) {
            final Source s = (Source) it.next();
            if (_log.isDebugEnabled()) _log.debug("Adding source " + s + " with uniqid " + s._uniqId + " to map to " + _uid + ":" + s._utag);
            final Source oldS = (Source) _feedIdToSrcMap.put(s._feedId, s);
            if (oldS != null) _log.error("ERROR! ERROR! ERROR! Multiple sources defined with same id: " + s._feedId + " for user " + getUid());
        }
    }

    private void InitializeIssues(final boolean genScanners) throws Exception {
        Parse();
        if (ParseUtils.EncounteredParseErrors(this)) throw new Exception("Parsing errors");
        InitializeUserObject();
        for (final Iterator it = GetIssues(); it.hasNext(); ) {
            final Issue i = (Issue) it.next();
            if (_log.isInfoEnabled()) _log.info("READING news for issue " + i.GetName() + "[" + i.hashCode() + "]");
            i.ReadCategorizedNews();
            if (genScanners) {
                i.Gen_JFLEX_RegExps();
                i.CompileScanners(_workDir);
                if (_log.isInfoEnabled()) _log.info("-- DONE GENERATING SCANNERS for " + i.GetName() + " --");
            }
        }
    }

    /**
	 * This method validates the user's profile and builds issues 
	 * defined in the user's files.  Any errors in the user's profile
	 * are reported.  News cannot be downloaded until issues are validated!
	 */
    public void ValidateIssues() throws Exception {
        if (_validationInProgress) throw new Exception("Validation in progress ... check again in a little while ... the server is probably overloaded!");
        try {
            _validationInProgress = true;
            InvalidateIssues(_filesModified);
            _isInitialized = false;
            InitializeIssues(_filesModified);
            _isInitialized = true;
        } catch (final Exception e) {
            final Iterator it = GetIssues();
            if (it != null) {
                while (it.hasNext()) ((Issue) it.next()).Invalidate();
            }
            Invalidate();
            throw e;
        } finally {
            _validationInProgress = false;
        }
    }

    /**
	 * This method verifies if the specified passwords the user's password
	 * @param p  The password to be checked
	 */
    public boolean PasswordMatches(final String p) throws Exception {
        final String encPassword = PasswordService.encrypt(p);
        return encPassword.equals(_password);
    }

    /**
	 * Check if a different user can access a file in this user's space
	 * @param u  User trying to access the file
	 * @param f  The file being accessed
	 */
    public boolean FileAccessible(final User u, final String f) {
        final PublicFile pf = new PublicFile(f, this._uid);
        if (_log.isInfoEnabled()) _log.info(u.getUid() + " trying to access " + pf);
        if (this == u) return true; else if (_publicFiles.contains(pf)) return true; else return false;
    }

    public boolean CanAccessFile(final User u, final String f) {
        return u.FileAccessible(this, f);
    }

    public boolean CanAccessFile(final String uid, final String f) {
        return CanAccessFile(GetUser(uid), f);
    }

    /**
	 * Downloads latest news and classifies them based on rules specified in
	 * the user's active profile.
	 */
    public void DownloadNews() throws Exception {
        if (!IsValidated()) throw new Exception("No issues defined yet!  Cannot download news!");
        try {
            DoPreDownloadBookkeeping();
            for (final Iterator it = GetIssues(); it.hasNext(); ) {
                if (ConcurrentProfileModification()) break;
                final Hashtable news = new Hashtable();
                final Issue i = (Issue) it.next();
                i.DownloadNews(news);
                i.ScanAndClassifyNewsItems(news);
                i.UpdateMaxNewsIds(news);
                i.SortArticles();
                i.StoreNewsToArchive();
            }
        } catch (final Exception e) {
            throw e;
        } finally {
            DoPostDownloadBookkeeping();
        }
    }

    public void DoPreDownloadBookkeeping() throws Exception {
        if (_downloadInProgress) throw new Exception("Download in Progress. Duplicate Request!");
        _concurrentProfileChange = false;
        _downloadInProgress = true;
    }

    public void DoPostDownloadBookkeeping() {
        _downloadInProgress = false;
    }

    public Iterator GetSources() {
        return (IsValidated()) ? GetUsedSources() : (new LinkedList()).iterator();
    }

    /**
	 * Reclassify news by reading in news from the entire
	 * archive since the beginning of time!!
	 *
	 * @param iname      Issue to reclassify
	 * @param srcs       Array of sources to classify from
	 * @param allSrcs    Should all sources be used?
	 * @param sd         Start date from which news should be classified (format is yyyymmdd)
	 * @param ed         End date from which news should be classified   (format is yyyymmdd)
	 * @param allDates   If true, all news items from the archive will
	 *                   be classified.  The sdate and edate parameter values
	 *                   will be ignored
	 * @param resetCats  If true, the categories will be reset and
	 *                   all existing news articles will be removed.
	 *                   If false, existing news articles will be retained.
	 *
	 * FIXME: This method should actually take a range of
	 * dates, a set of dates, or some other input like that.
	 */
    public void ReclassifyNews(final String iname, final String[] srcs, final boolean allSrcs, final String sd, final String ed, final boolean allDates, final boolean resetCats) throws Exception {
        if (allDates) {
            final String s = "Sorry! Turned off reclassification for the entire archive!" + "Please select a subset of news sources and/or a subset of dates (most recent)!" + "If you really want to query the entire archive for all sources, call this method " + "one month at a time!  Thanks for your understanding while this feature is fixed!";
            throw new Exception(s);
        }
        if (_reclassificationInProgress) throw new Exception("Reclassification in Progress. Duplicate Request!");
        try {
            _reclassificationInProgress = true;
            _concurrentProfileChange = false;
            final Issue issue = GetIssue(iname);
            if (resetCats) issue.ClearNews();
            if (_log.isInfoEnabled()) _log.info("Request to reclassify news");
            if (allSrcs) {
                int count = 0;
                final Iterator it = issue.GetUsedSources();
                while (it.hasNext()) {
                    if (ConcurrentProfileModification()) break;
                    issue.ReclassifyNews((Source) it.next(), allDates, sd, ed);
                    count++;
                }
            } else for (int i = 0; i < srcs.length; i++) {
                if (ConcurrentProfileModification()) break;
                issue.ReclassifyNews((Source) _feedIdToSrcMap.get(srcs[i]), allDates, sd, ed);
            }
        } catch (final Exception e) {
            throw e;
        } finally {
            _reclassificationInProgress = false;
        }
    }

    /**
	 * This method is a supporter method to test "Profile.java" as a standalone
	 * application.  This method reads news items from an index file and sets
	 * up a hashtable of news items -- mapped from their local copy name to the
	 * NewsItem object.
	 */
    public void SetupNewsTable(final Hashtable news, final String newsIndexFile) {
        try {
            final File newsIndex = new File(newsIndexFile);
            if (_log.isInfoEnabled()) _log.info("file - " + newsIndex);
            if (newsIndex.exists()) {
                final String r = GlobalConstants.GetProperty("flatfiledb.newsListing.digesterRules");
                final URL digesterRules = getClass().getResource(r);
                if (_log.isInfoEnabled()) _log.info("resource - " + r);
                if (_log.isInfoEnabled()) _log.info("rules    - " + digesterRules);
                final Digester digester = DigesterLoader.createDigester(digesterRules);
                final List arts = new java.util.ArrayList();
                digester.push(arts);
                digester.parse(Misc.GetUTF8Reader(newsIndexFile));
                final Iterator it = arts.iterator();
                while (it.hasNext()) {
                    final NewsItem ni = (NewsItem) it.next();
                    news.put(ni.GetLocalCopyPath(), ni);
                }
            }
        } catch (final Exception exc) {
            _log.error("Exception reading news index file - " + newsIndexFile + "\n" + exc);
            exc.printStackTrace();
        }
    }
}
