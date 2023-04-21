package net.wastl.webmail.storage.simple;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import javax.servlet.UnavailableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.wastl.webmail.storage.*;
import net.wastl.webmail.server.*;
import net.wastl.webmail.config.*;
import net.wastl.webmail.misc.*;
import net.wastl.webmail.xml.*;
import net.wastl.webmail.exceptions.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;

/**
 * This is the SimpleStorage class for the non-enterprise edition of WebMail.
 * It provides means of getting and storing data in ZIPFiles and
 * ResourceBundles.
 *
 * @see net.wastl.webmail.server.Storage
 * @author Sebastian Schaffert
 * @versin $Revision: 111 $
 */
public class SimpleStorage extends FileStorage {

    private static Log log = LogFactory.getLog(FileStorage.class);

    public static final String user_domain_separator = "|";

    protected Hashtable resources;

    protected Hashtable vdoms;

    protected ExpireableCache user_cache;

    protected int user_cache_size = 100;

    /**
     * Initialize SimpleStorage.
     * Fetch Configuration Information etc.
     */
    public SimpleStorage(WebMailServer parent) throws UnavailableException {
        super(parent);
        saveXMLSysData();
    }

    protected void initConfig() throws UnavailableException {
        log.info("Configuration ... ");
        loadXMLSysData();
        log.info("successfully parsed XML configuration file.");
    }

