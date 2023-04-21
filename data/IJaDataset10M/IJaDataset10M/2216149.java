package de.jetbytes.jme.icenotesme.cryptedrms.recordstores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;
import de.jetbytes.jme.icenotes.crypt.ICryptModule;
import de.jetbytes.jme.icenotesme.Logger;
import de.jetbytes.jme.icenotesme.cryptedrms.data.AccountData;
import de.jetbytes.jme.icenotesme.cryptedrms.data.NoteData;

public class NoteRMS extends AbstractRMSCryptSupport implements RecordFilter {

    /**
	 * The database storing all the records of the notes
	 */
    protected volatile RecordStore recordstore = null;

    private String headlineFilter = null;

    private String fileName;

    public NoteRMS(ICryptModule cryptModule) {
        super(cryptModule);
    }

    public void open(String fileName) throws RecordStoreNotFoundException, RecordStoreException, RecordStoreFullException {
        try {
            recordstore = RecordStore.openRecordStore(fileName, true, recordstore.AUTHMODE_ANY, true);
            this.fileName = fileName;
            Logger.logInfo(this, "open", "recordstore[" + fileName + "] is opened.");
        } catch (RecordStoreException rse) {
            Logger.logInfo(this, "open", rse);
        }
    }

    public void close() throws RecordStoreNotOpenException, RecordStoreException {
        if (recordstore != null) {
            String fileName = recordstore.getName();
            Logger.logInfo(this, "close", "Closing Recordstore [" + fileName + "].");
            recordstore.closeRecordStore();
        }
    }

    private NoteData getDecryptedFromRecordStore(int recId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        byte[] decryptedBytes = decrypt(recordstore.getRecord(recId));
        return new NoteData(recId, decryptedBytes);
    }

    public boolean matches(byte[] candidateRecordBytes) {
        if (this.headlineFilter == null) {
            return false;
        }
        NoteData noteCandidate = new NoteData(decrypt(candidateRecordBytes));
        Logger.logInfo(this, "matches", headlineFilter + "?==?" + noteCandidate.getNoteHeadline() + " -> " + this.headlineFilter.equalsIgnoreCase(noteCandidate.getNoteHeadline()));
        return (this.headlineFilter.equalsIgnoreCase(noteCandidate.getNoteHeadline()));
    }

    public int add(NoteData noteData) throws RecordStoreNotOpenException, RecordStoreFullException, RecordStoreException {
        byte[] encryptedBytes = encrypt(noteData.getBytes());
        int recId = recordstore.addRecord(encryptedBytes, 0, encryptedBytes.length);
        noteData.setRecId(recId);
        Logger.logInfo(this, "add", "RMS-DEBUG: Added NoteData: [" + noteData + "]");
        return recId;
    }

    public NoteData get(final int recId) {
        NoteData noteData = null;
        try {
            noteData = getDecryptedFromRecordStore(recId);
        } catch (RecordStoreException rse) {
            Logger.logInfo(this, "search", rse);
        }
        return noteData;
    }

    public Hashtable getAll() throws RecordStoreException, IOException {
        Hashtable table = new Hashtable();
        RecordEnumeration re = recordstore.enumerateRecords(null, null, true);
        while (re.hasNextElement()) {
            int recId = re.nextRecordId();
            NoteData tmpNoteData = getDecryptedFromRecordStore(recId);
            table.put(tmpNoteData.getKey(), tmpNoteData);
        }
        return table;
    }

    public void updateNote(NoteData noteData) {
        try {
            byte[] encryptedBytes = encrypt(noteData.getBytes());
            recordstore.setRecord(noteData.getRecId(), encryptedBytes, 0, encryptedBytes.length);
        } catch (Exception e) {
            Logger.logInfo(this, "updateNote", e);
        }
    }

    public void deleteNote(int noteRecId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        Logger.logInfo(this, "deleteNote", "removing noteId=[" + noteRecId + "]");
        recordstore.deleteRecord(noteRecId);
    }

    public void deleteEverything() throws RecordStoreException {
        if (recordstore != null) {
            try {
                recordstore.deleteRecordStore(fileName);
                recordstore = null;
                Logger.logInfo(this, "deleteEverything", "Deleted Recordstore [" + fileName + "]...");
            } catch (RecordStoreException e) {
                Logger.logInfo(this, "deleteEverything", e);
                throw new RecordStoreException("Deleting Note RMS failed");
            }
        }
    }
}
