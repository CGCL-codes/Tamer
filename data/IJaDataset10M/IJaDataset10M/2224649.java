package eu.popeye.middleware.dataSharing.centralizedDataManagement.dataSharingServer;

import java.io.*;
import java.util.*;
import java.rmi.RemoteException;
import javax.swing.JEditorPane;
import org.jdom.Document;
import org.jdom.JDOMException;
import eu.popeye.middleware.dataSharing.*;
import eu.popeye.middleware.dataSharing.centralizedDataManagement.LocalCacheManager;
import eu.popeye.middleware.dataSharing.centralizedDataManagement.SharedDataContainer;
import eu.popeye.middleware.dataSharing.centralizedDataManagement.dataSharingClient.RemoteObserver;
import eu.popeye.middleware.dataSharing.common.*;
import eu.popeye.middleware.dataSharing.dataSharingExceptions.*;
import eu.popeye.middleware.dataSharing.common.util.DocumentHandler;

public class SharedSpaceServerImpl implements SharedSpaceServer {

    private File homeDir;

    private String name;

    private int allowedSharedSpace;

    private JEditorPane logPane;

    private Integer counter = new Integer(0);

    private TreeStructure treeStructure;

    private Vector clientVector = null;

    private LocalCacheManager localCacheManager;

    public SharedSpaceServerImpl(String homeDirPath, String name, int allowedSharedSpace, JEditorPane logPane) {
        homeDir = new File(homeDirPath);
        boolean ok = false;
        if (!homeDir.exists()) {
            ok = homeDir.mkdir();
            if (!ok) {
                addLogMessage("ERROR: " + homeDirPath + " directory cannot be created");
                return;
            }
        }
        this.name = name;
        this.logPane = logPane;
        this.allowedSharedSpace = allowedSharedSpace;
        this.localCacheManager = new LocalCacheManager(homeDirPath);
        this.localCacheManager.setLogPane(this.logPane);
        Document savedTreeStructure = this.localCacheManager.getCurrentTreeStructure();
        if (savedTreeStructure != null) {
            Collection dataNames = DocumentHandler.getDataNames(savedTreeStructure);
            Map realPathDataNames = new HashMap();
            Iterator it = dataNames.iterator();
            while (it.hasNext()) {
                String path = (String) it.next();
                String realPath = this.homeDir + path;
                realPathDataNames.put(path, realPath);
            }
            Collection dirNames = DocumentHandler.getDirectoryNames(savedTreeStructure);
            Map realPathDirNames = new HashMap();
            it = dirNames.iterator();
            while (it.hasNext()) {
                String path = (String) it.next();
                String realPath = this.homeDir + path;
                realPathDirNames.put(path, realPath);
            }
            this.treeStructure = new TreeStructure(savedTreeStructure, realPathDirNames, realPathDataNames);
        } else this.treeStructure = new TreeStructure();
        this.clientVector = new Vector();
        addLogMessage("new Shared Space Server Impl created  name=" + name);
    }

    public String getName() {
        return this.name;
    }

    public SharedData accessRead(String path) throws DataDoesNotExistException {
        SharedDataContainer dataCont;
        dataCont = treeStructure.getData(path);
        return dataCont.getData();
    }

    public SharedData accessWrite(String path, String peerID) throws RemoteException, DataDoesNotExistException, NotAllowedException, NotImplementedYetException {
        try {
            SharedDataContainer dataContainer = treeStructure.getData(path);
            switch((SharingMode) dataContainer.metadata.get(Metadata.SHARINGMODE)) {
                case readOnly:
                    throw new NotAllowedException();
                case OwnerOnly:
                    if (peerID.equals(dataContainer.metadata.get(Metadata.OWNER))) {
                        dataContainer.acquireWriterLock(peerID);
                        SharedData toSend = dataContainer.getData();
                        return toSend;
                    } else throw new NotAllowedException();
                case SWMR:
                    dataContainer.acquireWriterLock(peerID);
                    SharedData toSend = dataContainer.getData();
                    return toSend;
                case MWMR:
                    throw new NotImplementedYetException();
                default:
                    throw new NotImplementedYetException();
            }
        } catch (InterruptedException ex) {
            throw new RemoteException();
        }
    }

