package net.sf.magicmap.client.interfaces;

/**
 * 
 * @author thuebner
 */
public interface MapNamesCallback {

    /**
     * Open the map dialog
     * @param names - the names of the maps
     */
    public void openMapDialog(String[] names);

    /**
     * Get the retrieved map names
     * @param names - the names of the maps
     */
    public void retrievedMapNames(String[] names);

    /**
     * Get the error msg of the failed map names retrievement
     * @param e - the occurred exception
     */
    public void getMapNamesError(Exception e);
}
