package org.openbandy.io.rms;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.rms.RecordStore;
import org.openbandy.io.Serializable;
import org.openbandy.io.SerializationIdentifiers;
import org.openbandy.io.storage.Storage;
import org.openbandy.service.LogService;
import org.openbandy.util.StringUtil;

/**
 * Abtstract and encapsule rms knowlegede (id generation etc.)
 * 
 * TODO �berpr�fe alle RmsConnections, wo kann ich sparen ??!!!!!
 * 
 * TODO NAMEN vereinfachen und konsequenter verwenden !!
 *
 * <br><br>
 * (c) Copyright P. Bolliger 2007, ALL RIGHTS RESERVED.
 *
 * @author Philipp Bolliger (philipp@bolliger.name)
 * @version 1.0
 */
public class RmsStorage extends Storage {

    public static final String TYPE_POSTFIX = "s";

    protected static final String TYPES_RECORDSTORE_NAME = "TypesRecordStore";

    private static RmsStorage instance;

    private static Hashtable rmsIds;

    private static RmsConnection typesRecordStoreConnection;

    private RmsStorage() {
        typesRecordStoreConnection = getRmsConnection(TYPES_RECORDSTORE_NAME);
        if (typesRecordStoreConnection != null) {
            typesRecordStoreConnection.connectAndCreateIfNecessary();
            typesRecordStoreConnection.setupAsRecordStoreWithHeader();
        } else {
            LogService.warn(this, "No connection for " + TYPES_RECORDSTORE_NAME);
        }
        rmsIds = new Hashtable();
        rebuildObjectsTable();
    }

    /**
	 * TODO comment!
	 *
	 * @return
	 */
    public static synchronized RmsStorage getInstance() {
        if (instance == null) {
            instance = new RmsStorage();
        }
        return instance;
    }

    public int existsInStorage(Serializable serializable) {
        if (!SerializationIdentifiers.isValidId(serializable.getIdValue())) {
            return 0;
        }
        String recordStoreName = getRecordStoreName(serializable);
        if (connections.containsKey(recordStoreName)) {
            return serializable.getIdValue();
        }
        return 0;
    }

    /**
	 * This method will create a new entry in the RMS for the
	 * serializable object. If the object already exists, the
	 * connection from the cache will be returned.
	 * If a new entry is created, the object will be assigned
	 * the corresponding id value.
	 * 
	 * @param serializable
	 * @return
	 */
    public synchronized RmsConnection addObject(Serializable serializable) {
        String objectRecordStoreName = getRecordStoreName(serializable);
        if (connections.containsKey(objectRecordStoreName)) {
            RmsConnection conn = (RmsConnection) connections.get(objectRecordStoreName);
            return conn;
        }
        String typeName = getTypeRecordStoreName(serializable);
        RmsConnection typeConn = null;
        RmsTypeRecordStoreHeader typeRecordStoreHeader;
        if (!connections.containsKey(typeName)) {
            typeConn = createTypeRecordStore(typeName);
            typeRecordStoreHeader = new RmsTypeRecordStoreHeader();
        } else {
            typeConn = (RmsConnection) connections.get(typeName);
            typeRecordStoreHeader = typeConn.getHeader();
        }
        int objectIdInTypeRecordStore = typeConn.getNewIdFromRecordStore();
        typeRecordStoreHeader.addId(objectIdInTypeRecordStore);
        if (!SerializationIdentifiers.isValidId(serializable.getIdValue())) {
            int newObjectId = SerializationIdentifiers.RMS_ID_MIN + objectIdInTypeRecordStore;
            serializable.setIdValue(newObjectId);
            LogService.debug(this, "Generated new rms id : " + serializable.getIdName() + "=" + serializable.getIdValue());
        }
        objectRecordStoreName = getRecordStoreName(serializable);
        typeConn.updateStringAt(objectIdInTypeRecordStore, objectRecordStoreName);
        typeConn.updateStringAt(1, typeRecordStoreHeader.getHeader());
        RmsConnection objectConn = getRmsConnection(objectRecordStoreName);
        Vector objectAttributes = getObjectAttributes(serializable);
        objectConn.createEmptyRecordStore(objectRecordStoreName, objectAttributes.size());
        if (objectConn != null) {
            rmsIds.put(objectRecordStoreName, new Integer(objectIdInTypeRecordStore));
        } else {
            LogService.warn(this, "No connection for object " + serializable.getLegibleString());
        }
        return objectConn;
    }

