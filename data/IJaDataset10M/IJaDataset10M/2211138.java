package cn.edu.thss.iise.beehivez.server.index.petrinetindex.pathindex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;
import cn.edu.thss.iise.beehivez.server.hashfunction.GeneralHashFunctionLibrary;
import cn.edu.thss.iise.beehivez.server.index.bplustree.BplusTreeLong2Long;
import cn.edu.thss.iise.beehivez.server.util.FileUtil;

public class GenericInvertedIndex {

    static final String pathIndexConfigFile = "PathIndexConfig.ini";

    BplusTreeLong2Long bpt = null;

    RandomAccessFile bptFile = null;

    RandomAccessFile fvFile = null;

    InvertedFile invertedList = null;

    static final String bptFileSuffix = ".bpt";

    static final String fvFileSuffix = ".ifl";

    private String indexDirectory = null;

    private String indexName = null;

    public GenericInvertedIndex(String dir, String fileName) {
        this.indexDirectory = dir;
        this.indexName = fileName;
    }

    public boolean destroy() {
        close();
        boolean r = FileUtil.deleteFile(indexDirectory);
        if (!r) {
            System.out.println("index: " + indexDirectory + "/" + indexName + " destory failed");
        }
        return r;
    }

    public void close() {
        try {
            if (bpt != null) {
                bpt.Commit();
                bpt = null;
            }
            if (bptFile != null) {
                bptFile.close();
                bptFile = null;
            }
            if (fvFile != null) {
                fvFile.close();
                fvFile = null;
            }
            if (invertedList != null) {
                invertedList.close();
                invertedList = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @param bplusTreeNodeSize
	 *            the size of node in the B plus tree
	 * @param bplusTreeCacheSize
	 *            the size of B plus tree cache
	 * @param invertedListSize
	 *            the max number of file id in a buffer in the inverted file
	 * @param invertedFileCacheSize
	 *            the cache size of the inverted file
	 * @throws Exception
	 */
    public void createNewIndex(int bplusTreeNodeSize, int bplusTreeCacheSize, int invertedListSize, int invertedFileCacheSize) throws Exception {
        java.io.File fIndexDir = new java.io.File(indexDirectory);
        if (fIndexDir.exists() && fIndexDir.isDirectory()) {
            FileUtil.deleteFile(fIndexDir);
        }
        fIndexDir = new java.io.File(indexDirectory);
        boolean r = fIndexDir.mkdirs();
        if (!r) {
            System.out.println("make directory: " + indexDirectory + " failed");
            return;
        }
        String bptFileName = fIndexDir.getPath() + "/" + this.indexName + bptFileSuffix;
        String fvFileName = fIndexDir.getPath() + "/" + this.indexName + fvFileSuffix;
        this.bptFile = new RandomAccessFile(bptFileName, "rw");
        this.fvFile = new RandomAccessFile(fvFileName, "rw");
        this.bpt = BplusTreeLong2Long.InitializeInStream(this.bptFile, bplusTreeNodeSize, bplusTreeCacheSize);
        this.bpt.Commit();
        this.invertedList = InvertedFile.initializeInvertedFile(fvFile, invertedListSize, invertedFileCacheSize);
    }

    public void createNewIndex() throws Exception {
        FileInputStream fin = new FileInputStream(pathIndexConfigFile);
        Properties p = new Properties();
        p.load(fin);
        String BplusTreeNodeSize = p.getProperty("BplusTreeNodeSize", "100");
        String BplusTreeCacheSize = p.getProperty("BplusTreeCacheSize", "500");
        String InvertedListSize = p.getProperty("InvertedListSize", "100");
        String InvertedFileCacheSize = p.getProperty("InvertedFileCacheSize", "1000");
        fin.close();
        int bplusTreeNodeSize = Integer.parseInt(BplusTreeNodeSize);
        int bplusTreeCacheSize = Integer.parseInt(BplusTreeCacheSize);
        int invertedListSize = Integer.parseInt(InvertedListSize);
        int invertedFileCacheSize = Integer.parseInt(InvertedFileCacheSize);
        createNewIndex(bplusTreeNodeSize, bplusTreeCacheSize, invertedListSize, invertedFileCacheSize);
    }

    public void setupFromExistingIndex() throws Exception {
        FileInputStream fin = new FileInputStream(pathIndexConfigFile);
        Properties p = new Properties();
        p.load(fin);
        String BplusTreeCacheSize = p.getProperty("BplusTreeCacheSize", "500");
        String InvertedFileCacheSize = p.getProperty("InvertedFileCacheSize", "1000");
        fin.close();
        int bplusTreeCacheSize = Integer.parseInt(BplusTreeCacheSize);
        int invertedFileCacheSize = Integer.parseInt(InvertedFileCacheSize);
        setupFromExistingIndex(bplusTreeCacheSize, invertedFileCacheSize);
    }

    public void setupFromExistingIndex(int bplusTreeCacheSize, int invertedFilecacheSize) throws Exception {
        java.io.File fIndexDir = new java.io.File(indexDirectory);
        if (!fIndexDir.isDirectory() || !fIndexDir.exists()) {
            System.out.println("index not exist");
            return;
        }
        String bptFileName = fIndexDir.getPath() + "/" + this.indexName + bptFileSuffix;
        String fvFileName = fIndexDir.getPath() + "/" + this.indexName + fvFileSuffix;
        java.io.File f = new java.io.File(bptFileName);
        if (!f.isFile()) {
            System.out.println("B+ tree index not exist");
            return;
        }
        f = new java.io.File(fvFileName);
        if (!f.isFile()) {
            System.out.println("file vector file not exist");
            return;
        }
        this.bptFile = new RandomAccessFile(bptFileName, "rw");
        this.fvFile = new RandomAccessFile(fvFileName, "rw");
        this.bpt = BplusTreeLong2Long.SetupFromExistingStream(this.bptFile, bplusTreeCacheSize);
        this.invertedList = InvertedFile.setupFromExistingInvertedFile(fvFile, invertedFilecacheSize);
    }

    private long makeIndexKey(String item) {
        long itemKey = GeneralHashFunctionLibrary.Hash(item.trim());
        return itemKey;
    }

    public HashSet query(PathQueryExpression e) {
        HashSet res = new HashSet();
        Vector items = e.getItems();
        if (items.isEmpty()) {
            return res;
        }
        Object o = items.get(0);
        if (o instanceof PathQueryExpression.Atom) {
            PathQueryExpression.Atom a = (PathQueryExpression.Atom) o;
            res.addAll(primitiveQuery(a.getAtom()));
        } else {
            PathQueryExpression qe = (PathQueryExpression) o;
            res.addAll(query(qe));
        }
        switch(e.getType()) {
            case PathQueryExpression.AND:
                for (int i = 1; i < items.size(); i++) {
                    Object item = items.get(i);
                    if (item instanceof PathQueryExpression.Atom) {
                        PathQueryExpression.Atom a = (PathQueryExpression.Atom) item;
                        res.retainAll(primitiveQuery(a.getAtom()));
                    } else {
                        PathQueryExpression qe = (PathQueryExpression) item;
                        res.retainAll(query(qe));
                    }
                }
                break;
            case PathQueryExpression.OR:
                for (int i = 1; i < items.size(); i++) {
                    Object item = items.get(i);
                    if (item instanceof PathQueryExpression.Atom) {
                        PathQueryExpression.Atom a = (PathQueryExpression.Atom) item;
                        res.addAll(primitiveQuery(a.getAtom()));
                    } else {
                        PathQueryExpression qe = (PathQueryExpression) item;
                        res.addAll(query(qe));
                    }
                }
                break;
            case PathQueryExpression.NOT:
                for (int i = 1; i < items.size(); i++) {
                    Object item = items.get(i);
                    if (item instanceof PathQueryExpression.Atom) {
                        PathQueryExpression.Atom a = (PathQueryExpression.Atom) item;
                        res.removeAll(primitiveQuery(a.getAtom()));
                    } else {
                        PathQueryExpression qe = (PathQueryExpression) item;
                        res.removeAll(query(qe));
                    }
                }
                break;
        }
        return res;
    }

    private HashSet primitiveQuery(String item) {
        HashSet res = new HashSet();
        if (this.bpt == null) {
            System.out.println("index not set up");
            return null;
        }
        if (item == null) {
            System.out.println("error parameter");
            return null;
        }
        long itemKey = makeIndexKey(item);
        try {
            if (this.bpt.ContainsKey(itemKey)) {
                long itemID = this.bpt.get(itemKey);
                res = this.invertedList.getFileList(itemID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public void addIndexNode(String node, long id) {
        long itemKey = makeIndexKey(node);
        addIndexItem(itemKey, id);
    }

    private void addIndexItem(long itemKey, long fileID) {
        try {
            if (this.bpt.ContainsKey(itemKey)) {
                long itemID = this.bpt.get(itemKey);
                this.invertedList.addFileID(itemID, fileID);
            } else {
                long itemID = this.invertedList.createNewInvertedList(fileID);
                this.bpt.set(itemKey, itemID);
                this.bpt.Commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
