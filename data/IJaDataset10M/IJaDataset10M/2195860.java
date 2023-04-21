package net.sourceforge.keepassj2me;

import java.io.IOException;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.TextField;
import net.sourceforge.keepassj2me.L10nKeys.keys;
import net.sourceforge.keepassj2me.datasource.DataSourceAdapter;
import net.sourceforge.keepassj2me.datasource.DataSourceRegistry;
import net.sourceforge.keepassj2me.datasource.SerializeStream;
import net.sourceforge.keepassj2me.datasource.UnserializeStream;
import net.sourceforge.keepassj2me.keydb.KeydbDatabase;
import net.sourceforge.keepassj2me.keydb.KeydbException;
import net.sourceforge.keepassj2me.keydb.KeydbUtil;
import net.sourceforge.keepassj2me.tools.DisplayStack;
import net.sourceforge.keepassj2me.tools.InputBox;
import net.sourceforge.keepassj2me.tools.MessageBox;
import net.sourceforge.keepassj2me.tools.ProgressForm;

/**
 * @author Stepan Strelets
 */
public class KeydbManager {

    protected KeydbDatabase db = null;

    protected DataSourceAdapter dbSource = null;

    protected DataSourceAdapter keySource = null;

    L10n lc;

    KeydbManager() {
        lc = Config.getInstance().getLocale();
    }

    /**
	 * Helper for open and display database
	 * @param last true for open last used source
	 * @throws KeePassException 
	 */
    public static void openAndDisplayDatabase(byte[] last) throws KeePassException {
        try {
            KeydbManager dm = new KeydbManager();
            if (last != null) dm.loadSources(last);
            try {
                dm.openDatabase(last == null);
                if (dm.db == null) return;
                dm.saveSourcesAsRecent();
                dm.displayDatabase();
            } finally {
                dm.closeDatabase();
            }
        } catch (KeydbException e) {
            throw new KeePassException(e.getMessage());
        }
    }

    /**
	 * Try get and unserialize last data sources
	 * @throws KeePassException
	 * @throws KeydbException
	 */
    private void loadSources(byte[] sources) throws KeePassException, KeydbException {
        if (sources != null) {
            UnserializeStream in = new UnserializeStream(sources);
            byte count;
            try {
                count = in.readByte();
            } catch (IOException e) {
                throw new KeePassException(e.getMessage());
            }
            this.setDbSource(DataSourceRegistry.unserializeDataSource(in));
            if (count > 1) this.setKeySource(DataSourceRegistry.unserializeDataSource(in));
        }
        ;
    }

    /**
	 * Try serialize data sources and store as last used 
	 */
    private void saveSourcesAsRecent() {
        try {
            SerializeStream out = new SerializeStream();
            DataSourceAdapter ks = this.getKeySource();
            out.writeByte(ks == null ? 1 : 2);
            this.getDbSource().serialize(out);
            if (ks != null) ks.serialize(out);
            Config.getInstance().setLastOpened(out.getBytes());
            ks = null;
            out = null;
        } catch (IOException e) {
        }
    }

    /**
	 * Create new database and display
	 * @throws KeePassException
	 * @throws KeydbException
	 */
    public static void createAndDisplayDatabase() throws KeePassException, KeydbException {
        KeydbManager dm = new KeydbManager();
        try {
            dm.createDatabase();
            if (dm.db != null) dm.displayDatabase();
        } finally {
            dm.closeDatabase();
        }
    }

    /**
	 * Get current database
	 * @return database
	 */
    public KeydbDatabase getDB() {
        return db;
    }

    /**
	 * Set KDB data source
	 * @param ds
	 */
    public void setDbSource(DataSourceAdapter ds) {
        dbSource = ds;
    }

    /**
	 * Set key file data source
	 * @param ks
	 */
    public void setKeySource(DataSourceAdapter ks) {
        keySource = ks;
    }

    /**
	 * Get KDB data source
	 * @return data source
	 */
    public DataSourceAdapter getDbSource() {
        return dbSource;
    }

    /**
	 * Get key file data source
	 * @return data source
	 */
    public DataSourceAdapter getKeySource() {
        return keySource;
    }

