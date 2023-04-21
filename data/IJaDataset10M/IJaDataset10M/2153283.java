package com.laoer.bbscs.web.servlet;

import java.net.URLDecoder;
import java.net.URLEncoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.laoer.bbscs.bean.UserInfo;
import com.laoer.bbscs.comm.Constant;
import com.laoer.bbscs.comm.DES;
import com.laoer.bbscs.comm.Util;
import com.laoer.bbscs.service.config.SysConfig;

public class UserCookieNew {

    private static final Log logger = LogFactory.getLog(UserCookie.class);

    private static final String PASS_USERNAME_KEY = "PASS_USERNAME";

    private static final String PASS_USERNAME_DES_KEY = "PASS_USERNAME_DES";

    private static final String BBSCS_UID = "UID";

    private static final String BBSCS_UID_DES = "UIDD";

    private static final String BBSCS_CKEY = "CK";

    private static final String BBSCS_AUTHCODE = "AC";

    private HttpServletRequest request;

    private HttpServletResponse response;

    private SysConfig sysConfig;

    private DES des;

    private String pusername = "";

    private String pusernamedes = "";

    private String userName = "";

    private String userNameDES = "";

    private String authCode = "";

    private String[] userCookieArray = { "10", "20", "0", "0", "0", "0", "0", "0", "0", "-", "-", "0", "GMT+08:00" };

    public UserCookieNew(HttpServletRequest request, HttpServletResponse response, SysConfig sysConfig) {
        this.request = request;
        this.response = response;
        this.sysConfig = sysConfig;
        try {
            des = new DES(DES._DESede);
        } catch (Exception ex) {
            logger.error(ex);
        }
        this.getCookies();
    }

