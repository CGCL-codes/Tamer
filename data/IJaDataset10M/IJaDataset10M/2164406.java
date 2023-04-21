package net.sourceforge.javabits.io.expiration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.javabits.io.ExpirationChecker;
import net.sourceforge.javabits.util.Many;
import org.codehaus.plexus.logging.AbstractLogEnabled;

/**
 * @author Jochen Kuhnle
 */
public class TimestampExpirationChecker extends AbstractLogEnabled implements ExpirationChecker {

    private final long tolerance;

    private Map<File, Long> lastModifiedMap = new HashMap<File, Long>();

    public TimestampExpirationChecker() {
        this(0L);
    }

    public TimestampExpirationChecker(long tolerance) {
        this.tolerance = tolerance;
    }

    protected synchronized long getLastModified(File file) {
        Long lastModified = lastModifiedMap.get(file);
        if (lastModified == null) {
            lastModified = file.lastModified();
            lastModifiedMap.put(file, lastModified);
        }
        return lastModified;
    }

    /**
     * Check task expiration by timestamp of the tasks source and target files.
     * 
     * @see net.sourceforge.javabits.io.ExpirationChecker#isExpired(java.util.Collection,
     *      java.util.Collection)
     */
    public synchronized boolean isExpired(Collection<File> sourceCollection, Collection<File> targetCollection) throws IOException {
        boolean expired = false;
        long newestSourceTime = 0;
        File newestSource = null;
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(String.format("Checking timestamps: Sources: %s, targets: %s", Many.join(sourceCollection, File.pathSeparator), Many.join(targetCollection, File.pathSeparator)));
        }
        for (File file : sourceCollection) {
            long lastModified = getLastModified(file);
            if (lastModified == 0) {
                throw new FileNotFoundException(String.format("File '%s' does not exist.", file));
            }
            if (lastModified > newestSourceTime) {
                newestSourceTime = lastModified;
                newestSource = file;
            }
        }
        if (sourceCollection.size() > 0 && newestSourceTime == 0) {
            if (getLogger().isDebugEnabled()) {
                getLogger().debug("No source file exists.");
            }
            expired = true;
        } else {
            for (File file : targetCollection) {
                long lastModified = getLastModified(file);
                if (lastModified == 0) {
                    if (getLogger().isDebugEnabled()) {
                        getLogger().debug(String.format("Target file '%s' does not exist.", file));
                    }
                    expired = true;
                    break;
                } else if (lastModified < newestSourceTime + tolerance) {
                    if (getLogger().isDebugEnabled()) {
                        getLogger().debug(String.format("Target file '%s' (%l) older than source file '%s' (%l).", file, lastModified, newestSource, newestSourceTime));
                    }
                    expired = true;
                    break;
                }
            }
        }
        return expired;
    }

    public synchronized void update(Collection<File> fileCollection) {
        long time = System.currentTimeMillis();
        for (File file : fileCollection) {
            lastModifiedMap.put(file, time);
        }
    }

    public synchronized void invalidate(Collection<File> fileCollection) {
        for (File file : fileCollection) {
            lastModifiedMap.remove(file);
        }
    }

    public synchronized void reset() {
        lastModifiedMap.clear();
    }
}
