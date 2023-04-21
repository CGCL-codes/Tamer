package org.opencms.search.documents;

import org.opencms.i18n.A_CmsMessageBundle;
import org.opencms.i18n.I_CmsMessageBundle;

/**
 * Convenience class to access the localized messages of this OpenCms package.<p> 
 * 
 * @author Jan Baudisch 
 * 
 * @version $Revision: 1.8 $ 
 * 
 * @since 6.0.0 
 */
public final class Messages extends A_CmsMessageBundle {

    /** Message constant for key in the resource bundle. */
    public static final String ERR_CREATE_DOC_KEY_0 = "ERR_CREATE_DOC_KEY_0";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_DECRYPTING_RESOURCE_1 = "ERR_DECRYPTING_RESOURCE_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_EXTRACTION_CLASS_2 = "ERR_EXTRACTION_CLASS_2";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_NO_CONTENT_1 = "ERR_NO_CONTENT_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_NO_EXCEL_FORMAT_1 = "ERR_NO_EXCEL_FORMAT_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_NO_RAW_CONTENT_1 = "ERR_NO_RAW_CONTENT_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_PWD_PROTECTED_1 = "ERR_PWD_PROTECTED_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_RESOURCE_TYPE_INSTANTIATION_1 = "ERR_RESOURCE_TYPE_INSTANTIATION_1";

    /** Message constant for key in the resource bundle. */
    public static final String ERR_TEXT_EXTRACTION_1 = "ERR_TEXT_EXTRACTION_1";

    /** Message constant for key in the resource bundle. */
    public static final String LOG_EXCERPT_CACHE_DELETE_ERROR_1 = "LOG_EXCERPT_CACHE_DELETE_ERROR_1";

    /** Name of the used resource bundle. */
    private static final String BUNDLE_NAME = "org.opencms.search.documents.messages";

    /** Static instance member. */
    private static final I_CmsMessageBundle INSTANCE = new Messages();

    /**
     * Hides the public constructor for this utility class.<p>
     */
    private Messages() {
    }

    /**
     * Returns an instance of this localized message accessor.<p>
     * 
     * @return an instance of this localized message accessor
     */
    public static I_CmsMessageBundle get() {
        return INSTANCE;
    }

    /**
     * Returns the bundle name for this OpenCms package.<p>
     * 
     * @return the bundle name for this OpenCms package
     */
    public String getBundleName() {
        return BUNDLE_NAME;
    }
}