    /**
	 * TODO comment!
	 *
	 * @param serializable
	 * @return
	 */
    public static RmsConnection getConnectionForObject(Serializable serializable) {
        String objectRecordStoreName = getRecordStoreName(serializable);
        return getConnection(objectRecordStoreName);
    }

    /**
	 * TODO comment!
	 *
	 * @param objectRecordStoreName
	 * @return
	 */
    public static RmsConnection getConnection(String objectRecordStoreName) {
        if (connections.containsKey(objectRecordStoreName)) {
            return (RmsConnection) connections.get(objectRecordStoreName);
        }
        return null;
    }

    /**
	 * TODO comment!
	 *
	 * @param serializable
	 * @return
	 */
    public static String getRecordStoreName(Serializable serializable) {
        return StringUtil.getShortClassName(serializable) + serializable.getIdValue();
    }

    /**
	 * TODO comment!
	 *
	 * @param serializable
	 * @return
	 */
    public static String getTypeRecordStoreName(Serializable serializable) {
        return StringUtil.getShortClassName(serializable) + TYPE_POSTFIX;
    }

    /**
	 * TODO comment!
	 *
	 * @param serializable
	 * @return
	 */
    public static Vector getObjectAttributes(Serializable serializable) {
        Vector objectAttributes = new Vector();
        objectAttributes.removeAllElements();
        serializable.getAttributeNames(objectAttributes);
        serializable.getReferencedIdNames(objectAttributes);
        return objectAttributes;
    }

    private RmsConnection getRmsConnection(String name) {
        if (connections.containsKey(name)) {
            return (RmsConnection) connections.get(name);
        } else {
            RmsConnection conn = new RmsConnection(name);
            if (addConnection(conn)) {
                return conn;
            }
        }
        return null;
    }

    private RmsConnection createTypeRecordStore(String typeRecordStoreName) {
        RmsConnection typeConn = getRmsConnection(typeRecordStoreName);
        if (typeConn != null) {
            typeConn.createEmptyRecordStore(typeRecordStoreName, 0);
            typeConn.setupAsRecordStoreWithHeader();
            int typesId = typesRecordStoreConnection.getNewIdFromRecordStore();
            RmsTypeRecordStoreHeader trsh = typesRecordStoreConnection.getHeader();
            trsh.addId(typesId);
            rmsIds.put(typeRecordStoreName, new Integer(typesId));
            typesRecordStoreConnection.updateStringAt(typesId, typeRecordStoreName);
            typesRecordStoreConnection.updateStringAt(1, trsh.getHeader());
            return typeConn;
        } else {
            LogService.warn(this, "No connection for " + typeRecordStoreName);
            return null;
        }
    }

    /**
	 * Rebuild the Bandy RMS Storage structure including
	 * the type recordstore.
	 */
    private void rebuildObjectsTable() {
        LogService.debug(this, "Rebuilding Caches");
        RmsRecord types = new RmsRecord(typesRecordStoreConnection.getRecordStore());
        types.moveTo(1);
        String headerString = types.readUTF();
        if (RmsTypeRecordStoreHeader.isValidHeaderString(headerString)) {
            RmsTypeRecordStoreHeader typesStoreHeader = new RmsTypeRecordStoreHeader();
            typesStoreHeader.setHeader(headerString);
            Enumeration elements = typesStoreHeader.getTypeRecordStoreIds();
            while (elements.hasMoreElements()) {
                String typeIdString = (String) elements.nextElement();
                int typeId = Integer.parseInt(typeIdString);
                types.moveTo(typeId);
                String typeRecordStoreName = types.readUTF();
                RmsConnection typeConnection = getRmsConnection(typeRecordStoreName);
                typeConnection.setHasHeader();
                if (typeConnection != null) {
                    typeConnection.connect();
                    rmsIds.put(typeRecordStoreName, new Integer(typeId));
                    LogService.debug(this, typeRecordStoreName + " already in store:");
                    RmsRecord objects = new RmsRecord(typeConnection.getRecordStore());
                    objects.moveTo(1);
                    String typeHeaderString = objects.readUTF();
                    if (RmsTypeRecordStoreHeader.isValidHeaderString(typeHeaderString)) {
                        RmsTypeRecordStoreHeader trsh = new RmsTypeRecordStoreHeader();
                        trsh.setHeader(typeHeaderString);
                        Enumeration typeRecordStoreIds = trsh.getTypeRecordStoreIds();
                        while (typeRecordStoreIds.hasMoreElements()) {
                            String objectIdString = (String) typeRecordStoreIds.nextElement();
                            int id = Integer.parseInt(objectIdString);
                            objects.moveTo(id);
                            String objectEntryName = objects.readUTF();
                            RmsConnection objectConnection = getRmsConnection(objectEntryName);
                            objectConnection.connect();
                            rmsIds.put(objectEntryName, new Integer(id));
                            LogService.debug(this, "- " + objectEntryName);
                        }
                    } else {
                        LogService.warn(this, "Invalid type record store: header missing");
                    }
                } else {
                    LogService.warn(this, "No connection for " + typeRecordStoreName);
                }
            }
        } else {
            LogService.warn(this, "Did not rebuild objects table: no valid header");
        }
    }

