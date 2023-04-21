package cz.hdf.cdnavigator.gui.data;

import cz.hdf.i18n.I18N;

/**
 * Structure to store some data.
 * 
 * @author hunter
 */
public class FoundedRecord {

    /** data from DB */
    public static final int TYPE_DB = 1;

    /** scanned data */
    public static final int TYPE_SCAN = 2;

    /** data from roast temp dir */
    public static final int TYPE_ROAST = 3;

    public int data;

    /** data are scanned from new CD or from real DB */
    public int type;

    public FoundedRecord() {
        type = TYPE_DB;
    }

    /**
   * DOCUMENT ME!
   *
   * @param _pad
   * @param _count
   * @param _type
   */
    public FoundedRecord(int _type) {
        type = _type;
    }

    /**
   * DOCUMENT ME!
   *
   * @return
   */
    public String toString() {
        String record = "";
        if (type == TYPE_SCAN) {
            record = "[" + I18N.translate("Scanned data") + "] ";
        } else if (type == TYPE_ROAST) {
            record = "[" + I18N.translate("Roast temp") + "] ";
        }
        return record;
    }
}
