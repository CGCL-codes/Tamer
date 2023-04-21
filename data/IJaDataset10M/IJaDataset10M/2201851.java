package storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Grava as credenciais dos usuários em arquivo
 */
public class Configuration {

    private File configDir;

    private String pathSeparator;

    public static final String ACCOUNT_FILE_PREFIX = "account-";

    public Configuration() {
        String userHome = System.getProperty("user.home");
        pathSeparator = System.getProperty("file.separator");
        if (userHome == null) {
            throw new IllegalStateException("user.home==null");
        }
        File home = new File(userHome);
        configDir = new File(home, ".jaytter");
        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                throw new IllegalStateException(configDir.toString());
            }
        }
    }

    public ArrayList<Properties> getStoredAccounts() {
        ArrayList<Properties> arr = new ArrayList();
        String[] list = configDir.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.startsWith(ACCOUNT_FILE_PREFIX);
            }
        });
        try {
            for (int i = 0; i < list.length; i++) {
                Properties p = new Properties();
                p.loadFromXML(new FileInputStream(configDir.getAbsolutePath() + pathSeparator + list[i]));
                arr.add(p);
            }
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arr;
    }

    public boolean storeAccount(String accountName, Properties values) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(configDir + pathSeparator + ACCOUNT_FILE_PREFIX + accountName + ".xml");
            values.storeToXML(os, "", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public Properties getAccount(String name) {
        Properties p = new Properties();
        try {
            p.loadFromXML(new FileInputStream(configDir.getAbsolutePath() + pathSeparator + ACCOUNT_FILE_PREFIX + name));
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }
}