    private void openDatabase(boolean ask) throws KeydbException, KeePassException {
        if (ask) {
            while (true) {
                dbSource = DataSourceRegistry.selectSource(lc.getString(keys.KDB_FILE), false, false);
                if (dbSource.selectLoad(lc.getString(keys.KDB_FILE))) break;
            }
            ;
        }
        ;
        byte[] kdbBytes = dbSource.load();
        if (kdbBytes == null) throw new KeePassException(lc.getString(keys.FILE_OPEN_ERROR));
        InputBox pwb = new InputBox(lc.getString(keys.ENTER_DB_PASS), null, 64, TextField.PASSWORD);
        if (pwb.getResult() != null) {
            try {
                byte[] keyfile = null;
                if (ask) {
                    while (true) {
                        keySource = DataSourceRegistry.selectSource(lc.getString(keys.KEY_FILE), true, false);
                        if ((keySource == null) || keySource.selectLoad(lc.getString(keys.KEY_FILE))) {
                            break;
                        }
                        ;
                    }
                    ;
                }
                if (keySource != null) {
                    keyfile = KeydbUtil.hash(keySource.getInputStream(), -1);
                }
                db = new KeydbDatabase();
                try {
                    ProgressForm form = new ProgressForm(true);
                    db.setProgressListener(form);
                    DisplayStack.getInstance().push(form);
                    try {
                        db.open(kdbBytes, pwb.getResult(), keyfile);
                    } finally {
                        DisplayStack.getInstance().pop();
                    }
                    ;
                } catch (Exception e) {
                    db.close();
                    throw e;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new KeePassException(e.getMessage());
            }
        }
        ;
    }

    /**
	 * Save database to data source
	 * @param ask if true - select data source (save as ...)
	 * @throws KeydbException
	 * @throws KeePassException
	 */
    public void saveDatabase(boolean ask) throws KeydbException, KeePassException {
        if (ask) {
            DataSourceAdapter source;
            try {
                while (true) {
                    source = DataSourceRegistry.selectSource(lc.getString(keys.KDB_FILE), false, true);
                    if (source.selectSave(lc.getString(keys.KDB_FILE), dbSource == null ? ".kdb" : dbSource.getName())) break;
                }
            } catch (KeePassException e) {
                return;
            }
            source.save(db.getEncoded());
            dbSource = source;
            this.saveSourcesAsRecent();
        } else {
            this.dbSource.save(db.getEncoded());
        }
        this.db.resetChangeIndicator();
    }

    /**
	 * Close database
	 */
    public void closeDatabase() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    /**
	 * Create new empty database
	 * @throws KeydbException
	 * @throws KeePassException
	 */
    public void createDatabase() throws KeydbException, KeePassException {
        InputBox pwb;
        do {
            pwb = new InputBox(lc.getString(keys.ENTER_DB_PASS), null, 64, TextField.PASSWORD);
            if (pwb.getResult() == null) return;
            InputBox pwb2 = new InputBox(lc.getString(keys.REPEAT_PASS), null, 64, TextField.PASSWORD);
            if (pwb2.getResult() == null) return;
            if (pwb.getResult().equals(pwb2.getResult())) break; else MessageBox.showAlert(lc.getString(keys.PASS_MISMATCH));
        } while (true);
        try {
            byte[] keyfile = null;
            while (true) {
                keySource = DataSourceRegistry.selectSource(lc.getString(keys.KEY_FILE), true, false);
                if (keySource != null) {
                    if (keySource.selectLoad(lc.getString(keys.KEY_FILE))) {
                        keyfile = KeydbUtil.hash(keySource.getInputStream(), -1);
                        break;
                    }
                    ;
                } else {
                    break;
                }
            }
            ;
            int rounds = Config.getInstance().getEncryptionRounds();
            InputBox ib = new InputBox(lc.getString(keys.ENCRYPTION_ROUNDS), Integer.toString(rounds), 10, TextField.NUMERIC);
            String tmp = ib.getResult();
            if (tmp != null) {
                try {
                    rounds = Integer.parseInt(tmp);
                } catch (NumberFormatException e) {
                }
                ;
            }
            db = new KeydbDatabase();
            try {
                ProgressForm form = new ProgressForm(true);
                db.setProgressListener(form);
                DisplayStack.getInstance().push(form);
                try {
                    db.create(pwb.getResult(), keyfile, rounds);
                } finally {
                    DisplayStack.getInstance().pop();
                }
                ;
            } catch (Exception e) {
                db.close();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new KeePassException(e.getMessage());
        }
    }

    /**
	 * Change password, key file, encryption rounds for database
	 * @throws KeydbException
	 * @throws KeePassException
	 */
    public void changeMasterKeyDatabase() throws KeydbException, KeePassException {
        InputBox pwb;
        do {
            pwb = new InputBox(lc.getString(keys.ENTER_DB_PASS), null, 64, TextField.PASSWORD);
            if (pwb.getResult() == null) return;
            InputBox pwb2 = new InputBox(lc.getString(keys.REPEAT_PASS), null, 64, TextField.PASSWORD);
            if (pwb2.getResult() == null) return;
            if (pwb.getResult().equals(pwb2.getResult())) break; else MessageBox.showAlert(lc.getString(keys.PASS_MISMATCH));
        } while (true);
        try {
            byte[] keyfile = null;
            DataSourceAdapter source = null;
            while (true) {
                if (db.isLocked()) return;
                source = DataSourceRegistry.selectSource(lc.getString(keys.KEY_FILE), true, false);
                if (source != null) {
                    if (source.selectLoad(lc.getString(keys.KEY_FILE))) {
                        keyfile = KeydbUtil.hash(source.getInputStream(), -1);
                        break;
                    }
                    ;
                } else {
                    break;
                }
            }
            ;
            int rounds = db.getHeader().getEncryptionRounds();
            InputBox ib = new InputBox(lc.getString(keys.ENCRYPTION_ROUNDS), Integer.toString(rounds), 10, TextField.NUMERIC);
            String tmp = ib.getResult();
            if (tmp != null) {
                try {
                    rounds = Integer.parseInt(tmp);
                } catch (NumberFormatException e) {
                }
                ;
            }
            try {
                ProgressForm form = new ProgressForm(true);
                db.setProgressListener(form);
                DisplayStack.getInstance().push(form);
                try {
                    db.changeMasterKey(pwb.getResult(), keyfile, rounds);
                    if (keySource != source) {
                        keySource = source;
                        saveSourcesAsRecent();
                    }
                    ;
                } finally {
                    DisplayStack.getInstance().pop();
                }
                ;
            } catch (Exception e) {
                db.close();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new KeePassException(e.getMessage());
        }
    }

    /**
	 * Display and handle KDB menu
	 */
    public void displayDatabase() {
        int menuitem = KeydbMenu.RESULT_BROWSE;
        do {
            KeydbBrowser br = null;
            String title = (this.db.isChanged() ? "*" : "") + (this.dbSource != null ? this.dbSource.getCaption() : "untitled");
            KeydbMenu menu = new KeydbMenu(title, (this.dbSource != null) && this.dbSource.canSave(), menuitem, this.db.isLocked());
            menu.displayAndWait();
            menuitem = menu.getResult();
            menu = null;
            try {
                try {
                    this.db.reassureWatchDog();
                } catch (Exception e) {
                }
                ;
                switch(menuitem) {
                    case KeydbMenu.RESULT_CLOSE:
                        if (!this.db.isChanged() || MessageBox.showConfirm(lc.getString(keys.DISCARD_AND_CLOSE_DB_Q))) return;
                        break;
                    case KeydbMenu.RESULT_BROWSE:
                        br = new KeydbBrowser(this.db);
                        br.display(KeydbBrowser.MODE_BROWSE);
                        break;
                    case KeydbMenu.RESULT_SEARCH:
                        br = new KeydbBrowser(this.db);
                        br.display(KeydbBrowser.MODE_SEARCH);
                        break;
                    case KeydbMenu.RESULT_SAVE:
                        this.saveDatabase(false);
                        break;
                    case KeydbMenu.RESULT_SAVEAS:
                        this.saveDatabase(true);
                        break;
                    case KeydbMenu.RESULT_INFORMATION:
                        MessageBox box = new MessageBox(title, lc.getString(keys.ENTRIES_COUNT) + ": " + db.getHeader().getEntriesCount() + "\r\n" + lc.getString(keys.GROUPS_COUNT) + ": " + db.getHeader().getGroupsCount() + "\r\n" + lc.getString(keys.SIZE) + ": " + KeydbUtil.toPrettySize(db.getSize()) + "\r\n" + lc.getString(keys.ENCRYPTION_ROUNDS) + ": " + db.getHeader().getEncryptionRounds() + "\r\n\r\n" + lc.getString(keys.INF_HARDWARE, KeydbUtil.getHWInfo()), AlertType.INFO, false, Icons.getInstance().getImageById(Icons.ICON_INFO));
                        box.displayAndWait();
                        break;
                    case KeydbMenu.RESULT_CHANGE_MASTER_KEY:
                        this.changeMasterKeyDatabase();
                        break;
                    case KeydbMenu.RESULT_UNLOCK:
                        this.unlockDB();
                        break;
                    default:
                }
            } catch (Exception e) {
                MessageBox.showAlert(e.getMessage());
            }
            ;
        } while (true);
    }

    private void unlockDB() {
        while (this.db.isLocked()) {
            InputBox pwb = new InputBox(lc.getString(keys.ENTER_DB_PASS), null, 64, TextField.PASSWORD);
            if (pwb.getResult() != null) {
                try {
                    byte[] keyfile = null;
                    if (keySource != null) {
                        keyfile = KeydbUtil.hash(keySource.getInputStream(), -1);
                    }
                    ProgressForm form = new ProgressForm(true);
                    db.setProgressListener(form);
                    DisplayStack.getInstance().push(form);
                    try {
                        db.unlock(pwb.getResult(), keyfile);
                    } finally {
                        DisplayStack.getInstance().pop();
                    }
                    ;
                } catch (Exception e) {
                    MessageBox.showAlert(e.getMessage());
                }
            } else {
                break;
            }
            ;
        }
        ;
    }
}
