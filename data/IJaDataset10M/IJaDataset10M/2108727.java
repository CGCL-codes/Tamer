package com.wangyu001.action;

import static com.wangyu001.constant.Logic.wangyuUserLogic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.sysolar.fileupload.core.SingleFileUpload;
import org.sysolar.fileupload.core.UploadChecker;
import org.sysolar.fileupload.core.UploadCheckerImpl;
import org.sysolar.fileupload.ex.ContentTypeException;
import org.sysolar.fileupload.ex.FileExtendException;
import org.sysolar.fileupload.ex.FileSizeException;
import org.sysolar.fileupload.ex.NullFileException;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.mvc.core.ActionForward;
import org.sysolar.sun.mvc.core.TransAction;
import org.sysolar.sun.mvc.support.Json;
import org.sysolar.sun.mvc.support.Pager;
import org.sysolar.sun.mvc.support.RequestWrapper;
import com.wangyu001.constant.C;
import com.wangyu001.constant.Dao;
import com.wangyu001.constant.Logic;
import com.wangyu001.el.I5;
import com.wangyu001.entity.UserBbsMsg;
import com.wangyu001.entity.UserFriend;
import com.wangyu001.entity.UserVisitor;
import com.wangyu001.entity.WangyuUser;
import com.wangyu001.logic.UserBbsMsgLogic;
import com.wangyu001.support.UserData;
import com.wangyu001.util.ImageUtils;

public class WangyuUserAction extends TransAction {

    private static double MAX_FILE_SIZE = 5 * 1024 * 1024 + 1000;

    private static String IMG_PATH = "\\img4user\\";

    private static String PHOTO_DRAG_TAG = "userPhoto";

    private static int[] PHOTO_MAX_WH = new int[] { 120, 120 };

    private static int[] PHOTO_MID_WH = new int[] { 60, 60 };

    private static int[] PHOTO_MIN_WH = new int[] { 16, 16 };

