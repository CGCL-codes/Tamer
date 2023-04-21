package alchemy.core;

import alchemy.fs.File;
import java.lang.ref.WeakReference;
import java.util.Hashtable;

/**
 * Cache of libraries.
 * Libraries are cached using filename and
 * file timestamp as keys.
 *
 * @author Sergey Basalaev
 */
class LibCache {

    /** Maps Files to LibCacheEntries. */
    private final Hashtable cache = new Hashtable();

    public LibCache() {
    }

    /**
	 * Returns library for given file and timestamp.
	 * If library with given parameters is not cached
	 * then <code>null</code> is returned.
	 */
    public synchronized Library getLibrary(File file, long tstamp) {
        LibCacheEntry entry = (LibCacheEntry) cache.get(file);
        if (entry == null) return null;
        if (entry.tstamp < tstamp) return null;
        return (Library) entry.lib.get();
    }

    /**
	 * Puts library in cache.
	 */
    public synchronized void putLibrary(File file, long tstamp, Library lib) {
        cache.put(file, new LibCacheEntry(new WeakReference(lib), tstamp));
    }
}

/**
 * Library, cached in <code>LibCache</code>.
 *
 * @author Sergey Basalaev
 */
class LibCacheEntry {

    WeakReference lib;

    long tstamp;

    public LibCacheEntry(WeakReference lib, long tstamp) {
        this.lib = lib;
        this.tstamp = tstamp;
    }
}
