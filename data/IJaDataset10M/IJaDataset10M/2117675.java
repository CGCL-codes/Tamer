package fr.lelouet.monitoring.snapshot.receivers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fr.lelouet.monitoring.snapshot.HVSnapshot;
import fr.lelouet.monitoring.snapshot.SharedMainConfig;
import fr.lelouet.monitoring.snapshot.SnapshotReceiver;
import fr.lelouet.monitoring.snapshot.VMSnapshot;

/**
 * keep a list of snapshots done . Implements the observer pattern and
 * auto-update with files in a given directory.<br />
 * This can either put files in a given directory, or create a directory named
 * using creation date in that directory, to put files in it.
 */
public class DirectoryWriter implements SnapshotReceiver {

    /** a snapshot file should end with that suffix */
    public static final String FILE_EXTENSION = ".hvs";

    /** a directory containing snapshots should start with that prefix */
    public static final String DIR_PREFIX = "snapshots_";

    private static final Logger logger = LoggerFactory.getLogger(DirectoryWriter.class);

    private static final long serialVersionUID = 1L;

    /** the key for the directory to write snapshots director in */
    public static final String OUTPUTDIR_KEY = "snapshotDirectoryWriter.dirOut";

    /** the key to specify the delay between writes */
    public static final String WRITESDELAY_KEY = "snapshotDirectoryWriter.writeDelay";

    /**
	 * the key to specify whether this should write snapshots on any
	 * modifications (true) or only on hypervisor modification (false)
	 */
    public static final String HANDLEVM_KEY = "snapshotDirectoryWriter.writeOnVMModification";

    protected final String outDirDefaultName = DIR_PREFIX + new SimpleDateFormat("yyyy-MM-dd, HH mm ss").format(new Date());

    /** the name of the directory to write snapshots into */
    protected String outDirName = SharedMainConfig.snapshotsDir;

    /** has the directory already been checked ? */
    boolean dirCreationTryed = false;

    /** the directory to put the snapshots into */
    protected File outDir = null;

    /**
	 * set the dir to write the snapshot dir into. this method is configured via
	 * the OUTPUTDIR_KEY string in the properties
	 */
    public void setDirName(String newPath) {
        outDirName = newPath;
        dirCreationTryed = false;
        outDir = null;
    }

    /**
	 * the minimum delay, in ms, between two writes of snapshots. if<=0, then no
	 * delay
	 */
    protected long writesDelay_ms = -1;

    /**
	 * specify the amount of time, in ms, to wait after a write, before another
	 * write is doable.
	 */
    public void setWritesDelay(long delay_ms) {
        writesDelay_ms = delay_ms;
    }

    /**
	 * should this write a snapshot every time a VM is modified ? if false,
	 * shnapshots will be written only when an hypervisor modification will
	 * happen
	 */
    protected boolean handleVMs = false;

    /**
	 * @param accept
	 *            should this write a snapshot every time a VM is modified ? If
	 *            false, then only {@link HVSnapshot} will imply the writing of
	 *            the snapshot
	 */
    public void setHandleVMs(boolean accept) {
        handleVMs = accept;
    }

    /**
	 * creates the directory to write snapshots once, and return it
	 * 
	 * @return the directory to put the data into, or null if not able to
	 */
    protected File getOutDir() {
        if (outDir == null) {
            if (dirCreationTryed || outDirName == null) {
                return null;
            } else {
                findDirectory();
            }
        }
        return outDir;
    }

    /**
	 * create the directory named by {@link #outDirName} if it doesn't exist,
	 * remove any file in it if {@link #shouldDeletePresentFiles}. If a file of
	 * name with such a name exist, do nothing.
	 */
    public void findDirectory() {
        dirCreationTryed = true;
        outDir = new File(outDirName + "/" + outDirDefaultName);
        if (!outDir.exists()) {
            boolean success = outDir.mkdirs();
            logger.info("creating logs directory : " + outDir + " , success : " + success);
            return;
        }
    }

    @Override
    public void configure(Properties prop) {
        setHandleVMs(Boolean.parseBoolean(prop.getProperty(HANDLEVM_KEY, "false")));
        setDirName(prop.getProperty(OUTPUTDIR_KEY, "./"));
        try {
            setWritesDelay(Long.parseLong(prop.getProperty(WRITESDELAY_KEY, "0")));
        } catch (Exception e) {
            logger.warn(e.toString());
        }
        getOutDir();
    }

    long nextTime = 0;

    /** the list of snapshots to add */
    LinkedHashSet<HVSnapshot> awaitingSnapshots = new LinkedHashSet<HVSnapshot>();

    /** the runnable to write the snapshots later */
    Runnable writeLater = null;

    protected Object writerLock = new Object();

    /** ask to write a snapshot as soon as possible */
    protected void pushSnapshot(HVSnapshot snapshot) {
        synchronized (writerLock) {
            if (writesDelay_ms <= 0) {
                writeSnapshot(snapshot);
            } else {
                long time = System.currentTimeMillis();
                if (time > nextTime) {
                    writeSnapshot(snapshot);
                    nextTime = time + writesDelay_ms;
                } else {
                    if (writeLater == null) {
                        final long delay = nextTime - time;
                        writeLater = new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(delay);
                                } catch (InterruptedException e) {
                                }
                                synchronized (writerLock) {
                                    for (HVSnapshot snapshot : awaitingSnapshots) {
                                        writeSnapshot(snapshot);
                                    }
                                    awaitingSnapshots.clear();
                                    writeLater = null;
                                    nextTime = System.currentTimeMillis() + writesDelay_ms;
                                }
                            }
                        };
                    } else {
                        awaitingSnapshots.add(snapshot);
                    }
                }
            }
        }
    }

    protected void writeSnapshot(HVSnapshot toPush) {
        File outDir = getOutDir();
        if (outDir != null) {
            try {
                File target = new File(outDir, "snapshot_" + nextSnapshotNumber + FILE_EXTENSION);
                OutputStream stream = new FileOutputStream(target);
                BufferedOutputStream buffer = new BufferedOutputStream(stream);
                ObjectOutputStream output = new ObjectOutputStream(buffer);
                output.writeObject(toPush);
                output.close();
                nextSnapshotNumber++;
            } catch (IOException e) {
                logger.error("while writting snapshot " + e.toString());
            }
        }
    }

    /** the number of the next snapshot */
    int nextSnapshotNumber = 1;

    @Override
    public void hypervisorModification(HVSnapshot snap) {
        pushSnapshot(snap);
    }

    @Override
    public void vmModification(VMSnapshot snapshot, HVSnapshot owner) {
        if (handleVMs) {
            hypervisorModification(owner);
        }
    }
}
