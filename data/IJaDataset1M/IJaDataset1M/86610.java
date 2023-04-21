package ants.p2p.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.beans.*;
import java.io.*;
import ants.p2p.filesharing.*;
import ants.p2p.utils.indexer.*;
import ants.p2p.query.*;
import org.apache.log4j.*;
import javax.swing.border.*;

public class DownloadAntPanel extends JPanel implements PropertyChangeListener {

    public static String pic = "backgrounds/bg2.jpg";

    static Logger _logger = Logger.getLogger(DownloadAntPanel.class.getName());

    public static int maxConcurrentDownloads = 0;

    Image buffer;

    JScrollPane jScrollPane1 = new JScrollPane();

    JScrollPane jScrollPane2 = new JScrollPane();

    JLabel jLabel1 = new JLabel();

    JLabel jLabel2 = new JLabel();

    DefaultMutableTreeNode downloadRoot = new DefaultMutableTreeNode("Downloads");

    DefaultMutableTreeNode interruptedRoot = new DefaultMutableTreeNode("Interrupted");

    DefaultTreeModel downloadTree = new DefaultTreeModel(null);

    DefaultTreeModel interruptedTree = new DefaultTreeModel(null);

    JTree jTree1 = new JTree(downloadTree);

    JTree jTree2 = new JTree(interruptedTree);

    GuiAnt container;

    Border border1;

    FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER);

    DownloadAntPanel instance = null;

    public DownloadAntPanel(GuiAnt container) {
        instance = this;
        this.container = container;
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            MediaTracker mt = new MediaTracker(this);
            buffer = tk.getImage(this.getClass().getClassLoader().getResource(pic));
            mt.waitForAll();
        } catch (Exception e) {
        }
        try {
            jbInit();
        } catch (Exception ex) {
            _logger.error("", ex);
        }
    }

    void jbInit() throws Exception {
        border1 = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        downloadTree.setRoot(downloadRoot);
        interruptedTree.setRoot(interruptedRoot);
        this.setLayout(flowLayout1);
        jLabel1.setPreferredSize(new Dimension(400, 15));
        jLabel1.setText(ji.JI.i("Downloading..."));
        jLabel2.setAlignmentX((float) 0.0);
        jLabel2.setAlignmentY((float) 0.5);
        jLabel2.setPreferredSize(new Dimension(400, 15));
        jLabel2.setText(ji.JI.i("Interrupted..."));
        jTree1.addMouseListener(new DownloadAntPanel_jTree1_mouseAdapter(this));
        jTree2.addMouseListener(new DownloadAntPanel_jTree2_mouseAdapter(this));
        jTree1.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTree1.setToolTipText(ji.JI.i("Active file transfers, right click for options"));
        jTree1.setRootVisible(false);
        jTree1.setCellRenderer(new DownloadCellRenderer(this));
        jScrollPane2.setPreferredSize(new Dimension(400, 200));
        this.setBorder(border1);
        this.addComponentListener(new DownloadAntPanel_this_componentAdapter(this));
        jScrollPane1.setPreferredSize(new Dimension(400, 200));
        jTree2.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTree2.setToolTipText(ji.JI.i("Files wating for an active route to a source or interrupted by the " + "user, right click for options"));
        jTree2.setRootVisible(false);
        jTree2.setCellRenderer(new DownloadCellRenderer(this));
        jScrollPane1.getViewport().add(jTree1, null);
        jScrollPane2.getViewport().add(jTree2, null);
        this.add(jLabel1, null);
        this.add(jScrollPane1, null);
        this.add(jLabel2, null);
        this.add(jScrollPane2, null);
        this.loadInterrupted();
        this.container.cap.warriorAnt.propertyChangeSupport.addPropertyChangeListener(this);
    }

    public void loadInterrupted() {
        WarriorAnt.checkChunksPath();
        this.interruptAllDownloads();
        this.interruptedRoot.removeAllChildren();
        BackgroundEngine.getInstance().resetPartialFiles();
        File currentDirectory = new File(WarriorAnt.chunksHome + WarriorAnt.chunksPath);
        if (currentDirectory.exists() && currentDirectory.isDirectory()) {
            File[] interrupted = currentDirectory.listFiles();
            for (int x = 0; x < interrupted.length; x++) {
                if (interrupted[x].getName().lastIndexOf(".mul") == interrupted[x].getName().length() - ".mul".length()) {
                    InterruptedDownload interruptedDownload = null;
                    try {
                        interruptedDownload = new InterruptedDownload(interrupted[x]);
                    } catch (Exception e) {
                        _logger.error("Load interrupted error", e);
                    }
                    if (interruptedDownload != null) {
                        if (this.container.sap.autoResumeOnRun()) {
                            AutoresumeEngine ae = new AutoresumeEngine(interruptedDownload, this.container.cap.warriorAnt, this);
                        }
                        this.interruptedRoot.add(new DefaultMutableTreeNode(interruptedDownload));
                    }
                }
            }
            this.jTree2.setModel(this.interruptedTree);
            this.interruptedTree.reload();
            this.repaint();
        }
    }

    public void interruptAllDownloads() {
        DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
        for (int x = 0; x < msdmInDownload.length; x++) {
            msdmInDownload[x] = (DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x);
        }
        for (int x = 0; x < this.interruptedRoot.getChildCount(); x++) {
            DefaultMutableTreeNode msdmInterrupted = (DefaultMutableTreeNode) this.interruptedRoot.getChildAt(x);
            ((InterruptedDownload) msdmInterrupted.getUserObject()).deactivateAutoresumeEngine();
        }
        for (int x = 0; x < msdmInDownload.length; x++) {
            final MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
            currentNode.removeAllPeers(true);
            BackgroundEngine.getInstance().removePartialFile(currentNode.getFileHash());
        }
    }

    boolean peerModificationInProgress = false;

    public void propertyChange(final PropertyChangeEvent e) {
        handleEvent(e);
    }

    public String getFileStatus(String hashKey, boolean bGetFileNameIfDownloaded) {
        try {
            int ICHILDREN_NUM = this.container.dap.downloadRoot.getChildCount();
            for (int x = 0; x < ICHILDREN_NUM; x++) {
                final MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x)).getUserObject();
                if (currentNode.getFileHash().equals(hashKey)) {
                    if (currentNode.isDownloaded()) return bGetFileNameIfDownloaded ? WarriorAnt.downloadPath + (new File(currentNode.getFileName())).getName() : "ok";
                    return "down";
                }
            }
            if (bGetFileNameIfDownloaded) return null;
            ICHILDREN_NUM = this.container.dap.interruptedRoot.getChildCount();
            for (int x = 0; x < ICHILDREN_NUM; x++) {
                MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) this.interruptedRoot.getChildAt(x)).getUserObject();
                if (currentNode.getFileHash().equals(hashKey)) return "int";
            }
        } catch (Exception e) {
        }
        return "";
    }

    public String getJavaScriptString() {
        String sJavaScript = "";
        try {
            int ICHILDREN_NUM = this.container.dap.downloadRoot.getChildCount();
            for (int x = 0; x < ICHILDREN_NUM; x++) {
                final MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x)).getUserObject();
                String sNodeScript = "[\"" + currentNode.getFileName() + "\"," + (currentNode.isDownloaded() ? '1' : '0') + ',' + '"' + currentNode.getFileHash() + '"' + ',' + currentNode.getIntPercentage() + ",\"" + currentNode.getPeerId() + "\"," + Long.toString(currentNode.getFileSize()) + "]";
                if (sJavaScript.length() > 0) sJavaScript += ',';
                sJavaScript += sNodeScript;
            }
            ICHILDREN_NUM = this.container.dap.interruptedRoot.getChildCount();
            for (int x = 0; x < ICHILDREN_NUM; x++) {
                final InterruptedDownload currentNode = (InterruptedDownload) ((DefaultMutableTreeNode) this.container.dap.interruptedRoot.getChildAt(x)).getUserObject();
                if (sJavaScript.length() > 0) sJavaScript += ',';
                sJavaScript += "[\"" + currentNode.getFileName() + "\",2,\"\"," + currentNode.getIntPercentage() + ",\"\"," + Long.toString(currentNode.getFileSize()) + "]";
            }
        } catch (Exception e) {
        }
        return sJavaScript;
    }

    public void handleEvent(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("filePartDownloadCompleted") || e.getPropertyName().equals("filePartUpdate") || e.getPropertyName().equals("refreshDownloadGraphic")) {
            this.repaint();
        }
        if (e.getPropertyName().equals("multipleDownloadTimedOut")) {
            try {
                synchronized (this) {
                    while (this.peerModificationInProgress == true) {
                        try {
                            _logger.info(Thread.currentThread() + " waiting for lock! 1");
                            this.wait();
                        } catch (InterruptedException ex) {
                            _logger.error("", ex);
                        }
                    }
                    this.peerModificationInProgress = true;
                    _logger.info(Thread.currentThread() + " lock aquired! 1");
                }
                DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
                for (int x = 0; x < msdmInDownload.length; x++) {
                    msdmInDownload[x] = (DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x);
                }
                for (int x = 0; x < msdmInDownload.length; x++) {
                    final MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
                    if (currentNode.equals(e.getNewValue())) {
                        synchronized (this) {
                            this.peerModificationInProgress = false;
                            this.notifyAll();
                            _logger.info(Thread.currentThread() + " lock released! 1");
                        }
                        Thread interrupter = new Thread() {

                            public void run() {
                                currentNode.removeAllPeers(false);
                            }
                        };
                        interrupter.start();
                        break;
                    }
                }
            } catch (Throwable t) {
            }
            synchronized (this) {
                this.peerModificationInProgress = false;
                this.notifyAll();
                _logger.info(Thread.currentThread() + " lock released! 1");
            }
        }
        if (e.getPropertyName().equals("removedSourcePeer")) {
            try {
                synchronized (this) {
                    while (this.peerModificationInProgress == true) {
                        try {
                            _logger.info(Thread.currentThread() + " waiting for lock! 2");
                            this.wait();
                        } catch (InterruptedException ex) {
                            _logger.error("", ex);
                        }
                    }
                    this.peerModificationInProgress = true;
                    _logger.info(Thread.currentThread() + " lock aquired! 2 " + e.getNewValue() + "\n" + e.getOldValue());
                }
                DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
                for (int x = 0; x < msdmInDownload.length; x++) {
                    msdmInDownload[x] = (DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x);
                }
                ((MultipleSourcesDownloadManager) e.getOldValue()).removePeer((String) e.getNewValue());
                for (int x = 0; x < msdmInDownload.length; x++) {
                    final DefaultMutableTreeNode currentNodeX = msdmInDownload[x];
                    final MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
                    if (currentNode.equals(e.getOldValue())) {
                        if (currentNode.getPeersNumber() > 0) {
                            for (int y = 0; y < msdmInDownload[x].getChildCount(); y++) {
                                DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) msdmInDownload[x].getChildAt(y);
                                if (((QueryFileTuple) currentChild.getUserObject()).getOwnerID().equals(e.getNewValue())) {
                                    this.downloadTree.removeNodeFromParent(currentChild);
                                }
                            }
                            jTree1.setModel(downloadTree);
                            downloadTree.reload();
                            this.repaint();
                            synchronized (this) {
                                this.peerModificationInProgress = false;
                                this.notifyAll();
                                _logger.info(Thread.currentThread() + " lock released! 2");
                            }
                            return;
                        } else {
                            _logger.info("Setting interrupt: " + currentNode);
                            currentNode.setInterrupt(false);
                            while (currentNode.isAlive()) {
                                currentNode.resume();
                                _logger.info("Waiting for graceful shutdown: " + currentNode);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException ex) {
                                }
                            }
                            _logger.info("Update graphic: " + currentNode);
                            try {
                                downloadTree.removeNodeFromParent(currentNodeX);
                                jTree1.setModel(downloadTree);
                                downloadTree.reload();
                                for (int k = 0; k < interruptedRoot.getChildCount(); k++) {
                                    DefaultMutableTreeNode msdmInterrupted = (DefaultMutableTreeNode) interruptedRoot.getChildAt(k);
                                    InterruptedDownload curId = (InterruptedDownload) msdmInterrupted.getUserObject();
                                    if (curId.getFileHash().equals(currentNode.getFileHash())) {
                                        currentNode.deactivateAutoresumeEngine();
                                        synchronized (instance) {
                                            peerModificationInProgress = false;
                                            instance.notifyAll();
                                            _logger.info(Thread.currentThread() + " lock released! interrupter");
                                        }
                                        return;
                                    }
                                }
                                InterruptedDownload interruptedDownload = new InterruptedDownload(currentNode);
                                if (currentNode.isAutoresumeActive()) {
                                    currentNode.getAutoresumeEngine().setInterruptedDownload(interruptedDownload);
                                    _logger.info(currentNode.getAutoresumeEngine() + " AutoresumeEngine switched to ID: " + currentNode.getFileHash());
                                }
                                interruptedRoot.add(new DefaultMutableTreeNode(interruptedDownload));
                                interruptedTree.reload();
                                jTree2.setModel(interruptedTree);
                            } catch (Throwable t) {
                                _logger.error("graphic not updated!!!!", t);
                            }
                            synchronized (instance) {
                                peerModificationInProgress = false;
                                instance.notifyAll();
                                _logger.info(Thread.currentThread() + " lock released! interrupter");
                            }
                            jTree2.revalidate();
                            repaint();
                            return;
                        }
                    }
                }
                synchronized (this) {
                    peerModificationInProgress = false;
                    this.notifyAll();
                    _logger.info(Thread.currentThread() + " lock released! EOC");
                }
            } catch (Throwable t) {
                synchronized (this) {
                    this.peerModificationInProgress = false;
                    this.notifyAll();
                    _logger.info(Thread.currentThread() + " lock released! 2");
                }
            }
        }
        if (e.getPropertyName().equals("securityConnectionError")) {
            DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
            for (int x = 0; x < msdmInDownload.length; x++) {
                msdmInDownload[x] = (DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x);
            }
            for (int x = 0; x < msdmInDownload.length; x++) {
                MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
                currentNode.propertyChangeSupport.firePropertyChange("removedSourcePeer", currentNode, (String) e.getOldValue());
            }
        }
        if (e.getPropertyName().equals("addSourcePeer")) {
            try {
                synchronized (this) {
                    while (instance.peerModificationInProgress == true) {
                        try {
                            _logger.info(Thread.currentThread() + " waiting for lock! 4");
                            this.wait();
                        } catch (InterruptedException ex) {
                            _logger.error("", ex);
                        }
                    }
                    instance.peerModificationInProgress = true;
                    _logger.info(Thread.currentThread() + " lock aquired! 4");
                }
                DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
                QueryFileTuple fileToDownload = (QueryFileTuple) e.getNewValue();
                for (int x = 0; x < msdmInDownload.length; x++) {
                    msdmInDownload[x] = (DefaultMutableTreeNode) this.downloadRoot.getChildAt(x);
                }
                DefaultMutableTreeNode[] msdmInterrupted = new DefaultMutableTreeNode[this.interruptedRoot.getChildCount()];
                for (int x = 0; x < msdmInterrupted.length; x++) {
                    msdmInterrupted[x] = (DefaultMutableTreeNode) this.interruptedRoot.getChildAt(x);
                }
                for (int x = 0; x < msdmInDownload.length; x++) {
                    MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
                    if (currentNode.getFileHash().equals(fileToDownload.getFileHash()) && currentNode.isAlive()) {
                        if (currentNode.addPeer(fileToDownload)) {
                            for (int y = 0; y < msdmInDownload[x].getChildCount(); y++) {
                                DefaultMutableTreeNode currentChild = (DefaultMutableTreeNode) msdmInDownload[x].getChildAt(y);
                                if (((QueryFileTuple) currentChild.getUserObject()).getOwnerID().equals(fileToDownload.getOwnerID())) {
                                    this.downloadTree.removeNodeFromParent(currentChild);
                                }
                            }
                            msdmInDownload[x].add(new DefaultMutableTreeNode(fileToDownload));
                            this.jTree1.setModel(this.downloadTree);
                            this.downloadTree.reload();
                            this.repaint();
                            _logger.info("Peer added to file: " + currentNode.getFileHash());
                        }
                        synchronized (this) {
                            this.peerModificationInProgress = false;
                            this.notifyAll();
                            _logger.info(Thread.currentThread() + " lock released! 4");
                        }
                        return;
                    } else if (currentNode.getFileHash().equals(fileToDownload.getFileHash()) && !currentNode.isAlive()) {
                        currentNode.deactivateAutoresumeEngine();
                        _logger.info("AutoresumeEngine Deactivated for MSDM: " + currentNode.getFileHash());
                        downloadTree.removeNodeFromParent(msdmInDownload[x]);
                        jTree1.setModel(downloadTree);
                        downloadTree.reload();
                    }
                }
                if (maxConcurrentDownloads == 0 || this.getActiveDownloadsNumber() < maxConcurrentDownloads) {
                    for (int x = 0; x < msdmInterrupted.length; x++) {
                        InterruptedDownload currentNode = (InterruptedDownload) msdmInterrupted[x].getUserObject();
                        if (currentNode.getFileHash().equals(fileToDownload.getFileHash())) {
                            MultipleSourcesDownloadManager msdm = new MultipleSourcesDownloadManager(this.container.cap.warriorAnt, currentNode.getFileName(), currentNode.getFileHash(), currentNode.getED2KFileHash(), currentNode.getByteDownloaded(), currentNode.getBlockSize(), currentNode.getFileSize(), fileToDownload.getExtendedInfos());
                            if (msdm.addPeer(fileToDownload)) {
                                DefaultMutableTreeNode fileGroup = new DefaultMutableTreeNode(msdm);
                                fileGroup.add(new DefaultMutableTreeNode(fileToDownload));
                                this.downloadRoot.add(fileGroup);
                                this.jTree1.setModel(this.downloadTree);
                                this.repaint();
                                this.interruptedTree.removeNodeFromParent(msdmInterrupted[x]);
                                this.jTree2.setModel(this.interruptedTree);
                                this.downloadTree.reload();
                                this.repaint();
                                msdm.propertyChangeSupport.addPropertyChangeListener(this);
                                msdm.start();
                                if (currentNode.isAutoresumeActive()) {
                                    currentNode.getAutoresumeEngine().setMultipleSourcesDownloadManager(msdm);
                                    _logger.info(currentNode.getAutoresumeEngine() + " AutoresumeEngine switched to MSDM: " + currentNode.getFileHash());
                                } else {
                                    AutoresumeEngine ae = new AutoresumeEngine(msdm, this);
                                    _logger.info(ae + " 1 New AutoresumeEngine for MSDM: " + currentNode.getFileHash());
                                }
                            } else {
                                msdm.start();
                                msdm.setInterrupt(false);
                            }
                            synchronized (this) {
                                this.peerModificationInProgress = false;
                                this.notifyAll();
                                _logger.info(Thread.currentThread() + " lock released! 4");
                            }
                            return;
                        }
                    }
                    MultipleSourcesDownloadManager currentNode = new MultipleSourcesDownloadManager(this.container.cap.warriorAnt, fileToDownload.getFileName(), fileToDownload.getFileHash(), fileToDownload.getED2KFileHash(), 0L, WarriorAnt.blockSizeInDownload, fileToDownload.getSize().longValue(), fileToDownload.getExtendedInfos());
                    DefaultMutableTreeNode fileGroup = new DefaultMutableTreeNode(currentNode);
                    if (currentNode.addPeer(fileToDownload)) {
                        fileGroup.add(new DefaultMutableTreeNode(fileToDownload));
                        this.downloadRoot.add(fileGroup);
                        this.jTree1.setModel(this.downloadTree);
                        this.downloadTree.reload();
                        this.repaint();
                        currentNode.propertyChangeSupport.addPropertyChangeListener(this);
                        currentNode.start();
                        AutoresumeEngine ae = new AutoresumeEngine(currentNode, this);
                        _logger.info(ae + " 2 New AutoresumeEngine for MSDM: " + currentNode.getFileHash());
                    } else {
                        currentNode.start();
                        currentNode.setInterrupt(false);
                    }
                }
            } catch (Throwable t) {
                _logger.error("Error in DownloadAntPanel refresh after a peer adding", t);
            }
            synchronized (this) {
                this.peerModificationInProgress = false;
                this.notifyAll();
                _logger.info(Thread.currentThread() + " lock released! 4");
            }
        }
    }

    public void createInterruptedDownload(QueryFileTuple fileToDownload) {
        try {
            DefaultMutableTreeNode[] msdmInDownload = new DefaultMutableTreeNode[this.downloadRoot.getChildCount()];
            for (int x = 0; x < msdmInDownload.length; x++) {
                msdmInDownload[x] = (DefaultMutableTreeNode) this.downloadRoot.getChildAt(x);
                MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) msdmInDownload[x].getUserObject();
                if (currentNode.getFileHash().equals(fileToDownload.getFileHash())) return;
            }
            synchronized (this) {
                while (instance.peerModificationInProgress == true) {
                    try {
                        _logger.info(Thread.currentThread() + " waiting for lock! 6");
                        this.wait();
                    } catch (InterruptedException ex) {
                        _logger.error("", ex);
                    }
                }
                instance.peerModificationInProgress = true;
                _logger.info(Thread.currentThread() + " lock aquired! 6");
            }
            DefaultMutableTreeNode[] msdmInterrupted = new DefaultMutableTreeNode[this.interruptedRoot.getChildCount()];
            for (int x = 0; x < msdmInterrupted.length; x++) {
                msdmInterrupted[x] = (DefaultMutableTreeNode) this.interruptedRoot.getChildAt(x);
            }
            for (int x = 0; x < msdmInterrupted.length; x++) {
                InterruptedDownload currentNode = (InterruptedDownload) msdmInterrupted[x].getUserObject();
                if (currentNode.getFileHash().equals(fileToDownload.getFileHash())) {
                    synchronized (this) {
                        this.peerModificationInProgress = false;
                        this.notifyAll();
                        _logger.info(Thread.currentThread() + " lock released! 4");
                    }
                    return;
                }
            }
            MultipleSourcesDownloadManager currentNode = new MultipleSourcesDownloadManager(this.container.cap.warriorAnt, fileToDownload.getFileName(), fileToDownload.getFileHash(), fileToDownload.getED2KFileHash(), 0L, WarriorAnt.blockSizeInDownload, fileToDownload.getSize().longValue(), fileToDownload.getExtendedInfos());
            currentNode.start();
            currentNode.setInterrupt(false);
            InterruptedDownload interruptedDownload = new InterruptedDownload(currentNode);
            this.interruptedRoot.add(new DefaultMutableTreeNode(interruptedDownload));
            this.jTree2.setModel(this.interruptedTree);
            AutoresumeEngine ae = new AutoresumeEngine(interruptedDownload, this.container.cap.warriorAnt, this);
            _logger.info(ae + " 3 New AutoresumeEngine for ID: " + currentNode.getFileHash());
            this.interruptedTree.reload();
            this.repaint();
        } catch (Exception e) {
            _logger.error("", e);
        }
        synchronized (this) {
            this.peerModificationInProgress = false;
            this.notifyAll();
            _logger.info(Thread.currentThread() + " lock released! 4");
        }
    }

    public void repaint() {
        super.repaint();
        this.setDonwloadsOverallSpeed();
    }

    public int getActiveDownloadsNumber() {
        int inactiveDownloads = 0;
        for (int x = 0; x < this.downloadRoot.getChildCount(); x++) {
            MultipleSourcesDownloadManager cur = ((MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) this.downloadRoot.getChildAt(x)).getUserObject());
            if (cur.checkCompleted() || !cur.isAlive()) inactiveDownloads++;
        }
        return this.downloadRoot.getChildCount() - inactiveDownloads;
    }

    public String getDonwloadsOverallSpeed() {
        double downloadOverallSpeed = 0;
        String s_downloadOverallSpeed = "0KB/s";
        int totalDownloads = 0;
        int activeDownloads = 0;
        if (this.downloadRoot != null) {
            for (int x = 0; x < this.downloadRoot.getChildCount(); x++) {
                MultipleSourcesDownloadManager currentNode = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) this.container.dap.downloadRoot.getChildAt(x)).getUserObject();
                downloadOverallSpeed += currentNode.getSpeedValue();
            }
            s_downloadOverallSpeed = ("" + downloadOverallSpeed).substring(0, ("" + downloadOverallSpeed).indexOf(".") + 2) + "KiB/s ";
            totalDownloads = this.getActiveDownloadsNumber() + this.interruptedRoot.getChildCount();
            activeDownloads = this.getActiveDownloadsNumber();
        }
        return ji.JI.i("Downloads") + " [" + ji.JI.i("Active") + ": " + activeDownloads + "/" + totalDownloads + " " + ji.JI.i("Speed") + ": " + s_downloadOverallSpeed + "]";
    }

    public void setDonwloadsOverallSpeed() {
        if (this.container != null && this.container.jTabbedPane1 != null) {
            Component[] tabs = this.container.jTabbedPane1.getComponents();
            for (int x = 0; tabs != null && x < tabs.length; x++) {
                if (tabs[x] == this) {
                    this.container.jTabbedPane1.setIconAt(x, new LabelIcon(getDonwloadsOverallSpeed(), DoubleBuffer.getInstance().getButton(17), this));
                    return;
                }
            }
        }
    }

    void jTree1_mousePressed(MouseEvent e) {
        int selRow = jTree1.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = jTree1.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            if (e.getClickCount() == 1 && (e.getButton() == MouseEvent.BUTTON3 || (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()))) {
                if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject() instanceof MultipleSourcesDownloadManager) {
                    MultipleSourcesDownloadManager downloader = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    AntPopupMenu popup = new AntPopupMenu(this, downloader, selRow, selPath);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                } else if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject() instanceof QueryFileTuple) {
                    QueryFileTuple source = (QueryFileTuple) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    MultipleSourcesDownloadManager downloader = (MultipleSourcesDownloadManager) ((DefaultMutableTreeNode) selPath.getParentPath().getLastPathComponent()).getUserObject();
                    AntPopupMenu popup = new AntPopupMenu(this, source, downloader, selRow, selPath);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            } else if (e.getClickCount() == 2) {
            }
        }
    }

    void jTree2_mousePressed(MouseEvent e) {
        int selRow = jTree2.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = jTree2.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1) {
            if (e.getClickCount() == 1 && (e.getButton() == MouseEvent.BUTTON3 || (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()))) {
                if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject() instanceof InterruptedDownload) {
                    InterruptedDownload toBeProcessed = (InterruptedDownload) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                    AntPopupMenu popup = new AntPopupMenu(this, toBeProcessed, selRow, selPath);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            } else if (e.getClickCount() == 2) {
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buffer != null) {
            int posX = (this.getWidth() - 650) / 2;
            int posY = (this.getHeight() - 550) / 2;
            if (posX < 0) posX = 0;
            if (posY < 0) posY = 0;
            g.drawImage(buffer, posX, posY, this);
        }
    }

    void this_componentResized(ComponentEvent e) {
        this.jLabel1.setPreferredSize(new Dimension(this.getWidth() - 40, this.jLabel1.getHeight()));
        this.jLabel2.setPreferredSize(new Dimension(this.getWidth() - 40, this.jLabel2.getHeight()));
        this.jScrollPane1.setPreferredSize(new Dimension(this.getWidth() - 40, (this.getHeight() - 100) / 2));
        this.jScrollPane2.setPreferredSize(new Dimension(this.getWidth() - 40, (this.getHeight() - 100) / 2));
        this.jLabel1.setSize(new Dimension(this.getWidth() - 40, this.jLabel1.getHeight()));
        this.jLabel2.setSize(new Dimension(this.getWidth() - 40, this.jLabel2.getHeight()));
        this.jScrollPane1.setSize(new Dimension(this.getWidth() - 40, (this.getHeight() - 100) / 2));
        this.jScrollPane2.setSize(new Dimension(this.getWidth() - 40, (this.getHeight() - 100) / 2));
    }
}

