package com.mobolus.servlets;

import java.io.*;
import java.sql.Connection;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.shared.servlets.*;
import com.shared.beans.*;
import com.mobolus.beans.*;

public class HndModuleItems extends HndMobolus {

    public static final String ADD_MODULE_ITEM = "ADD_MODULE_ITEM";

    public static final String VIEW_MODULE_ITEM = "VIEW_MODULE_ITEM";

    public static final String MODULE_ITEM_ID = "MODULE_ITEM_ID";

    public static final String DELETE_MODULE_ITEM = "DELETE_MODULE_ITEM";

    public static final String MODULE_ITEM_MODULE_ID = "MODULE_ITEM_MODULE_ID";

    public boolean doAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (super.doAll(request, response)) {
            Connection conn = null;
            try {
                conn = Database.getConnection();
                HttpSession session = request.getSession();
                String cmd = request.getParameter(COMMAND);
                HashMap userRow = (HashMap) session.getAttribute(USER);
                String userId = userRow.get("u_user_id").toString();
                String moduleIdStr = request.getParameter(MODULE_ITEM_MODULE_ID);
                int moduleId = Integer.parseInt(moduleIdStr);
                if (VIEW_MODULE_ITEM.equals(cmd)) {
                    if (moduleId == BnMobolus.MDL_CONVERSATIONS) {
                        doForward("HndConversation.serv?" + COMMAND + "=" + HndConversation.VIEW_CONVERSATION + "&id=" + request.getParameter("id"), request, response);
                    } else if (moduleId == BnMobolus.MDL_PHOTOS) {
                        doForward("HndPhoto.serv?" + COMMAND + "=" + HndPhoto.VIEW_PHOTO + "&id=" + request.getParameter("id"), request, response);
                    } else if (moduleId == BnMobolus.MDL_PHOTO_ALBUMS) {
                        doForward("HndPhoto.serv?" + COMMAND + "=" + HndPhoto.VIEW_PHOTO_ALBUM + "&id=" + request.getParameter("id"), request, response);
                    } else if (moduleId == BnMobolus.MDL_VIDEOS) {
                        doForward("HndVideo.serv?" + COMMAND + "=" + HndVideo.VIEW_VIDEO + "&id=" + request.getParameter("id"), request, response);
                    } else {
                        Log.msg("HndModuleItems", "Unknown moduleId: " + moduleId);
                    }
                    return true;
                } else if (ADD_MODULE_ITEM.equals(cmd)) {
                    if (moduleId == BnMobolus.MDL_CONVERSATIONS) {
                        doForward("AddMessageThread.jsp", request, response);
                    } else if (moduleId == BnMobolus.MDL_PHOTOS) {
                    } else if (moduleId == BnMobolus.MDL_PHOTO_ALBUMS) {
                        String ua = request.getHeader("User-Agent");
                        doForward("AddPhotoAlbum.jsp", request, response);
                    } else if (moduleId == BnMobolus.MDL_VIDEOS) {
                        doForward("AddVideo.jsp", request, response);
                    } else {
                        Log.msg("HndModuleItems", "Unknown moduleId: " + moduleId);
                    }
                    return true;
                } else if (DELETE_MODULE_ITEM.equals(cmd)) {
                    if (moduleId == BnMobolus.MDL_CONVERSATIONS) {
                        doForward("HndConversation.serv?" + COMMAND + "=" + HndConversation.DELETE_CONVERSATION + "&id=" + request.getParameter("id"), request, response);
                    } else if (moduleId == BnMobolus.MDL_PHOTOS) {
                        String photoId = Util.escapeForHTML(request.getParameter("id"));
                        String SQL = "SELECT * FROM photos,photo_albums WHERE ph_photo_id = '" + photoId + "' AND ph_photo_album_id = pa_photo_album_id";
                        HashMap photoData = Database.getSingleResult(SQL, conn);
                        String forwardTo = "HndPhoto.serv?" + COMMAND + "=" + HndPhoto.DELETE_PHOTO + "&id=" + photoId + "&" + HndPhoto.PHOTO_ALBUM_ID + "=" + photoData.get("pa_photo_album_id") + HndPhoto.PHOTO_OWNER + "=" + photoData.get("pa_owner") + "&ext=" + photoData.get("ph_ext");
                        doForward(forwardTo, request, response);
                    } else if (moduleId == BnMobolus.MDL_PHOTO_ALBUMS) {
                        doForward("HndPhoto.serv?" + COMMAND + "=" + HndPhoto.DELETE_PHOTO_ALBUM + "&id=" + request.getParameter("id"), request, response);
                    } else if (moduleId == BnMobolus.MDL_VIDEOS) {
                        doForward("HndVideo.serv?" + COMMAND + "=" + HndVideo.DELETE_VIDEO + "&id=" + request.getParameter("id"), request, response);
                    } else {
                        Log.msg("HndModuleItems", "Unknown moduleId: " + moduleId);
                    }
                    return true;
                }
            } catch (Exception e) {
                Log.msg("HndModuleItems", e);
            } finally {
                Database.closeConnection(conn);
            }
            return true;
        }
        return false;
    }
}
