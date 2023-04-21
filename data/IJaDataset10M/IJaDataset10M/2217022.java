package org.meshcms.core;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;
import org.meshcms.util.Path;
import org.meshcms.util.Utils;

/**
 * Profile of a user. Modifications made by calling the set methods are not
 * stored until you use {@link #store}.
 */
public class UserInfo implements Serializable {

    /**
   * Permission to add other users.
   */
    public static final int CAN_ADD_USERS = 1;

    /**
   * Permission to edit pages (in the home path of the user profile).
   */
    public static final int CAN_EDIT_PAGES = 2;

    /**
   * Permission to manage files.
   */
    public static final int CAN_MANAGE_FILES = 4;

    /**
   * Permission to view other user profiles.
   */
    public static final int CAN_VIEW_OTHER_USERINFO = 8;

    /**
   * Permission to do maintainance operations.
   */
    public static final int CAN_DO_ADMINTASKS = 16;

    /**
   * Permission to browse files.
   */
    public static final int CAN_BROWSE_FILES = 32;

    /**
   * Permissions for guest user (non-logged in).
   */
    public static final int GUEST = 0;

    /**
   * Permissions for a member (can't edit files).
   */
    public static final int MEMBER = CAN_BROWSE_FILES | CAN_VIEW_OTHER_USERINFO;

    /**
   * Permissions for an editor (can edit files, but is not an administrator).
   */
    public static final int EDITOR = CAN_EDIT_PAGES | CAN_MANAGE_FILES | CAN_VIEW_OTHER_USERINFO | CAN_BROWSE_FILES;

    /**
   * Permissions for an administrator (full permissions).
   */
    public static final int ADMIN = 0x00FFFFFF;

    protected static final String USERNAME = "P_USN";

    protected static final String PASSWORD = "P_PWS";

    protected static final String HOME_PATH = "P_HPT";

    protected static final String PERMISSIONS = "P_PRM";

    protected static final String E_MAIL = "P_EML";

    protected static final String LANGUAGE = "P_LNG";

    /**
   * Names for user detail fields.
   */
    public static final String[] DETAILS = { "salutation", "name", "surname", "company", "address", "zip", "city", "state", "country", "phone_number", "fax_number", "mobile_phone_number" };

    /**
   * Characters allowed in a username.
   */
    protected static final String VALID_USERNAME_CHARS = "abcdefghijklmnopqrstuvwxyz._0123456789";

    protected static final String SALT = "LV";

    protected Properties info;

    protected boolean global;

    /**
   * Creates a new empty instance. Use {@link #load} to load a defined user.
   */
    public UserInfo() {
        loadGuest();
    }

    /**
   * Sets the username for this user.
   */
    public void setUsername(String username) {
        if (username != null) {
            info.setProperty(USERNAME, username);
        }
    }

    /**
   * Returns the user's username.
   */
    public String getUsername() {
        return getValue(USERNAME);
    }

    /**
   * Sets the password for this user. The password will be encrypted.
   */
    public void setPassword(String password) {
        info.setProperty(PASSWORD, cryptPassword(password));
    }