    private void getCookies() {
        Cookie cookies[] = this.request.getCookies();
        Cookie sCookie = null;
        byte[] buf;
        try {
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    sCookie = cookies[i];
                    if (this.sysConfig.isUsePass()) {
                        if (sCookie.getName().equals(PASS_USERNAME_KEY)) {
                            this.pusername = sCookie.getValue();
                        }
                        if (sCookie.getName().equals(PASS_USERNAME_DES_KEY)) {
                            if (StringUtils.isNotBlank(sCookie.getValue())) {
                                buf = Util.base64decodebyte(sCookie.getValue());
                                byte[] dec = des.decode(buf, Util.base64decodebyte(this.sysConfig.getCookieKey()));
                                this.pusernamedes = new String(dec);
                            }
                        }
                    }
                    if (sCookie.getName().equals(BBSCS_UID)) {
                        this.userName = sCookie.getValue();
                    }
                    if (sCookie.getName().equals(BBSCS_UID_DES)) {
                        if (StringUtils.isNotBlank(sCookie.getValue())) {
                            buf = Util.base64decodebyte(URLDecoder.decode(sCookie.getValue(), Constant.CHARSET));
                            byte[] dec = des.decode(buf, Util.base64decodebyte(this.sysConfig.getCookieKey()));
                            this.userNameDES = new String(dec);
                        }
                    }
                    if (sCookie.getName().equals(BBSCS_AUTHCODE)) {
                        if (StringUtils.isNotBlank(sCookie.getValue())) {
                            buf = Util.base64decodebyte(URLDecoder.decode(sCookie.getValue(), Constant.CHARSET));
                            byte[] dec = des.decode(buf, Util.base64decodebyte(this.sysConfig.getCookieKey()));
                            this.authCode = new String(dec);
                        }
                    }
                    if (sCookie.getName().equals(BBSCS_CKEY)) {
                        if (StringUtils.isNotBlank(sCookie.getValue())) {
                            buf = Util.base64decodebyte(URLDecoder.decode(sCookie.getValue(), Constant.CHARSET));
                            byte[] dec = des.decode(buf, Util.base64decodebyte(this.sysConfig.getCookieKey()));
                            String ck = new String(dec);
                            String[] cks = ck.split(",");
                            if (cks != null && cks.length == userCookieArray.length) {
                                for (int j = 0; j < cks.length; j++) {
                                    userCookieArray[j] = cks[j];
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void addC(String name, String value, int maxage) {
        Cookie cookies = new Cookie(name, value);
        cookies.setPath(this.sysConfig.getCookiePath());
        cookies.setMaxAge(maxage);
        if (StringUtils.isNotBlank(this.sysConfig.getCookieDomain())) {
            cookies.setDomain(this.sysConfig.getCookieDomain());
        }
        this.response.addCookie(cookies);
    }

    public void addDES(String name, String value, int maxage) {
        try {
            des.setKey(Util.base64decodebyte(this.sysConfig.getCookieKey()));
            byte[] enc = des.encode(value.getBytes());
            value = URLEncoder.encode(Util.base64Encode(enc), Constant.CHARSET);
            logger.debug("des value:" + value);
            Cookie cookies = new Cookie(name, value);
            cookies.setPath(this.sysConfig.getCookiePath());
            cookies.setMaxAge(maxage);
            if (StringUtils.isNotBlank(this.sysConfig.getCookieDomain())) {
                cookies.setDomain(this.sysConfig.getCookieDomain());
            }
            this.response.addCookie(cookies);
        } catch (Exception ex) {
            logger.error("addDES(String name, String value)" + ex);
        }
    }

    public void addCookiesArray(int maxage) {
        StringBuffer sb = new StringBuffer();
        int len = userCookieArray.length;
        for (int i = 0; i < len; i++) {
            sb.append(userCookieArray[i]);
            if (i != (len - 1)) {
                sb.append(",");
            }
        }
        this.addDES(BBSCS_CKEY, sb.toString(), maxage);
    }

    public void addCookies(UserInfo ui) {
        this.setPostPerNum(ui.getPostPerNum());
        this.setForumPerNum(ui.getForumPerNum());
        this.setTimeZone(ui.getTimeZone());
        this.setForumViewMode(ui.getForumViewMode());
        this.setEditType(ui.getEditType());
        this.setLastActiveTime(System.currentTimeMillis());
        this.addCookiesArray(-1);
        this.userName = ui.getUserName();
        this.addC(BBSCS_UID, this.userName, -1);
        this.addDES(BBSCS_UID_DES, this.userName, -1);
    }

    public void addGuestCookies(String gUserName) {
        logger.debug(gUserName);
        this.userName = gUserName;
        this.addC(BBSCS_UID, gUserName, 300);
        this.addDES(BBSCS_UID_DES, gUserName, 300);
    }

    public void addLastNoteSendTime() {
        this.setLastSendNoteTime(System.currentTimeMillis());
        this.addCookiesArray(-1);
    }

    public void addLastPostTime() {
        this.setLastPostTime(System.currentTimeMillis());
        this.addCookiesArray(-1);
    }

    public void addForumViewMode(int vm) {
        this.setForumViewMode(vm);
        this.addCookiesArray(-1);
    }

    public void addLastActiveTime() {
        this.setLastActiveTime(System.currentTimeMillis());
        this.addCookiesArray(-1);
    }

    public void addAddedOnlineTime(long time) {
        this.setAddedOnlineTime(time);
        this.addCookiesArray(-1);
    }

    public void addAddedOnlineHour(long hour) {
        this.setAddedOnlineHour(hour);
        this.addCookiesArray(-1);
    }

    public void addValidateCode(String validateCode) {
        if (StringUtils.isBlank(validateCode)) {
            validateCode = "-";
        }
        this.setValidateCode(validateCode);
        this.addCookiesArray(-1);
    }

    public void addBoardPass(String boardPass) {
        if (StringUtils.isBlank(boardPass)) {
            boardPass = "-";
        }
        this.setBoardPass(boardPass);
        this.addCookiesArray(-1);
    }

    public void addBid(long bid) {
        this.setBid(bid);
        this.addCookiesArray(-1);
    }

    public void addAuthCode(String authCode) {
        this.addDES(BBSCS_AUTHCODE, authCode, -1);
    }

    public void removeAuthCode() {
        this.addC(BBSCS_AUTHCODE, "", 0);
    }

    public boolean isLoginPass() {
        if (StringUtils.isNotBlank(this.pusername) && StringUtils.isNotBlank(this.pusernamedes) && this.pusername.equals(this.pusernamedes)) {
            return true;
        }
        return false;
    }

    public boolean isLoginUser() {
        if (StringUtils.isNotBlank(this.userName) && StringUtils.isNotBlank(this.userNameDES) && this.userName.equals(this.userNameDES)) {
            return true;
        }
        return false;
    }

    public void removeAllCookies() {
        this.addC(BBSCS_UID, "", 0);
        this.addC(BBSCS_UID_DES, "", 0);
        this.addC(BBSCS_CKEY, "", 0);
        this.addC(BBSCS_AUTHCODE, "", 0);
    }

    public long getAddedOnlineHour() {
        return NumberUtils.toLong(userCookieArray[8], 0);
    }

    public void setAddedOnlineHour(long addedOnlineHour) {
        userCookieArray[8] = String.valueOf(addedOnlineHour);
    }

    public long getAddedOnlineTime() {
        return NumberUtils.toLong(userCookieArray[7], 0);
    }

    public void setAddedOnlineTime(long addedOnlineTime) {
        userCookieArray[7] = String.valueOf(addedOnlineTime);
    }

    public long getBid() {
        return NumberUtils.toLong(userCookieArray[11], 0);
    }

    public void setBid(long bid) {
        userCookieArray[11] = String.valueOf(bid);
    }

    public String getBoardPass() {
        return userCookieArray[10];
    }

    public void setBoardPass(String boardPass) {
        userCookieArray[10] = boardPass;
    }

    public int getEditType() {
        return NumberUtils.toInt(userCookieArray[5], 0);
    }

    public void setEditType(int editType) {
        userCookieArray[5] = String.valueOf(editType);
    }

    public int getForumPerNum() {
        return NumberUtils.toInt(userCookieArray[1], 20);
    }

    public void setForumPerNum(int forumPerNum) {
        userCookieArray[1] = String.valueOf(forumPerNum);
    }

    public int getForumViewMode() {
        return NumberUtils.toInt(userCookieArray[2], 0);
    }

    public void setForumViewMode(int forumViewMode) {
        userCookieArray[2] = String.valueOf(forumViewMode);
    }

    public long getLastActiveTime() {
        return NumberUtils.toLong(userCookieArray[6], 0);
    }

    public void setLastActiveTime(long lastActiveTime) {
        userCookieArray[6] = String.valueOf(lastActiveTime);
    }

    public long getLastPostTime() {
        return NumberUtils.toLong(userCookieArray[4], 0);
    }

    public void setLastPostTime(long lastPostTime) {
        userCookieArray[4] = String.valueOf(lastPostTime);
    }

    public long getLastSendNoteTime() {
        return NumberUtils.toLong(userCookieArray[3], 0);
    }

    public void setLastSendNoteTime(long lastSendNoteTime) {
        userCookieArray[3] = String.valueOf(lastSendNoteTime);
    }

    public int getPostPerNum() {
        return NumberUtils.toInt(userCookieArray[0], 20);
    }

    public void setPostPerNum(int postPerNum) {
        userCookieArray[0] = String.valueOf(postPerNum);
    }

    public String getTimeZone() {
        return userCookieArray[12];
    }

    public void setTimeZone(String timeZone) {
        userCookieArray[12] = timeZone;
    }

    public String getValidateCode() {
        return userCookieArray[9];
    }

    public void setValidateCode(String validateCode) {
        userCookieArray[9] = validateCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userID) {
        this.userName = userID;
    }

    public String getPusername() {
        return pusername;
    }

    public void setPusername(String pusername) {
        this.pusername = pusername;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
