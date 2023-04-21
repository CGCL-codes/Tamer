package com.xavax.xstore;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.sql.*;
import org.apache.log4j.Logger;
import com.xavax.xstore.exception.*;
import com.xavax.xstore.util.CollectionFactory;

/**
 * ClassMap encapsulates the metadata required to persist a class and
 * performs the actual persistence operations using pools of prepared
 * statements created for the class.  The metadata includes the names
 * of the interface, proxy class, and implementation class; the table
 * name and column names for the key fields; and collections of
 * AttributeMaps and AssociationMaps.
 */
public class ClassMap<T> extends PersistenceMap {

    /**
   * Construct a ClassMap.  Find the Class objects for the proxy class,
   * implementation class, and public interface.  Create a statement
   * manager for the prepared statements used by this class.
   *
   * @param pm  the persistence manager.
   * @param name  the name of the public interface to this class.
   * @param implName  the name of the implementation class.
   * @param proxyName  the name of the proxy class.
   * @param id  the identifier for this class.
   * @param parent  the class map for the parent class.
   * @param table  the table map for the table for this class.
   * @param keyDB  the column map for the column containing the database ID.
   * @param keyClass the column map for the column containing the class ID.
   * @param keyOID  the column map for the column containing the OID.
   * @throws PersistenceException if the interface, implementation class,
   *         or proxy class cannot be loaded.
   */
    ClassMap(PersistenceManager pm, String name, String implName, String proxyName, short id, ClassMap<? super T> parent, TableMap table, ColumnMap keyDB, ColumnMap keyClass, ColumnMap keyOID) throws PersistenceException {
        super(pm, name);
        _logger = Logger.getLogger(ClassMap.class);
        _classID = id;
        _key = new Short(id);
        _parent = parent;
        _implName = implName;
        _proxyName = proxyName;
        int i = _name.lastIndexOf('.');
        _basename = (i >= 0) ? _name.substring(i + 1) : _name;
        _hasComplexAssociations = false;
        _hasDependents = false;
        _keyDB = keyDB;
        _keyClass = keyClass;
        _keyOID = keyOID;
        _table = table;
        _tables = CollectionFactory.arrayList();
        _factory = new ProxyFactory<T>(pm, proxyName);
        _attributes = CollectionFactory.treeMap();
        _attrVector = CollectionFactory.vector();
        _associations = CollectionFactory.treeMap();
        _assocVector = CollectionFactory.vector();
        _assocTables = CollectionFactory.vector();
        try {
            _interface = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(pm, _msg[2], e);
        }
        try {
            _implClass = Class.forName(implName);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException(pm, _msg[3], e);
        }
        _smgr = new StatementManager();
    }

    /**
   * Init is called after all metadata is loaded.  Init builds the list
   * of databases that can support an object of this class and creates
   * all of the prepared statements needed to delete, insert, select,
   * and update objects of this class.
   */
    void init() {
        if (_parent != null) {
            _level = _parent._level;
            _top = _parent.top();
            _tables.addAll(_parent._tables);
            _hasComplexAssociations = _parent._hasComplexAssociations;
        } else {
            _level = 0;
            _top = this;
        }
        if (_table != null) {
            _tables.add(_table);
            _level++;
        }
        checkDatabases();
        buildSelect();
        buildDelete();
        buildInsert();
        buildUpdate();
    }

    /**
   * Returns true if the specified database supports this class.
   *
   * @return true if the specified database supports this class.
   */
    public boolean checkDatabase(DatabaseMap dmap) {
        return dmap.contains(_tables) && dmap.contains(_assocTables);
    }

    /**
   * Iterate over the databases registered with the persistence manager
   * testing each database to see if it contains the set of tables needed
   * to support an instance of this class.  This function is called by
   * init after the metadata is loaded and whenever a table is added.
   */
    void checkDatabases() {
        _databases = CollectionFactory.treeMap();
        for (DatabaseMap dmap : _pm.databases()) {
            if (dmap.contains(_tables) && dmap.contains(_assocTables)) {
                _databases.put(dmap.key(), dmap);
            }
        }
    }