    private UserBbsMsgLogic userBbsMsgLogic = Logic.userBbsMsgLogic;

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper, String dispatch) throws Exception {
        String resp = null;
        if ("fetchUser".equals(dispatch)) {
            resp = this.fetchUser(request, response, session, wrapper);
        } else if ("fetchUserList".equals(dispatch)) {
            resp = this.fetchUserList(request, response, session, wrapper);
        } else if ("updateNickname".equals(dispatch)) {
            resp = this.updateNickname(request, response, session, wrapper);
        } else if ("updateUserSign".equals(dispatch)) {
            resp = this.updateUserSign(request, response, session, wrapper);
        } else if ("uploadUserPhoto".equals(dispatch)) {
            resp = this.uploadUserPhoto(request, response, session, wrapper);
        } else if ("updateUserPhoto".equals(dispatch)) {
            resp = this.updateUserPhoto(request, response, session, wrapper);
        } else if ("loadUserHome".equals(dispatch)) {
            resp = this.loadUserHome(request, response, session, wrapper);
        } else if ("loadFriendHome".equals(dispatch)) {
            resp = this.loadFriendHome(request, response, session, wrapper);
        } else if ("randomLook".equals(dispatch)) {
            resp = this.randomLook(request, response, session, wrapper);
            return new ActionForward(resp, ActionForward.RESP_TYPE_STRING);
        }
        return new ActionForward(resp, ActionForward.RESP_TYPE_PAGE);
    }

    /**
     * 获得用户信息。
     */
    private String fetchUser(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = wrapper.getLong("userId");
        if (null != userId) {
            Dao.wangyuUserDao.updateUserVisitedNum(userId);
            WangyuUser wangyuUser = Dao.wangyuUserDao.fetch(userId);
            userData.setLastVisitedUser(wangyuUser);
            request.setAttribute("wangyuUser", wangyuUser);
            request.setAttribute("goldUser", Dao.goldUserDao.fetch(userId));
            return C.Page.INDEX_FRIEND_HEAD;
        }
        if (null == session.getAttribute("updateUserVisitedNum")) {
            Dao.wangyuUserDao.updateUserVisitedNum(userData.getWangyuUser().getUserId());
            session.setAttribute("updateUserVisitedNum", Boolean.TRUE);
        }
        wangyuUserLogic.updateLastVisitDateById(userData.getWangyuUser().getUserId());
        request.setAttribute("wangyuUser", userData.getWangyuUser());
        request.setAttribute("goldUser", userData.getGoldUser());
        return C.Page.INDEX_USER_HEAD;
    }

    /**
     * 获得用户列表。
     */
    private String fetchUserList(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        Integer pageNum = wrapper.getPageNum();
        int sex = wrapper.getInt("sex");
        int orderBy = wrapper.getInt("orderBy");
        String isNeedNav = wrapper.getString("isNeedNav");
        Pager<WangyuUser> pager = new Pager<WangyuUser>((null == pageNum) ? 1 : pageNum, 16, 500);
        request.setAttribute("pager", Dao.wangyuUserDao.fetchTopList(pager, sex, orderBy));
        if ("true".equals(isNeedNav)) {
            return C.Page.INDEX_TOP_USER;
        }
        return C.Page.TOP_USER_LIST;
    }

    /**
     * 修改昵称
     */
    private String updateNickname(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        WangyuUser wangyuUser = new WangyuUser().fill(wrapper);
        Long userId = wangyuUser.getUserId();
        if (Dao.wangyuUserDao.updateNickname(wangyuUser)) {
            UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
            userData.setWangyuUser(Dao.wangyuUserDao.fetch(userId));
            session.setAttribute(UserData.KEY_IN_SESSION, userData);
            request.setAttribute(C.Ajax.KEY_INT_VALUE, C.Ajax.RESULT_YES);
        } else {
            request.setAttribute(C.Ajax.KEY_INT_VALUE, C.Ajax.RESULT_NO);
        }
        return C.Page.RESULT;
    }

    /**
     * 修改签名
     */
    private String updateUserSign(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        WangyuUser wangyuUser = new WangyuUser().fill(wrapper);
        Long userId = wangyuUser.getUserId();
        if (Dao.wangyuUserDao.updateUserSign(wangyuUser)) {
            UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
            userData.setWangyuUser(Dao.wangyuUserDao.fetch(userId));
            session.setAttribute(UserData.KEY_IN_SESSION, userData);
            request.setAttribute(C.Ajax.KEY_INT_VALUE, C.Ajax.RESULT_YES);
        } else {
            request.setAttribute(C.Ajax.KEY_INT_VALUE, C.Ajax.RESULT_NO);
        }
        return C.Page.RESULT;
    }

    /**
     * 上传头像，对于用户上传的比较大的图片，先压缩，然后再保存为临时文件。
     */
    private String uploadUserPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = userData.getWangyuUser().getUserId();
        String filePath = new StringBuilder().append(AppContext.getAppRootPath()).append(IMG_PATH).toString();
        String numPath = createUserImgPath(filePath, userId);
        int callBackFlag = -1;
        if (numPath == null) return C.Page.RESULT;
        StringBuilder buffer = new StringBuilder(128);
        String saveDir = buffer.append(filePath).append("\\temp\\").toString();
        buffer.delete(0, buffer.length());
        String destFileName = buffer.append(userId).append("_temp").toString();
        File imageFile = null;
        String errMsg = null;
        int imageW = 0, imageH = 0;
        try {
            UploadChecker checker = new UploadCheckerImpl(MAX_FILE_SIZE, ".BMP.GIF.JPG.JPEG.PNG", null);
            imageFile = new SingleFileUpload(request).parseRequest(saveDir, destFileName, checker);
            BufferedImage image = ImageUtils.readImage(imageFile);
            imageW = image.getWidth();
            imageH = image.getHeight();
            callBackFlag = 1;
        } catch (ContentTypeException ex) {
            errMsg = "form 表单的 enctype 属性值设置错误！请设为 multipart/form-data ！";
            callBackFlag = -1;
        } catch (NullFileException ex) {
            errMsg = "请选择上传文件 ！";
            callBackFlag = -1;
        } catch (FileSizeException ex) {
            errMsg = ex.getFileName() + " 的大小应小于 " + MAX_FILE_SIZE + "M ！";
            callBackFlag = -1;
        } catch (FileExtendException ex) {
            errMsg = ex.getFileName() + " 的文件类型不正确 ！";
            callBackFlag = -1;
        } catch (Exception ex) {
            errMsg = "未知错误 ！";
            callBackFlag = -1;
        }
        buffer.delete(0, buffer.length());
        buffer.append("parent.WangyuUserAction.uploadUserPhoto(").append(callBackFlag).append(",'").append(errMsg).append("'");
        if (null != imageFile) {
            buffer.append(",").append(userId).append(",'").append(imageFile.getName().toLowerCase()).append("',").append(imageW).append(",").append(imageH);
        }
        buffer.append(");");
        request.setAttribute(C.Ajax.KEY_OBJECT_VALUE, buffer.toString());
        return C.Page.RESULT;
    }

    /**
     * 修改头像
     */
    private String updateUserPhoto(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        System.out.println("修改头像");
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = userData.getWangyuUser().getUserId();
        String filePath = new StringBuilder().append(AppContext.getAppRootPath()).append(IMG_PATH).toString();
        String numPath = createUserImgPath(filePath, userId);
        int callBackFlag = -1;
        if (numPath == null) return C.Page.RESULT;
        String minFilePath = new StringBuilder().append(filePath).append("\\min\\").append(numPath).append("\\").append(userId).append(".jpg").toString();
        String midFilePath = new StringBuilder().append(filePath).append("\\mid\\").append(numPath).append("\\").append(userId).append(".jpg").toString();
        String maxFilePath = new StringBuilder().append(filePath).append("\\max\\").append(numPath).append("\\").append(userId).append(".jpg").toString();
        String destFileName = new StringBuilder().append(filePath).append("\\temp\\").append(wrapper.getString(PHOTO_DRAG_TAG + "UpdateFilename")).toString();
        System.out.println(destFileName);
        File imageFile = new File(destFileName);
        BufferedImage image = ImageUtils.readImage(imageFile);
        String setting = wrapper.getString(PHOTO_DRAG_TAG + "UpdateSetting");
        System.out.println(setting + "," + (image == null));
        String errMsg = null;
        double height;
        int topX, topY;
        int originatorWidth, originatorHeight, originatorTopX, originatorTopY, originatorMaxWidth, originatorMaxHeight;
        height = topX = topY = 0;
        originatorWidth = originatorHeight = originatorTopX = originatorTopY = originatorMaxWidth = originatorMaxHeight = 0;
        if (setting.indexOf(",") > -1) {
            String[] arguments = setting.split(",");
            height = Double.parseDouble(arguments[1]);
            topX = Math.abs(Integer.parseInt(arguments[2]));
            topY = Math.abs(Integer.parseInt(arguments[3]));
        }
        if (image == null) {
            callBackFlag = -1;
        } else {
            BufferedImage maxImg, midImg, minImg, newImage;
            double scale = image.getHeight() / height;
            originatorWidth = image.getWidth();
            originatorHeight = image.getHeight();
            originatorTopX = (int) Math.round(topX * scale);
            originatorTopY = (int) Math.round(topY * scale);
            originatorMaxWidth = (int) Math.round(PHOTO_MAX_WH[0] * scale);
            originatorMaxHeight = (int) Math.round(PHOTO_MAX_WH[1] * scale);
            try {
                int newWidth = originatorWidth - originatorTopX;
                int newHeight = originatorHeight - originatorTopY;
                newWidth = newWidth > originatorMaxWidth ? originatorMaxWidth : newWidth;
                newHeight = newHeight > originatorMaxHeight ? originatorMaxHeight : newHeight;
                newImage = image.getSubimage(originatorTopX, originatorTopY, newWidth, newHeight);
                maxImg = ImageUtils.resizeImage(newImage, ImageUtils.IMAGE_JPEG, PHOTO_MAX_WH[0], PHOTO_MAX_WH[1]);
                ImageUtils.saveImage(maxImg, maxFilePath, ImageUtils.IMAGE_JPEG);
                midImg = ImageUtils.resizeImage(newImage, ImageUtils.IMAGE_JPEG, PHOTO_MID_WH[0], PHOTO_MID_WH[1]);
                ImageUtils.saveImage(midImg, midFilePath, ImageUtils.IMAGE_JPEG);
                minImg = ImageUtils.resizeImage(newImage, ImageUtils.IMAGE_JPEG, PHOTO_MIN_WH[0], PHOTO_MIN_WH[1]);
                ImageUtils.saveImage(minImg, minFilePath, ImageUtils.IMAGE_JPEG);
                boolean resizeTempFile = false;
                int maxTempSize = 600;
                if (image.getHeight() > maxTempSize) {
                    double tempWidth = 1.0 * maxTempSize * image.getWidth() / image.getHeight();
                    image = ImageUtils.resizeImage(image, ImageUtils.IMAGE_JPEG, maxTempSize, (int) tempWidth);
                    resizeTempFile = true;
                }
                if (image.getHeight() > maxTempSize) {
                    double tempHeight = 1.0 * maxTempSize * image.getHeight() / image.getWidth();
                    image = ImageUtils.resizeImage(image, ImageUtils.IMAGE_JPEG, (int) tempHeight, maxTempSize);
                    resizeTempFile = true;
                }
                if (resizeTempFile) {
                    ImageUtils.saveImage(image, imageFile.getAbsolutePath(), ImageUtils.IMAGE_JPEG);
                }
                callBackFlag = 1;
            } catch (IOException e) {
                callBackFlag = -1;
                errMsg = "图片转换错误 ！";
            }
            if (Dao.wangyuUserDao.updateUserPhoto(userData.getWangyuUser())) {
                userData.setWangyuUser(Dao.wangyuUserDao.fetch(userId));
                session.setAttribute(UserData.KEY_IN_SESSION, userData);
            }
        }
        System.out.println("parent.WangyuUserAction.updateUserPhoto(" + userId + "," + callBackFlag + ",'" + errMsg + "');");
        request.setAttribute(C.Ajax.KEY_OBJECT_VALUE, "parent.WangyuUserAction.updateUserPhoto(" + userId + "," + callBackFlag + ",'" + errMsg + "');");
        return C.Page.RESULT;
    }

    private String createUserImgPath(String filePath, Long userId) {
        String numPath = String.valueOf(I5.getPath(userId));
        String minPath = new StringBuilder().append(filePath).append("\\min\\").append(numPath).append("\\").toString();
        if (!new File(minPath).exists()) {
            new File(new StringBuilder().append(filePath).append("\\temp\\").toString()).mkdirs();
            new File(new StringBuilder().append(filePath).append("\\max\\").append(numPath).append("\\").toString()).mkdirs();
            new File(new StringBuilder().append(filePath).append("\\mid\\").append(numPath).append("\\").toString()).mkdirs();
            new File(minPath).mkdirs();
        }
        return numPath;
    }

    private String loadUserHome(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long userId = userData.getWangyuUser().getUserId();
        Integer pageNum = wrapper.getPageNum();
        Pager<UserBbsMsg> pager = new Pager<UserBbsMsg>((null == pageNum) ? 1 : pageNum, 10, 500);
        pager.setTotalNum(-1);
        Pager<UserBbsMsg> userBbsMsgList = userBbsMsgLogic.fetchUserBbsMsgList(userId, userId, 2, pager);
        request.setAttribute("pager", userBbsMsgList);
        request.setAttribute("atHome", 1);
        List<WangyuUser> imgUserList = Dao.wangyuUserDao.fetchListForImg(19);
        List<UserVisitor> userVisitorList = Dao.userVisitorDao.fetchLatest(userId);
        List<UserFriend> userFriendList = Dao.userFriendDao.fetchLatest(userId);
        request.setAttribute("imgUserList", imgUserList);
        request.setAttribute("userVisitorList", userVisitorList);
        request.setAttribute("userFriendList", userFriendList);
        return C.Page.INDEX_USER_HOME;
    }

    private String loadFriendHome(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        Long currentUserId = userData.getWangyuUser().getUserId();
        Long userId = wrapper.getLong("userId");
        Integer pageNum = wrapper.getPageNum();
        Pager<UserBbsMsg> pager = new Pager<UserBbsMsg>((null == pageNum) ? 1 : pageNum, 10, 500);
        pager.setTotalNum(-1);
        Pager<UserBbsMsg> userBbsMsgList = userBbsMsgLogic.fetchUserBbsMsgList(userId, currentUserId, 1, pager);
        request.setAttribute("pager", userBbsMsgList);
        List<WangyuUser> imgUserList = Dao.wangyuUserDao.fetchListForImg(19);
        request.setAttribute("imgUserList", imgUserList);
        return C.Page.INDEX_FRIEND_HOME;
    }

    /**
     * “随便看看”，方便用户随机访问其它用户的页面。
     * 
     * @return
     * @throws Exception
     */
    private String randomLook(HttpServletRequest request, HttpServletResponse response, HttpSession session, RequestWrapper wrapper) throws Exception {
        UserData userData = (UserData) session.getAttribute(UserData.KEY_IN_SESSION);
        WangyuUser wangyuUser = userData.getWangyuUser();
        Long currentUserId = null;
        if (null != wangyuUser) {
            currentUserId = wangyuUser.getUserId();
        }
        return Json.toJs("userListForRandomLook", wangyuUserLogic.randomLook(currentUserId, 10));
    }
}
