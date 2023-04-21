package org.gdbi.util.parse;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.gdbi.api.*;
import org.gdbi.util.cache.UCacheGdbiMiniRecord;
import org.gdbi.util.debug.UDebugError;
import org.gdbi.util.memdb.UMemdbContext;
import org.gdbi.util.memdb.UMemdbRecord;
import org.gdbi.util.memdb.UMemdbIntrDatabase;
import org.gdbi.util.name.UNameSearch;
import org.gdbi.util.rec.URecMiniRecord;
import static org.gdbi.api.GdbiConstants.*;

/**
 * RawdbDatabase is a GdbiIntrDatabase for text files and internal String
 * arrays.
 * TODO: are all records always in memory?
 */
public class RawdbDatabase implements GdbiIntrDatabase, UMemdbIntrDatabase {

    public static final GdbiDBInfo DB_INFO = new GdbiDBInfo(GdbiDBInfo.DB_IMPL_RAW, GdbiDBInfo.DB_SEARCH_TYPE_GENJ, 0);

    private int globalID = 1000;

    private static final GdbiUtilDebug udebug = UMemdbRecord.udebug;

    private static final String DEFAULT_NAME = "<internal>";

    private static final String TMP_SUFFIX = ".tmp";

    private static final String BAK_SUFFIX = ".bak";

    private static int internalID = 1;

    private final UCacheGdbiMiniRecord cacheFam = new UCacheGdbiMiniRecord();

    private final UCacheGdbiMiniRecord cacheIndi = new UCacheGdbiMiniRecord();

    private final UCacheGdbiMiniRecord cacheOther = new UCacheGdbiMiniRecord();

    private GdbiMiniRecord mrecHead, mrecTrlr;

    private final GdbiDatabase database;

    private final String dbName;

    private final File fileUser, fileBackup, fileTmp;

    private boolean isReadOnly;

    private boolean closed = false;

    private UParseNextXref nextXref;

    private boolean savedBackup;

    public RawdbDatabase() {
        this(null);
    }

    /**
     * RawdbDatabase constructor that writes to a file when non-null.
     *
     * Note that instanciating this does not read in the file.
     * You must use the public class, UParseGedcomParser.
     */
    public RawdbDatabase(File fileIn) {
        fileUser = fileIn;
        if (fileUser == null) {
            dbName = DEFAULT_NAME + internalID++;
            fileBackup = null;
            fileTmp = null;
        } else {
            dbName = fileUser.getName();
            fileBackup = new File(fileUser.getPath() + BAK_SUFFIX);
            fileTmp = new File(fileUser.getPath() + TMP_SUFFIX);
            savedBackup = false;
        }
        database = new GdbiDatabase(this);
        nextXref = new UParseNextXref(database);
    }

    public GdbiDatabase getGdbiDatabase() {
        return database;
    }

    public String getName() {
        return dbName;
    }

    public File getFile() {
        return fileUser;
    }

    public GdbiIndi createGdbiIndi(GdbiXref xref) {
        final RawdbContext.Indi rec = RawdbContext.Record.createIndi(this, nextXref.nextIndi.next(xref));
        final GdbiIndi indi = new GdbiIndi(rec);
        saveRecord(indi);
        return indi;
    }

    public GdbiFam createGdbiFam(GdbiXref xref) {
        final RawdbContext.Fam rec = RawdbContext.Record.createFam(this, nextXref.nextFam.next(xref));
        final GdbiFam fam = new GdbiFam(rec);
        saveRecord(fam);
        return fam;
    }

    public GdbiObje createGdbiObje(GdbiXref xref) {
        final UMemdbRecord.Obje rec = RawdbContext.Record.createObje(this, nextXref.nextObje.next(xref));
        final GdbiObje obje = new GdbiObje(rec);
        saveRecord(obje);
        return obje;
    }

    public GdbiNote createGdbiNote(GdbiXref xref) {
        final UMemdbRecord.Note rec = RawdbContext.Record.createNote(this, nextXref.nextNote.next(xref));
        final GdbiNote note = new GdbiNote(rec);
        saveRecord(note);
        return note;
    }