    protected void loadXMLSysData() throws UnavailableException {
        String datapath = parent.getProperty("webmail.data.path");
        String file = "file://" + datapath + System.getProperty("file.separator") + "webmail.xml";
        Document root;
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            root = parser.parse(file);
            log.debug("Configuration file parsed, document: " + root);
            sysdata = new XMLSystemData(root, cs);
            log.debug("SimpleStorage: WebMail configuration loaded.");
        } catch (Exception ex) {
            log.error("SimpleStorage: Failed to load WebMail configuration file", ex);
            throw new UnavailableException(ex.getMessage());
        }
    }

    protected void saveXMLSysData() {
        try {
            Document d = sysdata.getRoot();
            OutputStream cfg_out = new FileOutputStream(parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + "webmail.xml");
            XMLCommon.writeXML(d, cfg_out, "file://" + parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "sysdata.dtd");
            cfg_out.flush();
            cfg_out.close();
            sysdata.setLoadTime(System.currentTimeMillis());
            log.debug("SimpleStorage: WebMail configuration saved.");
        } catch (Exception ex) {
            log.error("SimpleStorage: Error while trying to save WebMail configuration", ex);
        }
    }

    protected void initCache() {
        super.initCache();
        cs.configRegisterIntegerKey(this, "CACHE SIZE USER", "100", "Size of the user cache");
        try {
            user_cache_size = 100;
            user_cache_size = Integer.parseInt(getConfig("CACHE SIZE USER"));
        } catch (NumberFormatException e) {
        }
        if (user_cache == null) {
            user_cache = new ExpireableCache(user_cache_size);
        } else {
            user_cache.setCapacity(user_cache_size);
        }
    }

    public Enumeration getUsers(String domain) {
        String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator");
        File f = new File(path);
        if (f.canRead() && f.isDirectory()) {
            final String[] files = f.list(new FilenameFilter() {

                public boolean accept(File file, String s) {
                    if (s.endsWith(".xml")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            return new Enumeration() {

                int i = 0;

                public boolean hasMoreElements() {
                    return i < files.length;
                }

                public Object nextElement() {
                    int cur = i++;
                    return files[cur].substring(0, files[cur].length() - 4);
                }
            };
        } else {
            log.warn("SimpleStorage: Could not list files in directory " + path);
            return new Enumeration() {

                public boolean hasMoreElements() {
                    return false;
                }

                public Object nextElement() {
                    return null;
                }
            };
        }
    }

    public XMLUserData createUserData(String user, String domain, String password) throws CreateUserDataException {
        XMLUserData data;
        String template = parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "userdata.xml";
        File f = new File(template);
        if (!f.exists()) {
            log.warn("SimpleStorage: User configuration template (" + template + ") doesn't exist!");
            throw new CreateUserDataException("User configuration template (" + template + ") doesn't exist!", user, domain);
        } else if (!f.canRead()) {
            log.warn("SimpleStorage: User configuration template (" + template + ") is not readable!");
            throw new CreateUserDataException("User configuration template (" + template + ") is not readable!", user, domain);
        }
        Document root;
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            root = parser.parse("file://" + template);
            data = new XMLUserData(root);
            data.init(user, domain, password);
            if (getConfig("SHOW ADVERTISEMENTS").toUpperCase().equals("YES")) {
                if (user.indexOf("@") != -1) {
                    data.setSignature(user + "\n\n" + getConfig("ADVERTISEMENT MESSAGE"));
                } else {
                    data.setSignature(user + "@" + domain + "\n\n" + getConfig("ADVERTISEMENT MESSAGE"));
                }
            } else {
                if (user.indexOf("@") != -1) {
                    data.setSignature(user);
                } else {
                    data.setSignature(user + "@" + domain);
                }
            }
            data.setTheme(parent.getDefaultTheme());
            WebMailVirtualDomain vdom = getVirtualDomain(domain);
            data.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://" + vdom.getDefaultServer(), user, password, vdom.getImapBasedir());
        } catch (Exception ex) {
            log.warn("SimpleStorage: User configuration template (" + template + ") exists but could not be parsed", ex);
            throw new CreateUserDataException("User configuration template (" + template + ") exists but could not be parsed", user, domain);
        }
        return data;
    }

    /**
     * @see net.wastl.webmail.server.Storage.getUserData()
     */
    public XMLUserData getUserData(String user, String domain, String password, boolean authenticate) throws UserDataException, InvalidPasswordException {
        if (authenticate) {
            auth.authenticatePreUserData(user, domain, password);
        }
        if (user.equals("")) {
            return null;
        }
        XMLUserData data = (XMLUserData) user_cache.get(user + user_domain_separator + domain);
        if (data == null) {
            user_cache.miss();
            String filename = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator") + user + ".xml";
            boolean error = true;
            File f = new File(filename);
            if (f.exists() && f.canRead()) {
                log.info("SimpleStorage: Reading user configuration (" + f.getAbsolutePath() + ") for " + user);
                long t_start = System.currentTimeMillis();
                try {
                    DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document root = parser.parse(new InputSource(new InputStreamReader(new FileInputStream(filename), "UTF-8")));
                    data = new XMLUserData(root);
                    log.debug("SimpleStorage: Parsed Document " + root);
                    error = false;
                } catch (Exception ex) {
                    log.warn("SimpleStorage: User configuration for " + user + " exists but could not be parsed (" + ex.getMessage() + ")", ex);
                    error = true;
                }
                long t_end = System.currentTimeMillis();
                log.debug("SimpleStorage: Parsing of XML userdata for " + user + ", domain " + domain + " took " + (t_end - t_start) + "ms.");
                if (authenticate) {
                    auth.authenticatePostUserData(data, domain, password);
                }
            }
            if (error && !f.exists()) {
                log.info("UserConfig: Creating user configuration for " + user);
                data = createUserData(user, domain, password);
                error = false;
                if (authenticate) {
                    auth.authenticatePostUserData(data, domain, password);
                }
            }
            if (error) {
                log.error("UserConfig: Could not read userdata for " + user + "!");
                throw new UserDataException("Could not read userdata!", user, domain);
            }
            user_cache.put(user + user_domain_separator + domain, data);
        } else {
            user_cache.hit();
            if (authenticate) {
                auth.authenticatePostUserData(data, domain, password);
            }
        }
        return data;
    }

    public void saveUserData(String user, String domain) {
        try {
            String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain;
            File p = new File(path);
            if ((p.exists() && p.isDirectory()) || p.mkdirs()) {
                File f = new File(path + System.getProperty("file.separator") + user + ".xml");
                if ((!f.exists() && p.canWrite()) || f.canWrite()) {
                    XMLUserData userdata = getUserData(user, domain, "", false);
                    Document d = userdata.getRoot();
                    long t_start = System.currentTimeMillis();
                    FileOutputStream out = new FileOutputStream(f);
                    XMLCommon.writeXML(d, out, "file://" + parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "userdata.dtd");
                    out.flush();
                    out.close();
                    long t_end = System.currentTimeMillis();
                    log.debug("SimpleStorage: Serializing userdata for " + user + ", domain " + domain + " took " + (t_end - t_start) + "ms.");
                } else {
                    log.warn("SimpleStorage: Could not write userdata (" + f.getAbsolutePath() + ") for user " + user);
                }
            } else {
                log.error("SimpleStorage: Could not create path " + path + ". Aborting with user " + user);
            }
        } catch (Exception ex) {
            log.error("SimpleStorage: Unexpected error while trying to save user configuration " + "for user " + user + "(" + ex.getMessage() + ").", ex);
        }
    }

    /**
     * Delete a WebMail user
     * @param user Name of the user to delete
     */
    public void deleteUserData(String user, String domain) {
        String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain + System.getProperty("file.separator") + user + ".xml";
        File f = new File(path);
        if (!f.canWrite() || !f.delete()) {
            log.error("SimpleStorage: Could not delete user " + user + " (" + path + ")!");
        } else {
            log.info("SimpleStorage: Deleted user " + user + "!");
        }
        user_cache.remove(user + user_domain_separator + domain);
    }

    public String toString() {
        String s = "SimpleStorage:\n" + super.toString();
        s += " - user cache:  Capacity " + user_cache.getCapacity() + ", Usage " + user_cache.getUsage();
        s += ", " + user_cache.getHits() + " hits, " + user_cache.getMisses() + " misses\n";
        return s;
    }
}
