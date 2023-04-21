package ranab.server.ftp.usermanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import ranab.io.IoUtils;
import ranab.server.ftp.FtpConfig;
import ranab.util.BaseProperties;
import ranab.util.EncryptUtils;

/**
 * Properties file based <code>UserManager</code>
 * implementation. We use <code>user.properties</code> file
 * to store user data.
 * 
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class PropertiesUserManager extends UserManager {

    private static final String PREFIX = "FtpServer.user.";

    private static final String USER_PROP = "user.properties";

    private BaseProperties mUserData;

    private File mUserDataFile;

    private boolean mbEncrypt;

    private long mlLastModified;

    /**
     * Instantiate user manager.
     *
     * @param cfg Ftp config object.
     */
    public PropertiesUserManager(FtpConfig cfg) throws IOException {
        super(cfg);
        mUserDataFile = new File(mConfig.getDataDir(), USER_PROP);
        mUserDataFile.createNewFile();
        mUserData = new BaseProperties(mUserDataFile);
        mbEncrypt = cfg.getBoolean(FtpConfig.PREFIX + "prop.encrypt", false);
        mlLastModified = mUserDataFile.lastModified();
        cfg.getLogger().info("Loaded user data file " + mUserDataFile.getAbsolutePath());
    }

    /**
     * Save user data. Store the properties.
     */
    public synchronized void save(User usr) throws IOException {
        if (usr.getName() == null) {
            throw new NullPointerException("User name is null.");
        }
        String thisPrefix = PREFIX + usr.getName() + '.';
        mUserData.setProperty(thisPrefix + User.ATTR_PASSWORD, getPassword(usr));
        mUserData.setProperty(thisPrefix + User.ATTR_HOME, usr.getVirtualDirectory().getRootDirectory());
        mUserData.setProperty(thisPrefix + User.ATTR_ENABLE, usr.getEnabled());
        mUserData.setProperty(thisPrefix + User.ATTR_WRITE_PERM, usr.getVirtualDirectory().getWritePermission());
        mUserData.setProperty(thisPrefix + User.ATTR_MAX_IDLE_TIME, usr.getMaxIdleTime());
        mUserData.setProperty(thisPrefix + User.ATTR_MAX_UPLOAD_RATE, usr.getMaxUploadRate());
        mUserData.setProperty(thisPrefix + User.ATTR_MAX_DOWNLOAD_RATE, usr.getMaxDownloadRate());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mUserDataFile);
            mUserData.store(fos, "Generated file - don't edit (please)");
            mlLastModified = mUserDataFile.lastModified();
        } finally {
            IoUtils.close(fos);
        }
    }

    /**
     * Delete an user. Removes all this user entries from the properties.
     * After removing the corresponding from the properties, save the data.
     */
    public synchronized void delete(String usrName) throws IOException {
        String thisPrefix = PREFIX + usrName + '.';
        Enumeration propNames = mUserData.propertyNames();
        ArrayList remKeys = new ArrayList();
        while (propNames.hasMoreElements()) {
            String thisKey = propNames.nextElement().toString();
            if (thisKey.startsWith(thisPrefix)) {
                remKeys.add(thisKey);
            }
        }
        Iterator remKeysIt = remKeys.iterator();
        while (remKeysIt.hasNext()) {
            mUserData.remove(remKeysIt.next().toString());
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mUserDataFile);
            mUserData.store(fos, "Generated file - don't edit (please)");
            mlLastModified = mUserDataFile.lastModified();
        } finally {
            IoUtils.close(fos);
        }
    }

    /**
     * Get user password. Returns the encrypted value.
     * <pre>
     * If the password value is not null
     *    password = new password 
     * else 
     *   if user does exist
     *     password = old password
     *   else 
     *     password = ""
     * </pre>
     */
    private String getPassword(User usr) {
        String password = usr.getPassword();
        if (password != null) {
            if (mbEncrypt) {
                password = EncryptUtils.encryptMD5(password);
            }
        } else if (doesExist(usr.getName())) {
            String key = PREFIX + usr.getName() + '.' + User.ATTR_PASSWORD;
            password = mUserData.getProperty(key, "");
        }
        if (password == null) {
            password = "";
        }
        return password;
    }

    /**
     * Get all user names.
     */
    public synchronized Collection getAllUserNames() {
        String suffix = '.' + User.ATTR_HOME;
        ArrayList ulst = new ArrayList();
        Enumeration allKeys = mUserData.propertyNames();
        while (allKeys.hasMoreElements()) {
            String key = (String) allKeys.nextElement();
            if (key.endsWith(suffix)) {
                String name = key.substring(PREFIX.length());
                int endIndex = name.length() - suffix.length();
                name = name.substring(0, endIndex);
                ulst.add(name);
            }
        }
        Collections.sort(ulst);
        return ulst;
    }

    /**
     * Load user data.
     */
    public synchronized User getUserByName(String userName) {
        if (!doesExist(userName)) {
            return null;
        }
        String baseKey = PREFIX + userName + '.';
        User user = new User();
        user.setName(userName);
        user.setEnabled(mUserData.getBoolean(baseKey + User.ATTR_ENABLE, true));
        user.getVirtualDirectory().setRootDirectory(mUserData.getFile(baseKey + User.ATTR_HOME, new File("/")));
        user.getVirtualDirectory().setWritePermission(mUserData.getBoolean(baseKey + User.ATTR_WRITE_PERM, false));
        user.setMaxIdleTime(mUserData.getInteger(baseKey + User.ATTR_MAX_IDLE_TIME, 0));
        user.setMaxUploadRate(mUserData.getInteger(baseKey + User.ATTR_MAX_UPLOAD_RATE, 0));
        user.setMaxDownloadRate(mUserData.getInteger(baseKey + User.ATTR_MAX_DOWNLOAD_RATE, 0));
        return user;
    }

    /**
     * User existance check
     */
    public synchronized boolean doesExist(String name) {
        String key = PREFIX + name + '.' + User.ATTR_HOME;
        return mUserData.containsKey(key);
    }

    /**
     * User authenticate method
     */
    public synchronized boolean authenticate(String user, String password) {
        String passVal = mUserData.getProperty(PREFIX + user + '.' + User.ATTR_PASSWORD);
        if (mbEncrypt) {
            password = EncryptUtils.encryptMD5(password);
        }
        return password.equals(passVal);
    }

    /**
     * Reload the user data if necessary
     */
    public synchronized void reload() throws IOException {
        long lastModified = mUserDataFile.lastModified();
        if (lastModified > mlLastModified) {
            FileInputStream fis = new FileInputStream(mUserDataFile);
            mUserData.load(fis);
            fis.close();
            mlLastModified = lastModified;
            getConfig().getLogger().info("File modified - loading " + mUserDataFile.getAbsolutePath());
        }
    }

    /**
     * Close the user manager - remove existing entries.
     */
    public synchronized void dispose() {
        if (mUserData != null) {
            mUserData.clear();
            mUserData = null;
        }
    }
}
