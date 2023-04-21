package com.elibera.m.rms;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import com.elibera.m.display.ProgressCanvas;
import com.elibera.m.events.RootThread;
import com.elibera.m.utils.HelperApp;
import com.elibera.m.utils.HelperStd;
import com.elibera.m.utils.HelperConverter;
import com.elibera.m.xml.HelperXMLParser;
import com.elibera.m.xml.PageSuite;
import com.elibera.m.xml.PageSuiteProperties;
import com.elibera.m.xml.display.DisplayElement;
import de.enough.polish.android.rms.*;
import com.elibera.m.zip.ZipInputStream;

public class RMS extends HelperConverter {

    public static int PAGE_MULTIPLEXER_RECORD_SIZE = 15 * 1024;

    /**
	 * öffnet eine abgespeicherte PageSuite
	 * @param storeid
	 * @return
	 */
    public static PageSuite openPageSuite(long storeid) throws Exception {
        RecordStoreImpl rs = openRecordStore(storeid, false, false);
        if (rs == null) throw new RecordStoreNotFoundException("" + storeid);
        System.out.println("openPageSuite:" + rs);
        System.out.println(rs.recordStoreId);
        System.out.println(rs.recIds.length);
        System.out.println(rs.pagesNums.length);
        System.out.println(rs.pagesRefs.length);
        System.out.println(rs.pagesRefs[0] + ":" + rs.pagesNums[0]);
        PageSuite ps = new PageSuite();
        ps.rmsStore = rs;
        ps.binariesRMS = getRecordStoreDataIntArray(10, rs);
        ps.pageIds = getRecordStoreDataStringArray(12, rs);
        ps.pageIdsNum = getRecordStoreDataIntArray(13, rs);
        ps.pageSize = getRecordStoreDataInt(14, rs);
        ps.lastViewedPage = getRecordStoreDataInt(8, rs);
        ps.index = getRecordStoreDataInt(6, rs);
        int pos = HelperStd.whereIsItInArray(HelperRMSStoreMLibera.dataStoreIds, storeid);
        if (pos >= 0) ps.serverID = HelperRMSStoreMLibera.dataStoreServerIDs[pos];
        int katId = getRecordStoreDataInt(9, rs);
        ps.kategorie = HelperRMSStoreMLibera.getNameForKatId(katId);
        ps.titel = getRecordStoreDataString(23, rs);
        setPageSuiteProperties(ps, getRecord(rs.recIds[22], rs));
        ps.mode = getRecordStoreDataInt(11, rs);
        ps.iconSysImg = getRecordStoreDataInt(15, rs);
        ps.iconCachePos = getRecordStoreDataInt(16, rs);
        ps.srcUrl = getRecordStoreDataString(20, rs);
        ps.srcTypeLong = getRecordStoreDataString(21, rs);
        return ps;
    }

