package de.kout.wlFxp;

import de.kout.wlFxp.ftp.FtpServer;
import de.kout.wlFxp.ftp.Site;
import de.kout.wlFxp.interfaces.wlFrame;
import de.kout.wlFxp.view.MainFrame;
import de.kout.wlFxp.view.commands.Command;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * 
DOCUMENT ME!
 *
 * @author alex
 */
public class Configuration {

    /** Description of the Field */
    public boolean autoreconnect;

    /** Description of the Field */
    public boolean autoresume;

    /** Description of the Field */
    public boolean cache = true;

    DefaultMutableTreeNode cmdRoot;

    String[] fileExist = new String[9];

    Vector<FtpServer> history;

    String settings = System.getProperty("user.home", ".") + File.separator + ".wlFxp";

    String historyFile = settings + File.separator + "history";

    int identdPort;

    String identUser;

    String idleAction = "NOOP";

    boolean isIdentd;

    boolean isIdentUser;

    String locale;

    boolean noop;

    int noopTime = 120;

    /** Description of the Field */
    public boolean passive = true;

    String plaf = "";

    Preferences prefs;

    Vector<String> prioList;

    String prioListFile = settings + File.separator + "priolist";

    String proxyHost;

    String proxyPasswd;

    String proxyPort;

    String proxyType;

    String proxyUser;

    /** Description of the Field */
    public int retrycount = 10;

    /** Description of the Field */
    public int retrydelay = 60;

    /** Description of the Field */
    public boolean showHidden;

    boolean skipEmptyDir;

    boolean skipEmptyFile;

    Vector<String> skiplist;

    String skiplistFile = settings + File.separator + "skiplist";

    /** Description of the Field */
    public int timeout = 20;

    boolean useSkiplist;

    String sites2File = settings + File.separator + "sites2";

    String sitesFile = settings + File.separator + "sites";

    DefaultMutableTreeNode sitesRoot;

    private wlFrame frame;

    String qdir = settings + File.separator + "queues";

    /** Description of the Field */
    public volatile boolean abort;

    /**
	 * Creates a new Configuration object.
	 *
	 * @param frame DOCUMENT ME!
	 */
    public Configuration(wlFrame frame) {
        this.frame = frame;
        Utilities.print("loading Commands...\n");
        frame.setSplashMsg("loading Commands...");
        loadCommands();
        Utilities.print("loading History...\n");
        frame.setSplashMsg("loading History...");
        loadHistory();
        Utilities.print("loading Skiplist...\n");
        frame.setSplashMsg("loading Skiplist...");
        loadSkiplist();
        Utilities.print("loading Priority List...\n");
        frame.setSplashMsg("loading Priority List...");
        loadPrioList();
        Utilities.print("loading Sites...\n");
        frame.setSplashMsg("loading Sites...");
        loadSites();
        Utilities.print("importing preferences...\n");
        frame.setSplashMsg("importing preferences...");
        importPrefs();
    }

    /**
	 * load the commands menu
	 */
    private void loadCommands() {
        InputStream is = null;
        Properties props = new Properties();
        try {
            is = new BufferedInputStream(new FileInputStream(settings + File.separator + "cmds.properties"));
        } catch (FileNotFoundException e) {
            return;
        }
        try {
            props.load(is);
        } catch (IllegalArgumentException e) {
        } catch (IOException e) {
        }
        cmdRoot = new DefaultMutableTreeNode(new Command("commands"));
        Vector<DefaultMutableTreeNode> cmds = new Vector<DefaultMutableTreeNode>(100);
        cmds.addElement(cmdRoot);
        DefaultMutableTreeNode parent;
        String value;
        String[] t;
        while (!cmds.isEmpty()) {
            parent = cmds.firstElement();
            cmds.removeElementAt(0);
            value = (String) props.getProperty(parent.toString());
            if ((value != null) && value.startsWith("{")) {
                value = value.substring(1, value.length() - 1);
                t = Utilities.split(value, ";");
                for (int i = 0; i < t.length; i++) {
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(new Command(t[i]));
                    parent.add(child);
                    cmds.addElement(child);
                }
            } else if (value != null) {
                ((Command) parent.getUserObject()).setCommandsWithSemis(value);
            }
        }
    }