    public GdbiRepo createGdbiRepo(GdbiXref xref) {
        final UMemdbRecord.Repo rec = RawdbContext.Record.createRepo(this, nextXref.nextRepo.next(xref));
        final GdbiRepo repo = new GdbiRepo(rec);
        saveRecord(repo);
        return repo;
    }

    public GdbiSour createGdbiSour(GdbiXref xref) {
        final RawdbContext.Sour rec = RawdbContext.Record.createSour(this, nextXref.nextSour.next(xref));
        final GdbiSour sour = new GdbiSour(rec);
        saveRecord(sour);
        return sour;
    }

    public GdbiSubm createGdbiSubm(GdbiXref xref) {
        final UMemdbRecord.Submitter rec = RawdbContext.Record.createSubm(this, nextXref.nextSubm.next(xref));
        final GdbiSubm subm = new GdbiSubm(rec);
        saveRecord(subm);
        return subm;
    }

    public GdbiSubn createGdbiSubn(GdbiXref xref) {
        final UMemdbRecord.Submission rec = RawdbContext.Record.createSubn(this, nextXref.nextSubn.next(xref));
        final GdbiSubn subn = new GdbiSubn(rec);
        saveRecord(subn);
        return subn;
    }

    /**
     * Return the GdbiRecord for this GdbiXref.
     */
    public GdbiRecord getRecord(GdbiXref xref) {
        GdbiRecord rec = getIndi(xref);
        if (rec == null) rec = getFam(xref);
        if (rec == null) rec = getOther(xref);
        return rec;
    }

    /**
     * Return the GdbiIndi for this GdbiXref.
     */
    public GdbiIndi getIndi(GdbiXref xref) {
        final GdbiMiniIndi mini = getMiniIndi(xref);
        return (isGoodMiniRecord(mini) ? mini.getIndi() : null);
    }

    /**
     * Return the GdbiFam for this GdbiXref.
     */
    public GdbiFam getFam(GdbiXref xref) {
        final GdbiMiniFam mini = getMiniFam(xref);
        return (isGoodMiniRecord(mini) ? mini.getFam() : null);
    }

    /**
     * Return the GdbiObje for this GdbiXref.
     */
    public GdbiObje getObje(GdbiXref xref) {
        final GdbiRecord rec = getOther(xref);
        return (rec instanceof GdbiObje) ? (GdbiObje) getOther(xref) : null;
    }

    /**
     * Return the GdbiNote for this GdbiXref.
     */
    public GdbiNote getNote(GdbiXref xref) {
        final GdbiRecord rec = getOther(xref);
        return (rec instanceof GdbiNote) ? (GdbiNote) getOther(xref) : null;
    }

    /**
     * Return the GdbiRepo for this GdbiXref.
     */
    public GdbiRepo getRepo(GdbiXref xref) {
        final GdbiRecord rec = getOther(xref);
        return (rec instanceof GdbiRepo) ? (GdbiRepo) getOther(xref) : null;
    }

    /**
     * Return the GdbiSour for this GdbiXref.
     */
    public GdbiSour getSour(GdbiXref xref) {
        final GdbiRecord rec = getOther(xref);
        return (rec instanceof GdbiSour) ? (GdbiSour) getOther(xref) : null;
    }

    /**
     */
    public boolean isCached(GdbiXref xref) {
        final GdbiRecord rec = getRecord(xref);
        return (rec != null);
    }

    public GdbiIndi getDefaultIndi() {
        udebug.dprintln("default INDI not available");
        return null;
    }

    public GdbiIndi getFirstIndi() {
        final GdbiXref[] xrefs = getXrefArray(GdbiRecType.INDI);
        return (xrefs.length > 0) ? getIndi(xrefs[0]) : null;
    }

    public GdbiIndi getLastIndi() {
        final GdbiXref[] xrefs = getXrefArray(GdbiRecType.INDI);
        return (xrefs.length > 0) ? getIndi(xrefs[xrefs.length - 1]) : null;
    }

    /**
     * Return the GdbiMiniFam for this GdbiXref.
     */
    public GdbiMiniFam getMiniFam(GdbiXref xref) {
        return (GdbiMiniFam) getMiniRecord(GdbiRecType.FAM, xref);
    }

