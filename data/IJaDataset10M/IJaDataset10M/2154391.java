package org.gdi3d.xnavi.navigator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.media.ding3d.BranchGroup;
import javax.swing.JOptionPane;
import javax.media.ding3d.vecmath.Point3d;
import javax.media.ding3d.vecmath.Vector3d;
import org.gdi3d.xnavi.listeners.BusyListener;
import org.gdi3d.xnavi.services.w3ds.FeatureTypeProperties;
import org.gdi3d.xnavi.services.w3ds.W3DS_Layer;
import org.gdi3d.xnavi.services.w3ds.Web3DService;

public class LODThread extends Thread {

    private static Vector<LODJob> queue = new Vector<LODJob>();

    private boolean killed = false;

    private static Vector<BusyListener> busyListeners = null;

    Vector3d currentPosition = null;

    private static int numParallelDownloadThreads = 4;

    private static DownLoadDataThread2[] downloadThreads = new DownLoadDataThread2[numParallelDownloadThreads];

    private static boolean downloadThreadsinitialized = false;

    private static CheckActiveTilesThread checkActiveTilesThread;

    private static boolean checkActiveTilesThreadStarted = false;

    private static Object checkActiveTilesThreadLock = new Object();

    public static List<Tile> activeTiles = new LinkedList<Tile>();

    private static Object activeTilesLock = new Object();

    public static void addActiveTile(Tile tile) {
        synchronized (activeTilesLock) {
            activeTiles.add(tile);
        }
    }

    private LODJob currentJob = null;

    private String lodthread_id;

    public String getLodthread_id() {
        return lodthread_id;
    }

    public void setLodthread_id(String lodthread_id) {
        this.lodthread_id = lodthread_id;
    }

    public LODThread() {
        setPriority(Thread.MIN_PRIORITY);
        synchronized (downloadThreads) {
            if (!downloadThreadsinitialized) {
                for (int i = 0; i < numParallelDownloadThreads; i++) {
                    downloadThreads[i] = new DownLoadDataThread2();
                    downloadThreads[i].setName("DownLoadDataThread2 " + i);
                    downloadThreads[i].start();
                }
                downloadThreadsinitialized = true;
            }
        }
        synchronized (checkActiveTilesThreadLock) {
            if (!checkActiveTilesThreadStarted) {
                checkActiveTilesThread = new CheckActiveTilesThread();
                checkActiveTilesThread.setName("CheckActiveTilesThread");
                checkActiveTilesThread.start();
            }
            checkActiveTilesThreadStarted = true;
        }
    }

    public static void addJob(LODJob job) {
        synchronized (queue) {
            queue.add(job);
        }
    }

    public static void addJobTopPriority(LODJob job) {
        synchronized (queue) {
            queue.insertElementAt(job, 0);
        }
    }