    /**
	 * schreibt den Rest der PageSuite in den Store
	 * @param ps
	 * @param thread
	 * @throws Exception
	 */
    public static void finishPageSuiteStore(PageSuite ps, RootThread thread, ProgressCanvas pc) throws Exception {
        RecordStoreImpl rs = ps.rmsStore;
        System.out.println("finishPageSuiteStore:" + rs);
        System.out.println("ps.pages:" + ps.pages);
        if (ps.pages.length > 0) {
            if (pc != null) pc.setMaxValue(ps.pages.length);
            for (int i = 0; i < ps.pages.length; i++) {
                System.out.println("ps.pages:" + i);
                addPageToStore(serialiseByteArray(ps.pages[i]), getByte(ps.pagesClassIDs[i]), rs);
                if (pc != null && i % 15 == 0) HelperXMLParser.updateProgressBar(i, pc);
            }
            finishWritingPagesToStore(rs);
        }
        System.out.println("ps.binaries:" + ps.binaries);
        if (ps.binaries.length > 0) {
            System.out.println("ps.binaries:" + ps.binaries.length);
            if (pc != null) pc.setMaxValue(ps.binaries.length);
            for (int i = 0; i < ps.binaries.length; i++) {
                byte[] b = ps.binaries[i];
                int recId = -1;
                if (b != null) recId = rs.addRecord(b, pc);
                ps.binariesRMS = HelperStd.incArray(ps.binariesRMS, recId);
                if (pc != null) pc.setValue(i);
            }
        }
        int katId = HelperRMSStoreMLibera.getIDForKatName(ps.kategorie);
        ps.mode = ps.mode | HelperXMLParser.MODE_PS_IS_STORED;
        ps.mode = ps.mode ^ HelperXMLParser.MODE_IS_STOREABLE_PS;
        if (ps.lastViewedPage > 0) saveRecordStoreData(8, ps.lastViewedPage, rs);
        saveRecordStoreData(6, ps.index, rs);
        saveRecordStoreData(9, katId, rs);
        saveRecordStoreData(11, ps.mode, rs);
        saveRecordStoreData(12, ps.pageIds, rs);
        saveRecordStoreData(13, ps.pageIdsNum, rs);
        saveRecordStoreData(14, ps.pageSize, rs);
        saveRecordStoreData(15, ps.iconSysImg, rs);
        saveRecordStoreData(16, ps.iconCachePos, rs);
        saveRecordStoreData(20, ps.url, rs);
        saveRecordStoreData(21, ps.typeLong, rs);
        saveRecordStoreData(22, serialiseSuiteProperties(ps.suiteProperties), rs);
        saveRecordStoreData(23, ps.titel, rs);
        HelperRMSStoreMLibera.setKatIdAndTitle(rs.recordStoreId, katId, ps.titel, ps.serverID);
        saveRecordStoreData(10, ps.binariesRMS, rs);
        System.out.println(ps.pageSize + ":" + getRecordStoreDataInt(14, rs));
    }

    /**
	 * öffnet entweder den Store, oder erstellt einen neuen
	 * @param serverID
	 * @return
	 * @throws Exception
	 */
    public static RecordStoreImpl createNewPageSuiteStore(int estimatedSize, String serverID, RootThread thread) throws Exception {
        long storeId = HelperRMSStoreMLibera.getStoreIdForExistingDataStoreWithStoreID(serverID);
        if (storeId >= 0) {
            RecordStoreImpl rs = openRecordStore(storeId, true, false);
            for (int i = 0; i < rs.pagesRefs.length; i++) {
                try {
                    rs.deleteRecord(rs.pagesRefs[i]);
                } catch (Exception ee) {
                    System.out.println("deleting page:" + rs.pagesNums[i] + ":" + rs.pagesRefs[i]);
                    ee.printStackTrace();
                }
            }
            rs.pagesNums = new int[0];
            rs.pagesRefs = new int[0];
            saveRecordStoreData(18, rs.pagesNums, rs);
            saveRecordStoreData(19, rs.pagesRefs, rs);
            saveRecordStoreData(17, rs.pagesNums, rs);
            saveRecordStoreData(12, rs.pagesNums, rs);
            saveRecordStoreData(13, rs.pagesNums, rs);
            saveRecordStoreData(7, rs.pagesNums, rs);
            int[] binariesRMS = getRecordStoreDataIntArray(10, rs);
            for (int i = 0; i < binariesRMS.length; i++) {
                try {
                    if (binariesRMS[i] >= 0) rs.deleteRecord(binariesRMS[i]);
                } catch (Exception ee) {
                    System.out.println("deleting binary:" + binariesRMS[i]);
                    ee.printStackTrace();
                }
            }
            saveRecordStoreData(10, rs.pagesNums, rs);
            return rs;
        }
        long space = getFreeSpaceForCurrentStoreSystem();
        if (estimatedSize > space && space >= 0) {
            System.out.println("freespace needed:" + estimatedSize + "," + space);
            HelperRMSStoreMLibera.setAppData(17, 1);
            System.out.println("fileio freespace:" + estimatedSize + "," + getFreeSpaceForCurrentStoreSystem());
            if (estimatedSize > getFreeSpaceForCurrentStoreSystem()) throw new RecordStoreFullException(HelperApp.translateWord(HelperApp.ERROR_TEXT_NOT_ENOUGH_MEMORY_FOR_DOWNLOAD));
        }
        storeId = HelperRMSStoreMLibera.addDataStoreMetaData(0, "parsing...", serverID);
        return openRecordStore(storeId, true, false);
    }

