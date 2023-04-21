package com.mobolus.beans;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.servlet.http.HttpSession;
import com.shared.beans.*;
import com.mobolus.servlets.*;
import com.mobolus.*;

public class BnMessage {

    public static final int MAX_MODULE_MESSAGES = 10;

    public static ArrayList<HashMap<String, Object>> getMessages(String userId, String referenceId, int moduleId) {
        String SQL = "SELECT * FROM messages, links" + " WHERE m_reference_id = " + referenceId + " AND m_module_id = " + moduleId + " AND (((SELECT COUNT(*) FROM message_links WHERE ml_receiver = " + userId + " AND ml_message_id = m_message_id) > 0)" + " OR (m_public = 1 AND m_owner IN (SELECT l_pal_id FROM links WHERE l_user_id = " + userId + " AND l_status = " + BnMobolus.L_ACTIVE + ")))" + " AND m_owner = l_pal_id AND l_user_id = " + userId + " AND l_status = " + BnMobolus.L_ACTIVE + " ORDER BY m_creation_date ASC";
        return Database.getResults(SQL, null);
    }

    public static ArrayList<HashMap<String, Object>> getMessages(String referenceId, int moduleId) {
        String SQL = "SELECT * FROM messages WHERE m_reference_id = " + referenceId + " AND m_module_id = " + moduleId + " ORDER BY m_creation_date ASC";
        return Database.getResults(SQL, null);
    }

    public static ArrayList<HashMap<String, Object>> getNewMessages(String userId, int moduleId, int referenceId, String extraSQL, long limit, long offset, String orderBy) {
        String SQL = "SELECT * FROM new_messages, links, messages" + " WHERE nm_message_id = m_message_id AND nm_receiver = " + userId + " AND (((SELECT COUNT(*) FROM message_links WHERE ml_receiver = " + userId + " AND ml_message_id = m_message_id) > 0)" + " OR (m_public = 1 AND m_owner IN (SELECT l_pal_id FROM links WHERE l_user_id = " + userId + " AND l_status = " + BnMobolus.L_ACTIVE + ")))" + " AND m_owner = l_pal_id AND l_user_id = " + userId + " AND l_pal_id != " + userId + " AND l_status = " + BnMobolus.L_ACTIVE;
        if (referenceId != -1) {
            SQL += " AND m_reference_id = " + referenceId;
        }
        if (moduleId != -1) {
            SQL += " AND m_module_id = " + moduleId;
        }
        if (null != extraSQL) {
            SQL += " " + extraSQL;
        }
        if (null != orderBy) {
            SQL += " " + orderBy;
        }
        if (limit > 0) {
            SQL += " LIMIT " + limit;
        }
        if (offset > 0) {
            SQL += " OFFSET " + offset;
        }
        return Database.getResults(SQL, null);
    }

    public static ArrayList<HashMap<String, Object>> getModuleMessages(String userId, int moduleId, String extraSQL, long limit, long offset, String orderBy) {
        String SQL = "SELECT * FROM messages, links WHERE ((m_public AND m_owner" + " IN (SELECT l_pal_id FROM links WHERE l_user_id = " + userId + "))" + " OR m_message_id IN (SELECT m_message_id FROM messages, message_links" + " WHERE m_message_id = ml_message_id AND ml_receiver = " + userId + "))" + " AND m_owner = l_pal_id AND l_user_id = " + userId + " AND l_pal_id != " + userId + " AND l_status = " + BnMobolus.L_ACTIVE + " AND m_parent_id != 0 ";
        if (moduleId != -1) {
            SQL += " AND m_module_id = " + moduleId;
        }
        if (null != extraSQL) {
            SQL += " " + extraSQL;
        }
        if (null != orderBy) {
            SQL += " " + orderBy;
        }
        if (limit > 0) {
            SQL += " LIMIT " + limit;
        }
        if (offset > 0) {
            SQL += " OFFSET " + offset;
        }
        return Database.getResults(SQL, null);
    }

