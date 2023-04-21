package de.mguennewig.pobjects;

import java.util.*;
import de.mguennewig.pobjects.metadata.*;

/** Abstract base class for database connections.
 *
 * @author Michael G�nnewig
 */
public abstract class AbstractContainer extends Object implements Container {

    private final Map<String, ClassDecl> classDict;

    private final PObjDictionary dictionary;

    private final String schema;

    /** A list of objects which have changed in-memory.
   *
   * <p>Objects may be listed multiple times if multiple changes have been made
   * to it.</p>
   */
    private final List<PObject> dirtyObjects;

    /**
   * A list of objects whose changes have been send to the DBMS within the
   * current transaction by {@link PObject#store()}.
   */
    protected final List<PObject> transactionObjects;

    /**
   * Specifies whether an exception should be thrown if there exist an modified
   * persistent object that has not been stored at the time of an commit.
   */
    private boolean strictMode;

    /** Creates a container without a connection to any DBMS.
   *
   * @throws NullPointerException if {@code null} is passed for the dictionary.
   */
    public AbstractContainer(final PObjDictionary dict, final String schema) {
        super();
        if (dict == null) throw new NullPointerException("dict");
        this.classDict = new LinkedHashMap<String, ClassDecl>();
        this.dirtyObjects = new ArrayList<PObject>();
        this.transactionObjects = new ArrayList<PObject>();
        this.dictionary = dict;
        this.schema = schema;
        this.strictMode = false;
    }

    /** {@inheritDoc} */
    public final boolean supportsBoolean() {
        return (getCapabilities() & SUPPORTS_BOOLEAN) != 0;
    }

    /** {@inheritDoc} */
    public final boolean supportsInherits() {
        return (getCapabilities() & SUPPORTS_INHERITS) != 0;
    }

    /** {@inheritDoc} */
    public final boolean supportsLimitOffset() {
        return (getCapabilities() & SUPPORTS_LIMIT_OFFSET) != 0;
    }

    /** {@inheritDoc} */
    public final boolean supportsOracleJoin() {
        return (getCapabilities() & SUPPORTS_ORACLE_JOIN) != 0;
    }

    /** {@inheritDoc} */
    public final boolean supportsSQL99Join() {
        return (getCapabilities() & SUPPORTS_SQL99_JOIN) != 0;
    }

    /** {@inheritDoc} */
    public final boolean useStreamToInsertLob() {
        return (getCapabilities() & USE_STREAM_TO_INSERT_LOB) != 0;
    }

    /** {@inheritDoc} */
    public final boolean useArrayForLob() {
        return (getCapabilities() & USE_ARRAY_FOR_LOB) != 0;
    }

    /** {@inheritDoc} */
    public final PObjDictionary getDictionary() {
        return dictionary;
    }

    /** {@inheritDoc} */
    public final String getSchema() {
        return schema;
    }

    /** {@inheritDoc} */
    public String getQualifiedName(final SqlEntity entity) {
        final String modulePrefix = entity.getModule().getSchemaPrefix();
        if (modulePrefix != null) {
            if ("".equals(modulePrefix)) return entity.getSchemaName();
            return modulePrefix + "." + entity.getSchemaName();
        } else if (schema != null && schema.length() > 0) {
            return schema + "." + entity.getSchemaName();
        } else {
            return entity.getSchemaName();
        }
    }

    /** {@inheritDoc} */
    public final boolean isStrictMode() {
        return strictMode;
    }

    /** {@inheritDoc} */
    public void setStrictMode(final boolean strictMode) {
        this.strictMode = strictMode;
    }

    /** {@inheritDoc} */
    public TableExpr getTableExpr(final Class<? extends Record> tableClass) {
        if (tableClass == null) throw new IllegalArgumentException("tableClass==null");
        final String packageName = tableClass.getPackage().getName();
        final Module module = this.dictionary.getModuleForPackage(packageName);
        if (module != null) {
            int len = packageName.length();
            if (len > 0) len++;
            return module.getTableExpr(tableClass.getName().substring(len));
        }
        return null;
    }

