package com.fom2008.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class FomTag {

    public static final class Tags implements BaseColumns {

        /**
		 * The content:// style URI for this table - this to see all fomtags or
		 * append an id to see just the matching tag.
		 */
        public static final Uri CONTENT_URI = Uri.parse("content://com.fom2008.db.fomtags/tags");

        /**
		 * The content URI that we intent to tag with geolocation information
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
        public static final String TAGGED_CONTENT = "uri";

        /**
		 * A short description of the tagged content
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
        public static final String DESCRIPTION = "description";

        /**
		 * The latitude for the tagged content
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
        public static final String LATITUDE = "latitude";

        /**
		 * The longitude for the tagged content
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
        public static final String LONGITUDE = "longitude";

        /**
		 * The default sort order
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
        public static final String SORT_ORDER = "latitude DESC";
    }

    /**
	 * Convenience definition for the projection used against the content
	 * provider
	 */
    public static final String[] FOMTAG_PROJECTION = { Tags._ID, Tags.TAGGED_CONTENT, Tags.LATITUDE, Tags.LONGITUDE, Tags.DESCRIPTION };

    /**
	 * The root authority for the WikiNotesProvider
	 */
    public static final String FOMTAG_AUTHORITY = "com.fom2008.db.fomtags";
}
