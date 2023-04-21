package backend.core.persistent.berkeley;

import java.io.File;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.log4j.Level;
import backend.core.AbstractONDEXGraph;
import backend.core.AttributeName;
import backend.core.CV;
import backend.core.ConceptAccession;
import backend.core.ConceptClass;
import backend.core.ConceptGDS;
import backend.core.ConceptName;
import backend.core.EvidenceType;
import backend.core.RelationGDS;
import backend.core.RelationKey;
import backend.core.RelationType;
import backend.core.Unit;
import backend.core.persistent.AbstractONDEXPersistent;
import backend.core.security.Session;
import backend.event.ONDEXListener;
import backend.event.type.DatabaseError;
import backend.event.type.EventType;
import backend.event.type.StatisticalOutput;
import backend.logging.ONDEXLogger;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentMutableConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public class BerkeleyEnv extends AbstractONDEXPersistent {

    private class RelationKeyBinding extends TupleBinding {

        private BerkeleyEnv berkeley;

        private Session s;

        public RelationKeyBinding(BerkeleyEnv berkeley, Session s) {
            this.berkeley = berkeley;
            this.s = s;
        }

        @Override
        public Object entryToObject(TupleInput ti) {
            BerkeleyConcept fromConcept = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(ti.readInt())));
            BerkeleyConcept toConcept = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(ti.readInt())));
            BerkeleyConcept qualifier = null;
            int qID = ti.readInt();
            if (qID > -1) {
                qualifier = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(qID)));
            }
            BerkeleyRelationTypeSet rtset = BerkeleyRelationTypeSet.deserialise(berkeley, s, getFromDatabase(BerkeleyRelationTypeSet.class, ti.readString()));
            RelationKey key = new RelationKey(s, fromConcept, toConcept, qualifier, rtset);
            return key;
        }

        @Override
        public void objectToEntry(Object o, TupleOutput to) {
            RelationKey key = (RelationKey) o;
            to.writeInt(key.getFromID().intValue());
            to.writeInt(key.getToID().intValue());
            if (key.getQualifierID() != null) to.writeInt(key.getQualifierID().intValue()); else to.writeInt(-1);
            to.writeString(key.getRTSetName());
        }
    }

    private class BerkeleyRelationKeyNameBinding extends TupleBinding {

        private BerkeleyEnv berkeley;

        private Session s;

        public BerkeleyRelationKeyNameBinding(BerkeleyEnv berkeley, Session s) {
            this.berkeley = berkeley;
            this.s = s;
        }

        @Override
        public Object entryToObject(TupleInput ti) {
            BerkeleyConcept fromConcept = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(ti.readInt())));
            BerkeleyConcept toConcept = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(ti.readInt())));
            BerkeleyConcept qualifier = null;
            int qID = ti.readInt();
            if (qID > -1) {
                qualifier = BerkeleyConcept.deserialise(berkeley, s, getFromDatabase(BerkeleyConcept.class, new Integer(qID)));
            }
            BerkeleyRelationTypeSet rtset = BerkeleyRelationTypeSet.deserialise(berkeley, s, getFromDatabase(BerkeleyRelationTypeSet.class, ti.readString()));
            RelationKey key = new RelationKey(s, fromConcept, toConcept, qualifier, rtset);
            String name = ti.readString();
            return new BerkeleyRelationKeyName(key, name);
        }

        @Override
        public void objectToEntry(Object o, TupleOutput to) {
            BerkeleyRelationKeyName keyname = (BerkeleyRelationKeyName) o;
            RelationKey key = keyname.getKey();
            to.writeInt(key.getFromID().intValue());
            to.writeInt(key.getToID().intValue());
            if (key.getQualifierID() != null) to.writeInt(key.getQualifierID().intValue()); else to.writeInt(-1);
            to.writeString(key.getRTSetName());
            if (keyname.getName() != null) to.writeString(keyname.getName()); else to.writeString("");
        }
    }

    private class BerkeleyIntegerNameBinding extends TupleBinding {

        @Override
        public Object entryToObject(TupleInput ti) {
            Integer key = new Integer(ti.readInt());
            String name = ti.readString();
            return new BerkeleyIntegerName(key, name);
        }

        @Override
        public void objectToEntry(Object o, TupleOutput to) {
            BerkeleyIntegerName keyname = (BerkeleyIntegerName) o;
            to.writeInt(keyname.getKey().intValue());
            to.writeString(keyname.getName());
        }
    }

    private class BerkeleyIntegerNameKeyCreator implements SecondaryKeyCreator {

        public boolean createSecondaryKey(SecondaryDatabase secDb, DatabaseEntry keyEntry, DatabaseEntry dataEntry, DatabaseEntry resultEntry) throws DatabaseException {
            BerkeleyIntegerName bin = (BerkeleyIntegerName) bindings.get(BerkeleyIntegerName.class).entryToObject(keyEntry);
            EntryBinding dataBinding = TupleBinding.getPrimitiveBinding(Integer.class);
            dataBinding.objectToEntry(bin.getKey(), resultEntry);
            return true;
        }
    }

    private class BerkeleyRelationKeyNameKeyCreator implements SecondaryKeyCreator {

        public boolean createSecondaryKey(SecondaryDatabase secDb, DatabaseEntry keyEntry, DatabaseEntry dataEntry, DatabaseEntry resultEntry) throws DatabaseException {
            BerkeleyRelationKeyName brkn = (BerkeleyRelationKeyName) bindings.get(BerkeleyRelationKeyName.class).entryToObject(keyEntry);
            RelationKey key = brkn.getKey();
            bindings.get(RelationKey.class).objectToEntry(key, resultEntry);
            return true;
        }
    }

    private HashSet<BerkeleyAbstractONDEXIterator> berkeleyIterators = new HashSet<BerkeleyAbstractONDEXIterator>();

    private Session s;

    private AbstractONDEXGraph root;

    private Environment myDbEnvironment = null;

    private Hashtable<Class, Database> databases = new Hashtable<Class, Database>();

    private Hashtable<Class, SecondaryDatabase> secDatabases = new Hashtable<Class, SecondaryDatabase>();

    private Hashtable<Class, Database> dupDatabases = new Hashtable<Class, Database>();

    private Hashtable<Class, EntryBinding> bindings = new Hashtable<Class, EntryBinding>();

    /**
	 * Create or loads Berkeley storage for the given filename. 
	 * 
	 * @param path - Berkeley database path
	 */
    public BerkeleyEnv(String path, ONDEXListener listener) {
        this(Session.NONE, path, listener);
    }

    /**
	 * Create or loads Berkeley storage for the given filename. 
	 * 
	 * @param s - session context
	 * @param path - Berkeley database path
	 */
    public BerkeleyEnv(Session s, String path, ONDEXListener listener) {
        File dir = new File(path);
        if (!dir.exists()) fireEventOccurred(new StatisticalOutput("New db to be creating...preparing dir"));
        if (dir.mkdirs()) fireEventOccurred(new StatisticalOutput("Created dirs " + path));
        StatisticalOutput error = new StatisticalOutput("Permissions error! on " + dir.getPath());
        error.setLog4jLevel(Level.FATAL);
        if (dir.canRead()) fireEventOccurred(error);
        if (dir.canWrite()) fireEventOccurred(error);
        addONDEXListener(listener);
        this.s = s;
        StatisticalOutput so = new StatisticalOutput("Using Berkeley with path " + path);
        so.setLog4jLevel(Level.INFO);
        fireEventOccurred(so);
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            myDbEnvironment = new Environment(new File(path), envConfig);
            EnvironmentMutableConfig envMutableConfig = new EnvironmentMutableConfig();
            envMutableConfig.setTxnNoSync(true);
            myDbEnvironment.setMutableConfig(envMutableConfig);
            setupDatabases();
            setupDupDatabases();
            setupBindings();
            root = new BerkeleyONDEXGraph(s, this);
            root.addONDEXGraphListener(new ONDEXLogger());
        } catch (DatabaseException dbe) {
            DatabaseError de = new DatabaseError("[BerkeleyEnv - BerkeleyEnv] " + dbe.getMessage());
            fireEventOccurred(de);
        }
    }

    /**
	 * Creates databases to hold ondex data. 
	 * 
	 * @throws DatabaseException
	 */
    private void setupDatabases() throws DatabaseException {
        Database myDb = null;
        SecondaryDatabase mySecDb = null;
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDeferredWrite(true);
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(false);
        SecondaryConfig mySecConfig = new SecondaryConfig();
        mySecConfig.setDeferredWrite(true);
        mySecConfig.setAllowCreate(true);
        mySecConfig.setSortedDuplicates(true);
        mySecConfig.setKeyCreator(new BerkeleyIntegerNameKeyCreator());
        SecondaryConfig mySecConfigRel = new SecondaryConfig();
        mySecConfigRel.setDeferredWrite(true);
        mySecConfigRel.setAllowCreate(true);
        mySecConfigRel.setSortedDuplicates(true);
        mySecConfigRel.setKeyCreator(new BerkeleyRelationKeyNameKeyCreator());
        myDb = myDbEnvironment.openDatabase(null, BerkeleyConcept.class.getName(), dbConfig);
        databases.put(BerkeleyConcept.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, BerkeleyRelation.class.getName(), dbConfig);
        databases.put(BerkeleyRelation.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, BerkeleyRelationTypeSet.class.getName(), dbConfig);
        databases.put(BerkeleyRelationTypeSet.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, AttributeName.class.getName(), dbConfig);
        databases.put(AttributeName.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, ConceptAccession.class.getName(), dbConfig);
        databases.put(ConceptAccession.class, myDb);
        mySecDb = myDbEnvironment.openSecondaryDatabase(null, "sec" + ConceptAccession.class.getName(), myDb, mySecConfig);
        secDatabases.put(ConceptAccession.class, mySecDb);
        myDb = myDbEnvironment.openDatabase(null, ConceptClass.class.getName(), dbConfig);
        databases.put(ConceptClass.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, ConceptGDS.class.getName(), dbConfig);
        databases.put(ConceptGDS.class, myDb);
        mySecDb = myDbEnvironment.openSecondaryDatabase(null, "sec" + ConceptGDS.class.getName(), myDb, mySecConfig);
        secDatabases.put(ConceptGDS.class, mySecDb);
        myDb = myDbEnvironment.openDatabase(null, ConceptName.class.getName(), dbConfig);
        databases.put(ConceptName.class, myDb);
        mySecDb = myDbEnvironment.openSecondaryDatabase(null, "sec" + ConceptName.class.getName(), myDb, mySecConfig);
        secDatabases.put(ConceptName.class, mySecDb);
        myDb = myDbEnvironment.openDatabase(null, CV.class.getName(), dbConfig);
        databases.put(CV.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, EvidenceType.class.getName(), dbConfig);
        databases.put(EvidenceType.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, RelationGDS.class.getName(), dbConfig);
        databases.put(RelationGDS.class, myDb);
        mySecDb = myDbEnvironment.openSecondaryDatabase(null, "sec" + RelationGDS.class.getName(), myDb, mySecConfigRel);
        secDatabases.put(RelationGDS.class, mySecDb);
        myDb = myDbEnvironment.openDatabase(null, RelationType.class.getName(), dbConfig);
        databases.put(RelationType.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, Unit.class.getName(), dbConfig);
        databases.put(Unit.class, myDb);
    }

    /**
	 * Creates databases containing duplicates.
	 * 
	 * @throws DatabaseException
	 */
    private void setupDupDatabases() throws DatabaseException {
        Database myDb = null;
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDeferredWrite(true);
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        myDb = myDbEnvironment.openDatabase(null, "dup" + RelationType.class.getName(), dbConfig);
        dupDatabases.put(RelationType.class, myDb);
        myDb = myDbEnvironment.openDatabase(null, "dup" + EvidenceType.class.getName(), dbConfig);
        dupDatabases.put(EvidenceType.class, myDb);
    }

    /**
	 * Setup data bindings.
	 * 
	 * @throws DatabaseException
	 */
    private void setupBindings() throws DatabaseException {
        EntryBinding dataBinding = TupleBinding.getPrimitiveBinding(Integer.class);
        bindings.put(Integer.class, dataBinding);
        dataBinding = TupleBinding.getPrimitiveBinding(String.class);
        bindings.put(String.class, dataBinding);
        dataBinding = new BerkeleyRelationKeyNameBinding(this, s);
        bindings.put(BerkeleyRelationKeyName.class, dataBinding);
        dataBinding = new BerkeleyIntegerNameBinding();
        bindings.put(BerkeleyIntegerName.class, dataBinding);
        dataBinding = new RelationKeyBinding(this, s);
        bindings.put(RelationKey.class, dataBinding);
    }

    /**
	 * Returns a DatabaseEntry representation for a given Object.
	 * 
	 * @param o - Object
	 * @return DatabaseEntry
	 */
    public DatabaseEntry convert(Object o) {
        DatabaseEntry theData = new DatabaseEntry();
        bindings.get(o.getClass()).objectToEntry(o, theData);
        return theData;
    }

    /**
	 * Returns index of databases.
	 * 
	 * @return Hashtable<Class,Database>
	 */
    public Hashtable<Class, Database> getDatabases() {
        return databases;
    }

    /**
	 * Returns index of secondary databases.
	 * 
	 * @return Hashtable<Class,SecondaryDatabase>
	 */
    public Hashtable<Class, SecondaryDatabase> getSecDatabases() {
        return secDatabases;
    }

    /**
	 * Returns index of dup databases.
	 * 
	 * @return Hashtable<Class,Database>
	 */
    public Hashtable<Class, Database> getDupDatabases() {
        return dupDatabases;
    }

    /**
	 * Inserts a key/data combination into corresponding database.
	 * 
	 * @param c - Class
	 * @param key - Object
	 * @param data - byte[]
	 */
    public void insertIntoDatabase(Class c, Object key, byte[] data) {
        Database myDb = databases.get(c);
        if (myDb != null) {
            try {
                DatabaseEntry theKey = convert(key);
                DatabaseEntry theData = new DatabaseEntry(data);
                myDb.put(null, theKey, theData);
            } catch (DatabaseException dbe) {
                DatabaseError de = new DatabaseError("[BerkeleyEnv - insertIntoDatabase] " + dbe.getMessage());
                fireEventOccurred(de);
            }
        }
    }

    /**
	 * Inserts a key/data combination into corresponding database.
	 * 
	 * @param c - Class
	 * @param key - Object
	 * @param data - byte[]
	 */
    public void insertIntoDupDatabase(Class c, Object key, byte[] data) {
        Database myDb = dupDatabases.get(c);
        if (myDb != null) {
            try {
                DatabaseEntry theKey = convert(key);
                DatabaseEntry theData = new DatabaseEntry(data);
                myDb.put(null, theKey, theData);
            } catch (DatabaseException dbe) {
                DatabaseError de = new DatabaseError("[BerkeleyEnv - insertIntoDupDatabase] " + dbe.getMessage());
                fireEventOccurred(de);
            }
        }
    }

    /**
	 * Returns value associated with a key in a database for a given class.
	 * 
	 * @param c - Class
	 * @param key - Object
	 * @return byte[]
	 */
    public byte[] getFromDatabase(Class c, Object key) {
        Database myDb = databases.get(c);
        if (myDb != null) {
            try {
                DatabaseEntry theKey = convert(key);
                DatabaseEntry theData = new DatabaseEntry();
                if (myDb.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                    return theData.getData();
                }
            } catch (DatabaseException dbe) {
                DatabaseError de = new DatabaseError("[BerkeleyEnv - getFromDatabase] " + dbe.getMessage());
                fireEventOccurred(de);
            }
        }
        return null;
    }

    /**
	 * Deletes a value from a primary database for a given key and class.
	 * 
	 * @param c - Class
	 * @param key - Object
	 * @return byte[]
	 */
    public byte[] deleteFromDatabase(Class c, Object key) {
        byte[] old = getFromDatabase(c, key);
        Database myDb = databases.get(c);
        if (old != null && myDb != null) {
            try {
                DatabaseEntry theKey = convert(key);
                if (myDb.delete(null, theKey) == OperationStatus.SUCCESS) return old;
            } catch (DatabaseException dbe) {
                DatabaseError de = new DatabaseError("[BerkeleyEnv - deleteFromDatabase] " + dbe.getMessage());
                fireEventOccurred(de);
            }
        }
        return null;
    }

    /**
	 * Deletes a value from a duplicated database for a given key, value and class.
	 * 
	 * @param c - Class
	 * @param key - Object
	 * @param value - byte[]
	 * @return boolean
	 */
    public boolean deleteFromDupDatabase(Class c, Object key, byte[] value) {
        Cursor cursor = null;
        Database myDb = dupDatabases.get(c);
        if (myDb != null) {
            try {
                DatabaseEntry theKey = convert(key);
                DatabaseEntry theData = new DatabaseEntry(value);
                cursor = myDb.openCursor(null, null);
                OperationStatus retVal = cursor.getSearchBoth(theKey, theData, LockMode.DEFAULT);
                if (retVal == OperationStatus.NOTFOUND) {
                    return false;
                } else {
                    retVal = cursor.delete();
                    if (retVal == OperationStatus.SUCCESS) return true; else return false;
                }
            } catch (DatabaseException dbe) {
                DatabaseError de = new DatabaseError("[BerkeleyEnv - deleteFromDupDatabase] " + dbe.getMessage());
                fireEventOccurred(de);
            } finally {
                if (cursor != null) {
                    try {
                        if (cursor != null) cursor.close();
                    } catch (DatabaseException dbe) {
                        DatabaseError de = new DatabaseError("[BerkeleyEnv - deleteFromDupDatabase] " + dbe.getMessage());
                        fireEventOccurred(de);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void commit() {
        try {
            Iterator<SecondaryDatabase> itSec = secDatabases.values().iterator();
            SecondaryDatabase secDB = null;
            while (itSec.hasNext()) {
                secDB = itSec.next();
                secDB.sync();
            }
            Iterator<Database> it = databases.values().iterator();
            Database db = null;
            while (it.hasNext()) {
                db = it.next();
                db.sync();
            }
            it = dupDatabases.values().iterator();
            while (it.hasNext()) {
                db = it.next();
                db.sync();
            }
            if (myDbEnvironment != null) {
                myDbEnvironment.sync();
            }
        } catch (DatabaseException dbe) {
            DatabaseError de = new DatabaseError("[BerkeleyEnv - commit] " + dbe.getMessage());
            fireEventOccurred(de);
        }
    }

    @Override
    public AbstractONDEXGraph getONDEXGraph() {
        return root;
    }

    @Override
    public void cleanup() {
        closeAllBerkeleyAbstractONDEXIterator();
        try {
            Iterator<SecondaryDatabase> itSec = secDatabases.values().iterator();
            SecondaryDatabase secDB = null;
            while (itSec.hasNext()) {
                secDB = itSec.next();
                secDB.sync();
                secDB.close();
            }
            Iterator<Database> it = databases.values().iterator();
            Database db = null;
            while (it.hasNext()) {
                db = it.next();
                db.sync();
                db.close();
            }
            it = dupDatabases.values().iterator();
            while (it.hasNext()) {
                db = it.next();
                db.sync();
                db.close();
            }
            if (myDbEnvironment != null) {
                myDbEnvironment.sync();
                myDbEnvironment.cleanLog();
                myDbEnvironment.close();
            }
        } catch (DatabaseException dbe) {
            DatabaseError de = new DatabaseError("[BerkeleyEnv - cleanup] " + dbe.getMessage());
            fireEventOccurred(de);
        }
    }

    public void propagateEventOccurred(EventType et) {
        fireEventOccurred(et);
    }

    public void addBerkeleyAbstractONDEXIterator(BerkeleyAbstractONDEXIterator i) {
        berkeleyIterators.add(i);
    }

    public void removeBerkeleyAbstractONDEXIterator(BerkeleyAbstractONDEXIterator i) {
        berkeleyIterators.remove(i);
    }

    public void closeAllBerkeleyAbstractONDEXIterator() {
        Iterator<BerkeleyAbstractONDEXIterator> it = berkeleyIterators.iterator();
        while (it.hasNext()) {
            it.next().close();
        }
        berkeleyIterators.clear();
    }
}
