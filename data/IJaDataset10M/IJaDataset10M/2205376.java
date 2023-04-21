package net.sf.passwordpurse.control;

import net.sf.passwordpurse.managers.PocketManager;
import net.sf.passwordpurse.managers.PurseManager;
import java.io.Writer;
import net.sf.passwordpurse.structural.Purse;
import net.sf.passwordpurse.structural.Pocket;
import net.sf.passwordpurse.crypto.CryptoManager;
import net.sf.passwordpurse.core.SelfDefinedField;
import net.sf.passwordpurse.core.Utf8String;
import net.sf.passwordpurse.core.LengthDefinedFieldParser;
import net.sf.passwordpurse.core.PurseStructuralException;

public abstract class PurseController implements WatchdogTimeoutHandler {

    protected static final int MAX_PURSE_SIZE_KB = 20;

    protected static final int MAX_PURSE_SIZE = MAX_PURSE_SIZE_KB * 1024;

    protected String filename;

    private byte[] rawPurse;

    private boolean isLocated;

    private boolean fileExists;

    private boolean isLoaded;

    private boolean isChanged;

    private boolean isOpen;

    private boolean hasTimedOut;

    private final Watchdog watchdog;

    private PurseManager purseManager;

    private CryptoManager cryptoManager;

    private Pocket[] lastPocketList;

    protected Writer errorWriter;

    /**
 * PurseController constructor
 */
    public PurseController(Writer errorWriter) {
        if (errorWriter == null) throw new NullPointerException("errorWriter is null!");
        this.errorWriter = errorWriter;
        this.watchdog = new Watchdog(this);
        this.watchdog.start();
    }

    /**
 * locate the file holding the purse and prepare to read (and write) it.
 * The file can be anywhere in the filesystem, identified by a relative
 * or absolute path, but we require the rights to read, write and, if
 * necessary, create it.
 *
 * @return boolean - true:  the purse file exists already
 *                 - false: the purse file needs to be created
 *
 * Note: the filename is saved so that close can write the contents
 */
    public boolean locate(String filename) {
        if (filename == null) throw new IllegalArgumentException("filename of Encrypted Password Purse must not be null");
        if (filename.length() < 1) throw new IllegalArgumentException("filename of Encrypted Password Purse must not be empty");
        this.filename = filename;
        this.fileExists = locate();
        this.isLocated = true;
        return this.fileExists;
    }

    /**
 * private method to establish the status of a purse file with the
 * name stored in this instance- (probably by locate( filename )
 *
 * @return boolean - true:  the purse file exists already
 *                 - false: the purse file needs to be created
 */
    protected abstract boolean locate();

    /**
 * abstract method to write the changed contents of purse file
 */
    protected abstract void writePurse(byte[] rawPurse);

    /**
 * load the contents of the EncryptedPurse from the file as raw bytes
 * (because they will be in an encrypted form). The file is closed
 * immediately after loading in case it is never updated.
 *
 * Note: if the file could not be found by locate, do NOT call load...
 *       an empty Purse will be created during open (when the
 *       passphrase is known)
 */
    protected abstract void load() throws PurseStructuralException;

    /**
 * load the raw bytes as a proposed EncryptedPurse
 */
    public void load(byte[] proposal, int offset, int count) throws PurseStructuralException {
        if (!isLocated()) throw new IllegalStateException("load forbidden - purse not yet located");
        if (proposal == null) throw new PurseStructuralException("Raw EncryptedPurse cannot be null!");
        int size = proposal.length;
        int remainder = size - offset;
        if (count < Purse.PURSE_MINIMUM_LENGTH) {
            throw new PurseStructuralException("Raw Encrypted Password Purse is smaller " + "than minimum size of " + Purse.MINIMUM_LENGTH);
        } else if (count >= MAX_PURSE_SIZE) {
            throw new IllegalArgumentException("Raw Encrypted Password Purse is larger " + "than maximum size of " + MAX_PURSE_SIZE);
        }
        if (remainder < count) {
            throw new PurseStructuralException("Raw Encrypted Password Purse is too small " + "for the claimed size of " + count);
        }
        this.rawPurse = new byte[count];
        System.arraycopy(proposal, offset, this.rawPurse, 0, count);
        this.isLoaded = true;
    }