    /**
   * Build the SQL statement needed to select objects of this class.
   * Prepared statements will be created from this string as needed
   * by the statement manager.  The select clause, from clause, and
   * portions of the where clause are also saved for later use by
   * the ad hoc query generator.  As a side effect, this function
   * also initializes the position of each attribute and association
   * in the select clause.
   */
    void buildSelect() {
        StringBuffer s1 = new StringBuffer();
        if (_parent != null) {
            s1.append(_parent._selectClause);
            _position = _parent._position;
            _hasDependents = _parent._hasDependents;
        } else {
            addSelectTerm(s1, 1, _keyDB, false);
            addSelectTerm(s1, 1, _keyClass, true);
            addSelectTerm(s1, 1, _keyOID, true);
            _position = 4;
        }
        for (AttributeMap amap : _attrVector) {
            ColumnMap cmap = amap.column();
            addSelectTerm(s1, _level, cmap, true);
            amap.position(_position++);
            amap.tableIndex(_level);
        }
        for (AssociationMap amap : _assocVector) {
            if (amap.dependent()) {
                _hasDependents = true;
            }
            if (amap.multiplicity()) {
                _hasComplexAssociations = true;
            } else {
                addSelectTerm(s1, _level, amap.destinationDB(), true);
                addSelectTerm(s1, _level, amap.destinationClass(), true);
                addSelectTerm(s1, _level, amap.destinationOID(), true);
                amap.position(_position);
                _position += 3;
            }
        }
        _selectClause = s1.toString();
        StringBuffer s2 = new StringBuffer(" from ");
        int n;
        for (n = 0; n < _level; ++n) {
            if (n > 0) {
                s2.append(", ");
            }
            TableMap tmap = (TableMap) _tables.get(n);
            s2.append(tmap.name() + " t" + (n + 1));
        }
        _fromClause = s2.toString();
        StringBuffer s3 = new StringBuffer(" where ");
        if (_tables.size() > 1) {
            for (n = 1; n < _level; ++n) {
                s3.append("t1.oid = t" + (n + 1) + ".oid and ");
            }
        }
        _whereClause = s3.toString();
        s1.insert(0, "select ");
        s1.append(_fromClause).append(_whereClause);
        _selectStmt = s1.toString();
        s1.append("t1.").append(_top._keyOID.name()).append(" = ?");
        String s = s1.toString();
        logStatement(s);
        _smgr.selectStatement(s);
        _enableUpdate = (_parent == null && _position > 4) || (_parent != null && _position > _parent._position);
        StringBuffer s4 = new StringBuffer("select ");
        addSelectTerm(s4, 1, _top._keyDB, false);
        addSelectTerm(s4, 1, _top._keyClass, true);
        addSelectTerm(s4, 1, _top._keyOID, true);
        s4.append(_fromClause).append(_whereClause);
        _query = s4.toString();
        logStatement(_query);
    }

    /**
   * Add a select term to the string buffer being used to assemble
   * a select statement.
   *
   * @param s  the string buffer.
   * @param n  the table number for the table alias name.
   * @param cmap  the column map for this select term.
   * @param flag  true if a leading comma should be prepended.
   */
    private void addSelectTerm(StringBuffer s, int n, ColumnMap cmap, boolean flag) {
        if (flag) {
            s.append(", ");
        }
        s.append("t").append(n).append(".").append(cmap.name());
    }

    /**
   * Build the SQL statement to delete objects of this class.
   */
    private void buildDelete() {
        if (_table != null) {
            String query = "delete from " + _table.name() + " where oid = ?";
            logStatement(query);
            _smgr.deleteStatement(query);
        }
    }