    public static long getNewMessageCount(String userId, int moduleId, String referenceId) {
        long count = 0;
        try {
            if (moduleId == BnMobolus.MDL_PHOTO_ALBUMS) {
                String photoAlbumSQL = "SELECT ph_photo_id FROM photos WHERE ph_photo_album_id = " + referenceId;
                ArrayList photos = Database.getResults(photoAlbumSQL, null);
                if (null != referenceId && photos.size() == 0) {
                    return 0;
                }
                referenceId = "";
                for (int i = 0; i < photos.size(); i++) {
                    HashMap photoRow = (HashMap) photos.get(i);
                    if (i != 0) {
                        referenceId += " OR m_reference_id = ";
                    }
                    referenceId += photoRow.get("ph_photo_id");
                }
                moduleId = BnMobolus.MDL_PHOTOS;
            }
            String SQL = "SELECT COUNT(*) as newMessages FROM new_messages, messages WHERE nm_message_id = m_message_id AND" + " nm_receiver=" + userId + " AND m_module_id = " + moduleId;
            if (null != referenceId && !"".equals(referenceId)) {
                SQL += " AND (m_reference_id = " + referenceId + ")";
            }
            ArrayList results = Database.getResults(SQL, null);
            if (results.size() > 0) {
                HashMap resultsRow = (HashMap) results.get(0);
                count = ((Long) resultsRow.get("newMessages")).longValue();
            }
        } catch (Exception e) {
            Log.msg("BnMessage.getMessageCount()", e);
        }
        return count;
    }

    public static boolean isMessageNew(String userId, String messageId, Connection conn) {
        String SQL = "SELECT * FROM new_messages WHERE nm_message_id = " + messageId + " AND " + "nm_receiver = " + userId;
        ArrayList<HashMap<String, Object>> results = Database.getResults(SQL, conn);
        return results.size() > 0;
    }