    /**
	 * Description of the Method
	 */
    private void loadHistory() {
        history = new Vector<FtpServer>(50, 50);
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdir();
            }
            if (new File(historyFile).exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(historyFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    history.addElement(new FtpServer(line, true));
                }
            }
        } catch (IOException e) {
        }
    }

    /**
	 * Description of the Method
	 */
    private void loadPrioList() {
        prioList = new Vector<String>(20, 10);
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdir();
            }
            if (new File(prioListFile).exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(prioListFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    prioList.addElement(line);
                }
            }
        } catch (IOException e) {
        }
    }

    /**
	 * Description of the Method
	 *
	 * @param file Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    public Vector<Transfer> loadQueue(String file) {
        Vector<Transfer> v = new Vector<Transfer>(150, 150);
        try {
            if (new File(qdir + File.separator + file).exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(qdir + File.separator + file));
                String line;
                MyFile from;
                MyFile to;
                FtpServer fs;
                FtpServer ts;
                while ((line = reader.readLine()) != null) {
                    fs = null;
                    ts = null;
                    String[] t = Utilities.split(line, "\t\t");
                    from = new MyFile();
                    from.load(t[0]);
                    to = new MyFile();
                    to.load(t[1]);
                    if (!t[5].equals(" ")) {
                        fs = new FtpServer(t[5], true);
                    }
                    if (!t[6].equals(" ")) {
                        ts = new FtpServer(t[6], true);
                    }
                    v.addElement(new Transfer(from, to, Integer.parseInt(t[2]), Integer.parseInt(t[3]), Integer.parseInt(t[4]), fs, ts));
                }
            }
        } catch (IOException e) {
        }
        return v;
    }

    /**
	 * Description of the Method
	 */
    private void loadSites() {
        Vector<Site> sites = new Vector<Site>(50, 30);
        sitesRoot = new DefaultMutableTreeNode(new Site("sites"));
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdir();
            }
            if (new File(sitesFile).exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(sitesFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    sites.addElement(new Site(line, true));
                }
                for (int i = 0; i < sites.size(); i++) sitesRoot.add(new DefaultMutableTreeNode(sites.elementAt(i)));
                new File(sitesFile).delete();
            } else if (new File(sites2File).exists()) {
                InputStream is = null;
                Properties props = new Properties();
                try {
                    is = new BufferedInputStream(new FileInputStream(sites2File));
                } catch (FileNotFoundException e) {
                    return;
                }
                try {
                    props.load(is);
                } catch (IllegalArgumentException e) {
                } catch (IOException e) {
                }
                Vector<DefaultMutableTreeNode> cmds = new Vector<DefaultMutableTreeNode>(100);
                cmds.addElement(sitesRoot);
                DefaultMutableTreeNode parent;
                String value;
                String[] t;
                while (!cmds.isEmpty()) {
                    parent = cmds.firstElement();
                    cmds.removeElementAt(0);
                    value = (String) props.getProperty(parent.toString());
                    if ((value != null) && value.startsWith("{")) {
                        value = value.substring(1, value.length() - 1);
                        t = Utilities.split(value, ";");
                        for (int i = 0; i < t.length; i++) {
                            DefaultMutableTreeNode child = new DefaultMutableTreeNode(new Site(t[i]));
                            parent.add(child);
                            cmds.addElement(child);
                        }
                    } else if (value != null) {
                        parent.setUserObject(new Site(value, true));
                        parent.setAllowsChildren(false);
                    }
                }
            }
        } catch (IOException e) {
        }
    }

    /**
	 * load the skiplist
	 */
    private void loadSkiplist() {
        skiplist = new Vector<String>(20, 10);
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdir();
            }
            if (new File(skiplistFile).exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(skiplistFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    skiplist.addElement(line);
                }
            }
        } catch (IOException e) {
        }
    }

    /**
	 * this method writes the history to a file
	 */
    public void writeHistory() {
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile));
            for (int i = 0; i < history.size(); i++) {
                writer.write(history.elementAt(i).toStringLong());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
        }
    }

    /**
	 * this method writes the skiplist entries to a file
	 */
    public void writePrioList() {
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(prioListFile));
            for (int i = 0; i < prioList.size(); i++) {
                writer.write(prioList.elementAt(i));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
        }
    }

    /**
	 * Description of the Method
	 */
    public void writeQueue() {
        if (frame.getQueueList().isEmpty()) {
            return;
        }
        try {
            if (!new File(qdir).isDirectory()) {
                new File(qdir).mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(qdir + File.separator + (System.currentTimeMillis() / 1000)));
            int i = 0;
            while (i < frame.getQueueList().vfiles().size()) {
                writer.write(((Transfer) frame.getQueueList().elementAt(i++)).toStringLong());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
        }
    }

    /**
	 * DOCUMENT ME!
	 */
    public void writeSites() {
        Properties props = new Properties();
        Vector<TreeNode> cmds = new Vector<TreeNode>();
        cmds.addElement(sitesRoot);
        DefaultMutableTreeNode node;
        StringBuffer buf = new StringBuffer(100);
        while (!cmds.isEmpty()) {
            node = (DefaultMutableTreeNode) cmds.firstElement();
            cmds.removeElementAt(0);
            if (node.getAllowsChildren()) {
                buf.delete(0, buf.length());
                buf.append("{");
                for (int i = 0; i < node.getChildCount(); i++) {
                    buf.append(node.getChildAt(i).toString() + ";");
                    cmds.addElement(node.getChildAt(i));
                }
                buf.append("}");
                props.put(node.toString(), buf.toString());
            } else {
                props.put(node.toString(), ((Site) node.getUserObject()).toStringLong());
            }
        }
        try {
            props.store(new FileOutputStream(sites2File), "");
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }

    /**
	 * this method writes the skiplist entries to a file
	 */
    public void writeSkiplist() {
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(skiplistFile));
            for (int i = 0; i < skiplist.size(); i++) {
                writer.write(skiplist.elementAt(i));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
        }
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getAbort() {
        return abort;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getAutoreconnect() {
        return autoreconnect;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getAutoresume() {
        return autoresume;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getCache() {
        return cache;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public DefaultMutableTreeNode getCmdRoot() {
        return cmdRoot;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String[] getFileExist() {
        return fileExist;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public Vector<FtpServer> getHistory() {
        return history;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public int getIdentdPort() {
        return identdPort;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getIdentUser() {
        return identUser;
    }

    /**
	 * Gets the idleAction attribute of the MainFrame object
	 *
	 * @return The idleAction value
	 */
    public String getIdleAction() {
        return idleAction;
    }

    /**
	 * Gets the noopTime attribute of the MainFrame object
	 *
	 * @return The noopTime value
	 */
    public int getNoopTime() {
        return noopTime;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getPassive() {
        return passive;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public Vector<String> getPrioList() {
        return prioList;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getProxyHost() {
        return proxyHost;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getProxyPasswd() {
        return proxyPasswd;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getProxyPort() {
        return proxyPort;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getProxyType() {
        return proxyType;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getProxyUser() {
        return proxyUser;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public int getRetrycount() {
        return retrycount;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public int getRetrydelay() {
        return retrydelay;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getShowHidden() {
        return showHidden;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public DefaultMutableTreeNode getSitesRoot() {
        return sitesRoot;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean getSkipEmptyDir() {
        return skipEmptyDir;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean getSkipEmptyFile() {
        return skipEmptyFile;
    }

    /**
	 * Gets the skiplist attribute of the MainFrame object
	 *
	 * @return The skiplist value
	 */
    public Vector<String> getSkiplist() {
        return skiplist;
    }

    /**
	 * Gets the timeout attribute of the MainFrame object
	 *
	 * @return The timeout value
	 */
    public int getTimeout() {
        return timeout;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public boolean getUseSkiplist() {
        return useSkiplist;
    }

    /**
	 * import prefs from xml file
	 */
    private void importPrefs() {
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(settings + File.separator + "prefs.xml"));
        } catch (FileNotFoundException e) {
        }
        try {
            Preferences.importPreferences(is);
        } catch (InvalidPreferencesFormatException e) {
        } catch (IOException e) {
        }
        prefs = Preferences.userNodeForPackage(MainFrame.class);
        autoresume = prefs.getBoolean("autoresume", false);
        autoreconnect = prefs.getBoolean("autoreconnect", false);
        passive = prefs.getBoolean("passive", true);
        showHidden = prefs.getBoolean("showHidden", false);
        retrycount = prefs.getInt("retrycount", 10);
        retrydelay = prefs.getInt("retrydelay", 60);
        cache = prefs.getBoolean("cache", true);
        timeout = prefs.getInt("timeout", 20);
        locale = prefs.get("locale", "ISO-8859-15");
        proxyType = prefs.get("proxyType", "none");
        proxyHost = prefs.get("proxyHost", "");
        proxyUser = prefs.get("proxyUser", "");
        proxyPort = prefs.get("proxyPort", "");
        proxyPasswd = prefs.get("proxyPasswd", "");
        useSkiplist = prefs.getBoolean("skiplist", false);
        noop = prefs.getBoolean("noop", false);
        noopTime = prefs.getInt("noopTime", 120);
        idleAction = prefs.get("idleAction", "NOOP");
        skipEmptyFile = prefs.getBoolean("skipEmptyFile", false);
        skipEmptyDir = prefs.getBoolean("skipEmptyDir", false);
        plaf = prefs.get("plaf", "");
        for (int i = 0; i < 9; i++) fileExist[i] = prefs.get("fileExist" + i, "ask");
        isIdentd = prefs.getBoolean("isIdentd", true);
        isIdentUser = prefs.getBoolean("isIdentUser", false);
        identUser = prefs.get("identUser", "user");
        identdPort = prefs.getInt("identdPort", 27280);
    }

    /**
	 * export prefs to xml file
	 */
    public void exportPrefs() {
        prefs = Preferences.userNodeForPackage(MainFrame.class);
        prefs.putBoolean("autoresume", autoresume);
        prefs.putBoolean("autoreconnect", autoreconnect);
        prefs.putBoolean("passive", passive);
        prefs.putBoolean("showHidden", showHidden);
        prefs.putInt("retrycount", retrycount);
        prefs.putInt("retrydelay", retrydelay);
        if (frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
            prefs.putInt("xposition", frame.getX());
            prefs.putInt("yposition", frame.getY());
            prefs.putInt("width", frame.getWidth());
            prefs.putInt("height", frame.getHeight());
        }
        prefs.putInt("dividerloc", frame.getSplitter().getDividerLocation());
        prefs.putInt("extstate", frame.getExtendedState());
        prefs.putBoolean("cache", cache);
        prefs.putInt("timeout", timeout);
        prefs.put("locale", locale);
        prefs.put("proxyType", proxyType);
        prefs.put("proxyHost", proxyHost);
        prefs.put("proxyUser", proxyUser);
        prefs.put("proxyPort", proxyPort);
        prefs.put("proxyPasswd", proxyPasswd);
        prefs.putBoolean("skiplist", useSkiplist);
        prefs.putBoolean("noop", noop);
        prefs.putInt("noopTime", noopTime);
        prefs.put("idleAction", idleAction);
        prefs.putBoolean("skipEmptyFile", skipEmptyFile);
        prefs.putBoolean("skipEmptyDir", skipEmptyDir);
        prefs.put("plaf", plaf);
        for (int i = 0; i < 9; i++) prefs.put("fileExist" + i, fileExist[i]);
        prefs.putBoolean("isIdentd", isIdentd);
        prefs.putBoolean("isIdentUser", isIdentUser);
        prefs.get("identUser", identUser);
        prefs.putInt("identdPort", identdPort);
        try {
            prefs.exportNode(new FileOutputStream(settings + File.separator + "prefs.xml"));
        } catch (IOException e) {
        } catch (BackingStoreException e) {
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isIdentUser() {
        return isIdentUser;
    }

    /**
	 * Gets the nOOP attribute of the MainFrame object
	 *
	 * @return The nOOP value
	 */
    public boolean isNOOP() {
        return noop;
    }

    /**
	 * Gets the proxy attribute of the MainFrame object
	 *
	 * @return The proxy value
	 */
    public boolean isProxy() {
        if (proxyType.equals("none")) {
            return false;
        }
        return true;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public String locale() {
        return locale;
    }

    /**
	 * save the commands menu
	 */
    public void saveCommands() {
        Properties props = new Properties();
        Vector<TreeNode> cmds = new Vector<TreeNode>();
        cmds.addElement(cmdRoot);
        DefaultMutableTreeNode node;
        StringBuffer buf = new StringBuffer(100);
        while (!cmds.isEmpty()) {
            node = (DefaultMutableTreeNode) cmds.firstElement();
            cmds.removeElementAt(0);
            if (node.getChildCount() > 0) {
                buf.delete(0, buf.length());
                buf.append("{");
                for (int i = 0; i < node.getChildCount(); i++) {
                    buf.append(node.getChildAt(i).toString() + ";");
                    cmds.addElement(node.getChildAt(i));
                }
                buf.append("}");
                props.put(node.toString(), buf.toString());
            } else {
                props.put(node.toString(), ((Command) node.getUserObject()).getCommandsWithSemis());
            }
        }
        try {
            props.store(new FileOutputStream(settings + File.separator + "cmds.properties"), "");
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }

    /**
	 * Sets the abort attribute of the MainFrame object
	 *
	 * @param b The new abort value
	 */
    public void setAbort(boolean b) {
        abort = b;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param autoreconnect DOCUMENT ME!
	 */
    public void setAutoreconnect(boolean autoreconnect) {
        this.autoreconnect = autoreconnect;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param autoresume DOCUMENT ME!
	 */
    public void setAutoresume(boolean autoresume) {
        this.autoresume = autoresume;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param cache DOCUMENT ME!
	 */
    public void setCache(boolean cache) {
        this.cache = cache;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param cmdRoot DOCUMENT ME!
	 */
    public void setCmdRoot(DefaultMutableTreeNode cmdRoot) {
        this.cmdRoot = cmdRoot;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param isIdentd DOCUMENT ME!
	 */
    public void setIdentd(boolean isIdentd) {
        this.isIdentd = isIdentd;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param text DOCUMENT ME!
	 */
    public void setIdentdPort(String text) {
        identdPort = Utilities.parseInt(text);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param isIdentUser DOCUMENT ME!
	 */
    public void setIdentUser(boolean isIdentUser) {
        this.isIdentUser = isIdentUser;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param identUser DOCUMENT ME!
	 */
    public void setIdentUser(String identUser) {
        this.identUser = identUser;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param idleAction DOCUMENT ME!
	 */
    public void setIdleAction(String idleAction) {
        this.idleAction = idleAction;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param locale DOCUMENT ME!
	 */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param noop DOCUMENT ME!
	 */
    public void setNoop(boolean noop) {
        this.noop = noop;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param noopTime DOCUMENT ME!
	 */
    public void setNoopTime(int noopTime) {
        this.noopTime = noopTime;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param passive DOCUMENT ME!
	 */
    public void setPassive(boolean passive) {
        this.passive = passive;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param v DOCUMENT ME!
	 */
    public void setPrioList(Vector<String> v) {
        prioList = v;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param proxyHost DOCUMENT ME!
	 */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param proxyPasswd DOCUMENT ME!
	 */
    public void setProxyPasswd(String proxyPasswd) {
        this.proxyPasswd = proxyPasswd;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param proxyPort DOCUMENT ME!
	 */
    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param proxyType DOCUMENT ME!
	 */
    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param proxyUser DOCUMENT ME!
	 */
    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param retrycount DOCUMENT ME!
	 */
    public void setRetrycount(int retrycount) {
        this.retrycount = retrycount;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param retrydelay DOCUMENT ME!
	 */
    public void setRetrydelay(int retrydelay) {
        this.retrydelay = retrydelay;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param showHidden DOCUMENT ME!
	 */
    public void setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param sitesRoot DOCUMENT ME!
	 */
    public void setSitesRoot(DefaultMutableTreeNode sitesRoot) {
        this.sitesRoot = sitesRoot;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param b DOCUMENT ME!
	 */
    public void setSkipEmptyDir(boolean b) {
        skipEmptyDir = b;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param b DOCUMENT ME!
	 */
    public void setSkipEmptyFile(boolean b) {
        skipEmptyFile = b;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param v DOCUMENT ME!
	 */
    public void setSkiplist(Vector<String> v) {
        skiplist = v;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param timeout DOCUMENT ME!
	 */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param b DOCUMENT ME!
	 */
    public void setUseSkiplist(boolean b) {
        useSkiplist = b;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getPlaf() {
        return plaf;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param plaf DOCUMENT ME!
	 */
    public void setPlaf(String plaf) {
        this.plaf = plaf;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param s DOCUMENT ME!
	 * @param d DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public int getInt(String s, int d) {
        return prefs.getInt(s, d);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public boolean isIdentd() {
        return isIdentd;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getQdir() {
        return qdir;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public String getSettings() {
        return settings;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param settings DOCUMENT ME!
	 */
    public void setSettings(String settings) {
        this.settings = settings;
    }
}
