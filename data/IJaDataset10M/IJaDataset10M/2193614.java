package eu.popeye.middleware.dataSharing.distributedDataManagement;

import java.awt.Dimension;
import java.io.File;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdom.Document;
import org.jdom.JDOMException;
import eu.popeye.middleware.dataSearch.HitsCollection;
import eu.popeye.middleware.dataSharing.Metadata;
import eu.popeye.middleware.dataSharing.SharedData;
import eu.popeye.middleware.dataSharing.SharedSpace;
import eu.popeye.middleware.dataSharing.SharedSpaceEvent;
import eu.popeye.middleware.dataSharing.SharingMode;
import eu.popeye.middleware.dataSharing.common.DataIdentifier;
import eu.popeye.middleware.dataSharing.dataSharingExceptions.*;
import eu.popeye.middleware.dataSharing.distributedDataManagement.ReplicationScheme.ReplicationResult;
import eu.popeye.middleware.dataSharing.distributedDataManagement.ReplicationScheme.ReplicationScheme;
import eu.popeye.middleware.dataSharing.distributedDataManagement.ReplicationScheme.Impl.ProactiveReplication;
import eu.popeye.middleware.dataSharing.distributedDataManagement.ReplicationScheme.Impl.SimpleReplication;
import eu.popeye.middleware.dataSharing.distributedDataManagement.cache.CacheManager;
import eu.popeye.middleware.dataSharing.distributedDataManagement.cache.CacheManagerException;
import eu.popeye.middleware.dataSharing.distributedDataManagement.coherenceScheme.CoherenceCommunication;
import eu.popeye.middleware.dataSharing.distributedDataManagement.coherenceScheme.Impl.OwnerOnlyCoherence;
import eu.popeye.middleware.dataSharing.distributedDataManagement.coherenceScheme.Impl.RaymondDMECoherence;
import eu.popeye.middleware.dataSharing.distributedDataManagement.coherenceScheme.Impl.ReadOnlyCoherence;
import eu.popeye.middleware.dataSharing.distributedDataManagement.dataSharingMessages.*;
import eu.popeye.middleware.dataSharing.distributedDataManagement.treeStructure.TSDiff;
import eu.popeye.middleware.groupmanagement.management.Workgroup;
import eu.popeye.middleware.groupmanagement.membership.Member;
import eu.popeye.middleware.naming.search.CtxSearch;
import eu.popeye.middleware.dataSearch.SearchEngine;
import eu.popeye.networkabstraction.communication.ApplicationMessageListener;
import eu.popeye.networkabstraction.communication.CommunicationChannel;
import eu.popeye.networkabstraction.communication.TimeoutException;
import eu.popeye.networkabstraction.communication.message.PopeyeMessage;

public class SharedSpacePeer extends SharedSpace implements ApplicationMessageListener {

    private CacheManager cache;

    private DataTransfer dataTransfer;

    private ReplicationScheme replication;

    private SearchEngine searchEngine;

    private Workgroup groupCommunication;

    private CommunicationChannel comChan;

    private EventContext naming;

    private InetAddress localIP;

    private static final String prefix = "_DM_";

    private String suffix;

    private Member myself;

    private int dataIDCounter = 0;

    private Object counterLock = new Object();

    NamingGUI namingGUI = null;

    private static Pattern validPathRegExp = Pattern.compile("(/[A-Za-z][A-Za-z0-9_\\-\\.\\ ]*)++");

    private SharedSpacePeer(Workgroup comGroup, String SharedSpaceName, String homeDirectory) {
        name = SharedSpaceName;
        groupCommunication = comGroup;
        this.myself = comGroup.getLocalMember();
        this.naming = comGroup.getContext();
        this.localIP = comGroup.getLocalMember().getInetAddress();
        suffix = "_" + localIP.toString();
        this.cleanNamingReplicas();
        comGroup.createNamedCommunicationChannel(name + "_default");
        comChan = comGroup.getNamedCommunicationChannel(name + "_default");
        comChan.addApplicationMessageListener(this);
        this.searchEngine = new SearchEngine(this.myself, comGroup, homeDirectory + File.separator + "INDEX");
        comGroup.createNamedCommunicationChannel(name + "_coherence");
        CoherenceCommunication.instance(comGroup.getNamedCommunicationChannel(name + "_coherence"), name, myself);
        this.replication = new ProactiveReplication(comGroup, this.prefix, this);
    }

