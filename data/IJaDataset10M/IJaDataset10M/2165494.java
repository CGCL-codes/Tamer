package cz.hdf.cdnavigator.db;

import cz.hdf.cdnavigator.Utils;

/**
 * This is data structure for Photo Album records from DB.
 *
 * @author hunter
 */
public class PhotoAlbumData {

    /** Null for int */
    public static final int BAD_INT_VALUE = -1;

    /** photo album id */
    private int id;

    /** CD id with photo album */
    private int cdid;

    /** photo album name */
    private String name;

    /** photo album info */
    private String info;

    /** photo rate */
    private int rate;

    /**
   * Initialize object with no data.
   */
    public PhotoAlbumData() {
        this(BAD_INT_VALUE, BAD_INT_VALUE, null, null, new Integer(Utils.RATING_0).intValue());
    }

    /**
   * Initilize object.
   *
   * @param _id id
   * @param _cdid CD id
   * @param _name album name
   * @param _info album info
   * @param _rate array of photos
   */
    public PhotoAlbumData(int _id, int _cdid, String _name, String _info, int _rate) {
        id = _id;
        cdid = _cdid;
        name = _name;
        info = _info;
        rate = _rate;
    }

    /**
   * Get photo album id.
   *
   * @return Returns the id.
   */
    public int getID() {
        return id;
    }

    /**
   * Set photo album id.
   *
   * @param _id The id to set.
   */
    public void setID(int _id) {
        id = _id;
    }

    /**
   * Get CD ID of CD with photo album.
   *
   * @return CD ID
   */
    public int getCDID() {
        return cdid;
    }

    /**
   * Set CD ID of CD with photo album.
   *
   * @param _cdid CD ID
   */
    public void setCDID(int _cdid) {
        cdid = _cdid;
    }

    /**
   * Get photo album name.
   *
   * @return album name
   */
    public String getName() {
        return name;
    }

    /**
   * Set photo album name
   *
   * @param _name album name
   */
    public void setName(String _name) {
        name = _name;
    }

    /**
   * Get photo album info.
   *
   * @return Returns the info.
   */
    public String getInfo() {
        return info;
    }

    /**
   * Set photo album info.
   *
   * @param _info The info to set.
   */
    public void setInfo(String _info) {
        info = _info;
    }

    /**
   * Get photo album rate.
   *
   * @return Returns the album rate.
   */
    public int getRate() {
        return rate;
    }

    /**
   * Set photo album rate.
   *
   * @param _rate The album rate to set.
   */
    public void setRate(int _rate) {
        rate = _rate;
    }

    /**
   * Copy all values.
   * 
   * @param _pad
   */
    public void set(PhotoAlbumData _pad) {
        setID(_pad.getID());
        setCDID(_pad.getCDID());
        setName(_pad.getName());
        setInfo(_pad.getInfo());
        setRate(_pad.getRate());
    }
}
