package werkzeugkasten.resource.synchronizer.nls;

import org.eclipse.osgi.util.NLS;

/**
 * 
 * @author taichi
 * 
 */
public class Strings extends NLS {

    public static String MSG_START_SERVER;

    public static String MSG_STOP_SERVER;

    public static String MSG_REFRESH_RESOURCE;

    public static String LABEL_START;

    public static String LABEL_STOP;

    public static String MSG_NEW_DEBUG_JSP;

    public static String TITLE_NEW_DEBUG_JSP;

    public static String MSG_CREATE_CONTENTS;

    public static String LABEL_CONTEXT_ROOT_DIR;

    public static String LABEL_OUT_PUT_DIR;

    public static String LABEL_BROWSE;

    public static String MSG_SELECT_CONTEXT_ROOT_DIR;

    public static String MSG_SELECT_OUTPUT_DIR;

    public static String ERR_SELECT_OUTPUT_DIR;

    public static String ERR_OUTPUT_DIR_HAS_ROOT;

    public static String ERR_OUTPUT_DIR_ALREADY_EXISTS;

    static {
        Class<Strings> clazz = Strings.class;
        NLS.initializeMessages(clazz.getName(), clazz);
    }
}
