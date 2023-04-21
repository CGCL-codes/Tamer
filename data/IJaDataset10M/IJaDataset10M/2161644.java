package org.eclipse.datatools.sqltools.internal.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.datatools.connectivity.ConnectEvent;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.IManagedConnectionListener;
import org.eclipse.datatools.connectivity.sqm.core.definition.DatabaseDefinition;
import org.eclipse.datatools.connectivity.sqm.internal.core.RDBCorePlugin;
import org.eclipse.datatools.modelbase.sql.datatypes.PredefinedDataType;
import org.eclipse.datatools.modelbase.sql.datatypes.UserDefinedType;
import org.eclipse.datatools.modelbase.sql.query.helper.DataTypeHelper;
import org.eclipse.datatools.modelbase.sql.routines.Routine;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Event;
import org.eclipse.datatools.modelbase.sql.schema.SQLObject;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.datatools.modelbase.sql.tables.Trigger;
import org.eclipse.datatools.sqltools.core.ConnectionException;
import org.eclipse.datatools.sqltools.core.DatabaseIdentifier;
import org.eclipse.datatools.sqltools.core.EditorCorePlugin;
import org.eclipse.datatools.sqltools.core.IControlConnection;
import org.eclipse.datatools.sqltools.core.IControlConnectionManager;
import org.eclipse.datatools.sqltools.core.ProcIdentifier;
import org.eclipse.datatools.sqltools.core.SQLDevToolsConfiguration;
import org.eclipse.datatools.sqltools.core.SQLToolsFacade;
import org.eclipse.datatools.sqltools.core.dbitem.IDBItem;
import org.eclipse.datatools.sqltools.core.dbitem.IItemWithCode;
import org.eclipse.datatools.sqltools.core.internal.dbitem.SQLObjectItem;
import org.eclipse.datatools.sqltools.core.profile.NoSuchProfileException;
import org.eclipse.datatools.sqltools.core.profile.ProfileUtil;
import org.eclipse.datatools.sqltools.core.services.ConnectionService;
import org.eclipse.datatools.sqltools.internal.SQLDevToolsUtil;
import org.eclipse.datatools.sqltools.sql.reference.IDatatype;
import org.eclipse.datatools.sqltools.sql.util.ModelUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.util.Assert;

/**
 * This class provides default implementations for <code>IControlConnection</code>
 * TODO: rename this class since it's not abstract.
 * @author Yang Liu
 */
public class AbstractControlConnection implements IControlConnection {

    protected Set _skipConnections = new HashSet();

    protected DatabaseIdentifier _databaseIdentifier;

    protected String _dbUserName = null;

    /**
	 * this map is used for internal proc info cache. key is ProcIdentifier. the
	 * refresh() method will discard everything in the cache.
	 * 
	 * ProcIdentifier --> IDBItem
	 */
    private Map _procInfoCache = new HashMap();

    protected ControlConnectionManager _manager;

    private SQLToolsManagedConnectionListener _managedConnectionListener;

    /**
	 * Constructs an AbstractControlConnection.
	 * 
	 * @param manager
	 *            {@link EditorCorePlugin#getControlConnectionManager()}
	 * @param databaseIdentifier
	 *            contains connection profile info and database name
	 */
    public AbstractControlConnection(IControlConnectionManager manager, DatabaseIdentifier databaseIdentifier) {
        Assert.isTrue(manager instanceof ControlConnectionManager);
        this._manager = (ControlConnectionManager) manager;
        this._databaseIdentifier = databaseIdentifier;
        try {
            IConnectionProfile profile = ProfileUtil.getProfile(databaseIdentifier.getProfileName());
            IManagedConnection managedConn = profile.getManagedConnection("java.sql.Connection");
            _managedConnectionListener = new SQLToolsManagedConnectionListener();
            managedConn.addConnectionListener(_managedConnectionListener);
        } catch (Exception e) {
            EditorCorePlugin.getDefault().log(e);
        }
    }

    public IControlConnectionManager getManager() {
        return _manager;
    }

