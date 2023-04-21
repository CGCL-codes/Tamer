package com.veronicasun.lma;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Sms {

    public static final String AUTHORITY = "com.veronica.lma.provider";

    private Sms() {
    }

    public static class SmsColumn implements BaseColumns {

        public SmsColumn() {
        }

        ;

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/sms");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.veronica.sms";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.veronica.sms";

        public static final String DEFAULT_SORT_ORDER = "date DESC";

        public static final String NUM = "num";

        public static final String CONTENT = "content";

        public static final String DATE = "date";
    }
}
