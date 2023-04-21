package com.daffodilwoods.daffodildb.server.datasystem.indexsystem;

import com.daffodilwoods.daffodildb.server.datasystem.btree.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.ClusterCharacteristics;
import java.util.*;
import com.daffodilwoods.daffodildb.utils.byteconverter.CbCzufIboemfs;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.Cluster;
import com.daffodilwoods.daffodildb.utils.byteconverter.CUbcmfLfzIboemfs;
import com.daffodilwoods.daffodildb.utils.byteconverter.CbCUsffWbmvfIboemfs;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.PersistentDatabase;
import com.daffodilwoods.daffodildb.server.datasystem.persistentsystem.*;

/**
 * FileNodeManager is used to create new node and providing any existing filenode with the help
 * of clusterProvider for read or write operations. it maintains a map to have mapping of btreeNodes
 * corresponing to clusterCharacteristics of cluster of these nodes.to provide any node first it checks
 * in map if it gets node from map then returns otherwise makes new BtreeNode Object and puts in Map.
 * It also maintains BtreeControlClutser operations with the help of BTreeControlCluster class. It provides
 * a root node also , if root node already exists then returns it otherwise  makes new node and stores root
 * node address and btree size in control cluster.
 *
 *
 * <p>Title: FileNodeManager
 */
public class FileNodeManager implements _NodeManager {

    /**
    * Used for getting a new node or already existed node
    */
    private _ClusterProvider clusterProvider;

    /**
    * IndexInformation of Btree for which this Manager is
    */
    private IndexColumnInformation iinf;

    /**
    * Control Cluster of Btree
    */
    private BTreeControlCluster btreeControl;

    /**
    * size of btree
    */
    private int size;

    /**
    * both contains the already loaded node
    */
    private Map nodeMap;

    /**
    * Btree for which this manager is.It is also required while creating a new node.
    */
    private _Index btree;

    /**
    * Maximum nodes which can reside in node map .
    */
    private static int Node_Limit = 10;

    private CbCUsffWbmvfIboemfs handler;

    private boolean isTempDatabase;

    /**
    * Constructs FileNodeManager and initializes IndexInforamtions , clusterProvider and constructs
    * BtreeControlCluster with the help of given Btree control Cluster address.
    * @param iinf1 indexinformations to provide handlers to convert bytes of keys into Object in BtreeElement
    * class
    * @param clusterProvider0 To provide cluster of btreeNodes for read and write
    * @param address address of btree control cluster
    */
    public FileNodeManager(IndexColumnInformation iinf1, _ClusterProvider clusterProvider0, int address, CbCUsffWbmvfIboemfs handler0, boolean isDefault) throws DException {
        iinf = iinf1;
        clusterProvider = clusterProvider0;
        double version = clusterProvider.getVersionHandler().getDbVersion();
        btreeControl = address == -1 ? new TempIndexBtreeControlCluster(address, clusterProvider) : version > 3.2 && !isDefault ? (BTreeControlCluster) new IndexBtreeControlCluster(address, clusterProvider) : new DefaultBTreeControlCluster(address, clusterProvider);
        size = btreeControl.getSize();
        nodeMap = new HashMap();
        handler = handler0;
        isTempDatabase = ((PersistentDatabase) clusterProvider).getDatabaseName().equalsIgnoreCase(DatabaseConstants.TEMPDATABASE);
    }

    /**
    * Returns root node of btree. First it checks in control cluster if root node address is null then
    * returns null.Then it checks cluster corresponding to this rootClusteraddress in map if it finds in
    * map then ckecks if this node is required for write then adds cluster corresponding to this node in
    * user and then returns.If it does not get from map then gets cluster for read or write from
    * clusterProvider, makes File Node and btreeNode Object, puts its entry in map and returns BTreeNode.
    * gets root node address from control cluster otherwise returns null.
    * @param user if node is required for write then adds cluster corresponding to node in user
    * @return rootNode if exists otherwise null
    */
    public BTreeNode getRootNode(_DatabaseUser user) throws DException {
        int rootClusterAddress = btreeControl.getRootClusterAddress();
        if (rootClusterAddress == 0) return null;
        ClusterCharacteristics cc = new ClusterCharacteristics(rootClusterAddress, true);
        BTreeNode btreeNode = (BTreeNode) nodeMap.get(cc);
        if (btreeNode != null) {
            if (user != null) {
                Cluster cls = ((FixedFileNode) btreeNode.getNode()).cluster;
                if (cls.readMode) cls = clusterProvider.getCluster(user, cc);
                user.addCluster(cls);
            }
            return btreeNode;
        }
        return getNode(user, cc);
    }

