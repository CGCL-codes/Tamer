package cz.hdf.cdnavigator.gui.data;

import cz.hdf.i18n.I18N;
import java.io.File;

/**
 * Structure to store song data for mtSongsList.
 * 
 * @author hunter
 */
public class SongsListRecord {

    /** song id */
    public int id;

    /** song album id */
    public int albumID;

    /** song number */
    public int number;

    /** song name */
    public String name;

    /** song file */
    public String file;

    /**
   * DOCUMENT ME!
   *
   * @param _id
   * @param _albumID
   * @param _number
   * @param _name
   * @param _file
   */
    public SongsListRecord(int _id, int _albumID, int _number, String _name, String _file) {
        id = _id;
        albumID = _albumID;
        number = _number;
        name = _name;
        file = _file;
    }

    /**
   * DOCUMENT ME!
   *
   * @return
   */
    public String toString() {
        String record = "";
        if (id < 0) {
            record += ("[" + I18N.translate("Scanned data") + "] ");
        }
        record += (number + ": ");
        if ((name != null) && (!name.equals(""))) {
            record += name;
        } else {
            if (file != null) {
                record += file;
            }
        }
        return record;
    }

    /**
   * DOCUMENT ME!
   *
   * @param obj
   *
   * @return
   */
    public boolean equals(Object obj) {
        SongsListRecord slr = null;
        try {
            slr = (SongsListRecord) obj;
        } catch (ClassCastException e) {
            return false;
        }
        if (slr == null) {
            return false;
        }
        if ((name == null) && (slr.name != null)) {
            return false;
        }
        if ((file == null) && (slr.file != null)) {
            return false;
        }
        return ((id == slr.id) && (albumID == slr.albumID) && (number == slr.number) && (((name == null) && (slr.name == null)) || (name.equals(slr.name))) && (((file == null) && (slr.file == null)) || (file.equals(slr.file))));
    }
}