    /**
   * Build the SQL statement to insert objects of this class.
   */
    private void buildInsert() {
        if (_table != null) {
            StringBuffer s1 = new StringBuffer();
            StringBuffer s2 = new StringBuffer();
            addInsertTerm(s1, _keyDB, false);
            addInsertTerm(s1, _keyClass, true);
            addInsertTerm(s1, _keyOID, true);
            s2.append("?, ?, ?");
            for (AttributeMap amap : _attrVector) {
                addInsertTerm(s1, amap.column(), true);
                s2.append(", ?");
            }
            for (AssociationMap amap : _assocVector) {
                if (!amap.multiplicity()) {
                    addInsertTerm(s1, amap.destinationDB(), true);
                    addInsertTerm(s1, amap.destinationClass(), true);
                    addInsertTerm(s1, amap.destinationOID(), true);
                    s2.append(", ?, ?, ?");
                }
            }
            String query = "insert into " + _table.name() + " (" + s1.toString() + ") values (" + s2.toString() + ")";
            _smgr.insertStatement(query);
            logStatement(query);
        }
    }

    /**
   * Add an insert term to the string buffer being used to assemble
   * an insert statement.
   *
   * @param s  the string buffer.
   * @param cmap  the column map for this select term.
   * @param flag  true if a leading comma should be prepended.
   */
    private void addInsertTerm(StringBuffer s, ColumnMap cmap, boolean flag) {
        if (flag) {
            s.append(", ");
        }
        s.append(cmap.name());
    }

    /**
   * Build the SQL statement to update objects of this class.
   */
    private void buildUpdate() {
        if (_enableUpdate) {
            boolean first = true;
            StringBuffer s1 = new StringBuffer();
            for (AttributeMap amap : _attrVector) {
                if (first) {
                    first = false;
                } else {
                    s1.append(", ");
                }
                s1.append(amap.column().name()).append("=?");
            }
            for (AssociationMap amap : _assocVector) {
                if (!amap.multiplicity()) {
                    if (first) {
                        first = false;
                    } else {
                        s1.append(", ");
                    }
                    s1.append(amap.destinationDB().name()).append("=?, ");
                    s1.append(amap.destinationClass().name()).append("=?, ");
                    s1.append(amap.destinationOID().name()).append("=?");
                }
            }
            String query = "update " + _table.name() + " set " + s1.toString() + " where oid = ?";
            _smgr.updateStatement(query);
            logStatement(query);
        }
    }

    /**
   * Log an SQL statement created for this class.
   *
   * @param s a string containing the SQL statement.
   */
    private void logStatement(String s) {
        String msg = _basename + ": " + s;
        _logger.trace(msg);
    }

    /**
   * Create a new proxy and a Link in the object directory for an object
   * of this class which is not yet loaded.  Verify that the specified
   * database supports this class. Return the Link.
   *
   * @param dmap  the database map of the target database.
   * @param loid  the LOID of the object.
   * @return the link (object directory entry) for the object.
   * @throws PersistenceException if creation of the proxy failed.
   * @throws ClassNotSupportedException if the target database does not
   *         contain the tables necessary to persist this class.
   */
    Link<T> createReference(DatabaseMap dmap, LOID loid) throws PersistenceException {
        _logger.debug("new reference: " + loid.toString());
        Link<T> link = null;
        if (_databases.containsKey(dmap.key())) {
            try {
                link = new Link<T>(dmap, this, loid);
                AbstractProxy<T> proxy = _factory.create();
                link.proxy(proxy);
                proxy.set$link(link);
            } catch (Exception e) {
                throw new PersistenceException(_pm, e);
            }
        } else {
            throw new ClassNotSupportedException(_pm, this, dmap, null);
        }
        return link;
    }

