package andre.grids.filesystem.server.directorymanagement;

import java.rmi.Remote;

/**
 *
 * @author andre
 */
public interface IDirectoryManager {

    public boolean isMasterFor(String path);

    public String getMasterOf(String path, boolean localOnly);

    public void createEntryFor(String path, String remoteMaster);

    public void createLogEntryFor(String path);

    public void notifyMasters(String path);

    public Remote getDirectoryOperationsFor(String master);

    public boolean requestMasterFor(String path, Remote dirMaster);

    public boolean notifySuccessFor(String path, Remote dirMaster);

    public void notifyMastersDirDeletion(String path);
}