    /**
 * instantiate a PurseManager, which will validate the proposed
 * passPhrase and then decrypt the contents (or reject them if invalid).
 */
    public boolean open(CryptoManager cryptoMgr) throws PurseStructuralException {
        if (this.isOpen) throw new IllegalStateException("open forbidden - purse already open");
        if (cryptoMgr == null) throw new NullPointerException("CryptoManager is null!");
        if (!cryptoMgr.canDecrypt()) throw new IllegalArgumentException("passPhrase is not known!");
        this.cryptoManager = cryptoMgr;
        LengthDefinedFieldParser parser = new LengthDefinedFieldParser();
        if (!this.isLoaded) {
            if (this.fileExists) throw new IllegalStateException("open forbidden - purse exists but not loaded yet!");
            byte[] emptyRawBytes = PurseManager.makeEmptyPurseRaw(this.cryptoManager);
            load(emptyRawBytes, 0, emptyRawBytes.length);
        }
        this.purseManager = new PurseManager(this.rawPurse, parser);
        this.isOpen = this.purseManager.open(this.cryptoManager);
        this.watchdog.watch();
        return this.isOpen;
    }

    /**
 * the watchdog will use its default timeout unless a new value is
 * set. This can be done before or after the purse is opened.
 */
    public void setIdleTimeout(int secs) {
        this.watchdog.setTimeSecs(secs);
    }

    /**
 * error logging - can't use PrintWriter and println in midlets
 */
    public abstract void logWarning(String s);

    /**
 * close the object and remove it from further use
 */
    public void close() {
        this.watchdog.kill();
        if (!this.isOpen) return;
        checkpoint();
        trashPurse();
    }

    /**
 * remove the Purse from further use, discarding any pending changes
 */
    private void trashPurse() {
        if (!this.isOpen) return;
        this.purseManager.close();
        this.purseManager = null;
        this.lastPocketList = null;
        this.isOpen = false;
        this.rawPurse = null;
        this.fileExists = false;
        this.filename = null;
        this.isLocated = false;
        this.isLoaded = false;
    }

    /**
 * write any pending changes to the external Purse file
 */
    public void checkpoint() {
        if (!this.isOpen) return;
        if (!this.isChanged) return;
        if (this.filename == null) {
            this.isChanged = false;
            return;
        }
        locate();
        try {
            writePurse(this.purseManager.getRawBytes());
        } finally {
            this.isChanged = false;
        }
    }

    /**
 * handle a timeout while running in the Watchdog Thread.
 * danger! the Watchdog lock will be held through this call!
 *
 * our external file handlers are not supposed to keep the purse file open,
 * so we can simply mark it as closed and wait for it to be noticed.
 */
    public void handleTimeout() {
        if (!this.isOpen) return;
        logWarning("Purse idle timeout after " + this.watchdog.getTimeoutMins() + " minutes and " + this.watchdog.getTimeoutSpareSecs() + " seconds");
        if (this.isChanged) {
            logWarning("The Purse has changed, but will not be saved!");
        }
        logWarning("The Purse will be closed immediately!");
        trashPurse();
        this.hasTimedOut = true;
    }

    /**
 * reset the Watchdog so it will start a new timeout interval
 */
    public void restartTimeout() {
        if (!this.isOpen) return;
        this.watchdog.watch();
    }

    /**
 * does the purse file exist on external storage?
 * @return boolean
 */
    public boolean fileExists() {
        return this.fileExists;
    }

    /**
 * have we looked for the purse on external storage yet?
 * @return boolean
 */
    public boolean isLocated() {
        return this.isLocated;
    }

    /**
 * have the contents been loaded from external storage?
 * @return boolean
 */
    public boolean isLoaded() {
        return this.isLoaded;
    }

