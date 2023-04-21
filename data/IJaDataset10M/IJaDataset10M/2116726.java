package com.hk.web.admin.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.bean.AdminHkb;
import com.hk.bean.DefFollowUser;
import com.hk.bean.User;
import com.hk.bean.UserCmpFunc;
import com.hk.bean.UserOtherInfo;
import com.hk.bean.UserTool;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.UserCmpFuncService;
import com.hk.svr.UserService;
import com.hk.svr.pub.Err;
import com.hk.web.pub.action.BaseAction;

@Component("/admin/admin")
public class AdminAction extends BaseAction {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCmpFuncService userCmpFuncService;

    private int size = 20;

    public String execute(HkRequest req, HkResponse resp) throws Exception {
        return null;
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toaddgd(HkRequest req, HkResponse resp) throws Exception {
        long userId = req.getLong("userId");
        User user = this.userService.getUser(userId);
        UserTool userTool = this.userService.getUserTool(user.getUserId());
        if (userTool == null) {
            userTool = UserTool.createDefault(user.getUserId());
        }
        userTool.setUser(user);
        req.setAttribute("userTool", userTool);
        return "/WEB-INF/page/admin/addgd.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String addgd(HkRequest req, HkResponse resp) throws Exception {
        long userId = req.getLong("userId");
        int count = req.getInt("count");
        if (count != 0) {
            if (req.getString("lessen") != null) {
                count = -count;
            }
            User user = this.userService.getUser(userId);
            UserTool userTool = this.userService.checkUserTool(user.getUserId());
            userTool.setGroundCount(userTool.getGroundCount() + count);
            this.userService.updateuserTool(userTool);
            String s = "修改地皮成功，" + user.getNickName() + "当前的地皮数量为" + userTool.getGroundCount();
            req.setSessionMessage(s);
        }
        return "r:/admin/admin_gdlist.do";
    }

    /**
	 * 地皮记录
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String gdlist(HkRequest req, HkResponse resp) throws Exception {
        String nickName = req.getString("nickName");
        List<UserTool> list = null;
        SimplePage page = req.getSimplePage(size);
        if (DataUtil.isEmpty(nickName)) {
            list = this.userService.getUserTooList(page.getBegin(), size);
            page.setListSize(list.size());
            List<Long> idList = new ArrayList<Long>();
            for (UserTool ut : list) {
                idList.add(ut.getUserId());
            }
            Map<Long, User> map = this.userService.getUserMapInId(idList);
            for (UserTool ut : list) {
                ut.setUser(map.get(ut.getUserId()));
            }
        } else {
            User user = this.userService.getUserByNickName(nickName);
            if (user != null) {
                UserTool userTool = this.userService.getUserTool(user.getUserId());
                if (userTool == null) {
                    userTool = UserTool.createDefault(user.getUserId());
                }
                userTool.setUser(user);
                list = new ArrayList<UserTool>();
                list.add(userTool);
            }
        }
        req.setAttribute("list", list);
        return "/WEB-INF/page/admin/gdlist.jsp";
    }

    /**
	 * 酷币充值记录
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String hkblog(HkRequest req, HkResponse resp) throws Exception {
        String nickName = req.getString("nickName");
        long userId = 0;
        if (!DataUtil.isEmpty(nickName)) {
            User user = this.userService.getUserByNickName(nickName);
            if (user != null) {
                userId = user.getUserId();
            }
        }
        SimplePage page = req.getSimplePage(size);
        List<AdminHkb> list = this.userService.getAdminHkbList(userId, page.getBegin(), size);
        page.setListSize(list.size());
        List<AdminHkbVo> adminhkbvolist = AdminHkbVo.createVoList(list);
        req.setAttribute("adminhkbvolist", adminhkbvolist);
        req.setEncodeAttribute("nickName", nickName);
        return "/WEB-INF/page/admin/hkblog.jsp";
    }

    /**
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String toaddHkb(HkRequest req, HkResponse resp) throws Exception {
        String nickName = req.getString("nickName");
        User user = (User) req.getAttribute("user");
        if (user == null) {
            if (!DataUtil.isEmpty(nickName)) {
                user = this.userService.getUserByNickName(nickName);
            }
        }
        if (user == null) {
            long userId = req.getLong("userId");
            if (userId > 0) {
                user = this.userService.getUser(userId);
            }
        }
        if (user != null) {
            UserOtherInfo info = this.userService.getUserOtherInfo(user.getUserId());
            req.setAttribute("info", info);
        }
        req.setAttribute("user", user);
        req.reSetAttribute("f");
        return "/WEB-INF/page/admin/addhkb.jsp";
    }

    /**
	 * 进行充值
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String addHkb(HkRequest req, HkResponse resp) throws Exception {
        long userId = req.getLong("userId");
        byte addflg = req.getByte("addflg");
        int addCount = req.getInt("addCount");
        int money = req.getInt("money");
        String content = req.getString("content");
        AdminHkb o = new AdminHkb();
        o.setUserId(userId);
        o.setAddflg(addflg);
        o.setAddCount(addCount);
        o.setContent(DataUtil.toHtmlRow(content));
        if (addflg == AdminHkb.ADDFLG_MONEYBUY) {
            o.setMoney(money);
        }
        o.setOpuserId(this.getLoginUser(req).getUserId());
        User user = this.userService.getUser(userId);
        req.setAttribute("user", user);
        req.setAttribute("o", o);
        int code = o.validate();
        if (code != Err.SUCCESS) {
            req.setMessage(req.getText(code + ""));
            return "/admin/admin_toaddHkb.do";
        }
        this.userService.createAdminHkb(o);
        req.setSessionMessage(req.getText("op.exeok"));
        return "r:/admin/admin_toaddHkb.do?f=1&userId=" + userId;
    }

    /**
	 * 查看默认关注
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String deffollowuser(HkRequest req, HkResponse resp) throws Exception {
        SimplePage page = req.getSimplePage(size);
        List<DefFollowUser> list = this.userService.getDefFollowUserList(page.getBegin(), size);
        page.setListSize(list.size());
        req.setAttribute("list", list);
        return "/WEB-INF/page/admin/deffollowuser.jsp";
    }

    /**
	 * 添加默认关注
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String adddeffollowuser(HkRequest req, HkResponse resp) throws Exception {
        String nickName = req.getString("nickName");
        User user = this.userService.getUserByNickName(nickName);
        if (user != null) {
            this.userService.createDefFollowUser(user.getUserId());
            req.setSessionMessage(req.getText("op.exeok"));
        }
        return "r:/admin/admin_deffollowuser.do";
    }

    /**
	 * 删除默认关注
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String deldeffollowuser(HkRequest req, HkResponse resp) throws Exception {
        long userId = req.getLong("userId");
        this.userService.delteDefFollowUser(userId);
        req.setSessionMessage(req.getText("op.exeok"));
        return "r:/admin/admin_deffollowuser.do";
    }

    /**
	 * 查找用户(为添加企业功能)
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String seluserforcmpfunc(HkRequest req, HkResponse resp) {
        int ch = req.getIntAndSetAttr("ch");
        if (ch == 0) {
            return this.getWapJsp("admin/seluserforcmpfunc.jsp");
        }
        String nickName = req.getString("nickName");
        if (DataUtil.isEmpty(nickName)) {
            return "r:/admin/admin_seluserforcmpfunc.do";
        }
        User user = this.userService.getUserByNickName(nickName);
        if (user != null) {
            UserCmpFunc userCmpFunc = this.userCmpFuncService.getUserCmpFunc(user.getUserId());
            req.setAttribute("user", user);
            req.setAttribute("userCmpFunc", userCmpFunc);
        }
        req.setEncodeAttribute("nickName", nickName);
        return this.getWapJsp("admin/seluserforcmpfunc.jsp");
    }

    /**
	 * 查找用户(为添加企业功能)
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
    public String setcmpfunc(HkRequest req, HkResponse resp) {
        long userId = req.getLongAndSetAttr("userId");
        User user = this.userService.getUser(userId);
        req.setAttribute("user", user);
        byte boxflg = req.getByte("boxflg");
        byte couponflg = req.getByte("couponflg");
        byte adflg = req.getByte("adflg");
        UserCmpFunc userCmpFunc = this.userCmpFuncService.getUserCmpFunc(userId);
        if (userCmpFunc == null) {
            userCmpFunc = new UserCmpFunc();
            userCmpFunc.setUserId(userId);
        }
        userCmpFunc.setBoxflg(boxflg);
        userCmpFunc.setCouponflg(couponflg);
        userCmpFunc.setAdflg(adflg);
        this.userCmpFuncService.saveUserCmpFunc(userCmpFunc);
        this.setOpFuncSuccessMsg(req);
        return "r:/admin/admin_seluserforcmpfunc.do?ch=1&nickName=" + DataUtil.urlEncoder(user.getNickName());
    }
}