    /** {@inheritDoc} */
    public ClassDecl getClassDecl(final String schemaName) {
        if (classDict.containsKey(schemaName)) return classDict.get(schemaName);
        return null;
    }

    /** {@inheritDoc} */
    public final Iterator<ClassDecl> getClassDecls() {
        return Collections.unmodifiableCollection(classDict.values()).iterator();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unused")
    public void beginTransaction() throws PObjSQLException {
        if (transactionObjects.size() != 0) throw new IllegalStateException("Still a transaction open!");
    }

    /** {@inheritDoc} */
    public void commitTransaction() throws PObjSQLException {
        if (!dirtyObjects.isEmpty()) {
            if (isStrictMode()) {
                throw new PObjSQLException("Modified but unstored object detected: " + PObject.getKey(dirtyObjects.get(0)));
            }
            System.err.print("WARNING: Modified but not stored: ");
            for (PObject obj : dirtyObjects) System.err.print(PObject.getKey(obj) + ", ");
            System.err.println();
        }
        for (final PObject obj : transactionObjects) obj.setPObjState(PObject.STATE_COMMITED);
        transactionObjects.clear();
    }

    /** {@inheritDoc} */
    public void rollbackTransaction() throws PObjSQLException {
        for (final PObject obj : transactionObjects) obj.revert();
        transactionObjects.clear();
    }

    /** {@inheritDoc} */
    public void reset() {
        dirtyObjects.clear();
        try {
            rollbackTransaction();
        } catch (PObjSQLException e) {
            System.err.println("Failed to rollback transaction on reset: " + e.getMessage());
        }
    }

    /** {@inheritDoc} */
    public Record createObject(final TableExpr te) {
        try {
            final Class<?> c = Class.forName(te.getClassName(), true, this.dictionary.getClassLoader());
            return (Record) c.newInstance();
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InstantiationException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof Error) throw (Error) cause;
            if (cause instanceof RuntimeException) throw (RuntimeException) cause;
            throw new Error("Unexpected checked exception", cause);
        }
    }

    /** {@inheritDoc} */
    public Record getData(final TableExpr te) {
        if (te instanceof DynamicSelectExpr) return new DynamicRecord((DynamicSelectExpr) te);
        if (te.isWritableClass()) {
            throw new IllegalArgumentException("Class declarations with id field are not supported.");
        }
        return createObject(te);
    }

    /** {@inheritDoc} */
    public PObject getObject(final ClassDecl te, final String id) {
        if (te.getIdField() == null) {
            throw new IllegalArgumentException("Class declarations without id field are not supported.");
        }
        if (id == null) return null;
        final PObject obj = (PObject) createObject(te);
        obj.setId(id);
        obj.setPObjState(PObject.STATE_REFERENCE);
        obj.setContainer(this);
        return obj;
    }

    /** {@inheritDoc} */
    public PObject getObject(final Class<? extends PObject> tableClass, final String id) {
        if (!PObject.class.isInstance(tableClass)) throw new IllegalArgumentException(tableClass + " is not a PObject class");
        return getObject((ClassDecl) getTableExpr(tableClass), id);
    }

    /** {@inheritDoc} */
    public void makePersistent(final PObject obj) {
        if (obj.getContainer() != null) throw new IllegalStateException("object already attached to a container");
        final ClassDecl cdecl = obj.getClassDecl();
        for (int i = 0; i < cdecl.getNumFields(); i++) {
            final Field field = cdecl.getField(i);
            final Object value = obj.get(field.getIndex());
            if (value instanceof PObject) {
                final PObject ref = (PObject) value;
                if (ref == obj) {
                } else if (!ref.isPersistent()) {
                    throw new IllegalStateException("field `" + field.getName() + "' refers to non-persistent object");
                } else if (ref.getContainer() != this) {
                    throw new IllegalStateException("field `" + field.getName() + "' refers to an object from another container");
                }
            }
        }
        obj.setContainer(this);
        obj.setPObjState(PObject.STATE_NEW_OBJECT);
    }