    /**
 * is the object open now and available for use?
 * @return boolean
 */
    public boolean isOpen() {
        return this.isOpen;
    }

    /**
 * has the purse been modified and not yet saved?
 * @return boolean
 */
    public boolean isDirty() {
        return this.isChanged;
    }

    /**
 * is the purse still accessible, i.e. not timed out and trashed?
 * @return boolean
 */
    public boolean isAvailable() {
        return (!this.hasTimedOut);
    }

    /**
 * trivial gatekeeper method
 */
    private void mustBeOpen() {
        if (!isOpen()) throw new IllegalStateException("access is forbidden when closed!");
    }

    /**
 * the object must be open, then return the number of Pockets
 * within the unencrypted Purse
 *
 * @return int
 */
    public int size() {
        mustBeOpen();
        return this.purseManager.getPocketCount();
    }

    /**
 * assign the new pass phrase to the open Purse file, including the
 * individual Pockets, and checkpoint the Purse to external storage
 */
    public void setNewPassphrase(CryptoManager cryptoMgr) throws PurseStructuralException {
        mustBeOpen();
        if (cryptoMgr == null) throw new NullPointerException("CryptoManager is null!");
        if (!cryptoMgr.canDecrypt()) throw new IllegalArgumentException("passPhrase is not known!");
        try {
            this.purseManager.setNewPassphrase(cryptoMgr);
            this.cryptoManager = cryptoMgr;
            this.isChanged = true;
            checkpoint();
        } catch (IllegalArgumentException iae) {
            logWarning("new passphrase " + cryptoMgr + " is not valid! " + iae.getMessage());
            logWarning(iae.getMessage());
        }
    }

    /**
 * The PurseManager must be open and self-consistent. Get a shallow
 * clone of the Pockets within the unencrypted Purse and cache it
 * for subsequent inspection.
 * Note: the individual Pockets are immutable, but be careful that
 * they might not remain members of the Purse.
 *
 * @return Pocket[] a deep clone of the contents - never null
 */
    public Pocket[] list() {
        mustBeOpen();
        try {
            this.purseManager.reOpen();
        } catch (PurseStructuralException pse) {
            throw new IllegalStateException("PurseManager reOpen failed! " + pse.toString());
        }
        SelfDefinedField[] list = this.purseManager.list();
        Pocket[] newList = new Pocket[list.length];
        for (int i = 0; i < list.length; i++) {
            newList[i] = (Pocket) list[i];
        }
        this.lastPocketList = newList;
        return newList;
    }

    /**
 * return a numbered list of the members of the Pocket array which can be
 * accessed with the findCachedPocket method.
 *
 * @return String of an indexed list of the contents
 */
    public String getPocketInventory() {
        mustBeOpen();
        refreshPocketInventory();
        StringBuffer s = new StringBuffer();
        int num = this.lastPocketList.length;
        s.append("purse holds ");
        s.append(num);
        s.append(" pockets");
        for (int i = 0; i < num; i++) {
            Pocket pocket = this.lastPocketList[i];
            s.append("\n");
            s.append(i);
            s.append(": ");
            s.append(pocket.getIdentifier().getPayloadAsString());
        }
        return s.toString();
    }