    /**
     * Return the GdbiMiniIndi for this GdbiXref.
     */
    public GdbiMiniIndi getMiniIndi(GdbiXref xref) {
        return (GdbiMiniIndi) getMiniRecord(GdbiRecType.INDI, xref);
    }

    /**
     * Return existing mini-rec, or create an undefined one for the type.
     */
    public GdbiMiniRecord getMiniRecord(GdbiRecType type, GdbiXref xref) {
        GdbiMiniRecord mrec;
        switch(type.id) {
            case GdbiRecType.ID_FAM:
                mrec = (GdbiMiniFam) cacheFam.get(xref);
                break;
            case GdbiRecType.ID_INDI:
                mrec = (GdbiMiniIndi) cacheIndi.get(xref);
                break;
            default:
                mrec = cacheOther.get(xref);
                break;
        }
        if (mrec == null) mrec = URecMiniRecord.createMiniRecord(type, database, xref);
        return mrec;
    }

    public GdbiMiniRecord[] getMiniRecordArray(GdbiRecType type) {
        return null;
    }

    public void close() {
        saveModified();
        closed = true;
    }

    /**
     * Get list of all locked records from list of all caches.
     */
    public GdbiRecord[] getLockedRecords() {
        final UCacheGdbiMiniRecord[] cacheList = { cacheFam, cacheIndi, cacheOther };
        final Vector<GdbiRecord> vector = new Vector<GdbiRecord>();
        for (UCacheGdbiMiniRecord cache : cacheList) {
            GdbiMiniRecord[] mrecs = cache.getSortedArray();
            for (GdbiMiniRecord mrec : mrecs) {
                if (isGoodMiniRecord(mrec)) {
                    final GdbiRecord rec = mrec.getRecord();
                    if (rec.getLocked()) vector.add(rec);
                }
            }
        }
        return vector.toArray(GdbiRecord.TOARRAY);
    }

    public int getSize(GdbiRecType type) {
        if (type == GdbiRecType.FAM) return cacheFam.keySet().size();
        if (type == GdbiRecType.INDI) return cacheIndi.keySet().size();
        return GdbiDatabase.defaultGetSize(database, type);
    }

    public GdbiXref[] getXrefArray(GdbiRecType type) {
        final String tag = (type == null) ? null : type.tag;
        udebug.dprintln("getXrefArray(" + tag + ")");
        GdbiMiniRecord[] mrecs = GdbiMiniRecord.TOARRAY;
        if (tag.equals(TAG_FAM)) mrecs = cacheFam.getSortedArray(); else if (tag.equals(TAG_INDI)) mrecs = cacheIndi.getSortedArray(); else mrecs = getOtherKeyArray(tag);
        final GdbiXref[] xrefs = new GdbiXref[mrecs.length];
        for (int i = 0; i < mrecs.length; i++) {
            xrefs[i] = mrecs[i].getXref();
            udebug.vprintln("xrefs[" + i + "]=" + xrefs[i]);
        }
        udebug.dprintln("returning...");
        return xrefs;
    }

    /**
     * Search for all INDIs with this name.
     */
    public GdbiMiniIndi[] searchName(String namePattern) {
        return UNameSearch.searchName(database, namePattern);
    }