    /**
   * Sets the password for this user after verification of the old password.
   * The password will be encrypted.
   *
   * @return the result of the operation
   */
    public boolean updatePassword(String oldPassword, String newPassword) {
        if (verifyPassword(oldPassword)) {
            setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
   * Returns the user's (encrypted) password.
   */
    public String getPassword() {
        return getValue(PASSWORD);
    }

    /**
   * Sets the e-mail address of this user.
   * {@link org.meshcms.util.Utils#checkAddress} is used to verify the new
   * address.
   *
   * @return the result of the operation
   */
    public boolean setEmail(String email) {
        if (Utils.checkAddress(email)) {
            info.setProperty(E_MAIL, email);
            return true;
        }
        return false;
    }

    /**
   * Returns the user's e-mail address.
   */
    public String getEmail() {
        return getValue(E_MAIL);
    }

    /**
   * Sets the home path for the user. A user can't edit files outside his own
   * home path.
   */
    public void setHomePath(Path homePath) {
        if (homePath != null) {
            info.setProperty(HOME_PATH, homePath.toString());
        }
    }

    /**
   * Returns the user's home path.
   */
    public Path getHomePath() {
        return new Path(getValue(HOME_PATH));
    }

    /**
   * Sets permissions for the user. This method should be called when creating
   * the user.
   */
    public void setPermissions(int permissions) {
        info.setProperty(PERMISSIONS, Integer.toHexString(permissions));
    }

    /**
   * Returns the user's permissions.
   */
    public int getPermissions() {
        try {
            return Integer.parseInt(getValue(PERMISSIONS), 16);
        } catch (Exception ex) {
        }
        return GUEST;
    }

    /**
   * Returns the preferred locale for the user, in a form like
   * <code>en_US</code>, <code>it</code> or similar.
   */
    public String getPreferredLocaleCode() {
        return getValue(LANGUAGE);
    }

    /**
   * Sets the preferred locale for the user.
   */
    public void setPreferredLocaleCode(String localeCode) {
        if (localeCode == null || localeCode.length() < 2) {
            localeCode = "en_US";
        }
        info.setProperty(LANGUAGE, localeCode);
    }

    /**
   * Loads the guest user.
   */
    public void loadGuest() {
        info = new Properties();
        global = false;
    }

    /**
   * Loads a specific user.
   */
    public boolean load(WebSite webSite, String username, String password) {
        if (Utils.isNullOrEmpty(username)) {
            return false;
        }
        boolean globalUser = false;
        Path userPath = getUserPath(webSite, username);
        if (!webSite.getFile(userPath).exists() && webSite instanceof VirtualWebSite) {
            webSite = ((VirtualWebSite) webSite).getMainWebSite();
            userPath = getUserPath(webSite, username);
            globalUser = true;
        }
        if (webSite.getFile(userPath).exists()) {
            Properties p = (Properties) webSite.loadFromXML(userPath);
            if (p != null && (password == null || p.getProperty(PASSWORD).equals(cryptPassword(password)))) {
                Properties bak = info;
                info = p;
                if (globalUser) {
                    if (canDo(CAN_DO_ADMINTASKS) && getHomePath().isRoot()) {
                        global = true;
                    } else {
                        info = bak;
                        return false;
                    }
                }
                return true;
            }
        } else if (username.equals("admin") && password.equals("admin")) {
            info = new Properties();
            info.setProperty(USERNAME, "admin");
            info.setProperty(PASSWORD, cryptPassword("admin"));
            info.setProperty(HOME_PATH, "");
            info.setProperty(PERMISSIONS, Integer.toHexString(ADMIN));
            info.setProperty(LANGUAGE, "en_US");
            store(webSite);
            this.global = globalUser;
            return true;
        }
        return false;
    }

    /**
   * Stores the user's profile in a file.
   */
    public boolean store(WebSite webSite) {
        if (global) {
            webSite = ((VirtualWebSite) webSite).getMainWebSite();
        }
        return webSite.storeToXML(info, getUserPath(webSite, getUsername()));
    }

    /**
   * Crypts the password if it has not been encrypted yet.
   */
    private String cryptPassword(String password) {
        if (Utils.isNullOrEmpty(password)) {
            return "";
        }
        return com.kingwoodcable.locutus.jfd.JCrypt.crypt(SALT, password);
    }

    /**
   * Checks if the username is valid (i.e. contains characters in
   * {@link #VALID_USERNAME_CHARS} only).
   */
    public static boolean verifyUsername(String username) {
        if (Utils.isNullOrEmpty(username)) {
            return false;
        }
        for (int i = 0; i < username.length(); i++) {
            if (VALID_USERNAME_CHARS.indexOf(username.charAt(i)) == -1) {
                return false;
            }
        }
        return true;
    }

    /**
   * Verifies the given password agains the one in the current profile.
   */
    public boolean verifyPassword(String password) {
        return getPassword().equals(cryptPassword(password));
    }

    private File getUserFile(WebSite webSite, String username) {
        return webSite.getFile(getUserPath(webSite, username));
    }

    private Path getUserPath(WebSite webSite, String username) {
        return webSite.getUsersPath().add(username + ".xml");
    }

    /**
   * Verifies the permissions to do a certain thing. Example:
   * <code>user.canDo(UserInfo.CAN_EDIT_PAGES)</code>
   */
    public boolean canDo(int what) {
        return (getPermissions() & what) != 0;
    }

    /**
   * Verifies all permissions to write the file at a certain path in the web
   * application.
   */
    public boolean canWrite(WebSite webSite, Path filePath) {
        if (filePath == null || !canDo(CAN_EDIT_PAGES) || filePath.isContainedIn(webSite.getAdminPath())) {
            return false;
        }
        return filePath.isContainedIn(getHomePath());
    }

    /**
   * Sets a user's detail. Available details are specified in
   * {@link #DETAILS}. Other details can be set, but they will not be stored
   * when {@link #store} is called.
   *
   * @see #getValue
   */
    public boolean setDetail(String name, String value) {
        if (Utils.searchString(DETAILS, name, false) != -1) {
            info.setProperty(name, value);
            return true;
        }
        return false;
    }

    /**
   * Returns the value of a specific property. It is used internally, but can
   * be use to retrieve the value of user's details.
   *
   * @see #setDetail
   */
    public String getValue(String name) {
        return Utils.noNull(info.getProperty(name));
    }

    /**
   * Returns the value of the given user detail.
   */
    public String getDetailValue(String name) {
        if (name != null) {
            name = name.toLowerCase();
            if (Utils.searchString(DETAILS, name, false) != -1) {
                return getValue(name);
            }
        }
        return null;
    }

    /**
   * Returns the name of the user detail at the given index.
   */
    public String getDetailName(int index) {
        return DETAILS[index];
    }

    /**
   * Returns a string suitable to describe the user. It can be his full name,
   * partial name or username, according to the available data.
   */
    public String getDisplayName() {
        String name = getValue(DETAILS[1]);
        String surname = getValue(DETAILS[2]);
        if (name.equals("") && surname.equals("")) {
            return isGuest() ? "guest" : getUsername();
        }
        if (name.equals("")) {
            return surname;
        }
        if (surname.equals("")) {
            return name;
        }
        return name + " " + surname;
    }

    /**
   * Checks if the user is a guest.
   */
    public boolean isGuest() {
        return getPermissions() == GUEST;
    }

    /**
   * Checks if the user exists. A user exists when the corresponding file
   * exists.
   */
    public boolean exists(WebSite webSite, String username) {
        return getUserFile(webSite, username).exists();
    }

    public boolean isGlobal() {
        return global;
    }
}