    public static void setMessageNewState(String userId, String messageId, boolean setToNew) {
        if (setToNew) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("nm_message_id", messageId);
            values.put("nm_receiver", userId);
            Database.insert("new_messages", values, null);
        } else {
            String whereSQL = "WHERE nm_message_id = " + messageId + " AND " + "nm_receiver = " + userId;
            Database.delete("new_messages", whereSQL, null);
        }
    }

    public static HashMap getMessageInfo(long messageId) {
        String SQL = "SELECT * FROM messages WHERE m_message_id = " + messageId;
        ArrayList result = Database.getResults(SQL, null);
        return (HashMap) result.get(0);
    }

    public static ArrayList<HashMap<String, Object>> getMessagesForModule(String userId, String referenceId, int moduleId) {
        ArrayList<HashMap<String, Object>> messages = new ArrayList<HashMap<String, Object>>();
        try {
            ArrayList<HashMap<String, Object>> results = null;
            if (null == userId) {
                results = getMessages(referenceId, moduleId);
            } else {
                results = getMessages(userId, referenceId, moduleId);
            }
            HashMap<String, ArrayList<HashMap<String, Object>>> tempData = new HashMap<String, ArrayList<HashMap<String, Object>>>();
            for (int i = 0; i < results.size(); i++) {
                HashMap<String, Object> currMessage = results.get(i);
                String parentId = currMessage.get("m_parent_id").toString();
                ArrayList<HashMap<String, Object>> children = null;
                if (null == tempData.get(parentId)) {
                    children = new ArrayList<HashMap<String, Object>>();
                } else {
                    children = tempData.get(parentId);
                }
                children.add(currMessage);
                tempData.put(parentId, children);
            }
            if (tempData.size() > 0) {
                populateMessageList((tempData.get("0")).get(0), tempData, messages);
            }
        } catch (Exception e) {
            Log.msg("BnMessage.getMessagesForModule() userId: " + userId + " referenceId: " + referenceId, e);
        }
        return messages;
    }

    public static Set<String> getUsersWhoCanSeeThisMessage(String userId, String referenceId, int moduleId, String messageId) {
        HashMap<String, HashMap<String, Object>> messageMap = getModuleMessageMap(userId, referenceId, moduleId);
        HashMap<String, Object> currentMessage = messageMap.get(messageId);
        HashSet<String> messageOwnerSet = new HashSet();
        HashSet<String> whoCanSeeTheMessageSet = new HashSet();
        String[] activePals = BnMobolus.getCurrentPalIds(currentMessage.get("m_owner").toString(), true);
        whoCanSeeTheMessageSet.addAll(Arrays.asList(activePals));
        messageOwnerSet.add(currentMessage.get("m_owner").toString());
        while (!"0".equals(currentMessage.get("m_parent_id").toString())) {
            String parentId = currentMessage.get("m_parent_id").toString();
            currentMessage = messageMap.get(parentId);
            if (null != currentMessage) {
                String ownerId = currentMessage.get("m_owner").toString();
                boolean addedNewOwner = messageOwnerSet.add(ownerId);
                if (addedNewOwner) {
                    activePals = BnMobolus.getCurrentPalIds(ownerId, true);
                    ArrayList<String> activePalsPlusThemSelves = new ArrayList(Arrays.asList(activePals));
                    activePalsPlusThemSelves.add(ownerId);
                    whoCanSeeTheMessageSet.retainAll(activePalsPlusThemSelves);
                }
            } else {
                Log.trace("BnMessage.getUsersWhoCanSeeThisMessage()", "No message found for id:" + parentId);
                break;
            }
        }
        ArrayList<HashMap<String, Object>> deletedModules = Database.getResults("SELECT dmi_deleter FROM deleted_module_items WHERE dmi_module_id = " + moduleId + " AND dmi_reference_id = " + referenceId, null);
        ArrayList<String> deleterList = new ArrayList();
        for (HashMap<String, Object> deleterRow : deletedModules) {
            deleterList.add(deleterRow.get("dmi_deleter").toString());
        }
        whoCanSeeTheMessageSet.removeAll(deleterList);
        return whoCanSeeTheMessageSet;
    }

    public static HashMap<String, HashMap<String, Object>> getModuleMessageMap(String userId, String referenceId, int moduleId) {
        ArrayList<HashMap<String, Object>> results = getMessages(userId, referenceId, moduleId);
        HashMap<String, HashMap<String, Object>> messageMap = new HashMap();
        for (HashMap<String, Object> currMessage : results) {
            String currMessageId = currMessage.get("m_message_id").toString();
            messageMap.put(currMessageId, currMessage);
        }
        return messageMap;
    }

    public static HashMap getMessageReceivers(String userId, String referenceId, int moduleId) {
        String SQL = "SELECT m_message_id,l_pal_id FROM links, messages, message_links" + " WHERE m_reference_id = " + referenceId + " AND m_module_id = " + moduleId + " AND m_message_id = ml_message_id" + " AND ml_receiver = l_pal_id AND l_user_id = " + userId + " AND l_pal_id != " + userId + " AND (l_status = " + BnMobolus.L_ACTIVE + " OR l_status = " + BnMobolus.L_PENDING + " OR l_status = " + BnMobolus.L_PENDING_GROUP + ")" + "  AND l_pal_id in (select l_pal_id from links where l_user_id = m_owner" + " AND l_pal_id not in (select dmi_deleter from deleted_module_items where dmi_module_id = " + moduleId + " AND dmi_reference_id = " + referenceId + " AND dmi_deleter = l_pal_id))";
        HashMap messageReceivers = new HashMap();
        ArrayList nonPublicResults = Database.getResults(SQL, null);
        for (int i = 0; i < nonPublicResults.size(); i++) {
            HashMap currData = (HashMap) nonPublicResults.get(i);
            String currMessageId = ((Long) currData.get("m_message_id")).toString();
            String currReceivers = (String) messageReceivers.get(currMessageId);
            if (null == currReceivers) {
                currReceivers = ((Long) currData.get("l_pal_id")).toString();
            } else {
                currReceivers += "," + ((Long) currData.get("l_pal_id")).toString();
            }
            messageReceivers.put(currMessageId, currReceivers);
        }
        SQL = "SELECT m_message_id,l_pal_id FROM links, messages" + " WHERE m_reference_id = " + referenceId + " AND m_module_id = " + moduleId + " AND m_public = 1 AND l_user_id = " + userId + " AND l_pal_id != " + userId + " AND (l_status = " + BnMobolus.L_ACTIVE + " OR l_status = " + BnMobolus.L_PENDING + " OR l_status = " + BnMobolus.L_PENDING_GROUP + ")" + "  AND l_pal_id in (select l_pal_id from links where l_user_id = m_owner" + " AND l_pal_id not in (select dmi_deleter from deleted_module_items where dmi_module_id = " + moduleId + " AND dmi_reference_id = " + referenceId + " AND dmi_deleter = l_pal_id))";
        ArrayList publicResults = Database.getResults(SQL, null);
        for (int i = 0; i < publicResults.size(); i++) {
            HashMap currData = (HashMap) publicResults.get(i);
            String currMessageId = ((Long) currData.get("m_message_id")).toString();
            String currReceivers = (String) messageReceivers.get(currMessageId);
            if (null == currReceivers) {
                currReceivers = ((Long) currData.get("l_pal_id")).toString();
            } else {
                currReceivers += "," + ((Long) currData.get("l_pal_id")).toString();
            }
            messageReceivers.put(currMessageId, currReceivers);
        }
        return messageReceivers;
    }

    private static void populateMessageList(HashMap currMessageData, HashMap allData, ArrayList messages) {
        messages.add(currMessageData);
        String currMessageId = currMessageData.get("m_message_id").toString();
        ArrayList currMessageChildren = (ArrayList) allData.get(currMessageId);
        if (null == currMessageChildren) {
            return;
        }
        for (int i = 0; i < currMessageChildren.size(); i++) {
            populateMessageList((HashMap) ((ArrayList) allData.get(currMessageId)).get(i), allData, messages);
        }
    }

    public static HashMap<String, Object> getRootMessage(String referenceId, int moduleId, Connection conn) {
        HashMap<String, Object> rootMessage = Database.getSingleResult("SELECT * FROM messages WHERE m_reference_id=" + referenceId + " AND m_module_id = " + moduleId + " AND m_parent_id = " + HndMessage.ROOT_MESSAGE, conn);
        return rootMessage;
    }

    public static long addMessage(String referenceId, String message, String replyToId, int moduleId, String ownerId, boolean isPublic, String[] selectedIds, Connection conn) {
        try {
            if ((moduleId == BnMobolus.MDL_CONVERSATIONS && (null == message || "".equals(message))) || null == ownerId || ((null == selectedIds || selectedIds.length == 0) && !isPublic)) {
                return -1;
            }
            String level = "0";
            boolean isRootMessage = HndMessage.ROOT_MESSAGE.equals(replyToId);
            if (!isRootMessage) {
                ArrayList replyToMessage = Database.getResults("SELECT m_level FROM messages WHERE m_message_id = " + replyToId, conn);
                HashMap replyToMessageRow = (HashMap) replyToMessage.get(0);
                level = String.valueOf(Integer.parseInt(replyToMessageRow.get("m_level").toString()) + 1);
            }
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("m_reference_id", Util.escapeForHTML(referenceId));
            values.put("m_owner", Util.escapeForHTML(ownerId));
            values.put("m_creation_date", "NOW()");
            values.put("m_message", "'" + Util.escapeForHTML(message) + "'");
            values.put("m_parent_id", Util.escapeForHTML(replyToId));
            values.put("m_level", Util.escapeForHTML(level));
            values.put("m_module_id", moduleId);
            values.put("m_last_modified", "NOW()");
            values.put("m_public", isPublic);
            long messageId = Database.insert("messages", values, conn);
            if (-1 == messageId) {
                return -1;
            }
            if (!isPublic) {
                values = new HashMap<String, Object>();
                values.put("ml_receiver", Util.escapeForHTML(ownerId));
                values.put("ml_message_id", messageId);
                Database.insert("message_links", values, conn);
                String[] palIds = BnMobolus.getCurrentPalIds(ownerId, false);
                for (int i = 0; i < selectedIds.length; i++) {
                    boolean isValidPal = false;
                    for (int j = 0; j < palIds.length; j++) {
                        if (palIds[j].equals(selectedIds[i])) {
                            isValidPal = true;
                            break;
                        }
                    }
                    if (isValidPal) {
                        values.put("ml_receiver", selectedIds[i]);
                        values.put("ml_message_id", messageId);
                        Database.insert("message_links", values, conn);
                        if (!isRootMessage) {
                            HashMap<String, Object> nmValues = new HashMap<String, Object>();
                            nmValues.put("nm_message_id", messageId);
                            nmValues.put("nm_receiver", selectedIds[i]);
                            try {
                                Database.insert("new_messages", nmValues, conn);
                            } catch (Exception e) {
                                Log.msg("BnMobolus.addMessage()", "Already found new module entry for: " + nmValues);
                            }
                        }
                    } else {
                        Log.trace("BnMessage.addMessage()", "Invalid user id in selectedIdList: " + selectedIds[i]);
                    }
                }
            } else {
                if (!isRootMessage) {
                    Set<String> whoCanSeeTheMessage = getUsersWhoCanSeeThisMessage(ownerId, referenceId, moduleId, String.valueOf(messageId));
                    for (String palId : whoCanSeeTheMessage) {
                        HashMap<String, Object> nmValues = new HashMap<String, Object>();
                        nmValues.put("nm_message_id", messageId);
                        nmValues.put("nm_receiver", palId);
                        try {
                            Database.insert("new_messages", nmValues, conn);
                        } catch (Exception e) {
                            Log.msg("BnMobolus.addMessage()", "Already found new module entry for: " + nmValues);
                        }
                    }
                }
            }
            return messageId;
        } catch (Exception e) {
            Log.msg("BnMessage.addMessage()", e);
            return -1;
        }
    }

    public static boolean editMessage(String messageId, String message) {
        HashMap values = new HashMap();
        values.put("m_message", "\"" + Util.escapeForHTML(message) + "\"");
        values.put("m_last_modified", "NOW()");
        long effectedRows = Database.set("messages", values, "WHERE m_message_id=" + messageId, null);
        if (0 == effectedRows) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean editRootMessage(String referenceId, int moduleId, String message) {
        HashMap values = new HashMap();
        values.put("m_message", "\"" + Util.escapeForHTML(message) + "\"");
        values.put("m_last_modified", "NOW()");
        long effectedRows = Database.set("messages", values, "WHERE m_reference_id=" + referenceId + " AND m_module_id=" + moduleId + " AND m_parent_id=" + HndMessage.ROOT_MESSAGE, null);
        if (0 == effectedRows) {
            return false;
        } else {
            return true;
        }
    }

    public static String getListOfChildren(String messageId, String userId, String referenceId, int moduleId) {
        String childIdList = messageId;
        boolean startAdding = false;
        int messageLevel = -1;
        ArrayList messages = BnMessage.getMessagesForModule(userId, referenceId, moduleId);
        for (int messageIdx = 0; messageIdx < messages.size(); messageIdx++) {
            HashMap dataRow = (HashMap) messages.get(messageIdx);
            String id = dataRow.get("m_message_id").toString();
            if (startAdding) {
                int currMessageLevel = Integer.parseInt(dataRow.get("m_level").toString());
                if (currMessageLevel <= messageLevel) {
                    break;
                }
                childIdList += "," + dataRow.get("m_message_id");
            }
            if (!startAdding && id.equals(messageId)) {
                startAdding = true;
                messageLevel = Integer.parseInt(dataRow.get("m_level").toString());
            }
        }
        return childIdList;
    }

    /**
	 * 
	 * @param messageId
	 * @param referenceId
	 * @param moduleId
	 * @param userId -- Pass in null to delete message for everyone
	 * @param conn
	 * @return
	 */
    public static boolean deleteMessage(String messageId, String referenceId, int moduleId, String userId, Connection conn) {
        try {
            if (null != messageId && null != referenceId) {
                String idsToDelete = getListOfChildren(messageId, userId, referenceId, moduleId);
                String whereClause = "WHERE ml_message_id IN (" + idsToDelete + ")";
                if (null != userId) {
                    whereClause += " AND ml_receiver = " + userId;
                }
                Database.delete("message_links", whereClause, conn);
                if (null == userId) {
                    Database.delete("messages", "WHERE m_message_id IN (" + idsToDelete + ")", conn);
                    Database.delete("messages", "WHERE m_message_id = " + messageId, conn);
                }
                String newMessagesWhereClause = "WHERE nm_message_id = " + messageId + " OR nm_message_id IN (" + idsToDelete + ")";
                if (null != userId) {
                    newMessagesWhereClause += " AND nm_receiver = " + userId;
                }
                Database.delete("new_messages", newMessagesWhereClause, conn);
                return true;
            }
        } catch (Exception e) {
            Log.msg("HndMessage.deleteMessage()", e);
        }
        return false;
    }
}