    /**
    * Makes new cluster with the help of clusterProvider, Initializes cluster header.makes File Node
    * and BtreeNode and puts BtreeNode Entry in Map.
    * @param user to get Node For Write Operations
    * @return New Btree Node
    */
    public synchronized BTreeNode getNewNode(_DatabaseUser user) throws DException {
        FixedBTreeCluster cluster = (FixedBTreeCluster) clusterProvider.getNewCluster(user);
        cluster.setHeader();
        FixedFileNode fileNode = iinf.isVariableColumn() ? new VariableFileNode(cluster, iinf, handler) : new FixedFileNode(cluster, iinf, handler);
        BTreeNode node = new BTreeNode(btree, fileNode);
        updateNodeMapping(cluster.getClusterCharacteristics(), node);
        return node;
    }

    /**
    * Updates Total Btree size, if incremnent variable is true then it increments size by 1 otherwise
    * decreases size by 1
    * Updates root node address and BtreeSize in BtreeControl Cluster with the help of BtreeControlCluster
    * @param increment whether btreeSize has to increase or decrease
    * @param user To get control cluster for write
    */
    public void updateSizeAndBTreeInfo(_DatabaseUser user, boolean increment, BTreeNode rootNode) throws DException {
        size += increment ? 1 : -1;
        if (size == 0) addInFreeList(user, rootNode);
        btreeControl.updateBTreeInfo(user, rootNode, size);
    }

    /**
    * returns total BtreeSize
    * @returns total BtreeSize
    */
    public int getSize() {
        return size;
    }

    /**
    * provides node corresponding to given nodeKey. First it checks node corresponding to
    * clustercharacteristics of node in Map. if it finds in map then ckecks if this
    * node is required for write then adds cluster corresponding to this node in user and then returns.
    * If it does not get from map then gets cluster for read or write from clusterProvider, makes File
    * Node and btreeNode Object, puts its entry in map and returns BTreeNode.
    *
    * @param user if node is required for write then adds cluster corresponding to node in user
    * @param nodeKey whose corresponding node is required
    * @return BTreeNode corresponding to given nodeKey
    */
    public synchronized BTreeNode getNode(_DatabaseUser user, Object nodekey) throws DException {
        ClusterCharacteristics cc = (ClusterCharacteristics) nodekey;
        BTreeNode btreeNode = (BTreeNode) nodeMap.get(cc);
        if (btreeNode != null) {
            if (user != null) {
                _Node node = btreeNode.getNode();
                Cluster cls = ((FixedFileNode) node).cluster;
                if (cls.readMode) cls = clusterProvider.getCluster(user, cc);
                user.addCluster(cls);
            }
            return btreeNode;
        }
        FixedBTreeCluster cluster = user == null ? (FixedBTreeCluster) clusterProvider.getReadCluster(cc) : (FixedBTreeCluster) clusterProvider.getCluster(user, cc);
        FixedFileNode fileNode = iinf.isVariableColumn() ? new VariableFileNode(cluster, iinf, handler) : new FixedFileNode(cluster, iinf, handler);
        btreeNode = new BTreeNode(btree, fileNode);
        updateNodeMapping(cc, btreeNode);
        return btreeNode;
    }

    /**
    * sets Btree which is required to set In BtreeNode
    * @param btree0 To set in BtreeNode
    */
    public void setBTree(_Index btree0) {
        btree = btree0;
    }

    /**
    * returns handlers corresponing to columns types of columns in Btree from IndexInformations
    * @return handlers corresponing to columns types of columns in Btree from IndexInformations
    */
    public CbCzufIboemfs[] getByteHandlers() throws DException {
        return iinf.getHandlers();
    }

    /**
    * returns column types of columns on which Btree is maintained from IndexInformations
    * @return column types of columns on which Btree is maintained from IndexInformations
    */
    public int[] getColumnTypes() throws DException {
        return iinf.getTypeOfColumns();
    }

