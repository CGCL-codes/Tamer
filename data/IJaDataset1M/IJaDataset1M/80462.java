package ch.unibe.id.se.a3ublogin.persistence.readersandwriters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.unibe.id.se.a3ublogin.persistence.Constants;
import ch.unibe.id.se.a3ublogin.persistence.serializeddbtables.core.QuickPersistenceManagerProperties_v02;

public class PropertieFileHandler {

    private Constants cs = null;

    private Log log = LogFactory.getLog(getClass());

    /** instance of the singleton */
    private static PropertieFileHandler instance = null;

    private HashMap<String, Properties> map = new HashMap<String, Properties>();

    private HashMap<String, Integer> countM = new HashMap<String, Integer>();

    /** returns the singleton */
    public static synchronized PropertieFileHandler getInstance() {
        if (instance == null) instance = new PropertieFileHandler();
        return instance;
    }

    /** privat constructor */
    private PropertieFileHandler() {
        cs = Constants.getInstance();
        instance = this;
        map = (HashMap<String, Properties>) instance.readPropertieMap();
    }

    private Properties getProperties(String applicationIdentifier) {
        Properties ret = map.get(applicationIdentifier);
        if (ret == null) {
            FileInputStream fin = null;
            Properties pro = new Properties();
            try {
                fin = new FileInputStream(cs.getPropertiesFileLocation() + applicationIdentifier.trim() + cs.getPropertiesEnding());
                pro.load(fin);
            } catch (FileNotFoundException e) {
                pro = null;
            } catch (IOException e) {
                pro = null;
            } finally {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (pro != null) {
                map.put(applicationIdentifier, pro);
                countM.put(applicationIdentifier, new Integer(0));
            } else {
                Integer i = countM.get(applicationIdentifier);
                if (i == null) {
                    countM.put(applicationIdentifier, new Integer(1));
                } else {
                    countM.put(applicationIdentifier, new Integer(i.intValue() + 1));
                }
            }
        }
        return ret;
    }

    public boolean isApplicationRegistred(String applicationIdentifier) {
        Properties p = getProperties(applicationIdentifier);
        if (p == null) {
            return false;
        }
        return true;
    }

    public Properties readProperties(String applicationIdentifier) {
        return getProperties(applicationIdentifier);
    }

    public Properties writeProperties(String applicationIdentifier) {
        return null;
    }

    public Map<String, Properties> readPropertieMap() {
        HashMap<String, Properties> result = new HashMap<String, Properties>();
        File f = new File(cs.getPropertiesFileLocation());
        File[] farr = f.listFiles();
        if (farr != null) {
            for (int i = 0; i < farr.length; i++) {
                Properties tempP = new Properties();
                try {
                    if (!farr[i].getName().contains(".DS")) {
                        tempP.load(new FileInputStream(farr[i]));
                    }
                } catch (FileNotFoundException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("PropertieFileHandler: ", e);
                    }
                } catch (IOException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("PropertieFileHandler: ", e);
                    }
                }
                result.put(farr[i].getName().replaceFirst(cs.getPropertiesEnding(), ""), tempP);
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        PropertieFileHandler handler = new PropertieFileHandler();
        QuickPersistenceManagerProperties_v02 p = new QuickPersistenceManagerProperties_v02(false);
        Map<String, Properties> map = handler.readPropertieMap();
        for (String s : map.keySet()) {
            p.writeProperties(s, map.get(s));
        }
    }
}
