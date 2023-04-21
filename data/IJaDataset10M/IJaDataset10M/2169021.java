package ch.iserver.ace.net.impl.protocol;

/**
 *
 */
public interface ProtocolConstants {

    public static final String PROFILE_URI = "http://ace.iserver.ch/profiles/ACE";

    public static final int LISTENING_PORT = 4123;

    /** constants for serialization **/
    public static final int PUBLISHED_DOCUMENTS = 0;

    public static final int PUBLISH = 1;

    public static final int CONCEAL = 2;

    public static final int SEND_DOCUMENTS = 3;

    public static final int DOCUMENT_DETAILS_CHANGED = 4;

    public static final String[] NAMES = new String[] { "docs" };

    /** constants for deserialization **/
    public static final String QUERY_TYPE = "type";

    public static final String QUERY_TYPE_PUBLISHED_DOCUMENTS = "docs";

    public static final String RESPONSE_PUBLISHED_DOCUMENTS = "publishedDocs";

    public static final String NOTIFICATION_PUBLISHED_DOCUMENTS = "publishedDocs";

    public static final String NOTIFICATION_DOCUMENT_DETAILS_CHANGED = "docDetailsChanged";

    public static final String DOCUMENT_NAME = "name";

    public static final String DOCUMENT_ID = "id";

    public static final String USER_ID = "userid";

    public static final String TAG_QUERY = "query";

    public static final String TAG_PUBLISHED_DOCS = "publishedDocs";

    public static final String TAG_PUBLISH = "publishDocs";

    public static final String TAG_CONCEAL = "concealDocs";

    public static final String TAG_DOCUMENT_DETAILS_CHANGED = "docDetailsChanged";

    public static final String TAG_DOC = "doc";
}
