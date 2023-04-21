package com.akop.spark;

import android.net.Uri;
import android.provider.BaseColumns;
import com.akop.spark.provider.PsnProvider;

public class PSN {

    public static final class Profiles implements BaseColumns {

        private Profiles() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + PsnProvider.AUTHORITY + "/profiles");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.psn-profile";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.psn-profile";

        public static final String ACCOUNT_ID = "AccountId";

        public static final String UUID = "Uuid";

        public static final String ONLINE_ID = "OnlineId";

        public static final String ICON_URL = "IconUrl";

        public static final String LEVEL = "Level";

        public static final String PROGRESS = "Progress";

        public static final String DEFAULT_SORT_ORDER = ONLINE_ID + " ASC";
    }
}
