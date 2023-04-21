package com.android.launcher;

import android.content.ContentValues;
import java.util.ArrayList;

/**
 * Represents a folder containing shortcuts or apps.
 */
class UserFolderInfo extends FolderInfo {

    /**
     * The apps and shortcuts 
     */
    ArrayList<ApplicationInfo> contents = new ArrayList<ApplicationInfo>();

    UserFolderInfo() {
        itemType = LauncherSettings.Favorites.ITEM_TYPE_USER_FOLDER;
    }

    /**
     * Add an app or shortcut
     * 
     * @param item
     */
    public void add(ApplicationInfo item) {
        contents.add(item);
    }

    @Override
    void onAddToDatabase(ContentValues values) {
        super.onAddToDatabase(values);
        values.put(LauncherSettings.Favorites.TITLE, title.toString());
    }
}