    public DatabaseIdentifier getDatabaseIdentifier() {
        return _databaseIdentifier;
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    /**
	 * @param name
	 *            The _profileName to set.
	 */
    public void profileRenamed(String name) {
        IControlConnection controlConnection = (IControlConnection) _manager._controlConnectionMap.get(_databaseIdentifier);
        _manager._controlConnectionMap.remove(_databaseIdentifier);
        _databaseIdentifier.setProfileName(name);
        _manager._controlConnectionMap.put(_databaseIdentifier, controlConnection);
        _manager.fireRefreshed(this);
    }

    public String getDatabaseName() {
        return _databaseIdentifier.getDBname();
    }

    public String getDbUsername() throws SQLException {
        if (this._dbUserName == null) {
            _dbUserName = this.getReusableConnection().getMetaData().getUserName();
        }
        return this._dbUserName;
    }

    /**
	 * Checks if the Text of the procedural object is hidden This is applicable
	 * for ASE only
	 * 
	 * @return true if text hidden, else false
	 */
    public boolean isTextHidden(DatabaseIdentifier databaseIdentifier, String dbObjectName, int dbObjectType) {
        return false;
    }

    public void fireChange() {
        _manager.fireRefreshed(this);
    }

    /**
	 * Disposes the resources. Child class can override this method, but
	 * normally should call super.dispose()
	 */
    protected void dispose() {
        try {
            IConnectionProfile profile = ProfileUtil.getProfile(_databaseIdentifier.getProfileName());
            IManagedConnection managedConn = profile.getManagedConnection("java.sql.Connection");
            managedConn.removeConnectionListener(_managedConnectionListener);
        } catch (NoSuchProfileException e) {
        } catch (Exception e) {
            EditorCorePlugin.getDefault().log(e);
        }
    }

    /**
	 * Whether it's ok to disconnect this control connection.
	 * 
	 */
    public boolean okToDisconnect() {
        return true;
    }

    protected void aboutToDisconnect() {
    }

    public boolean disconnect() {
        return disconnect(false);
    }

    public boolean disconnect(boolean force) {
        dispose();
        _manager.remove(this);
        return true;
    }

    public void executeDDL(String[] src) throws SQLException {
        Connection con;
        con = getReusableConnection();
        Statement stmt = con.createStatement();
        try {
            try {
                for (int i = 0; i < src.length; i++) {
                    stmt.executeUpdate(src[i]);
                }
                refresh();
            } catch (SQLException ex) {
                throw ex;
            }
        } finally {
            stmt.close();
        }
    }

    public ProcIdentifier[] getAllProcs() throws SQLException {
        Database db = ProfileUtil.getDatabase(_databaseIdentifier);
        ArrayList procs = new ArrayList();
        if (db != null) {
            EList schemas = ModelUtil.getSchemas(db, _databaseIdentifier.getDBname());
            Iterator i = schemas.iterator();
            for (; i.hasNext(); ) {
                Schema schema = (Schema) i.next();
                EList tables = schema.getTables();
                for (Iterator iter = tables.iterator(); iter.hasNext(); ) {
                    Table table = (Table) iter.next();
                    EList triggers = table.getTriggers();
                    for (Iterator itera = triggers.iterator(); itera.hasNext(); ) {
                        Trigger trigger = (Trigger) itera.next();
                        procs.add(SQLDevToolsUtil.getProcIdentifier(_databaseIdentifier, trigger));
                    }
                }
                EList routines = schema.getRoutines();
                for (Iterator iter = routines.iterator(); iter.hasNext(); ) {
                    Routine routine = (Routine) iter.next();
                    procs.add(SQLDevToolsUtil.getProcIdentifier(_databaseIdentifier, routine));
                }
            }
            EList events = db.getEvents();
            for (Iterator iter = events.iterator(); iter.hasNext(); ) {
                Event event = (Event) iter.next();
                procs.add(SQLDevToolsUtil.getProcIdentifier(_databaseIdentifier, event));
            }
        }
        return (ProcIdentifier[]) procs.toArray(new ProcIdentifier[procs.size()]);
    }

    public String getProcSource(ProcIdentifier proc) throws SQLException {
        IDBItem item = getDBItem(proc);
        if (item instanceof IItemWithCode) {
            return ((IItemWithCode) item).getCode();
        } else {
            throw new SQLException(Messages.AbstractControlConnection_invalid_store_procedure_description);
        }
    }

    public void saveRoutine(ProcIdentifier proc, String code) throws SQLException {
        IDBItem item = getDBItem(proc);
        if (item instanceof IItemWithCode) {
            ((IItemWithCode) item).save(code);
            _manager.fireRefreshed(this, proc);
        } else {
            throw new SQLException(Messages.AbstractControlConnection_invalid_store_procedure_description);
        }
        ModelUtil.findProceduralObject(proc, true);
    }

    public IDBItem getDBItem(ProcIdentifier procIdentifier) {
        IDBItem item = (IDBItem) this._procInfoCache.get(procIdentifier);
        if (item == null) {
            item = createDBItem(procIdentifier);
            if (item != null) _procInfoCache.put(procIdentifier, item);
        }
        return item;
    }

    protected ProcIdentifier findSPUDF(String owner, String name) {
        for (Iterator iter = _procInfoCache.keySet().iterator(); iter.hasNext(); ) {
            ProcIdentifier proc = (ProcIdentifier) iter.next();
            if (proc.getType() == ProcIdentifier.TYPE_SP || proc.getType() == ProcIdentifier.TYPE_UDF) {
                if (proc.getOwnerName().equals(owner) && proc.getProcName().equals(name)) return proc;
            }
        }
        return null;
    }

    protected Map getDBItemCache() {
        return _procInfoCache;
    }

    /**
	 * Subclass should implement this method to create db item based on
	 * ProcIdentifier.
	 * 
	 * @param proc
	 */
    protected IDBItem createDBItem(ProcIdentifier proc) {
        SQLObject obj = ModelUtil.findProceduralObject(proc);
        if (obj != null) {
            return new SQLObjectItem(proc, obj, this);
        }
        return null;
    }

    public void refresh() {
        for (Iterator iter = _procInfoCache.values().iterator(); iter.hasNext(); ) {
            IDBItem item = (IDBItem) iter.next();
            item.dispose();
        }
        _procInfoCache.clear();
    }

    public void refresh(ProcIdentifier procIdentifier) {
        IDBItem item = (IDBItem) _procInfoCache.get(procIdentifier);
        if (item != null) {
            item.dispose();
        }
        _procInfoCache.remove(procIdentifier);
    }

    public String convertToInternalConnId(String externalId, String exteranlName) throws ConnectionException {
        return externalId;
    }

    public Connection createConnection(String[] connId) throws SQLException, CoreException, NoSuchProfileException {
        SQLDevToolsConfiguration f = SQLToolsFacade.getConfigurationByProfileName(getDatabaseIdentifier().getProfileName());
        ConnectionService conService = f.getConnectionService();
        Connection con = conService.createConnection(getDatabaseIdentifier().getProfileName(), getDatabaseIdentifier().getDBname());
        if (connId != null && connId.length == 1) {
            connId[0] = "0";
        }
        return con;
    }

    public Connection getReusableConnection() {
        try {
            return ProfileUtil.getReusableConnection(_databaseIdentifier);
        } catch (Exception e) {
            return null;
        }
    }

    public void registerSkip(int connid) {
        Integer id = new Integer(connid);
        _skipConnections.add(id);
    }

    public void unregisterSkip(int connid) {
        _skipConnections.remove(new Integer(connid));
    }

    protected boolean shouldSkip(int connid) {
        return _skipConnections.contains(new Integer(connid));
    }

    /**
	 * get the Datatype object of a user-defined datatype
	 * 
	 * @param typeName,
	 *            name of a user-defined datatype
	 * @return
	 */
    protected IDatatype getUserDataType(String typeName) throws SQLException {
        Database db = ProfileUtil.getDatabase(_databaseIdentifier);
        String user = ProfileUtil.getProfileUserName(_databaseIdentifier, false);
        DatabaseDefinition dbdef = RDBCorePlugin.getDefault().getDatabaseDefinitionRegistry().getDefinition(db);
        for (Iterator i = db.getSchemas().iterator(); i.hasNext(); ) {
            Schema schema = (Schema) i.next();
            if (schema.getName().equals(user)) {
                EList tables = schema.getUserDefinedTypes();
                for (Iterator iter = tables.iterator(); iter.hasNext(); ) {
                    UserDefinedType type = (UserDefinedType) iter.next();
                    if (type.getName().equals(typeName)) {
                        return ModelUtil.map(dbdef, type, user);
                    }
                }
            }
        }
        return null;
    }

    public boolean supportsDebugging() {
        return false;
    }

    public IDatatype getTypeByNameStr(String nameStr) throws Exception {
        Database db = ProfileUtil.getDatabase(_databaseIdentifier);
        String user = ProfileUtil.getProfileUserName(_databaseIdentifier, false);
        DatabaseDefinition dbdef = RDBCorePlugin.getDefault().getDatabaseDefinitionRegistry().getDefinition(db);
        PredefinedDataType pretype = DataTypeHelper.getPredefinedDataTypeForNamedType(nameStr);
        if (pretype != null) {
            return ModelUtil.map(dbdef, pretype, user);
        } else {
            return getUserDataType(nameStr);
        }
    }

    public class SQLToolsManagedConnectionListener implements IManagedConnectionListener {

        public void opened(ConnectEvent event) {
        }

        public void modified(ConnectEvent event) {
        }

        public boolean okToClose(ConnectEvent event) {
            return okToDisconnect();
        }

        public void aboutToClose(ConnectEvent event) {
            aboutToDisconnect();
        }

        public void closed(ConnectEvent event) {
            disconnect(true);
        }
    }
}
