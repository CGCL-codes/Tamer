package org.nakedobjects.nof.persist.objectstore.inmemory;

import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedReference;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.persist.InstancesCriteria;
import org.nakedobjects.noa.persist.ObjectNotFoundException;
import org.nakedobjects.noa.persist.ObjectPersistenceException;
import org.nakedobjects.noa.persist.UnsupportedFindException;
import org.nakedobjects.noa.reflect.NakedObjectField;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.util.Debug;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.persist.objectstore.NakedObjectStore;
import org.nakedobjects.nof.persist.transaction.CreateObjectCommand;
import org.nakedobjects.nof.persist.transaction.DestroyObjectCommand;
import org.nakedobjects.nof.persist.transaction.ExecutionContext;
import org.nakedobjects.nof.persist.transaction.PersistenceCommand;
import org.nakedobjects.nof.persist.transaction.SaveObjectCommand;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;

public class MemoryObjectStore implements NakedObjectStore {

    private static final Logger LOG = Logger.getLogger(MemoryObjectStore.class);

    protected Hashtable instances;

    public MemoryObjectStore() {
        LOG.info("creating object store");
        instances = new Hashtable();
    }

    public void abortTransaction() {
        LOG.debug("transaction aborted");
    }

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new CreateObjectCommand() {

            public void execute(final ExecutionContext context) throws ObjectPersistenceException {
                LOG.debug("  create object " + object);
                save(object);
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "CreateObjectCommand [object=" + object + "]";
            }
        };
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        return new DestroyObjectCommand() {

            public void execute(final ExecutionContext context) throws ObjectPersistenceException {
                LOG.info("  delete object '" + object + "'");
                destroy(object);
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "DestroyObjectCommand [object=" + object + "]";
            }
        };
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        return new SaveObjectCommand() {

            public void execute(final ExecutionContext context) throws ObjectPersistenceException {
                save(object);
            }

            public NakedObject onObject() {
                return object;
            }

            public String toString() {
                return "SaveObjectCommand [object=" + object + "]";
            }
        };
    }

    private String debugCollectionGraph(final NakedCollection collection, final int level, final Vector recursiveElements) {
        StringBuffer s = new StringBuffer();
        if (recursiveElements.contains(collection)) {
            s.append("*\n");
        } else {
            recursiveElements.addElement(collection);
            Enumeration e = ((NakedCollection) collection).elements();
            while (e.hasMoreElements()) {
                indent(s, level);
                NakedObject element;
                try {
                    element = ((NakedObject) e.nextElement());
                } catch (ClassCastException ex) {
                    LOG.error(ex);
                    return s.toString();
                }
                s.append(element);
                s.append(debugGraph(element, level + 1, recursiveElements));
            }
        }
        return s.toString();
    }

    public void debugData(final DebugString debug) {
        debug.appendTitle("Business Objects");
        Enumeration e = instances.keys();
        while (e.hasMoreElements()) {
            NakedObjectSpecification spec = (NakedObjectSpecification) e.nextElement();
            debug.appendln(spec.getFullName());
            MemoryObjectStoreInstances instances = instancesFor(spec);
            Enumeration f = instances.elements();
            debug.indent();
            if (!f.hasMoreElements()) {
                debug.appendln("no instances");
            }
            while (f.hasMoreElements()) {
                debug.appendln(f.nextElement().toString());
            }
        }
        debug.unindent();
        debug.appendln();
        debug.appendTitle("Object graphs");
        Vector dump = new Vector();
        e = instances.keys();
        while (e.hasMoreElements()) {
            NakedObjectSpecification spec = (NakedObjectSpecification) e.nextElement();
            MemoryObjectStoreInstances instances = instancesFor(spec);
            Enumeration f = instances.elements();
            while (f.hasMoreElements()) {
                NakedObject object = (NakedObject) f.nextElement();
                debug.append(spec.getFullName());
                debug.append(": ");
                debug.append(object);
                debug.appendln(debugGraph(object, 0, dump));
            }
        }
    }

    private String debugGraph(final NakedObject object, final int level, final Vector recursiveElements) {
        if (level > 3) {
            return "...\n";
        }
        Vector elements;
        if (recursiveElements == null) {
            elements = new Vector(25, 10);
        } else {
            elements = recursiveElements;
        }
        if (object instanceof NakedCollection) {
            return "\n" + debugCollectionGraph((NakedCollection) object, level, elements);
        } else {
            return "\n" + debugObjectGraph(object, level, elements);
        }
    }

    private String debugObjectGraph(final NakedObject object, final int level, final Vector recursiveElements) {
        StringBuffer s = new StringBuffer();
        recursiveElements.addElement(object);
        NakedObjectField[] fields;
        fields = object.getSpecification().getFields();
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField field = fields[i];
            Object obj = field.get(object);
            String id = field.getId();
            indent(s, level);
            if (field.isCollection()) {
                s.append(id + ": \n" + debugCollectionGraph((NakedCollection) obj, level + 1, recursiveElements));
            } else {
                if (obj instanceof NakedObject) {
                    if (recursiveElements.contains(obj)) {
                        s.append(id + ": " + obj + "*\n");
                    } else {
                        s.append(id + ": " + obj);
                        s.append(debugGraph((NakedObject) obj, level + 1, recursiveElements));
                    }
                } else {
                    s.append(id + ": " + obj);
                    s.append("\n");
                }
            }
        }
        return s.toString();
    }

    public String debugTitle() {
        return name();
    }

    private void destroy(final NakedObject object) {
        NakedObjectSpecification specification = object.getSpecification();
        LOG.debug("   destroy object " + object + " as instance of " + specification.getShortName());
        MemoryObjectStoreInstances ins = instancesFor(specification);
        ins.remove(object.getOid());
    }

    public void endTransaction() {
        LOG.debug("end transaction");
    }

    public void execute(final PersistenceCommand[] commands) throws ObjectPersistenceException {
        LOG.info("start execution of transaction ");
        for (int i = 0; i < commands.length; i++) {
            commands[i].execute(null);
        }
        LOG.info("end execution");
    }

    protected void finalize() throws Throwable {
        super.finalize();
        LOG.info("finalizing object store");
    }

    public NakedObject[] getInstances(final InstancesCriteria criteria) throws ObjectPersistenceException, UnsupportedFindException {
        Vector instances = new Vector();
        getInstances(criteria, instances);
        return toInstancesArray(instances);
    }

    private void getInstances(final InstancesCriteria criteria, final Vector instances) {
        NakedObjectSpecification spec = criteria.getSpecification();
        instancesFor(spec).instances(criteria, instances);
        if (criteria.includeSubclasses()) {
            NakedObjectSpecification[] subclasses = spec.subclasses();
            for (int i = 0; i < subclasses.length; i++) {
                getInstances(subclasses[i], instances, true);
            }
        }
    }

    private void getInstances(final NakedObjectSpecification spec, final Vector instances, final boolean includeSubclasses) {
        instancesFor(spec).instances(instances);
        if (includeSubclasses) {
            NakedObjectSpecification[] subclasses = spec.subclasses();
            for (int i = 0; i < subclasses.length; i++) {
                getInstances(subclasses[i], instances, true);
            }
        }
    }

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) throws ObjectNotFoundException, ObjectPersistenceException {
        LOG.debug("getObject " + oid);
        MemoryObjectStoreInstances ins = instancesFor(hint);
        NakedObject object = ins.getObject(oid);
        if (object == null) {
            throw new ObjectNotFoundException(oid);
        } else {
            setupReferencedObjects(object);
            return object;
        }
    }

    public Oid getOidForService(String name) {
        return null;
    }

    public boolean hasInstances(final NakedObjectSpecification spec, final boolean includeSubclasses) {
        if (instancesFor(spec).hasInstances()) {
            return true;
        }
        if (includeSubclasses) {
            NakedObjectSpecification[] subclasses = spec.subclasses();
            for (int i = 0; i < subclasses.length; i++) {
                if (hasInstances(subclasses[i], includeSubclasses)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInitialized() {
        return false;
    }

    private void indent(final StringBuffer s, final int level) {
        for (int indent = 0; indent < level; indent++) {
            s.append(Debug.indentString(4) + "|");
        }
        s.append(Debug.indentString(4) + "+--");
    }

    public void init() {
    }

    private MemoryObjectStoreInstances instancesFor(final NakedObjectSpecification spec) {
        MemoryObjectStoreInstances ins = (MemoryObjectStoreInstances) instances.get(spec);
        if (ins == null) {
            ins = new MemoryObjectStoreInstances(NakedObjectsContext.getObjectLoader());
            instances.put(spec, ins);
        }
        return ins;
    }

    public String name() {
        return "Transient Object Store";
    }

    public void registerService(String name, Oid oid) {
    }

    public void reset() {
        NakedObjectsContext.getObjectLoader().reset();
        Enumeration e = instances.elements();
        while (e.hasMoreElements()) {
            MemoryObjectStoreInstances element = (MemoryObjectStoreInstances) e.nextElement();
            element.reset();
        }
    }

    public void resolveField(final NakedObject object, final NakedObjectField field) throws ObjectPersistenceException {
        NakedReference reference = (NakedReference) field.get(object);
        NakedObjectsContext.getObjectLoader().start(reference, ResolveState.RESOLVING);
        NakedObjectsContext.getObjectLoader().end(reference);
    }

    public void resolveImmediately(final NakedObject object) throws ObjectPersistenceException {
        LOG.debug("resolve " + object);
        setupReferencedObjects(object);
        NakedObjectsContext.getObjectLoader().start(object, ResolveState.RESOLVING);
        NakedObjectsContext.getObjectLoader().end(object);
    }

    private void save(final NakedObject object) throws ObjectPersistenceException {
        NakedObjectSpecification specification = object.getSpecification();
        LOG.debug("   saving object " + object + " as instance of " + specification.getShortName());
        MemoryObjectStoreInstances ins = instancesFor(specification);
        ins.save(object);
    }

    private void setupReferencedObjects(final NakedObject object) {
        setupReferencedObjects(object, new Vector());
    }

    private void setupReferencedObjects(final NakedObject object, final Vector all) {
        if (true) return;
        if (object == null || all.contains(object)) {
            return;
        }
        all.addElement(object);
        NakedObjectsContext.getObjectLoader().start(object, ResolveState.RESOLVING);
        NakedObjectField[] fields = object.getSpecification().getFields();
        for (int i = 0; i < fields.length; i++) {
            NakedObjectField field = fields[i];
            if (field.isCollection()) {
                NakedCollection col = (NakedCollection) field.get(object);
                for (Enumeration e = col.elements(); e.hasMoreElements(); ) {
                    NakedObject element = (NakedObject) e.nextElement();
                    setupReferencedObjects(element, all);
                }
            } else if (field.isObject()) {
                NakedObject fieldContent = (NakedObject) field.get(object);
                setupReferencedObjects(fieldContent, all);
            }
        }
        NakedObjectsContext.getObjectLoader().end(object);
    }

    public void shutdown() {
        LOG.info("shutdown " + this);
        for (Enumeration e = instances.elements(); e.hasMoreElements(); ) {
            MemoryObjectStoreInstances inst = (MemoryObjectStoreInstances) e.nextElement();
            inst.shutdown();
        }
        instances.clear();
    }

    public void startTransaction() {
        LOG.debug("start transaction");
    }

    private NakedObject[] toInstancesArray(final Vector instances) {
        NakedObject[] ins = new NakedObject[instances.size()];
        for (int i = 0; i < ins.length; i++) {
            NakedObject object = (NakedObject) instances.elementAt(i);
            setupReferencedObjects(object);
            if (object.getResolveState().isResolvable(ResolveState.RESOLVING)) {
                NakedObjectsContext.getObjectLoader().start(object, ResolveState.RESOLVING);
                NakedObjectsContext.getObjectLoader().end(object);
            }
            ins[i] = object;
        }
        return ins;
    }

    public boolean flush(PersistenceCommand[] commands) {
        return false;
    }
}