    public boolean getReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean val) {
        isReadOnly = val;
    }

    /**
     * There is no maximum search size since everything is in memory.
     */
    public GdbiDBInfo getDBInfo() {
        return DB_INFO;
    }

    /**
     */
    public String[] getPlaces(boolean reread) {
        UDebugError.not_implemented();
        return null;
    }

    /**
     * Save all modified records.
     */
    public void saveModified() {
        saveFile();
    }

    public boolean isValid() {
        return (!closed);
    }

    /**
     * Used by FsdbFam only: create new mini-rec and cache result.
     */
    public GdbiMiniFam createMiniFam(GdbiFam fam) {
        UDebugError.not_needed();
        return null;
    }

    /**
     * Used by FsdbIndi only: create new mini-rec and cache result.
     */
    public GdbiMiniIndi createMiniIndi(GdbiIndi indi) {
        UDebugError.not_needed();
        return null;
    }

    /**
     * Delete record.
     */
    public void deleteRecord(UMemdbContext.RecordBase rec) {
        UDebugError.not_needed();
    }

    public boolean isComparable() {
        return true;
    }

    /**
     * Generate int used by Comparable.
     */
    public int nextXrefInt() {
        return globalID++;
    }

    public void toDatabase(GdbiIntrRecord rec) {
        saveModified();
    }

    public boolean useRecAsMiniRec() {
        return true;
    }

    void saveFile() {
        if (fileUser == null) return;
        udebug.dprintln("save to: " + fileTmp);
        PrintWriter output = null;
        try {
            output = new PrintWriter(fileTmp);
        } catch (IOException e) {
            database.logError(this, "Could not write GEDCOM", null);
            return;
        }
        if (mrecHead != null) output.write(UParseStatic.toGedcom(mrecHead));
        for (int tnum = 0; tnum < GdbiRecType.list.length; tnum++) {
            final GdbiRecType type = GdbiRecType.list[tnum];
            final GdbiXref[] xrefs = getXrefArray(type);
            udebug.dprintln("list of " + type.tag + "s, length=" + xrefs.length);
            for (int xnum = 0; xnum < xrefs.length; xnum++) {
                final GdbiRecord rec = getRecord(xrefs[xnum]);
                udebug.vprintln("writing record: " + xrefs[xnum]);
                if (rec != null) output.write(UParseStatic.toGedcom(rec));
            }
        }
        if (mrecTrlr != null) output.write(UParseStatic.toGedcom(mrecTrlr));
        udebug.dprintln("closing file...");
        output.close();
        if (!savedBackup) {
            renameTo(fileUser, fileBackup);
            savedBackup = true;
        }
        renameTo(fileTmp, fileUser);
    }

    /**
     * Since the records are also mini-recs, when we save the mini-rec in the
     * standard cache, we are caching the entire record.
     */
    public void saveRecord(GdbiRecord rec) {
        final GdbiMiniRecord mini = rec.getMiniRecord();
        assert (mini != null);
        if (mini == null) return;
        final GdbiRecType type = rec.getRecType();
        if (type == GdbiRecType.FAM) cacheFam.put(mini); else if (type == GdbiRecType.INDI) cacheIndi.put(mini); else if (type == GdbiRecType.HEAD) mrecHead = mini; else if (type == GdbiRecType.TRLR) mrecTrlr = mini; else cacheOther.put(mini);
    }

    /**
     * Used by getObje, getNote, and getSour to
     * return the GdbiRecord for this GdbiXref.
     */
    GdbiRecord getOther(GdbiXref xref) {
        final GdbiMiniRecord mini = cacheOther.get(xref);
        return (isGoodMiniRecord(mini) ? mini.getRecord() : null);
    }

    private GdbiMiniRecord[] getOtherKeyArray(String tag1) {
        final GdbiMiniRecord[] mrecs = cacheOther.getSortedArray();
        udebug.dprintln("finding " + tag1 + ", other length: " + mrecs.length);
        final Vector<GdbiMiniRecord> vector = new Vector<GdbiMiniRecord>();
        for (int i = 0; i < mrecs.length; i++) {
            final GdbiRecord rec = mrecs[i].getRecord();
            if ((rec != null) && tag1.equals(rec.getTag())) vector.add(mrecs[i]);
        }
        udebug.dprintln("vector size: " + vector.size());
        return vector.toArray(GdbiMiniRecord.TOARRAY);
    }

    /**
     * You delete a record by setting defined to false.
     */
    private boolean isGoodMiniRecord(GdbiMiniRecord mrec) {
        return ((mrec != null) && mrec.isDefined());
    }

    private void renameTo(File from, File to) {
        if (!from.exists()) return;
        if (to.exists()) to.delete();
        boolean worked = false;
        try {
            worked = from.renameTo(to);
        } catch (Exception e) {
            database.logError(this, "" + e, null);
        }
        if (!worked) {
            database.logWarning(this, "Could not rename GEDCOM to " + to.getAbsolutePath(), null);
            try {
                to.delete();
                final FileReader in = new FileReader(from);
                final FileWriter out = new FileWriter(to);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
                from.delete();
            } catch (Exception e) {
                database.logError(this, "" + e, null);
            }
        }
    }
}
