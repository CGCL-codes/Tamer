package esferacore.configuration;

import esferacore.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author neo
 */
public class CacheManager extends Properties {

    private static CacheManager cacheMan = new CacheManager();

    private CacheManager() {
        String cacheName = EsferaConfiguration.getEsferaConfiguration().getUserName();
        File f = new File("cache/" + cacheName + ".cache");
        if (!f.exists()) {
            try {
                FileOutputStream fos = new FileOutputStream(f);
                try {
                    fos.write("#This is a cache file".getBytes());
                } catch (IOException ex) {
                    Logger.getLogger(CacheManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CacheManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            load(new FileInputStream(f));
        } catch (IOException ex) {
            Logger.getLogger(CacheManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIPByPeerName(String pName) {
        return getProperty(pName).split(" ")[0];
    }

    public String getPortByPeerName(String pName) {
        return getProperty(pName).split(" ")[1];
    }

    public String getPeerNameByIP(String ip) {
        String pName = null;
        String pNameAux;
        Enumeration en = this.keys();
        while (en.hasMoreElements()) {
            pNameAux = (String) en.nextElement();
            if (getProperty(pNameAux).split(" ")[0].equals(ip)) {
                pName = pNameAux;
            }
        }
        return pName;
    }

    public static CacheManager getCacheManager() {
        return cacheMan;
    }
}