class DownloadAntPanel_jTree1_mouseAdapter extends java.awt.event.MouseAdapter {

    DownloadAntPanel adaptee;

    DownloadAntPanel_jTree1_mouseAdapter(DownloadAntPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        adaptee.jTree1_mousePressed(e);
    }
}

class DownloadAntPanel_jTree2_mouseAdapter extends java.awt.event.MouseAdapter {

    DownloadAntPanel adaptee;

    DownloadAntPanel_jTree2_mouseAdapter(DownloadAntPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void mousePressed(MouseEvent e) {
        adaptee.jTree2_mousePressed(e);
    }
}

class DownloadAntPanel_this_componentAdapter extends java.awt.event.ComponentAdapter {

    DownloadAntPanel adaptee;

    DownloadAntPanel_this_componentAdapter(DownloadAntPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void componentResized(ComponentEvent e) {
        adaptee.this_componentResized(e);
    }
}

class DownloadCellRenderer extends DefaultTreeCellRenderer {

    DownloadAntPanel dap;

    public DownloadCellRenderer(DownloadAntPanel dap) {
        this.dap = dap;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode && !selected) {
            Object content = ((DefaultMutableTreeNode) value).getUserObject();
            if (content instanceof InterruptedDownload) {
                InterruptedDownload id = (InterruptedDownload) content;
                if (id.isAutoresumeActive()) c.setForeground(new Color(40, 100, 200)); else c.setForeground(new Color(100, 100, 100));
            } else if (content instanceof MultipleSourcesDownloadManager) {
                MultipleSourcesDownloadManager msdm = (MultipleSourcesDownloadManager) content;
                if (msdm.getChunkHashes() == null) c.setForeground(new Color(150, 150, 200)); else {
                    int sourcesPercent = (int) ((msdm.getPeersNumber() * 100.0) / MultipleSourcesDownloadManager.MaxSources);
                    c.setForeground(new Color(200 - (sourcesPercent > 100 ? 100 : sourcesPercent), 0, 100 + (sourcesPercent > 100 ? 100 : sourcesPercent)));
                }
                if (!msdm.isAlive() && !msdm.checkCompleted()) {
                    this.setVisible(false);
                }
            } else if (content instanceof QueryPartialFileTuple || content instanceof QueryCompletedFileTuple) {
                QueryFileTuple qft = ((QueryFileTuple) content);
                Object[] objPath = ((DefaultMutableTreeNode) value).getUserObjectPath();
                MultipleSourcesDownloadManager msdm = (MultipleSourcesDownloadManager) objPath[objPath.length - 2];
                if (msdm.findInServicePeer(qft.getOwnerID()) != null) c.setForeground(new Color(0, 0, 200)); else c.setForeground(new Color(200, 0, 0));
            }
        }
        return c;
    }
}
