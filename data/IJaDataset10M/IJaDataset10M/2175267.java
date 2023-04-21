package uk.ac.cam.caret.minibix.archive.impl.file;

import java.io.*;
import java.util.*;
import uk.ac.cam.caret.minibix.archive.api.Archive;
import uk.ac.cam.caret.minibix.archive.api.Connection;
import uk.ac.cam.caret.minibix.archive.api.ItemFactory;
import uk.ac.cam.caret.minibix.archive.api.LowLevelStorageException;
import uk.ac.cam.caret.minibix.archive.api.MetadatumFactory;
import uk.ac.cam.caret.minibix.archive.api.PlannedDowntimeException;
import uk.ac.cam.caret.minibix.archive.impl.file.lock.Flock;
import uk.ac.cam.caret.minibix.archive.impl.file.lock.FlockSet;

/** An implementation of Archive in the VCRUD API. After construction, before calling any
 * other methods, {@link #setDirectory(String)} must be called.
 * 
 * @author Dan Sheppard, dan@caret.cam.ac.uk
 *
 */
public class FileArchive implements Archive {

    private File dir;

    private Map<String, ItemFactory> item_factories = new HashMap<String, ItemFactory>();

    private Map<String, MetadatumFactory> md_factories = new HashMap<String, MetadatumFactory>();

    private int conn = 0;

    private FlockSet archivelocks = new FlockSet();

    private boolean inited = false;

    /** Create a new FileArchive. Note that {@link #setDirectory(String)} must be called
	 * before any other method.
	 */
    public FileArchive() {
    }

    private void init() throws LowLevelStorageException {
        if (inited) return;
        archivelocks.setLocation(new File(dir, "archlocks"));
        inited = true;
    }

    void increaseConnections(int delta) {
        conn += delta;
    }

    /** Set the directory in which the archive will be created. If the directory may not
	 * exist, call {@link #createIfNeeded()} before any other methods. It is safe to call
	 * that method even if the archive may exist.
	 * 
	 * @param in path to archive directory (may or may not exist)
	 * @throws LowLevelStorageException IOException, or similar
	 */
    public void setDirectory(String in) throws LowLevelStorageException {
        dir = new File(in);
        inited = false;
    }

    File getDirectory() {
        return dir;
    }

    FlockSet getArchiveLocks() {
        return archivelocks;
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public Connection getReadWriteConnection(int seq) throws LowLevelStorageException, PlannedDowntimeException {
        init();
        return new ConnectionImpl(this, seq, false, true);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public Connection getReadOnlyConnection(int seq) throws LowLevelStorageException, PlannedDowntimeException {
        init();
        return new ConnectionImpl(this, seq, true, true);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public Connection getReadOnlyConnection() throws LowLevelStorageException, PlannedDowntimeException {
        init();
        return new ConnectionImpl(this, -1, true, true);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public Connection getReadWriteConnection() throws LowLevelStorageException, PlannedDowntimeException {
        init();
        return new ConnectionImpl(this, -1, false, true);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public void registerItemFactory(ItemFactory in) {
        item_factories.put(in.getName(), in);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public void registerMetadatumFactory(MetadatumFactory in) {
        md_factories.put(in.getName(), in);
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public void preparePlannedDowntime(String message) throws LowLevelStorageException {
        try {
            init();
            Flock down = archivelocks.getLock("down");
            down.acquireExclusive();
            try {
                File downtime = new File(dir, "downtime");
                BufferedWriter out = new BufferedWriter(new FileWriter(downtime));
                out.write(message);
                out.close();
                File downtime_prepare = new File(dir, "downtime-prepare");
                downtime_prepare.createNewFile();
            } finally {
                down.drop();
            }
        } catch (IOException x) {
            throw new LowLevelStorageException("Could not create downtime", x);
        }
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public void beginPlannedDowntime() throws LowLevelStorageException {
        try {
            File downtime_prepare = new File(dir, "downtime-prepare");
            if (!downtime_prepare.exists()) throw new LowLevelStorageException("No downtime prepared");
            File downtime_on = new File(dir, "downtime-on");
            downtime_on.createNewFile();
        } catch (IOException x) {
            throw new LowLevelStorageException("Could not create downtime", x);
        }
    }

    /** As defined in the VCRUD API. Implemented by File implementation.
	 */
    public void endPlannedDowntime() throws LowLevelStorageException {
        init();
        File downtime_prepare = new File(dir, "downtime-prepare");
        File downtime_on = new File(dir, "downtime-on");
        File downtime = new File(dir, "downtime");
        Flock down = archivelocks.getLock("down");
        down.acquireExclusive();
        try {
            downtime_on.delete();
            downtime_prepare.delete();
            downtime.delete();
        } finally {
            down.drop();
        }
    }

    ItemFactory getItemFactory(String name) {
        return item_factories.get(name);
    }

    MetadatumFactory getMetadatumFactory(String name) {
        return md_factories.get(name);
    }

    /** Create archive if needed. Note that this is not synchronized, so should not be
	 * called in-process as part of regular operations.
	 * 
	 * @throws IOException Something went wrong creating it (already existing is not an error)
	 */
    private void mkdir(File in) throws IOException {
        if (!in.exists()) if (!in.mkdir()) throw new IOException("Could not create " + in.getAbsolutePath());
    }

    /** Creates the directory structure which must exist in a datastore before execution.
	 * Can be called safely on an existing archive.
	 * 
	 * @throws IOException error creating datastore.
	 */
    public void createIfNeeded() throws IOException {
        if (dir == null) throw new IOException("Attempt to create archive at null");
        mkdir(dir);
        mkdir(new File(dir, "locks"));
        mkdir(new File(dir, "misclocks"));
        mkdir(new File(dir, "archlocks"));
        mkdir(new File(dir, "redolocks"));
        mkdir(new File(dir, "data"));
        File m = new File(dir, "mapper");
        mkdir(m);
        mkdir(new File(m, "data"));
    }

    private void recursiveDelete(File f) {
        if (f.isDirectory()) {
            for (File g : f.listFiles()) recursiveDelete(g);
        }
        f.delete();
    }

    /** @exclude */
    public void delete() throws IOException {
        if (dir == null) throw new IOException("Attempt to delete archive at null");
        recursiveDelete(dir);
    }

    /** @exclude */
    public boolean allConnectionsClosed() {
        return conn == 0;
    }

    /** @exclude */
    public int numberOfDeposits() {
        File data = new File(dir, "data");
        return data.listFiles().length;
    }

    /** @exclude */
    public boolean anyDegraded() {
        File data = new File(dir, "data");
        for (File d : data.listFiles()) if (new File(d, "degraded").exists()) return true;
        return false;
    }
}
