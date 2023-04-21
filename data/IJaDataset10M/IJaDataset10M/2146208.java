package net.jforum.view.forum.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.entities.User;
import net.jforum.util.I18n;
import net.jforum.util.MD5;
import net.jforum.util.SafeHtml;
import net.jforum.util.image.ImageUtils;
import net.jforum.util.legacy.commons.fileupload.FileItem;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: UserCommon.java,v 1.29 2007/09/19 14:08:57 rafaelsteil Exp $
 */
public class UserCommon {

    private static final Logger logger = Logger.getLogger(UserCommon.class);

    /**
	 * Updates the user information
	 * 
	 * @param userId int The user id we are saving
     * @return List
	 */
    public static List saveUser(int userId) {
        List errors = new ArrayList();
        UserDAO um = DataAccessDriver.getInstance().newUserDAO();
        User u = um.selectById(userId);
        RequestContext request = JForumExecutionContext.getRequest();
        boolean isAdmin = SessionFacade.getUserSession().isAdmin();
        if (isAdmin) {
            String username = request.getParameter("username");
            if (username != null) {
                u.setUsername(username.trim());
            }
            if (request.getParameter("rank_special") != null) {
                u.setRankId(request.getIntParameter("rank_special"));
            }
        }
        SafeHtml safeHtml = new SafeHtml();
        u.setId(userId);
        u.setIcq(safeHtml.makeSafe(request.getParameter("icq")));
        u.setAim(safeHtml.makeSafe(request.getParameter("aim")));
        u.setMsnm(safeHtml.makeSafe(request.getParameter("msn")));
        u.setYim(safeHtml.makeSafe(request.getParameter("yim")));
        u.setFrom(safeHtml.makeSafe(request.getParameter("location")));
        u.setOccupation(safeHtml.makeSafe(request.getParameter("occupation")));
        u.setInterests(safeHtml.makeSafe(request.getParameter("interests")));
        u.setBiography(safeHtml.makeSafe(request.getParameter("biography")));
        u.setSignature(safeHtml.makeSafe(request.getParameter("signature")));
        u.setViewEmailEnabled(request.getParameter("viewemail").equals("1"));
        u.setViewOnlineEnabled(request.getParameter("hideonline").equals("0"));
        u.setNotifyPrivateMessagesEnabled(request.getParameter("notifypm").equals("1"));
        u.setNotifyOnMessagesEnabled(request.getParameter("notifyreply").equals("1"));
        u.setAttachSignatureEnabled(request.getParameter("attachsig").equals("1"));
        u.setHtmlEnabled(request.getParameter("allowhtml").equals("1"));
        u.setLang(request.getParameter("language"));
        u.setBbCodeEnabled("1".equals(request.getParameter("allowbbcode")));
        u.setSmiliesEnabled("1".equals(request.getParameter("allowsmilies")));
        u.setNotifyAlways("1".equals(request.getParameter("notify_always")));
        u.setNotifyText("1".equals(request.getParameter("notify_text")));
        String website = safeHtml.makeSafe(request.getParameter("website"));
        if (!StringUtils.isEmpty(website) && !website.toLowerCase().startsWith("http://")) {
            website = "http://" + website;
        }
        u.setWebSite(website);
        String currentPassword = request.getParameter("current_password");
        boolean isCurrentPasswordEmpty = currentPassword == null || "".equals(currentPassword.trim());
        if (isAdmin || !isCurrentPasswordEmpty) {
            if (!isCurrentPasswordEmpty) {
                currentPassword = MD5.crypt(currentPassword);
            }
            if (isAdmin || u.getPassword().equals(currentPassword)) {
                u.setEmail(safeHtml.makeSafe(request.getParameter("email")));
                String newPassword = request.getParameter("new_password");
                if (newPassword != null && newPassword.length() > 0) {
                    u.setPassword(MD5.crypt(newPassword));
                }
            } else {
                errors.add(I18n.getMessage("User.currentPasswordInvalid"));
            }
        }
        if (request.getParameter("avatardel") != null) {
            File f = new File(SystemGlobals.getAvatarDir() + "/" + u.getUsername());
            f.delete();
            u.setAvatar(null);
        }
        if (request.getObjectParameter("avatar") != null) {
            try {
                UserCommon.handleAvatar(u);
            } catch (Exception e) {
                UserCommon.logger.warn("Problems while uploading the avatar: " + e);
                errors.add(I18n.getMessage("User.avatarUploadError"));
            }
        } else if (SystemGlobals.getBoolValue(ConfigKeys.AVATAR_ALLOW_EXTERNAL_URL)) {
            String avatarUrl = request.getParameter("avatarUrl");
            if (!StringUtils.isEmpty(avatarUrl)) {
                if (avatarUrl.toLowerCase().startsWith("http://")) {
                    u.setAvatar(avatarUrl);
                } else {
                    errors.add(I18n.getMessage("User.avatarUrlShouldHaveHttp"));
                }
            }
        }
        if (errors.size() == 0) {
            um.update(u);
        }
        if (SessionFacade.getUserSession().getUserId() == userId) {
            SessionFacade.getUserSession().setLang(u.getLang());
        }
        return errors;
    }

    /**
	 * @param u User
	 */
    private static void handleAvatar(User u) {
        String fileName = u.getUsername();
        FileItem item = (FileItem) JForumExecutionContext.getRequest().getObjectParameter("avatar");
        UploadUtils uploadUtils = new UploadUtils(item);
        String extension = uploadUtils.getExtension().toLowerCase();
        int type = ImageUtils.IMAGE_UNKNOWN;
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            type = ImageUtils.IMAGE_JPEG;
        } else if (extension.equals("gif") || extension.equals("png")) {
            type = ImageUtils.IMAGE_PNG;
        }
        if (type != ImageUtils.IMAGE_UNKNOWN) {
            String avatarTmpFileName = SystemGlobals.getAvatarDir() + fileName + "_tmp." + extension;
            if (extension.toLowerCase().equals("gif")) {
                extension = "png";
            }
            String avatarFinalFileName = SystemGlobals.getAvatarDir() + fileName + "." + extension;
            uploadUtils.saveUploadedFile(avatarTmpFileName);
            int maxWidth = SystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_WIDTH);
            int maxHeight = SystemGlobals.getIntValue(ConfigKeys.AVATAR_MAX_HEIGHT);
            BufferedImage image = ImageUtils.resizeImage(avatarTmpFileName, type, maxWidth, maxHeight);
            ImageUtils.saveImage(image, avatarFinalFileName, type);
            u.setAvatar(fileName + "." + extension);
            new File(avatarTmpFileName).delete();
        }
    }
}