    /**
	 * öffnet oder erstellt einen Record Store
	 * @param id
	 * @param create
	 * @param forceJavaRMS
	 * @return
	 * @throws RecordStoreException
	 * @throws RecordStoreFullException
	 * @throws RecordStoreNotFoundException
	 */
    public static RecordStoreImpl openRecordStore(long id, boolean create, boolean forceJavaRMS) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
        System.out.println("\n\nopenRecordStore:" + id);
        return new JavaRecordStore(id, create);
    }

    static Object[] openedRs = new Object[0];

    /**
	 * zeigt uns einen offenen RecordStore an
	 * @param rs
	 */
    public static void registerOpenRecordStore(RecordStoreImpl rs) {
        openedRs = HelperStd.incArray(openedRs, rs);
    }

    /**
	 * informiert uns über einen Record Store der geschlossen wird
	 * @param rs
	 */
    public static void registerClosedRecordStore(RecordStoreImpl rs) {
        Object[] newopenedRs = new Object[0];
        for (int i = 0; i < openedRs.length; i++) {
            if (openedRs[i] == null || openedRs[i].equals(rs)) continue;
            newopenedRs = HelperStd.incArray(newopenedRs, openedRs[i]);
        }
        try {
            rs.closeRecordStoreInternal();
        } catch (Exception e) {
        }
        openedRs = newopenedRs;
    }

    public static RecordStoreImpl findOpenedRecordStore(long id) {
        for (int i = 0; i < openedRs.length; i++) {
            if (openedRs[i] != null && ((RecordStoreImpl) openedRs[i]).recordStoreId == id) return (RecordStoreImpl) openedRs[i];
        }
        return null;
    }

    /**
	 * schließt alle offenen RecordStores
	 *
	 */
    public static void closeAllOpenRecordStores() {
        for (int i = 0; i < openedRs.length; i++) {
            try {
                if (openedRs[i] != null) ((RecordStoreImpl) openedRs[i]).closeRecordStore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        openedRs = new Object[0];
    }

    /**
	 * gibt uns den maximal freien Speicherplatz zurück
	 * @return
	 */
    public static int getFreeSpaceForCurrentStoreSystem() {
        try {
            if (HelperRMSStoreMLibera.getAppData(17) != 1) return HelperRMSStoreMLibera.getFreeSpaceOfRootStore();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return -1;
    }

    /**
	 * schreibt eine PageSuite-Seite in den Store
	 * sammelt mehrere seiten um diese zu aggregieren
	 * unbedingt am Ende des Parsends finishWritingPagesToStore() aufrufen
	 * @param page
	 * @param classids
	 * @param store
	 */
    public static void addPageToStore(byte[] page, byte[] classids, RecordStoreImpl store) {
        try {
            if (store.out == null) {
                store.curpage = 0;
                store.curpages = new int[0];
                store.out = new ByteArrayOutputStream();
                store.out.write(getByte(0));
            }
            ByteArrayOutputStream out = store.out;
            store.curpage++;
            store.curpages = HelperStd.incArray(store.curpages, out.size());
            out.write(page);
            store.curpages = HelperStd.incArray(store.curpages, out.size());
            out.write(classids);
            if (out.size() > PAGE_MULTIPLEXER_RECORD_SIZE) {
                writeMultiplexedPage(out.toByteArray(), store.curpage, store.curpages, store);
                store.out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * schreibt ausstehende Seiten in dem RMS raus
	 * @param store
	 */
    public static void finishWritingPagesToStore(RecordStoreImpl store) throws Exception {
        System.out.println("finishing");
        if (store.out != null) writeMultiplexedPage(store.out.toByteArray(), store.curpage, store.curpages, store);
        store.out = null;
        store.curpage = -1;
        store.curpages = null;
        System.out.println("finishWritingPagesToStore:pagesNums:" + store.pagesNums.length);
        System.out.println("finishWritingPagesToStore:pagesRefs:" + store.pagesRefs.length);
        saveRecordStoreData(18, store.pagesNums, store);
        saveRecordStoreData(19, store.pagesRefs, store);
    }

    /**
	 * schreibt eine aggregierte sammlung von seiten in den Store
	 * @param pages
	 * @param numpages
	 * @param store
	 */
    public static void writeMultiplexedPage(byte[] pages, int numpages, int[] positions, RecordStoreImpl store) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = getByte(positions);
        out.write(getByte(b.length));
        out.write(b);
        out.write(pages);
        System.out.println("writeMultiplexedPage:" + b.length + ":" + getByte(b.length).length + ":" + pages.length + "::" + out.toByteArray().length + ":" + (b.length + getByte(b.length).length + pages.length));
        writeMultiplexedPagesBlock(out.toByteArray(), numpages, store);
    }

    /**
	 * schreibt einen ganzen Seiten Block, der bereits serialisiert wurde
	 * @param pages
	 * @param numpages
	 * @param store
	 * @throws Exception
	 */
    public static void writeMultiplexedPagesBlock(byte[] pages, int numpages, RecordStoreImpl store) throws Exception {
        int lastPageNum = 0;
        if (store.pagesNums.length > 0) lastPageNum = store.pagesNums[store.pagesNums.length - 1];
        lastPageNum += numpages;
        store.pagesRefs = HelperStd.incArray(store.pagesRefs, addRecord(pages, store));
        store.pagesNums = HelperStd.incArray(store.pagesNums, lastPageNum);
        System.out.println("writeMultiplexedPagesBlock:" + pages + ":" + pages.length + ":" + store.pagesRefs[store.pagesRefs.length - 1] + ":" + getRecord(store.pagesRefs[store.pagesRefs.length - 1], store).length);
    }

    /**
	 * lädt eine Seite aus dem RMS
	 * @param pageNum
	 * @param store
	 * @param ps
	 * @param asNote
	 * @return
	 */
    public static DisplayElement[] openPage(int pageNum, RecordStoreImpl store, PageSuite ps, boolean asNote) {
        if (pageNum < 0) pageNum = 0;
        if (pageNum > store.pagesNums[store.pagesNums.length - 1]) pageNum = store.pagesNums[store.pagesNums.length - 1];
        System.out.println("openPage:" + pageNum + ":" + store.curpage + ":" + store.curpage2 + ":" + store.pagesNums[store.pagesNums.length - 1] + ":" + (store.curpage2 > store.curpage && pageNum <= store.curpage2 && pageNum >= store.curpage));
        if (!(store.curpage >= 0 && store.curpage2 > store.curpage && pageNum < store.curpage2 && pageNum >= store.curpage)) {
            System.out.println("openPage loading");
            int[] pageNums = store.pagesNums;
            int startpos = -1, pageNumszehntel = pageNums.length / 15;
            if (pageNumszehntel < 1) pageNumszehntel = 1;
            if (pageNum < pageNums[0]) startpos = 0; else {
                for (int i = 15; i > 0; i--) {
                    if (i * pageNumszehntel < pageNums.length) System.out.println(pageNum + ":" + i + ":" + pageNumszehntel + ":" + (i * pageNumszehntel) + ":" + pageNums[i * pageNumszehntel]);
                    if (i * pageNumszehntel < pageNums.length && pageNum < pageNums[i * pageNumszehntel]) startpos = i * pageNumszehntel;
                }
            }
            System.out.println("startpos:" + startpos + ":" + pageNumszehntel);
            if (startpos < 0) startpos = pageNums.length;
            startpos -= pageNumszehntel;
            if (startpos < 0) startpos = 0;
            int foundpos = -1;
            if (startpos == 0) {
                startpos++;
                if (pageNum < pageNums[0]) {
                    foundpos = 0;
                    startpos = pageNums.length;
                }
            }
            System.out.println("startpos:" + startpos + ":" + pageNums.length);
            for (int i = startpos; i < pageNums.length; i++) {
                System.out.println("searching:" + i + ":" + pageNums[i - 1] + ":" + pageNums[i]);
                if (pageNum >= pageNums[i - 1] && pageNum < pageNums[i]) {
                    foundpos = i;
                    break;
                }
            }
            if (foundpos < 0) {
                for (int i = 0; i < pageNums.length; i++) {
                    System.out.println("searching2:" + i + ":" + pageNums[i - 1] + ":" + pageNums[i]);
                    if (pageNum >= pageNums[i - 1] && pageNum < pageNums[i]) {
                        foundpos = i;
                        break;
                    }
                }
            }
            store.curpage = foundpos < 1 ? 0 : pageNums[foundpos - 1];
            store.curpage2 = pageNums[foundpos];
            System.out.println("new curPage:" + store.curpage + ":" + store.curpage2 + ":" + foundpos + ":" + store.pagesRefs[foundpos]);
            byte[] b = getRecord(store.pagesRefs[foundpos], store);
            System.out.println("record:" + b + ":" + b.length + ":" + store.pagesRefs[foundpos]);
            int possize = getIntFromByte(b, 0);
            System.out.println(possize);
            byte[] posses = getBytesFromByte(b, 4, possize);
            System.out.println(posses.length);
            store.cachedBlock = getBytesFromByte(b, 4 + possize, b.length - possize - 4);
            store.curpages = getIntArrayFromByte(posses);
            System.out.println(store.cachedBlock.length);
            b = null;
        }
        System.out.println("retrieving for pageNum:" + pageNum + ":" + (pageNum - store.curpage) + ":" + (pageNum * 2) + ":" + store.curpages.length);
        pageNum -= store.curpage;
        pageNum *= 2;
        System.out.println("retrieving for pageNum:" + pageNum + ":" + store.curpages.length);
        if (pageNum >= store.curpages.length) pageNum = store.curpages.length - 2;
        int pos1 = store.curpages[pageNum];
        int pos2 = store.curpages[pageNum + 1];
        int pos3 = store.cachedBlock.length;
        if (pageNum + 2 < store.curpages.length) pos3 = store.curpages[pageNum + 2];
        System.out.println(pageNum + ":" + pos1 + ":" + pos2 + ":" + pos3 + "::" + store.curpages.length);
        byte[] page = getBytesFromByte(store.cachedBlock, pos1, pos2 - pos1);
        byte[] clids = getBytesFromByte(store.cachedBlock, pos2, pos3 - pos2);
        return HelperXMLParser.openPage(getIntArrayFromByte(clids), deSerialiseByteArray(page), ps, asNote);
    }

    /**
	 * lädt die RMS-DB arrays für den store
	 * @param store
	 */
    public static void loadStoreData(RecordStoreImpl store) {
        if (store.store1ids != null) return;
        store.store1ids = getRecordStoreDataStringArray(0, store);
        store.store1 = getRecordStoreDataIntArray(1, store);
        store.store2ids = getRecordStoreDataStringArray(2, store);
        store.store2 = getRecordStoreDataIntArray(3, store);
    }

    public static String[] getRecordStoreDataStringArray(int pos, RecordStoreImpl rs) {
        return getRecordStringArray(rs.recIds[pos], rs);
    }

    public static int[] getRecordStoreDataIntArray(int pos, RecordStoreImpl rs) {
        return getRecordIntArray(rs.recIds[pos], rs);
    }

    public static int getRecordStoreDataInt(int pos, RecordStoreImpl rs) {
        return getRecordInt(rs.recIds[pos], rs);
    }

    public static String getRecordStoreDataString(int pos, RecordStoreImpl rs) {
        return getRecordString(rs.recIds[pos], rs);
    }

    public static void saveRecordStoreData(int pos, String[] value, RecordStoreImpl rs) {
        saveRecordStoreData(pos, getByte(value), rs);
    }

    public static void saveRecordStoreData(int pos, int[] value, RecordStoreImpl rs) {
        saveRecordStoreData(pos, getByte(value), rs);
    }

    public static void saveRecordStoreData(int pos, int value, RecordStoreImpl rs) {
        saveRecordStoreData(pos, getByte(value), rs);
    }

    public static void saveRecordStoreData(int pos, String value, RecordStoreImpl rs) {
        saveRecordStoreData(pos, value.getBytes(), rs);
    }

    public static void saveRecordStoreData(int pos, byte[] value, RecordStoreImpl rs) {
        setRecord(rs.recIds[pos], value, rs);
    }

    public static byte[] getRecord(int rec, RecordStoreImpl rs) {
        try {
            return rs.getRecord(rec, null);
        } catch (Exception e) {
        }
        return new byte[0];
    }

    public static int[] getRecordIntArray(int rec, RecordStoreImpl rs) {
        return getIntArrayFromByte(getRecord(rec, rs));
    }

    public static long[] getRecordLongArray(int rec, RecordStoreImpl rs) {
        return getLongArrayFromByte(getRecord(rec, rs));
    }

    public static int getRecordInt(int rec, RecordStoreImpl rs) {
        return getIntFromByte(getRecord(rec, rs));
    }

    public static long getRecordLong(int rec, RecordStoreImpl rs) {
        return getLongFromByte(getRecord(rec, rs));
    }

    public static String getRecordString(int rec, RecordStoreImpl rs) {
        return new String(getRecord(rec, rs));
    }

    public static String[] getRecordStringArray(int rec, RecordStoreImpl rs) {
        return getStringArrayFromByte(getRecord(rec, rs));
    }

    public static void setRecord(int recordId, byte[] newData, RecordStoreImpl rs) {
        try {
            rs.setRecord(recordId, newData, 0, newData.length);
        } catch (Exception e) {
        }
    }

    public static void setRecord(int recordId, int[] newData, RecordStoreImpl rs) {
        setRecord(recordId, getByte(newData), rs);
    }

    public static void setRecord(int recordId, long[] newData, RecordStoreImpl rs) {
        setRecord(recordId, getByte(newData), rs);
    }

    public static void setRecord(int recordId, String[] newData, RecordStoreImpl rs) {
        setRecord(recordId, getByte(newData), rs);
    }

    public static void setRecord(int recordId, long newData, RecordStoreImpl rs) {
        setRecord(recordId, getByte(newData), rs);
    }

    public static void setRecord(int recordId, String newData, RecordStoreImpl rs) {
        setRecord(recordId, newData.getBytes(), rs);
    }

    public static int addRecord(byte[] data, RecordStoreImpl rs) throws RecordStoreFullException {
        try {
            return rs.addRecord(data, 0, data.length);
        } catch (Exception e) {
            throw new RecordStoreFullException(e.getMessage());
        }
    }

    public static int addRecord(int data, RecordStoreImpl rs) throws RecordStoreFullException {
        return addRecord(getByte(data), rs);
    }

    public static int addRecord(int[] data, RecordStoreImpl rs) throws RecordStoreFullException {
        return addRecord(getByte(data), rs);
    }

    public static int addRecord(String[] data, RecordStoreImpl rs) throws RecordStoreFullException {
        return addRecord(getByte(data), rs);
    }

    public static int addRecord(long data, RecordStoreImpl rs) throws RecordStoreFullException {
        return addRecord(getByte(data), rs);
    }

    public static int addRecord(String data, RecordStoreImpl rs) throws RecordStoreFullException {
        return addRecord(data.getBytes(), rs);
    }

    public static boolean exsistInStore1(String id, RecordStoreImpl store) {
        return HelperStd.whereIsItInArray(store.store1ids, id) >= 0;
    }

    public static boolean exsistInStore2(String id, RecordStoreImpl store) {
        return HelperStd.whereIsItInArray(store.store2ids, id) >= 0;
    }

    public static String getStringValueFromStore1(String id, RecordStoreImpl store) {
        return new String(getFromStore(id, 0, store, false));
    }

    public static byte[] getValueFromStore1(String id, RecordStoreImpl store) {
        return getFromStore(id, 0, store, false);
    }

    public static byte[] getValueFromStore2(String id, RecordStoreImpl store, boolean getnull) {
        return getFromStore(id, 1, store, getnull);
    }

    public static void setInStore2(String id, byte[] data, RecordStoreImpl store) throws RecordStoreFullException {
        setInStore(id, 1, data, store);
    }

    public static void setInStore1(String id, byte[] data, RecordStoreImpl store) throws RecordStoreFullException {
        setInStore(id, 0, data, store);
    }

    public static void setInStore1(String id, String data, RecordStoreImpl store) throws RecordStoreFullException {
        setInStore(id, 0, data.getBytes(), store);
    }

    public static void deleteInStore2(String id, RecordStoreImpl store) throws RecordStoreFullException {
        deleteInStore(id, 0, store);
    }

    public static void deleteInStore1(String id, RecordStoreImpl store) throws RecordStoreFullException {
        deleteInStore(id, 0, store);
    }

    /** 
	 * lädt daten von einem store
	 * storeIds --> alle IDs
	 * store --> RS IDs
	 * id --> id die ich suche
	 * gibt ein leeres Byte-Array zurück bei Fehlern
	 */
    public static byte[] getFromStore(String id, int storeNumber, RecordStoreImpl store, boolean getnull) {
        loadStoreData(store);
        int pos = HelperStd.whereIsItInArray(storeNumber == 0 ? store.store1ids : store.store2ids, id);
        if (pos >= 0) {
            int[] storedb = storeNumber == 0 ? store.store1 : store.store2;
            return getRecord(storedb[pos], store);
        }
        return getnull ? null : new byte[0];
    }

    public static void setInStore(String id, int storeNumber, byte[] value, RecordStoreImpl store) throws RecordStoreFullException {
        loadStoreData(store);
        int[] storedb = storeNumber == 0 ? store.store1 : store.store2;
        int pos = HelperStd.whereIsItInArray(storeNumber == 0 ? store.store1ids : store.store2ids, id);
        if (pos < 0) {
            storedb = HelperStd.incArray(storedb, addRecord(value, store));
            switch(storeNumber) {
                case 0:
                    store.store1ids = HelperStd.incArray(store.store1ids, id);
                    store.store1 = storedb;
                    saveRecordStoreData(0, store.store1ids, store);
                    saveRecordStoreData(1, storedb, store);
                    break;
                case 1:
                    store.store2ids = HelperStd.incArray(store.store2ids, id);
                    saveRecordStoreData(2, store.store2ids, store);
                    store.store2 = storedb;
                    saveRecordStoreData(3, storedb, store);
                    break;
            }
        } else setRecord(storedb[pos], value, store);
    }

    /**
	 * löscht den Wert aus dem Store
	 */
    public static void deleteInStore(String id, int storeNumber, RecordStoreImpl store) {
        loadStoreData(store);
        int[] storedb = storeNumber == 0 ? store.store1 : store.store2;
        int pos = HelperStd.whereIsItInArray(storeNumber == 0 ? store.store1ids : store.store2ids, id);
        if (pos >= 0) {
            storedb = HelperStd.removeIndexFromArray(storedb, pos);
            switch(storeNumber) {
                case 0:
                    store.store1ids = HelperStd.removeIndexFromArray(store.store1ids, pos);
                    store.store1 = storedb;
                    saveRecordStoreData(0, store.store1ids, store);
                    saveRecordStoreData(1, storedb, store);
                    break;
                case 1:
                    store.store2ids = HelperStd.removeIndexFromArray(store.store2ids, pos);
                    store.store1 = storedb;
                    saveRecordStoreData(2, store.store2ids, store);
                    saveRecordStoreData(3, storedb, store);
                    break;
            }
        }
    }

    /**
	 * deserialisiert die Properties für die PageSuite
	 * @param ps
	 * @param data
	 */
    public static void setPageSuiteProperties(PageSuite ps, byte[] b) {
        byte[][] data = deSerialiseByteArray(b);
        ps.suiteProperties.metaData = getStringArrayFromByte(data[0]);
        ps.suiteProperties.backgroundColor = getIntFromByte(data[1]);
        ps.suiteProperties.stdTextColor = getIntFromByte(data[2]);
        ps.suiteProperties.rndPageOrder = data.length < 4 ? null : getIntArrayFromByte(data[3]);
        ps.suiteProperties.showTabBar = (data.length < 5 ? 1 : getIntFromByte(data[4])) == 1;
    }

    /**
	 * serialisiert die Suite Properties
	 * @param ps
	 * @return
	 */
    public static byte[] serialiseSuiteProperties(PageSuiteProperties suiteProperties) {
        byte[][] data = { getByte(suiteProperties.metaData), getByte(suiteProperties.backgroundColor), getByte(suiteProperties.stdTextColor), getByte(suiteProperties.rndPageOrder), getByte(suiteProperties.showTabBar ? 1 : 0) };
        return serialiseByteArray(data);
    }

    /**
	 * setzt eine besuchte Seite
	 * @param rs
	 * @param pageNum
	 */
    public static void addViewedPage(RecordStoreImpl rs, int pageNum) {
        int[] viewedPages = getRecordStoreDataIntArray(7, rs);
        if (HelperStd.whereIsItInArray(viewedPages, pageNum) >= 0) return;
        viewedPages = HelperStd.incArray(viewedPages, pageNum);
        try {
            saveRecordStoreData(7, viewedPages, rs);
        } catch (Exception e) {
        }
    }
}
