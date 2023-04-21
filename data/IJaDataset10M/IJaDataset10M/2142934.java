package yu.ac.bg.rcub.grid.dataManagement;

/**
 * Provides information and access to data storage resources of a grid.
 * <p>
 * Information includes list of available storage elements, virtual organisation,
 * and information about grid file catalalogue: catalog type, file catalogue path-separator
 * character and reference to it's root directory.
 * <p>
 * Information is independent of underlying grid middleware.
 * @author Dragan Okiljevi�
 * @version 1.2 06/10/15
 * @see SEList
 * @see DirectoryItem
 * @since 1.0
 */
public interface DataStorageInterface {

    /**
     * Returns list of available storage elements.
     * @return list of available storage elements.
     * @see SEList
     */
    public abstract SEList getSEList();

    /**
     * Returns name of Virtual Organisation.
     * @return name of Virtual Organisation.
     */
    public abstract String getVO();

    /**
     * Returns type of used grid file catalogue.
     * @return type of used grid file catalogue.
     */
    public abstract String getLFCHost();

    /**
     * Returns host of the logical file catalogue.
     * @return host of the logical file catalogue.
     */
    public abstract String getX509UserProxy();

    /**
     * Returns host of the logical file catalogue.
     * @return host of the logical file catalogue.
     */
    public abstract String getCatalogType();

    /**
     * Returns root directory of grid file catalogue.
     * @return root directory of grid file catalogue.
     * @see DirectoryItem
     */
    public abstract DirectoryItem getRoot();

    /**
     * Returns default pathname-separator character for used grid file catalogue.
     * @return default pathname-separator character for used grid file catalogue.
     */
    public abstract String getSeparator();

    /**
     * Returns the {@link Item} of the corresponding type for a given pathname.
     * @param pathName Pathname for the <CODE>Item</CODE> to look for in a grid file catalogue.
     * @return The <CODE>Item</CODE> of the corresponding type for a given pathname or <CODE>null</CODE> if the pathname is invalid.
     * @since 1.2
     * @see FileItem
     * @see DirectoryItem
     * @see AliasItem
     */
    public abstract Item getItem(String vo, String lfc_host, String bdii_server, String userProxy, String pathName);
}
