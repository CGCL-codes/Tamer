package org.nakedobjects.object.persistence;

import org.nakedobjects.container.configuration.ComponentException;
import org.nakedobjects.container.configuration.ConfigurationException;
import org.nakedobjects.object.NakedClass;
import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.utility.Logger;

public class ObjectStoreLogger extends Logger implements NakedObjectStore {

    private final NakedObjectStore decorated;

    public ObjectStoreLogger(final NakedObjectStore decorated, final String logFileName) {
        super(logFileName, false);
        this.decorated = decorated;
    }

    public ObjectStoreLogger(final NakedObjectStore decorated) {
        super(null, true);
        this.decorated = decorated;
    }

    public void abortTransaction() throws ObjectManagerException {
        log("Abort transaction started");
        decorated.abortTransaction();
        log("Abort transaction complete");
    }

    public CreateObjectCommand createCreateObjectCommand(NakedObject object) {
        log("Create object " + object);
        return decorated.createCreateObjectCommand(object);
    }

    public DestroyObjectCommand createDestroyObjectCommand(NakedObject object) {
        log("Destroy object " + object);
        return decorated.createDestroyObjectCommand(object);
    }

    public SaveObjectCommand createSaveObjectCommand(NakedObject object) {
        log("Save object " + object);
        return decorated.createSaveObjectCommand(object);
    }

    public void endTransaction() throws ObjectManagerException {
        log("End transaction");
        decorated.endTransaction();
    }

    public String getDebugData() {
        return decorated.getDebugData();
    }

    public String getDebugTitle() {
        return decorated.getDebugTitle();
    }

    protected Class getDecoratedClass() {
        return decorated.getClass();
    }

    public NakedObject[] getInstances(InstancesCriteria criteria) throws ObjectManagerException, UnsupportedFindException {
        log("Get instances matching " + criteria);
        return decorated.getInstances(criteria);
    }

    public NakedObject[] getInstances(NakedObjectSpecification specification, boolean includeSubclasses) throws ObjectManagerException {
        log("Get instances of " + specification.getShortName());
        return decorated.getInstances(specification, includeSubclasses);
    }

    public NakedClass getNakedClass(String name) throws ObjectNotFoundException, ObjectManagerException {
        NakedClass cls = decorated.getNakedClass(name);
        log("Get class " + name, cls);
        return cls;
    }

    public NakedObject getObject(Oid oid, NakedObjectSpecification hint) throws ObjectNotFoundException, ObjectManagerException {
        NakedObject object = decorated.getObject(oid, hint);
        log("Get object for " + oid + " (of type " + hint.getShortName() + ")", object.getObject());
        return object;
    }

    public boolean hasInstances(NakedObjectSpecification specification, boolean includeSubclasses) throws ObjectManagerException {
        boolean hasInstances = decorated.hasInstances(specification, includeSubclasses);
        log("Has instances of " + specification.getShortName(), "" + hasInstances);
        return hasInstances;
    }

    public void init() throws ConfigurationException, ComponentException, ObjectManagerException {
        log("Initialising " + name());
        decorated.init();
    }

    public String name() {
        return decorated.name();
    }

    public int numberOfInstances(NakedObjectSpecification specification, boolean includedSubclasses) throws ObjectManagerException {
        int number = decorated.numberOfInstances(specification, includedSubclasses);
        log("Number of instances of " + specification.getShortName(), "" + number);
        return number;
    }

    public void reset() {
        log("Reset");
        decorated.reset();
    }

    public void resolveField(NakedObject object, NakedObjectField field) throws ObjectManagerException {
        log("Resolve eagerly object in field " + field + " of " + object);
        decorated.resolveField(object, field);
    }

    public void resolveImmediately(NakedObject object) throws ObjectManagerException {
        log("Resolve immediately: " + object);
        decorated.resolveImmediately(object);
    }

    public void runTransaction(PersistenceCommand[] commands) throws ObjectManagerException {
        log("Run transactions");
        for (int i = 0; i < commands.length; i++) {
            log("  " + i + " " + commands[i]);
        }
        decorated.runTransaction(commands);
    }

    public void shutdown() throws ObjectManagerException {
        log("Shutting down " + decorated);
        decorated.shutdown();
        close();
    }

    public void startTransaction() throws ObjectManagerException {
        log("Start transaction");
        decorated.startTransaction();
    }
}