    /** {@inheritDoc} */
    public void notifyChange(final PObject obj, final boolean reverted) {
        if (reverted) dirtyObjects.remove(obj); else dirtyObjects.add(obj);
    }

    /** {@inheritDoc} */
    public final void storeAll() throws PObjConstraintException, PObjSQLException {
        final PObject[] list = dirtyObjects.toArray(new PObject[dirtyObjects.size()]);
        for (int i = 0; i < list.length; i++) list[i].store();
    }

    /** {@inheritDoc} */
    public final int deleteAll(final Class<? extends Record> tableClass) throws PObjSQLException, PObjConstraintException {
        final TableExpr te = getTableExpr(tableClass);
        if (te instanceof ClassDecl) return deleteAll((ClassDecl) te);
        throw new IllegalArgumentException(tableClass + " does not represent a persistent table expression");
    }

    /** {@inheritDoc} */
    public final Query newQuery(final TableExpr tableExpr) {
        final Query q = newQuery();
        q.addTableExpr(tableExpr, true);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final TableExpr tableExpr, final Predicate filter) {
        final Query q = newQuery();
        q.addTableExpr(tableExpr, true);
        q.addConj(filter);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final TableExpr tableExpr, final Predicate[] filters) {
        final Query q = newQuery();
        q.addTableExpr(tableExpr, true);
        for (int i = 0; i < filters.length; i++) q.addConj(filters[i]);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final TableExpr[] tableExprs, final Predicate[] filters) {
        final Query q = newQuery();
        for (int i = 0; i < tableExprs.length; i++) q.addTableExpr(tableExprs[i], (i == 0));
        for (int i = 0; i < filters.length; i++) q.addConj(filters[i]);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final Class<? extends Record> tableClass) {
        return newQuery(getTableExpr(tableClass));
    }

    /** {@inheritDoc} */
    public final Query newQuery(final Class<? extends Record> tableClass, final Predicate filter) {
        final Query q = newQuery();
        q.addTableExpr(tableClass, true);
        q.addConj(filter);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final Class<? extends Record> tableClass, final Predicate[] filters) {
        final Query q = newQuery();
        q.addTableExpr(tableClass, true);
        for (int i = 0; i < filters.length; i++) q.addConj(filters[i]);
        return q;
    }

    /** {@inheritDoc} */
    public final Query newQuery(final Class<? extends Record>[] tableClasses, final Predicate[] filters) {
        final Query q = newQuery();
        for (int i = 0; i < tableClasses.length; i++) q.addTableExpr(tableClasses[i], (i == 0));
        for (int i = 0; i < filters.length; i++) q.addConj(filters[i]);
        return q;
    }

    /** Registers all table expressions from all modules for usage.
   *
   * @see #getDictionary()
   * @see #registerClass(ClassDecl)
   */
    protected final void registerDictionary() {
        for (int i = 0; i < this.dictionary.getNumModules(); i++) registerModule(this.dictionary.getModule(i));
    }

    /** Registers all table expressions from the module for usage.
   *
   * @see #registerClass(ClassDecl)
   */
    private void registerModule(final Module module) {
        for (int i = 0; i < module.getNumTableExprs(); i++) {
            final TableExpr te = module.getTableExpr(i);
            if (te instanceof ClassDecl) registerClass((ClassDecl) te);
        }
    }

    /** Registers the table expression for usage.
   *
   * @see #getDictionary()
   * @see #registerClass(ClassDecl)
   */
    protected void registerClass(final ClassDecl te) {
        classDict.put(te.getSchemaName(), te);
    }

    /** Sets the container of the PObject instance. */
    protected final void setPObjContainer(final PObject obj) {
        obj.setContainer(this);
    }

    /** Sets the internal state of the PObject instance. */
    protected static void setPObjState(final PObject obj, final int state) {
        obj.setPObjState(state);
    }
}