    /**
   * Load an instance of this class from the database.
   *
   * @param loid the LOID of the object to be loaded.
   * @throws ObjectNotFoundException if the specified object is not found.
   * @throws PersistenceException if the implementation object could not be
   *         instantiated or an SQL error occurred while loading the object.
   */
    PObject load(LOID loid) throws ObjectNotFoundException, PersistenceException {
        _logger.debug("load: " + loid.toString());
        PObject target = null;
        DatabaseMap dmap = _pm.findDatabase(loid.databaseID());
        if (dmap != null && _databases.containsKey(dmap.key())) {
            PersistenceContext pctx = _pm.currentContext();
            long oid = loid.oid();
            Connection con = dmap.connect(true);
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                stmt = _smgr.selectStatement(con);
                stmt.setLong(1, oid);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    target = (PObject) _implClass.newInstance();
                    processResultSet(pctx, rs, target);
                } else {
                    throw new ObjectNotFoundException(_pm, loid, null);
                }
                rs.close();
                if (_hasComplexAssociations) {
                    for (AssociationMap amap : _assocVector) {
                        if (amap.multiplicity()) {
                            amap.load(pctx, con, loid, target);
                        }
                    }
                }
                target.post$load();
            } catch (IllegalAccessException e) {
                throw new PersistenceException(_pm, _msg[0], e);
            } catch (InstantiationException e) {
                throw new PersistenceException(_pm, _msg[0], e);
            } catch (SQLException e) {
                throw new PersistenceException(_pm, _msg[1], e);
            } finally {
                PersistenceManager.cleanup(rs, null);
                dmap.release(con);
            }
        }
        return target;
    }

    /**
   * Create references for each object in the database that matches the
   * search criteria. Returns a vector of proxies for objects matching
   * the search criteria.
   *
   * @param pctx  the persistence context for this invocation.
   * @param criteria  the search criteria
   * @return a vector of proxies for objects matching the search criteria.
   * @throws PersistenceException if an SQL error occurred or a proxy
   *         could not be created.
   */
    Collection<? extends AbstractProxy<?>> load(PersistenceContext pctx, Criteria<?> criteria) throws ObjectNotFoundException, PersistenceException {
        Collection<AbstractProxy<?>> c = CollectionFactory.arrayList();
        DatabaseMap dmap = criteria.database();
        if (dmap != null) {
            Statement stmt = null;
            ResultSet rs = null;
            Connection con = dmap.connect(true);
            try {
                stmt = con.createStatement();
                String query = _query + "(" + criteria.query() + ")";
                _logger.debug("load: " + query);
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    short databaseID = rs.getShort(1);
                    short classID = rs.getShort(2);
                    long oid = rs.getLong(3);
                    LOID loid = new LOID(databaseID, classID, oid);
                    AbstractProxy<?> proxy = null;
                    Link<?> link = pctx.findLink(loid);
                    if (link == null) {
                        link = new Link<T>(dmap, this, loid);
                        proxy = _factory.create();
                        link.proxy(proxy);
                        proxy.set$link(link);
                        pctx.addLink(link);
                    } else {
                        proxy = link.proxy();
                    }
                    c.add(proxy);
                }
            } catch (SQLException e) {
                throw new PersistenceException(_pm, _msg[1], e);
            } finally {
                PersistenceManager.cleanup(rs, stmt);
                dmap.release(con);
            }
        }
        return c;
    }

    /**
   * Process the result set generated by executing a select statement.
   *
   * @param pctx  the persistence context for this activity.
   * @param rs  the result set to be processed.
   * @param target  the object being loaded.
   * @throws PersistenceException if an SQL error occurs.
   */
    private void processResultSet(PersistenceContext pctx, ResultSet rs, PObject target) throws PersistenceException {
        if (_parent != null) {
            _parent.processResultSet(pctx, rs, target);
        }
        for (AttributeMap amap : _attrVector) {
            amap.setField(rs, target);
        }
        for (AssociationMap amap : _assocVector) {
            if (!amap.multiplicity()) {
                amap.setField(pctx, rs, target);
            }
        }
    }

    /**
   * Delete an instance of this class from the database.
   *
   * @param pctx  the persistence context for this activity.
   * @param link  the directory entry for the object to be deleted.
   * @throws PersistenceException if an SQL error occurs.
   */
    void delete(PersistenceContext pctx, Link<?> link) throws PersistenceException {
        LOID loid = link.loid();
        _logger.debug("delete: " + loid.toString());
        DatabaseMap dmap = _pm.findDatabase(loid.databaseID());
        if (dmap != null) {
            long oid = loid.oid();
            Connection con = pctx.connect(dmap);
            delete(pctx, con, oid);
        }
    }

    /**
   * Delete an instance of this class from the database using the
   * specified connnection.
   *
   * @param pctx  the persistence context for this activity.
   * @param con  the connection to use for this activity.
   * @param oid  the OID of the object to be deleted.
   * @throws PersistenceException if an SQL error occurs.
   */
    void delete(PersistenceContext pctx, Connection con, long oid) throws PersistenceException {
        if (_parent != null) {
            _parent.delete(pctx, con, oid);
        }
        PreparedStatement stmt = null;
        try {
            if (_table != null) {
                stmt = _smgr.deleteStatement(con);
                stmt.setLong(1, oid);
                stmt.addBatch();
                pctx.addDeleteBatch(stmt);
            }
            for (AssociationMap amap : _assocVector) {
                if (amap.multiplicity()) {
                    amap.delete(pctx, con, oid);
                }
            }
        } catch (Exception e) {
            throw new PersistenceException(_pm, e);
        }
    }

    /**
   * Insert an instance of this class into the database.
   *
   * @param pctx  the persistence context for this activity.
   * @param con  the connection to use for this activity.
   * @param link  the directory entry for the object to be inserted.
   * @throws PersistenceException if an SQL error occurs.
   */
    void insert(PersistenceContext pctx, Connection con, Link<?> link) throws PersistenceException {
        if (_parent != null) {
            _parent.insert(pctx, con, link);
        }
        PreparedStatement stmt = null;
        try {
            LOID loid = link.loid();
            PObject target = link.target();
            if (_table != null) {
                stmt = _smgr.insertStatement(con);
                stmt.setShort(1, loid.databaseID());
                stmt.setShort(2, loid.classID());
                stmt.setLong(3, loid.oid());
                int position = 4;
                for (AttributeMap amap : _attrVector) {
                    amap.getField(stmt, position, target);
                    ++position;
                }
                for (AssociationMap amap : _assocVector) {
                    if (!amap.multiplicity()) {
                        long oid = 0;
                        short classID = 0;
                        short databaseID = 0;
                        Link<?> dlink = amap.getField(target);
                        if (dlink != null) {
                            LOID dloid = dlink.loid();
                            databaseID = dloid.databaseID();
                            classID = dloid.classID();
                            oid = dloid.oid();
                        }
                        stmt.setShort(position++, databaseID);
                        stmt.setShort(position++, classID);
                        stmt.setLong(position++, oid);
                    }
                }
                stmt.addBatch();
                pctx.addUpdateBatch(stmt);
            }
            for (AssociationMap amap : _assocVector) {
                if (amap.multiplicity()) {
                    amap.store(pctx, con, loid, target);
                }
            }
        } catch (Exception e) {
            throw new PersistenceException(_pm, e);
        }
    }

    /**
   * Update an instance of this class in the database.
   *
   * @param pctx  the persistence context for this activity.
   * @param con  the connection to use for this activity.
   * @param link  the directory entry for the object to be inserted.
   * @throws PersistenceException if an SQL error occurs.
   */
    void update(PersistenceContext pctx, Connection con, Link<?> link) throws PersistenceException {
        if (_parent != null) {
            _parent.update(pctx, con, link);
        }
        PreparedStatement stmt = null;
        try {
            LOID loid = link.loid();
            PObject target = link.target();
            if (_enableUpdate) {
                stmt = _smgr.updateStatement(con);
                int position = 1;
                for (AttributeMap amap : _attrVector) {
                    amap.getField(stmt, position, target);
                    ++position;
                }
                for (AssociationMap amap : _assocVector) {
                    if (!amap.multiplicity()) {
                        long oid = 0;
                        short classID = 0;
                        short databaseID = 0;
                        Link<?> dlink = amap.getField(target);
                        if (dlink != null) {
                            LOID dloid = dlink.loid();
                            databaseID = dloid.databaseID();
                            classID = dloid.classID();
                            oid = dloid.oid();
                        }
                        stmt.setShort(position++, databaseID);
                        stmt.setShort(position++, classID);
                        stmt.setLong(position++, oid);
                    }
                }
                stmt.setLong(position++, loid.oid());
                stmt.addBatch();
                pctx.addUpdateBatch(stmt);
            }
            for (AssociationMap amap : _assocVector) {
                if (amap.multiplicity()) {
                    amap.delete(pctx, con, loid.oid());
                    amap.store(pctx, con, loid, target);
                }
            }
        } catch (Exception e) {
            throw new PersistenceException(_pm, e);
        }
    }

    /**
   * Add an association to this class.
   *
   * @param amap  the association map of the association to be added.
   */
    void addAssociation(AssociationMap amap) {
        _associations.put(amap.name(), amap);
        _assocVector.add(amap);
        TableMap tmap = amap.table();
        if (amap.multiplicity()) {
            if (!_assocTables.contains(tmap)) {
                _assocTables.add(tmap);
            }
        }
    }

    /**
   * Add an attribute to this class.
   *
   * @param amap  the attribute map of the attribute to be added.
   */
    void addAttribute(AttributeMap amap) {
        _attributes.put(amap.name(), amap);
        _attrVector.add(amap);
    }

    /**
   * Returns the association with the specified name belonging to
   * this class.
   *
   * @param name  the association name.
   * @return the association map matching the specified name.
   */
    public AssociationMap findAssociation(String name) {
        AssociationMap result = (AssociationMap) _associations.get(name);
        if (result == null && _parent != null) {
            result = _parent.findAssociation(name);
        }
        return result;
    }

    /**
   * Returns the attribute with the specified name belonging to
   * this class.
   *
   * @param name  the attribute name.
   * @return the attribute map matching the specified name.
   */
    public AttributeMap findAttribute(String name) {
        AttributeMap result = (AttributeMap) _attributes.get(name);
        if (result == null && _parent != null) {
            result = _parent.findAttribute(name);
        }
        return result;
    }

    /**
   * Output a description of this class on stdout.  Call the show
   * method of each AttributeMap and AssociationMap.
   */
    public void show() {
        System.out.println(_classID + " " + name() + " " + _implName + " {");
        for (AssociationMap amap : _associations.values()) {
            amap.show();
        }
        for (AttributeMap amap : _attributes.values()) {
            amap.show();
        }
        System.out.println("  }");
        System.out.print("  databases:");
        for (DatabaseMap dmap : _databases.values()) {
            System.out.print(" " + dmap.name());
        }
        System.out.print("\n  tables:");
        for (TableMap tmap : _tables) {
            System.out.print(" " + tmap.name());
        }
        System.out.println("\n  query: " + _selectStmt + "\n");
    }

    /**
   * Output an XML representation of this class on the specified print
   * writer.  Call the outputXML method for each AssociationMap and
   * AttributeMap.
   *
   * @param out  the print writer to use for all output.
   */
    public void outputXML(PrintWriter out) {
        out.println("  <class-descriptor>");
        out.println("    <class-id>" + classID() + "</class-id>");
        out.println("    <class-name>" + name() + "</class-name>");
        out.println("    <implementation-name>" + implementationName() + "</implementation-name>");
        out.println("    <proxy-name>" + proxyName() + "</proxy-name>");
        if (_parent != null) {
            out.println("    <parent-class>" + _parent.name() + "</parent-class>");
        }
        out.println("    <table>" + _keyDB.table().name() + "</table>");
        out.println("    <database-id-column>" + _keyDB.name() + "</database-id-column>");
        out.println("    <class-id-column>" + _keyClass.name() + "</class-id-column>");
        out.println("    <oid-column>" + _keyOID.name() + "</oid-column>");
        for (AssociationMap amap : _associations.values()) {
            amap.outputXML(out);
        }
        for (AttributeMap amap : _attributes.values()) {
            amap.outputXML(out);
        }
        out.println("  </class-descriptor>");
    }

    /**
   * Returns the class identifier for this class.
   *
   * @return the class identifier for this class.
   */
    public short classID() {
        return _classID;
    }

    /**
   * Returns the interface for this class.
   *
   * @return the interface for this class.
   */
    Class<? extends Object> getInterface() {
        return _interface;
    }

    /**
   * Returns true if objects of this class have dependent objects.
   *
   * @return true if objects of this class have dependent objects.
   */
    boolean hasDependents() {
        return _hasDependents;
    }

    /**
   * Returns the implementation class for this class.
   *
   * @return the implementation class for this class.
   */
    Class<? extends Object> implementation() {
        return _implClass;
    }

    /**
   * Returns the name of the implementation class for this class.
   *
   * @return the name of the implementation class for this class.
   */
    public String implementationName() {
        return _implName;
    }

    /**
   * Returns the key used for this class in the maps maintained by
   * the persistence manager.
   *
   * @return the key for this class.
   */
    Short key() {
        return _key;
    }

    /**
   * Returns the name of this class.
   *
   * @return the name of this class.
   */
    public String name() {
        return _name;
    }

    /**
   * Returns the class map for the parent of this class.
   *
   * @return a class map for the parent class.
   */
    public ClassMap<? super T> parent() {
        return _parent;
    }

    /**
   * Returns the top class (the farthest ancestor) in a class hierarchy.
   *
   * @return a class map for the top class.
   */
    ClassMap<? super T> top() {
        return _top;
    }

    ProxyFactory<T> proxyFactory() {
        return _factory;
    }

    /**
   * Returns the name of the proxy class for this class.
   *
   * @return the name of the proxy class for this class.
   */
    public String proxyName() {
        return _proxyName;
    }

    /**
   * Returns the name of this class minus the package name.
   *
   * @return the name of this class minus the package name.
   */
    public String basename() {
        return _basename;
    }

    /**
   * Returns the table map for the table to which this class is mapped.
   *
   * @return the table map for the table to which this class is mapped.
   */
    public TableMap table() {
        return _table;
    }

    /**
   * Returns the vector of tables used by this class.
   *
   * @return the vector of tables used by this class.
   */
    public Collection<TableMap> tables() {
        return _tables;
    }

    /**
   * Returns the column map for the column to which the OID is mapped.
   *
   * @return the column map for the column to which the OID is mapped.
   */
    public ColumnMap keyOID() {
        return _keyOID;
    }

    /**
   * Returns the level of this class in a class hierarchy.  Level 0
   * is the root of a hierarchy.
   *
   * @return the level of this class in a class hierarchy.
   */
    int level() {
        return _level;
    }

    /**
   * Set the level of this class in a class hierarchy.
   *
   * @param i  the class level.
   */
    void level(int i) {
        _level = i;
    }

    void close() {
        _smgr.close();
    }

    private static final String[] _msg = new String[] { "failed to instantiate implementation object.", "SQL exception while loading object.", "interface class not found.", "implementation class not found." };

    private int _level;

    private int _position;

    private boolean _hasComplexAssociations;

    private boolean _hasDependents;

    private boolean _enableUpdate;

    private short _classID;

    private Short _key;

    private Class<? extends Object> _implClass;

    private Class<? extends Object> _interface;

    private ClassMap<? super T> _parent;

    private ClassMap<? super T> _top;

    private ColumnMap _keyDB;

    private ColumnMap _keyClass;

    private ColumnMap _keyOID;

    private Logger _logger;

    private ProxyFactory<T> _factory;

    private StatementManager _smgr;

    private String _basename;

    private String _implName;

    private String _proxyName;

    private String _fromClause;

    private String _whereClause;

    private String _selectClause;

    private String _selectStmt;

    private String _query;

    private TableMap _table;

    private List<AssociationMap> _assocVector;

    private List<AttributeMap> _attrVector;

    private List<TableMap> _tables;

    private List<TableMap> _assocTables;

    private Map<String, AssociationMap> _associations;

    private Map<String, AttributeMap> _attributes;

    private Map<Short, DatabaseMap> _databases;
}
