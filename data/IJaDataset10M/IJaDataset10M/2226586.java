package com.akop.spark;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import com.akop.spark.provider.XblProvider;

public class XBoxLive {

    public static final int STATUS_INVITE_SENT = 1;

    public static final int STATUS_INVITE_RCVD = 2;

    public static final int STATUS_ONLINE = 3;

    public static final int STATUS_AWAY = 4;

    public static final int STATUS_OFFLINE = 5;

    public static final int STATUS_OTHER = 99;

    public static final int MESSAGE_OTHER = 0;

    public static final int MESSAGE_TEXT = 1;

    public static final int MESSAGE_VOICE = 2;

    public static final class Profiles implements BaseColumns {

        private Profiles() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + XblProvider.AUTHORITY + "/profiles");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.xbl-profile";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.xbl-profile";

        public static final String ACCOUNT_ID = "AccountId";

        public static final String UUID = "Uuid";

        public static final String GAMERTAG = "Gamertag";

        public static final String ICON_URL = "IconUrl";

        public static final String REP = "Rep";

        public static final String GAMERSCORE = "Gamerscore";

        public static final String DEFAULT_SORT_ORDER = GAMERTAG + " ASC";
    }

    public static final class Friends implements BaseColumns {

        private Friends() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + XblProvider.AUTHORITY + "/friends");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.xbl-friend";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.xbl-friend";

        public static final String ACCOUNT_ID = "AccountId";

        public static final String GAMERTAG = "Gamertag";

        public static final String ICON_URL = "IconUrl";

        public static final String GAMERSCORE = "Gamerscore";

        public static final String STATUS = "StatusDescription";

        public static final String STATUS_CODE = "StatusCode";

        public static final String NAME = "Name";

        public static final String LOCATION = "Location";

        public static final String BIO = "Bio";

        public static final String REP = "Rep";

        public static final String DESCRIPTION = "Info";

        public static final String DELETE_MARKER = "DeleteMarker";

        public static final String LAST_UPDATED = "LastUpdated";

        public static final String DEFAULT_SORT_ORDER = STATUS_CODE + " ASC," + GAMERTAG + " COLLATE NOCASE ASC";

        public static String getGamertag(Context context, long friendId) {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(CONTENT_URI, friendId), new String[] { GAMERTAG }, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) return cursor.getString(0);
                throw new IllegalArgumentException(context.getString(R.string.error_friend_not_found));
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        public static int getActiveFriendCount(Context context, XblAccount account) {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] { _ID }, ACCOUNT_ID + "=" + account.getId() + " AND (" + STATUS_CODE + "=" + STATUS_ONLINE + " OR " + STATUS_CODE + "=" + "=" + STATUS_AWAY + ")", null, null);
            try {
                if (cursor != null) return cursor.getCount();
            } finally {
                if (cursor != null) cursor.close();
            }
            return 0;
        }
    }

    public static final class Games implements BaseColumns {

        private Games() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + XblProvider.AUTHORITY + "/games");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.xbl-game";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.xbl-game";

        public static final String ACCOUNT_ID = "AccountId";

        public static final String TITLE = "Title";

        public static final String UID = "Uid";

        public static final String ICON_URL = "IconUrl";

        public static final String LAST_PLAYED = "LastPlayed";

        public static final String POINTS_ACQUIRED = "PointsAcquired";

        public static final String POINTS_TOTAL = "PointsTotal";

        public static final String ACHIEVEMENTS_UNLOCKED = "AchievementsUnlocked";

        public static final String ACHIEVEMENTS_TOTAL = "AchievementsTotal";

        public static final String ACHIEVEMENTS_STATUS = "AchievementsStatus";

        public static final String LAST_UPDATED = "LastUpdated";

        public static final String DEFAULT_SORT_ORDER = LAST_PLAYED + " DESC";

        public static String getUid(Context context, long gameId) {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(CONTENT_URI, gameId), new String[] { UID }, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) return cursor.getString(0);
                throw new IllegalArgumentException(context.getString(R.string.error_game_not_found));
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        public static String getLastPlayed(Context context, XblAccount account) {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] { TITLE }, ACCOUNT_ID + "=" + account.getId(), null, LAST_PLAYED + " DESC");
            try {
                if (cursor != null && cursor.moveToFirst()) return cursor.getString(0);
                return null;
            } finally {
                if (cursor != null) cursor.close();
            }
        }
    }

    public static final class Achievements implements BaseColumns {

        private Achievements() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + XblProvider.AUTHORITY + "/achievements");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.xbl-achievement";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.xbl-achievement";

        public static final String GAME_ID = "GameId";

        public static final String UID = "Uid";

        public static final String TITLE = "Title";

        public static final String DESCRIPTION = "Description";

        public static final String ICON_URL = "IconUrl";

        public static final String POINTS = "Points";

        public static final String ACQUIRED = "Acquired";

        public static final String LOCKED = "Locked";

        public static final String INDEX = "ListIndex";

        public static final String DEFAULT_SORT_ORDER = INDEX + " ASC";
    }

    public static final class Messages implements BaseColumns {

        private Messages() {
        }

        public static final Uri CONTENT_URI = Uri.parse("content://" + XblProvider.AUTHORITY + "/messages");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.akop.spark.xbl-message";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.akop.spark.xbl-message";

        public static final String ACCOUNT_ID = "AccountId";

        public static final String UID = "Uid";

        public static final String SENDER = "Sender";

        public static final String TYPE = "MessageType";

        public static final String BODY = "Body";

        public static final String SENT = "Sent";

        public static final String IS_DIRTY = "IsDirty";

        public static final String IS_READ = "IsRead";

        public static final String DELETE_MARKER = "DeleteMarker";

        public static final String DEFAULT_SORT_ORDER = SENT + " DESC";

        public static long getUid(Context context, long messageId) {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(CONTENT_URI, messageId), new String[] { UID }, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) return cursor.getLong(0);
                throw new IllegalArgumentException(context.getString(R.string.error_msg_not_found));
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        public static Account getAccount(Context context, long messageId) {
            Cursor cursor = context.getContentResolver().query(ContentUris.withAppendedId(CONTENT_URI, messageId), new String[] { ACCOUNT_ID }, null, null, null);
            long accountId = -1;
            try {
                if (cursor != null && cursor.moveToFirst()) accountId = cursor.getLong(0);
                if (accountId < 0) throw new IllegalArgumentException(context.getString(R.string.error_msg_not_found));
            } finally {
                if (cursor != null) cursor.close();
            }
            return Preferences.getPreferences(context).getAccount(accountId);
        }

        public static int getUnreadMessageCount(Context context, XblAccount account) {
            Cursor cursor = context.getContentResolver().query(CONTENT_URI, new String[] { _ID }, ACCOUNT_ID + "=" + account.getId() + " AND " + IS_READ + "=0", null, null);
            try {
                if (cursor != null) return cursor.getCount();
            } finally {
                if (cursor != null) cursor.close();
            }
            return 0;
        }
    }

    public static final class ProductInfo implements Serializable {

        private static final long serialVersionUID = -4578906561456458231L;

        public List<Map<String, Object>> values;

        public ProductInfo() {
            values = new ArrayList<Map<String, Object>>();
        }
    }

    public static final class ComparedGameInfo implements Serializable {

        private static final long serialVersionUID = -7864419735761064375L;

        public static final String UID = "Uid";

        public static final String TITLE = "GameTitle";

        public static final String LAST_PLAYED = "LastPlayed";

        public static final String MY_ACH_UNLOCKED = "MyAchUnlocked";

        public static final String YOUR_ACH_UNLOCKED = "YourAchUnlocked";

        public static final String MY_GP_EARNED = "MyGpEarned";

        public static final String YOUR_GP_EARNED = "YourGpEarned";

        public static final String ICON_URL = "GameIconUrl";

        public List<Map<String, Object>> games;

        public int myGamesPlayed;

        public int myAchievementCount;

        public int myGamerscore;

        public String myAvatarIconUrl;

        public int yourGamesPlayed;

        public int yourAchievementCount;

        public int yourGamerscore;

        public String yourAvatarIconUrl;

        public ComparedGameInfo() {
            games = new ArrayList<Map<String, Object>>();
            myGamerscore = myGamesPlayed = myAchievementCount = 0;
            yourGamerscore = yourGamesPlayed = yourAchievementCount = 0;
        }
    }

    public static final class ComparedAchievementInfo implements Serializable {

        private static final long serialVersionUID = 8053526431540231540L;

        public static final String UID = "Uid";

        public static final String TITLE = "Title";

        public static final String DESCRIPTION = "Description";

        public static final String MY_GP_EARNED = "MyGpEarned";

        public static final String YOUR_GP_EARNED = "YourGpEarned";

        public static final String MY_ACQUIRED = "MyAcquired";

        public static final String YOUR_ACQUIRED = "YourAcquired";

        public static final String MY_ICON_URL = "MyIconUrl";

        public static final String YOUR_ICON_URL = "YourIconUrl";

        public List<Map<String, Object>> achievements;

        public int myGamerscore;

        public int yourGamerscore;

        public int myAchievementCount;

        public int yourAchievementCount;

        public int totalGamerscore;

        public int totalAchievements;

        public ComparedAchievementInfo() {
            achievements = new ArrayList<Map<String, Object>>();
            myGamerscore = yourGamerscore = 0;
            myAchievementCount = yourAchievementCount = 0;
            totalGamerscore = totalAchievements = 0;
        }
    }

    public static final class GameInfo implements Serializable {

        private static final long serialVersionUID = -2962146474397804509L;

        public static final String _ID = "Id";

        public static final String UID = "Uid";

        public static final String TITLE = "Title";

        public static final String LAST_PLAYED = "LastPlayed";

        public static final String ACHIEVEMENTS = "Achievements";

        public static final String GAMERSCORE = "Gamerscore";

        public static final String ICON_URL = "IconUrl";

        public static Map<String, Object> create(Context context, long id, String title, long lastPlayed, int achUnlocked, int achTotal, int gsAcquired, int gsTotal, String iconUrl) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put(_ID, id);
            info.put(TITLE, title);
            info.put(ICON_URL, iconUrl);
            info.put(LAST_PLAYED, context.getString(R.string.last_played, DateFormat.getDateInstance().format(lastPlayed)));
            if (achTotal <= 0) {
                info.put(ACHIEVEMENTS, context.getString(R.string.game_achievements_none));
            } else {
                info.put(ACHIEVEMENTS, context.getString(R.string.game_achievements_unlocked_format, achUnlocked, achTotal));
            }
            info.put(GAMERSCORE, context.getString(R.string.x_of_xg, gsAcquired, gsTotal));
            return info;
        }
    }

    public static final class AchievementInfo implements Serializable {

        private static final long serialVersionUID = 3092581492042073191L;

        public static final String TITLE = "Title";

        public static final String DESCRIPTION = "Description";

        public static final String ACQUIRED = "Acquired";

        public static final String GAMERSCORE = "Gamerscore";

        public static final String ICON_URL = "IconUrl";

        public static final String LOCKED = "Locked";

        public static Map<String, Object> create(Context context, String title, String description, String iconUrl, int gamerscore, boolean locked, long acquired) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put(TITLE, title);
            info.put(DESCRIPTION, description);
            info.put(ICON_URL, iconUrl);
            info.put(GAMERSCORE, context.getString(R.string.xg, gamerscore));
            info.put(LOCKED, locked);
            if (locked) {
                info.put(ACQUIRED, context.getString(R.string.achievement_locked));
            } else {
                if (acquired == 0) info.put(ACQUIRED, context.getString(R.string.achievement_acquired_offline)); else info.put(ACQUIRED, context.getString(R.string.achievement_acquired_format, DateFormat.getDateInstance().format(acquired)));
            }
            return info;
        }
    }

    public static final class FriendInfo implements Serializable {

        private static final long serialVersionUID = -5400701235122414310L;

        public static final String GAMERTAG = "Gamertag";

        public static final String STATUS_TEXT = "StatusText";

        public static final String STATUS_CODE = "StatusCode";

        public static final String DESCRIPTION = "Description";

        public static final String GAMERSCORE = "Gamerscore";

        public static final String ICON_URL = "IconUrl";

        public static Map<String, Object> create(Context context, String gamertag, String statusText, int statusCode, String description, int gamerscore, String iconUrl) {
            Map<String, Object> info = new HashMap<String, Object>();
            info.put(GAMERTAG, gamertag);
            info.put(GAMERSCORE, context.getString(R.string.xg, gamerscore));
            info.put(STATUS_CODE, statusCode);
            info.put(STATUS_TEXT, statusText);
            info.put(DESCRIPTION, description);
            info.put(ICON_URL, iconUrl);
            return info;
        }
    }
}
