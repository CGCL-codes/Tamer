package net.sourceforge.mazix.components.constants.log;

/**
 * The class which defines all info messages constants which are only written in the log file. The
 * messages also contains the info code.
 *
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.6
 * @version 0.7
 */
public final class InfoConstants {

    /** The copy file info message. */
    public static final String COPY_FILE_INFO = "[info-mazix-components-001] : Copying file {0} to {1}...";

    /** The delete directory info message. */
    public static final String DELETE_DIRECTORY_INFO = "[info-mazix-components-002] : Deleting directory {0}...";

    /** The displaying sound properties info message. */
    public static final String DISPLAYING_AUDIO_PROPERTIES_INFO = "[info-mazix-components-003] : Displaying sound properties of file {0} : {1}";

    /** The fonts found from local resources info message. */
    public static final String FONTS_FOUND_FROM_RESOURCES_INFO = "[info-mazix-components-004] : Fonts retrieved from the local resources directory ({0})";

    /** The read file info message. */
    public static final String READ_FILE_INFO = "[info-mazix-components-005] : Reading file content {0}...";

    /** The cached resource info message. */
    public static final String RESOURCE_CACHED_INFO = "[info-mazix-components-006] : Cached resource retrieved ({0})";

    /** The resource key built info message. */
    public static final String RESOURCE_KEY_BUILT_INFO = "[info-mazix-components-007] : Resource key built ({0})";

    /** The write file info message. */
    public static final String WRITE_FILE_INFO = "[info-mazix-components-008] : Writing content into file {0}...";

    /**
     * Private constructor to prevent from instantiation.
     *
     * @since 0.7
     */
    private InfoConstants() {
        super();
    }
}
