package com.gobynote.android.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class CategoryData {

    public static String AUTHORITY = "com.gobynote.data.CategoryProvider";

    public CategoryData() {
    }

    public static final class CategoryColumns implements BaseColumns {

        private CategoryColumns() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/category");

        public static final String ID = "id";

        public static final String TITLE = "title";
    }

    public static String[] getAllColumns() {
        String[] columns = new String[] { CategoryColumns.ID, CategoryColumns.TITLE };
        return columns;
    }
}
