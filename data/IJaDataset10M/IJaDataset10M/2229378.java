package org.snipsnap.versioning;

import org.snipsnap.snip.Snip;
import java.util.List;

/**
 * Stores versions of snips
 *
 * @author Stephan J. Schmidt
 * @version $Id: VersionStorage.java 1268 2003-12-15 09:18:10Z leo $
 */
public interface VersionStorage {

    /**
   * Return a list of VersionInfo objects for the
   * given snip. Objects should be ordered by decreasing version
   *
   * @param snip Snip for which the revision should be loaded
   * @return
   */
    public List getVersionHistory(Snip snip);

    /**
   * Load a version of a snip from the storage
   *
   * @param snip Example of a snip to load
   * @param version Version number
   * @return
   */
    public Snip loadVersion(Snip snip, int version);

    /**
   * Stora a version of a snip in the storage.
   *
   * @param snip Snip to store
   */
    public void storeVersion(Snip snip);
}
