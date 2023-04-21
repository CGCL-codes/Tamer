package com.etbhk.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import weibo4j.http.RequestToken;
import com.hk.bean.taobao.Tb_User;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.HkUtil;
import com.hk.frame.util.page.SimplePage;
import com.hk.frame.web.action.Action;
import com.hk.frame.web.http.HkRequest;
import com.hk.frame.web.http.HkResponse;
import com.hk.svr.Tb_AdminService;
import com.hk.svr.pub.Err;

public abstract class BaseTaoBaoAction implements Action {

    /**
	 * 是否是跳转页面。true:是，false:是提交数据
	 * 
	 * @param req
	 * @return
	 *         2010-8-29
	 */
    protected boolean isForwardPage(HkRequest req) {
        if (req.getInt("ch") == 0) {
            return true;
        }
        return false;
    }

    protected Tb_User getLoginTb_User(HkRequest req) {
        return (Tb_User) req.getAttribute(TbHkWebUtil.LOGIN_USER_ATTRKEY);
    }

    protected String getWebJsp(String refpath) {
        return "/WEB-INF/webtb/" + refpath;
    }

    protected String getAdminJsp(String refpath) {
        return "/WEB-INF/webtbadmin/" + refpath;
    }

    protected String onSuccess(HkRequest req, String functionName, Object respValue) {
        return this.onError(req, Err.SUCCESS, functionName, respValue);
    }

    protected String onError(HkRequest req, int code, String functionName, Object respValue) {
        req.setAttribute("error", code);
        req.setAttribute("error_msg", req.getText(String.valueOf(code)));
        req.setAttribute("functionName", functionName);
        if (respValue != null) {
            req.setAttribute("respValue", respValue);
        }
        return this.getWebJsp("inc/onerror.jsp");
    }

    protected String onError(HkRequest req, int code, Object[] args, String functionName, Object respValue) {
        req.setAttribute("error", code);
        req.setAttribute("error_msg", req.getText(String.valueOf(code), args));
        req.setAttribute("functionName", functionName);
        if (respValue != null) {
            req.setAttribute("respValue", respValue);
        }
        return this.getWebJsp("inc/onerror.jsp");
    }

    protected String onErrorList(HkRequest req, Collection<Integer> list, String functionName) {
        Map<String, String> map = new HashMap<String, String>();
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            map.put(String.valueOf("error_" + i), req.getText(String.valueOf(i)));
            sb.append(i).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        String json = DataUtil.toJson(map);
        req.setAttribute("json", json);
        req.setAttribute("errorlist", sb.toString());
        req.setAttribute("functionName", functionName);
        return this.getWebJsp("inc/onerrorlist.jsp");
    }

    protected void setSuccessMsg(HkRequest req) {
        req.setSessionText("op.setting.op.success");
    }

    protected void setDelSuccessMsg(HkRequest req) {
        req.setSessionMessage("信息删除成功");
    }

    protected void setOAuth_sina_Cookie(HkRequest req, HkResponse resp, RequestToken requestToken) {
        Cookie cookie_sina_requestToken = new Cookie("sina_requestToken", requestToken.getToken());
        cookie_sina_requestToken.setPath("/");
        cookie_sina_requestToken.setDomain(req.getServerName());
        cookie_sina_requestToken.setMaxAge(TbHkWebUtil.COOKIE_MAXAGE);
        resp.addCookie(cookie_sina_requestToken);
        Cookie cookie_sina_requestTokenSecret = new Cookie("sina_requestTokenSecret", requestToken.getTokenSecret());
        cookie_sina_requestTokenSecret.setPath("/");
        cookie_sina_requestTokenSecret.setDomain(req.getServerName());
        cookie_sina_requestTokenSecret.setMaxAge(TbHkWebUtil.COOKIE_MAXAGE);
        resp.addCookie(cookie_sina_requestTokenSecret);
    }

    protected void clearOauth_sina_cookie(HkRequest req, HkResponse resp) {
        Cookie cookie_sina_requestToken = new Cookie("sina_requestToken", "");
        cookie_sina_requestToken.setPath("/");
        cookie_sina_requestToken.setDomain(req.getServerName());
        cookie_sina_requestToken.setMaxAge(0);
        resp.addCookie(cookie_sina_requestToken);
        Cookie cookie_sina_requestTokenSecret = new Cookie("sina_requestTokenSecret", "");
        cookie_sina_requestTokenSecret.setPath("/");
        cookie_sina_requestTokenSecret.setDomain(req.getServerName());
        cookie_sina_requestTokenSecret.setMaxAge(0);
        resp.addCookie(cookie_sina_requestTokenSecret);
    }

    protected String getOauth_sina_requestToken(HkRequest req) {
        Cookie cookie_sina_requestToken = req.getCookie("sina_requestToken");
        if (cookie_sina_requestToken != null) {
            return cookie_sina_requestToken.getValue();
        }
        return "";
    }

    protected String getOauth_sina_requestTokenSecret(HkRequest req) {
        Cookie cookie_sina_requestTokenSecret = req.getCookie("sina_requestTokenSecret");
        if (cookie_sina_requestTokenSecret != null) {
            return cookie_sina_requestTokenSecret.getValue();
        }
        return "";
    }

    protected void processListForPage(SimplePage page, List<?> list) {
        if (list.size() == page.getSize() + 1) {
            page.setHasNext(true);
            list.remove(page.getSize());
        }
    }

    /**
	 * 是否是网站系统管理员
	 * 
	 * @param req
	 * @return
	 *         2010-9-21
	 */
    protected boolean isUserSysAdmin(HkRequest req) {
        Boolean user_sysadmin = (Boolean) req.getAttribute("user_sysadmin");
        if (user_sysadmin == null) {
            Tb_User tbUser = this.getLoginTb_User(req);
            if (tbUser == null) {
                user_sysadmin = false;
            } else {
                Tb_AdminService tbAdminService = (Tb_AdminService) HkUtil.getBean("tb_AdminService");
                if (tbAdminService.getTb_AdminByUserid(tbUser.getUserid()) != null) {
                    user_sysadmin = true;
                } else {
                    user_sysadmin = false;
                }
            }
            req.setAttribute("user_sysadmin", user_sysadmin);
        }
        return user_sysadmin;
    }
}
