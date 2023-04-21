package jpersist.interfaces;

/**
 * This interface is optionally implemented to provide overriding mapping 
 * between classes and tables and is only needed when a match can't be 
 * made due to a vast difference in naming and/or a collision will occur.
 */
public interface TableMapping {

    /**
     * Method returns the mapped name.
     *
     * @param name the lowercase name that is being mapped
     *
     * @return the lowercase mapped name
     */
    String getDatabaseTableName(String name);
}