    public static SharedSpacePeer createSharedSpace(Workgroup comGroup, String SharedSpaceName, String homeDirectory) {
        CacheManager cm;
        SharedSpacePeer newSS = new SharedSpacePeer(comGroup, SharedSpaceName, homeDirectory);
        try {
            cm = CacheManager.createCacheManager(newSS.myself.getKey().toString(), new File(homeDirectory + File.separator + "CACHE"));
        } catch (CacheManagerException e) {
            e.printStackTrace();
            return null;
        }
        newSS.setCache(cm);
        newSS.createDataTransfer();
        newSS.searchEngine.reset();
        return newSS;
    }

    public static SharedSpacePeer joinSharedSpace(Workgroup comGroup, String SharedSpaceName, String homeDirectory) {
        CacheManager cm = null;
        SharedSpacePeer newSS = new SharedSpacePeer(comGroup, SharedSpaceName, homeDirectory);
        List<Member> membersList = comGroup.getMembers();
        ArrayList<String> membersIDList = new ArrayList<String>();
        for (int i = 0; i < membersList.size(); i++) {
            membersIDList.add(membersList.get(i).getKey().toString());
        }
        Member getFrom = null;
        int i = 0;
        while (membersList.get(i).equals(newSS.myself)) i++;
        if (i < membersList.size()) getFrom = membersList.get(i); else return null;
        PopeyeMessage msg = null;
        try {
            newSS.comChan.setSynchronousQueueEnabled(true);
            newSS.comChan.send(getFrom, new PullTreeStructureMsg(newSS.myself));
            boolean ok = false;
            while (!ok) {
                msg = newSS.comChan.receive(2 * 1000 * 60);
                if (msg instanceof TreeStructureCarrierMsg) {
                    break;
                }
            }
            newSS.comChan.setSynchronousQueueEnabled(false);
        } catch (TimeoutException e) {
            return null;
        }
        try {
            cm = CacheManager.createCacheManager(newSS.myself.getKey().toString(), new File(homeDirectory + File.separator + "CACHE"), ((TreeStructureCarrierMsg) msg).treeStructure);
        } catch (CacheManagerException e) {
            return null;
        }
        newSS.setCache(cm);
        newSS.createDataTransfer();
        newSS.searchEngine.reset();
        return newSS;
    }

    public static SharedSpacePeer restoreSharedSpace(Workgroup comGroup, String SharedSpaceName, String homeDirectory) {
        CacheManager cm;
        SharedSpacePeer newSS = new SharedSpacePeer(comGroup, SharedSpaceName, homeDirectory);
        try {
            cm = CacheManager.loadCacheManager(newSS.myself.getKey().toString(), new File(homeDirectory + File.separator + "CACHE"));
        } catch (CacheManagerException e) {
            return null;
        }
        newSS.setCache(cm);
        newSS.createDataTransfer();
        return newSS;
    }

