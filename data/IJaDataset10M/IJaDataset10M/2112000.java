package com.hk.web.user.action;

import java.io.File;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.City;
import com.hk.bean.Notice;
import com.hk.bean.Randnum;
import com.hk.bean.RegCode;
import com.hk.bean.ScoreLog;
import com.hk.bean.User;
import com.hk.bean.UserBindInfo;
import com.hk.bean.UserCard;
import com.hk.bean.UserMailAuth;
import com.hk.bean.UserNoticeInfo;
import com.hk.bean.UserOtherInfo;
import com.hk.bean.UserProtect;
import com.hk.bean.UserTool;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.HkUtil;
import com.hk.frame.util.image.ImageException;
import com.hk.frame.util.image.JMagickUtil;
import com.hk.frame.util.image.NotPermitImageFormatException;
import com.hk.frame.util.image.OutOfSizeException;
import com.hk.frame.util.mail.MailUtil;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.frame.web.view.CssColor;
import com.hk.frame.web.view.CssColorUtil;
import com.hk.frame.web.view.ShowMode;
import com.hk.frame.web.view.ShowModeUtil;
import com.hk.frame.web.view.UrlMode;
import com.hk.frame.web.view.UrlModeUtil;
import com.hk.sms.SmsClient;
import com.hk.sms2.SmsPortProcessAble;
import com.hk.svr.RegCodeService;
import com.hk.svr.UserCardService;
import com.hk.svr.UserMailAuthService;
import com.hk.svr.UserService;
import com.hk.svr.pub.Err;
import com.hk.svr.pub.HkLog;
import com.hk.svr.pub.ImageConfig;
import com.hk.svr.pub.ProtectBean;
import com.hk.svr.pub.ProtectConfig;
import com.hk.svr.pub.ScoreConfig;
import com.hk.svr.pub.ZoneUtil;
import com.hk.svr.user.exception.CreateRandnumException;
import com.hk.svr.user.exception.EmailDuplicateException;
import com.hk.svr.user.exception.IllegalEmailException;
import com.hk.svr.user.exception.IllegalMobileException;
import com.hk.svr.user.exception.MobileDuplicateException;
import com.hk.svr.user.exception.MsnDuplicateException;
import com.hk.svr.user.exception.ProtectValueErrorException;
import com.hk.svr.user.exception.UserNotExistException;
import com.hk.svr.user.validate.UserValidate;
import com.hk.web.pub.action.BaseAction;
import com.hk.web.util.HkStatus;
import com.hk.web.util.HkWebUtil;

@Component("/user/set/set")
public class SetAction extends BaseAction {

    private final Log log = LogFactory.getLog(SetAction.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CssColorUtil cssColorUtil;

    @Autowired
    private ShowModeUtil showModeUtil;

    @Autowired
    private UrlModeUtil urlModeUtil;

    @Autowired
    private UserCardService userCardService;

    @Autowired
    private SmsClient smsClient;

    @Autowired
    private RegCodeService regCodeService;

    @Autowired
    private UserMailAuthService userMailAuthService;

    @Autowired
    private MailUtil mailUtil;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        long userId = this.getLoginUser(req).getUserId();
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        RegCode regCode = this.regCodeService.getRegCodeByUserId(userId);
        HkStatus hkStatus = HkWebUtil.getHkStatus(req);
        List<CssColor> csslist = this.cssColorUtil.getCssColorList();
        List<ShowMode> showlist = this.showModeUtil.getModeList();
        List<UrlMode> urlModelist = this.urlModeUtil.getUrlModeList();
        User user = this.userService.getUser(userId);
        req.setAttribute("validateemail", userOtherInfo.isAuthedMail());
        req.setAttribute("mobileBind", userOtherInfo.isMobileAlreadyBind());
        req.setAttribute("user", user);
        req.setAttribute("regCode", regCode);
        req.setAttribute("urlModelist", urlModelist);
        req.setAttribute("csslist", csslist);
        req.setAttribute("showlist", showlist);
        req.setAttribute("hkStatus", hkStatus);
        return "/WEB-INF/page/user/set/set.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String web(HkRequest req, HkResponse resp) throws Exception {
        return "r:/user/set/set_tosetinfoweb.do";
    }

