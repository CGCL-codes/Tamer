package de.joergjahnke.gameboy.jme;

import de.joergjahnke.common.util.LRUCache;
import de.joergjahnke.gameboy.Cartridge;
import de.joergjahnke.gameboy.ROMManager;
import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 * Implementation of a ROMManager which stores the ROM contents in a J2ME RecordStore and only parts in Java memory
 *
 * @author Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public class RecordStoreROMManager implements ROMManager {

    /**
     * mobile device's record store where we store the ROM content
     */
    private final RecordStore rs;

    /**
     * maps ROM bank numbers to record store IDs
     */
    private final Vector romBankIds = new Vector();

    /**
     * contains Integer objects for the ROM bank nos.
     */
    private final Vector romBankNos = new Vector();

    /**
     * here we cache some ROM pages
     */
    private final LRUCache cache;

    /**
     * Create a new RecordStoreROMManager
     *
     * @param   storageName name of the RecordStore on the mobile device
     * @param   cacheSize   no. of 16k ROM banks we store in Java memory
     */
    public RecordStoreROMManager(final String storageName, final int cacheSize) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        try {
            RecordStore.deleteRecordStore(storageName);
        } catch (Exception e) {
        }
        this.rs = RecordStore.openRecordStore(storageName, true);
        this.cache = new LRUCache(cacheSize);
    }

    public void reset() {
        this.cache.clear();
    }

    public void destroy() {
        this.cache.clear();
        try {
            this.rs.closeRecordStore();
            RecordStore.deleteRecordStore(this.rs.getName());
        } catch (Exception e) {
        }
    }

    public void storeROMBank(final int romBank, final byte[] data) {
        final byte[] romBankData = new byte[Cartridge.ROM_BANK_SIZE];
        System.arraycopy(data, 0, romBankData, 0, data.length);
        try {
            if (this.romBankIds.size() > romBank && this.romBankIds.elementAt(romBank) != null) {
                this.rs.setRecord(((Integer) this.romBankIds.elementAt(romBank)).intValue(), romBankData, 0, romBankData.length);
            } else {
                final int id = this.rs.addRecord(romBankData, 0, romBankData.length);
                this.romBankIds.setSize(romBank + 1);
                this.romBankIds.setElementAt(new Integer(id), romBank);
                this.romBankNos.setSize(romBank + 1);
                this.romBankNos.setElementAt(new Integer(romBank), romBank);
            }
            if (this.cache.size() < this.cache.capacity()) {
                this.cache.put(new Integer(romBank), romBankData);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not save ROM page on the device! The original error was:\n" + e);
        }
    }

    public byte[] loadROMBank(final int romBank) {
        final Integer romBankNo = (Integer) this.romBankNos.elementAt(romBank);
        try {
            byte[] result = (byte[]) this.cache.get(romBankNo);
            if (result == null) {
                result = this.rs.getRecord(((Integer) this.romBankIds.elementAt(romBank)).intValue());
                this.cache.put(romBankNo, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Could not load ROM page from the device! The original error was:\n" + e);
        }
    }
}
