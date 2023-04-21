package net.sf.pdfizer.model;

/**
 * This interface denotes objects which allow dropping files on themselves  
 *
 */
public interface Droppable extends ICatalogReferencer {

    /**
     * Path to put dropping file
     * @return
     */
    String getPathToDrop();

    /**
     * Whether drop is permitted for given file 
     * @param aFilename
     * @return
     */
    boolean isDropEnabled();
}
