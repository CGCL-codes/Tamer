package com.alexeytsvetkov.android.myplacesalarm;

import android.location.Location;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MyPlacesAlarm {

    public static final String AUTHORITY = "com.alexeytsvetkov.provider.MyPlacesProvider";

    private MyPlacesAlarm() {
    }

    /**
	 * Places table
	 */
    public static final class Places implements BaseColumns {

        private Places() {
        }

        public static Location mlocation;

        /**
		 * The content:// style URL for this table
		 */
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/places");

        /**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of places.
		 */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.alexeytsvetkov.place";

        /**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * place.
		 */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.alexeytsvetkov.place";

        /**
		 * The default sort order for this table
		 */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /**
		 * The title of the place
		 * <P>
		 * Type: TEXT
		 * </P>
		 */
        public static final String TITLE = "title";

        /**
		 * State of place alarm
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
        public static final String ACTIVE = "active";

        /**
		 * Ringtone of alert
		 * <P>
		 * Type: STRING 
		 * </P>
		 */
        public static final String RINGTONE = "ringtone";

        /**
		 * Volume of alert
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
        public static final String VOLUME = "volume";

        /**
		 * Alert vibro 
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
        public static final String VIBRO = "vibro";

        /**
		 * The latitude of place
		 * <P>
		 * Type: DOUBLE
		 * </P>
		 */
        public static final String LATITUDE = "latitude";

        /**
		 * The longitude of place
		 * <P>
		 * Type: DOUBLE
		 * </P>
		 */
        public static final String LONGITUDE = "longitude";

        /**
		 * The radius of place
		 * <P>
		 * Type: INTEGER
		 * </P>
		 */
        public static final String RADIUS = "radius";

        /**
		 * The place zoom level  
		 * <P>
		 * Type: INTEGER 
		 * </P>
		 */
        public static final String ZOOMLEVEL = "zoomlevel";

        /**
		 * The timestamp for when the place was created
		 * <P>
		 * Type: INTEGER (long from System.curentTimeMillis())
		 * </P>
		 */
        public static final String CREATED_DATE = "created";

        /**
		 * The timestamp for when the place was last modified
		 * <P>
		 * Type: INTEGER (long from System.curentTimeMillis())
		 * </P>
		 */
        public static final String MODIFIED_DATE = "modified";
    }
}