    public static void removeJobs(Tile tile) {
        synchronized (queue) {
            if (false) {
                for (int i = 0; i < Navigator.numLODThreads; i++) {
                    if (Navigator.lodThreads[i].currentJob != null && Navigator.lodThreads[i].currentJob.getTile() == tile) {
                        Navigator.lodThreads[i].interrupt();
                    }
                }
            }
            try {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    LODJob job = queue.get(i);
                    if (job.getTile() == tile) queue.remove(i);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean containsJob(Tile tile) {
        boolean contains = false;
        synchronized (queue) {
            try {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    LODJob job = queue.get(i);
                    if (job.getTile() == tile) return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return contains;
    }

    public void removeActiveTiles(W3DS_Layer layer) {
        synchronized (activeTilesLock) {
            try {
                for (int i = activeTiles.size() - 1; i >= 0; i--) {
                    Tile tile = activeTiles.get(i);
                    if (tile.getLayer() == layer) activeTiles.remove(i);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeAllActiveTiles() {
        synchronized (activeTilesLock) {
            activeTiles.clear();
        }
    }

    public void removeJobs(W3DS_Layer layer) {
        synchronized (queue) {
            try {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    LODJob job = queue.get(i);
                    if (job.getTile().getLayer() == layer) queue.remove(i);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        removeActiveTiles(layer);
    }

    public static void removeAllJobs() {
        System.out.println("LODThread.removeAllJobs");
        synchronized (queue) {
            queue.clear();
        }
        removeAllActiveTiles();
    }

    public void run() {
        long checkCacheTime0 = 0, checkCacheTime1 = 0;
        long checkCacheTimeInterval = 200;
        while (!killed) {
            try {
                while (!killed) {
                    LODJob job = null;
                    try {
                        synchronized (queue) {
                            if (queue.size() > 0) {
                                LODJob j = queue.get(0);
                                if (j.getStatus() == LODJob.STATUS_WAITING || j.getStatus() == LODJob.STATUS_FINISHED_DOWNLOADING_DATA) {
                                    job = j;
                                    try {
                                        queue.remove(0);
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (busyListeners != null) {
                                    int numBusyListeners = busyListeners.size();
                                    for (int i = 0; i < numBusyListeners; i++) {
                                        busyListeners.get(i).setBusy(true);
                                    }
                                }
                            } else {
                                int numBusyListeners = busyListeners.size();
                                for (int i = 0; i < numBusyListeners; i++) {
                                    busyListeners.get(i).setBusy(false);
                                }
                            }
                        }
                        currentJob = job;
                        if (job != null) {
                            if (job.getMode() == LODJob.MODE_DECREASE) {
                                decreaseLOD(job.getTile());
                            } else if (job.getMode() == LODJob.MODE_INCREASE) {
                                increaseLOD(job.getTile());
                            }
                            job.getTile().currentJobStatus = Tile.JOB_STATUS_NONE;
                            currentJob = null;
                            yield();
                        }
                        checkCacheTime1 = System.currentTimeMillis();
                        if ((checkCacheTime1 - checkCacheTime0) > checkCacheTimeInterval) {
                            if (Navigator.web3DServices != null) {
                                Web3DService.checkCache();
                            } else {
                                System.out.println("ERROR in LODThread.run: Navigator.web3DService == null");
                            }
                            checkCacheTime0 = System.currentTimeMillis();
                        }
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        System.out.println("run INTERRUPTED job:" + job + " mode:" + job.getMode() + " tile:" + job.getTile() + " childrenStatus:" + job.getTile().childrenStatus + " numChildren:" + job.getTile().numChildren());
                        if (job != null) {
                            Tile t = job.getTile();
                            int numChildren = t.numChildren();
                            if (t.childrenStatus == Tile.CHILDREN_STATUS_SCENE_BRANCHGROUP && numChildren == 0) {
                                System.out.println("run detected invalid Tile after interrupt: " + t + " CHILDREN_STATUS_SCENE_BRANCHGROUP && numChildren == 0");
                                t.clear();
                            }
                            if (t.childrenStatus == Tile.CHILDREN_STATUS_CHILDREN_BRANCHGROUP) {
                                if (numChildren != 1) {
                                    System.out.println("run detected invalid Tile after interrupt: " + t + " CHILDREN_STATUS_CHILDREN_BRANCHGROUP && numChildren == " + numChildren);
                                    t.clear();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                System.out.println("LODThread java.lang.ArrayIndexOutOfBoundsException");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("LODThread " + this.hashCode() + " exiting");
    }

    void decreaseLOD(Tile tile) throws InterruptedException {
        synchronized (tile) {
            try {
                if (tile.childrenStatus == Tile.CHILDREN_STATUS_SCENE_BRANCHGROUP) {
                    return;
                }
                if (!tile.isLive()) {
                    System.out.println("LODThread.decreaseLOD  tile not live");
                    return;
                }
                SceneBranchGroup sceneBranchGroup = null;
                if (tile.getCRSSize() <= tile.getLayer().getTileMaxSize()) {
                    if (tile.getLayer().isSplitTilesOnHighestLOD()) {
                        sceneBranchGroup = tile.getLayer().getWeb3DService().loadData(tile);
                    }
                    boolean doIt = true;
                    if (sceneBranchGroup.getParent() != null) {
                        System.out.println("LODThread.decreaseLOD setting sceneBranchGroup with parent");
                        if (sceneBranchGroup.getParent() == tile) {
                            System.out.println("LODThread.decreaseLOD sceneBranchGroup.getParent() == tile");
                            doIt = false;
                        }
                    }
                    if (doIt) {
                        tile.setSceneBranchGroup(sceneBranchGroup);
                    }
                } else {
                    tile.clear();
                }
                if (tile.childrenTiles != null) {
                    Tile[] ct = tile.childrenTiles;
                    tile.childrenTiles = null;
                    synchronized (activeTilesLock) {
                        for (int i = 0; i < ct.length; i++) {
                            LODThread.activeTiles.remove(ct[i]);
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("LODThread.decreaseLOD interrupted");
            }
        }
    }

    void increaseLOD(Tile tile) throws InterruptedException {
        synchronized (tile) {
            if (tile.getChildrenStatus() == Tile.CHILDREN_STATUS_CHILDREN_BRANCHGROUP) {
                if (false) {
                    int numChildren = tile.numChildren();
                    if (numChildren != 1) {
                        System.out.println("LODThread.increaseLOD    invalid numChildren = " + numChildren);
                    } else {
                        BranchGroup bg = (BranchGroup) tile.getChild(0);
                        if (!bg.getName().equals("childrenBranchGroup")) {
                            System.out.println("LODThread.increaseLOD    invalid bg name, expected childrenBranchGroup: " + bg.getName());
                        } else {
                            return;
                        }
                    }
                }
                return;
            }
            if (!tile.isLive()) {
                System.out.println("LODThread.increaseLOD  tile not live");
                return;
            }
            DownLoadDataThread[] downLoadDataThreads = null;
            try {
                double size = tile.getCRSSize();
                Point3d CRSCenter = tile.getCRSCenter();
                W3DS_Layer layer = tile.getLayer();
                FeatureTypeProperties featureTypeProperties = layer.getFeatureTypeProperties();
                ChildrenBranchGroup childrenBranchGroup = null;
                double childrenSize = size / 2.0;
                Tile[] childrenTiles = null;
                if (layer.isSplitTilesOnHighestLOD() == false && childrenSize <= layer.getTileMinSize()) {
                    if (tile.getChildrenStatus() == Tile.CHILDREN_STATUS_SCENE_BRANCHGROUP) {
                        System.out.println("LODThread.increaseLOD status == Tile.STATUS_HAS_SCENE_BRANCHGROUP");
                    } else {
                        SceneBranchGroup sceneBranchGroup = layer.getWeb3DService().loadData(tile);
                        tile.setSceneBranchGroup(sceneBranchGroup);
                    }
                    return;
                }
                boolean doSetChildrenBranchGroup = true;
                int childrenW3DSLevel = tile.getW3DS_TileLevel() + 1;
                childrenTiles = new Tile[4];
                childrenTiles[0] = new Tile(new Point3d(CRSCenter.x - childrenSize / 2.0, CRSCenter.y - childrenSize / 2.0, 0.0), childrenSize, layer);
                childrenTiles[0].setW3DS_TileLevel(childrenW3DSLevel);
                childrenTiles[0].setW3DS_TileCol(2 * tile.getW3DS_TileX());
                childrenTiles[0].setW3DS_TileRow(2 * tile.getW3DS_TileY());
                childrenTiles[1] = new Tile(new Point3d(CRSCenter.x - childrenSize / 2.0, CRSCenter.y + childrenSize / 2.0, 0.0), childrenSize, layer);
                childrenTiles[1].setW3DS_TileLevel(childrenW3DSLevel);
                childrenTiles[1].setW3DS_TileCol(2 * tile.getW3DS_TileX());
                childrenTiles[1].setW3DS_TileRow(2 * tile.getW3DS_TileY() + 1);
                childrenTiles[2] = new Tile(new Point3d(CRSCenter.x + childrenSize / 2.0, CRSCenter.y - childrenSize / 2.0, 0.0), childrenSize, layer);
                childrenTiles[2].setW3DS_TileLevel(childrenW3DSLevel);
                childrenTiles[2].setW3DS_TileCol(2 * tile.getW3DS_TileX() + 1);
                childrenTiles[2].setW3DS_TileRow(2 * tile.getW3DS_TileY());
                childrenTiles[3] = new Tile(new Point3d(CRSCenter.x + childrenSize / 2.0, CRSCenter.y + childrenSize / 2.0, 0.0), childrenSize, layer);
                childrenTiles[3].setW3DS_TileLevel(childrenW3DSLevel);
                childrenTiles[3].setW3DS_TileCol(2 * tile.getW3DS_TileX() + 1);
                childrenTiles[3].setW3DS_TileRow(2 * tile.getW3DS_TileY() + 1);
                if (childrenSize <= layer.getTileMaxSize()) {
                    boolean[] display = new boolean[4];
                    for (int i = 0; i < 4; i++) {
                        display[i] = true;
                    }
                    downLoadDataThreads = new DownLoadDataThread[4];
                    boolean[] load = new boolean[4];
                    for (int i = 0; i < 4; i++) {
                        if (display[i]) {
                            try {
                                load[i] = true;
                                downLoadDataThreads[i] = new DownLoadDataThread(childrenTiles[i]);
                                downLoadDataThreads[i].setName("DownLoadDataThread " + i);
                                downLoadDataThreads[i].start();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    for (int i = 0; i < 4; i++) {
                        if (downLoadDataThreads[i] != null) {
                            downLoadDataThreads[i].join();
                        }
                    }
                    int numChildren = 0;
                    for (int i = 0; i < 4; i++) {
                        if (display[i]) {
                            SceneBranchGroup data = downLoadDataThreads[i].getData();
                            if (data != null) {
                                if (data.getParent() != null) {
                                    System.out.println("caller1: data with parent");
                                }
                                childrenTiles[i].setSceneBranchGroup(data);
                                numChildren += data.getNumChildren();
                            } else System.out.println("data == null");
                        }
                    }
                    boolean isSurfaceGeometryType = false;
                    if (featureTypeProperties != null) {
                        isSurfaceGeometryType = (featureTypeProperties.getGeometryType() == FeatureTypeProperties.GEOMETRYTYPE_SURFACE);
                    }
                    if (numChildren == 0 && isSurfaceGeometryType) {
                        doSetChildrenBranchGroup = false;
                    }
                }
                if (doSetChildrenBranchGroup) {
                    childrenBranchGroup = new ChildrenBranchGroup();
                    childrenBranchGroup.setName("childrenBranchGroup");
                    childrenBranchGroup.setCapability(BranchGroup.ALLOW_DETACH);
                    childrenBranchGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
                    childrenBranchGroup.setBoundsAutoCompute(true);
                    for (int i = 0; i < childrenTiles.length; i++) {
                        childrenBranchGroup.addChild(childrenTiles[i]);
                    }
                    tile.childrenTiles = childrenTiles;
                    tile.setChildrenBranchGroup(childrenBranchGroup);
                    synchronized (activeTilesLock) {
                        for (int i = 0; i < 4; i++) {
                            LODThread.activeTiles.add(childrenTiles[i]);
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("LODThread.increaseLOD interrupted");
                e.printStackTrace();
                if (downLoadDataThreads != null) {
                    for (DownLoadDataThread downLoadDataThread : downLoadDataThreads) {
                        downLoadDataThread.interrupt();
                    }
                }
            }
        }
    }

    void finishIncreaseLOD(Tile tile, ChildrenBranchGroup childrenBranchGroup) {
        tile.setChildrenBranchGroup(childrenBranchGroup);
    }

    void finishDecreaseLOD(Tile tile, SceneBranchGroup sceneBranchGroup) {
        tile.setSceneBranchGroup(sceneBranchGroup);
    }

    public static void addBusyListener(BusyListener busyListener) {
        if (busyListeners == null) {
            busyListeners = new Vector<BusyListener>();
        }
        if (!busyListeners.contains(busyListener)) {
            busyListeners.add(busyListener);
        }
    }

    public static boolean removeBusyListener(BusyListener busyListener) {
        if (busyListeners != null) {
            return busyListeners.remove(busyListener);
        } else {
            return false;
        }
    }

    public void setKilled(boolean killed) {
        this.killed = killed;
    }

    private class DownLoadDataThread extends Thread {

        private Tile tile;

        private SceneBranchGroup data;

        public DownLoadDataThread(Tile tile) {
            this.tile = tile;
        }

        public void run() {
            try {
                data = tile.getLayer().getWeb3DService().loadData(tile);
                if (data.getParent() != null) {
                    System.out.println("DownLoadDataThread: data with parent");
                }
            } catch (InterruptedException e) {
                System.out.println("DownLoadDataThread.tun interrupted");
            } catch (Exception e) {
                e.printStackTrace();
            } catch (java.lang.OutOfMemoryError er) {
                er.printStackTrace();
                Web3DService.sceneBranchGroupManager.clearAllBranchGroups();
                Object[] objects = { Navigator.i18n.getString("OUT_OF_MEMORY") };
                JOptionPane.showMessageDialog(null, objects, Navigator.i18n.getString("WARNING"), JOptionPane.WARNING_MESSAGE);
                try {
                    Thread.currentThread().sleep(10000);
                } catch (InterruptedException e) {
                }
            }
        }

        public SceneBranchGroup getData() {
            return data;
        }
    }

    private class CheckActiveTilesThread extends Thread {

        private boolean catt_killed = false;

        private Vector3d currentJ3DPosition = new Vector3d();

        public CheckActiveTilesThread() {
        }

        public void setKilled(boolean killed) {
            this.catt_killed = killed;
        }

        public void run() {
            System.out.println("checkActiveTilesThread run");
            while (!catt_killed) {
                try {
                    while (!catt_killed) {
                        int numIncrease = 0;
                        int numDecrease = 0;
                        if (Navigator.viewer != null && Navigator.viewer.navigation != null) {
                            Navigator.currentJ3DTransform.get(currentJ3DPosition);
                            Vector3d currentJ3DViewingDirection = Navigator.viewer.navigation.getJ3DViewingDirection();
                            List activeTilesCopy = null;
                            synchronized (activeTilesLock) {
                                activeTilesCopy = new LinkedList(activeTiles);
                            }
                            Iterator<Tile> it = activeTilesCopy.iterator();
                            while (it.hasNext()) {
                                Tile activeTile = it.next();
                                if (activeTile != null) {
                                    Point3d center = activeTile.getJava3DCenter();
                                    if (center != null) {
                                        boolean withinRange = activeTile.withinRange(currentJ3DPosition, currentJ3DViewingDirection);
                                        if (withinRange) {
                                            if (activeTile.rangeStatus == Tile.RANGE_STATUS_INSIDE) {
                                            } else {
                                                activeTile.rangeStatus = Tile.RANGE_STATUS_INSIDE;
                                                activeTile.increaseLOD();
                                                numIncrease++;
                                            }
                                        } else {
                                            if (activeTile.rangeStatus == Tile.RANGE_STATUS_OUTSIDE) {
                                            } else {
                                                activeTile.rangeStatus = Tile.RANGE_STATUS_OUTSIDE;
                                                activeTile.decreaseLOD();
                                                numDecrease++;
                                            }
                                        }
                                    } else System.out.println("LODThread.run center = null");
                                }
                            }
                        }
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("CheckActiveTilesThread " + this.hashCode() + " exiting");
        }
    }

    private class DownLoadDataThread2 extends Thread {

        private boolean ddt2_killed = false;

        public DownLoadDataThread2() {
        }

        public void setKilled(boolean killed) {
            this.ddt2_killed = killed;
        }

        public void run() {
            while (!killed) {
                try {
                    while (!ddt2_killed) {
                        int i = 0;
                        while (!ddt2_killed) {
                            LODJob job = null;
                            int numJobs = 0;
                            synchronized (queue) {
                                numJobs = queue.size();
                                if (i < numJobs) {
                                    job = queue.get(i);
                                }
                            }
                            i++;
                            if (i > numJobs) i = 0;
                            if (job != null) {
                                int status = job.getStatus();
                                if (status != LODJob.STATUS_DOWNLOADING_DATA && status != LODJob.STATUS_FINISHED_DOWNLOADING_DATA) {
                                    Tile tile = job.getTile();
                                    if (tile.getReader() == null) {
                                        W3DS_Layer layer = tile.getLayer();
                                        if (job.getMode() == LODJob.MODE_DECREASE) {
                                            if (tile.getCRSSize() <= tile.getLayer().getTileMaxSize()) {
                                                job.setStatus(LODJob.STATUS_DOWNLOADING_DATA);
                                                tile.downloadData();
                                                job.setStatus(LODJob.STATUS_FINISHED_DOWNLOADING_DATA);
                                            }
                                        } else if (job.getMode() == LODJob.MODE_INCREASE) {
                                            double size = tile.getCRSSize();
                                            double childrenSize = size / 2.0;
                                            if (layer.isSplitTilesOnHighestLOD() == false && childrenSize <= layer.getTileMinSize()) {
                                                if (tile.getChildrenStatus() == Tile.CHILDREN_STATUS_SCENE_BRANCHGROUP) {
                                                } else {
                                                    job.setStatus(LODJob.STATUS_DOWNLOADING_DATA);
                                                    tile.downloadData();
                                                    job.setStatus(LODJob.STATUS_FINISHED_DOWNLOADING_DATA);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Thread.sleep(5);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("DownLoadDataThread2 " + this.hashCode() + " exiting");
        }

        private void prepareReader(LODJob job, Tile tile) {
        }
    }

    public static void clearStaticFields() {
        System.out.println("LODThread.clearStaticFields");
        activeTiles = new LinkedList<Tile>();
        busyListeners = null;
        checkActiveTilesThread = null;
        checkActiveTilesThreadLock = new Object();
        checkActiveTilesThreadStarted = false;
        downloadThreads = new DownLoadDataThread2[numParallelDownloadThreads];
        downloadThreadsinitialized = false;
        numParallelDownloadThreads = 4;
        queue = new Vector<LODJob>();
    }

    public static void stopThreads() {
        try {
            if (checkActiveTilesThread != null) {
                checkActiveTilesThread.setKilled(true);
                checkActiveTilesThread.join();
            }
            if (downloadThreads != null) {
                for (int i = 0; i < downloadThreads.length; i++) {
                    if (downloadThreads[i] != null) {
                        downloadThreads[i].setKilled(true);
                        downloadThreads[i].join();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
