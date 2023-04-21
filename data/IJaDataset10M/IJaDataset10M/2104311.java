package wsl.fw.datasource;

import wsl.fw.remote.RmiServantBase;
import wsl.fw.util.Util;
import wsl.fw.util.Log;
import wsl.fw.datasource.DataManager;
import wsl.fw.datasource.DataSource;
import wsl.fw.datasource.RemoteDataSourceServant;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Vector;

/**
 * RMI sevant for remote data manager access. Works in conjunction with the
 * local data manager, local data sources and the RemoteDataSource servants
 * and proxies.
 */
public class RemoteDataManagerServant extends RmiServantBase implements RemoteDataManager {

    private static final String _ident = "$Date: 2002/06/11 23:11:42 $  $Revision: 1.1.1.1 $ " + "$Archive: /Framework/Source/wsl/fw/datasource/RemoteDataManagerServant.java $ ";

    private Vector _servants = new Vector();

    private boolean _terminating = false;

    /**
     * Default constructor.
     */
    public RemoteDataManagerServant() {
    }

    /**
     * Create a remote data source.
     * @param dsName the name of the data source to create
     */
    public RemoteDataSource createDataSource(String dsName) throws RemoteException {
        if (_terminating) return null;
        RemoteDataSource rds = null;
        DataSource lds = DataManager.getDataSource(dsName);
        if (lds != null) {
            rds = new RemoteDataSourceServant(lds);
            UnicastRemoteObject.exportObject(rds);
            _servants.add(rds);
            Log.debug("RemoteDataManagerServant.createDataSource(\"" + dsName + "\")");
        }
        return rds;
    }

    /**
     * Create a remote data source.
     * @param param, the param defining the data source to create
     */
    public RemoteDataSource createDataSource(DataSourceParam param) throws RemoteException {
        if (_terminating) return null;
        RemoteDataSource rds = null;
        DataSource lds = DataManager.getDataSource(param);
        if (lds != null) {
            rds = new RemoteDataSourceServant(lds);
            UnicastRemoteObject.exportObject(rds);
            _servants.add(rds);
            Log.debug("RemoteDataManagerServant.createDataSource(\"" + param.getFullName() + "\")");
        }
        return rds;
    }

    /**
     * @return DataObjectVersionCache the version cache in the dm
     */
    protected DataObjectVersionCache getVersionCache() {
        return DataManager.getDataManager().getVersionCache();
    }

    /**
     * Returns a version for an object
     * @param entityName the name of the entity
     * @param key the unique key of the object
     * @return Integer the version of the object, null if no versioned object
     */
    public Integer getObjectVersion(String entityName, Object key) throws RemoteException {
        Util.argCheckEmpty(entityName);
        Util.argCheckNull(key);
        Integer version = (Integer) getVersionCache().getObjectVersion(entityName, key);
        return version;
    }

    /**
     * Overload from RmiServantBase, terminate all servants.
     */
    public synchronized void terminate() {
        if (!_terminating) {
            _terminating = true;
            for (int i = 0; i < _servants.size(); i++) {
                Remote r = (Remote) _servants.get(i);
                if (r != null) try {
                    UnicastRemoteObject.unexportObject(r, true);
                } catch (Exception e) {
                }
            }
        }
    }
}
