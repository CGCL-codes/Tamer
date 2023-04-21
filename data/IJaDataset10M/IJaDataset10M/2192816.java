package org.dctmutils.common.constant;

/**
 * Generic Documentum constants. TODO - replace this with enums.
 * 
 * @author <a href="mailto:luther@dctmutils.org">Luther E. Birdzell</a>
 */
public interface DctmConstants {

    /**
     * r_object_id
     */
    public static final String OBJECT_ID = "r_object_id";

    /**
     * r_object_type
     */
    public static final String OBJECT_TYPE = "r_object_type";

    /**
     * object_name
     */
    public static final String OBJECT_NAME = "object_name";

    /**
     * title
     */
    public static final String TITLE = "title";

    /**
     * subject
     */
    public static final String SUBJECT = "subject";

    /**
     * authors
     */
    public static final String AUTHORS = "authors";

    /**
     * keywords
     */
    public static final String KEYWORDS = "keywords";

    /**
     * Documentum attribute: a_content_type
     */
    public static final String ATTR_A_CONTENT_TYPE = "a_content_type";

    /**
     * Documentum attribute: i_chronicle_id
     */
    public static final String ATTR_I_CHRONICLE_ID = "i_chronicle_id";

    /**
     * Documentum attribute: object_name
     */
    public static final String ATTR_OBJECT_NAME = "object_name";

    /**
     * Documentum attribute: r_content_size
     */
    public static final String ATTR_R_CONTENT_SIZE = "r_content_size";

    /**
     * Documentum attribute: r_object_id
     */
    public static final String ATTR_R_OBJECT_ID = "r_object_id";

    /**
     * The 'xml' format. Used with IDfDocument.setContentType().
     */
    public static final String FORMAT_XML = "xml";

    /**
     * The 'pdf' format. Used with IDfDocument.setContentType().
     */
    public static final String FORMAT_PDF = "pdf";

    /**
     * The dm_document object type.
     */
    public static final String TYPE_DM_DOCUMENT = "dm_document";

    /**
     * The dm_folder object type.
     */
    public static final String TYPE_DM_FOLDER = "dm_folder";

    /**
     * The dm_cabinet object type.
     */
    public static final String TYPE_DM_CABINET = "dm_cabinet";

    /**
     * The wcm_channel_fld object type.
     */
    public static final String TYPE_WCM_CHANNEL_FLD = "wcm_channel_fld";

    /**
     * The wcm_channel object type.
     */
    public static final String TYPE_WCM_CHANNEL = "wcm_channel";

    /**
     * Default WCM WIP Lifecycle state.
     */
    public static final String WCM_WIP_LC_STATE = "WIP";

    /**
     * Default WCM Staging Lifecycle state.
     */
    public static final String WCM_STAGING_LC_STATE = "Staging";

    /**
     * Default WCM Approved Lifecycle state.
     */
    public static final String WCM_APPROVED_LC_STATE = "Approved";

    /**
     * Default WCM 'Active' version label.
     */
    public static final String WCM_ACTIVE_VERSION_LABEL = "Active";

    /**
     * Default WCM 'Expired' version label.
     */
    public static final String WCM_EXPIRED_VERSION_LABEL = "Expired";
}
