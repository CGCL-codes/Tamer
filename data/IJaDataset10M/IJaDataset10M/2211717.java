package com.mobolus.beans;

import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;
import com.shared.beans.*;
import com.mobolus.servlets.*;

public class BnMobolus {

    public static final int US_PENDING = 0;

    public static final int US_COMPLETE = 1;

    public static final int L_ALL_STATUS = -1;

    public static final int L_DELETED = 0;

    public static final int L_ACTIVE = 1;

    public static final int L_PENDING = 2;

    public static final int L_REJECTED = 3;

    public static final int L_PENDING_GROUP = 4;

    public static final int A_NOT_USER_SPECIFIC = -1;

    public static final int A_ADMIN = 0;

    public static final int A_USER_LEVEL_1 = 1;

    public static final int A_USER_LEVEL_2 = 2;

    public static final int A_USER_LEVEL_3 = 3;

    public static final String CREATE_ACCOUNT_WITHOUT_INVITE = "CREATE_ACCOUNT_WITHOUT_INVITE";

    public static final String CAN_INVITE_OTHERS = "CAN_INVITE_OTHERS";

    public static final String TOTAL_MEMORY_LIMIT = "TOTAL_MEMORY_LIMIT";

    public static final int MDL_INVALID = 0;

    public static final int MDL_CONVERSATIONS = 1;

    public static final int MDL_PHOTOS = 2;

    public static final int MDL_PHOTO_ALBUMS = 3;

    public static final int MDL_VIDEOS = 4;

    public static final int SI_SINGLE_INVITE = 0;

    public static final int SI_GROUP_INVITE = 1;

    public static final int SI_CHANGE_EMAIL = 2;

    public static final int KB = 1024;

    public static final int MB = KB * 1024;

    public static final long UNLIMITED_STORAGE = 0;

    public static final int NUM_LISTING_PAGES_TO_SHOW = 10;

    public static final String LISTING_BEG_LIST = "LISTING_BEG_LIST";

    public static final String LISTING_BEG_PAGE = "LISTING_BEG_PAGE";

    public static final String LISTING_END_LIST = "LISTING_END_LIST";

    public static final String LISTING_END_PAGE = "LISTING_END_PAGE";

    public static final String LISTING_NUM_PAGES = "LISTING_NUM_PAGES";

    public static final String LISTING_CURR_PAGE = "LISTING_CURR_PAGE";

    public static final int HOME_PAGE_MESSAGE_LENGTH = 55;

    public static final int HOME_PAGE_MAX_MODULE_DISPLAY = 3;

    public static final int MAX_GROUP_INVITEES = 30;

    public static final HashMap WEEK_DAYS = new HashMap();

    static {
        WEEK_DAYS.put("Sunday", 0);
        WEEK_DAYS.put("Monday", 1);
        WEEK_DAYS.put("Tuesday", 2);
        WEEK_DAYS.put("Wednesday", 3);
        WEEK_DAYS.put("Thursday", 4);
        WEEK_DAYS.put("Friday", 5);
        WEEK_DAYS.put("Saturday", 6);
    }

    public static HashMap<String, String> MIME_TYPES = null;

    public BnMobolus() {
    }

    public static ArrayList getGroupFilters(String userId) {
        String SQL = "SELECT * FROM groups WHERE g_owner = " + userId + " ORDER BY g_name";
        ArrayList result = Database.getResults(SQL, null);
        return result;
    }

    public static HashMap getSecretInfo(int type, String secretId, Connection conn) {
        boolean closeConn = false;
        if (null == conn) {
            conn = Database.getConnection();
            closeConn = true;
        }
        HashMap dataMap = new HashMap();
        String SQL = "SELECT * FROM secret_info WHERE si_secret_info_id = " + Util.escapeForHTML(secretId) + " AND si_type = " + type;
        ArrayList siResults = Database.getResults(SQL, conn);
        if (siResults.size() == 0) {
            return null;
        } else {
            dataMap = (HashMap) siResults.get(0);
        }
        System.err.println("added  id: " + secretId + " secretInfo: " + dataMap);
        if (closeConn) {
            Database.closeConnection(conn);
        }
        return dataMap;
    }

    public static String setSecretInfo(int type, ArrayList dataList, Connection conn) {
        boolean closeConn = false;
        if (null == conn) {
            conn = Database.getConnection();
            closeConn = true;
        }
        String secretId = null;
        boolean secretAdded = false;
        int tryCount = 0;
        while (!secretAdded && tryCount < 5) {
            Random rand = new Random(Calendar.getInstance().getTimeInMillis());
            secretId = rand.nextInt(100) + "" + (Calendar.getInstance().getTimeInMillis() / 13);
            try {
                HashMap<String, Object> secretValues = new HashMap<String, Object>();
                secretValues.put("si_secret_info_id", "'" + secretId + "'");
                secretValues.put("si_type", String.valueOf(type));
                for (int i = 0; i < dataList.size(); i++) {
                    secretValues.put("si_data" + (i + 1), "'" + dataList.get(i) + "'");
                }
                Database.insert("secret_info", secretValues, conn);
                secretAdded = true;
            } catch (Exception e) {
                tryCount++;
                e.printStackTrace();
            }
        }
        if (closeConn) {
            Database.closeConnection(conn);
        }
        return secretId;
    }

    public static HashMap<String, Object> getUser(String userId) {
        int resultSize = -1;
        HashMap<String, Object> userMap = null;
        try {
            ArrayList<HashMap<String, Object>> userResults = Database.getResults("SELECT * FROM users WHERE u_user_id = '" + Util.escapeForHTML(userId) + "'", null);
            resultSize = userResults.size();
            if (resultSize != 1) {
                throw new Exception("Found " + resultSize + " users with userId: " + userId);
            } else {
                userMap = userResults.get(0);
            }
        } catch (Exception e) {
            Log.msg("BnMobolus.getUser()", e);
        }
        return userMap;
    }

    public static String getPalName(String userId, String palId) {
        HashMap linkRow = Database.getSingleResult("SELECT * FROM links WHERE l_user_id = " + userId + " AND l_pal_id = " + palId, null);
        String palName = linkRow.get("l_pal_name").toString();
        return palName;
    }