    public static SharedSpacePeer rejoinSharedSpace(Workgroup comGroup, String SharedSpaceName, String homeDirectory) {
        CacheManager cm = null;
        SharedSpacePeer newSS = new SharedSpacePeer(comGroup, SharedSpaceName, homeDirectory);
        List<Member> membersList = comGroup.getMembers();
        ArrayList<String> membersIDList = new ArrayList<String>();
        for (int i = 0; i < membersList.size(); i++) {
            membersIDList.add(membersList.get(i).getKey().toString());
        }
        Member getFrom = null;
        int i = 0;
        while (membersList.get(i).equals(newSS.myself)) i++;
        if (i < membersList.size()) getFrom = membersList.get(i); else return null;
        PopeyeMessage msg = null;
        try {
            newSS.comChan.setSynchronousQueueEnabled(true);
            newSS.comChan.send(getFrom, new PullTreeStructureMsg(newSS.myself));
            boolean ok = false;
            while (!ok) {
                msg = newSS.comChan.receive(2 * 1000 * 60);
                if (msg instanceof TreeStructureCarrierMsg) break;
            }
            newSS.comChan.setSynchronousQueueEnabled(false);
        } catch (TimeoutException e) {
            return null;
        }
        TSDiff diff = new TSDiff();
        try {
            CacheManager.loadCacheManager(newSS.myself.getKey().toString(), new File(homeDirectory + File.separator + "CACHE"), ((TreeStructureCarrierMsg) msg).treeStructure, membersIDList, diff);
        } catch (CacheManagerException e) {
            return null;
        }
        newSS.setCache(cm);
        newSS.createDataTransfer();
        return newSS;
    }

    private void setCache(CacheManager cm) {
        this.cache = cm;
    }

    ;

    private void createDataTransfer() {
        this.groupCommunication.createNamedCommunicationChannel(name + "_dataTranfer");
        dataTransfer = new DataTransfer(groupCommunication.getNamedCommunicationChannel(name + "_dataTranfer"), myself, cache, name);
        Thread thread = new Thread(dataTransfer);
        thread.start();
    }

    private void unbindReplicas() {
        Object[] localReplicas = cache.getLocalReplicas().toArray();
        for (int i = 0; i < localReplicas.length; i++) try {
            naming.unbind(prefix + (String) localReplicas[i] + suffix);
        } catch (NamingException e) {
            continue;
        }
    }

