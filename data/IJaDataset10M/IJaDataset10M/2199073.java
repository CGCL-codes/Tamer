package com.knowgate.dataobjs;

import java.lang.System;
import java.util.TreeMap;
import java.util.Iterator;
import java.lang.Thread;

final class DBSubsetCacheReaper extends Thread {

    private DBSubsetCache oDBCache;

    DBSubsetCacheReaper(DBSubsetCache objDBCache) {
        oDBCache = objDBCache;
    }

    private void reapEntries() {
        int iLRU = oDBCache.iTopIndex - oDBCache.iUsed;
        int iDiscardCount = (oDBCache.capacity() * 2) / 10;
        int iComma;
        String sEntry;
        String sTable;
        String sKey;
        for (int i = iLRU; i < iLRU + iDiscardCount; i++) {
            sEntry = oDBCache.getKey(iLRU);
            if (null != sEntry) {
                iComma = sEntry.indexOf(',');
                sTable = sEntry.substring(0, iComma);
                sKey = sEntry.substring(iComma + 1);
                oDBCache.expire(sKey);
            }
        }
        oDBCache.iUsed -= iDiscardCount;
    }

    public void run() {
        reapEntries();
    }
}

/**
   *
   * <p>Descripci�n: Cache local de objetos DBSubset</p>
   * <p>Copyright: Copyright (c) KnowGate 2003</p>
   * @version 1.0
   */
public final class DBSubsetCache {

    /**
     *
     * @param iCapacity N�mero m�ximo de entradas que admitir� el cache
     */
    public DBSubsetCache(int iCapacity) {
        iUsed = 0;
        iTopIndex = 0;
        iCacheCapacity = iCapacity;
        LRUList = new String[iCacheCapacity];
        for (int s = 0; s < iCacheCapacity; s++) LRUList[s] = null;
        oCache = new TreeMap();
    }

    /**
   * N�mero m�ximo de entradas que admite el cache
   */
    public int capacity() {
        return iCacheCapacity;
    }

    /**
   * A�ade una nueva entra al cache
   * @param sTableName Tabla asociada (opcional)
   * @param sKey Clave �nica para la entrada de cache
   * @param oDBSS Conjunto de filas a asociadas a la clave
   */
    public synchronized void put(String sTableName, String sKey, DBSubset oDBSS) {
        int iIndex = iTopIndex % iCacheCapacity;
        iTopIndex++;
        iUsed++;
        DBCacheEntry oEntry = new DBCacheEntry(oDBSS, sTableName, iIndex);
        DBSubsetCacheReaper oReaper;
        if (null == sTableName) sTableName = "none";
        oCache.put(sKey, oEntry);
        LRUList[iIndex] = sTableName + "," + sKey;
        if (iUsed >= iCacheCapacity - 1) {
            oReaper = new DBSubsetCacheReaper(this);
            oReaper.run();
        }
    }

    /**
   * Elimina una entrada del cache
   * @param sKey Identificador �nico de la entrada a eliminar
   * @return <b>true</b> si el cache conten�a una entrada con el identificador especificado, <b>false</b> si no se elimin� ninguna entrada del cache.
   */
    public synchronized boolean expire(String sKey) {
        Object objEntry = oCache.get(sKey);
        if (null != objEntry) {
            setKey(null, ((DBCacheEntry) objEntry).iIndex);
            oCache.remove(sKey);
        }
        return null != objEntry ? true : false;
    }

    /**
   * Reemplaza una entrada del cache con un nuevo valor
   * @param sTableName Nombre de la tabla asociada (opcional)
   * @param sKey Identificador �nico de la entrada a reemplazar
   * @param oDBSS Nuevo DBSubset a almacenar en el cache
   */
    public void replace(String sTableName, String sKey, DBSubset oDBSS) {
        expire(sKey);
        put(sTableName, sKey, oDBSS);
    }

    /**
   * Limpia todo el cache
   */
    public synchronized void clear() {
        oCache.clear();
        for (int s = 0; s < iCacheCapacity; s++) LRUList[s] = null;
        iTopIndex = 0;
        iUsed = 0;
    }

    /**
   * Elimina todas las entradas del cache asociadas a una tabla
   * @param sTable Nombre de la tabla
   */
    public synchronized void clear(String sTable) {
        Iterator oIter = oCache.keySet().iterator();
        String sKey;
        DBCacheEntry oEntry;
        if (sTable == null) sTable = "none";
        while (oIter.hasNext()) {
            sKey = (String) oIter.next();
            oEntry = (DBCacheEntry) oCache.get(sKey);
            if (sTable.equals(oEntry.sTable)) expire(sKey);
        }
    }

    /**
   * Obtiene un DBSubset del cache
   * @param sKey Identificador �nico del DBSubset a obtener
   * @return Referencia al DBSubset o <b>null</b> si no se encontr� ning�n DBSubset con dicho identificador �nico
   */
    public DBSubset get(String sKey) {
        Object oObj = oCache.get(sKey);
        DBCacheEntry oEntry;
        if (oObj != null) {
            oEntry = (DBCacheEntry) oObj;
            oEntry.iTimesUsed++;
            oEntry.lastUsed = System.currentTimeMillis();
            return oEntry.oDBSubset;
        } else return null;
    }

    /**
   * Obtiene una entrada del cache
   * @param sKey Identificador �nico del DBSubset a obtener
   * @return Referencia al DBCacheEntry o <b>null</b> si no se encontr� ning�n DBSubset con dicho identificador �nico
   */
    public DBCacheEntry getEntry(String sKey) {
        Object oObj = oCache.get(sKey);
        DBCacheEntry oEntry;
        if (oObj != null) {
            oEntry = (DBCacheEntry) oObj;
            oEntry.iTimesUsed++;
            oEntry.lastUsed = System.currentTimeMillis();
            return oEntry;
        } else return null;
    }

    public String getKey(int iEntryIndex) {
        return LRUList[iEntryIndex % iCacheCapacity];
    }

    public void setKey(String sKey, int iEntryIndex) {
        LRUList[iEntryIndex % iCacheCapacity] = sKey;
    }

    public class DBCacheEntry {

        public long lastModified;

        public long lastUsed;

        public int iTimesUsed;

        public int iIndex;

        public String sTable;

        public DBSubset oDBSubset;

        DBCacheEntry(DBSubset oDBSS, String sTbl, int iIdx) {
            sTable = sTbl;
            iIndex = iIdx;
            iTimesUsed = 0;
            lastUsed = lastModified = System.currentTimeMillis();
            oDBSubset = oDBSS;
        }
    }

    private int iCacheCapacity;

    private String LRUList[];

    private TreeMap oCache;

    public int iTopIndex;

    public int iUsed;
}
