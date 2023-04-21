package net.sf.camobird.core;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import net.sf.camobird.debugging.DefaultLogger;

/**
 *
 * @author alberto
 */
public class AccountPool {

    private static Map<UUID, Account> pool;

    private AccountPool() {
    }

    static Map<UUID, Account> getPool() {
        DefaultLogger.getInstance().entering("AccountPool", "getPool");
        if (pool == null) {
            DefaultLogger.getInstance().logp(Level.FINE, "AccountPool", "getPool", "Pool Map object has not been initialized. Initializing now.");
            pool = new HashMap<UUID, Account>();
        }
        return pool;
    }

    public static Boolean save() {
        DefaultLogger.getInstance().entering("AccountPool", "save() - not overloaded");
        try {
            save("accounts.dat");
        } catch (FileNotFoundException ex) {
            DefaultLogger.getInstance().logp(Level.WARNING, "AccountPool", "save", "FileNotFoundException when trying to save account pool (maybe incorrect OS permissions?)", ex);
        } catch (IOException ex) {
            DefaultLogger.getInstance().logp(Level.WARNING, "AccountPool", "save", "IOException when trying to save account pool", ex);
        }
        return true;
    }

    public static Boolean save(String filename) throws FileNotFoundException, IOException {
        DefaultLogger.getInstance().logp(Level.INFO, "AccountPool", "save(String filename)", "Attempting to save account pool as " + filename);
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(getPool());
        oos.close();
        return true;
    }

    public static void load() {
        DefaultLogger.getInstance().entering("AccountPool", "load() - not overloaded");
        try {
            load("accounts.dat");
        } catch (FileNotFoundException ex) {
            DefaultLogger.getInstance().logp(Level.WARNING, "AccountPool", "load", "FileNotFoundException when trying to load account pool", ex);
        } catch (IOException ex) {
            DefaultLogger.getInstance().logp(Level.WARNING, "AccountPool", "load", "FileNotFoundException when trying to load account pool", ex);
        } catch (ClassNotFoundException ex) {
            DefaultLogger.getInstance().logp(Level.WARNING, "AccountPool", "load", "ClassNotFoundException when trying to load account pool", ex);
        }
    }

    public static void load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        DefaultLogger.getInstance().logp(Level.INFO, "AccountPool", "load(String filename)", "Trying to load file " + filename);
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        pool = (Map<UUID, Account>) ois.readObject();
        ois.close();
    }
}
