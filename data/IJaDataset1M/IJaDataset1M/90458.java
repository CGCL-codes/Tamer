package org.redwood.business.etl.logfile;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;

public interface LogFileHome extends EJBHome {

    public static final String COMP_NAME = "LogFile";

    public static final String JNDI_NAME = "LogFile";

    public LogFileObject create(long id, String filename) throws RemoteException, CreateException;

    public LogFileObject findByPrimaryKey(LogFilePK primaryKey) throws RemoteException, FinderException;

    public Collection findByRw_processState(String processstate) throws RemoteException, FinderException;

    public Collection findByWebsiteFilename(String websiteid, String filename) throws RemoteException, FinderException;

    public Collection findByWebsiteSizeDateFilename(String websiteID, long filesize, java.sql.Timestamp filedate, String filename) throws RemoteException, FinderException;

    public Collection findByWebsiteDateFilename(String websiteID, java.sql.Timestamp filedate, String filename) throws RemoteException, FinderException;

    public Collection findByDatasource(String datasourceID) throws RemoteException, FinderException;

    public Collection findByWebsiteChecksum(String websiteid, String checksum) throws RemoteException, FinderException;
}
