package net.sf.nbb2;

import static com.numiton.VarHandling.*;
import java.io.IOException;
import javax.servlet.ServletException;
import net.sf.nbb2.includes.*;
import org.apache.log4j.Logger;
import com.numiton.*;
import com.numiton.Math;
import com.numiton.array.Array;
import com.numiton.array.ArrayEntry;
import com.numiton.java.JStrings;

public class viewforum extends NumitonServlet {

    protected static final Logger LOG = Logger.getLogger(viewforum.class.getName());

    public int total_announcements;

    public Array<Object> moderators = new Array<Object>();

    public Array<Object> topic_rowset = new Array<Object>();

    public String view_topic_url;

    public String select_topic_days;

    public String limit_topics_time;

    public String no_topics_msg;

    public int topics_count;

    public int min_topic_time;

    public viewforum() {
    }

    @SuppressWarnings("unchecked")
    public Object generateContent(PhpWebEnvironment webEnv) throws IOException, ServletException {
        gVars.webEnv = webEnv;
        gConsts.setIN_PHPBB(intval(true));
        gVars.phpbb_root_path = "./";
        PhpWeb.include(gVars, gConsts, net.sf.nbb2.extension.class);
        PhpWeb.include(gVars, gConsts, net.sf.nbb2.common.class);
        if (isset(gVars.HTTP_GET_VARS.getValue(gConsts.getPOST_FORUM_URL())) || isset(gVars.HTTP_POST_VARS.getValue(gConsts.getPOST_FORUM_URL()))) {
            gVars.forum_id = (isset(gVars.HTTP_GET_VARS.getValue(gConsts.getPOST_FORUM_URL())) ? intval(gVars.HTTP_GET_VARS.getValue(gConsts.getPOST_FORUM_URL())) : intval(gVars.HTTP_POST_VARS.getValue(gConsts.getPOST_FORUM_URL())));
        } else if (isset(gVars.HTTP_GET_VARS.getValue("forum"))) {
            gVars.forum_id = intval(gVars.HTTP_GET_VARS.getValue("forum"));
        } else {
            gVars.forum_id = intval("");
        }
        gVars.start = isset(gVars.HTTP_GET_VARS.getValue("start")) ? intval(gVars.HTTP_GET_VARS.getValue("start")) : 0;
        gVars.start = (gVars.start < 0) ? 0 : gVars.start;
        if (isset(gVars.HTTP_GET_VARS.getValue("mark")) || isset(gVars.HTTP_POST_VARS.getValue("mark"))) {
            gVars.mark_read = (isset(gVars.HTTP_POST_VARS.getValue("mark")) ? strval(gVars.HTTP_POST_VARS.getValue("mark")) : strval(gVars.HTTP_GET_VARS.getValue("mark")));
        } else {
            gVars.mark_read = "";
        }
        if (!empty(gVars.forum_id)) {
            gVars.sqlStr = "SELECT *\n\t\tFROM " + gConsts.getFORUMS_TABLE() + "\n\t\tWHERE forum_id = " + strval(gVars.forum_id);
            if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
                (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not obtain forums information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
            }
        } else {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_MESSAGE(), "Forum_not_exist", "", "", "", "");
        }
        if (!booleanval(gVars.forum_row = gVars.db.sql_fetchrow(gVars.result))) {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_MESSAGE(), "Forum_not_exist", "", "", "", "");
        }
        gVars.userdata = (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).session_pagestart(gVars.user_ip, gVars.forum_id);
        (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).init_userprefs(gVars.userdata);
        gVars.is_auth = new Array<Object>();
        gVars.is_auth = (((auth) PhpWeb.getIncluded(auth.class, gVars, gConsts))).auth(gConsts.getAUTH_ALL(), gVars.forum_id, gVars.userdata, gVars.forum_row);
        if (!booleanval(gVars.is_auth.getValue("auth_read")) || !booleanval(gVars.is_auth.getValue("auth_view"))) {
            if (!booleanval(gVars.userdata.getValue("session_logged_in"))) {
                gVars.redirect = gConsts.getPOST_FORUM_URL() + "=" + strval(gVars.forum_id) + (isset(gVars.start) ? ("&start=" + strval(gVars.start)) : "");
                (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).redirect((((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("login." + gVars.phpEx + "?redirect=viewforum." + gVars.phpEx + "&" + gVars.redirect, true));
            }
            gVars.message = ((!booleanval(gVars.is_auth.getValue("auth_view"))) ? strval(gVars.lang.getValue("Forum_not_exist")) : JStrings.sprintf(strval(gVars.lang.getValue("Sorry_auth_read")), gVars.is_auth.getValue("auth_read_type")));
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_MESSAGE(), gVars.message, "", "", "", "");
        }
        if (equal(gVars.mark_read, "topics")) {
            if (booleanval(gVars.userdata.getValue("session_logged_in"))) {
                gVars.sqlStr = "SELECT MAX(post_time) AS last_post \n\t\t\tFROM " + gConsts.getPOSTS_TABLE() + " \n\t\t\tWHERE forum_id = " + strval(gVars.forum_id);
                if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
                    (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not obtain forums information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
                }
                if (booleanval(gVars.row = gVars.db.sql_fetchrow(gVars.result))) {
                    gVars.tracking_forums = (isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f")) ? (Array<Object>) unserialize(strval(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f"))) : new Array<Object>());
                    gVars.tracking_topics = (isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_t")) ? (Array<Object>) unserialize(strval(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_t"))) : new Array<Object>());
                    if (((Array.count(gVars.tracking_forums) + Array.count(gVars.tracking_topics)) >= 150) && empty(gVars.tracking_forums.getValue(gVars.forum_id))) {
                        Array.asort(gVars.tracking_forums);
                        gVars.tracking_forums.arrayUnset(Array.key(gVars.tracking_forums));
                    }
                    if (intval(gVars.row.getValue("last_post")) > intval(gVars.userdata.getValue("user_lastvisit"))) {
                        gVars.tracking_forums.putValue(gVars.forum_id, DateTime.time());
                        Network.setcookie(gVars.webEnv, strval(gVars.board_config.getValue("cookie_name")) + "_f", serialize(gVars.tracking_forums), 0, strval(gVars.board_config.getValue("cookie_path")), strval(gVars.board_config.getValue("cookie_domain")), booleanval(gVars.board_config.getValue("cookie_secure")));
                    }
                }
                gVars.template.assign_vars(new Array<Object>(new ArrayEntry("META", "<meta http-equiv=\"refresh\" content=\"3;url=" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id, false) + "\">")));
            }
            gVars.message = strval(gVars.lang.getValue("Topics_marked_read")) + "<br /><br />" + JStrings.sprintf(strval(gVars.lang.getValue("Click_return_forum")), "<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + strval(gVars.forum_id), false) + "\">", "</a> ");
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_MESSAGE(), gVars.message, "", "", "", "");
        }
        gVars.tracking_topics = (isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_t")) ? (Array<Object>) unserialize(strval(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_t"))) : new Array<Object>());
        gVars.tracking_forums = (isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f")) ? (Array<Object>) unserialize(strval(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f"))) : new Array<Object>());
        if (booleanval(gVars.is_auth.getValue("auth_mod")) && booleanval(gVars.board_config.getValue("prune_enable"))) {
            if ((intval(gVars.forum_row.getValue("prune_next")) < DateTime.time()) && booleanval(gVars.forum_row.getValue("prune_enable"))) {
                PhpWeb.include(gVars, gConsts, net.sf.nbb2.includes.prune.class);
                PhpWeb.require(gVars, gConsts, net.sf.nbb2.includes.functions_admin.class);
                (((prune) PhpWeb.getIncluded(prune.class, gVars, gConsts))).auto_prune(gVars.forum_id);
            }
        }
        gVars.sqlStr = "SELECT u.user_id, u.username \n\tFROM " + gConsts.getAUTH_ACCESS_TABLE() + " aa, " + gConsts.getUSER_GROUP_TABLE() + " ug, " + gConsts.getGROUPS_TABLE() + " g, " + gConsts.getUSERS_TABLE() + " u\n\tWHERE aa.forum_id = " + strval(gVars.forum_id) + " \n\t\tAND aa.auth_mod = " + strval(true) + " \n\t\tAND g.group_single_user = 1\n\t\tAND ug.group_id = aa.group_id \n\t\tAND g.group_id = aa.group_id \n\t\tAND u.user_id = ug.user_id \n\tGROUP BY u.user_id, u.username  \n\tORDER BY u.user_id";
        if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not query forum moderator information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
        }
        moderators = new Array<Object>();
        while (booleanval(gVars.row = gVars.db.sql_fetchrow(gVars.result))) {
            moderators.putValue("<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("profile." + gVars.phpEx + "?mode=viewprofile&amp;" + gConsts.getPOST_USERS_URL() + "=" + strval(gVars.row.getValue("user_id")), false) + "\">" + strval(gVars.row.getValue("username")) + "</a>");
        }
        gVars.sqlStr = "SELECT g.group_id, g.group_name \n\tFROM " + gConsts.getAUTH_ACCESS_TABLE() + " aa, " + gConsts.getUSER_GROUP_TABLE() + " ug, " + gConsts.getGROUPS_TABLE() + " g \n\tWHERE aa.forum_id = " + strval(gVars.forum_id) + "\n\t\tAND aa.auth_mod = " + strval(true) + " \n\t\tAND g.group_single_user = 0\n\t\tAND g.group_type <> " + gConsts.getGROUP_HIDDEN() + "\n\t\tAND ug.group_id = aa.group_id \n\t\tAND g.group_id = aa.group_id \n\tGROUP BY g.group_id, g.group_name  \n\tORDER BY g.group_id";
        if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not query forum moderator information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
        }
        while (booleanval(gVars.row = gVars.db.sql_fetchrow(gVars.result))) {
            moderators.putValue("<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("groupcp." + gVars.phpEx + "?" + gConsts.getPOST_GROUPS_URL() + "=" + strval(gVars.row.getValue("group_id")), false) + "\">" + strval(gVars.row.getValue("group_name")) + "</a>");
        }
        gVars.l_moderators = (equal(Array.count(moderators), 1) ? strval(gVars.lang.getValue("Moderator")) : strval(gVars.lang.getValue("Moderators")));
        String forum_moderatorsStr = (booleanval(Array.count(moderators)) ? Strings.implode(", ", moderators) : strval(gVars.lang.getValue("None")));
        moderators = null;
        gVars.previous_days = new Array<Object>(new ArrayEntry(0), new ArrayEntry(1), new ArrayEntry(7), new ArrayEntry(14), new ArrayEntry(30), new ArrayEntry(90), new ArrayEntry(180), new ArrayEntry(364));
        gVars.previous_days_text = new Array<Object>(new ArrayEntry(gVars.lang.getValue("All_Topics")), new ArrayEntry(gVars.lang.getValue("1_Day")), new ArrayEntry(gVars.lang.getValue("7_Days")), new ArrayEntry(gVars.lang.getValue("2_Weeks")), new ArrayEntry(gVars.lang.getValue("1_Month")), new ArrayEntry(gVars.lang.getValue("3_Months")), new ArrayEntry(gVars.lang.getValue("6_Months")), new ArrayEntry(gVars.lang.getValue("1_Year")));
        if (!empty(gVars.HTTP_POST_VARS.getValue("topicdays")) || !empty(gVars.HTTP_GET_VARS.getValue("topicdays"))) {
            gVars.topic_days = ((!empty(gVars.HTTP_POST_VARS.getValue("topicdays"))) ? intval(gVars.HTTP_POST_VARS.getValue("topicdays")) : intval(gVars.HTTP_GET_VARS.getValue("topicdays")));
            min_topic_time = DateTime.time() - (gVars.topic_days * 86400);
            gVars.sqlStr = "SELECT COUNT(t.topic_id) AS forum_topics \n\t\tFROM " + gConsts.getTOPICS_TABLE() + " t, " + gConsts.getPOSTS_TABLE() + " p \n\t\tWHERE t.forum_id = " + strval(gVars.forum_id) + " \n\t\t\tAND p.post_id = t.topic_last_post_id\n\t\t\tAND p.post_time >= " + strval(min_topic_time);
            if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
                (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not obtain limited topics count information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
            }
            gVars.row = gVars.db.sql_fetchrow(gVars.result);
            topics_count = (booleanval(gVars.row.getValue("forum_topics")) ? intval(gVars.row.getValue("forum_topics")) : 1);
            limit_topics_time = "AND p.post_time >= " + strval(min_topic_time);
            if (!empty(gVars.HTTP_POST_VARS.getValue("topicdays"))) {
                gVars.start = 0;
            }
        } else {
            topics_count = (booleanval(gVars.forum_row.getValue("forum_topics")) ? intval(gVars.forum_row.getValue("forum_topics")) : 1);
            limit_topics_time = "";
            gVars.topic_days = 0;
        }
        select_topic_days = "<select name=\"topicdays\">";
        for (gVars.i = 0; gVars.i < Array.count(gVars.previous_days); gVars.i++) {
            gVars.selected = (equal(gVars.topic_days, gVars.previous_days.getValue(gVars.i)) ? " selected=\"selected\"" : "");
            select_topic_days = select_topic_days + "<option value=\"" + strval(gVars.previous_days.getValue(gVars.i)) + "\"" + gVars.selected + ">" + strval(gVars.previous_days_text.getValue(gVars.i)) + "</option>";
        }
        select_topic_days = select_topic_days + "</select>";
        gVars.sqlStr = "SELECT t.*, u.username, u.user_id, u2.username as user2, u2.user_id as id2, p.post_time, p.post_username\n\tFROM " + gConsts.getTOPICS_TABLE() + " t, " + gConsts.getUSERS_TABLE() + " u, " + gConsts.getPOSTS_TABLE() + " p, " + gConsts.getUSERS_TABLE() + " u2\n\tWHERE t.forum_id = " + strval(gVars.forum_id) + " \n\t\tAND t.topic_poster = u.user_id\n\t\tAND p.post_id = t.topic_last_post_id\n\t\tAND p.poster_id = u2.user_id\n\t\tAND t.topic_type = " + gConsts.getPOST_ANNOUNCE() + " \n\tORDER BY t.topic_last_post_id DESC ";
        if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not obtain topic information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
        }
        topic_rowset = new Array<Object>();
        total_announcements = 0;
        while (booleanval(gVars.row = gVars.db.sql_fetchrow(gVars.result))) {
            topic_rowset.putValue(gVars.row);
            total_announcements++;
        }
        gVars.db.sql_freeresult(gVars.result);
        gVars.sqlStr = "SELECT t.*, u.username, u.user_id, u2.username as user2, u2.user_id as id2, p.post_username, p2.post_username AS post_username2, p2.post_time \n\tFROM " + gConsts.getTOPICS_TABLE() + " t, " + gConsts.getUSERS_TABLE() + " u, " + gConsts.getPOSTS_TABLE() + " p, " + gConsts.getPOSTS_TABLE() + " p2, " + gConsts.getUSERS_TABLE() + " u2\n\tWHERE t.forum_id = " + strval(gVars.forum_id) + "\n\t\tAND t.topic_poster = u.user_id\n\t\tAND p.post_id = t.topic_first_post_id\n\t\tAND p2.post_id = t.topic_last_post_id\n\t\tAND u2.user_id = p2.poster_id \n\t\tAND t.topic_type <> " + gConsts.getPOST_ANNOUNCE() + " \n\t\t" + limit_topics_time + "\n\tORDER BY t.topic_type DESC, t.topic_last_post_id DESC \n\tLIMIT " + strval(gVars.start) + ", " + strval(gVars.board_config.getValue("topics_per_page"));
        if (!booleanval(gVars.result = gVars.db.sql_query(gVars.sqlStr))) {
            (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).message_die(gConsts.getGENERAL_ERROR(), "Could not obtain topic information", "", SourceCodeInfo.getCurrentLine(), SourceCodeInfo.getCurrentFile(), gVars.sqlStr);
        }
        gVars.total_topics = 0;
        while (booleanval(gVars.row = gVars.db.sql_fetchrow(gVars.result))) {
            topic_rowset.putValue(gVars.row);
            gVars.total_topics++;
        }
        gVars.db.sql_freeresult(gVars.result);
        gVars.total_topics = gVars.total_topics + total_announcements;
        gVars.orig_word = new Array<String>();
        gVars.replacement_word = new Array<String>();
        (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).obtain_word_list(gVars.orig_word, gVars.replacement_word);
        gVars.template.assign_vars(new Array<Object>(new ArrayEntry("L_DISPLAY_TOPICS", gVars.lang.getValue("Display_topics")), new ArrayEntry("U_POST_NEW_TOPIC", (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("posting." + gVars.phpEx + "?mode=newtopic&amp;" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id, false)), new ArrayEntry("S_SELECT_TOPIC_DAYS", select_topic_days), new ArrayEntry("S_POST_DAYS_ACTION", (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id + "&amp;start=" + gVars.start, false))));
        gVars.s_auth_can = strval(booleanval(gVars.is_auth.getValue("auth_post")) ? gVars.lang.getValue("Rules_post_can") : gVars.lang.getValue("Rules_post_cannot")) + "<br />";
        gVars.s_auth_can = gVars.s_auth_can + strval(booleanval(gVars.is_auth.getValue("auth_reply")) ? gVars.lang.getValue("Rules_reply_can") : gVars.lang.getValue("Rules_reply_cannot")) + "<br />";
        gVars.s_auth_can = gVars.s_auth_can + strval(booleanval(gVars.is_auth.getValue("auth_edit")) ? gVars.lang.getValue("Rules_edit_can") : gVars.lang.getValue("Rules_edit_cannot")) + "<br />";
        gVars.s_auth_can = gVars.s_auth_can + strval(booleanval(gVars.is_auth.getValue("auth_delete")) ? gVars.lang.getValue("Rules_delete_can") : gVars.lang.getValue("Rules_delete_cannot")) + "<br />";
        gVars.s_auth_can = gVars.s_auth_can + strval(booleanval(gVars.is_auth.getValue("auth_vote")) ? gVars.lang.getValue("Rules_vote_can") : gVars.lang.getValue("Rules_vote_cannot")) + "<br />";
        if (booleanval(gVars.is_auth.getValue("auth_mod"))) {
            gVars.s_auth_can = gVars.s_auth_can + JStrings.sprintf(strval(gVars.lang.getValue("Rules_moderate")), "<a href=\"modcp." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + strval(gVars.forum_id) + "&amp;start=" + strval(gVars.start) + "&amp;sid=" + strval(gVars.userdata.getValue("session_id")) + "\">", "</a>");
        }
        gVars.nav_links.putValue("up", new Array<Object>(new ArrayEntry("url", (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("index." + gVars.phpEx, false)), new ArrayEntry("title", JStrings.sprintf(strval(gVars.lang.getValue("Forum_Index")), gVars.board_config.getValue("sitename")))));
        gConsts.setSHOW_ONLINE(true);
        gVars.page_title = strval(gVars.lang.getValue("View_forum")) + " - " + strval(gVars.forum_row.getValue("forum_name"));
        PhpWeb.include(gVars, gConsts, net.sf.nbb2.includes.page_header.class);
        gVars.template.set_filenames(new Array<Object>(new ArrayEntry("body", "viewforum_body.tpl")));
        (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).make_jumpbox("viewforum." + gVars.phpEx, 0);
        gVars.template.assign_vars(new Array<Object>(new ArrayEntry("FORUM_ID", gVars.forum_id), new ArrayEntry("FORUM_NAME", gVars.forum_row.getValue("forum_name")), new ArrayEntry("MODERATORS", forum_moderatorsStr), new ArrayEntry("POST_IMG", equal(gVars.forum_row.getValue("forum_status"), gConsts.getFORUM_LOCKED()) ? gVars.images.getValue("post_locked") : gVars.images.getValue("post_new")), new ArrayEntry("FOLDER_IMG", gVars.images.getValue("folder")), new ArrayEntry("FOLDER_NEW_IMG", gVars.images.getValue("folder_new")), new ArrayEntry("FOLDER_HOT_IMG", gVars.images.getValue("folder_hot")), new ArrayEntry("FOLDER_HOT_NEW_IMG", gVars.images.getValue("folder_hot_new")), new ArrayEntry("FOLDER_LOCKED_IMG", gVars.images.getValue("folder_locked")), new ArrayEntry("FOLDER_LOCKED_NEW_IMG", gVars.images.getValue("folder_locked_new")), new ArrayEntry("FOLDER_STICKY_IMG", gVars.images.getValue("folder_sticky")), new ArrayEntry("FOLDER_STICKY_NEW_IMG", gVars.images.getValue("folder_sticky_new")), new ArrayEntry("FOLDER_ANNOUNCE_IMG", gVars.images.getValue("folder_announce")), new ArrayEntry("FOLDER_ANNOUNCE_NEW_IMG", gVars.images.getValue("folder_announce_new")), new ArrayEntry("L_TOPICS", gVars.lang.getValue("Topics")), new ArrayEntry("L_REPLIES", gVars.lang.getValue("Replies")), new ArrayEntry("L_VIEWS", gVars.lang.getValue("Views")), new ArrayEntry("L_POSTS", gVars.lang.getValue("Posts")), new ArrayEntry("L_LASTPOST", gVars.lang.getValue("Last_Post")), new ArrayEntry("L_MODERATOR", gVars.l_moderators), new ArrayEntry("L_MARK_TOPICS_READ", gVars.lang.getValue("Mark_all_topics")), new ArrayEntry("L_POST_NEW_TOPIC", equal(gVars.forum_row.getValue("forum_status"), gConsts.getFORUM_LOCKED()) ? gVars.lang.getValue("Forum_locked") : gVars.lang.getValue("Post_new_topic")), new ArrayEntry("L_NO_NEW_POSTS", gVars.lang.getValue("No_new_posts")), new ArrayEntry("L_NEW_POSTS", gVars.lang.getValue("New_posts")), new ArrayEntry("L_NO_NEW_POSTS_LOCKED", gVars.lang.getValue("No_new_posts_locked")), new ArrayEntry("L_NEW_POSTS_LOCKED", gVars.lang.getValue("New_posts_locked")), new ArrayEntry("L_NO_NEW_POSTS_HOT", gVars.lang.getValue("No_new_posts_hot")), new ArrayEntry("L_NEW_POSTS_HOT", gVars.lang.getValue("New_posts_hot")), new ArrayEntry("L_ANNOUNCEMENT", gVars.lang.getValue("Post_Announcement")), new ArrayEntry("L_STICKY", gVars.lang.getValue("Post_Sticky")), new ArrayEntry("L_POSTED", gVars.lang.getValue("Posted")), new ArrayEntry("L_JOINED", gVars.lang.getValue("Joined")), new ArrayEntry("L_AUTHOR", gVars.lang.getValue("Author")), new ArrayEntry("S_AUTH_LIST", gVars.s_auth_can), new ArrayEntry("U_VIEW_FORUM", (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id, false)), new ArrayEntry("U_MARK_READ", (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id + "&amp;mark=topics", false))));
        if (booleanval(gVars.total_topics)) {
            for (gVars.i = 0; gVars.i < gVars.total_topics; gVars.i++) {
                gVars.topic_id = intval(topic_rowset.getArrayValue(gVars.i).getValue("topic_id"));
                gVars.topic_title = (booleanval(Array.count(gVars.orig_word)) ? RegExPerl.preg_replace(gVars.orig_word, gVars.replacement_word, strval(topic_rowset.getArrayValue(gVars.i).getValue("topic_title"))) : strval(topic_rowset.getArrayValue(gVars.i).getValue("topic_title")));
                gVars.replies = intval(topic_rowset.getArrayValue(gVars.i).getValue("topic_replies"));
                gVars.topic_type = strval(topic_rowset.getArrayValue(gVars.i).getValue("topic_type"));
                if (equal(gVars.topic_type, gConsts.getPOST_ANNOUNCE())) {
                    gVars.topic_type = strval(gVars.lang.getValue("Topic_Announcement")) + " ";
                } else if (equal(gVars.topic_type, gConsts.getPOST_STICKY())) {
                    gVars.topic_type = strval(gVars.lang.getValue("Topic_Sticky")) + " ";
                } else {
                    gVars.topic_type = "";
                }
                if (booleanval(topic_rowset.getArrayValue(gVars.i).getValue("topic_vote"))) {
                    gVars.topic_type = gVars.topic_type + strval(gVars.lang.getValue("Topic_Poll")) + " ";
                }
                if (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_MOVED())) {
                    gVars.topic_type = strval(gVars.lang.getValue("Topic_Moved")) + " ";
                    gVars.topic_id = intval(topic_rowset.getArrayValue(gVars.i).getValue("topic_moved_id"));
                    gVars.folder_image = strval(gVars.images.getValue("folder"));
                    gVars.folder_alt = strval(gVars.lang.getValue("Topics_Moved"));
                    gVars.newest_post_img = "";
                } else {
                    if (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_type"), gConsts.getPOST_ANNOUNCE())) {
                        gVars.folder = strval(gVars.images.getValue("folder_announce"));
                        gVars.folder_new = strval(gVars.images.getValue("folder_announce_new"));
                    } else if (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_type"), gConsts.getPOST_STICKY())) {
                        gVars.folder = strval(gVars.images.getValue("folder_sticky"));
                        gVars.folder_new = strval(gVars.images.getValue("folder_sticky_new"));
                    } else if (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_LOCKED())) {
                        gVars.folder = strval(gVars.images.getValue("folder_locked"));
                        gVars.folder_new = strval(gVars.images.getValue("folder_locked_new"));
                    } else {
                        if (gVars.replies >= intval(gVars.board_config.getValue("hot_threshold"))) {
                            gVars.folder = strval(gVars.images.getValue("folder_hot"));
                            gVars.folder_new = strval(gVars.images.getValue("folder_hot_new"));
                        } else {
                            gVars.folder = strval(gVars.images.getValue("folder"));
                            gVars.folder_new = strval(gVars.images.getValue("folder_new"));
                        }
                    }
                    gVars.newest_post_img = "";
                    if (booleanval(gVars.userdata.getValue("session_logged_in"))) {
                        if (intval(topic_rowset.getArrayValue(gVars.i).getValue("post_time")) > intval(gVars.userdata.getValue("user_lastvisit"))) {
                            if (!empty(gVars.tracking_topics) || !empty(gVars.tracking_forums) || isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f_all"))) {
                                gVars.unread_topics = true;
                                if (!empty(gVars.tracking_topics.getValue(gVars.topic_id))) {
                                    if (intval(gVars.tracking_topics.getValue(gVars.topic_id)) >= intval(topic_rowset.getArrayValue(gVars.i).getValue("post_time"))) {
                                        gVars.unread_topics = false;
                                    }
                                }
                                if (!empty(gVars.tracking_forums.getValue(gVars.forum_id))) {
                                    if (intval(gVars.tracking_forums.getValue(gVars.forum_id)) >= intval(topic_rowset.getArrayValue(gVars.i).getValue("post_time"))) {
                                        gVars.unread_topics = false;
                                    }
                                }
                                if (isset(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f_all"))) {
                                    if (intval(gVars.HTTP_COOKIE_VARS.getValue(gVars.board_config.getValue("cookie_name") + "_f_all")) >= intval(topic_rowset.getArrayValue(gVars.i).getValue("post_time"))) {
                                        gVars.unread_topics = false;
                                    }
                                }
                                if (gVars.unread_topics) {
                                    gVars.folder_image = gVars.folder_new;
                                    gVars.folder_alt = strval(gVars.lang.getValue("New_posts"));
                                    gVars.newest_post_img = "<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewtopic." + gVars.phpEx + "?" + gConsts.getPOST_TOPIC_URL() + "=" + gVars.topic_id + "&amp;view=newest", false) + "\"><img src=\"" + strval(gVars.images.getValue("icon_newest_reply")) + "\" alt=\"" + strval(gVars.lang.getValue("View_newest_post")) + "\" title=\"" + strval(gVars.lang.getValue("View_newest_post")) + "\" border=\"0\" /></a> ";
                                } else {
                                    gVars.folder_image = gVars.folder;
                                    gVars.folder_alt = (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_LOCKED()) ? strval(gVars.lang.getValue("Topic_locked")) : strval(gVars.lang.getValue("No_new_posts")));
                                    gVars.newest_post_img = "";
                                }
                            } else {
                                gVars.folder_image = gVars.folder_new;
                                gVars.folder_alt = (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_LOCKED()) ? strval(gVars.lang.getValue("Topic_locked")) : strval(gVars.lang.getValue("New_posts")));
                                gVars.newest_post_img = "<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewtopic." + gVars.phpEx + "?" + gConsts.getPOST_TOPIC_URL() + "=" + gVars.topic_id + "&amp;view=newest", false) + "\"><img src=\"" + strval(gVars.images.getValue("icon_newest_reply")) + "\" alt=\"" + strval(gVars.lang.getValue("View_newest_post")) + "\" title=\"" + strval(gVars.lang.getValue("View_newest_post")) + "\" border=\"0\" /></a> ";
                            }
                        } else {
                            gVars.folder_image = gVars.folder;
                            gVars.folder_alt = (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_LOCKED()) ? strval(gVars.lang.getValue("Topic_locked")) : strval(gVars.lang.getValue("No_new_posts")));
                            gVars.newest_post_img = "";
                        }
                    } else {
                        gVars.folder_image = gVars.folder;
                        gVars.folder_alt = (equal(topic_rowset.getArrayValue(gVars.i).getValue("topic_status"), gConsts.getTOPIC_LOCKED()) ? strval(gVars.lang.getValue("Topic_locked")) : strval(gVars.lang.getValue("No_new_posts")));
                        gVars.newest_post_img = "";
                    }
                }
                if ((gVars.replies + 1) > intval(gVars.board_config.getValue("posts_per_page"))) {
                    gVars.total_pages = Math.ceil((gVars.replies + 1) / floatval(gVars.board_config.getValue("posts_per_page")));
                    gVars.goto_page = " [ <img src=\"" + strval(gVars.images.getValue("icon_gotopost")) + "\" alt=\"" + strval(gVars.lang.getValue("Goto_page")) + "\" title=\"" + strval(gVars.lang.getValue("Goto_page")) + "\" />" + strval(gVars.lang.getValue("Goto_page")) + ": ";
                    gVars.times = 1;
                    for (gVars.j = 0; gVars.j < (gVars.replies + 1); gVars.j = gVars.j + intval(gVars.board_config.getValue("posts_per_page"))) {
                        gVars.goto_page = gVars.goto_page + "<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewtopic." + gVars.phpEx + "?" + gConsts.getPOST_TOPIC_URL() + "=" + gVars.topic_id + "&amp;start=" + strval(gVars.j), false) + "\">" + strval(gVars.times) + "</a>";
                        if (equal(gVars.times, 1) && (gVars.total_pages > 4)) {
                            gVars.goto_page = gVars.goto_page + " ... ";
                            gVars.times = gVars.total_pages - 3;
                            gVars.j = gVars.j + ((gVars.total_pages - 4) * intval(gVars.board_config.getValue("posts_per_page")));
                        } else if (gVars.times < gVars.total_pages) {
                            gVars.goto_page = gVars.goto_page + ", ";
                        }
                        gVars.times++;
                    }
                    gVars.goto_page = gVars.goto_page + " ] ";
                } else {
                    gVars.goto_page = "";
                }
                view_topic_url = (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewtopic." + gVars.phpEx + "?" + gConsts.getPOST_TOPIC_URL() + "=" + gVars.topic_id, false);
                gVars.topic_author = ((!equal(topic_rowset.getArrayValue(gVars.i).getValue("user_id"), gConsts.getANONYMOUS())) ? ("<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("profile." + gVars.phpEx + "?mode=viewprofile&amp;" + gConsts.getPOST_USERS_URL() + "=" + strval(topic_rowset.getArrayValue(gVars.i).getValue("user_id")), false) + "\">") : "");
                gVars.topic_author = gVars.topic_author + strval((!equal(topic_rowset.getArrayValue(gVars.i).getValue("user_id"), gConsts.getANONYMOUS())) ? topic_rowset.getArrayValue(gVars.i).getValue("username") : ((!equal(topic_rowset.getArrayValue(gVars.i).getValue("post_username"), "")) ? ((Array<Object>) topic_rowset.getValue(gVars.i)).getValue("post_username") : gVars.lang.getValue("Guest")));
                gVars.topic_author = gVars.topic_author + ((!equal(topic_rowset.getArrayValue(gVars.i).getValue("user_id"), gConsts.getANONYMOUS())) ? "</a>" : "");
                gVars.first_post_time = (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).create_date(strval(gVars.board_config.getValue("default_dateformat")), intval(topic_rowset.getArrayValue(gVars.i).getValue("topic_time")), intval(gVars.board_config.getValue("board_timezone")));
                gVars.last_post_time = (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).create_date(strval(gVars.board_config.getValue("default_dateformat")), intval(topic_rowset.getArrayValue(gVars.i).getValue("post_time")), intval(gVars.board_config.getValue("board_timezone")));
                gVars.last_post_author = (equal(topic_rowset.getArrayValue(gVars.i).getValue("id2"), gConsts.getANONYMOUS()) ? ((!equal(topic_rowset.getArrayValue(gVars.i).getValue("post_username2"), "")) ? (strval(((Array<Object>) topic_rowset.getValue(gVars.i)).getValue("post_username2")) + " ") : (strval(gVars.lang.getValue("Guest")) + " ")) : ("<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("profile." + gVars.phpEx + "?mode=viewprofile&amp;" + gConsts.getPOST_USERS_URL() + "=" + strval(topic_rowset.getArrayValue(gVars.i).getValue("id2")), false) + "\">" + strval(topic_rowset.getArrayValue(gVars.i).getValue("user2")) + "</a>"));
                gVars.last_post_url = "<a href=\"" + (((sessions) PhpWeb.getIncluded(sessions.class, gVars, gConsts))).append_sid("viewtopic." + gVars.phpEx + "?" + gConsts.getPOST_POST_URL() + "=" + strval(topic_rowset.getArrayValue(gVars.i).getValue("topic_last_post_id")), false) + "#" + strval(topic_rowset.getArrayValue(gVars.i).getValue("topic_last_post_id")) + "\"><img src=\"" + strval(gVars.images.getValue("icon_latest_reply")) + "\" alt=\"" + strval(gVars.lang.getValue("View_latest_post")) + "\" title=\"" + strval(gVars.lang.getValue("View_latest_post")) + "\" border=\"0\" /></a>";
                gVars.views = intval(topic_rowset.getArrayValue(gVars.i).getValue("topic_views"));
                gVars.row_color = (!booleanval(gVars.i % 2)) ? gVars.theme.getValue("td_color1") : gVars.theme.getValue("td_color2");
                gVars.row_class = ((!booleanval(gVars.i % 2)) ? gVars.theme.getValue("td_class1") : gVars.theme.getValue("td_class2"));
                gVars.template.assign_block_vars("topicrow", new Array<Object>(new ArrayEntry("ROW_COLOR", gVars.row_color), new ArrayEntry("ROW_CLASS", gVars.row_class), new ArrayEntry("FORUM_ID", gVars.forum_id), new ArrayEntry("TOPIC_ID", gVars.topic_id), new ArrayEntry("TOPIC_FOLDER_IMG", gVars.folder_image), new ArrayEntry("TOPIC_AUTHOR", gVars.topic_author), new ArrayEntry("GOTO_PAGE", gVars.goto_page), new ArrayEntry("REPLIES", gVars.replies), new ArrayEntry("NEWEST_POST_IMG", gVars.newest_post_img), new ArrayEntry("TOPIC_TITLE", gVars.topic_title), new ArrayEntry("TOPIC_TYPE", gVars.topic_type), new ArrayEntry("VIEWS", gVars.views), new ArrayEntry("FIRST_POST_TIME", gVars.first_post_time), new ArrayEntry("LAST_POST_TIME", gVars.last_post_time), new ArrayEntry("LAST_POST_AUTHOR", gVars.last_post_author), new ArrayEntry("LAST_POST_IMG", gVars.last_post_url), new ArrayEntry("L_TOPIC_FOLDER_ALT", gVars.folder_alt), new ArrayEntry("U_VIEW_TOPIC", view_topic_url)));
            }
            topics_count = topics_count - total_announcements;
            gVars.template.assign_vars(new Array<Object>(new ArrayEntry("PAGINATION", (((functions) PhpWeb.getIncluded(functions.class, gVars, gConsts))).generate_pagination("viewforum." + gVars.phpEx + "?" + gConsts.getPOST_FORUM_URL() + "=" + gVars.forum_id + "&amp;topicdays=" + gVars.topic_days, topics_count, intval(gVars.board_config.getValue("topics_per_page")), gVars.start, true)), new ArrayEntry("PAGE_NUMBER", JStrings.sprintf(strval(gVars.lang.getValue("Page_of")), Math.floor(gVars.start / floatval(gVars.board_config.getValue("topics_per_page"))) + 1, Math.ceil(topics_count / floatval(gVars.board_config.getValue("topics_per_page"))))), new ArrayEntry("L_GOTO_PAGE", gVars.lang.getValue("Goto_page"))));
        } else {
            no_topics_msg = strval(equal(gVars.forum_row.getValue("forum_status"), gConsts.getFORUM_LOCKED()) ? gVars.lang.getValue("Forum_locked") : gVars.lang.getValue("No_topics_post_one"));
            gVars.template.assign_vars(new Array<Object>(new ArrayEntry("L_NO_TOPICS", no_topics_msg)));
            gVars.template.assign_block_vars("switch_no_topics", new Array<Object>());
        }
        gVars.template.pparse("body");
        PhpWeb.include(gVars, gConsts, net.sf.nbb2.includes.page_tail.class);
        return PhpWeb.DEFAULT_VAL;
    }
}