    public static ArrayList getLinks(String userId, int status, boolean includeSelf, boolean joinInUsersTable, String orderBy) {
        String SQL = "SELECT * FROM links";
        if (joinInUsersTable) {
            SQL += ", users";
        }
        SQL += " WHERE ";
        if (joinInUsersTable) {
            SQL += "u_user_id = l_pal_id AND ";
        }
        SQL += "l_user_id = " + userId;
        if (status != L_ALL_STATUS) {
            SQL += " AND l_status = " + status;
        }
        if (!includeSelf) {
            SQL += " AND l_pal_id != " + userId;
        }
        if (null != orderBy) {
            SQL += " ORDER BY " + orderBy;
        }
        ArrayList links = Database.getResults(SQL, null);
        return links;
    }

    public static String[] getCurrentPalIds(String userId, boolean getOnlyActivePals) {
        int status = L_ALL_STATUS;
        if (getOnlyActivePals) {
            status = L_ACTIVE;
        }
        ArrayList linkResults = getLinks(userId, status, false, false, null);
        String[] palIds = new String[linkResults.size()];
        for (int i = 0; i < palIds.length; i++) {
            palIds[i] = String.valueOf(((HashMap) linkResults.get(i)).get("l_pal_id"));
        }
        return palIds;
    }

    public static String getTimeAgo(String time, long userOffset) {
        String timeAgoStr = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            java.util.Date date = sdf.parse(time);
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            timeAgoStr = getTimeAgo(timestamp, userOffset);
        } catch (Exception e) {
            Log.msg("BnMobolus.getTimeAgo()", e);
        }
        return timeAgoStr;
    }

    public static String getTimeAgo(Timestamp time, long userOffset) {
        long SECOND = 1000;
        long MINUTE = 60 * SECOND;
        long HOUR = 60 * MINUTE;
        long DAY = 24 * HOUR;
        long WEEK = 7 * DAY;
        long timeMilliseconds = time.getTime() + userOffset;
        long currTimeMilliseconds = System.currentTimeMillis() + userOffset;
        long timeDiff = currTimeMilliseconds - timeMilliseconds;
        SimpleDateFormat formattedTime = new SimpleDateFormat("ss:mm:h:d:M:yyyy:aaa:EEEE:MMMM");
        String[] dateInfo = formattedTime.format(timeMilliseconds).split(":");
        String[] currDateInfo = formattedTime.format(currTimeMilliseconds).split(":");
        int dateDayIdx = (Integer) WEEK_DAYS.get(dateInfo[7]);
        int currDateDayIdx = (Integer) WEEK_DAYS.get(currDateInfo[7]);
        if (timeDiff <= (2 * SECOND)) {
            return "a moment ago";
        } else if (timeDiff <= MINUTE) {
            return (timeDiff / SECOND) + " seconds ago";
        } else if (timeDiff <= (HOUR - MINUTE)) {
            long numMinutes = timeDiff / MINUTE;
            return (numMinutes) + " minute" + ((numMinutes > 1) ? "s" : "") + " ago";
        } else if (timeDiff < DAY && dateDayIdx == currDateDayIdx) {
            return "at " + dateInfo[2] + ":" + dateInfo[1] + " " + dateInfo[6].toLowerCase();
        } else if (timeDiff < WEEK && (currDateDayIdx - dateDayIdx == 1 || (currDateDayIdx == 0 && dateDayIdx == 6))) {
            return "yesterday at " + dateInfo[2] + ":" + dateInfo[1] + " " + dateInfo[6].toLowerCase();
        } else if (timeDiff <= 3 * DAY) {
            return "on " + dateInfo[7] + " at " + dateInfo[2] + ":" + dateInfo[1] + " " + dateInfo[6].toLowerCase();
        } else {
            return "on " + dateInfo[8] + " " + dateInfo[3] + ", " + dateInfo[5] + " at " + dateInfo[2] + ":" + dateInfo[1] + " " + dateInfo[6].toLowerCase();
        }
    }

    public static long getUserTimeOffset(long userId) {
        String SQL = "SELECT tz_java_id FROM users,time_zones WHERE u_user_id=" + userId + " AND u_time_zone=tz_id";
        ArrayList results = Database.getResults(SQL, null);
        HashMap timeZoneMap = (HashMap) results.get(0);
        String timeZoneId = (String) timeZoneMap.get("tz_java_id");
        long currTime = System.currentTimeMillis();
        long serverOffset = TimeZone.getDefault().getOffset(currTime);
        long userOffset = TimeZone.getTimeZone(timeZoneId).getOffset(currTime);
        return userOffset - serverOffset;
    }

    public static boolean updateLinkStatus(String userId, String palId, int status, Connection conn) {
        HashMap statusMap = new HashMap();
        statusMap.put("l_status", status);
        long updatedRows = Database.set("links", statusMap, "WHERE l_status = " + BnMobolus.L_PENDING + " AND ((l_user_id = " + Util.escapeForHTML(userId) + " AND l_pal_id = " + Util.escapeForHTML(palId) + ") OR (l_user_id = " + Util.escapeForHTML(palId) + " AND l_pal_id = " + Util.escapeForHTML(userId) + "))", conn);
        if (BnMobolus.L_DELETED == status) {
            HashMap palUser = BnMobolus.getUser(palId);
            String userStatusStr = palUser.get("u_user_status").toString();
            long userStatus = Long.parseLong(userStatusStr);
            if (BnMobolus.US_PENDING == userStatus) {
                Database.delete("users", "WHERE u_user_id = " + Util.escapeForHTML(palId), conn);
            }
        }
        if (2 != updatedRows) {
            Log.msg("BnMobolus.updateLinkStatus()", "pal_names deleted: " + updatedRows + " pal_id: " + palId + " userId: " + userId + " status: " + status + " something is wrong!");
            return false;
        } else {
            return true;
        }
    }

    public static ArrayList getInviteeGroupFromSecretId(String secretId, Connection conn) {
        ArrayList inviteeList = new ArrayList();
        HashMap dataMap = BnMobolus.getSecretInfo(BnMobolus.SI_GROUP_INVITE, secretId, conn);
        if (null != dataMap) {
            String inviterId = (String) dataMap.get("si_data1");
            String inviteeId = (String) dataMap.get("si_data2");
            String inviteeIdList = inviterId + "," + (String) dataMap.get("si_data3");
            String SQL = "SELECT * FROM users WHERE u_user_id IN (" + inviteeIdList + ") AND u_user_id != " + inviteeId + " AND u_user_id NOT IN" + " (SELECT l_pal_id FROM links WHERE l_user_id = " + inviteeId + " AND l_status = " + BnMobolus.L_ACTIVE + ")";
            inviteeList = Database.getResults(SQL, conn);
        }
        return inviteeList;
    }

    /**
	 * Will insert the two links if there are none already found. 
	 * If they are there already it only updates the status
	 * @param inviterId
	 * @param inviteeId
	 * @param inviterStatus
	 * @param inviteeStatus
	 * @return Whether or not the link setup was successful
	 */
    public static boolean setupLink(String inviterId, String inviteeId, int inviterStatus, int inviteeStatus) {
        String SQL = "SELECT * FROM links WHERE l_user_id = " + inviterId + " AND l_pal_id = " + inviteeId;
        ArrayList inviterLinkResult = Database.getResults(SQL, null);
        SQL = "SELECT * FROM links WHERE l_user_id = " + inviteeId + " AND l_pal_id = " + inviterId;
        ArrayList inviteeLinkResult = Database.getResults(SQL, null);
        Log.msg("SETUP_LINK", "In setup link inviterLinkResult: " + inviterLinkResult.size() + " inviteeLinkResult: " + inviteeLinkResult.size());
        boolean success = true;
        if (inviterLinkResult.size() == 0) {
            HashMap<String, Object> inviteeRow = BnMobolus.getUser(inviteeId);
            HashMap<String, Object> linkValues = new HashMap<String, Object>();
            linkValues.put("l_user_id", inviterId);
            linkValues.put("l_pal_id", inviteeId);
            linkValues.put("l_inviter_id", inviterId);
            if (null == inviteeRow.get("u_first_name") || null == inviteeRow.get("u_first_name")) {
                linkValues.put("l_pal_name", "'Unknown'");
            } else {
                linkValues.put("l_pal_name", "'" + inviteeRow.get("u_first_name") + " " + inviteeRow.get("u_last_name") + "'");
            }
            linkValues.put("l_creation_date", "NOW()");
            linkValues.put("l_last_updated", "NOW()");
            linkValues.put("l_status", String.valueOf(inviterStatus));
            linkValues.put("l_flags", "0");
            Database.insert("links", linkValues, null);
        } else {
            HashMap<String, Object> linkValues = new HashMap<String, Object>();
            linkValues.put("l_status", String.valueOf(inviterStatus));
            Database.set("links", linkValues, "WHERE l_user_id = " + inviterId + " AND l_pal_id = " + inviteeId, null);
        }
        if (inviteeLinkResult.size() == 0) {
            HashMap<String, Object> inviterRow = BnMobolus.getUser(inviterId);
            HashMap<String, Object> linkValues = new HashMap<String, Object>();
            linkValues.put("l_user_id", inviteeId);
            linkValues.put("l_pal_id", inviterId);
            linkValues.put("l_inviter_id", inviterId);
            linkValues.put("l_pal_name", "'" + inviterRow.get("u_first_name") + " " + inviterRow.get("u_last_name") + "'");
            linkValues.put("l_creation_date", "NOW()");
            linkValues.put("l_last_updated", "NOW()");
            linkValues.put("l_status", String.valueOf(inviteeStatus));
            linkValues.put("l_flags", "0");
            Database.insert("links", linkValues, null);
        } else {
            HashMap linkValues = new HashMap();
            linkValues.put("l_status", String.valueOf(inviteeStatus));
            Database.set("links", linkValues, "WHERE l_user_id = " + inviteeId + " AND l_pal_id = " + inviterId, null);
        }
        return success;
    }

    public static boolean hasRightsTo(String settingName, HashMap user) {
        String value = getRightValue(settingName, user);
        return "true".equals(value);
    }

    public static String getRightValue(String settingName, HashMap user) {
        try {
            HashMap settingMap = Database.getSingleResult("SELECT * FROM settings WHERE s_name = '" + settingName + "' AND s_account_id = " + BnMobolus.A_NOT_USER_SPECIFIC, null);
            if (null != settingMap) {
                return (String) settingMap.get("s_value");
            } else if (null == user) {
                Log.trace("BnMobolus.hasRightsTo() rightName: " + settingName, "user passed in was null AND this is a user specific right...pass in the user!");
            } else {
                String accountType = user.get("u_account_id").toString();
                ArrayList rights = Database.getResults("SELECT * FROM settings WHERE s_name = '" + settingName + "' AND " + accountType + " <= s_account_id order by s_account_id asc", null);
                if (rights.size() > 0) {
                    settingMap = (HashMap) rights.get(0);
                    if (null != settingMap) {
                        return (String) settingMap.get("s_value");
                    }
                }
            }
        } catch (Exception e) {
            Log.msg("BnMobolus.getRightValue()", e);
        }
        return null;
    }

    public static ArrayList getOnlinePals(HashMap userRow) {
        String userId = userRow.get("u_user_id").toString();
        ArrayList pals = Database.getResults("SELECT l_pal_name FROM links WHERE l_user_id = " + userId + " AND l_user_id != l_pal_id AND l_pal_id IN (SELECT u_user_id FROM users WHERE u_logged_in = 1)", null);
        if (null == pals) {
            pals = new ArrayList();
        }
        return pals;
    }

    public static void updateMembership(String selectedIdsStr, int moduleId, long referenceId, String userId, Connection conn) {
        String selectedIdsStrTmp = "," + selectedIdsStr + "," + userId + ",";
        String idsToDelete = "";
        String[] idsToInsert = null;
        String message = "";
        if (BnMobolus.MDL_PHOTO_ALBUMS == moduleId) {
            String SQL = "SELECT * FROM photo_album_links WHERE pal_photo_album_id = " + referenceId;
            ArrayList limitedTo = Database.getResults(SQL, conn);
            for (int i = 0; i < limitedTo.size(); i++) {
                HashMap currLimitedTo = (HashMap) limitedTo.get(i);
                String currReceiver = String.valueOf(currLimitedTo.get("pal_receiver"));
                if (selectedIdsStrTmp.contains("," + currReceiver + ",")) {
                    selectedIdsStrTmp = selectedIdsStrTmp.replace("," + currReceiver + ",", ",");
                } else {
                    if (idsToDelete.length() > 0) {
                        idsToDelete += ",";
                    }
                    idsToDelete += currReceiver;
                }
            }
            if (selectedIdsStrTmp.length() > 2) {
                selectedIdsStrTmp = selectedIdsStrTmp.substring(1, selectedIdsStrTmp.lastIndexOf(","));
            } else {
                selectedIdsStrTmp = "";
            }
            HashMap photoAlbum = Database.getSingleResult("SELECT pa_title FROM photo_albums WHERE pa_photo_album_id = " + referenceId, conn);
            message = "You were removed from the photo album <b>" + photoAlbum.get("pa_title") + "</b>";
            String messagesInPhotoAlbum = "";
            String rootMessageIds = "";
            String photoIds = BnPhotoAlbum.getPhotoIdsInAlbum(String.valueOf(referenceId));
            String[] photoIdArray = photoIds.split(",");
            for (int i = 0; i < photoIdArray.length; i++) {
                long rootMessageId = BnPhoto.getPhotoRootMessage(photoIdArray[i]);
                if (rootMessageIds.length() > 0) {
                    rootMessageIds += ",";
                }
                rootMessageIds += rootMessageId;
                if (messagesInPhotoAlbum.length() > 0) {
                    messagesInPhotoAlbum += ",";
                }
                messagesInPhotoAlbum += BnMessage.getListOfChildren(String.valueOf(rootMessageId), userId, photoIdArray[i], BnMobolus.MDL_PHOTOS);
            }
            if (!"".equals(idsToDelete)) {
                Database.delete("photo_album_links", "WHERE pal_receiver IN (" + idsToDelete + ")", conn);
                Database.delete("photo_links", "WHERE pl_receiver IN (" + idsToDelete + ")", conn);
                Database.delete("message_links", "WHERE ml_receiver IN (" + idsToDelete + ") AND ml_message_id IN (" + messagesInPhotoAlbum + ")", conn);
                Database.delete("new_module_items", "WHERE nmi_receiver IN (" + idsToDelete + ") AND nmi_reference_id = " + referenceId + " AND nmi_module_id = " + BnMobolus.MDL_PHOTO_ALBUMS, conn);
                Database.delete("new_module_items", "WHERE nmi_receiver IN (" + idsToDelete + ") AND nmi_reference_id IN (" + photoIds + ") AND nmi_module_id = " + BnMobolus.MDL_PHOTOS, conn);
                Database.delete("new_messages", "WHERE nm_receiver IN (" + idsToDelete + ") AND nm_message_id IN (" + messagesInPhotoAlbum + ")", conn);
            }
            idsToInsert = selectedIdsStrTmp.split(",");
            for (int i = 0; i < idsToInsert.length; i++) {
                if (!"".equals(idsToInsert[i])) {
                    HashMap<String, Object> values = new HashMap<String, Object>();
                    values.put("pal_photo_album_id", referenceId);
                    values.put("pal_receiver", idsToInsert[i]);
                    Database.insert("photo_album_links", values, conn);
                    if (!userId.equals(idsToInsert[i])) {
                        HashMap<String, Object> nmiValues = new HashMap<String, Object>();
                        nmiValues.put("nmi_module_id", BnMobolus.MDL_PHOTO_ALBUMS);
                        nmiValues.put("nmi_reference_id", referenceId);
                        nmiValues.put("nmi_receiver", idsToInsert[i]);
                        try {
                            Database.insert("new_module_items", nmiValues, conn);
                        } catch (Exception e) {
                            Log.msg("BnMobolus.updateMembership()", "Already found new module entry for: " + nmiValues);
                        }
                    }
                    for (int j = 0; j < photoIdArray.length; j++) {
                        HashMap<String, Object> phValues = new HashMap<String, Object>();
                        phValues.put("pl_photo_id", photoIdArray[j]);
                        phValues.put("pl_receiver", idsToInsert[i]);
                        Database.insert("photo_links", phValues, conn);
                        if (!userId.equals(idsToInsert[i])) {
                            HashMap<String, Object> nmiValues = new HashMap<String, Object>();
                            nmiValues.put("nmi_module_id", BnMobolus.MDL_PHOTOS);
                            nmiValues.put("nmi_reference_id", photoIdArray[j]);
                            nmiValues.put("nmi_receiver", idsToInsert[i]);
                            try {
                                Database.insert("new_module_items", nmiValues, conn);
                            } catch (Exception e) {
                                Log.msg("BnMobolus.updateMembership()", "Already found new module entry for: " + nmiValues);
                            }
                        }
                    }
                    String[] rootMessageIdsArray = rootMessageIds.split(",");
                    for (int j = 0; j < rootMessageIdsArray.length; j++) {
                        HashMap<String, Object> msgValues = new HashMap<String, Object>();
                        msgValues.put("ml_message_id", rootMessageIdsArray[j]);
                        msgValues.put("ml_receiver", idsToInsert[i]);
                        Database.insert("message_links", msgValues, conn);
                    }
                }
            }
            HashMap values = new HashMap();
            values.put("pa_public", false);
            Database.set("photo_albums", values, "WHERE pa_photo_album_id = " + referenceId, conn);
            HashMap messageValues = new HashMap();
            messageValues.put("m_public", false);
            Database.set("messages", messageValues, "WHERE m_message_id IN (" + messagesInPhotoAlbum + ")", conn);
        } else if (BnMobolus.MDL_CONVERSATIONS == moduleId) {
            String SQL = "SELECT * FROM conversation_links WHERE cl_conversation_id = " + referenceId;
            ArrayList limitedTo = Database.getResults(SQL, conn);
            for (int i = 0; i < limitedTo.size(); i++) {
                HashMap currLimitedTo = (HashMap) limitedTo.get(i);
                String currReceiver = String.valueOf(currLimitedTo.get("cl_receiver"));
                if (selectedIdsStrTmp.contains("," + currReceiver + ",")) {
                    selectedIdsStrTmp = selectedIdsStrTmp.replace("," + currReceiver + ",", ",");
                } else {
                    if (idsToDelete.length() > 0) {
                        idsToDelete += ",";
                    }
                    idsToDelete += currReceiver;
                }
            }
            if (selectedIdsStrTmp.length() > 2) {
                selectedIdsStrTmp = selectedIdsStrTmp.substring(1, selectedIdsStrTmp.lastIndexOf(","));
            } else {
                selectedIdsStrTmp = "";
            }
            HashMap<String, Object> conversation = Database.getSingleResult("SELECT c_subject FROM conversations WHERE c_conversation_id = " + referenceId, conn);
            message = "You were removed from the conversation <b>" + conversation.get("c_subject") + "</b>";
            HashMap<String, Object> rootMessage = BnMessage.getRootMessage(String.valueOf(referenceId), BnMobolus.MDL_CONVERSATIONS, conn);
            String rootMessageId = String.valueOf(rootMessage.get("m_message_id"));
            String messagesInConversation = BnMessage.getListOfChildren(rootMessageId, userId, String.valueOf(referenceId), moduleId);
            if (!"".equals(idsToDelete)) {
                Database.delete("conversation_links", "WHERE cl_receiver IN (" + idsToDelete + ")", conn);
                Database.delete("message_links", "WHERE ml_receiver IN (" + idsToDelete + ") AND ml_message_id IN (" + messagesInConversation + ")", conn);
                Database.delete("new_module_items", "WHERE nmi_receiver IN (" + idsToDelete + ") AND nmi_reference_id = " + referenceId + " AND nmi_module_id = " + moduleId, conn);
                Database.delete("new_messages", "WHERE nm_receiver IN (" + idsToDelete + ") AND nm_message_id IN (" + messagesInConversation + ")", conn);
            }
            idsToInsert = selectedIdsStrTmp.split(",");
            for (int i = 0; i < idsToInsert.length; i++) {
                if (!"".equals(idsToInsert[i])) {
                    HashMap<String, Object> values = new HashMap<String, Object>();
                    values.put("cl_conversation_id", referenceId);
                    values.put("cl_receiver", idsToInsert[i]);
                    Database.insert("conversation_links", values, conn);
                    if (!userId.equals(idsToInsert[i])) {
                        HashMap<String, Object> nmiValues = new HashMap<String, Object>();
                        nmiValues.put("nmi_module_id", BnMobolus.MDL_CONVERSATIONS);
                        nmiValues.put("nmi_reference_id", referenceId);
                        nmiValues.put("nmi_receiver", idsToInsert[i]);
                        try {
                            Database.insert("new_module_items", nmiValues, conn);
                        } catch (Exception e) {
                            Log.msg("BnMobolus.updateMembership()", "Already found new module entry for: " + nmiValues);
                        }
                    }
                    HashMap<String, Object> msgValues = new HashMap<String, Object>();
                    msgValues.put("ml_message_id", rootMessageId);
                    msgValues.put("ml_receiver", idsToInsert[i]);
                    Database.insert("message_links", msgValues, conn);
                }
            }
            HashMap values = new HashMap();
            values.put("c_public", false);
            Database.set("conversations", values, "WHERE c_conversation_id = " + referenceId, conn);
            HashMap messageValues = new HashMap();
            messageValues.put("m_public", false);
            Database.set("messages", messageValues, "WHERE m_module_id = " + BnMobolus.MDL_CONVERSATIONS + " AND m_reference_id = " + referenceId, conn);
        } else if (BnMobolus.MDL_VIDEOS == moduleId) {
            String SQL = "SELECT * FROM video_links WHERE vl_video_id = " + referenceId;
            ArrayList limitedTo = Database.getResults(SQL, conn);
            for (int i = 0; i < limitedTo.size(); i++) {
                HashMap currLimitedTo = (HashMap) limitedTo.get(i);
                String currReceiver = String.valueOf(currLimitedTo.get("vl_receiver"));
                if (selectedIdsStrTmp.contains("," + currReceiver + ",")) {
                    selectedIdsStrTmp = selectedIdsStrTmp.replace("," + currReceiver + ",", ",");
                } else {
                    if (idsToDelete.length() > 0) {
                        idsToDelete += ",";
                    }
                    idsToDelete += currReceiver;
                }
            }
            if (selectedIdsStrTmp.length() > 2) {
                selectedIdsStrTmp = selectedIdsStrTmp.substring(1, selectedIdsStrTmp.lastIndexOf(","));
            } else {
                selectedIdsStrTmp = "";
            }
            HashMap<String, Object> video = Database.getSingleResult("SELECT v_subject FROM video WHERE v_video_id = " + referenceId, conn);
            message = "You were removed from the video <b>" + video.get("v_title") + "</b>";
            HashMap<String, Object> rootMessage = BnMessage.getRootMessage(String.valueOf(referenceId), BnMobolus.MDL_VIDEOS, conn);
            String rootMessageId = String.valueOf(rootMessage.get("m_message_id"));
            String messagesInVideo = BnMessage.getListOfChildren(rootMessageId, userId, String.valueOf(referenceId), moduleId);
            if (!"".equals(idsToDelete)) {
                Database.delete("video_links", "WHERE vl_receiver IN (" + idsToDelete + ")", conn);
                Database.delete("message_links", "WHERE ml_receiver IN (" + idsToDelete + ") AND ml_message_id IN (" + messagesInVideo + ")", conn);
                Database.delete("new_module_items", "WHERE nmi_receiver IN (" + idsToDelete + ") AND nmi_reference_id = " + referenceId + " AND nmi_module_id = " + moduleId, conn);
                Database.delete("new_messages", "WHERE nm_receiver IN (" + idsToDelete + ") AND nm_message_id IN (" + messagesInVideo + ")", conn);
            }
            idsToInsert = selectedIdsStrTmp.split(",");
            for (int i = 0; i < idsToInsert.length; i++) {
                if (!"".equals(idsToInsert[i])) {
                    HashMap<String, Object> values = new HashMap<String, Object>();
                    values.put("vl_conversation_id", referenceId);
                    values.put("vl_receiver", idsToInsert[i]);
                    Database.insert("video_links", values, conn);
                    if (!userId.equals(idsToInsert[i])) {
                        HashMap<String, Object> nmiValues = new HashMap<String, Object>();
                        nmiValues.put("nmi_module_id", BnMobolus.MDL_VIDEOS);
                        nmiValues.put("nmi_reference_id", referenceId);
                        nmiValues.put("nmi_receiver", idsToInsert[i]);
                        try {
                            Database.insert("new_module_items", nmiValues, conn);
                        } catch (Exception e) {
                            Log.msg("BnMobolus.updateMembership()", "Already found new module entry for: " + nmiValues);
                        }
                    }
                    HashMap<String, Object> msgValues = new HashMap<String, Object>();
                    msgValues.put("ml_message_id", rootMessageId);
                    msgValues.put("ml_receiver", idsToInsert[i]);
                    Database.insert("message_links", msgValues, conn);
                }
            }
            HashMap values = new HashMap();
            values.put("v_public", false);
            Database.set("videos", values, "WHERE v_video_id = " + referenceId, conn);
            HashMap messageValues = new HashMap();
            messageValues.put("m_public", false);
            Database.set("messages", messageValues, "WHERE m_module_id = " + BnMobolus.MDL_VIDEOS + " AND m_reference_id = " + referenceId, conn);
        } else {
            Log.trace("BnMobolus.updateMembership()", "unknown module type: " + moduleId);
        }
        if (!"".equals(idsToDelete)) {
            String[] removeListArray = idsToDelete.split(",");
            for (int removeIdx = 0; removeIdx < removeListArray.length; removeIdx++) {
                addAlert(removeListArray[removeIdx], message, conn);
            }
        }
    }

    public static ArrayList getAlerts(String userId) {
        String sql = "SELECT * FROM alerts WHERE al_user_id = " + userId + " ORDER BY al_creation_date DESC";
        ArrayList alerts = Database.getResults(sql, null);
        return alerts;
    }

    public static void addAlert(String userId, String message, Connection conn) {
        HashMap<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put("al_user_id", userId);
        messageMap.put("al_message", "'" + message + "'");
        messageMap.put("al_creation_date", "NOW()");
        Database.insert("alerts", messageMap, conn);
    }

    /**
	 * 
	 * @param userId The person you retrieving for
	 * @param groupId albums only submited by a certain group (i.e. EVERYONE_ID or a custom group or just pass in null if you want don't want to limit the search by groupId
	 * @param onlyNew Whether or not to skip the albums that have already been viewed
	 * @param onlyWatched Whether or not to skip the albums that are not being watched
	 * @param limit If you can ever specify this you should. If you want all of the results pass in 0 for the limit
	 * @param orderBy the order by string. It should include the actual "ORDER BY ..." in this string
	 * @return A list of Hashmaps of rows with all the colums found in photo_albums, photo_album_links, and links
	 */
    public static HashMap<String, Object> getNewModuleIdMap(int moduleId, String userId) {
        ArrayList<HashMap<String, Object>> refIdList = Database.getResults("SELECT nmi_reference_id FROM new_module_items WHERE nmi_module_id = " + moduleId + " AND nmi_receiver = " + userId, null);
        HashMap<String, Object> refIdMap = new HashMap<String, Object>();
        for (HashMap<String, Object> refId : refIdList) {
            String currId = refId.get("nmi_reference_id").toString();
            refIdMap.put(currId, "true");
        }
        return refIdMap;
    }

    public static HashMap<String, Integer> getListingVars(int currPage, int numList, int totalList) {
        HashMap<String, Integer> listingVars = new HashMap<String, Integer>();
        int begList = -1;
        int endList = -1;
        int numPages = -1;
        int begPage = -1;
        int endPage = -1;
        if (numList < 0) {
            begList = 1;
            endList = totalList;
            numPages = 1;
            begPage = 1;
            endPage = 1;
        } else {
            numPages = totalList / numList;
            if (totalList % numList != 0) {
                numPages++;
            }
            if (currPage > numPages) {
                currPage = numPages;
            }
            begList = ((currPage - 1) * numList) + 1;
            endList = begList + numList - 1;
            if (endList > totalList) {
                endList = totalList;
            }
            if (begList > endList) {
                begList = endList;
            }
            begPage = 1;
            if (currPage > (NUM_LISTING_PAGES_TO_SHOW / 2) + 1) {
                begPage = currPage - (NUM_LISTING_PAGES_TO_SHOW / 2);
            }
            endPage = begPage + (NUM_LISTING_PAGES_TO_SHOW - 1);
            if (endPage > numPages) {
                endPage = numPages;
                begPage = endPage - ((NUM_LISTING_PAGES_TO_SHOW - 1));
                if (begPage <= 0) {
                    begPage = 1;
                }
            }
        }
        listingVars.put(LISTING_BEG_LIST, begList);
        listingVars.put(LISTING_END_LIST, endList);
        listingVars.put(LISTING_BEG_PAGE, begPage);
        listingVars.put(LISTING_END_PAGE, endPage);
        listingVars.put(LISTING_NUM_PAGES, numPages);
        listingVars.put(LISTING_CURR_PAGE, currPage);
        return listingVars;
    }

    public static boolean updateAccount(String userId, String account) {
        boolean success = false;
        if (null != userId && null != account) {
            try {
                Long.parseLong(userId);
                long accountId = Long.parseLong(account);
                if (accountId > 0 && accountId <= 3) {
                    HashMap values = new HashMap();
                    values.put("u_account_id", account);
                }
            } catch (Exception e) {
                Log.msg("Unable to update account: " + account + " userId: " + userId, e);
            }
        }
        return success;
    }

    public static long getMemoryLimit(String userId) {
        long memLimit = 1;
        try {
            memLimit = Long.parseLong(getRightValue(TOTAL_MEMORY_LIMIT, BnMobolus.getUser(userId)));
        } catch (Exception e) {
            Log.msg("BnMobolus.getMemoryLimit() memLimit: " + getRightValue(TOTAL_MEMORY_LIMIT, getUser(userId)), e);
        }
        return memLimit;
    }

    public static double getMemoryUsed(String userId) {
        return BnPhoto.getTotalSizeAllPhotos(userId) + BnVideo.getTotalSizeAllVideos(userId);
    }

    public static String getMimeType(String ext) {
        if (null == MIME_TYPES) {
            MIME_TYPES = new HashMap<String, String>();
            MIME_TYPES.put("323", "text/h323");
            MIME_TYPES.put("acx", "application/internet-property-stream");
            MIME_TYPES.put("ai", "application/postscript");
            MIME_TYPES.put("aif", "audio/x-aiff");
            MIME_TYPES.put("aifc", "audio/x-aiff");
            MIME_TYPES.put("aiff", "audio/x-aiff");
            MIME_TYPES.put("asf", "video/x-ms-asf");
            MIME_TYPES.put("asr", "video/x-ms-asf");
            MIME_TYPES.put("asx", "video/x-ms-asf");
            MIME_TYPES.put("au", "audio/basic");
            MIME_TYPES.put("avi", "video/x-msvideo");
            MIME_TYPES.put("axs", "application/olescript");
            MIME_TYPES.put("bas", "text/plain");
            MIME_TYPES.put("bcpio", "application/x-bcpio");
            MIME_TYPES.put("bin", "application/octet-stream");
            MIME_TYPES.put("bmp", "image/bmp");
            MIME_TYPES.put("c", "text/plain");
            MIME_TYPES.put("cat", "application/vnd.ms-pkiseccat");
            MIME_TYPES.put("cdf", "application/x-cdf");
            MIME_TYPES.put("cer", "application/x-x509-ca-cert");
            MIME_TYPES.put("class", "application/octet-stream");
            MIME_TYPES.put("clp", "application/x-msclip");
            MIME_TYPES.put("cmx", "image/x-cmx");
            MIME_TYPES.put("cod", "image/cis-cod");
            MIME_TYPES.put("cpio", "application/x-cpio");
            MIME_TYPES.put("crd", "application/x-mscardfile");
            MIME_TYPES.put("crl", "application/pkix-crl");
            MIME_TYPES.put("crt", "application/x-x509-ca-cert");
            MIME_TYPES.put("csh", "application/x-csh");
            MIME_TYPES.put("css", "text/css");
            MIME_TYPES.put("dcr", "application/x-director");
            MIME_TYPES.put("der", "application/x-x509-ca-cert");
            MIME_TYPES.put("dir", "application/x-director");
            MIME_TYPES.put("dll", "application/x-msdownload");
            MIME_TYPES.put("dms", "application/octet-stream");
            MIME_TYPES.put("doc", "application/msword");
            MIME_TYPES.put("dot", "application/msword");
            MIME_TYPES.put("dvi", "application/x-dvi");
            MIME_TYPES.put("dxr", "application/x-director");
            MIME_TYPES.put("eps", "application/postscript");
            MIME_TYPES.put("etx", "text/x-setext");
            MIME_TYPES.put("evy", "application/envoy");
            MIME_TYPES.put("exe", "application/octet-stream");
            MIME_TYPES.put("fif", "application/fractals");
            MIME_TYPES.put("flr", "x-world/x-vrml");
            MIME_TYPES.put("gif", "image/gif");
            MIME_TYPES.put("gtar", "application/x-gtar");
            MIME_TYPES.put("gz", "application/x-gzip");
            MIME_TYPES.put("h", "text/plain");
            MIME_TYPES.put("hdf", "application/x-hdf");
            MIME_TYPES.put("hlp", "application/winhlp");
            MIME_TYPES.put("hqx", "application/mac-binhex40");
            MIME_TYPES.put("hta", "application/hta");
            MIME_TYPES.put("htc", "text/x-component");
            MIME_TYPES.put("htm", "text/html");
            MIME_TYPES.put("html", "text/html");
            MIME_TYPES.put("htt", "text/webviewhtml");
            MIME_TYPES.put("ico", "image/x-icon");
            MIME_TYPES.put("ief", "image/ief");
            MIME_TYPES.put("iii", "application/x-iphone");
            MIME_TYPES.put("ins", "application/x-internet-signup");
            MIME_TYPES.put("isp", "application/x-internet-signup");
            MIME_TYPES.put("jfif", "image/pipeg");
            MIME_TYPES.put("jpe", "image/jpeg");
            MIME_TYPES.put("jpeg", "image/jpeg");
            MIME_TYPES.put("jpg", "image/jpeg");
            MIME_TYPES.put("js", "application/x-javascript");
            MIME_TYPES.put("latex", "application/x-latex");
            MIME_TYPES.put("lha", "application/octet-stream");
            MIME_TYPES.put("lsf", "video/x-la-asf");
            MIME_TYPES.put("lsx", "video/x-la-asf");
            MIME_TYPES.put("lzh", "application/octet-stream");
            MIME_TYPES.put("m13", "application/x-msmediaview");
            MIME_TYPES.put("m14", "application/x-msmediaview");
            MIME_TYPES.put("m3u", "audio/x-mpegurl");
            MIME_TYPES.put("man", "application/x-troff-man");
            MIME_TYPES.put("mdb", "application/x-msaccess");
            MIME_TYPES.put("me", "application/x-troff-me");
            MIME_TYPES.put("mht", "message/rfc822");
            MIME_TYPES.put("mhtml", "message/rfc822");
            MIME_TYPES.put("mid", "audio/mid");
            MIME_TYPES.put("mny", "application/x-msmoney");
            MIME_TYPES.put("mov", "video/quicktime");
            MIME_TYPES.put("movie", "video/x-sgi-movie");
            MIME_TYPES.put("mp2", "video/mpeg");
            MIME_TYPES.put("mp3", "audio/mpeg");
            MIME_TYPES.put("mpa", "video/mpeg");
            MIME_TYPES.put("mpe", "video/mpeg");
            MIME_TYPES.put("mpeg", "video/mpeg");
            MIME_TYPES.put("mpg", "video/mpeg");
            MIME_TYPES.put("mpp", "application/vnd.ms-project");
            MIME_TYPES.put("mpv2", "video/mpeg");
            MIME_TYPES.put("ms", "application/x-troff-ms");
            MIME_TYPES.put("mvb", "application/x-msmediaview");
            MIME_TYPES.put("nws", "message/rfc822");
            MIME_TYPES.put("oda", "application/oda");
            MIME_TYPES.put("p10", "application/pkcs10");
            MIME_TYPES.put("p12", "application/x-pkcs12");
            MIME_TYPES.put("p7b", "application/x-pkcs7-certificates");
            MIME_TYPES.put("p7c", "application/x-pkcs7-mime");
            MIME_TYPES.put("p7m", "application/x-pkcs7-mime");
            MIME_TYPES.put("p7r", "application/x-pkcs7-certreqresp");
            MIME_TYPES.put("p7s", "application/x-pkcs7-signature");
            MIME_TYPES.put("pbm", "image/x-portable-bitmap");
            MIME_TYPES.put("pdf", "application/pdf");
            MIME_TYPES.put("pfx", "application/x-pkcs12");
            MIME_TYPES.put("pgm", "image/x-portable-graymap");
            MIME_TYPES.put("pko", "application/ynd.ms-pkipko");
            MIME_TYPES.put("pma", "application/x-perfmon");
            MIME_TYPES.put("pmc", "application/x-perfmon");
            MIME_TYPES.put("pml", "application/x-perfmon");
            MIME_TYPES.put("pmr", "application/x-perfmon");
            MIME_TYPES.put("pmw", "application/x-perfmon");
            MIME_TYPES.put("pnm", "image/x-portable-anymap");
            MIME_TYPES.put("pot,", "application/vnd.ms-powerpoint");
            MIME_TYPES.put("ppm", "image/x-portable-pixmap");
            MIME_TYPES.put("pps", "application/vnd.ms-powerpoint");
            MIME_TYPES.put("ppt", "application/vnd.ms-powerpoint");
            MIME_TYPES.put("prf", "application/pics-rules");
            MIME_TYPES.put("ps", "application/postscript");
            MIME_TYPES.put("pub", "application/x-mspublisher");
            MIME_TYPES.put("qt", "video/quicktime");
            MIME_TYPES.put("ra", "audio/x-pn-realaudio");
            MIME_TYPES.put("ram", "audio/x-pn-realaudio");
            MIME_TYPES.put("ras", "image/x-cmu-raster");
            MIME_TYPES.put("rgb", "image/x-rgb");
            MIME_TYPES.put("rmi", "audio/mid");
            MIME_TYPES.put("roff", "application/x-troff");
            MIME_TYPES.put("rtf", "application/rtf");
            MIME_TYPES.put("rtx", "text/richtext");
            MIME_TYPES.put("scd", "application/x-msschedule");
            MIME_TYPES.put("sct", "text/scriptlet");
            MIME_TYPES.put("setpay", "application/set-payment-initiation");
            MIME_TYPES.put("setreg", "application/set-registration-initiation");
            MIME_TYPES.put("sh", "application/x-sh");
            MIME_TYPES.put("shar", "application/x-shar");
            MIME_TYPES.put("sit", "application/x-stuffit");
            MIME_TYPES.put("snd", "audio/basic");
            MIME_TYPES.put("spc", "application/x-pkcs7-certificates");
            MIME_TYPES.put("spl", "application/futuresplash");
            MIME_TYPES.put("src", "application/x-wais-source");
            MIME_TYPES.put("sst", "application/vnd.ms-pkicertstore");
            MIME_TYPES.put("stl", "application/vnd.ms-pkistl");
            MIME_TYPES.put("stm", "text/html");
            MIME_TYPES.put("svg", "image/svg+xml");
            MIME_TYPES.put("sv4cpio", "application/x-sv4cpio");
            MIME_TYPES.put("sv4crc", "application/x-sv4crc");
            MIME_TYPES.put("swf", "application/x-shockwave-flash");
            MIME_TYPES.put("t", "application/x-troff");
            MIME_TYPES.put("tar", "application/x-tar");
            MIME_TYPES.put("tcl", "application/x-tcl");
            MIME_TYPES.put("tex", "application/x-tex");
            MIME_TYPES.put("texi", "application/x-texinfo");
            MIME_TYPES.put("texinfo", "application/x-texinfo");
            MIME_TYPES.put("tgz", "application/x-compressed");
            MIME_TYPES.put("tif", "image/tiff");
            MIME_TYPES.put("tiff", "image/tiff");
            MIME_TYPES.put("tr", "application/x-troff");
            MIME_TYPES.put("trm", "application/x-msterminal");
            MIME_TYPES.put("tsv", "text/tab-separated-values");
            MIME_TYPES.put("txt", "text/plain");
            MIME_TYPES.put("uls", "text/iuls");
            MIME_TYPES.put("ustar", "application/x-ustar");
            MIME_TYPES.put("vcf", "text/x-vcard");
            MIME_TYPES.put("vrml", "x-world/x-vrml");
            MIME_TYPES.put("wav", "audio/x-wav");
            MIME_TYPES.put("wcm", "application/vnd.ms-works");
            MIME_TYPES.put("wdb", "application/vnd.ms-works");
            MIME_TYPES.put("wks", "application/vnd.ms-works");
            MIME_TYPES.put("wmf", "application/x-msmetafile");
            MIME_TYPES.put("wps", "application/vnd.ms-works");
            MIME_TYPES.put("wri", "application/x-mswrite");
            MIME_TYPES.put("wrl", "x-world/x-vrml");
            MIME_TYPES.put("wrz", "x-world/x-vrml");
            MIME_TYPES.put("xaf", "x-world/x-vrml");
            MIME_TYPES.put("xbm", "image/x-xbitmap");
            MIME_TYPES.put("xla", "application/vnd.ms-excel");
            MIME_TYPES.put("xlc", "application/vnd.ms-excel");
            MIME_TYPES.put("xlm", "application/vnd.ms-excel");
            MIME_TYPES.put("xls", "application/vnd.ms-excel");
            MIME_TYPES.put("xlt", "application/vnd.ms-excel");
            MIME_TYPES.put("xlw", "application/vnd.ms-excel");
            MIME_TYPES.put("xof", "x-world/x-vrml");
            MIME_TYPES.put("xpm", "image/x-xpixmap");
            MIME_TYPES.put("xwd", "image/x-xwindowdump");
            MIME_TYPES.put("z", "application/x-compress");
            MIME_TYPES.put("zip", "application/zip");
        }
        return MIME_TYPES.get(ext);
    }
}
