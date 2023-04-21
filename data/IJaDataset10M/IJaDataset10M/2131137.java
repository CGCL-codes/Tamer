package cz.hdf.cdnavigator.gui.data;

import cz.hdf.cdnavigator.db.CDData;

/**
 * Structure to store CD ID and file to cache.
 *
 * @author hunter
 */
public class CacheFileRecord implements FileRecord {

    /** CD ID with this file */
    private CDData cd;

    /** file without cache dir, only relative path on CD */
    private String file;

    /**
   * Initialize file record.
   *
   * @param _cdID CD ID
   * @param _file relative file name
   */
    public CacheFileRecord(CDData _cd, String _file) {
        cd = _cd;
        file = _file;
    }

    /**
   * @see FileRecord#getCD()
   */
    public CDData getCD() {
        return cd;
    }

    /**
   * @see FileRecord#getFileName()
   */
    public String getFileName() {
        return file;
    }
}