    /**
	 * 转载喇叭的火友
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String lesschar(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        UserTool userTool = this.userService.getUserTool(loginUser.getUserId());
        if (userTool == null) {
            userTool = UserTool.createDefault(loginUser.getUserId());
        }
        req.setAttribute("userTool", userTool);
        return "/WEB-INF/page/user/set/lesschar.jsp";
    }

    /**
	 * 转载喇叭的火友
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setlesscharfilter(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        byte showReply = req.getByte("showReply");
        UserTool userTool = this.userService.checkUserTool(loginUser.getUserId());
        userTool.setShowReply(showReply);
        int code = userTool.validate();
        if (code == Err.SUCCESS) {
            this.userService.updateuserTool(userTool);
        }
        req.setSessionText("op.exeok");
        return "r:/square.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetregcode(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        long userId = loginUser.getUserId();
        RegCode o = (RegCode) req.getAttribute("o");
        boolean hasone = false;
        if (o == null) {
            o = this.regCodeService.getRegCodeByUserId(userId);
            if (o != null) {
                hasone = true;
            }
        }
        int count = this.regCodeService.countNoUseRegCode();
        int size = 10;
        int begin = 0;
        if (count > size) {
            begin = DataUtil.getRandomNumber(count - size);
        }
        List<RegCode> tuilist = this.regCodeService.getRegCodeList(begin, size);
        req.setAttribute("tuilist", tuilist);
        req.setAttribute("hasone", hasone);
        req.setAttribute("o", o);
        return "/WEB-INF/page/user/set/setregcode.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setregcode(HkRequest req, HkResponse resp) throws Exception {
        User loginUser = this.getLoginUser(req);
        long userId = loginUser.getUserId();
        String name = req.getString("name");
        RegCode o = this.regCodeService.getRegCodeByUserId(userId);
        if (o == null) {
            o = new RegCode();
            o.setName(name);
            o.setObjId(loginUser.getUserId());
            o.setObjType(RegCode.OBJTYPE_USER);
            int code = o.validate();
            req.setAttribute("o", o);
            if (code != Err.SUCCESS) {
                req.setText(code + "");
                return "/user/set/set_tosetregcode.do";
            }
            this.regCodeService.createRegCode(o);
            req.setSessionText("func.regcode_create_ok");
        }
        return "r:/user/set/set_tosetregcode.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetNickName(HkRequest req, HkResponse resp) throws Exception {
        return "/WEB-INF/page/user/set/setnickname.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String toSetMsn(HkRequest req, HkResponse resp) {
        if (req.getAttribute("info") == null) {
            UserBindInfo info = this.userService.getUserBindInfo(this.getLoginUser(req).getUserId());
            req.setAttribute("info", info);
        }
        return "/WEB-INF/page/user/set/setmsn.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetmsnweb(HkRequest req, HkResponse resp) {
        if (req.getAttribute("info") == null) {
            UserBindInfo info = this.userService.getUserBindInfo(this.getLoginUser(req).getUserId());
            req.setAttribute("info", info);
        }
        req.setAttribute("op_func", 5);
        return this.getWeb3Jsp("user/set/setmsn.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setMsn(HkRequest req, HkResponse resp) {
        String msn = req.getString("msn");
        if (isEmpty(msn) || msn.length() > 50) {
            req.setMessage("请输入正确的msn地址");
            return "/user/set/set_toSetMsn.do";
        }
        long userId = this.getLoginUser(req).getUserId();
        UserBindInfo info = this.userService.getUserBindInfo(userId);
        if (info == null) {
            info = new UserBindInfo();
            info.setUserId(userId);
        }
        info.setMsn(msn);
        try {
            this.userService.updateUserBindInfo(info);
        } catch (MsnDuplicateException e) {
            req.setMessage(msn + "已经被其他账号绑定");
            return "/user/set/set_toSetMsn.do";
        }
        return "r:/user/set/set_setMsnok.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setmsnweb(HkRequest req, HkResponse resp) {
        String msn = req.getString("msn");
        if (isEmpty(msn) || msn.length() > 50) {
            return this.initError(req, Err.MSN_ERROR, -1, null, "msn", "onmsnerror", null);
        }
        long userId = this.getLoginUser(req).getUserId();
        UserBindInfo info = this.userService.getUserBindInfo(userId);
        if (info == null) {
            info = new UserBindInfo();
            info.setUserId(userId);
        }
        info.setMsn(msn);
        try {
            this.userService.updateUserBindInfo(info);
            this.setOpFuncSuccessMsg(req);
            return this.initSuccess(req, "msn", "onmsnsuccess", null);
        } catch (MsnDuplicateException e) {
            return this.initError(req, Err.MSN_ALREADY_EXIST, -1, null, "msn", "onmsnerror", null);
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setMsnok(HkRequest req, HkResponse resp) {
        return "/WEB-INF/page/user/set/setmsnok.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetInfo(HkRequest req, HkResponse resp) {
        if (req.getAttribute("info") == null) {
            UserOtherInfo info = this.userService.getUserOtherInfo(this.getLoginUser(req).getUserId());
            req.setAttribute("info", info);
        }
        req.setAttribute("user", this.userService.getUser(this.getLoginUser(req).getUserId()));
        return "/WEB-INF/page/user/set/setinfo.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetinfoweb(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        User user = this.userService.getUser(loginUser.getUserId());
        UserOtherInfo info = this.userService.getUserOtherInfo(loginUser.getUserId());
        req.setAttribute("user", user);
        req.setAttribute("info", info);
        int provinceId = 0;
        City city = ZoneUtil.getCity(user.getCityId());
        if (city != null) {
            provinceId = city.getProvinceId();
        }
        req.setAttribute("provinceId", provinceId);
        req.setAttribute("op_func", 2);
        return this.getWeb3Jsp("user/set/info.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setinfoweb(HkRequest req, HkResponse resp) {
        String nickName = req.getString("nickName");
        nickName = DataUtil.toHtmlRow(nickName);
        User loginUser = this.getLoginUser(req);
        int code = User.validateNickName(nickName);
        if (code != Err.SUCCESS) {
            return this.initError(req, code, "edit");
        }
        if (!this.userService.updateNickName(loginUser.getUserId(), nickName)) {
            return this.initError(req, Err.NICKNAME_DUPLICATE, "edit");
        }
        byte sex = req.getByte("sex");
        this.userService.updateSex(loginUser.getUserId(), sex);
        int pcityId = req.getInt("pcityId");
        this.userService.updateCityId(loginUser.getUserId(), pcityId);
        String name = req.getString("name");
        String intro = req.getString("intro");
        UserOtherInfo info = this.userService.getUserOtherInfo(loginUser.getUserId());
        info.setName(DataUtil.toHtmlRow(name));
        info.setIntro(DataUtil.toHtml(intro));
        code = info.validate();
        if (code != Err.SUCCESS) {
            return this.initError(req, code, "edit");
        }
        this.userService.updateUserOtherInfo(info);
        UserCard userCard = this.userCardService.getUserCard(loginUser.getUserId());
        if (userCard != null) {
            userCard.setName(DataUtil.toHtmlRow(name));
            this.userCardService.updateUserCard(userCard);
        }
        loginUser = this.userService.getUser(loginUser.getUserId());
        this.setOpFuncSuccessMsg(req);
        return this.initSuccess(req, "edit");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setInfo(HkRequest req, HkResponse resp) {
        String name = req.getString("name");
        String intro = req.getString("intro");
        String cityCode = req.getString("cityCode");
        byte sex = req.getByte("sex");
        int birthdayDate = req.getInt("birthdayDate");
        int birthdayMonth = req.getInt("birthdayMonth");
        User loginUser = this.getLoginUser(req);
        UserOtherInfo info = this.userService.getUserOtherInfo(loginUser.getUserId());
        info.setName(DataUtil.toHtmlRow(name));
        info.setCityCode(DataUtil.toHtmlRow(cityCode));
        info.setBirthdayDate(birthdayDate);
        info.setBirthdayMonth(birthdayMonth);
        info.setIntro(DataUtil.toHtml(intro));
        req.setAttribute("info", info);
        if (!isEmpty(name) && name.length() > 10) {
            req.setMessage("请正确输入姓名");
            return "/user/set/set_toSetInfo.do";
        }
        if (birthdayDate != 0 || birthdayMonth != 0) {
            if (birthdayDate < 1 || birthdayDate > 31) {
                req.setMessage("请正确输入生日");
                return "/user/set/set_toSetInfo.do";
            }
            if (birthdayMonth < 1 || birthdayMonth > 12) {
                req.setMessage("请正确输入生日");
                return "/user/set/set_toSetInfo.do";
            }
        }
        if (intro != null && intro.length() > 200) {
            req.setMessage("个人介绍不能超过200个字符");
            return "/user/set/set_toSetInfo.do";
        }
        this.userService.updateSex(loginUser.getUserId(), sex);
        this.userService.updateUserOtherInfo(info);
        UserCard userCard = this.userCardService.getUserCard(loginUser.getUserId());
        if (userCard != null) {
            userCard.setName(DataUtil.toHtmlRow(name));
            this.userCardService.updateUserCard(userCard);
        }
        loginUser = this.userService.getUser(loginUser.getUserId());
        req.setSessionMessage("修改个人信息成功");
        return "r:/user/set/set.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setNickName(HkRequest req, HkResponse resp) throws Exception {
        String nickName = req.getString("nickName", "");
        User user = this.getLoginUser(req);
        long userId = user.getUserId();
        if (User.validateNickName(nickName) != Err.SUCCESS) {
            req.setMessage("请正确输入昵称");
            return "/user/set/set_toUpdateNickName.do";
        }
        nickName = nickName.replaceAll("　", "");
        if (this.userService.updateNickName(userId, nickName)) {
            UserCard userCard = this.userCardService.getUserCard(userId);
            if (userCard != null) {
                userCard.setNickName(nickName);
                this.userCardService.updateUserCard(userCard);
            }
            user.setNickName(nickName);
            req.setSessionMessage("修改昵称成功");
            return "r:/user/set/set.do";
        }
        req.setMessage("昵称已经被占用,请重新输入");
        return "/user/set/set_toSetNickName.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetHead(HkRequest req, HkResponse resp) throws Exception {
        User user = this.userService.getUser(this.getLoginUser(req).getUserId());
        req.setAttribute("user", user);
        return "/WEB-INF/page/user/set/sethead.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setHead(HkRequest req, HkResponse resp) {
        File headFile = req.getFile("f");
        if (headFile == null) {
            return "r:/user/set/set.do";
        }
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateUser(userId);
        } catch (UserNotExistException e) {
            log.warn(e.getMessage());
            return "r:/user/set/set.do";
        }
        try {
            this.userService.updateHead(userId, headFile);
            req.setSessionMessage("修改头像成功");
            req.setSessionValue("newhead", System.currentTimeMillis());
            return "r:/user/set/set.do";
        } catch (ImageException e) {
            req.setMessage("图片处理错误,请重新上传");
            log.error(e.getMessage());
            return "/user/set/set_toSetHead.do";
        } catch (NotPermitImageFormatException e) {
            req.setMessage("图片只支持jpg,png,gif格式");
            return "/user/set/set_toSetHead.do";
        } catch (OutOfSizeException e) {
            req.setMessage("图片大小不能超过1M");
            return "/user/set/set_toSetHead.do";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetPwd(HkRequest req, HkResponse resp) throws Exception {
        SmsPortProcessAble smsPortProcessAble = (SmsPortProcessAble) HkUtil.getBean("updatePwd_smsPort");
        String number = this.smsClient.getSmsConfig().getSpNumber() + smsPortProcessAble.getBaseSmsPort();
        req.setAttribute("number", number);
        return "/WEB-INF/page/user/set/setpwd.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetpwdweb(HkRequest req, HkResponse resp) throws Exception {
        SmsPortProcessAble smsPortProcessAble = (SmsPortProcessAble) HkUtil.getBean("updatePwd_smsPort");
        String number = this.smsClient.getSmsConfig().getSpNumber() + smsPortProcessAble.getBaseSmsPort();
        req.setAttribute("number", number);
        req.setAttribute("op_func", 6);
        return this.getWeb3Jsp("user/set/pwd.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setPwd(HkRequest req, HkResponse resp) {
        String oldPwd = req.getString("oldPwd");
        String newPwd = req.getString("newPwd");
        if (DataUtil.isEmpty(oldPwd)) {
            return null;
        }
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateUser(userId);
        } catch (UserNotExistException e) {
            log.warn(e.getMessage());
            return "r:/user/set/set.do";
        }
        if (UserValidate.validatePassword(newPwd) != Err.SUCCESS) {
            req.setMessage("密码为4-16位的数字和字母");
            return "/user/set/set_toSetPwd.do";
        }
        if (!this.userService.updatePwd(userId, oldPwd, newPwd)) {
            req.setText(Err.USER_OLDPASSWORD_ERROR + "");
            return "/user/set/set_toSetPwd.do";
        }
        req.setSessionMessage("密码修改成功");
        return "r:/user/set/set.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setpwdweb(HkRequest req, HkResponse resp) {
        String oldPwd = req.getString("oldPwd");
        String newPwd = req.getString("newPwd");
        long userId = this.getLoginUser(req).getUserId();
        if (DataUtil.isEmpty(oldPwd)) {
            return this.initError(req, Err.PASSWORD_DATA_ERROR, -1, null, "pwd", "onpwderror", null);
        }
        int code = UserValidate.validatePassword(newPwd);
        if (code != Err.SUCCESS) {
            return this.initError(req, code, -1, null, "pwd", "onpwderror", null);
        }
        if (this.userService.updatePwd(userId, oldPwd, newPwd)) {
            req.setSessionMessage(req.getText("func.updatepwdok"));
            return this.initSuccess(req, "pwd", "onpwdsuccess", null);
        }
        return this.initError(req, Err.PASSWORD_OLD_ERROR, -1, null, "pwd", "onpwderror", null);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetMobile(HkRequest req, HkResponse resp) throws Exception {
        return "/WEB-INF/page/user/set/setmobile.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetMobile1(HkRequest req, HkResponse resp) throws Exception {
        long userId = this.getLoginUser(req).getUserId();
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        req.setAttribute("userOtherInfo", userOtherInfo);
        return "/WEB-INF/page/user/set/setmobile1.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetMobile2(HkRequest req, HkResponse resp) throws Exception {
        int refresh = req.getInt("refresh");
        long userId = this.getLoginUser(req).getUserId();
        Randnum o = null;
        if (refresh == 1) {
            o = this.userService.getUserRandnum(userId);
        } else {
            o = this.userService.createUserRandnum(userId);
        }
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        req.setAttribute("userOtherInfo", userOtherInfo);
        req.setAttribute("ran", o);
        return "/WEB-INF/page/user/set/setmobile2.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetmobile2web(HkRequest req, HkResponse resp) throws Exception {
        int refresh = req.getInt("refresh");
        long userId = this.getLoginUser(req).getUserId();
        Randnum o = null;
        if (refresh == 1) {
            o = this.userService.getUserRandnum(userId);
        } else {
            o = this.userService.createUserRandnum(userId);
        }
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        req.setAttribute("userOtherInfo", userOtherInfo);
        req.setAttribute("ran", o);
        req.setAttribute("op_func", 4);
        return this.getWeb3Jsp("user/set/setmobile2.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toChgMobile(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        Randnum o = this.userService.getUserRandnum(userId);
        if (o == null) {
            try {
                o = this.userService.createUserRandnum(userId);
            } catch (CreateRandnumException e) {
            }
        }
        req.setAttribute("ran", o);
        req.setAttribute("userOtherInfo", userOtherInfo);
        return "/WEB-INF/page/user/set/chgmobile.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tochgmobileweb(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        Randnum o = this.userService.getUserRandnum(userId);
        if (o == null) {
            try {
                o = this.userService.createUserRandnum(userId);
            } catch (CreateRandnumException e) {
            }
        }
        req.setAttribute("ran", o);
        req.setAttribute("userOtherInfo", userOtherInfo);
        req.setAttribute("op_func", 4);
        return this.getWeb3Jsp("user/set/chgmobile.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String reChgMobile(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        try {
            this.userService.createUserRandnum(userId);
        } catch (CreateRandnumException e) {
        }
        return "r:/user/set/set_toChgMobile.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String rechgmobileweb(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        try {
            Randnum randnum = this.userService.createUserRandnum(userId);
            resp.sendHtml(randnum.getRandvalue());
        } catch (CreateRandnumException e) {
        }
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String sendyzok(HkRequest req, HkResponse resp) throws Exception {
        long userId = this.getLoginUser(req).getUserId();
        UserOtherInfo userOtherInfo = this.userService.getUserOtherInfo(userId);
        req.setAttribute("userOtherInfo", userOtherInfo);
        return "/WEB-INF/page/user/set/sendyzok.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setMobile(HkRequest req, HkResponse resp) {
        String mobile = req.getString("mobile");
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateMobile(mobile);
        } catch (IllegalMobileException e) {
            req.setMessage("请输入正确的手机号码");
            return "/user/set/set_toSetMobile.do";
        }
        try {
            this.userService.updateMobile(userId, mobile);
            req.setSessionMessage("修改手机号码成功");
            return "r:/user/set/set.do";
        } catch (MobileDuplicateException e) {
            req.setMessage("手机号码已经存在,请更换一个唯一的手机号码");
            return "/user/set/set_toSetMobile.do";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetEmail(HkRequest req, HkResponse resp) throws Exception {
        UserOtherInfo info = this.userService.getUserOtherInfo(this.getLoginUser(req).getUserId());
        req.setAttribute("info", info);
        return "/WEB-INF/page/user/set/setemail.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetemailweb(HkRequest req, HkResponse resp) throws Exception {
        UserOtherInfo info = this.userService.getUserOtherInfo(this.getLoginUser(req).getUserId());
        req.setAttribute("info", info);
        req.setAttribute("op_func", 3);
        return this.getWeb3Jsp("user/set/email.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setEmail(HkRequest req, HkResponse resp) {
        String email = req.getString("email");
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateUser(userId);
        } catch (UserNotExistException e) {
            log.warn(e.getMessage());
            return "r:/user/set/set.do";
        }
        int code = UserOtherInfo.validateEmail(email);
        if (code != Err.SUCCESS) {
            req.setText(code + "");
            return "/user/set/set_toSetEmail.do";
        }
        try {
            UserOtherInfo info = this.userService.getUserOtherInfo(this.getLoginUser(req).getUserId());
            if (info.getEmail().equals(email) && info.isAuthedMail()) {
                return "r:/user/set/set.do";
            }
            this.userService.updateEmail(userId, email);
            UserMailAuth o = this.userMailAuthService.createUserMailAuth(info.getUserId());
            if (info.isAuthedMail()) {
                ScoreLog scoreLog = ScoreLog.create(userId, HkLog.CANCELVALIDATEEMAIL, 0, -ScoreConfig.getCancelValidateEmail());
                this.userService.addScore(scoreLog);
            }
            this.userService.updateValidateEmail(userId, UserOtherInfo.VALIDATEEMAIL_N);
            String title = req.getText("func.mail.validateemail.title");
            String content = req.getText("func.mail.validateemail.content", o.getAuthcode());
            this.mailUtil.sendHtmlMail(email, title, content);
            req.setSessionText("func.mail.sendauthcodemailok");
            return "r:/user/set/set.do";
        } catch (EmailDuplicateException e) {
            req.setText("func.mail.mailduplicate");
            return "/user/set/set_toSetEmail.do";
        } catch (MessagingException e) {
            req.setText("func.mail.sendfail");
            e.printStackTrace();
            return "/user/set/set_toSetEmail.do";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setemailweb(HkRequest req, HkResponse resp) {
        String email = req.getString("email");
        long userId = this.getLoginUser(req).getUserId();
        int code = UserOtherInfo.validateEmail(email);
        if (code != Err.SUCCESS) {
            return this.initError(req, code, -1, null, "email", "onemailerror", null);
        }
        try {
            UserOtherInfo info = this.userService.getUserOtherInfo(this.getLoginUser(req).getUserId());
            if (info.getEmail().equals(email) && info.isAuthedMail()) {
                return null;
            }
            this.userService.updateEmail(userId, email);
            UserMailAuth o = this.userMailAuthService.createUserMailAuth(info.getUserId());
            if (info.isAuthedMail()) {
                ScoreLog scoreLog = ScoreLog.create(userId, HkLog.CANCELVALIDATEEMAIL, 0, -ScoreConfig.getCancelValidateEmail());
                this.userService.addScore(scoreLog);
            }
            this.userService.updateValidateEmail(userId, UserOtherInfo.VALIDATEEMAIL_N);
            String title = req.getText("func.mail.validateemail.title");
            String content = req.getText("func.mail.validateemail.contentweb", o.getAuthcode());
            this.mailUtil.sendHtmlMail(email, title, content);
            req.setSessionText("func.mail.sendauthcodemailok");
            return this.initSuccess(req, "email", "onemailsuccess", null);
        } catch (EmailDuplicateException e) {
            return this.initError(req, Err.EMAIL_ALREADY_EXIST, -1, null, "email", "onemailerror", null);
        } catch (MessagingException e) {
            e.printStackTrace();
            return this.initError(req, Err.EMAIL_SEND_ERROR, -1, null, "email", "onemailerror", null);
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setEmail2(HkRequest req, HkResponse resp) {
        String email = req.getString("email");
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateUser(userId);
        } catch (UserNotExistException e) {
            log.warn(e.getMessage());
            return "r:/user/set/set.do";
        }
        try {
            UserValidate.validateEmail(email);
        } catch (IllegalEmailException e) {
            req.setMessage("请输入正确的email");
            return "/user/set/set_toSetEmail.do";
        }
        try {
            this.userService.updateEmail(userId, email);
            req.setSessionMessage("修改email成功");
            return "r:/user/set/set.do";
        } catch (EmailDuplicateException e) {
            req.setMessage("您输入的email已经存在,请重新输入");
            return "/user/set/set_toSetEmail.do";
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toSetProtect(HkRequest req, HkResponse resp) throws Exception {
        long userId = this.getLoginUser(req).getUserId();
        UserProtect userProtect = (UserProtect) req.getAttribute("userProtect");
        if (userProtect == null) {
            userProtect = this.userService.getUserProtect(userId);
        }
        List<ProtectBean> list = ProtectConfig.getProtectList();
        req.setAttribute("list", list);
        req.setAttribute("userProtect", userProtect);
        return "/WEB-INF/page/user/set/setprotect.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String tosetprotectweb(HkRequest req, HkResponse resp) throws Exception {
        long userId = this.getLoginUser(req).getUserId();
        UserProtect userProtect = (UserProtect) req.getAttribute("userProtect");
        if (userProtect == null) {
            userProtect = this.userService.getUserProtect(userId);
        }
        List<ProtectBean> list = ProtectConfig.getProtectList();
        req.setAttribute("list", list);
        req.setAttribute("userProtect", userProtect);
        req.setAttribute("op_func", 7);
        return this.getWeb3Jsp("user/set/protect.jsp");
    }

    /**
	 * 修改密码保护
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setProtect(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        try {
            UserValidate.validateUser(userId);
        } catch (UserNotExistException e) {
            log.warn(e.getMessage());
            return "r:/user/set/set.do";
        }
        String pvalue = req.getString("pvalue");
        int pconfig = req.getInt("pconfig");
        try {
            UserValidate.validateSetProtect(pvalue);
        } catch (ProtectValueErrorException e) {
            req.setMessage("请正确输入密码保护答案");
            UserProtect userProtect = new UserProtect();
            userProtect.setPconfig(pconfig);
            userProtect.setPvalue(pvalue);
            req.setAttribute("userProtect", userProtect);
            return "/user/set/set_toSetProtect.do";
        }
        this.userService.updateUserProtect(userId, pconfig, pvalue);
        req.setSessionMessage("修改密码保护成功");
        return "r:/user/set/set.do";
    }

    /**
	 * 修改密码保护
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setprotectweb(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        String pvalue = req.getString("pvalue");
        int pconfig = req.getInt("pconfig");
        int code = UserValidate.validateSetProtect2(pvalue);
        if (code != Err.SUCCESS) {
            return this.initError(req, code, -1, null, "pwd", "onproerror", null);
        }
        this.userService.updateUserProtect(userId, pconfig, pvalue);
        req.setSessionMessage(req.getText("func.updatepwdprotectok"));
        return this.initSuccess(req, "pwd", "onprosuccess", null);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String toSetNoticeInfo(HkRequest req, HkResponse resp) {
        if (req.getAttribute("info") == null) {
            long userId = this.getLoginUser(req).getUserId();
            UserNoticeInfo info = this.userService.getUserNoticeInfo(userId);
            if (info == null) {
                info = new UserNoticeInfo();
                info.setUserId(userId);
                info.setLabaReplyNotice(UserNoticeInfo.NOTICE_Y);
                info.setMsgNotice(UserNoticeInfo.NOTICE_Y);
                info.setFollowNotice(UserNoticeInfo.NOTICE_Y);
                info.setFollowSysNotice(UserNoticeInfo.NOTICE_Y);
                info.setLabaReplySysNotice(UserNoticeInfo.NOTICE_Y);
            }
            req.setAttribute("info", info);
        }
        return "/WEB-INF/page/user/set/setnoticeinfo.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetnoticeinfoweb(HkRequest req, HkResponse resp) {
        if (req.getAttribute("info") == null) {
            long userId = this.getLoginUser(req).getUserId();
            UserNoticeInfo info = this.userService.getUserNoticeInfo(userId);
            if (info == null) {
                info = new UserNoticeInfo();
                info.setUserId(userId);
                info.setLabaReplyNotice(UserNoticeInfo.NOTICE_Y);
                info.setMsgNotice(UserNoticeInfo.NOTICE_Y);
                info.setFollowNotice(UserNoticeInfo.NOTICE_Y);
                info.setFollowSysNotice(UserNoticeInfo.NOTICE_Y);
                info.setLabaReplySysNotice(UserNoticeInfo.NOTICE_Y);
            }
            req.setAttribute("info", info);
        }
        req.setAttribute("op_func", 8);
        return this.getWeb3Jsp("user/set/noticeinfo.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String psn(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        UserNoticeInfo userNoticeInfo = this.userService.getUserNoticeInfo(userId);
        if (userNoticeInfo == null) {
            userNoticeInfo = new UserNoticeInfo();
            userNoticeInfo.setUserId(userId);
            userNoticeInfo.setLabaReplyNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setMsgNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setFollowNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setFollowSysNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setLabaReplySysNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setLabaReplyIMNotice(UserNoticeInfo.NOTICE_Y);
            userNoticeInfo.setFollowIMNotice(UserNoticeInfo.NOTICE_Y);
        }
        byte noticeType = req.getByte("noticeType");
        if (noticeType == Notice.NOTICETYPE_LABAREPLY) {
            if (userNoticeInfo.getLabaReplySysNotice() == UserNoticeInfo.NOTICE_N) {
                userNoticeInfo.setLabaReplySysNotice(UserNoticeInfo.NOTICE_Y);
            } else {
                userNoticeInfo.setLabaReplySysNotice(UserNoticeInfo.NOTICE_N);
            }
            this.userService.updateUserNoticeInfo(userNoticeInfo);
        } else if (noticeType == Notice.NOTICETYPE_FOLLOW) {
            if (userNoticeInfo.getFollowSysNotice() == UserNoticeInfo.NOTICE_N) {
                userNoticeInfo.setFollowSysNotice(UserNoticeInfo.NOTICE_Y);
            } else {
                userNoticeInfo.setFollowSysNotice(UserNoticeInfo.NOTICE_N);
            }
        } else if (noticeType == Notice.NOTICETYPE_USER_IN_LABA) {
            if (userNoticeInfo.isNoticeUserInLaba()) {
                userNoticeInfo.setUserInLabaSysNotice(UserNoticeInfo.NOTICE_N);
            } else {
                userNoticeInfo.setUserInLabaSysNotice(UserNoticeInfo.NOTICE_Y);
            }
        }
        this.userService.updateUserNoticeInfo(userNoticeInfo);
        return "r:/notice/notice.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setNoticeInfo(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        UserNoticeInfo info = this.userService.getUserNoticeInfo(userId);
        byte labaReplyNotice = req.getByte("labaReplyNotice", (byte) 1);
        byte msgNotice = req.getByte("msgNotice", (byte) 1);
        byte followNotice = req.getByte("followNotice", (byte) 1);
        byte followIMNotice = req.getByte("followIMNotice", (byte) 1);
        byte followSysNotice = req.getByte("followSysNotice", (byte) 1);
        byte labaReplySysNotice = req.getByte("labaReplySysNotice", (byte) 1);
        byte labaReplyIMNotice = req.getByte("labaReplyIMNotice", (byte) 1);
        if (info == null) {
            info = new UserNoticeInfo();
        }
        info.setUserId(userId);
        info.setFollowIMNotice(followIMNotice);
        info.setLabaReplyIMNotice(labaReplyIMNotice);
        info.setLabaReplyNotice(labaReplyNotice);
        info.setMsgNotice(msgNotice);
        info.setFollowNotice(followNotice);
        info.setFollowSysNotice(followSysNotice);
        info.setLabaReplySysNotice(labaReplySysNotice);
        this.userService.updateUserNoticeInfo(info);
        req.setSessionMessage("保存消息设置成功");
        return "r:/user/set/set.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setnoticeinfoweb(HkRequest req, HkResponse resp) {
        long userId = this.getLoginUser(req).getUserId();
        UserNoticeInfo info = this.userService.getUserNoticeInfo(userId);
        byte labaReplyNotice = req.getByte("labaReplyNotice", (byte) 1);
        byte msgNotice = req.getByte("msgNotice", (byte) 1);
        byte followNotice = req.getByte("followNotice", (byte) 1);
        byte followIMNotice = req.getByte("followIMNotice", (byte) 1);
        byte followSysNotice = req.getByte("followSysNotice", (byte) 1);
        byte labaReplySysNotice = req.getByte("labaReplySysNotice", (byte) 1);
        byte labaReplyIMNotice = req.getByte("labaReplyIMNotice", (byte) 1);
        if (info == null) {
            info = new UserNoticeInfo();
        }
        info.setUserId(userId);
        info.setFollowIMNotice(followIMNotice);
        info.setLabaReplyIMNotice(labaReplyIMNotice);
        info.setLabaReplyNotice(labaReplyNotice);
        info.setMsgNotice(msgNotice);
        info.setFollowNotice(followNotice);
        info.setFollowSysNotice(followSysNotice);
        info.setLabaReplySysNotice(labaReplySysNotice);
        this.userService.updateUserNoticeInfo(info);
        req.setSessionMessage("保存消息设置成功");
        return this.initSuccess(req, "notice", "onnoticesuccess", null);
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String toAbolishUser(HkRequest req, HkResponse resp) {
        return "/WEB-INF/page/user/set/abolishuser.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String abolishUser(HkRequest req, HkResponse resp) {
        String input = req.getString("input");
        String pwd = req.getString("pwd");
        if (isEmpty(input) || isEmpty(pwd)) {
            req.setSessionMessage("请输入用户和密码");
            return "r:/user/set/set_toAbolishUser.do";
        }
        log.info("abolishUser ownnerId[ " + this.getLoginUser(req).getUserId() + " ] input [ " + input + " ]");
        User user = this.userService.abolishUser(this.getLoginUser(req).getUserId(), input, pwd);
        if (user != null) {
            req.setSessionMessage("操作成功");
        } else {
            req.setSessionMessage("您输入的用户或密码错误");
        }
        return "r:/user/set/set_toAbolishUser.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetclient(HkRequest req, HkResponse resp) {
        HkStatus hkStatus = HkWebUtil.getHkStatus(req);
        List<CssColor> csslist = this.cssColorUtil.getCssColorList();
        List<ShowMode> showlist = this.showModeUtil.getModeList();
        List<UrlMode> urlModelist = this.urlModeUtil.getUrlModeList();
        req.setAttribute("urlModelist", urlModelist);
        req.setAttribute("csslist", csslist);
        req.setAttribute("showlist", showlist);
        req.setAttribute("hkStatus", hkStatus);
        return "/WEB-INF/page/user/set/setclient.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setclient(HkRequest req, HkResponse resp) {
        HkStatus hkStatus = HkWebUtil.getHkStatus(req);
        resp.setHeader("Pragma", "No-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setHeader("X-Wap-Proxy-Cookie", "none");
        int cssColorId = req.getInt("cssColorId");
        int showModeId = req.getInt("showModeId");
        int urlModeId = req.getInt("urlModeId");
        hkStatus.setCssColorId(cssColorId);
        hkStatus.setShowModeId(showModeId);
        hkStatus.setUrlModeId(urlModeId);
        HkWebUtil.setHkCookie(req, resp, hkStatus);
        return "r:/square.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetlabartflg(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        UserTool userTool = this.userService.checkUserTool(loginUser.getUserId());
        req.setAttribute("userTool", userTool);
        return "/WEB-INF/page/user/set/setlabartflg.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetlabartflgweb(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        UserTool userTool = this.userService.checkUserTool(loginUser.getUserId());
        req.setAttribute("userTool", userTool);
        req.setAttribute("op_func", 9);
        return this.getWeb3Jsp("user/set/labartflg.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setheadweb(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        int x1 = req.getInt("x1");
        int x2 = req.getInt("x2");
        int y1 = req.getInt("y1");
        int y2 = req.getInt("y2");
        String picurl = req.getString("picurl");
        String name = ImageConfig.getFileInServerPath(picurl);
        File f = new File(name);
        if (!f.exists()) {
            return null;
        }
        try {
            this.userService.updateHeadWithCut(loginUser.getUserId(), f, x1, y1, x2, y2);
            this.setOpFuncSuccessMsg(req);
            return this.initSuccess(req, "op");
        } catch (Exception e) {
            return this.initError(req, Err.IMG_UPLOAD_ERROR, "op");
        } finally {
            f.delete();
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetheadweb(HkRequest req, HkResponse resp) {
        req.setAttribute("op_func", 1);
        return this.getWeb3Jsp("user/set/uploadhead.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String tosetheadweb2(HkRequest req, HkResponse resp) {
        req.reSetAttribute("picurl");
        req.reSetAttribute("width");
        req.reSetAttribute("height");
        req.setAttribute("op_func", 1);
        return this.getWeb3Jsp("user/set/uploadhead2.jsp");
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String uploadheadpic(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        File f = req.getFile("f");
        try {
            JMagickUtil util = new JMagickUtil(f, 1);
            String filePath = ImageConfig.getTempUploadFilePath();
            String fileName = loginUser.getUserId() + req.getRequestedSessionId() + DataUtil.getRandom(5);
            util.makeImage(filePath, fileName + ".jpg", JMagickUtil.IMG_OBLONG, 500);
            String name = filePath + fileName + ".jpg";
            File file = new File(name);
            String picurl = ImageConfig.getTempUploadPic(fileName) + ".jpg";
            util = new JMagickUtil(file, 1);
            int width = util.getWidth();
            int height = util.getHeight();
            return this.onSuccess(req, picurl + ";" + width + ";" + height, "uploadpic");
        } catch (ImageException e) {
            return this.initError(req, Err.IMG_UPLOAD_ERROR, "uploadpic");
        } catch (NotPermitImageFormatException e) {
            return this.initError(req, Err.IMG_FMT_ERROR, "uploadpic");
        } catch (OutOfSizeException e) {
            return this.initError(req, Err.IMG_OUTOFSIZE_ERROR, "uploadpic");
        }
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setlabartflg(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        byte labartflg = req.getByte("labartflg");
        UserTool userTool = this.userService.getUserTool(loginUser.getUserId());
        if (userTool != null) {
            userTool.setLabartflg(labartflg);
            int code = userTool.validate();
            if (code == Err.SUCCESS) {
                this.userService.updateuserTool(userTool);
                req.removeSessionvalue("userTool");
                req.setSessionText("op.exeok");
            }
        }
        return "r:/user/set/set.do";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 */
    public String setlabartflgweb(HkRequest req, HkResponse resp) {
        User loginUser = this.getLoginUser(req);
        byte labartflg = req.getByte("labartflg");
        UserTool userTool = this.userService.getUserTool(loginUser.getUserId());
        if (userTool != null) {
            userTool.setLabartflg(labartflg);
            int code = userTool.validate();
            if (code == Err.SUCCESS) {
                this.userService.updateuserTool(userTool);
                req.removeSessionvalue("userTool");
                this.setOpFuncSuccessMsg(req);
            }
        }
        return this.initSuccess(req, "labart", "onlabartsuccess", null);
    }
}