    public void commit(String path, SharedData newData, String peerID) throws NotAllowedException, DataDoesNotExistException {
        SharedDataContainer dataContainer;
        dataContainer = treeStructure.getData(path);
        if (dataContainer.getWriterLockOwner().equals(peerID)) {
            dataContainer.setData(newData);
            dataContainer.releaseWriterLock();
        } else throw new NotAllowedException();
    }

    public Document listData() {
        return treeStructure.getList();
    }

    public void shareData(SharedData data, String dataLocation, boolean isDirectory, SharingMode mode, String peerID) throws RemoteException, DataAlreadyExistException, InvalidPathException {
        try {
            DataIdentifier id;
            synchronized (this.counter) {
                id = new DataIdentifier(peerID, counter);
                counter++;
            }
            Metadata newMetadata = new Metadata(dataLocation, isDirectory ? Metadata.TYPE_DIR : Metadata.TYPE_FILE, mode, id);
            SharedDataContainer newContainer = new SharedDataContainer(data, newMetadata);
            treeStructure.addData(newContainer, isDirectory);
            this.addLogMessage("SHARED DATA: " + dataLocation);
            this.localCacheManager.update(treeStructure.getList());
            int eventType;
            if (isDirectory) {
                eventType = SharedSpaceEvent.DIRECTORY_ADDED;
                this.localCacheManager.createDirectory(newMetadata, (String) newMetadata.get(Metadata.PATH));
            } else {
                eventType = SharedSpaceEvent.DATA_ADDED;
                newMetadata.set(Metadata.CLASS, data.getClass().getName());
                this.localCacheManager.createFile(data, newMetadata, (String) newMetadata.get(Metadata.PATH));
            }
            SharedSpaceEvent event = new SharedSpaceEvent(eventType, dataLocation, peerID, this.treeStructure.getList());
            this.notifyObservers(event);
        } catch (JDOMException e) {
            throw new RemoteException();
        } catch (MetadataException e) {
            e.printStackTrace();
        }
    }

    public void withdrawData(String dataPath, String peerID) throws RemoteException, DataDoesNotExistException, NotAllowedException {
        boolean isDirectory = (treeStructure.getData(dataPath).metadata.get(Metadata.TYPE) == Metadata.TYPE_DIR) ? true : false;
        int eventType;
        if (isDirectory) eventType = SharedSpaceEvent.DIRECTORY_REMOVED; else eventType = SharedSpaceEvent.DATA_REMOVED;
        try {
            addLogMessage("Server: withdraw data request: " + dataPath);
            treeStructure.removeData(dataPath, peerID);
        } catch (NotAllowedException e) {
            System.out.println("NOT ALLOWED EXCEPTION");
            this.localCacheManager.update(treeStructure.getList());
        } catch (JDOMException e) {
            e.printStackTrace();
            throw new RemoteException();
        }
        this.localCacheManager.update(treeStructure.getList());
        SharedSpaceEvent event = new SharedSpaceEvent(eventType, dataPath, peerID, this.treeStructure.getList());
        this.notifyObservers(event);
    }

    public void move(String peerID, String oldPath, String newPath) throws RemoteException, InvalidPathException, DataDoesNotExistException, DataAlreadyExistException {
        try {
            this.treeStructure.moveData(peerID, oldPath, newPath);
        } catch (JDOMException e) {
            e.printStackTrace();
            throw new RemoteException();
        }
    }

    public Metadata readMetadata(String path) throws DataDoesNotExistException {
        return treeStructure.getData(path).metadata;
    }

    public void writeMetadata(String path, String field, Object newValue) throws DataDoesNotExistException, MetadataException {
        treeStructure.getData(path).metadata.set(field, newValue);
    }

    ;

    public String[] searchData(String Query) {
        return null;
    }

    public void registerForNotification(RemoteObserver observer) throws RemoteException {
        this.clientVector.addElement(observer);
    }

    public void notifyObservers(SharedSpaceEvent event) throws RemoteException {
        System.out.println("Server: notifyObservers");
        Iterator it = this.clientVector.iterator();
        while (it.hasNext()) {
            RemoteObserver n = (RemoteObserver) it.next();
            n.notify(event);
        }
    }

    private void addLogMessage(String msg) {
        if (this.logPane == null) System.out.println(msg); else this.logPane.setText(this.logPane.getText() + msg + "\n\n");
    }

    public boolean close() {
        return this.localCacheManager.saveCurrentState();
    }

    public boolean destroy() {
        return this.localCacheManager.destroy();
    }
}