    public void reset() {
        closeAllConnections();
        deleteAllRecordStores();
        instance = null;
    }

    public boolean isEmpty() {
        return (rmsIds.size() <= 15);
    }

    /**
	 * TODO comment!
	 *
	 * @return
	 */
    public static int numberOfUsedRecordStores() {
        String[] stores = RecordStore.listRecordStores();
        return stores.length;
    }

    private void deleteAllRecordStores() {
        String[] stores = RecordStore.listRecordStores();
        if (stores != null) {
            for (int i = 0; i < stores.length; i++) {
                RmsConnection.deleteRecordStore(stores[i]);
            }
        }
        rmsIds.clear();
        instance = new RmsStorage();
        LogService.info(this, "Deleted ALL RecordStores");
    }

    public boolean deleteShallow(Serializable serializable) {
        String objectRecordStoreName = RmsStorage.getRecordStoreName(serializable);
        RmsConnection objectConn = (RmsConnection) connections.get(objectRecordStoreName);
        if (objectConn != null) {
            if (objectConn.executeDELETE(serializable)) {
                removeConnection(objectConn);
                deleteEntryInTypeRecordStore(serializable, objectRecordStoreName);
                rmsIds.remove(objectConn.getRecordStoreName());
                return true;
            }
        }
        LogService.warn(this, "Object deletion failed: " + serializable.getLegibleString());
        return false;
    }

    private void deleteEntryInTypeRecordStore(Serializable serializable, String objectRecordStoreName) {
        String typeRecordStoreName = getTypeRecordStoreName(serializable);
        RmsConnection typeConn = (RmsConnection) connections.get(typeRecordStoreName);
        deleteEntryFromRecordStoreWithHeader(typeConn, objectRecordStoreName);
        if (typeConn.getNumberOfRecords() == 1) {
            if (typeConn.isConnected()) {
                typeConn.close();
            }
            RmsConnection.deleteRecordStore(typeRecordStoreName);
            removeConnection(typeConn);
            deleteEntryFromRecordStoreWithHeader(typesRecordStoreConnection, typeRecordStoreName);
            rmsIds.remove(typeRecordStoreName);
        }
    }

    private void deleteEntryFromRecordStoreWithHeader(RmsConnection rmsConnection, String recordStoreName) {
        RmsRecord typeRecord = new RmsRecord(rmsConnection.getRecordStore());
        typeRecord.moveTo(1);
        String headerString = typeRecord.readUTF();
        RmsTypeRecordStoreHeader trsh = new RmsTypeRecordStoreHeader();
        trsh.setHeader(headerString);
        int idInTypeRecordStore = ((Integer) rmsIds.get(recordStoreName)).intValue();
        trsh.removeId(idInTypeRecordStore);
        rmsConnection.updateStringAt(1, trsh.getHeader());
        rmsConnection.deleteRecord(idInTypeRecordStore);
    }

    /**
	 * TODO comment!
	 *
	 */
    public void printAllRecordStores() {
        System.out.println("+++++++++++++++ RMS +++++++++++++++");
        System.out.println("--------------- ID Cache (size:" + rmsIds.size() + ")---------------");
        for (Enumeration e = rmsIds.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            System.out.println(key + " key:" + rmsIds.get(key));
        }
        System.out.println("");
        System.out.println("");
        System.out.println("--------------- STORES ---------------");
        System.out.println("");
        String[] stores = RecordStore.listRecordStores();
        if (stores != null) {
            if (stores.length > 1) {
                for (int i = 0; i < stores.length; i++) {
                    System.out.print("RecordStore " + i + ": " + stores[i]);
                    RmsConnection co = getRmsConnection(stores[i]);
                    if (co != null) {
                        co.connect();
                        co.printRecordStore();
                    }
                    System.out.println("");
                }
            }
        }
    }
}