    final void updateNodeMapping(ClusterCharacteristics clusterCharacteristics, BTreeNode node) throws DException {
        if (nodeMap.size() > Node_Limit) {
            synchronized (this) {
                if (nodeMap.size() > Node_Limit) {
                    ArrayList arr = new ArrayList(5);
                    Iterator iter = nodeMap.keySet().iterator();
                    int cnt = 0;
                    while (iter.hasNext() && ++cnt < Node_Limit / 2) {
                        BTreeNode obj = (BTreeNode) nodeMap.get(iter.next());
                        if (!obj.isNodeCanBeRemovedFromMap()) {
                            cnt--;
                            continue;
                        }
                        arr.add(obj);
                    }
                    for (int i = 0; i < arr.size(); i++) {
                        BTreeNode kv = (BTreeNode) arr.get(i);
                        kv.setFlagForNodeRemovedFromMap();
                        FixedFileNode fixedFileNode = (FixedFileNode) kv.getNode();
                        Cluster cluster = fixedFileNode.cluster;
                        ClusterCharacteristics cc = cluster.getClusterCharacteristics();
                        kv.clearReferences();
                        fixedFileNode.clearReferences();
                        if (cluster.isDirty()) clusterProvider.updateWriteClusters(cluster);
                        fixedFileNode.clearReferences();
                        nodeMap.remove(cc);
                    }
                    arr.clear();
                    Object isFirstTime = nodeMap.put(clusterCharacteristics, node);
                    if (isFirstTime != null) new Exception(" Error in FileNodeManager class getNode or getNewNode method node already found in map from if consult to Kuldeep kumar ").printStackTrace();
                } else {
                    Object isFirstTime = nodeMap.put(clusterCharacteristics, node);
                    if (isFirstTime != null) new Exception(" Error in FileNodeManager class getNode or getNewNode method node already found in map from else consult to Kuldeep kumar ").printStackTrace();
                }
            }
        } else {
            Object isFirstTime = nodeMap.put(clusterCharacteristics, node);
            if (isFirstTime != null) new Exception(" Error in FileNodeManager class getNode or getNewNode method node already found in map from outer else consult to Kuldeep kumar ").printStackTrace();
        }
    }

    private void addInFreeList(_DatabaseUser user, BTreeNode rootNode) throws DException {
        if (rootNode.isLeaf()) return;
        ArrayList freeNodeList = new ArrayList(0);
        BTreeNode childNode;
        for (int i = 0; i < rootNode.getElementCount(); i++) {
            childNode = rootNode.getElement(i, null).getChild();
            if (childNode != null) addInFreeList(user, childNode, freeNodeList);
        }
        ClusterCharacteristics cc;
        for (int i = freeNodeList.size() - 1; i >= 0; i--) {
            if (isTempDatabase && user.getSize() >= 10) user.writeToFileWithLock();
            cc = (ClusterCharacteristics) freeNodeList.get(i);
            nodeMap.remove(cc);
            clusterProvider.addFreeCluster(user, cc.getStartAddress());
        }
        FixedBTreeCluster cluster = ((FixedFileNode) rootNode.getNode()).cluster;
        cluster.setHeader();
        cluster.reInitializeActiveRecordCount();
        cluster.reInitializeActualRecordCount();
        ((FixedFileNode) rootNode.getNode()).clearCache();
        BTreeElement element = new BTreeElement();
        element.setCurrentNode(rootNode);
        ((FixedFileNode) rootNode.getNode()).insertDummyElement(element);
    }

    private void addInFreeListCompletely(_DatabaseUser user, BTreeNode rootNode) throws DException {
        ArrayList freeNodeList = new ArrayList(0);
        BTreeNode childNode;
        for (int i = 0; i < rootNode.getElementCount(); i++) {
            childNode = rootNode.getElement(i, null).getChild();
            if (childNode != null) addInFreeList(user, childNode, freeNodeList);
        }
        freeNodeList.add((ClusterCharacteristics) rootNode.getNode().getNodeKey());
        ClusterCharacteristics cc;
        for (int i = freeNodeList.size() - 1; i >= 0; i--) {
            cc = (ClusterCharacteristics) freeNodeList.get(i);
            nodeMap.remove(cc);
            clusterProvider.addFreeCluster(user, cc.getStartAddress());
            if (isTempDatabase && user.getSize() >= 10) user.writeToFileWithLock();
        }
        btreeControl.releaseControlCluster(user);
    }

    private void addInFreeList(_DatabaseUser user, BTreeNode node, ArrayList freeNodeList) throws DException {
        if (isTempDatabase && user.getSize() >= 10) user.writeToFileWithLock();
        if (!node.isLeaf()) {
            BTreeNode childNode;
            if (node.isNodeRemovedFromMap()) node = getNode(user, node.getNodeKey());
            for (int i = 0; i < node.getElementCount(); i++) {
                childNode = node.getElement(i, null).getChild();
                if (childNode != null) addInFreeList(user, childNode, freeNodeList);
            }
        }
        freeNodeList.add((ClusterCharacteristics) node.getNode().getNodeKey());
    }

    public int getTotalSizeOfNodeMapAndWeakNodeMap() {
        return nodeMap.size();
    }

    public Map getNodeMap() {
        return nodeMap;
    }

    public PersistentDatabase getUnderlyingDatabase() {
        return (PersistentDatabase) clusterProvider;
    }

    public CUbcmfLfzIboemfs getTableKeyHandler() {
        return (CUbcmfLfzIboemfs) handler;
    }

    public void releaseResource(_DatabaseUser user, boolean releaseCompletely) throws DException {
        BTreeNode rootNode = getRootNode(user);
        if (rootNode != null) {
            if (releaseCompletely) addInFreeListCompletely(user, rootNode); else addInFreeList(user, rootNode);
        }
    }

    public void setSize(int size0) {
        size = size0;
    }
}