    private void cleanNamingReplicas() {
        System.out.println("CLEANING NAMING (IN CASE PREVIOUS SESSION WAS NOT STOPPED PROPERLY)");
        NamingEnumeration<Binding> hostingEnum = this.groupCommunication.getContextSearch().search(prefix + "*" + suffix);
        try {
            while (hostingEnum.hasMore()) {
                Binding binding = (Binding) hostingEnum.next();
                System.out.println("CLEANING NAMING: " + binding.getName());
                naming.unbind(binding.getName());
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private void closeChannels() {
        this.groupCommunication.destroyNamedCommunicationChannel(name + "_default");
        this.groupCommunication.destroyNamedCommunicationChannel(name + "_dataTranfer");
        this.groupCommunication.destroyNamedCommunicationChannel(name + "_coherence");
    }

    public void leave() {
        System.err.println("LEAVING SHARED SPACE: " + this.name + "...");
        unbindReplicas();
        this.replication.stopReplication();
        closeChannels();
        this.cache.save();
        System.err.println("LEAVING SHARED SPACE: " + this.name + " DONE");
    }

    public void quit() {
        System.err.println("QUITING SHARED SPACE: " + this.name + "...");
        unbindReplicas();
        this.replication.stopReplication();
        Iterator it = cache.getDataOwnedBy(this.myself.getKey().toString()).iterator();
        while (it.hasNext()) {
            this.comChan.sendGroup(new RemoveDataMsg(myself, (String) it.next()));
        }
        closeChannels();
        this.cache.destroy();
    }

    @Override
    public boolean exists(String dataLocation) {
        return (this.cache.dataExists(dataLocation) || this.cache.directoryExists(dataLocation));
    }

    @Override
    public SharedData accessRead(String path) throws DataDoesNotExistException, CouldNotGetException, CouldNotGetLastVersionException {
        SharedDataContainer SDC = getData(path);
        return SDC.getData();
    }

    @Override
    public SharedData accessWrite(String path) throws DataDoesNotExistException, CouldNotGetLastVersionException, CouldNotGetException, NotAllowedException {
        SharedDataContainer SDC = getData(path);
        Metadata md = SDC.getMetadata();
        SharingMode mode = SharingMode.valueOf((String) md.get(Metadata.SHARINGMODE));
        switch(mode) {
            case readOnly:
                throw new NotAllowedException();
            case OwnerOnly:
                if (myself.getKey().toString().equals(md.get(Metadata.OWNER))) {
                    SDC.coherence.acquire();
                    return SDC.getData();
                } else throw new NotAllowedException();
            case SWMR:
                SDC.coherence.acquire();
                return SDC.getData();
            case MWMR:
                throw new NotAllowedException();
            default:
                throw new NotAllowedException();
        }
    }

    @Override
    public void commit(String path, SharedData newData) throws NotAllowedException, DataDoesNotExistException, CouldNotGetLastVersionException, CouldNotGetException {
        SharedDataContainer SDC = getData(path);
        Metadata md = SDC.getMetadata();
        SharingMode mode = SharingMode.valueOf((String) md.get(Metadata.SHARINGMODE));
        switch(mode) {
            case readOnly:
                throw new NotAllowedException();
            case OwnerOnly:
                if (myself.getKey().toString().equals(md.get(Metadata.OWNER))) {
                    SDC.setData(newData, true);
                    SDC.coherence.release();
                    break;
                } else throw new NotAllowedException();
            case SWMR:
                System.err.println("Trying to commit: " + path);
                SDC.setData(newData, true);
                SDC.coherence.release();
                break;
            case MWMR:
                throw new NotAllowedException();
            default:
                throw new NotAllowedException();
        }
    }

    @Override
    public void copyBranch(String path) {
    }

    @Override
    public Document listData() {
        return cache.getTreeStructureView();
    }

    @Override
    public void shareData(SharedData data, String dataLocation, SharingMode mode, boolean isDirectory) throws DataAlreadyExistException, InvalidPathException {
        if (isDirectory) {
            cache.addDirectoryToTS(dataLocation);
            comChan.sendGroup(new AddDirectoryMsg(myself, dataLocation));
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DIRECTORY_ADDED, dataLocation, myself.getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
        } else {
            cache.addDataToTS(dataLocation, myself.getKey().toString(), false);
            SharedDataContainer SDC = null;
            try {
                SDC = cache.getSDC(dataLocation);
            } catch (DataDoesNotExistException e) {
            }
            if (SDC.getData() != null) throw new DataAlreadyExistException();
            Metadata md = null;
            try {
                md = new Metadata(dataLocation, "", mode, new DataIdentifier(myself.getKey().toString(), this.getNextDataID()));
                SDC.setData(data, true);
                md.set(Metadata.CLASS, data.getClass().getName());
                md.set(Metadata.SIZE, SDC.getDataSize() / 1024 + " KB");
                Date date = new Date();
                md.set(Metadata.CREATIONDATE, date.toString());
                SDC.setCached(true);
                SDC.setMetadata(md, true);
                this.searchEngine.addMetadata(md);
            } catch (MetadataException e) {
                e.printStackTrace();
            }
            switch(mode) {
                case readOnly:
                    ReadOnlyCoherence c0 = new ReadOnlyCoherence(SDC, name);
                    SDC.setCoherence(c0);
                    break;
                case OwnerOnly:
                    OwnerOnlyCoherence c1 = new OwnerOnlyCoherence(SDC, name);
                    SDC.setCoherence(c1);
                    break;
                case SWMR:
                    RaymondDMECoherence c2 = new RaymondDMECoherence(SDC, name, myself);
                    SDC.setCoherence(c2);
                    break;
                case MWMR:
                    break;
            }
            try {
                this.naming.bind(prefix + dataLocation + suffix, myself);
            } catch (NamingException e) {
                e.printStackTrace();
            }
            comChan.sendGroup(new newDataMsg(myself, md, false));
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DATA_ADDED, dataLocation, myself.getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
        }
    }

    @Override
    public void withdrawData(String path) throws RemoteException, DataDoesNotExistException, NotAllowedException {
        if (!this.isValidPath(path)) return;
        if (cache.isDirectory(path)) {
            cache.removeDirectoryFromTS(path);
            comChan.sendGroup(new RemoveDirectoryMsg(myself, path));
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DIRECTORY_REMOVED, path, myself.getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
        } else {
            SharedDataContainer SDC = cache.getSDC(path);
            synchronized (SDC) {
                if (SDC.isCached()) try {
                    this.naming.unbind(prefix + path + suffix);
                } catch (NamingException e) {
                }
                this.searchEngine.removeMetadata(path);
                cache.removeDataFromTS(path, myself.getKey().toString());
            }
            comChan.sendGroup(new RemoveDataMsg(myself, path));
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DATA_REMOVED, path, myself.getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
        }
    }

    @Override
    public Metadata readMetadata(String path) throws InvalidPathException, DataDoesNotExistException, CouldNotGetException {
        SharedDataContainer SDC = getMetadata(path);
        return (Metadata) SDC.getMetadata().clone();
    }

    @Override
    public void writeMetadata(String path, String field, Object newValue) throws InvalidPathException, MetadataException, NotAllowedException, DataDoesNotExistException {
        if (field == Metadata.OWNER || field == Metadata.SHARINGMODE || field == Metadata.PATH) throw new NotAllowedException();
        SharedDataContainer SDC;
        try {
            SDC = getMetadata(path);
        } catch (CouldNotGetException e) {
            throw new MetadataException();
        }
        if (!SDC.getMetadata().get(Metadata.OWNER).equals(this.myself.getKey().toString())) {
            throw new NotAllowedException();
        } else {
            synchronized (SDC) {
                SDC.getMetadata().set(field, newValue);
            }
        }
        this.searchEngine.updateMetadata(SDC.getMetadata());
        SDC.coherence.updateMetadata();
    }

    @Override
    public HitsCollection searchData(String Query, String expectedMetadataAttribute) {
        return this.searchEngine.search(Query, expectedMetadataAttribute, false);
    }

    @Override
    public void snapshot(String popeyePath, String destinationPath) {
        Iterator dirIterator = this.cache.getTreeStructure().getDirectoryList().iterator();
        while (dirIterator.hasNext()) {
            String directoryPath = (String) dirIterator.next();
            if (directoryPath.startsWith(popeyePath) && !directoryPath.equals(popeyePath)) {
                String finalPath = destinationPath + directoryPath.substring(popeyePath.lastIndexOf("/") + 1);
                File dir = new File(finalPath);
                dir.mkdirs();
            }
        }
        try {
            Iterator fileIterator = this.cache.getTreeStructure().getDataList().iterator();
            while (fileIterator.hasNext()) {
                String filePath = (String) fileIterator.next();
                if (filePath.startsWith(popeyePath)) {
                    SharedData data = this.accessRead(filePath);
                    String finalPath = destinationPath + File.separator + filePath.substring(popeyePath.lastIndexOf("/") + 1);
                    data.save(finalPath);
                }
            }
        } catch (DataDoesNotExistException e) {
            e.printStackTrace();
        } catch (CouldNotGetException e) {
            e.printStackTrace();
        } catch (CouldNotGetLastVersionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveData(String oldPath, String newPath) {
    }

    @Override
    public void purgeLocalDevice() {
    }

    public void onMessage(PopeyeMessage msg) {
        if (!(msg instanceof DataSharingMsg) || ((DataSharingMsg) msg).getSender().equals(myself)) return;
        if (msg instanceof newDataMsg) {
            Metadata md = ((newDataMsg) msg).getMetadata();
            try {
                cache.addDataToTS((String) md.get(Metadata.PATH), (String) md.get(Metadata.OWNER), ((newDataMsg) msg).getGhost());
            } catch (InvalidPathException e) {
            } catch (DataAlreadyExistException e) {
            }
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DATA_ADDED, ((newDataMsg) msg).getPath(), ((DataSharingMsg) msg).getSender().getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
            return;
        } else if (msg instanceof RemoveDataMsg) {
            String path = ((RemoveDataMsg) msg).getPath();
            try {
                naming.unbind(prefix + path + suffix);
            } catch (NamingException e1) {
            }
            try {
                cache.removeDataFromTS(path, ((RemoveDataMsg) msg).getSender().getKey().toString());
            } catch (DataDoesNotExistException e) {
            } catch (NotAllowedException e) {
            }
            this.searchEngine.removeMetadata(path);
            this.dataTransfer.cancelDataTransfer(path);
            this.dataTransfer.cancelMetadataTransfer(path);
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DATA_REMOVED, ((RemoveDataMsg) msg).getPath(), ((DataSharingMsg) msg).getSender().getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
            return;
        } else if (msg instanceof RemoveDirectoryMsg) {
            try {
                cache.removeDirectoryFromTS(((RemoveDirectoryMsg) msg).path);
            } catch (DataDoesNotExistException e) {
            } catch (NotAllowedException e) {
            }
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DIRECTORY_REMOVED, ((RemoveDirectoryMsg) msg).getPath(), ((DataSharingMsg) msg).getSender().getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
            return;
        } else if (msg instanceof AddDirectoryMsg) {
            try {
                cache.addDirectoryToTS(((AddDirectoryMsg) msg).path);
            } catch (InvalidPathException e) {
            } catch (DataAlreadyExistException e) {
            }
            SharedSpaceEvent event = new SharedSpaceEvent(SharedSpaceEvent.DIRECTORY_ADDED, ((AddDirectoryMsg) msg).getPath(), ((DataSharingMsg) msg).getSender().getUsername(), cache.getTreeStructureView());
            this.notifyTreeStructureChanged(event);
        } else if (msg instanceof PullTreeStructureMsg) {
            TreeStructureCarrierMsg rep = new TreeStructureCarrierMsg(myself, this.cache.getTreeStructure());
            comChan.send(((PullTreeStructureMsg) msg).getSender(), rep);
        }
    }

    private SharedDataContainer getMetadata(String path) throws DataDoesNotExistException, CouldNotGetException {
        SharedDataContainer SDC = cache.getSDC(path);
        synchronized (SDC.waitOnMD) {
            try {
                if (SDC.getMetadata() != null && !SDC.isDirtyMD()) return SDC; else {
                    if (SDC.isMetadataTransfering() || SDC.isDataTransfering()) SDC.waitOnMD.wait(); else {
                        ArrayList<Member> alreadyAsked = new ArrayList<Member>();
                        boolean tryAgain = true;
                        while (tryAgain) {
                            ReplicationResult repres = this.replication.accessData(path, alreadyAsked);
                            if (repres.host == null) throw new CouldNotGetException();
                            this.dataTransfer.getMetadata(path, repres.host, false);
                            SDC.setMetadataTransfering(true);
                            SDC.waitOnMD.wait();
                            alreadyAsked.add(repres.host);
                            if (SDC.getAbsolutePath() != null) {
                                tryAgain = false;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (SDC.getMetadata() == null) throw new CouldNotGetException();
        return SDC;
    }

    private SharedDataContainer getData(String path) throws DataDoesNotExistException, CouldNotGetLastVersionException, CouldNotGetException {
        SharedDataContainer SDC = cache.getSDC(path);
        ReplicationResult repres = null;
        synchronized (SDC.waitOnData) {
            try {
                if (SDC.isCached() && !SDC.isDirty()) return SDC; else {
                    if (SDC.isDataTransfering()) SDC.waitOnData.wait(); else {
                        List<Member> alreadyAsked = new ArrayList<Member>();
                        boolean tryAgain = true;
                        while (tryAgain) {
                            if (SDC.isCached()) repres = new ReplicationResult(false, SDC.getLastEditionBy()); else repres = this.replication.accessData(path, alreadyAsked);
                            if (repres.host == null) throw new CouldNotGetException();
                            System.err.println("Asking data to: " + repres.host.getInetAddress() + " (I am: " + this.myself.getInetAddress() + ")");
                            this.dataTransfer.getData(path, repres.host, repres.replicate);
                            SDC.setDataTransfering(true);
                            SDC.setMetadataTransfering(true);
                            SDC.waitOnData.wait();
                            alreadyAsked.add(repres.host);
                            if (SDC.getData() != null) {
                                tryAgain = false;
                            }
                        }
                    }
                }
            } catch (InterruptedException e) {
                return null;
            }
        }
        if (SDC.getData() == null) throw new CouldNotGetException();
        if (SDC.isDirty()) throw new CouldNotGetLastVersionException();
        if (repres.replicate) {
            this.bindData(path, SDC, repres.host);
        }
        if (SDC.coherence == null) {
            this.setCoherence(SDC, repres.host);
        }
        return SDC;
    }

    public void bindData(String dataPath, SharedDataContainer sdc, Member provider) {
        this.searchEngine.addMetadata(sdc.getMetadata());
        sdc.setLastEditionBy(provider);
        try {
            this.naming.bind(prefix + dataPath + suffix, myself);
        } catch (NamingException e) {
        }
    }

    public void setCoherence(SharedDataContainer sdc, Member provider) {
        SharingMode mode = SharingMode.valueOf((String) sdc.getMetadata().get(Metadata.SHARINGMODE));
        switch(mode) {
            case readOnly:
                ReadOnlyCoherence c0 = new ReadOnlyCoherence(sdc, this.name);
                c0.addNeighbour(provider);
                sdc.setCoherence(c0);
                break;
            case OwnerOnly:
                OwnerOnlyCoherence c1 = new OwnerOnlyCoherence(sdc, this.name);
                c1.addNeighbour(provider);
                sdc.setCoherence(c1);
                break;
            case SWMR:
                RaymondDMECoherence c2 = new RaymondDMECoherence(sdc, name, provider);
                sdc.setCoherence(c2);
                c2.addNeighbour(provider);
                break;
            case MWMR:
                break;
        }
    }

    private int getNextDataID() {
        int res;
        synchronized (this.counterLock) {
            res = this.dataIDCounter;
            this.dataIDCounter++;
        }
        return res;
    }

    private static boolean isValidPath(String path) {
        Matcher m = validPathRegExp.matcher(path);
        return m.matches();
    }

    public SearchEngine getSearchEngine() {
        return this.searchEngine;
    }

    public CacheManager getCacheManager() {
        return this.cache;
    }

    public DataTransfer getDataTransfer() {
        return this.dataTransfer;
    }

    private class NamingGUI extends Thread {

        private JTextArea debugArea;

        private CtxSearch namingSearch;

        public NamingGUI(CtxSearch search, String sharedSpaceName) {
            this.namingSearch = search;
            debugArea = new JTextArea();
            JFrame frame = new JFrame("Naming Service Debug Area - " + sharedSpaceName);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new JScrollPane(debugArea));
            this.showNamingService("INITIALIZATION");
            frame.pack();
            frame.setSize(new Dimension(400, 400));
            frame.setVisible(true);
        }

        public void addDebugMsg(String msg) {
            this.debugArea.append(msg + "\n");
        }

        public void run() {
            for (; ; ) {
                try {
                    this.sleep(1000);
                    this.showNamingService("");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void showNamingService(String header) {
            this.debugArea.setText("");
            this.addDebugMsg("--- NAMING SERVICE CONTENT (" + header + ")---");
            System.err.println("--- NAMING SERVICE CONTENT (" + header + ")---");
            NamingEnumeration enume = this.namingSearch.search("*");
            boolean empty = true;
            try {
                while (enume.hasMore()) {
                    empty = false;
                    Binding binding = (Binding) enume.next();
                    this.addDebugMsg("   " + binding.getName() + binding.toString() + "\n");
                    System.err.println("   " + binding.getName() + binding.toString());
                }
                if (empty) {
                    this.addDebugMsg("NAMING SERVICE IS EMPTY");
                    System.err.println("NAMING SERVICE IS EMPTY");
                }
                this.addDebugMsg("-----------------------");
                System.err.println("-----------------------");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }
}