    /**
 * Helper method to manipulate the cached Pocket array created in
 * the list method. Locate the Pocket by either its index number or
 * the given String (whiole identity or substring)
 *
 * TODO: Danger! mechanism needs to be refactored and made neater and safer!
 *
 * @return String of the Pocket if found, or a suitable message
 */
    public Pocket findCachedPocket(String s) {
        if (s == null) throw new IllegalArgumentException("null search string!");
        String searchString = s.trim();
        int sLen = searchString.length();
        if (sLen < 1) throw new IllegalArgumentException("empty search string!");
        mustBeOpen();
        refreshPocketInventory();
        Pocket pocket = null;
        int count = this.lastPocketList.length;
        if (count == 0) return pocket;
        char firstChar = searchString.charAt(0);
        if (Character.isDigit(firstChar)) {
            int index = -1;
            try {
                index = Integer.parseInt(searchString);
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException(searchString + " index is not all-numeric!");
            }
            int maxIndex = count - 1;
            if ((index < 0) || (index > maxIndex)) {
                throw new IllegalArgumentException("there are " + count + " pockets, so index " + index + " is out of range!");
            }
            pocket = this.lastPocketList[index];
        } else {
            String searchArg = searchString.toLowerCase();
            for (int i = 0; i < count; i++) {
                Pocket temp = this.lastPocketList[i];
                String id = temp.getIdentifier().getValue().toLowerCase();
                int ix = id.indexOf(searchArg);
                if (ix > -1) {
                    pocket = temp;
                    break;
                }
            }
        }
        return pocket;
    }

    /**
 * generate a shallow cloned cache of the Pockets currently in the Purse.
 */
    private void refreshPocketInventory() {
        if ((this.lastPocketList == null) || (this.lastPocketList.length != this.size())) {
            this.lastPocketList = list();
        }
    }

    /**
 * indicate whether the target object contains an unencrypted entity
 * which matches the given identifier
 */
    public boolean contains(String identifier) {
        mustBeOpen();
        Utf8String id = new Utf8String(identifier);
        return this.purseManager.contains(id);
    }

    /**
 * get the Pocket which matches the given identifier
 *
 * @return Pocket or null if not found
 */
    public Pocket find(String identifier) {
        mustBeOpen();
        Pocket pocket = null;
        Utf8String id = new Utf8String(identifier);
        SelfDefinedField field = this.purseManager.find(id);
        if (field != null) {
            if (!(field instanceof Pocket)) throw new IllegalArgumentException("matched entity is not a Pocket: " + field.toString());
            pocket = (Pocket) field;
        }
        return pocket;
    }

    /**
 * rename the Pocket (which must exist) and refresh the Purse.
 */
    public void rename(Pocket pocket, String newName) throws PurseStructuralException {
        mustBeOpen();
        this.purseManager.rename(pocket, newName);
        this.purseManager.reOpen();
        this.isChanged = true;
    }

    /**
 * rename the Pocket controlled by the given PocketManager.
 */
    public void rename(PocketManager pocketMgr, String newName) throws PurseStructuralException {
        mustBeOpen();
        this.purseManager.rename(pocketMgr.getPocket(), newName);
        this.purseManager.reOpen();
        this.isChanged = true;
    }

    /**
 * a managed Purse can contain multiple Pocket instances, so
 * replace or add the given entry as appropriate.
 */
    public void replace(Pocket pocket) {
        mustBeOpen();
        this.purseManager.replace(pocket);
        this.isChanged = true;
    }

    /**
 * a managed Purse can contain multiple Pocket instances, so
 * replace or add the Pocket controlled by the given PocketManager
 * as appropriate.
 */
    public void replace(PocketManager pocketMgr) {
        if (pocketMgr == null) throw new IllegalArgumentException("null PocketManager!");
        mustBeOpen();
        this.purseManager.replace(pocketMgr.getPocket());
        this.isChanged = true;
    }

    /**
 * remove the Pocket matching the given identity if it exists
 */
    public void remove(String identifier) {
        mustBeOpen();
        Utf8String id = new Utf8String(identifier);
        this.purseManager.remove(id);
        this.isChanged = true;
    }

    /**
 * a managed Purse can contain multiple Pocket instances, so
 * remove the Pocket controlled by the given PocketManager.
 */
    public void remove(PocketManager pocketMgr) {
        mustBeOpen();
        Utf8String id = pocketMgr.getPocket().getIdentifier();
        this.purseManager.remove(id);
        this.isChanged = true;
    }

    /**
 * just say if the object is open at the moment
 * @return java.lang.String
 */
    public String toString() {
        String s = isOpen() ? "open with " + size() + " pockets" : "closed";
        return "Purse is " + s;
    }
}
